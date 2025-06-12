package com.alexattt.ticketpriceapi.models;

import java.math.BigDecimal;

public class LuggageItemPricing {
    private BigDecimal price;

    // Constructors
    public LuggageItemPricing() {}

    public LuggageItemPricing(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
