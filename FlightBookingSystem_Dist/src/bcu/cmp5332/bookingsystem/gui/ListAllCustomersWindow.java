package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListAllCustomersWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    public ListAllCustomersWindow(FlightBookingSystem fbs) {
        setTitle("List of All Customers");
        setSize(600, 400);

        List<Customer> customers = fbs.getCustomers();
        String[] columnNames = {"ID", "Name", "Phone", "Email", "Booking Count"};
        Object[][] data = new Object[customers.size()][5];

        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getName();
            data[i][2] = c.getPhone();
            data[i][3] = c.getEmail();
            data[i][4] = c.getBookings().size();  // Show how many bookings they have
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
	