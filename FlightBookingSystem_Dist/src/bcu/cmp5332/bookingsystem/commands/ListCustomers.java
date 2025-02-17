package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * A command that lists all active (non-deleted) customers in the system.
 */
public class ListCustomers implements Command {

    /**
     * Executes the command, printing each customer's short details.
     * Also prints the total number of active customers found.
     *
     * @param fbs the flight booking system from which to retrieve customers
     */
    @Override
    public void execute(FlightBookingSystem fbs) {
        System.out.println("Listing all customers:");
        for (Customer customer : fbs.getCustomers()) {
            System.out.println(customer.getDetailsShort());
        }
        System.out.println("Total customers: " + fbs.getCustomers().size());
    }
}
