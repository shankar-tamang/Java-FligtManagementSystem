package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

public class ListBookingsCustomer implements Command {
    private final int customerId;

    public ListBookingsCustomer(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // Find the customer
        Customer customer;
        try {
            customer = fbs.getCustomerById(customerId);
        } catch (IllegalArgumentException ex) {
            throw new FlightBookingSystemException("Customer ID " + customerId + " not found.");
        }

        System.out.println("=== Bookings for Customer #" + customerId + " (" + customer.getName() + ") ===");
        List<Booking> bookings = customer.getBookings(); // direct from customer
        if (bookings.isEmpty()) {
            System.out.println("No bookings found for this customer.");
            return;
        }
        for (Booking b : bookings) {
            System.out.println(formatBooking(b));
        }
        System.out.println(bookings.size() + " booking(s) found for this customer.");
    }

    private String formatBooking(Booking b) {
        return "Booking #" + b.getBookingId()
               + " | Flight #" + b.getFlight().getId() + " (" + b.getFlight().getFlightNumber() + ")"
               + " | Booked On: " + b.getBookingDate();
    }
}
