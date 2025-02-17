package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A command that lists all bookings for a specific flight, identified by flightId.
 */
public class ListBookingsFlight implements Command {
    private final int flightId;

    /**
     * Constructs a <code>ListBookingsFlight</code> command for the specified flight.
     *
     * @param flightId the ID of the flight to list bookings for
     */
    public ListBookingsFlight(int flightId) {
        this.flightId = flightId;
    }

    /**
     * Executes the listing of all bookings associated with the given flight ID.
     * Prints each booking and the total count.
     *
     * @param fbs the flight booking system to search in
     * @throws FlightBookingSystemException if the flight ID is invalid
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // Verify the flight
        Flight flight;
        try {
            flight = fbs.getFlightById(flightId);
        } catch (IllegalArgumentException ex) {
            throw new FlightBookingSystemException("Flight ID " + flightId + " not found.");
        }

        System.out.println("=== Bookings for Flight #" + flightId + " (" + flight.getFlightNumber() + ") ===");
        List<Booking> all = fbs.getAllBookings();
        List<Booking> filtered = all.stream()
            .filter(b -> b.getFlight().getId() == flightId)
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("No bookings found for this flight.");
            return;
        }
        for (Booking b : filtered) {
            System.out.println(formatBooking(b));
        }
        System.out.println(filtered.size() + " booking(s) found for this flight.");
    }

    /**
     * Helper method to format a single booking entry for printing.
     *
     * @param b the booking to format
     * @return a string describing the booking
     */
    private String formatBooking(Booking b) {
        return "Booking #" + b.getBookingId() + " | Customer #" + b.getCustomer().getId()
               + " (" + b.getCustomer().getName() + ")"
               + " | Booked On: " + b.getBookingDate();
    }
}
