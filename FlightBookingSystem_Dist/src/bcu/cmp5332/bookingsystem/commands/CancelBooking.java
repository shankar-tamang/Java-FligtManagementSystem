package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import java.io.IOException;

/**
 * A command that cancels an existing booking. It removes the booking from the
 * <code>Customer</code> and the passenger from the <code>Flight</code>.
 * A fixed cancellation fee is applied to the booking record.
 */
public class CancelBooking implements Command {
    private final int customerId;
    private final int flightId;

    /**
     * Constructs a CancelBooking command to remove the booking between the given
     * customer and flight IDs.
     *
     * @param customerId the ID of the customer
     * @param flightId   the ID of the flight
     */
    public CancelBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }

    /**
     * Executes the cancellation, applying a fixed fee (e.g., $50), then tries
     * to store the system. If storing fails, it rolls back.
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if no matching booking is found,
     *                                      or if saving fails
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // 1) Take snapshot for rollback
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        Customer customer = fbs.getCustomerById(customerId);
        Flight flight = fbs.getFlightById(flightId);

        // Locate the relevant booking
        Booking bookingToCancel = null;
        for (Booking b : customer.getBookings()) {
            if (b.getFlight().getId() == flightId) {
                bookingToCancel = b;
                break;
            }
        }
        if (bookingToCancel == null) {
            throw new FlightBookingSystemException(
                "No booking found for customer " + customerId + " on flight " + flightId
            );
        }

        // Apply a fixed cancellation fee
        double cancellationFee = 50.0;
        bookingToCancel.setFee(cancellationFee);

        // Remove references
        customer.cancelBookingForFlight(flight);
        flight.removePassenger(customer);

        // Attempt to store or rollback
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException(
                "Failed to store data. Cancellation rolled back.\n" + ex.getMessage()
            );
        }

        System.out.println("Booking cancelled. Cancellation fee of $" + cancellationFee + " applied.");
    }
}
