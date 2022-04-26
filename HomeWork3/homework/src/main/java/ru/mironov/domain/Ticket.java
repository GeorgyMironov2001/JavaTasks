package ru.mironov.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
public class Ticket {
    private final String ticketNo;
    private final String bookRef;
    private final String passengerId;
    private final String passengerName;
    private final String contactData;
}
