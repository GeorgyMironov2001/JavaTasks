package ru.mironov.service.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.ObjectUtils;
import ru.mironov.domain.Booking;
import ru.mironov.domain.Flight;
import ru.mironov.domain.Ticket;
import ru.mironov.service.db.SimpleJdbcTemplate;
import ru.mironov.domain.Aircraft;
import lombok.AllArgsConstructor;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serial;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@AllArgsConstructor
public class FlightDao {
    private final SimpleJdbcTemplate source;

    private Timestamp getTimeStamp(String str) throws ParseException {
        if (!Objects.equals(str, "")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(str.substring(0, str.length() - 3));
            Timestamp q = new Timestamp(parsedDate.getTime());
            return new Timestamp(parsedDate.getTime());
        } else {
            return null;
        }
    }

    private Flight createFlight(String[] str) throws SQLException, ParseException {
        return new Flight(Integer.parseInt(str[0]), str[1], getTimeStamp(str[2]), getTimeStamp(str[3]), str[4], str[5],
                str[6], str[7], getTimeStamp(str[8]), getTimeStamp(str[9]));
    }

    public void dounloadData() throws IOException, CsvException, SQLException, ParseException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/ru/mironov/flights.csv"));
        List<String[]> allraws = reader.readAll();
        ArrayList<Flight> flights = new ArrayList<>();
        for (String[] row : allraws) {
            Flight a = this.createFlight(row);
            flights.add(a);
        }
        this.saveFlights(flights);
    }

    public void saveFlights(Collection<Flight> flights) throws SQLException {
        source.preparedStatement("insert into" +
                        " model.flights(flight_id, flight_no, scheduled_departure, scheduled_arrival," +
                        " departure_airport, arrival_airport, status, aircraft_code, actual_departure, actual_arrival)" +
                        " values" +
                        " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                insertSpeaker -> {
                    for (Flight flight : flights) {
                        insertSpeaker.setObject(1, flight.getFlightId());
                        insertSpeaker.setString(2, flight.getFlightNo());
                        insertSpeaker.setTimestamp(3, flight.getScheduledDeparture());
                        insertSpeaker.setTimestamp(4, flight.getScheduledArrival());
                        insertSpeaker.setString(5, flight.getDepartureAirport());
                        insertSpeaker.setString(6, flight.getArrivalAirport());
                        insertSpeaker.setString(7, flight.getStatus());
                        insertSpeaker.setString(8, flight.getAircraftCode());
                        insertSpeaker.setTimestamp(9, flight.getActualDeparture());
                        insertSpeaker.setTimestamp(10, flight.getActualArrival());
                        insertSpeaker.execute();
                    }
                });
    }

    public void getCitiesWithCanselledFlight() throws SQLException {
        source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select city, count(status)  from model.flights as f " +
                    "Inner join model.airports as a on f.departure_airport = a.airport_code" +
                    " where status = 'Cancelled' group by city order by count(status) DESC");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("city") + " " + resultSet.getString("count(status)"));
            }

        });
    }

    public void getAverageTimeToAirport() throws SQLException {
        source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select city," +
                    " arrival_airport, avg(abs(DateDiff( 'ss', actual_departure, actual_arrival ))) as fly_time" +
                    "  from model.flights as f Inner join model.airports as a on f.departure_airport = a.airport_code " +
                    " group by city, arrival_airport");
            while (resultSet.next()) {
                if (resultSet.getString("fly_time") != null) {
                    int seconds = resultSet.getInt("fly_time");

                    System.out.println(resultSet.getString("city").split(", ")[0].substring(4) + " " + resultSet.getString("arrival_airport") + " " + String.format("%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60, seconds % 60));
                }
            }

        });
    }

    public void getCancelledOnMounth() throws SQLException {
        source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select scheduled_departure from model.flights " +
                    "where status = 'Cancelled'");
            int[] cancelled = new int[12];
            while (resultSet.next()) {
                String s = resultSet.getString("scheduled_departure");
                String str = s.split("-")[1];
                int mounth = Integer.parseInt(str);
                cancelled[mounth - 1] += 1;
            }
            System.out.println("Месяц, отмены");
            for (int i = 0; i < 12; ++i) {
                System.out.println(i + 1 + " " + cancelled[i]);
            }
        });
    }
}
