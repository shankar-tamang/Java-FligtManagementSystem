package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

/**
 * A simple report window that shows total flights, customers, bookings,
 * total revenue, and the most booked flight.
 */
public class AdminReportsWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private final FlightBookingSystem fbs;
    private JTextArea textArea = new JTextArea();

    /**
     * Constructs an AdminReportsWindow, which displays summary statistics about
     * flights, customers, bookings, and revenue.
     *
     * @param fbs the flight booking system from which to gather data
     */
    public AdminReportsWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        setTitle("System Reports");
        setSize(500, 400);
        setLayout(new BorderLayout());

        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        generateReport();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Gathers data from the FlightBookingSystem and populates the text area
     * with stats including total flights, customers, bookings, total revenue,
     * and the most booked flight.
     */
    private void generateReport() {
        List<Booking> allBookings = fbs.getAllBookings();

        double totalRevenue = 0.0;
        for (Booking b : allBookings) {
            totalRevenue += (b.getBookingPrice() + b.getFee());
        }

        Flight mostBooked = fbs.getFlights().stream()
            .max(Comparator.comparingInt(f -> f.getPassengers().size()))
            .orElse(null);

        StringBuilder sb = new StringBuilder();
        sb.append("==== System Reports ====\n");
        sb.append("Total Active Flights: ").append(fbs.getFlights().size()).append("\n");
        sb.append("Total Active Customers: ").append(fbs.getCustomers().size()).append("\n");
        sb.append("Total Bookings: ").append(allBookings.size()).append("\n");
        sb.append(String.format("Total Revenue: $%.2f\n", totalRevenue));

        if (mostBooked != null) {
            sb.append("Most Booked Flight: ").append(mostBooked.getFlightNumber())
              .append(" with ").append(mostBooked.getPassengers().size()).append(" passengers.\n");
        } else {
            sb.append("No flights have passengers yet.\n");
        }

        textArea.setText(sb.toString());
    }
}
