package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.IOException;

/**
 * A command that marks a Customer as deleted so they are excluded from listings.
 * This operation is soft-delete, so the record remains in the system for rollback or future reference.
 */
public class DeleteCustomer implements Command {

    private final int customerId;

    /**
     * Creates a <code>DeleteCustomer</code> command for the specified customer ID.
     *
     * @param customerId the ID of the customer to be soft-deleted
     */
    public DeleteCustomer(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Marks the customer as deleted, attempts to store the updated system,
     * and rolls back if saving fails.
     *
     * @param fbs the flight booking system to modify
     * @throws FlightBookingSystemException if the customer is not found or storing fails
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // 1) Snapshot for potential rollback
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        // 2) Locate customer and mark as deleted
        Customer customer = fbs.getCustomerById(customerId);
        customer.setDeleted(true);

        // 3) Attempt to store
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            // 4) Roll back on failure
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store data. Deletion rolled back.\n" + ex.getMessage());
        }

        System.out.println("Customer #" + customerId + " marked as deleted.");
    }
}
