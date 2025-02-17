package tests;

import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Customer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class FlightTest {

    @Test
    void testFlightCapacityAndPrice() {
        Flight flight = new Flight(1, "ABC123", "London", "New York", LocalDate.of(2025, 5, 10), 100, 500.0);

        assertEquals(1, flight.getId());
        assertEquals("ABC123", flight.getFlightNumber());
        assertEquals("London", flight.getOrigin());
        assertEquals("New York", flight.getDestination());
        assertEquals(LocalDate.of(2025, 5, 10), flight.getDepartureDate());
        assertEquals(100, flight.getCapacity());
        assertEquals(500.0, flight.getPrice());
    }

    @Test
    void testFlightOverbooking() {
        Flight flight = new Flight(2, "XYZ789", "Paris", "Tokyo", LocalDate.of(2025, 6, 15), 2, 800.0);
        Customer c1 = new Customer(1, "Alice", "12345", "alice@example.com");
        Customer c2 = new Customer(2, "Bob", "67890", "bob@example.com");

        flight.addPassenger(c1);
        flight.addPassenger(c2);

        assertThrows(IllegalStateException.class, () -> {
            Customer c3 = new Customer(3, "Charlie", "11111", "charlie@example.com");
            flight.addPassenger(c3);
        });
    }
}
