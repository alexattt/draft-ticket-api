package com.alexattt.ticketpriceapi.models;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Request object containing parameters needed for ticket price calculation")
public class PricingRequest {

    @Schema(description = "List of passengers for whom tickets are being priced", required = true)
    private List<Passenger> passengers;

    @Schema(description = "Departure terminal", example = "Riga", required = true)
    private String fromTerminal;

    @Schema(description = "Arrival terminal", example = "Vilnius", required = true)
    private String toTerminal;

    @Schema(description = "Date when the ticket is being purchased",
            example = "2025-06-12T10:30:00.000Z", required = true)
    private String purchaseDate;

    // Constructors, getters, and setters
    public PricingRequest() {}

    public PricingRequest(List<Passenger> passengers, String fromTerminal, String toTerminal, String purchaseDate) {
        this.passengers = passengers;
        this.fromTerminal = fromTerminal;
        this.toTerminal = toTerminal;
        this.purchaseDate = purchaseDate;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public String getFromTerminal() {
        return fromTerminal;
    }

    public void setFromTerminal(String fromTerminal) {
        this.fromTerminal = fromTerminal;
    }

    public String getToTerminal() {
        return toTerminal;
    }

    public void setToTerminal(String toTerminal) {
        this.toTerminal = toTerminal;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
