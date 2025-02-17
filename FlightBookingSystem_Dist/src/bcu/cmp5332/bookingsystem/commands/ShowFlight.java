package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * A command that displays details about a specific flight, including seat capacities,
 * base price, deletion status, and passenger list.
 */
public class ShowFlight implements Command {

    private final int flightId;

    /**
     * Constructs a <code>ShowFlight</code> command for the specified flight ID.
     *
     * @param flightId the ID of the flight to show
     */
    public ShowFlight(int flightId) {
        this.flightId = flightId;
    }

    /**
     * Retrieves the flight by ID and prints its detailed information
     * (e.g., seat capacities, base price, passenger list).
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if the flight ID is invalid
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        Flight flight;
        try {
            flight = fbs.getFlightById(flightId);
        } catch (IllegalArgumentException ex) {
            throw new FlightBookingSystemException("Flight with ID " + flightId + " not found.");
        }

        // Print flight details
        System.out.println("=== Flight Details ===");
        System.out.println("ID: " + flight.getId());
        System.out.println("Flight Number: " + flight.getFlightNumber());
        System.out.println("Origin: " + flight.getOrigin());
        System.out.println("Destination: " + flight.getDestination());
        System.out.println("Departure Date: " + flight.getDepartureDate());
        System.out.println("Economy Seats: " + flight.getEconCapacity());
        System.out.println("Business Seats: " + flight.getBusinessCapacity());
        System.out.println("First Class Seats: " + flight.getFirstCapacity());
        System.out.println("Base Price (Economy): " + flight.getBasePrice());
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
