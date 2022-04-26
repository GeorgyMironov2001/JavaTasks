create SCHEMA model;

create table model.aircrafts
(
    aircraft_code CHAR(3),
    model         TEXT,
    range         INTEGER
);

create table model.airports
(
    airport_code CHAR(3),
    airport_name TEXT,
    city         TEXT,
    coordinates  TEXT,
    timezone     TEXT
);

create table model.bookings
(
    book_ref     CHAR(6),
    book_date    TIMESTAMP,
    total_amount NUMERIC(10, 2)
);

create table model.tickets
(
    ticket_no      CHAR(13),
    book_ref       CHAR(6),
    passenger_id   VARCHAR(20),
    passenger_name TEXT,
    contact_data   TEXT
);

create table model.flights
(
    flight_id           SERIAL,
    flight_no           CHAR(6),
    scheduled_departure timestamp,
    scheduled_arrival   timestamp,
    departure_airport   CHAR(3),
    arrival_airport     CHAR(3),
    status              VARCHAR(20),
    aircraft_code       CHAR(3),
    actual_departure    timestamp,
    actual_arrival      timestamp
);

create table model.seats
(
    aircraft_code   Char(3),
    seat_no         VARCHAR(4),
    fare_conditions VARCHAR(10)

);

create table model.ticket_flights
(
    ticket_no       CHAR(13),
    flight_id       INTEGER,
    fare_conditions VARCHAR(10),
    amount          NUMERIC(10, 2)
);

create table model.boarding_passes
(
    ticket_no   CHAR(13),
    flight_id   INTEGER,
    boarding_no INTEGER,
    seat_no     VARCHAR(4)
);
/*alter table model.ticket_flights
    add constraint FK_Ticket_No foreign key (ticket_no) references tickets (ticket_no);
alter table model.tickets
    add constraint FK_Book_Ref foreign key (book_ref) references bookings (book_ref);
alter table model.flights
    add constraint FK_Departure_Airport foreign key (departure_airport) references airports (airport_code);
alter table model.flights
    add constraint FK_Arrival_Airport foreign key (arrival_airport) references airports (airport_code);
alter table model.seats
    add constraint FK_Aircraft_Code foreign key (aircraft_code) references aircrafts (aircraft_code);
alter table model.ticket_flights
    add constraint FK_Flight_Id foreign key (flight_id) references flights (flight_id);
alter table model.ticket_flights
    add constraint FK_Fare_Condition foreign key (fare_conditions) references seats (fare_conditions);
alter table model.boarding_passes
    add constraint FK_Ticket_No foreign key (ticket_no) references tickets (ticket_no);
alter table model.boarding_passes
    add constraint FK_Flight_Id foreign key (flight_id) references flights (flight_id);
alter table model.boarding_passes
    add constraint FK_Seat_No foreign key (seat_no) references seats (seat_no);*/