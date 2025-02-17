package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import java.io.IOException;

/**
 * Updates an existing booking to a new flight.
 * Removes the customer from the old flight, adds to the new flight, updates the booking's flight reference,
 * and applies a rebooking fee.
 */
public class EditBooking implements Command {
    private final int bookingId;
    private final int newFlightId;
    
    public EditBooking(int bookingId, int newFlightId) {
        this.bookingId = bookingId;
        this.newFlightId = newFlightId;
    }
    
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // Take snapshot for rollback
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);
        
        try {
            // Locate the booking using the unique booking ID
            Booking booking = fbs.getBookingById(bookingId);
            
            // Get the old flight and new flight
            Flight oldFlight = booking.getFlight();
            Flight newFlight = fbs.getFlightById(newFlightId);
            
            // Check capacity for new flight
            if (newFlight.isFull()) {
                throw new FlightBookingSystemException("Cannot update booking. New flight is full.");
            }
            
            // Remove customer from old flight
            oldFlight.removePassenger(booking.getCustomer());
            
            // Add customer to new flight
            newFlight.addPassenger(booking.getCustomer());
            
            // Update the booking to refer to the new flight
            booking.setFlight(newFlight);
            
            // Apply a rebooking fee (e.g., $30)
            double rebookingFee = 30.0;
            booking.setFee(rebookingFee);
            
            // Attempt to store updated system; rollback on failure
            FlightBookingSystemData.store(fbs);
            
            System.out.println("Booking #" + bookingId + " updated to Flight #" + newFlightId 
                    + ". Rebooking fee of $" + rebookingFee + " applied.");
            
        } catch (IOException ex) {
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store updated booking. Changes rolled back.\n" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Error updating booking: " + ex.getMessage());
        }
    }
}
