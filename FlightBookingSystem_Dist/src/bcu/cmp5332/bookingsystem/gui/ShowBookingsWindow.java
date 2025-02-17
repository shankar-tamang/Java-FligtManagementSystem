package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShowBookingsWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    public ShowBookingsWindow(Customer customer) {
        setTitle("Bookings for " + customer.getName());
        setSize(400, 300);

        List<Booking> bookings = customer.getBookings();
        String[] columnNames = {"Flight No", "Origin", "Destination", "Departure Date"};
        Object[][] data = new Object[bookings.size()][4];

        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            data[i][0] = booking.getFlight().getFlightNumber();
            data[i][1] = booking.getFlight().getOrigin();
            data[i][2] = booking.getFlight().getDestination();
            data[i][3] = booking.getFlight().getDepartureDate();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }
}
