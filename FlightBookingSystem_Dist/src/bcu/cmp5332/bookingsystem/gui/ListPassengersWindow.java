package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A window that displays all passengers (customers) assigned to a specific Flight.
 */
public class ListPassengersWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a window listing passengers for the given flight.
     *
     * @param flight the flight whose passengers are to be listed
     */
    public ListPassengersWindow(Flight flight) {
        setTitle("Passengers for Flight " + flight.getFlightNumber());
        setSize(400, 300);

        // If you're on Java 16+, you can do:
        // List<Customer> passengers = flight.getPassengers().stream().toList();
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
