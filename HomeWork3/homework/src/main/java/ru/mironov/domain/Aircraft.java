package ru.mironov.domain;

import lombok.Data;
import org.h2.util.json.JSONArray;

import java.util.Set;
@Data
public class Aircraft {
    private final String aircraftCode;
    private final String model;
    private final int range;

}
