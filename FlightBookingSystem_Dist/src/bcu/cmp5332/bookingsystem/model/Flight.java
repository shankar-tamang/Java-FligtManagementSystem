package bcu.cmp5332.bookingsystem.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a flight in the booking system, with seat capacities for multiple seat types.
 */
public class Flight implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int nextFlightId = 1;

    private final int id;
    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;

    private int econCapacity;
    private int businessCapacity;
    private int firstCapacity;

    private final double basePrice;
    private boolean deleted = false;

    private final Set<Customer> passengers = new HashSet<>();

    /**
     * Constructor for creating a new Flight, with seat capacities for economy, business, and first class.
     */
    public Flight(String flightNumber, String origin, String destination,
                  LocalDate departureDate, int econCap, int bizCap, int firstCap,
                  double basePrice) {
        this.id = nextFlightId++;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.econCapacity = econCap;
        this.businessCapacity = bizCap;
        this.firstCapacity = firstCap;
        this.basePrice = basePrice;
    }

    /**
     * Constructor for loading from file with explicit ID
     */
    public Flight(int id, String flightNumber, String origin, String destination,
                  LocalDate departureDate, int econCap, int bizCap, int firstCap,
                  double basePrice, boolean deleted) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.econCapacity = econCap;
        this.businessCapacity = bizCap;
        this.firstCapacity = firstCap;
        this.basePrice = basePrice;
        this.deleted = deleted;
        if (id >= nextFlightId) {
            nextFlightId = id + 1;
        }
    }

    public int getId() {
        return id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getEconCapacity() {
        return econCapacity;
    }

    public int getBusinessCapacity() {
        return businessCapacity;
    }

    public int getFirstCapacity() {
        return firstCapacity;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Customer> getPassengers() {
        return passengers;
    }

    /**
     * Checks if the flight is full for a given seat type.
     */
    public boolean isFull(SeatType seatType) {
        switch (seatType) {
            case ECONOMY:
                return econCapacity <= 0;
            case BUSINESS:
                return businessCapacity <= 0;
            case FIRST:
                return firstCapacity <= 0;
        }
        return true;
    }

    /**
     * Adds a passenger to the flight in the specified seat type.
     */
    public void addPassenger(Customer customer, SeatType seatType) {
        if (isFull(seatType)) {
            throw new IllegalStateException("No seats available in " + seatType + " class.");
        }
        switch (seatType) {
            case ECONOMY:
                econCapacity--;
                break;
            case BUSINESS:
                businessCapacity--;
                break;
            case FIRST:
                firstCapacity--;
                break;
        }
        passengers.add(customer);
    }

    /**
     * Removes a passenger from the flight (does NOT restore seat capacity).
     */
    public void removePassenger(Customer customer) {
        passengers.remove(customer);
    }

    public String getDetailsShort() {
        return "Flight #" + id + ": " + flightNumber +
               " from " + origin + " to " + destination +
               " on " + departureDate +
               " | Econ: " + econCapacity +
               " | Biz: " + businessCapacity +
               " | First: " + firstCapacity +
               " | BasePrice: $" + basePrice +
               (deleted ? " [DELETED]" : "");
    }
}
