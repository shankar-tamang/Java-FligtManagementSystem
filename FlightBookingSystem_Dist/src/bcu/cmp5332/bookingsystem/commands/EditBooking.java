package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.SeatType;
import bcu.cmp5332.bookingsystem.model.Customer;

import java.io.IOException;

/**
 * A command to update an existing booking to a new flight, with an option to change seat type.
 * <p>
 * The command removes the passenger from the old flight and seats them on the new flight,
 * adjusting seat capacity, and applies a fixed rebooking fee.
 */
public class EditBooking implements Command {
    private final int bookingId;
    private final int newFlightId;
    private final SeatType newSeatType; // can be null if seat type is unchanged

    /**
     * Simplified constructor that preserves the old seat type.
     *
     * @param bookingId   the booking to update
     * @param newFlightId the new flight ID
     */
    public EditBooking(int bookingId, int newFlightId) {
        this(bookingId, newFlightId, null);
    }

    /**
     * Full constructor that can also specify a new seat type for the new flight.
     *
     * @param bookingId   the booking to update
     * @param newFlightId the new flight ID
     * @param newSeatType the new seat type (or null to keep old seat type)
     */
    public EditBooking(int bookingId, int newFlightId, SeatType newSeatType) {
        this.bookingId = bookingId;
        this.newFlightId = newFlightId;
        this.newSeatType = newSeatType;
    }

    /**
     * Executes the booking update, removing the passenger from the old flight,
     * adding them to the new flight (with the seat type if applicable),
     * and applying a rebooking fee.
     *
     * @param fbs the flight booking system to modify
     * @throws FlightBookingSystemException if booking or new flight is invalid,
     *                                      or if saving fails
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // 1) Snapshot
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        try {
            // 2) Find the existing booking
            Booking booking = fbs.getBookingById(bookingId);

            // old flight & seat type
            Flight oldFlight = booking.getFlight();
            SeatType oldSeatType = booking.getSeatType();
            Customer customer = booking.getCustomer();

            // new flight
            Flight newFlight = fbs.getFlightById(newFlightId);

            // seat type to use
            SeatType seatTypeToUse = (newSeatType != null) ? newSeatType : oldSeatType;

            // check capacity on new flight
            if (newFlight.isFull(seatTypeToUse)) {
                throw new FlightBookingSystemException(
                    "Cannot update booking. New flight is full in " + seatTypeToUse + " class."
                );
            }

            // remove from old flight
            oldFlight.removePassenger(customer);

            // add to new flight
            newFlight.addPassenger(customer, seatTypeToUse);

            // update booking references
            booking.setFlight(newFlight);
            booking.setSeatType(seatTypeToUse);

            // rebooking fee
            double rebookingFee = 30.0;
            booking.setFee(rebookingFee);

            // store
            FlightBookingSystemData.store(fbs);

            System.out.println(
                "Booking #" + bookingId +
                " updated to Flight #" + newFlightId +
                " (seat type=" + seatTypeToUse + "). Fee=$" + rebookingFee
            );

        } catch (IOException ex) {
            // rollback
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store updated booking. Rolled back.\n" + ex.getMessage());

        } catch (IllegalArgumentException ex) {
            // also rollback
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Error updating booking: " + ex.getMessage());
        }
    }
}
