package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.IOException;

/**
 * Marks a customer as deleted so they no longer appear in listings.
 */
public class DeleteCustomer implements Command {

    private final int customerId;

    /**
     * Creates a DeleteCustomer command with the specified customerId.
     *
     * @param customerId the ID of the customer to delete
     */
    public DeleteCustomer(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // 1) Snapshot
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        // 2) Mark the customer as deleted
        Customer customer = fbs.getCustomerById(customerId);
        customer.setDeleted(true);

        // 3) Attempt to store
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            // 4) Rollback
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store data. Deletion rolled back.\n" + ex.getMessage());
        }

        System.out.println("Customer #" + customerId + " marked as deleted.");
    }
}
