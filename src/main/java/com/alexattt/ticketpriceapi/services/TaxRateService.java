package com.alexattt.ticketpriceapi.services;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class TaxRateService {
    public List<Double> getTaxRates(String purchaseDate) {
        LocalDateTime localDateTime = LocalDateTime.parse(purchaseDate,
                DateTimeFormatter.ISO_ZONED_DATE_TIME);

        // Weekends have higher tax rates (24%)
        if (isWeekend(localDateTime)) {
            return Arrays.asList(15.0, 6.0, 3.0);
        } else {
            return Arrays.asList(15.0, 6.0);
        }
    }

    private static boolean isWeekend(LocalDateTime date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
    }
}
