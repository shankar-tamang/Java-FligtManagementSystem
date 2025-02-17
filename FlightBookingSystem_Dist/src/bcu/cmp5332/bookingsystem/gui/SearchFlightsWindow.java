package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A window that allows users to search for flights based on optional
 * origin, destination, and a date range.
 */
public class SearchFlightsWindow extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final FlightBookingSystem fbs;

    private JTextField originField = new JTextField();
    private JTextField destField = new JTextField();
    private JTextField startDateField = new JTextField();
    private JTextField endDateField = new JTextField();

    private JButton searchBtn = new JButton("Search");
    private JButton closeBtn = new JButton("Close");

    /**
     * Constructs the flight search window, which collects criteria and displays matches.
     *
     * @param fbs the flight booking system for searching flights
     */
    public SearchFlightsWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        setTitle("Search Flights");
        setSize(400, 250);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Origin (optional):"));
        add(originField);

        add(new JLabel("Destination (optional):"));
        add(destField);

        add(new JLabel("Start Date (YYYY-MM-DD):"));
        add(startDateField);

        add(new JLabel("End Date (YYYY-MM-DD):"));
        add(endDateField);

        searchBtn.addActionListener(this);
        closeBtn.addActionListener(e -> setVisible(false));

        add(searchBtn);
        add(closeBtn);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            doSearch();
        }
    }

    /**
     * Gathers the criteria from user input, parses dates, and filters flights accordingly.
     * Displays the results in a modal dialog.
     */
    private void doSearch() {
        try {
            String origin = originField.getText().trim();
            String destination = destField.getText().trim();
            String startStr = startDateField.getText().trim();
            String endStr = endDateField.getText().trim();

            LocalDate startDate = startStr.isEmpty() ? LocalDate.MIN : LocalDate.parse(startStr);
            LocalDate endDate = endStr.isEmpty() ? LocalDate.MAX : LocalDate.parse(endStr);

            List<Flight> flights = fbs.getFlights().stream()
                .filter(f -> origin.isEmpty() || f.getOrigin().equalsIgnoreCase(origin))
                .filter(f -> destination.isEmpty() || f.getDestination().equalsIgnoreCase(destination))
                .filter(f -> !f.getDepartureDate().isBefore(startDate))
                .filter(f -> !f.getDepartureDate().isAfter(endDate))
                .collect(Collectors.toList());

            displayResults(flights);
        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the search results in a separate dialog, listing each flight's details.
     *
     * @param flights the list of flights matching the user's criteria
     */
    private void displayResults(List<Flight> flights) {
        JDialog dlg = new JDialog(this, "Search Results", true);
        dlg.setSize(600, 400);

        String[] columns = {"ID", "Flight No", "Origin", "Destination",
            "Departure", "Econ", "Biz", "First", "BasePrice"};
        Object[][] data = new Object[flights.size()][9];

        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            data[i][0] = f.getId();
            data[i][1] = f.getFlightNumber();
            data[i][2] = f.getOrigin();
            data[i][3] = f.getDestination();
            data[i][4] = f.getDepartureDate();
            data[i][5] = f.getEconCapacity();
            data[i][6] = f.getBusinessCapacity();
            data[i][7] = f.getFirstCapacity();
            data[i][8] = f.getBasePrice();
        }

        JTable table = new JTable(data, columns);
        dlg.add(new JScrollPane(table), BorderLayout.CENTER);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }
}
