package com.alexattt.ticketpriceapi.models;

import java.math.BigDecimal;
import java.util.List;

public class PassengerPricing {
    private String passengerName;
    private BigDecimal ticketPrice;
    private BigDecimal luggagePrice;
    private BigDecimal passengerTotal;
    private List<LuggageItemPricing> luggageDetails;
    private boolean isChild;

    // Constructors
    public PassengerPricing() {}

    public PassengerPricing(String passengerName, BigDecimal ticketPrice, BigDecimal luggagePrice,
                            BigDecimal passengerTotal, List<LuggageItemPricing> luggageDetails, boolean isChild) {
        this.passengerName = passengerName;
        this.ticketPrice = ticketPrice;
        this.luggagePrice = luggagePrice;
        this.passengerTotal = passengerTotal;
        this.luggageDetails = luggageDetails;
        this.isChild = isChild;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean child) {
        isChild = child;
    }

    public List<LuggageItemPricing> getLuggageDetails() {
        return luggageDetails;
    }

    public void setLuggageDetails(List<LuggageItemPricing> luggageDetails) {
        this.luggageDetails = luggageDetails;
    }

    public BigDecimal getPassengerTotal() {
        return passengerTotal;
    }

    public void setPassengerTotal(BigDecimal passengerTotal) {
        this.passengerTotal = passengerTotal;
    }

    public BigDecimal getLuggagePrice() {
        return luggagePrice;
    }

    public void setLuggagePrice(BigDecimal luggagePrice) {
        this.luggagePrice = luggagePrice;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
}
