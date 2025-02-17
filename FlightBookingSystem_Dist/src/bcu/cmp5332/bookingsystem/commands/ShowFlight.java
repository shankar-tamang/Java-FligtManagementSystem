package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ShowFlight implements Command {
    private final int flightId;

    public ShowFlight(int flightId) {
        this.flightId = flightId;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // Retrieve the flight
        Flight flight;
        try {
            flight = fbs.getFlightById(flightId);
        } catch (IllegalArgumentException ex) {
            throw new FlightBookingSystemException("Flight with ID " + flightId + " not found.");
        }

        // Print flight details
        System.out.println("=== Flight Details ===");
        System.out.println("ID: " + flight.getId());
        System.out.println("Number: " + flight.getFlightNumber());
        System.out.println("Origin: " + flight.getOrigin());
        System.out.println("Destination: " + flight.getDestination());
        System.out.println("Departure Date: " + flight.getDepartureDate());
        System.out.println("Capacity: " + flight.getCapacity());
        System.out.println("Price: " + flight.getPrice());
        System.out.println("Is Deleted? " + (flight.isDeleted() ? "Yes" : "No"));
        
        // Print passenger list
        if (!flight.getPassengers().isEmpty()) {
            System.out.println("Passengers:");
            flight.getPassengers().forEach(cust ->
                System.out.println(" - Customer " + cust.getId() + ": " + cust.getName())
            );
        } else {
            System.out.println("No passengers assigned yet.");
        }
    }
}
