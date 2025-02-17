package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

/**
 * A command that lists all bookings in the system.
 */
public class ListBookings implements Command {

    /**
     * Fetches all bookings from the {@link FlightBookingSystem} and prints
     * a short summary of each. Prints a count at the end.
     *
     * @param fbs the flight booking system to read from
     * @throws FlightBookingSystemException never thrown here
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        System.out.println("=== All Bookings ===");
        List<Booking> all = fbs.getAllBookings();
        if (all.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        for (Booking b : all) {
            System.out.println(formatBooking(b));
        }
        System.out.println(all.size() + " booking(s) found.");
    }

    /**
     * Formats a single booking for display, including booking ID, customer ID,
     * flight ID, flight number, and booking date.
     *
     * @param b the booking to format
     * @return a string representation of the booking
     */
    private String formatBooking(Booking b) {
        return "Booking #" + b.getBookingId() + " | Customer #" + b.getCustomer().getId()
               + " | Flight #" + b.getFlight().getId() + " (" + b.getFlight().getFlightNumber() + ")"
               + " | Booked On: " + b.getBookingDate();
    }
}
