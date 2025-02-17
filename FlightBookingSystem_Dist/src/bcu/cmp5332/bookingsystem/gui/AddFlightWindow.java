package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddFlightWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private MainWindow mw;

    private JTextField flightNoText = new JTextField();
    private JTextField originText = new JTextField();
    private JTextField destinationText = new JTextField();
    private JTextField depDateText = new JTextField();
    private JTextField capacityText = new JTextField();
    private JTextField priceText = new JTextField();

    private JButton addBtn = new JButton("Add");
    private JButton cancelBtn = new JButton("Cancel");

    public AddFlightWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Add a New Flight");
        setSize(400, 300);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(7, 2));
        topPanel.add(new JLabel("Flight No: "));
        topPanel.add(flightNoText);
        topPanel.add(new JLabel("Origin: "));
        topPanel.add(originText);
        topPanel.add(new JLabel("Destination: "));
        topPanel.add(destinationText);
        topPanel.add(new JLabel("Departure Date (YYYY-MM-DD): "));
        topPanel.add(depDateText);
        topPanel.add(new JLabel("Capacity: "));
        topPanel.add(capacityText);
        topPanel.add(new JLabel("Price: "));
        topPanel.add(priceText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(addBtn);
        bottomPanel.add(cancelBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        getContentPane().add(topPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            addFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
        }
    }

    private void addFlight() {
        try {
            String flightNumber = flightNoText.getText();
            String origin = originText.getText();
            String destination = destinationText.getText();

            LocalDate departureDate;
            try {
                departureDate = LocalDate.parse(depDateText.getText());
            } catch (DateTimeParseException dtpe) {
                throw new FlightBookingSystemException("Date must be in YYYY-MM-DD format");
            }

            int capacity;
            try {
                capacity = Integer.parseInt(capacityText.getText());
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("Capacity must be an integer");
            }

            double price;
            try {
                price = Double.parseDouble(priceText.getText());
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("Price must be a valid number");
            }

            // Create and execute the AddFlight command with all required parameters
            Command addFlightCmd = new AddFlight(flightNumber, origin, destination, departureDate, capacity, price);
            addFlightCmd.execute(mw.getFlightBookingSystem());

            // Attempt auto-save
            try {
                FlightBookingSystemData.store(mw.getFlightBookingSystem());
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this,
                    "Flight added, but saving to file failed.\n" + ioe.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
            }

            // Refresh the flight list in MainWindow
            mw.displayFlights();
            // Close this window
            this.setVisible(false);

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
