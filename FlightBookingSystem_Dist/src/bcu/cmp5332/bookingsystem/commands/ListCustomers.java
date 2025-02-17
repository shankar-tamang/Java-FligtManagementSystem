package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ListCustomers implements Command {
    @Override
    public void execute(FlightBookingSystem fbs) {
        System.out.println("Listing all customers:");
        for (Customer customer : fbs.getCustomers()) {
            System.out.println(customer.getDetailsShort());
        }
        System.out.println("Total customers: " + fbs.getCustomers().size());
    }
}
