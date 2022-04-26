package ru.mironov.service.dao;

import ru.mironov.domain.Airport;
import ru.mironov.domain.MyPoint;
import ru.mironov.service.db.SimpleJdbcTemplate;
import ru.mironov.domain.Aircraft;
import lombok.AllArgsConstructor;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.AllArgsConstructor;
import ru.mironov.domain.Aircraft;
import ru.mironov.service.db.SimpleJdbcTemplate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class AirportDao {
    private final SimpleJdbcTemplate source;

    private Map<String, String> getName(String str) {
        String[] aiportName = str.split(",");
        Map<String, String> aName = new HashMap<String, String>();
        for(String x : aiportName) {
            String[] y = x.split(": ");
            aName.put(y[0].substring(2, y[0].length()-1), y[1].substring(1, y[1].length()-1));
        }
        return aName;
    }

    private Airport createAirport(String[] str) throws SQLException {
        String[] s = str[3].split(",");
        String coordinate1 = s[0].substring(1);
        String coordinate2 = s[1].substring(0, s[1].length() - 1);
        double c1 = Double.parseDouble(coordinate1);
        double c2 = Double.parseDouble(coordinate2);
        return new Airport(str[0], getName(str[1]), getName(str[2]), new MyPoint(c1, c2), str[4]);
    }

    public void dounloadData() throws IOException, CsvException, SQLException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/ru/mironov/airports_data.csv"));
        List<String[]> allraws = null;
        allraws = reader.readAll();
        ArrayList<Airport> airports = new ArrayList<>();
        for (String[] row : allraws) {
            Airport a = this.createAirport(row);
            airports.add(a);
        }
        this.saveAirports(airports);
    }

    public void saveAirports(Collection<Airport> airports) throws SQLException {
        source.preparedStatement("insert into model.airports(airport_code, airport_name, city, coordinates, timezone) values (?, ?, ?, ?, ?)",
                insertSpeaker -> {
                    for (Airport airport : airports) {
                        insertSpeaker.setString(1, airport.getAirportCode());
                        insertSpeaker.setString(2, airport.getAirportName().toString());
                        insertSpeaker.setString(3, airport.getCity().toString());
                        insertSpeaker.setString(4, airport.getCoordinates().toString());
                        insertSpeaker.setString(5, airport.getTimezone());
                        insertSpeaker.execute();
                    }
                });
    }

    public void getCitesWithAirports() throws SQLException {
        source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select city, airport_code from model.airports ");
            Map<String, ArrayList<String>> resMap = new HashMap<>();
            while (resultSet.next()) {
                if (resMap.containsKey(resultSet.getString("city"))) {
                    resMap.get(resultSet.getString("city")).add(resultSet.getString("airport_code"));
                } else {
                    ArrayList<String> a = new ArrayList<>();
                    a.add(resultSet.getString("airport_code"));
                    resMap.put(resultSet.getString("city"), a);
                }
            }
            for (Map.Entry<String, ArrayList<String>> item : resMap.entrySet()) {
                if (item.getValue().size() > 1) {
                    System.out.println(item.getKey() + " " + item.getValue().toString());
                }
            }
        });
    }
}

