package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.IOException;

/**
 * A command that creates a new Customer record in the system.
 * It assigns an auto-generated ID based on <code>fbs.getAllCustomers().size() + 1</code>.
 */
public class AddCustomer implements Command {
    private final String name;
    private final String phone;
    private final String email;

    /**
     * Constructs an <code>AddCustomer</code> command with basic details.
     *
     * @param name  the customer's name
     * @param phone the customer's phone number
     * @param email the customer's email address
     */
    public AddCustomer(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    /**
     * Executes the creation of the customer, then stores the updated system state.
     * If storing fails, it rolls back.
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if storing fails or other logic errors occur
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // 1) Take a snapshot
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        // 2) Modify the system in memory
        int newId = fbs.getAllCustomers().size() + 1;
        Customer customer = new Customer(newId, name, phone, email, false);
        fbs.addCustomer(customer);

        // 3) Attempt to store
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            // 4) Rollback if storing fails
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store data. Changes rolled back.\n" + ex.getMessage());
        }

        System.out.println("Customer added successfully: " + name);
    }
}
