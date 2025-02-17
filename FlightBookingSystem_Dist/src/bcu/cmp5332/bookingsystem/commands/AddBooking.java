package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Issues a new booking by applying dynamic pricing based on remaining capacity and days until departure.
 */
public class AddBooking implements Command {
    private final int customerId;
    private final int flightId;
    
    public AddBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }
    
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // Take snapshot for rollback
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);
        
        // Retrieve customer and flight
        Customer customer = fbs.getCustomerById(customerId);
        Flight flight = fbs.getFlightById(flightId);
        
        // Check if flight is full
        if (flight.isFull()) {
            throw new FlightBookingSystemException("Cannot add booking. Flight is full.");
        }
        
        // Calculate dynamic pricing:
        // Base price plus surcharge based on capacity usage and days until departure.
        double basePrice = flight.getPrice();
        double capacityFactor = 0.2; // 20% surcharge if near capacity
        double dateFactor = 1.0;     // $1 per day remaining (example)
        
        long daysUntilDeparture = ChronoUnit.DAYS.between(fbs.getSystemDate(), flight.getDepartureDate());
        if (daysUntilDeparture < 0) {
            throw new FlightBookingSystemException("Cannot book a past flight.");
        }
        
        double loadRatio = (double) flight.getPassengers().size() / flight.getCapacity();
        double dynamicPrice = basePrice + (capacityFactor * loadRatio * basePrice) + (dateFactor * daysUntilDeparture);
        
        // Create booking and set the computed dynamic price
        Booking booking = new Booking(customer, flight, fbs.getSystemDate());
        booking.setBookingPrice(dynamicPrice);
        
        // Add booking to customer and add customer as passenger on flight
        customer.addBooking(booking);
        flight.addPassenger(customer);
        
        // Attempt to store updated system; rollback on failure
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store data. Changes rolled back.\n" + ex.getMessage());
        }
        
        System.out.println("Booking added successfully with price $" + dynamicPrice);
    }
}
