package br.com.southsystem.importer.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimeUtils {

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.now(ZoneId.of("America/Bahia"));
    }

    public LocalDate getLocalDate() {
        return LocalDate.now(ZoneId.of("America/Bahia"));
    }

    public LocalTime getLocalTime() {
        return LocalTime.now(ZoneId.of("America/Bahia"));
    }

    public LocalDate parse(String date) {
        return LocalDate.parse(date);
    }

    public LocalDate parse(String date, DateTimeFormatter formatter) {
        return LocalDate.parse(date, formatter);
    }
}
