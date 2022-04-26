package ru.mironov.service.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ru.mironov.domain.Seat;
import ru.mironov.domain.TicketFlight;
import ru.mironov.service.db.SimpleJdbcTemplate;
import ru.mironov.domain.Aircraft;
import lombok.AllArgsConstructor;

import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class TicketFlightDao {
    private final SimpleJdbcTemplate source;

    private TicketFlight createTicketFlight(String[] str) throws SQLException {
        return new TicketFlight(str[0], Integer.parseInt(str[1]), str[2], (int) (Double.parseDouble(str[3])));
    }

    public void dounloadData() throws IOException, CsvException, SQLException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/ru/mironov/ticket_flights.csv"));
        List<String[]> allraws = null;
        allraws = reader.readAll();
        ArrayList<TicketFlight> ticketFlights = new ArrayList<>();
        for (String[] row : allraws) {
            TicketFlight a = this.createTicketFlight(row);
            ticketFlights.add(a);
        }
        this.saveTicketFlights(ticketFlights);
    }

    public void saveTicketFlights(Collection<TicketFlight> ticketFlights) throws SQLException {
        source.preparedStatement("insert into model.ticket_flights(ticket_no, flight_id, fare_conditions, amount) values (?, ?, ?, ?)",
                insertSpeaker -> {
                    for (TicketFlight ticketFlight : ticketFlights) {
                        insertSpeaker.setString(1, ticketFlight.getTicketNo());
                        insertSpeaker.setInt(2, ticketFlight.getFlightId());
                        insertSpeaker.setString(3, ticketFlight.getFareConditions());
                        insertSpeaker.setInt(4, ticketFlight.getAmount());
                        insertSpeaker.execute();
                    }
                });
    }
}

