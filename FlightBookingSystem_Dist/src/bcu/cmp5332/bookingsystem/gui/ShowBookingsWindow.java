package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShowBookingsWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    /**
     * Shows all bookings in the system.
     */
    public ShowBookingsWindow(FlightBookingSystem fbs) {
        setTitle("All Bookings");
        setSize(700, 400);

        List<Booking> bookings = fbs.getAllBookings();
        String[] columnNames = {
            "Booking ID", "Customer ID", "Flight #",
            "Seat Type", "Food Option", "Price", "Fee", "Booked On"
        };
        Object[][] data = new Object[bookings.size()][8];

        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            data[i][0] = b.getBookingId();
            data[i][1] = b.getCustomer().getId();
            data[i][2] = b.getFlight().getFlightNumber();
            data[i][3] = b.getSeatType();
            data[i][4] = b.getFoodOption();
            data[i][5] = b.getBookingPrice();
            data[i][6] = b.getFee();
            data[i][7] = b.getBookingDate();
        }

        JTable table = new JTable(data, columnNames);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Shows bookings for a single customer.
     */
    public ShowBookingsWindow(Customer customer) {
        setTitle("Bookings for " + customer.getName());
        setSize(700, 400);

        List<Booking> bookings = customer.getBookings();
        String[] columnNames = {
            "Booking ID", "Flight #", "Seat Type", "Food Option",
            "Price", "Fee", "Booked On"
        };
        Object[][] data = new Object[bookings.size()][7];

        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            data[i][0] = b.getBookingId();
            data[i][1] = b.getFlight().getFlightNumber();
            data[i][2] = b.getSeatType();
            data[i][3] = b.getFoodOption();
            data[i][4] = b.getBookingPrice();
            data[i][5] = b.getFee();
            data[i][6] = b.getBookingDate();
        }

        JTable table = new JTable(data, columnNames);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setVisible(true);
    }
}
