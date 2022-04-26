package ru.mironov;

import com.opencsv.exceptions.CsvException;
import org.h2.jdbcx.JdbcConnectionPool;

import org.h2.store.fs.FileUtils;
import ru.mironov.domain.Aircraft;
import ru.mironov.domain.Airport;
import ru.mironov.domain.Flight;
import ru.mironov.service.db.SimpleJdbcTemplate;

import javax.sql.DataSource;

import ru.mironov.service.db.SimpleJdbcTemplate;
import ru.mironov.service.db.DbInit;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mironov.service.dao.*;

import java.awt.print.Book;
import java.io.*;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.URL;
import java.io.IOException;
import java.sql.*;
import java.util.Date;

import ru.mironov.service.dao.AircraftDao.*;
import com.opencsv.CSVReader;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws SQLException, IOException, CsvException, ParseException {
        DataSource pool = JdbcConnectionPool.create(
                "jdbc:h2:mem:database;DB_CLOSE_DELAY=-1", "", "");
        SimpleJdbcTemplate source = new SimpleJdbcTemplate(pool);
        new DbInit(source).create();
        AircraftDao aircraftDao = new AircraftDao(source);
        AirportDao airportDao = new AirportDao(source);
        BoardingPassDao boardingPassDao = new BoardingPassDao(source);
        BookingDao bookingDao = new BookingDao(source);
        FlightDao flightDao = new FlightDao(source);
        SeatDao seatDao = new SeatDao(source);
        TicketDao ticketDao = new TicketDao(source);
        TicketFlightDao ticketFlightDao = new TicketFlightDao(source);

        //aircraftDao.dounloadData();
        airportDao.dounloadData();
        flightDao.dounloadData();
        /*boardingPassDao.dounloadData();
        bookingDao.dounloadData();
        flightDao.dounloadData();
        seatDao.dounloadData();
        ticketDao.dounloadData();
        ticketFlightDao.dounloadData();*/

        //flightDao.getCancelledOnMounth();



        source.statement(stmt -> {
            stmt.execute("drop all objects;");
        });
    }
}
