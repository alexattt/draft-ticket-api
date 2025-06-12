package com.alexattt.ticketpriceapi.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TicketBasePriceService {

    public BigDecimal getBasePrice(String fromTerminal, String toTerminal) {
        // Mock implementation - in real scenario this would call external service/database
        String route = fromTerminal.toLowerCase() + "-" + toTerminal.toLowerCase();

        // Sample routes with different prices
        return switch (route.toLowerCase()) {
            case "riga-vilnius" -> new BigDecimal("30.00");
            case "vilnius-riga" -> new BigDecimal("10.00");
            case "riga-tallinn" -> new BigDecimal("45.00");
            case "tallinn-riga" -> new BigDecimal("50.00");
            default -> new BigDecimal("25.00"); // Default price
        };
    }
}
