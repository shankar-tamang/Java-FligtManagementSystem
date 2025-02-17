package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Customer;

import bcu.cmp5332.bookingsystem.commands.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.*;

/**
 * The main GUI window for the Flight Booking Management System.
 */
public class MainWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JMenuBar menuBar;

    private JMenu adminMenu, flightsMenu, bookingsMenu, customersMenu;
    private JMenuItem adminExit;
    private JMenuItem flightsView, flightsAdd, flightsDel, flightsRemove; // flightsRemove = "Delete Flight" 
    private JMenuItem bookingsIssue, bookingsUpdate, bookingsCancel;
    private JMenuItem custView, custAdd, custDel, custListAll;

    private FlightBookingSystem fbs;

    /**
     * Constructs the main window for the flight booking system GUI.
     *
     * @param fbs the FlightBookingSystem instance
     */
    public MainWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

    /**
     * Gets the FlightBookingSystem instance used by this window.
     *
     * @return the FlightBookingSystem
     */
    public FlightBookingSystem getFlightBookingSystem() {
        return fbs;
    }

    /**
     * Initializes the GUI components and menu layout.
     */
    private void initialize() {
        setTitle("Flight Booking Management System");
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Admin Menu
        adminMenu = new JMenu("Admin");
        menuBar.add(adminMenu);
        adminExit = new JMenuItem("Exit");
        adminMenu.add(adminExit);
        adminExit.addActionListener(this);

        // Flights Menu
        flightsMenu = new JMenu("Flights");
        menuBar.add(flightsMenu);

        flightsView = new JMenuItem("View All");
        flightsAdd = new JMenuItem("Add Flight");
        flightsDel = new JMenuItem("View Passengers");
        flightsRemove = new JMenuItem("Delete Flight"); // new

        flightsMenu.add(flightsView);
        flightsMenu.add(flightsAdd);
        flightsMenu.add(flightsDel);
        flightsMenu.add(flightsRemove);

        for (int i = 0; i < flightsMenu.getItemCount(); i++) {
            flightsMenu.getItem(i).addActionListener(this);
        }

        // Bookings Menu
        bookingsMenu = new JMenu("Bookings");
        menuBar.add(bookingsMenu);

        bookingsIssue = new JMenuItem("Issue");
        bookingsUpdate = new JMenuItem("Update");
        bookingsCancel = new JMenuItem("Cancel");
        bookingsMenu.add(bookingsIssue);
        bookingsMenu.add(bookingsUpdate);
        bookingsMenu.add(bookingsCancel);

        for (int i = 0; i < bookingsMenu.getItemCount(); i++) {
            bookingsMenu.getItem(i).addActionListener(this);
        }

        // Customers Menu
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

    /**
     * Handles menu actions triggered by the user.
     *
     * @param ae the ActionEvent representing the user's click
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // Admin Exit
        if (ae.getSource() == adminExit) {
            try {
                FlightBookingSystemData.store(fbs);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);

        // Flights
        } else if (ae.getSource() == flightsView) {
            displayFlights();

        } else if (ae.getSource() == flightsAdd) {
            new AddFlightWindow(this);

        } else if (ae.getSource() == flightsDel) {
            // "View Passengers" for flight
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
            // "Delete Flight"
            String input = JOptionPane.showInputDialog("Enter Flight ID to delete:");
            if (input == null) return;

            try {
                int flightId = Integer.parseInt(input);
                Command cmd = new DeleteFlight(flightId);
                cmd.execute(fbs);
                JOptionPane.showMessageDialog(this, "Flight #" + flightId + " deleted (hidden).");
            } catch (FlightBookingSystemException fex) {
                JOptionPane.showMessageDialog(this, "Error: " + fex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Flight ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        // Bookings
        } else if (ae.getSource() == bookingsIssue) {
            JOptionPane.showMessageDialog(this, "Not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);

        } else if (ae.getSource() == bookingsUpdate) {
            JOptionPane.showMessageDialog(this, "Not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);

        } else if (ae.getSource() == bookingsCancel) {
            JOptionPane.showMessageDialog(this, "Not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);

        // Customers
        } else if (ae.getSource() == custView) {
            String input = JOptionPane.showInputDialog("Enter Customer ID to view bookings:");
            if (input == null) return;

            try {
                int customerId = Integer.parseInt(input);
                Customer customer = fbs.getCustomerById(customerId);
                new ShowBookingsWindow(customer);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid Customer ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == custAdd) {
            new AddCustomerWindow(fbs);

        } else if (ae.getSource() == custDel) {
            // "Delete Customer"
            String input = JOptionPane.showInputDialog("Enter Customer ID to delete:");
            if (input == null) return;

            try {
                int customerId = Integer.parseInt(input);
                Command cmd = new DeleteCustomer(customerId);
                cmd.execute(fbs);
                JOptionPane.showMessageDialog(this, "Customer #" + customerId + " deleted (hidden).");
            } catch (FlightBookingSystemException fex) {
                JOptionPane.showMessageDialog(this, "Error: " + fex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Customer ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == custListAll) {
            // Show all customers
            new ListAllCustomersWindow(fbs);
        }
    }

    /**
     * Displays all non-deleted flights in a table.
     */
    public void displayFlights() {
        List<Flight> flightsList = fbs.getFlights();
        String[] columns = {"ID", "Flight No", "Origin", "Destination", "Departure Date", "Capacity", "Price"};
        Object[][] data = new Object[flightsList.size()][7];

        for (int i = 0; i < flightsList.size(); i++) {
            Flight flight = flightsList.get(i);
            data[i][0] = flight.getId();
            data[i][1] = flight.getFlightNumber();
            data[i][2] = flight.getOrigin();
            data[i][3] = flight.getDestination();
            data[i][4] = flight.getDepartureDate();
            data[i][5] = flight.getCapacity();
            data[i][6] = flight.getPrice();
        }

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().removeAll();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
