package tests;

import bcu.cmp5332.bookingsystem.model.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Customer class, focusing on email property
 * and basic constructor usage.
 */
class CustomerTest {

    @Test
    void testCustomerEmail() {
        // If you use a constructor that automatically assigns ID, adapt accordingly.
        // For example, if your constructor is (String name, String phone, String email):
        Customer customer = new Customer("John Doe", "123456789", "john@example.com");

        // If your constructor doesn't let you specify ID=1, remove this line:
        // or if you have a constructor that does: Customer c = new Customer(1, "John Doe", ...)
        // Adjust as needed.

        assertEquals("John Doe", customer.getName());
        assertEquals("123456789", customer.getPhone());
        assertEquals("john@example.com", customer.getEmail());

        // If you track deleted or bookings, you can test those too:
        assertFalse(customer.isDeleted());
        assertTrue(customer.getBookings().isEmpty());
    }
}
