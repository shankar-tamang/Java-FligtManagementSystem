package bcu.cmp5332.bookingsystem.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a booking made by a customer for a specific flight.
 * Each booking is assigned a unique bookingId and stores the final price paid and any fee applied.
 */
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Static counter to assign unique booking IDs
    private static int nextBookingId = 1;
    
    // Unique ID for this booking
    private final int bookingId;
    
    private final Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    
    // Fee applied for cancellation or rebooking (if any)
    private double fee = 0.0;
    
    // The final price for this booking (including dynamic pricing adjustments)
    private double bookingPrice = 0.0;
    
    /**
     * Constructs a new Booking.
     *
     * @param customer the customer making the booking
     * @param flight the flight being booked
     * @param bookingDate the date of the booking
     */
    public Booking(Customer customer, Flight flight, LocalDate bookingDate) {
        this.bookingId = nextBookingId++;
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
    }
    
    public int getBookingId() {
        return bookingId;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public Flight getFlight() {
        return flight;
    }
    
    /**
     * Updates the flight associated with this booking.
     *
     * @param newFlight the new flight to assign
     */
    public void setFlight(Flight newFlight) {
        this.flight = newFlight;
    }
    
    public LocalDate getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public double getFee() {
        return fee;
    }
    
    public void setFee(double fee) {
        this.fee = fee;
    }
    
    public double getBookingPrice() {
        return bookingPrice;
    }
    
    public void setBookingPrice(double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }
    
    /**
     * Returns a short description of the booking.
     *
     * @return a string describing the booking
     */
    public String getDetailsShort() {
        return "Booking #" + bookingId + " | Flight: " + flight.getFlightNumber() 
               + " | Price: $" + bookingPrice + " | Fee: $" + fee;
    }
}
