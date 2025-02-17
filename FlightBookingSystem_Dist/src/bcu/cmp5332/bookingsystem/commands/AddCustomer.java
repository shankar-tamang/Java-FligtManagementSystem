package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.IOException;

public class AddCustomer implements Command {
    private final String name;
    private final String phone;
    private final String email;

    public AddCustomer(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

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
