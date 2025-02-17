package bcu.cmp5332.bookingsystem.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The core Flight Booking System, managing flights, customers, and bookings.
 */
public class FlightBookingSystem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final Map<Integer, Customer> customers = new HashMap<>();
    private final Map<Integer, Flight> flights = new HashMap<>();
    private LocalDate systemDate = LocalDate.now();
    
    public LocalDate getSystemDate() {
        return systemDate;
    }
    
    public void setSystemDate(LocalDate systemDate) {
        this.systemDate = systemDate;
    }
    
    /**
     * Returns a list of non-deleted customers.
     *
     * @return list of customers
     */
    public List<Customer> getCustomers() {
        return customers.values().stream()
                .filter(c -> !c.isDeleted())
                .collect(Collectors.toList());
    }
    
    public Customer getCustomerById(int id) {
        if (!customers.containsKey(id)) {
            throw new IllegalArgumentException("Customer ID " + id + " not found.");
        }
        return customers.get(id);
    }
    
    public void addCustomer(Customer customer) {
        if (customers.containsKey(customer.getId())) {
            throw new IllegalArgumentException("Customer ID already exists: " + customer.getId());
        }
        customers.put(customer.getId(), customer);
    }
    
    /**
     * Returns a list of non-deleted flights.
     *
     * @return list of flights
     */
    public List<Flight> getFlights() {
        return flights.values().stream()
                .filter(f -> !f.isDeleted())
                .collect(Collectors.toList());
    }
    
    /**
     * Returns a list of upcoming flights (departure date after systemDate).
     *
     * @return list of future flights
     */
    public List<Flight> getUpcomingFlights() {
        return flights.values().stream()
                .filter(f -> !f.isDeleted())
                .filter(f -> f.getDepartureDate().isAfter(systemDate))
                .collect(Collectors.toList());
    }
    
    public Flight getFlightById(int id) {
        if (!flights.containsKey(id)) {
            throw new IllegalArgumentException("Flight ID " + id + " not found.");
        }
        return flights.get(id);
    }
    
    public void addFlight(Flight flight) {
        if (flights.containsKey(flight.getId())) {
            throw new IllegalArgumentException("Flight ID already exists: " + flight.getId());
        }
        flights.put(flight.getId(), flight);
    }
    
    /**
     * Retrieves all bookings from all non-deleted customers.
     *
     * @return list of all bookings
     */
    public List<Booking> getAllBookings() {
        List<Booking> results = new ArrayList<>();
        for (Customer c : getCustomers()) {
            results.addAll(c.getBookings());
        }
        return results;
    }
    
    /**
     * Searches for a booking by its unique booking ID.
     *
     * @param bookingId the booking ID
     * @return the booking if found
     * @throws IllegalArgumentException if not found
     */
    public Booking getBookingById(int bookingId) {
        for (Customer c : customers.values()) {
            for (Booking b : c.getBookings()) {
                if (b.getBookingId() == bookingId) {
                    return b;
                }
            }
        }
        throw new IllegalArgumentException("Booking ID " + bookingId + " not found.");
    }
}
