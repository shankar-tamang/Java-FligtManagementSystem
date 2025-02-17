package tests;

import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.SeatType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Tests for Flight class that uses separate seat capacities
 * (econCap, bizCap, firstCap) and a base price.
 */
class FlightTest {

    @Test
    void testFlightConstructorAndGetters() {
        // Using a 10-parameter constructor with 'deleted=false' for test
        Flight flight = new Flight(
            1,
            "ABC123",
            "London",
            "New York",
            LocalDate.of(2025, 5, 10),
            50,       // econ seats
            30,       // business seats
            20,       // first seats
            500.0,    // base price
            false     // deleted
        );

        assertEquals(1, flight.getId());
        assertEquals("ABC123", flight.getFlightNumber());
        assertEquals("London", flight.getOrigin());
        assertEquals("New York", flight.getDestination());
        assertEquals(LocalDate.of(2025, 5, 10), flight.getDepartureDate());

        assertEquals(50, flight.getEconCapacity());
        assertEquals(30, flight.getBusinessCapacity());
        assertEquals(20, flight.getFirstCapacity());
        assertEquals(500.0, flight.getBasePrice(), 0.0001);

        assertFalse(flight.isDeleted());
        assertTrue(flight.getPassengers().isEmpty());
    }

    @Test
    void testFlightOverbookingEconomy() {
        // econCap=2, businessCap=10, firstCap=5
        Flight flight = new Flight(
            2,
            "XYZ789",
            "Paris",
            "Tokyo",
            LocalDate.of(2025, 6, 15),
            2,     // economy seats
            10,    // business seats
            5,     // first seats
            800.0, // base price
            false
        );

        Customer c1 = new Customer("Alice", "12345", "alice@example.com");
        Customer c2 = new Customer("Bob", "67890", "bob@example.com");

        // Fill up economy seats
        flight.addPassenger(c1, SeatType.ECONOMY);
        flight.addPassenger(c2, SeatType.ECONOMY);

        // The 3rd economy passenger should fail
        assertThrows(IllegalStateException.class, () -> {
            Customer c3 = new Customer("Charlie", "11111", "charlie@example.com");
            flight.addPassenger(c3, SeatType.ECONOMY);
        });

        // But we can still add a business seat, for instance
        Customer c4 = new Customer("Diana", "22222", "diana@example.com");
        flight.addPassenger(c4, SeatType.BUSINESS);
        assertEquals(1, flight.getBusinessCapacity()); // was 10, now 9 left
    }
}
