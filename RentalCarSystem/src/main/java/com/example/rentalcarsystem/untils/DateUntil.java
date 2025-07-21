package com.example.rentalcarsystem.untils;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateUntil {
    public  String formatInstant(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return zonedDateTime.format(formatter);
}
}
