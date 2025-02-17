package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.*;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * The main GUI window for the Flight Booking Management System.
 * Includes menu items for flights, bookings, customers, admin actions, and new features:
 * - Search Flights
 * - Admin Reports
 */
public class MainWindow extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JMenuBar menuBar;
    private JMenu adminMenu, flightsMenu, bookingsMenu, customersMenu;

    // Admin
    private JMenuItem adminExit;
    private JMenuItem adminReports;

    // Flights
    private JMenuItem flightsView, flightsAdd, flightsDel, flightsRemove, flightsSearch;

    // Bookings
    private JMenuItem bookingsIssue, bookingsUpdate, bookingsCancel, bookingsViewAll;

    // Customers
    private JMenuItem custView, custAdd, custDel, custListAll;

    private final FlightBookingSystem fbs;

    public MainWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

    public FlightBookingSystem getFlightBookingSystem() {
        return fbs;
    }

    private void initialize() {
        setTitle("Flight Booking Management System");
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // =========== Admin Menu ===========
        adminMenu = new JMenu("Admin");
        menuBar.add(adminMenu);

        adminReports = new JMenuItem("Generate Reports");
        adminMenu.add(adminReports);
        adminReports.addActionListener(this);

        adminExit = new JMenuItem("Exit");
        adminMenu.add(adminExit);
        adminExit.addActionListener(this);

        // =========== Flights Menu ===========
        flightsMenu = new JMenu("Flights");
        menuBar.add(flightsMenu);

        flightsView = new JMenuItem("View All");
        flightsAdd = new JMenuItem("Add Flight");
        flightsDel = new JMenuItem("View Passengers");
        flightsRemove = new JMenuItem("Delete Flight");
        flightsSearch = new JMenuItem("Search Flights"); // new

        flightsMenu.add(flightsView);
        flightsMenu.add(flightsAdd);
        flightsMenu.add(flightsDel);
        flightsMenu.add(flightsRemove);
        flightsMenu.add(flightsSearch);

        flightsView.addActionListener(this);
        flightsAdd.addActionListener(this);
        flightsDel.addActionListener(this);
        flightsRemove.addActionListener(this);
        flightsSearch.addActionListener(this);

        // =========== Bookings Menu ===========
        bookingsMenu = new JMenu("Bookings");
        menuBar.add(bookingsMenu);

        bookingsIssue = new JMenuItem("Issue");
        bookingsUpdate = new JMenuItem("Update");
        bookingsCancel = new JMenuItem("Cancel");
        bookingsViewAll = new JMenuItem("View All Bookings");

        bookingsMenu.add(bookingsIssue);
        bookingsMenu.add(bookingsUpdate);
        bookingsMenu.add(bookingsCancel);
        bookingsMenu.add(bookingsViewAll);

        bookingsIssue.addActionListener(this);
        bookingsUpdate.addActionListener(this);
        bookingsCancel.addActionListener(this);
        bookingsViewAll.addActionListener(this);

        // =========== Customers Menu ===========
        customersMenu = new JMenu("Customers");
        menuBar.add(customersMenu);

        custView = new JMenuItem("View Bookings (Single)");
        custAdd = new JMenuItem("Add Customer");
        custDel = new JMenuItem("Delete Customer");
        custListAll = new JMenuItem("List All Customers");

        customersMenu.add(custView);
        customersMenu.add(custAdd);
        customersMenu.add(custDel);
        customersMenu.add(custListAll);

        custView.addActionListener(this);
        custAdd.addActionListener(this);
        custDel.addActionListener(this);
        custListAll.addActionListener(this);

        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // Admin
        if (ae.getSource() == adminExit) {
            try {
                FlightBookingSystemData.store(fbs);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);

        } else if (ae.getSource() == adminReports) {
            new AdminReportsWindow(fbs);

        // Flights
        } else if (ae.getSource() == flightsView) {
            displayFlights();

        } else if (ae.getSource() == flightsAdd) {
            new AddFlightWindow(this);

        } else if (ae.getSource() == flightsDel) {
            String input = JOptionPane.showInputDialog("Enter Flight ID to view passengers:");
            if (input == null) return;
            try {
                int flightId = Integer.parseInt(input);
                Flight flight = fbs.getFlightById(flightId);
                new ListPassengersWindow(flight);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid Flight ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == flightsRemove) {
            String input = JOptionPane.showInputDialog("Enter Flight ID to delete:");
            if (input == null) return;
            try {
                int flightId = Integer.parseInt(input);
                new DeleteFlight(flightId).execute(fbs);
                JOptionPane.showMessageDialog(this, "Flight #" + flightId + " deleted (hidden).");
            } catch (FlightBookingSystemException fex) {
                JOptionPane.showMessageDialog(this, "Error: " + fex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Flight ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == flightsSearch) {
            new SearchFlightsWindow(fbs);

        // Bookings
        } else if (ae.getSource() == bookingsIssue) {
            new IssueBookingWindow(fbs);

        } else if (ae.getSource() == bookingsUpdate) {
            new UpdateBookingWindow(fbs);

        } else if (ae.getSource() == bookingsCancel) {
            new CancelBookingWindow(fbs);

        } else if (ae.getSource() == bookingsViewAll) {
            new ShowBookingsWindow(fbs);

        // Customers
        } else if (ae.getSource() == custView) {
            String input = JOptionPane.showInputDialog("Enter Customer ID to view bookings:");
            if (input == null) return;
            try {
                int customerId = Integer.parseInt(input);
                new ShowBookingsWindow(fbs.getCustomerById(customerId));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid Customer ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == custAdd) {
            new AddCustomerWindow(fbs);

        } else if (ae.getSource() == custDel) {
            String input = JOptionPane.showInputDialog("Enter Customer ID to delete:");
            if (input == null) return;
            try {
                int customerId = Integer.parseInt(input);
                new DeleteCustomer(customerId).execute(fbs);
                JOptionPane.showMessageDialog(this, "Customer #" + customerId + " deleted (hidden).");
            } catch (FlightBookingSystemException fex) {
                JOptionPane.showMessageDialog(this, "Error: " + fex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Customer ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == custListAll) {
            new ListAllCustomersWindow(fbs);
        }
    }

    /**
     * Displays all non-deleted flights in a table.
     */
    public void displayFlights() {
        List<Flight> flightsList = fbs.getFlights();

        // For seat classes, we might just show the sum or show them individually
        String[] columns = {"ID", "Flight No", "Origin", "Destination",
            "Departure", "Econ", "Biz", "First", "BasePrice"};
        Object[][] data = new Object[flightsList.size()][9];

        for (int i = 0; i < flightsList.size(); i++) {
            Flight flight = flightsList.get(i);
            data[i][0] = flight.getId();
            data[i][1] = flight.getFlightNumber();
            data[i][2] = flight.getOrigin();
            data[i][3] = flight.getDestination();
            data[i][4] = flight.getDepartureDate();
            data[i][5] = flight.getEconCapacity();
            data[i][6] = flight.getBusinessCapacity();
            data[i][7] = flight.getFirstCapacity();
            data[i][8] = flight.getBasePrice();
        }

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        getContentPane().removeAll();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
