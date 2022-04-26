package ru.mironov.domain;

import lombok.Data;

import java.io.Serial;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Set;

@Data
public class Flight {
    private final int flightId;
    private final String flightNo;
    private final Timestamp scheduledDeparture;
    private final Timestamp scheduledArrival;
    private final String departureAirport;
    private final String arrivalAirport;
    private final String status;
    private final String aircraftCode;
    private final Timestamp actualDeparture;
    private final Timestamp actualArrival;
}
