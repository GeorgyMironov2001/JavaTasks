package ru.mironov.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
public class Seat {
    private final String aircraftCode;
    private final String seatNoo;
    private final String fareConditions;
}
