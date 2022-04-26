package ru.mironov.service.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ru.mironov.domain.Booking;
import ru.mironov.domain.Seat;
import ru.mironov.service.db.SimpleJdbcTemplate;
import ru.mironov.domain.Aircraft;
import lombok.AllArgsConstructor;

import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class SeatDao {
    private final SimpleJdbcTemplate source;

    private Seat createSeat(String[] str) throws SQLException {
        return new Seat(str[0], str[1], str[2]);
    }

    public void dounloadData() throws IOException, CsvException, SQLException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/ru/mironov/seats.csv"));
        List<String[]> allraws = null;
        allraws = reader.readAll();
        ArrayList<Seat> seats = new ArrayList<>();
        for (String[] row : allraws) {
            Seat a = this.createSeat(row);
            seats.add(a);
        }
        this.saveSeats(seats);
    }

    public void saveSeats(Collection<Seat> seats) throws SQLException {
        source.preparedStatement("insert into model.seats(aircraft_code, seat_no, fare_conditions) values (?, ?, ?)",
                insertSpeaker -> {
                    for (Seat seat : seats) {
                        insertSpeaker.setString(1, seat.getAircraftCode());
                        insertSpeaker.setString(2, seat.getSeatNoo());
                        insertSpeaker.setString(3, seat.getFareConditions());
                        insertSpeaker.execute();
                    }
                });
    }
}

