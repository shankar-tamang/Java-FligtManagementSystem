package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ListPassengersWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    public ListPassengersWindow(Flight flight) {
        setTitle("Passengers for Flight " + flight.getFlightNumber());
        setSize(400, 300);

        // If you're on Java 16+, you can do:
        // List<Customer> passengers = flight.getPassengers().stream().toList();
        // Otherwise:
        List<Customer> passengers = flight.getPassengers().stream()
                .collect(Collectors.toList());

        String[] columnNames = {"ID", "Name", "Phone", "Email"};
        Object[][] data = new Object[passengers.size()][4];

        for (int i = 0; i < passengers.size(); i++) {
            Customer customer = passengers.get(i);
            data[i][0] = customer.getId();
            data[i][1] = customer.getName();
            data[i][2] = customer.getPhone();
            data[i][3] = customer.getEmail();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }
}
