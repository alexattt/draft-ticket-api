package com.alexattt.ticketpriceapi;

import com.alexattt.ticketpriceapi.models.*;
import com.alexattt.ticketpriceapi.services.PriceService;
import com.alexattt.ticketpriceapi.services.TaxRateService;
import com.alexattt.ticketpriceapi.services.TicketBasePriceService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TicketPriceApiApplicationTests {

    private PriceService priceService;

    private static class TestTicketBasePriceService extends TicketBasePriceService {
        private final Map<String, BigDecimal> prices = new HashMap<>();

        public TestTicketBasePriceService() {
            prices.put("Riga-Vilnius", new BigDecimal("50.00"));
            prices.put("Vilnius-Riga", new BigDecimal("35.00"));
            prices.put("Riga-Tallinn", new BigDecimal("100.00"));
            prices.put("Tallinn-Riga", new BigDecimal("100.00"));
        }

        @Override
        public BigDecimal getBasePrice(String from, String to) {
            return prices.getOrDefault(from + "-" + to, new BigDecimal("50.00"));
        }
    }

    private static class TestTaxRateService extends TaxRateService {
        @Override
        public List<Double> getTaxRates(String dateTime) {
            if (dateTime.equals("2025-06-15T16:30:00.000Z")) {
                return Arrays.asList(10.0, 5.0); // 15% total for weekend
            }
            return Arrays.asList(7.0, 2.0); // 9% total for regular days
        }
    }

    @Before
    public void setUp() throws Exception {


        TestTicketBasePriceService ticketBasePriceService = new TestTicketBasePriceService();
        TestTaxRateService taxRateService = new TestTaxRateService();

        priceService = new PriceService();

        // Inject services using reflection
        Field ticketBasePriceField = PriceService.class.getDeclaredField("ticketBasePriceService");
        ticketBasePriceField.setAccessible(true);
        ticketBasePriceField.set(priceService, ticketBasePriceService);

        Field taxRateField = PriceService.class.getDeclaredField("taxRateService");
        taxRateField.setAccessible(true);
        taxRateField.set(priceService, taxRateService);
    }

    @Test
    public void testCalculateDraftPrice_AdultAndChildPassengers() {
        Passenger adultWithLuggage = new Passenger("Traveler", 35,
                List.of(
                        new LuggageItem(25.0)
                ));
        Passenger childWithLuggage = new Passenger("Traveler child", 9,
                List.of(
                        new LuggageItem(10.0)
                ));
        PricingRequest request = new PricingRequest(
                List.of(adultWithLuggage, childWithLuggage),
                "Vilnius",
                "Riga",
                "2025-06-15T16:30:00.000Z"
        );

        PricingResponse response = priceService.calculateDraftPrice(request);

        assertNotNull(response);
        assertEquals(2, response.getPassengerPricings().size());

        // Adult passenger: 35 ticket + 10.50 luggage = 45.50
        PassengerPricing adultPricing = response.getPassengerPricings().get(0);
        assertEquals("Traveler", adultPricing.getPassengerName());
        assertEquals(new BigDecimal("35.00"), adultPricing.getTicketPrice());
        assertEquals(new BigDecimal("10.50"), adultPricing.getLuggagePrice());
        assertEquals(new BigDecimal("45.50"), adultPricing.getPassengerTotal());
        assertFalse(adultPricing.isChild());

        // Child passenger: 17.50 ticket (50% discount) + 10.50 luggage = 28
        PassengerPricing childPricing = response.getPassengerPricings().get(1);
        assertEquals("Traveler child", childPricing.getPassengerName());
        assertEquals(new BigDecimal("17.50"), childPricing.getTicketPrice());
        assertEquals(new BigDecimal("10.50"), childPricing.getLuggagePrice());
        assertEquals(new BigDecimal("28.00"), childPricing.getPassengerTotal());
        assertTrue(childPricing.isChild());

        // Total: 73.50 + 11.03 tax (9%) = 84.53
        assertEquals(new BigDecimal("73.50"), response.getSubtotal());
        assertEquals(new BigDecimal("11.03"), response.getTotalTax());
        assertEquals(new BigDecimal("84.53"), response.getTotalPrice());
    }

    @Test
    public void testCalculateDraftPrice_PassengerWithoutLuggage() {
        Passenger passengerNoLuggage = new Passenger("Bob Smith", 25, Collections.emptyList());
        PricingRequest requestNoLuggage = new PricingRequest(
                Collections.singletonList(passengerNoLuggage),
                "Vilnius",
                "Riga",
                "2025-06-15T16:30:00.000Z"
        );

        PricingResponse response = priceService.calculateDraftPrice(requestNoLuggage);

        PassengerPricing pricing = response.getPassengerPricings().get(0);
        assertEquals(new BigDecimal("35.00"), pricing.getTicketPrice());
        assertEquals(new BigDecimal("0.00"), pricing.getLuggagePrice());
        assertEquals(new BigDecimal("35.00"), pricing.getPassengerTotal());
    }

    @Test
    public void testCalculateDraftPrice_ChildDiscount() {
        Passenger child = new Passenger("Child Test", 10, Collections.emptyList());
        PricingRequest childRequest = new PricingRequest(
                Collections.singletonList(child),
                "Riga",
                "Tallinn",
                "2025-06-17T16:30:00.000Z"
        );

        PricingResponse response = priceService.calculateDraftPrice(childRequest);

        PassengerPricing pricing = response.getPassengerPricings().get(0);
        assertEquals(new BigDecimal("50.00"), pricing.getTicketPrice()); // 50% of 100 base price
        assertTrue(pricing.isChild());
    }

    @Test
    public void testCalculateDraftPrice_LuggagePricing() {
        Passenger passengerWithMultipleLuggage = new Passenger("Traveler", 35,
                Arrays.asList(
                        new LuggageItem(25.0),
                        new LuggageItem(10.0)
                ));
        PricingRequest luggageRequest = new PricingRequest(
                Collections.singletonList(passengerWithMultipleLuggage),
                "Tallinn",
                "Riga",
                "2025-06-15T16:30:00.000Z"
        );

        PricingResponse response = priceService.calculateDraftPrice(luggageRequest);

        PassengerPricing pricing = response.getPassengerPricings().get(0);
        // Each luggage item costs 30% of base price = $30
        assertEquals(new BigDecimal("60.00"), pricing.getLuggagePrice()); // 2 items * $30
        assertEquals(2, pricing.getLuggageDetails().size());
    }

    @Test
    public void testCalculateDraftPrice_TaxCalculation() {
        PricingRequest holidayRequest = new PricingRequest(
                Collections.singletonList(new Passenger("Holiday Traveler", 30, Collections.emptyList())),
                "Riga",
                "Tallinn",
                "2025-06-15T16:30:00.000Z"
        );

        PricingResponse response = priceService.calculateDraftPrice(holidayRequest);

        // $100 subtotal * 15% tax = $15
        assertEquals(new BigDecimal("15.00"), response.getTotalTax());
        assertEquals(new BigDecimal("115.00"), response.getTotalPrice());
    }

    @Test
    public void testIsChildLogic() {
        Passenger child17 = new Passenger("Teen", 17, Collections.emptyList());
        Passenger adult18 = new Passenger("Young Adult", 18, Collections.emptyList());

        assertTrue(child17.isChild());
        assertFalse(adult18.isChild());
    }
}