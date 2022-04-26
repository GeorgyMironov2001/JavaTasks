package ru.mironov.service.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.AllArgsConstructor;
import ru.mironov.domain.Aircraft;
import ru.mironov.service.db.SimpleJdbcTemplate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class AircraftDao {
    private final SimpleJdbcTemplate source;

    public Aircraft createAircraft(String[] str) throws SQLException {
        return new Aircraft(str[0], str[1], Integer.parseInt(str[2]));
    }

    public void dounloadData() throws IOException, CsvException, SQLException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/ru/mironov/aircrafts_data.csv"));
        List<String[]> allraws = null;
        allraws = reader.readAll();
        ArrayList<Aircraft> aircrafts = new ArrayList<>();
        for (String[] row : allraws) {
            Aircraft a = this.createAircraft(row);
            aircrafts.add(a);
        }
        this.saveAircrafts(aircrafts);
    }

    public void saveAircrafts(Collection<Aircraft> aircrafts) throws SQLException {
        source.preparedStatement("insert into model.aircrafts(aircraft_code, model, range) values (?, ?, ?)",
                insertSpeaker -> {
                    for (Aircraft aircraft : aircrafts) {
                        insertSpeaker.setString(1, aircraft.getAircraftCode());
                        insertSpeaker.setString(2, aircraft.getModel());
                        insertSpeaker.setInt(3, aircraft.getRange());
                        insertSpeaker.execute();
                    }
                });
    }


}

