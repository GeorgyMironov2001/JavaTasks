package ru.mironov.service.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ru.mironov.domain.Airport;
import ru.mironov.domain.BoardingPass;
import ru.mironov.domain.Booking;
import ru.mironov.service.db.SimpleJdbcTemplate;
import ru.mironov.domain.Aircraft;
import lombok.AllArgsConstructor;
import ru.mironov.App;

import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class BookingDao {
    private final SimpleJdbcTemplate source;

    private Timestamp getTimeStamp(String str) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date parsedDate = dateFormat.parse(str.substring(0, str.length() - 3));
        return new Timestamp(parsedDate.getTime());
    }

    private Booking createBooking(String[] str) throws SQLException, ParseException {
        Timestamp timestamp = getTimeStamp(str[1]);
        return new Booking(str[0], timestamp, Integer.parseInt(str[2].substring(0, str[2].length() - 3)));
    }

    public void dounloadData() throws IOException, CsvException, SQLException, ParseException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/ru/mironov/bookings.csv"));
        List<String[]> allraws = null;
        allraws = reader.readAll();
        ArrayList<Booking> bookings = new ArrayList<>();
        for (String[] row : allraws) {
            Booking a = this.createBooking(row);
            bookings.add(a);
        }
        this.saveBookings(bookings);
    }

    public void saveBookings(Collection<Booking> bookings) throws SQLException {
        source.preparedStatement("insert into model.bookings(book_ref, book_date, total_amount) values (?, ?, ?)",
                insertSpeaker -> {
                    for (Booking booking : bookings) {
                        insertSpeaker.setString(1, booking.getBookRef());
                        insertSpeaker.setTimestamp(2, booking.getBookDate());
                        insertSpeaker.setInt(3, booking.getTotalAmount());
                        insertSpeaker.execute();
                    }
                });
    }
}
