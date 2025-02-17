package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * A command that displays detailed information about a single customer,
 * identified by customerId, including a list of their bookings.
 */
public class ShowCustomer implements Command {
    private final int customerId;

    /**
     * Constructs a <code>ShowCustomer</code> command for the specified customer ID.
     *
     * @param customerId the ID of the customer to show
     */
    public ShowCustomer(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Retrieves the customer by ID and prints their detailed information:
     * including name, phone, email, deletion status, and any associated bookings.
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
            throw new FlightBookingSystemException("Customer with ID " + customerId + " not found.");
        }

        // Print basic customer details
        System.out.println("=== Customer Details ===");
        System.out.println("ID: " + customer.getId());
        System.out.println("Name: " + customer.getName());
        System.out.println("Phone: " + customer.getPhone());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Is Deleted? " + (customer.isDeleted() ? "Yes" : "No"));

        // Print bookings
        if (!customer.getBookings().isEmpty()) {
            System.out.println("Bookings:");
            for (Booking b : customer.getBookings()) {
                System.out.println(" - Booking #" + b.getBookingId()
                    + " on Flight " + b.getFlight().getFlightNumber()
                    + " (Flight ID: " + b.getFlight().getId() + ")"
                    + " booked on " + b.getBookingDate());
            }
        } else {
            System.out.println("No bookings found for this customer.");
        }
    }
}
