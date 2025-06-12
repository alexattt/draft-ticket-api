package com.alexattt.ticketpriceapi.models;

import java.math.BigDecimal;
import java.util.List;

public class PricingResponse {
    private BigDecimal totalPrice;
    private List<PassengerPricing> passengerPricings;
    private BigDecimal totalTax;
    private BigDecimal subtotal;

    // Constructors
    public PricingResponse() {}

    public PricingResponse(BigDecimal totalPrice, List<PassengerPricing> passengerPricings,
                           BigDecimal totalTax, BigDecimal subtotal) {
        this.totalPrice = totalPrice;
        this.passengerPricings = passengerPricings;
        this.totalTax = totalTax;
        this.subtotal = subtotal;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<PassengerPricing> getPassengerPricings() {
        return passengerPricings;
    }

    public void setPassengerPricings(List<PassengerPricing> passengerPricings) {
        this.passengerPricings = passengerPricings;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
