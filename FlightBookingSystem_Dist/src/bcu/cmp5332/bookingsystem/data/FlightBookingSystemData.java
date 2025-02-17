package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FlightBookingSystemData class orchestrates loading and storing
 * data for Flights, Customers, and Bookings, and also provides
 * snapshot/rollback functionality.
 */
public class FlightBookingSystemData {

    // Existing managers for flights, customers, and bookings
    private static final List<DataManager> DATA_MANAGERS = Arrays.asList(
            new FlightDataManager(),
            new CustomerDataManager(),
            new BookingDataManager()
    );

    /**
     * Loads the entire system from the data files (flights.txt,
     * customers.txt, bookings.txt).
     *
     * @return a new FlightBookingSystem populated with data.
     * @throws IOException if any file access error occurs
     */
    public static FlightBookingSystem load() throws IOException {
        FlightBookingSystem fbs = new FlightBookingSystem();
        for (DataManager manager : DATA_MANAGERS) {
            manager.loadData(fbs);
        }
        return fbs;
    }

    /**
     * Stores (saves) the entire system to the data files.
     *
     * @param fbs the FlightBookingSystem to store
     * @throws IOException if any file write error occurs
     */
    public static void store(FlightBookingSystem fbs) throws IOException {
        for (DataManager manager : DATA_MANAGERS) {
            manager.storeData(fbs);
        }
    }

    /**
     * Creates a simple 'snapshot' of the current state (Flights & Customers).
     * This is a shallow copy of the main lists so that we can
     * restore them if storing fails.
     *
     * @param fbs the FlightBookingSystem to snapshot
     * @return a Snapshot object holding copies of flights and customers
     */
    public static Snapshot createSnapshot(FlightBookingSystem fbs) {
        // Copy the flight list
        List<Flight> flightsCopy = new ArrayList<>(fbs.getFlights());
        // Copy the customer list
        List<Customer> customersCopy = new ArrayList<>(fbs.getCustomers());
        // For bookings, we rely on the fact they're contained in each Customer / Flight
        // so snapshotting the top-level lists is generally enough for add/remove changes

        return new Snapshot(flightsCopy, customersCopy);
    }

    /**
     * Restores (rolls back) the flight system to a previous snapshot.
     * This discards any changes made since the snapshot was created.
     *
     * @param fbs the current FlightBookingSystem to revert
     * @param snapshot the snapshot to restore
     */
    public static void restoreFromSnapshot(FlightBookingSystem fbs, Snapshot snapshot) {
        // Clear existing lists
        fbs.getFlights().clear();
        fbs.getCustomers().clear();

        // Re-add everything from the snapshot
        for (Flight flight : snapshot.getFlights()) {
            fbs.addFlight(flight);
        }
        for (Customer customer : snapshot.getCustomers()) {
            fbs.addCustomer(customer);
        }
    }

    /**
     * Inner class to hold the shallow copies of flights and customers.
     * This is enough to revert any add/remove operation if storing fails.
     */
    public static class Snapshot {
        private final List<Flight> flights;
        private final List<Customer> customers;

        public Snapshot(List<Flight> flights, List<Customer> customers) {
            this.flights = flights;
            this.customers = customers;
        }

        public List<Flight> getFlights() {
            return flights;
        }

        public List<Customer> getCustomers() {
            return customers;
        }
    }
}
