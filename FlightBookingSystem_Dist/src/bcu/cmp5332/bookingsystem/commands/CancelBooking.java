package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import java.io.IOException;

/**
 * Cancels a booking by removing the booking from a customer and removing the passenger from the flight.
 * Applies a fixed cancellation fee.
 */
public class CancelBooking implements Command {
    private final int customerId;
    private final int flightId;
    
    public CancelBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }
    
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // Take snapshot for rollback
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);
        
        Customer customer = fbs.getCustomerById(customerId);
        Flight flight = fbs.getFlightById(flightId);
        
        // Find the booking in customer's bookings
        Booking bookingToCancel = null;
        for (Booking b : customer.getBookings()) {
            if (b.getFlight().getId() == flightId) {
                bookingToCancel = b;
                break;
            }
        }
        if (bookingToCancel == null) {
            throw new FlightBookingSystemException("No booking found for customer " + customerId + " on flight " + flightId);
        }
        
        // Apply a fixed cancellation fee (e.g., $50)
        double cancellationFee = 50.0;
        bookingToCancel.setFee(cancellationFee);
        
        // Remove the booking from the customer and remove the customer from the flight
        customer.cancelBookingForFlight(flight);
        flight.removePassenger(customer);
        
        // Attempt to store updated system; rollback on failure
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store data. Cancellation rolled back.\n" + ex.getMessage());
        }
        
        System.out.println("Booking cancelled. Cancellation fee of $" + cancellationFee + " applied.");
    }
}
