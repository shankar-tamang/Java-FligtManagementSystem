package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

public class ListBookings implements Command {
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

    private String formatBooking(Booking b) {
        return "Booking #" + b.getBookingId() + " | Customer #" + b.getCustomer().getId()
               + " | Flight #" + b.getFlight().getId() + " (" + b.getFlight().getFlightNumber() + ")"
               + " | Booked On: " + b.getBookingDate();
    }
}
