package ru.mironov.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
public class BoardingPass {
    private final String ticketNo;
    private final int flightId;
    private final int boardingNo;
    private final String seatNo;
}
