package bcu.cmp5332.bookingsystem.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a flight in the booking system.
 */
public class Flight implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The unique ID of this flight. */
    private final int id;

    /** The flight number (e.g. BA123). */
    private final String flightNumber;

    /** The origin airport/location. */
    private final String origin;

    /** The destination airport/location. */
    private final String destination;

    /** The LocalDate representing departure date. */
    private final LocalDate departureDate;

    /** The maximum capacity (number of seats) for this flight. */
    private final int capacity;

    /** The base price for this flight. */
    private final double price;

    /** Keeps track of which customers are on this flight. */
    private final Set<Customer> passengers = new HashSet<>();

    /**
     * Whether this flight is marked as deleted.
     * A deleted flight should not appear in system listings.
     */
    private boolean deleted = false;

    /**
     * Constructs a new Flight with the given parameters.
     *
     * @param id the unique ID for this flight
     * @param flightNumber the flight number (e.g. "BA123")
     * @param origin the origin airport/location
     * @param destination the destination airport/location
     * @param departureDate the date this flight departs
     * @param capacity the maximum number of seats on this flight
     * @param price the base price per seat
     */
    public Flight(int id, String flightNumber, String origin, String destination,
                  LocalDate departureDate, int capacity, double price) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = capacity;
        this.price = price;
    }

    /**
     * Gets the unique ID of this flight.
     *
     * @return the flight ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the flight number.
     *
     * @return the flight number (e.g. "BA123")
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Gets the origin airport/location for this flight.
     *
     * @return the origin location
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Gets the destination airport/location for this flight.
     *
     * @return the destination location
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Gets the scheduled departure date for this flight.
     *
     * @return the LocalDate representing the departure date
     */
    public LocalDate getDepartureDate() {
        return departureDate;
    }

    /**
     * Gets the maximum capacity for this flight.
     *
     * @return the maximum number of seats
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the base price for this flight.
     *
     * @return the price per seat
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the set of passengers (customers) on this flight.
     *
     * @return a Set of Customer objects
     */
    public Set<Customer> getPassengers() {
        return passengers;
    }

    /**
     * Checks if this flight is full (passengers >= capacity).
     *
     * @return true if the flight is at or over capacity, false otherwise
     */
    public boolean isFull() {
        return passengers.size() >= capacity;
    }

    /**
     * Adds a passenger to this flight if not full.
     *
     * @param customer the Customer to add as a passenger
     * @throws IllegalStateException if the flight is already full
     */
    public void addPassenger(Customer customer) {
        if (isFull()) {
            throw new IllegalStateException("Cannot add passenger. Flight is full.");
        }
        passengers.add(customer);
    }

    /**
     * Removes a passenger from this flight.
     *
     * @param customer the Customer to remove
     */
    public void removePassenger(Customer customer) {
        passengers.remove(customer);
    }

    /**
     * Gets a short details string about this flight.
     *
     * @return a String describing the flight succinctly
     */
    public String getDetailsShort() {
        return "Flight #" + id + ": " + flightNumber + " from " + origin + " to " + destination +
               " departing on " + departureDate + " | Capacity: " + capacity + " | Price: " + price;
    }

    /**
     * Whether this flight is marked as deleted.
     *
     * @return true if deleted, false otherwise
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets this flight as deleted or undeleted.
     * A deleted flight should no longer appear in standard lists.
     *
     * @param deleted the boolean flag to mark deletion
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
