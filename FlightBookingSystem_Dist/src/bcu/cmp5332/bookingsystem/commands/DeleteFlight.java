package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.IOException;

/**
 * Marks a flight as deleted so it no longer appears in listings.
 */
public class DeleteFlight implements Command {

    private final int flightId;

    /**
     * Creates a DeleteFlight command with the specified flightId.
     *
     * @param flightId the ID of the flight to delete
     */
    public DeleteFlight(int flightId) {
        this.flightId = flightId;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // 1) Take a snapshot in case storing fails
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        // 2) Mark the flight as deleted
        Flight flight = fbs.getFlightById(flightId);
        flight.setDeleted(true);

        // 3) Attempt to store
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            // 4) Rollback on failure
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store data. Deletion rolled back.\n" + ex.getMessage());
        }

        System.out.println("Flight #" + flightId + " marked as deleted.");
    }
}
