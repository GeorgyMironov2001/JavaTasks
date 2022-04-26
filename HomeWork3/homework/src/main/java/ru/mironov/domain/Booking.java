package ru.mironov.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
public class Booking {
    private final String bookRef;
    private final Timestamp bookDate;
    private final int totalAmount;
}
