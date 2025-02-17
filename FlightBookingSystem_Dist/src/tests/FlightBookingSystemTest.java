package tests;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class FlightBookingSystemTest {

    @Test
    void testAddCustomer() {
        FlightBookingSystem fbs = new FlightBookingSystem();
        Customer customer = new Customer(1, "Alice", "12345", "alice@example.com");

        fbs.addCustomer(customer);
        assertEquals(1, fbs.getCustomers().size());
        assertEquals(customer, fbs.getCustomerById(1));
    }

    @Test
    void testAddFlight() {
        FlightBookingSystem fbs = new FlightBookingSystem();
        Flight flight = new Flight(1, "LH123", "Berlin", "New York", LocalDate.of(2025, 4, 20), 150, 450.0);

        fbs.addFlight(flight);
        assertEquals(1, fbs.getFlights().size());
        assertEquals(flight, fbs.getFlightById(1));
    }

    @Test
    void testDuplicateCustomerThrowsException() {
        FlightBookingSystem fbs = new FlightBookingSystem();
        Customer customer1 = new Customer(1, "Alice", "12345", "alice@example.com");
        Customer customer2 = new Customer(1, "Bob", "67890", "bob@example.com");

        fbs.addCustomer(customer1);
        assertThrows(IllegalArgumentException.class, () -> fbs.addCustomer(customer2));
    }
}
