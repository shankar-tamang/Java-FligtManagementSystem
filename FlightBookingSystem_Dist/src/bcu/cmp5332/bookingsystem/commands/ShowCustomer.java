package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ShowCustomer implements Command {
    private final int customerId;

    public ShowCustomer(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // Retrieve the customer
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
