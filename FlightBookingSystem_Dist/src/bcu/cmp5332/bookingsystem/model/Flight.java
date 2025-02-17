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

    /**
     * The number of seats available in Economy class.
     */
    private int econCapacity;

    /**
     * The number of seats available in Business class.
     */
    private int businessCapacity;

    /**
     * The number of seats available in First class.
     */
    private int firstCapacity;

    /**
     * The base price for an Economy seat. Business/First might apply multipliers.
     */
    private final double basePrice;
    private boolean deleted = false;

    private final Set<Customer> passengers = new HashSet<>();

    /**
     * Constructor for creating a new <code>Flight</code>, assigning a unique ID automatically.
     * Useful when adding flights at runtime (not loaded from file).
     *
     * @param flightNumber    the flight number (e.g. "BA123")
     * @param origin          the origin airport or city
     * @param destination     the destination airport or city
     * @param departureDate   the date of departure
     * @param econCap         number of Economy seats
     * @param bizCap          number of Business seats
     * @param firstCap        number of First class seats
     * @param basePrice       the base price for an Economy seat
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
     * Constructor for loading a Flight from file, specifying all fields explicitly.
     * Updates the static counter if necessary.
     *
     * @param id             the flight ID loaded from file
     * @param flightNumber   the flight number
     * @param origin         the origin
     * @param destination    the destination
     * @param departureDate  the date of departure
     * @param econCap        number of Economy seats
     * @param bizCap         number of Business seats
     * @param firstCap       number of First class seats
     * @param basePrice      base price for Economy seats
     * @param deleted        whether this flight is marked as deleted
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

    /**
     * Returns the base price for an Economy seat on this flight.
     * Higher classes might have multipliers.
     *
     * @return the base price
     */
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

    /**
     * Returns the set of <code>Customer</code> passengers on this flight.
     * This set grows each time a customer is added; removing them does not restore seat capacity automatically.
     *
     * @return the set of customers currently on this flight
     */
    public Set<Customer> getPassengers() {
        return passengers;
    }

    /**
     * Checks if the flight is full for a given seat type.
     *
     * @param seatType which seat class to check
     * @return true if no seats left in the specified seat type
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
     * Adds a passenger to the flight in the specified seat type, decrementing that seat's capacity.
     *
     * @param customer the customer to add
     * @param seatType the seat class (ECONOMY, BUSINESS, FIRST)
     * @throws IllegalStateException if the flight is full in that seat class
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
     * If you want to restore capacity, handle it in the booking/cancellation logic.
     *
     * @param customer the customer to remove
     */
    public void removePassenger(Customer customer) {
        passengers.remove(customer);
    }

    /**
     * Returns a short descriptive string of flight details, including seat capacities and base price.
     *
     * @return a short flight detail string
     */
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
