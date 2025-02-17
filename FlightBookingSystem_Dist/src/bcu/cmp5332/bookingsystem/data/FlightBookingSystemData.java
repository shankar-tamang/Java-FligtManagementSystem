package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Manages loading and storing the entire Flight Booking System using multiple DataManagers.
 */
public class FlightBookingSystemData {
    private static final List<DataManager> DATA_MANAGERS = Arrays.asList(
        new FlightDataManager(),
        new CustomerDataManager(),
        new BookingDataManager()
    );

    public static FlightBookingSystem load() throws IOException {
        FlightBookingSystem fbs = new FlightBookingSystem();
        for (DataManager manager : DATA_MANAGERS) {
            manager.loadData(fbs);
        }
        return fbs;
    }

    public static void store(FlightBookingSystem fbs) throws IOException {
        for (DataManager manager : DATA_MANAGERS) {
            manager.storeData(fbs);
        }
    }
    
    /**
     * Creates a snapshot of the current system (flights and customers).
     */
    public static Snapshot createSnapshot(FlightBookingSystem fbs) {
        return new Snapshot(fbs.getAllFlights(), fbs.getAllCustomers());
    }
    
    /**
     * Restores the system from a snapshot.
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
     * A simple container class for a snapshot of flights and customers.
     */
    public static class Snapshot {
        private final java.util.List<bcu.cmp5332.bookingsystem.model.Flight> flights;
        private final java.util.List<bcu.cmp5332.bookingsystem.model.Customer> customers;
        
        public Snapshot(java.util.List<bcu.cmp5332.bookingsystem.model.Flight> flights,
                        java.util.List<bcu.cmp5332.bookingsystem.model.Customer> customers) {
            this.flights = flights;
            this.customers = customers;
        }
        
        public java.util.List<bcu.cmp5332.bookingsystem.model.Flight> getFlights() {
            return flights;
        }
        
        public java.util.List<bcu.cmp5332.bookingsystem.model.Customer> getCustomers() {
            return customers;
        }
    }
}
