package ru.mironov.domain;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class Airport {
    private final String airportCode;
    private final Map<String, String> airportName;
    private final Map<String, String> city;
    private final MyPoint coordinates;
    private final String timezone;
}
