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
     * @param name  the customer's name
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
     * @param id      the customer ID loaded from file
     * @param name    the customer's name
     * @param phone   the customer's phone number
     * @param email   the customer's email address
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

    /**
     * Returns the unique ID for this customer.
     *
     * @return the customer ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the customer's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the customer's phone number.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the customer's email address.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns a list of all bookings made by this customer.
     *
     * @return the list of bookings
     */
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

    /**
     * Returns a short string with the customer's basic details: ID, name, phone, email.
     *
     * @return a short detail string
     */
    public String getDetailsShort() {
        return "Customer #" + id + ": " + name + " - " + phone + " - " + email;
    }

    /**
     * Returns a long string with the customer's details plus each booking's short details.
     *
     * @return a multi-line detail string with bookings
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
     * Checks whether this customer is marked as deleted.
     *
     * @return true if deleted, false otherwise
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Marks or unmarks the customer as deleted.
     *
     * @param deleted true to mark as deleted, false to restore
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
