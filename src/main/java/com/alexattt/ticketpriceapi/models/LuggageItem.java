package com.alexattt.ticketpriceapi.models;

public class LuggageItem {
    private double weight;

    public LuggageItem() {}

    public LuggageItem(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}