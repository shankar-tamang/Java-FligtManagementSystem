package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.EditBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UpdateBookingWindow extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private FlightBookingSystem fbs;

    private JTextField bookingIdField = new JTextField();
    private JTextField newFlightIdField = new JTextField();
    private JButton updateBtn = new JButton("Update");
    private JButton cancelBtn = new JButton("Cancel");

    public UpdateBookingWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        setTitle("Update Booking");
        setSize(300, 150);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Booking ID:"));
        add(bookingIdField);
        add(new JLabel("New Flight ID:"));
        add(newFlightIdField);
        add(updateBtn);
        add(cancelBtn);

        updateBtn.addActionListener(this);
        cancelBtn.addActionListener(e -> setVisible(false));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            int newFltId = Integer.parseInt(newFlightIdField.getText());

            EditBooking cmd = new EditBooking(bookingId, newFltId);
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
