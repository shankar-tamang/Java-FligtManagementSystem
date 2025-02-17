package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.EditBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.SeatType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * A window that allows updating a booking to a new flight, optionally changing the seat type.
 */
public class UpdateBookingWindow extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final FlightBookingSystem fbs;

    private JTextField bookingIdField = new JTextField();
    private JTextField newFlightIdField = new JTextField();

    // Seat type combo, if we want to let the user pick a new seat class
    private JComboBox<String> seatTypeCombo = new JComboBox<>(new String[] {
        "ECONOMY","BUSINESS","FIRST"
    });

    private JButton updateBtn = new JButton("Update");
    private JButton cancelBtn = new JButton("Cancel");

    /**
     * Constructs the window for updating an existing booking, including potential seat-type changes.
     *
     * @param fbs the flight booking system
     */
    public UpdateBookingWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        setTitle("Update Booking");
        setSize(350, 200);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Booking ID:"));
        add(bookingIdField);

        add(new JLabel("New Flight ID:"));
        add(newFlightIdField);

        add(new JLabel("New Seat Type:"));
        add(seatTypeCombo);

        add(updateBtn);
        add(cancelBtn);

        updateBtn.addActionListener(this);
        cancelBtn.addActionListener(e -> setVisible(false));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateBtn) {
            try {
                int bookingId = Integer.parseInt(bookingIdField.getText().trim());
                int newFltId = Integer.parseInt(newFlightIdField.getText().trim());
                // Seat type changes if user picks something new
                SeatType newSeatType = SeatType.valueOf((String) seatTypeCombo.getSelectedItem());

                // Check if the new flight has capacity for that seat type
                Flight newFlight = fbs.getFlightById(newFltId);
                if (newFlight.isFull(newSeatType)) {
                    JOptionPane.showMessageDialog(this,
                        "Cannot update. New flight is full in " + newSeatType + " class!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Edit the booking with new flight and seat type
                EditBooking cmd = new EditBooking(bookingId, newFltId, newSeatType);
                cmd.execute(fbs);

                // Auto-save
                try {
                    bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.store(fbs);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Booking updated, but saving failed: " + ex.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(this, "Booking updated successfully!");
                setVisible(false);

            } catch (NumberFormatException nfex) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
