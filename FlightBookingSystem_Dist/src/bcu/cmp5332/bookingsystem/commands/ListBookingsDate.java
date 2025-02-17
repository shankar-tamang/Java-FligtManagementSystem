package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ListBookingsDate implements Command {
    private final LocalDate date;

    public ListBookingsDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        System.out.println("=== Bookings on " + date + " ===");
        List<Booking> all = fbs.getAllBookings();
        List<Booking> filtered = all.stream()
            .filter(b -> b.getBookingDate().equals(date))
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("No bookings found on this date.");
            return;
        }
        for (Booking b : filtered) {
            System.out.println(formatBooking(b));
        }
        System.out.println(filtered.size() + " booking(s) found on " + date + ".");
    }

    private String formatBooking(Booking b) {
        return "Booking #" + b.getBookingId()
               + " | Customer #" + b.getCustomer().getId() + " (" + b.getCustomer().getName() + ")"
               + " | Flight #" + b.getFlight().getId() + " (" + b.getFlight().getFlightNumber() + ")";
    }
}
