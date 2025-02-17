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

/**
 * A window that collects data for adding a new Flight with separate seat capacities
 * (economy, business, first) plus a base price.
 */
public class AddFlightWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final MainWindow mw;

    private JTextField flightNoText = new JTextField();
    private JTextField originText = new JTextField();
    private JTextField destinationText = new JTextField();
    private JTextField depDateText = new JTextField();

    private JTextField econCapText = new JTextField();
    private JTextField bizCapText = new JTextField();
    private JTextField firstCapText = new JTextField();

    private JTextField basePriceText = new JTextField();

    private JButton addBtn = new JButton("Add");
    private JButton cancelBtn = new JButton("Cancel");

    public AddFlightWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Add a New Flight");
        setSize(400, 400);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(9, 2));

        topPanel.add(new JLabel("Flight No: "));
        topPanel.add(flightNoText);
        topPanel.add(new JLabel("Origin: "));
        topPanel.add(originText);
        topPanel.add(new JLabel("Destination: "));
        topPanel.add(destinationText);
        topPanel.add(new JLabel("Departure Date (YYYY-MM-DD): "));
        topPanel.add(depDateText);

        topPanel.add(new JLabel("Economy Seats: "));
        topPanel.add(econCapText);
        topPanel.add(new JLabel("Business Seats: "));
        topPanel.add(bizCapText);
        topPanel.add(new JLabel("First Class Seats: "));
        topPanel.add(firstCapText);

        topPanel.add(new JLabel("Base Price: "));
        topPanel.add(basePriceText);

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
            String flightNumber = flightNoText.getText().trim();
            String origin = originText.getText().trim();
            String destination = destinationText.getText().trim();

            LocalDate departureDate;
            try {
                departureDate = LocalDate.parse(depDateText.getText().trim());
            } catch (DateTimeParseException dtpe) {
                throw new FlightBookingSystemException("Date must be in YYYY-MM-DD format");
            }

            int econCap;
            try {
                econCap = Integer.parseInt(econCapText.getText().trim());
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("Economy capacity must be an integer");
            }

            int bizCap;
            try {
                bizCap = Integer.parseInt(bizCapText.getText().trim());
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("Business capacity must be an integer");
            }

            int firstCap;
            try {
                firstCap = Integer.parseInt(firstCapText.getText().trim());
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("First capacity must be an integer");
            }

            double basePrice;
            try {
                basePrice = Double.parseDouble(basePriceText.getText().trim());
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("Base price must be a valid number");
            }

            // create and execute the AddFlight command with all 8 parameters
            Command addFlightCmd = new AddFlight(
                flightNumber, origin, destination, departureDate,
                econCap, bizCap, firstCap, basePrice
            );
            addFlightCmd.execute(mw.getFlightBookingSystem());

            // attempt auto-save
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
