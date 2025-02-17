package bcu.cmp5332.bookingsystem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the booking system.
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The unique ID for this customer. */
    private final int id;

    /** The name of this customer. */
    private final String name;

    /** The phone number of this customer. */
    private final String phone;

    /** The email address of this customer. */
    private final String email;

    /** The list of bookings that this customer has. */
    private final List<Booking> bookings = new ArrayList<>();

    /**
     * Whether this customer is marked as deleted.
     * A deleted customer should not appear in system listings.
     */
    private boolean deleted = false;

    /**
     * Constructs a new Customer with the given information.
     *
     * @param id the unique ID for this customer
     * @param name the customer's name
     * @param phone the customer's phone number
     * @param email the customer's email address
     */
    public Customer(int id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    /**
     * Gets the ID of this customer.
     *
     * @return the customer's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the customer's name.
     *
     * @return the customer's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the customer's phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the customer's email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the list of bookings this customer has.
     *
     * @return the list of Booking objects
     */
    public List<Booking> getBookings() {
        return bookings;
    }

    /**
     * Adds a booking for this customer.
     *
     * @param booking the Booking to add
     * @throws IllegalArgumentException if the booking already exists
     */
    public void addBooking(Booking booking) {
        if (bookings.contains(booking)) {
            throw new IllegalArgumentException("Booking already exists for this customer.");
        }
        bookings.add(booking);
    }

    /**
     * Cancels (removes) a booking for a particular flight.
     *
     * @param flight the Flight to cancel
     */
    public void cancelBookingForFlight(Flight flight) {
        bookings.removeIf(booking -> booking.getFlight().equals(flight));
    }

    /**
     * Gets a short detail string about this customer.
     *
     * @return a String describing the customer succinctly
     */
    public String getDetailsShort() {
        return "Customer #" + id + ": " + name + " - " + phone + " - " + email;
    }

    /**
     * Gets a longer detail string about this customer, including bookings.
     *
     * @return a String describing the customer's details and bookings
     */
    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDetailsShort()).append("\nBookings:\n");
        for (Booking booking : bookings) {
            sb.append(" * ").append(booking.getDetailsShort()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Whether this customer is marked as deleted.
     *
     * @return true if deleted, false otherwise
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Marks or unmarks this customer as deleted.
     *
     * @param deleted the boolean flag for deletion
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
