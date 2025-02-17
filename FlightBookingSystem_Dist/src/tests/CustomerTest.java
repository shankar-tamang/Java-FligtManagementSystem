package tests;

import bcu.cmp5332.bookingsystem.model.Customer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerEmail() {
        Customer customer = new Customer(1, "John Doe", "123456789", "john@example.com");

        assertEquals(1, customer.getId());
        assertEquals("John Doe", customer.getName());
        assertEquals("123456789", customer.getPhone());
        assertEquals("john@example.com", customer.getEmail());
    }
}
