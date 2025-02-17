package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;
import java.util.stream.Collectors;

public class ListBookingsFlight implements Command {
    private final int flightId;

    public ListBookingsFlight(int flightId) {
        this.flightId = flightId;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // Verify flight
        Flight flight;
        try {
            flight = fbs.getFlightById(flightId);
        } catch (IllegalArgumentException ex) {
            throw new FlightBookingSystemException("Flight ID " + flightId + " not found.");
        }

        System.out.println("=== Bookings for Flight #" + flightId + " (" + flight.getFlightNumber() + ") ===");
        // Get all bookings, then filter by flight
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

    private String formatBooking(Booking b) {
        return "Booking #" + b.getBookingId() + " | Customer #" + b.getCustomer().getId()
               + " (" + b.getCustomer().getName() + ")"
               + " | Booked On: " + b.getBookingDate();
    }
}
