package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.IOException;

/**
 * A command that marks a Flight as deleted so it no longer appears in flight listings,
 * implementing a soft-delete strategy.
 */
public class DeleteFlight implements Command {

    private final int flightId;

    /**
     * Creates a <code>DeleteFlight</code> command for the specified flight ID.
     *
     * @param flightId the ID of the flight to delete
     */
    public DeleteFlight(int flightId) {
        this.flightId = flightId;
    }

    /**
     * Soft-deletes the flight, attempts to store the updated system,
     * and rolls back if the save fails.
     *
     * @param fbs the flight booking system to modify
     * @throws FlightBookingSystemException if the flight is not found or storing fails
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // 1) Snapshot for rollback
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        // 2) Find flight and mark it as deleted
        Flight flight = fbs.getFlightById(flightId);
        flight.setDeleted(true);

        // 3) Try saving
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            // 4) Roll back if saving fails
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store data. Deletion rolled back.\n" + ex.getMessage());
        }

        System.out.println("Flight #" + flightId + " marked as deleted.");
    }
}
