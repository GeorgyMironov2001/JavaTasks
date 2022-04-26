package ru.mironov.service.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ru.mironov.domain.Seat;
import ru.mironov.domain.Ticket;
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
public class TicketDao {
    private final SimpleJdbcTemplate source;

    private Ticket createTicket(String[] str) throws SQLException {
        return new Ticket(str[0], str[1], str[2], str[3], str[4]);
    }

    public void dounloadData() throws IOException, CsvException, SQLException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/ru/mironov/tickets.csv"));
        List<String[]> allraws;
        allraws = reader.readAll();
        ArrayList<Ticket> tickets = new ArrayList<>();
        for (String[] row : allraws) {
            Ticket a = this.createTicket(row);
            tickets.add(a);
        }
        this.saveTickets(tickets);
    }

    public void saveTickets(Collection<Ticket> tickets) throws SQLException {
        source.preparedStatement("insert into model.tickets(ticket_no, book_ref, passenger_id, passenger_name, contact_data)" +
                        " values (?, ?, ?, ?, ?)",
                insertSpeaker -> {
                    for (Ticket ticket : tickets) {
                        insertSpeaker.setString(1, ticket.getTicketNo());
                        insertSpeaker.setString(2, ticket.getBookRef());
                        insertSpeaker.setString(3, ticket.getPassengerId());
                        insertSpeaker.setString(4, ticket.getPassengerName());
                        insertSpeaker.setString(5, ticket.getContactData());
                        insertSpeaker.execute();
                    }
                });
    }
}
