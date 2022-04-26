package ru.mironov.service.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ru.mironov.domain.*;
import ru.mironov.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class BoardingPassDao {
    private final SimpleJdbcTemplate source;

    private BoardingPass createBoardingPass(String[] str) throws SQLException {
        return new BoardingPass(str[0], Integer.parseInt(str[1]), Integer.parseInt(str[2]), str[3]);
    }

    public void dounloadData() throws IOException, CsvException, SQLException {
        CSVReader reader = new CSVReader(new FileReader("src/main/resources/ru/mironov/boarding_passes.csv"));
        List<String[]> allraws = null;
        allraws = reader.readAll();
        ArrayList<BoardingPass> boardingPasses = new ArrayList<>();
        for (String[] row : allraws) {
            BoardingPass a = this.createBoardingPass(row);
            boardingPasses.add(a);
        }
        this.saveBoardingPasses(boardingPasses);
    }

    public void saveBoardingPasses(Collection<BoardingPass> boardingPasses) throws SQLException {
        source.preparedStatement("insert into model.boarding_passes(ticket_no, flight_id, boarding_no, seat_no) values (?, ?, ?, ?)",
                insertSpeaker -> {
                    for (BoardingPass boardingPass : boardingPasses) {
                        insertSpeaker.setString(1, boardingPass.getTicketNo());
                        insertSpeaker.setInt(2, boardingPass.getFlightId());
                        insertSpeaker.setInt(3, boardingPass.getBoardingNo());
                        insertSpeaker.setString(4, boardingPass.getSeatNo());
                        insertSpeaker.execute();
                    }
                });
    }
}

