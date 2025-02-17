package tests;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Tests adding flights/customers to the FlightBookingSystem.
 */
class FlightBookingSystemTest {

    @Test
    void testAddCustomer() {
        FlightBookingSystem fbs = new FlightBookingSystem();
        Customer customer = new Customer("Alice", "12345", "alice@example.com");

        fbs.addCustomer(customer);
        assertEquals(1, fbs.getCustomers().size());
        // This test might fail if you rely on auto-generated ID. We can fetch by ID if you track it
        // or check that getCustomers().contains(customer).
        assertTrue(fbs.getCustomers().contains(customer));
    }

    @Test
    void testAddFlight() {
        FlightBookingSystem fbs = new FlightBookingSystem();
        Flight flight = new Flight(
            1,
            "LH123",
            "Berlin",
            "New York",
            LocalDate.of(2025, 4, 20),
            100, 30, 20, 450.0,
            false
        );

        fbs.addFlight(flight);
        assertEquals(1, fbs.getFlights().size());
        // If you track ID => flight ID =1, then we can do:
        assertEquals(flight, fbs.getFlightById(1));
    }

    @Test
    void testDuplicateCustomerThrowsException() {
        FlightBookingSystem fbs = new FlightBookingSystem();
        Customer customer1 = new Customer("Alice", "12345", "alice@example.com");
        // Suppose auto-generated ID => first = #1, second also = #1 if we forcibly set or replicate.
        // Or we test with the same ID
        // If your system doesn't track ID this way, adapt as needed.

        fbs.addCustomer(customer1);

        Customer customer2 = new Customer("Bob", "67890", "bob@example.com");
        // Attempt to forcibly set same ID => might not be possible if your constructor auto-increments
        // so you might test differently. Or if you rely on name conflict, adapt.

        // We'll rely on the logic that if we do a direct ID conflict or name conflict, it fails
        // If your system doesn't do that, remove this test or adapt it.

        assertThrows(IllegalArgumentException.class, () -> {
            fbs.addCustomer(customer2);
            fbs.addCustomer(customer2); // adding same customer again might cause duplication
        });
    }
}
