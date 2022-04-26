package ru.mironov.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
public class TicketFlight {
    private final String ticketNo;
    private final int flightId;
    private final String fareConditions;
    private final int amount;
}
