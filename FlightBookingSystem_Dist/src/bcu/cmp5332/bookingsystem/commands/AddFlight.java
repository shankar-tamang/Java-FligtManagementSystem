package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.IOException;
import java.time.LocalDate;

public class AddFlight implements Command {
    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final int capacity;
    private final double price;

    public AddFlight(String flightNumber, String origin, String destination,
                     LocalDate departureDate, int capacity, double price) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = capacity;
        this.price = price;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // 1) Take a snapshot before making changes
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        // 2) Modify the system in memory
        int newId = fbs.getFlights().size() + 1;
        Flight flight = new Flight(newId, flightNumber, origin, destination, departureDate, capacity, price);
        fbs.addFlight(flight);

        // 3) Attempt to store the updated system
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            // 4) Rollback if storing fails
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store data. Changes rolled back.\n" + ex.getMessage());
        }

        System.out.println("Flight added successfully: " + flightNumber);
    }
}
