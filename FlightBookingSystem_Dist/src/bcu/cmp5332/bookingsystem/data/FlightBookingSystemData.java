package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Manages loading and storing the entire <code>FlightBookingSystem</code>
 * by delegating to various <code>DataManager</code> implementations
 * (e.g. FlightDataManager, CustomerDataManager, BookingDataManager).
 * <p>
 * Also supports snapshot/restore functionality for rollback operations.
 */
public class FlightBookingSystemData {
    // A list of DataManager objects for flights, customers, bookings, etc.
    private static final List<DataManager> DATA_MANAGERS = Arrays.asList(
        new FlightDataManager(),
        new CustomerDataManager(),
        new BookingDataManager()
    );

    /**
     * Loads data into a new <code>FlightBookingSystem</code> instance by calling
     * each <code>DataManager</code> in <code>DATA_MANAGERS</code>.
     *
     * @return a populated <code>FlightBookingSystem</code> instance
     * @throws IOException if any data file cannot be read
     */
    public static FlightBookingSystem load() throws IOException {
        FlightBookingSystem fbs = new FlightBookingSystem();
        for (DataManager manager : DATA_MANAGERS) {
            manager.loadData(fbs);
        }
        return fbs;
    }

    /**
     * Stores all data from the given <code>FlightBookingSystem</code> by calling
     * each <code>DataManager</code> in <code>DATA_MANAGERS</code>.
     *
     * @param fbs the flight booking system to save
     * @throws IOException if any data file cannot be written
     */
    public static void store(FlightBookingSystem fbs) throws IOException {
        for (DataManager manager : DATA_MANAGERS) {
            manager.storeData(fbs);
        }
    }

    /**
     * Creates a snapshot of the current flights and customers (including
     * their states) in the system, typically used before making changes
     * that may need a rollback if storage fails.
     *
     * @param fbs the flight booking system to snapshot
     * @return a <code>Snapshot</code> containing lists of flights/customers
     */
    public static Snapshot createSnapshot(FlightBookingSystem fbs) {
        return new Snapshot(fbs.getAllFlights(), fbs.getAllCustomers());
    }

    /**
     * Restores the system from a previously taken snapshot, discarding
     * any recent changes.
     *
     * @param fbs       the flight booking system to restore
     * @param snapshot  the snapshot from which to restore
     */
    public static void restoreFromSnapshot(FlightBookingSystem fbs, Snapshot snapshot) {
        fbs.getAllFlights().clear();
        fbs.getAllCustomers().clear();
        for (bcu.cmp5332.bookingsystem.model.Flight flight : snapshot.getFlights()) {
            fbs.addFlight(flight);
        }
        for (bcu.cmp5332.bookingsystem.model.Customer customer : snapshot.getCustomers()) {
            fbs.addCustomer(customer);
        }
    }

    /**
     * A simple container class for storing lists of flights and customers
     * at a given moment in time (for rollback or backup).
     */
    public static class Snapshot {
        private final java.util.List<bcu.cmp5332.bookingsystem.model.Flight> flights;
        private final java.util.List<bcu.cmp5332.bookingsystem.model.Customer> customers;

        /**
         * Constructs a new <code>Snapshot</code> with the provided flight and customer lists.
         *
         * @param flights   a list of flights to include in the snapshot
         * @param customers a list of customers to include in the snapshot
         */
        public Snapshot(
            java.util.List<bcu.cmp5332.bookingsystem.model.Flight> flights,
            java.util.List<bcu.cmp5332.bookingsystem.model.Customer> customers
        ) {
            this.flights = flights;
            this.customers = customers;
        }

        /**
         * Returns the list of flights in this snapshot.
         *
         * @return a list of flights
         */
        public java.util.List<bcu.cmp5332.bookingsystem.model.Flight> getFlights() {
            return flights;
        }

        /**
         * Returns the list of customers in this snapshot.
         *
         * @return a list of customers
         */
        public java.util.List<bcu.cmp5332.bookingsystem.model.Customer> getCustomers() {
            return customers;
        }
    }
}
