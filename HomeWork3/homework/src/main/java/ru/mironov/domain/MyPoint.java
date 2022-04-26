package ru.mironov.domain;

import lombok.Data;

@Data
public class MyPoint {
    double x;
    double y;

    public MyPoint(double c1, double c2) {
        this.x = c1;
        this.y = c2;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
