package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

/**
 * A command that lists all bookings for a specific customer, identified by their customer ID.
 */
public class ListBookingsCustomer implements Command {
    private final int customerId;

    /**
     * Constructs a <code>ListBookingsCustomer</code> command for the specified customer.
     *
     * @param customerId the ID of the customer whose bookings to list
     */
    public ListBookingsCustomer(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Executes the command, printing a header and all bookings associated
     * with the given customer, followed by a total count.
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if the customer ID is invalid
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        Customer customer;
        try {
            customer = fbs.getCustomerById(customerId);
        } catch (IllegalArgumentException ex) {
            throw new FlightBookingSystemException("Customer ID " + customerId + " not found.");
        }

        System.out.println("=== Bookings for Customer #" + customerId + " (" + customer.getName() + ") ===");
        List<Booking> bookings = customer.getBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings found for this customer.");
            return;
        }
        for (Booking b : bookings) {
            System.out.println(formatBooking(b));
        }
        System.out.println(bookings.size() + " booking(s) found for this customer.");
    }

    /**
     * Helper method to format a single booking for printing.
     *
     * @param b the booking to format
     * @return a string describing the booking
     */
    private String formatBooking(Booking b) {
        return "Booking #" + b.getBookingId()
               + " | Flight #" + b.getFlight().getId() + " (" + b.getFlight().getFlightNumber() + ")"
               + " | Booked On: " + b.getBookingDate();
    }
}
