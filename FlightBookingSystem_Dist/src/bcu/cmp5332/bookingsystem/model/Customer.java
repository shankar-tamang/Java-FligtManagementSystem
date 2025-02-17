package bcu.cmp5332.bookingsystem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the booking system.
 * Uses an auto-incrementing static counter for unique customer IDs.
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Static counter for generating unique customer IDs.
    private static int nextCustomerId = 1;
    
    // Unique customer ID.
    private final int id;
    private final String name;
    private final String phone;
    private final String email;
    private final List<Booking> bookings = new ArrayList<>();
    
    // Soft deletion flag.
    private boolean deleted = false;
    
    /**
     * Constructor for creating a new Customer.
     * Automatically assigns a unique ID.
     *
     * @param name the customer's name
     * @param phone the customer's phone number
     * @param email the customer's email address
     */
    public Customer(String name, String phone, String email) {
        this.id = nextCustomerId++;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    
    /**
     * Constructor for loading a Customer from storage.
     * Updates the static counter if necessary.
     *
     * @param id the customer ID loaded from file
     * @param name the customer's name
     * @param phone the customer's phone number
     * @param email the customer's email address
     * @param deleted the deletion status
     */
    public Customer(int id, String name, String phone, String email, boolean deleted) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.deleted = deleted;
        if (id >= nextCustomerId) {
            nextCustomerId = id + 1;
        }
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public List<Booking> getBookings() {
        return bookings;
    }
    
    /**
     * Adds a booking to this customer.
     *
     * @param booking the booking to add.
     * @throws IllegalArgumentException if the booking already exists.
     */
    public void addBooking(Booking booking) {
        if (bookings.contains(booking)) {
            throw new IllegalArgumentException("Booking already exists for this customer.");
        }
        bookings.add(booking);
    }
    
    /**
     * Cancels (removes) a booking for the given flight.
     *
     * @param flight the flight to cancel booking for.
     */
    public void cancelBookingForFlight(Flight flight) {
        bookings.removeIf(booking -> booking.getFlight().equals(flight));
    }
    
    public String getDetailsShort() {
        return "Customer #" + id + ": " + name + " - " + phone + " - " + email;
    }
    
    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDetailsShort()).append("\nBookings:\n");
        for (Booking booking : bookings) {
            sb.append(" * ").append(booking.getDetailsShort()).append("\n");
        }
        return sb.toString();
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    
    /**
     * Marks or unmarks the customer as deleted.
     *
     * @param deleted true to mark as deleted.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
