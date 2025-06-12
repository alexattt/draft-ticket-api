package com.alexattt.ticketpriceapi.services;

import com.alexattt.ticketpriceapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriceService {
    private static final BigDecimal CHILD_DISCOUNT = new BigDecimal("0.50");
    private static final BigDecimal LUGGAGE_RATE = new BigDecimal("0.30");

    @Autowired
    private TicketBasePriceService ticketBasePriceService;

    @Autowired
    private TaxRateService taxRateService;

    public PricingResponse calculateDraftPrice(PricingRequest request) {
        BigDecimal basePrice = ticketBasePriceService.getBasePrice(request.getFromTerminal(), request.getToTerminal());
        List<Double> taxRates = taxRateService.getTaxRates(request.getPurchaseDate());

        List<PassengerPricing> passengerPricings = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Passenger passenger : request.getPassengers()) {
            PassengerPricing passengerPricing = calculatePassengerPricing(passenger, basePrice);
            passengerPricings.add(passengerPricing);
            subtotal = subtotal.add(passengerPricing.getPassengerTotal());
        }

        BigDecimal totalTax = calculateTotalTax(subtotal, taxRates);
        BigDecimal totalPrice = subtotal.add(totalTax);

        return new PricingResponse(totalPrice, passengerPricings, totalTax, subtotal);
    }

    private PassengerPricing calculatePassengerPricing(Passenger passenger, BigDecimal basePrice) {
        BigDecimal ticketPrice = passenger.isChild() ?
                basePrice.multiply(BigDecimal.ONE.subtract(CHILD_DISCOUNT)) : basePrice;

        List<LuggageItemPricing> luggageDetails = new ArrayList<>();
        BigDecimal totalLuggagePrice = BigDecimal.ZERO;

        if (passenger.getLuggage() != null) {
            for (LuggageItem luggage : passenger.getLuggage()) {
                BigDecimal luggagePrice = basePrice.multiply(LUGGAGE_RATE);
                luggageDetails.add(new LuggageItemPricing(luggagePrice));
                totalLuggagePrice = totalLuggagePrice.add(luggagePrice);
            }
        }

        BigDecimal passengerTotal = ticketPrice.add(totalLuggagePrice);

        return new PassengerPricing(
                passenger.getName(),
                ticketPrice.setScale(2, RoundingMode.HALF_UP),
                totalLuggagePrice.setScale(2, RoundingMode.HALF_UP),
                passengerTotal.setScale(2, RoundingMode.HALF_UP),
                luggageDetails,
                passenger.isChild()
        );
    }

    private BigDecimal calculateTotalTax(BigDecimal subtotal, List<Double> taxRates) {
        BigDecimal totalTax = BigDecimal.ZERO;

        for (Double rate : taxRates) {
            BigDecimal tax = subtotal.multiply(new BigDecimal(rate).divide(new BigDecimal("100")));
            totalTax = totalTax.add(tax);
        }

        return totalTax.setScale(2, RoundingMode.HALF_UP);
    }
}
