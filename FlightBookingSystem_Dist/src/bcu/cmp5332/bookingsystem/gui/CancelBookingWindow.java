package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * A GUI window that allows a user to cancel an existing booking by
 * specifying the customer and flight IDs.
 */
public class CancelBookingWindow extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final FlightBookingSystem fbs;

    private JTextField customerIdField = new JTextField();
    private JTextField flightIdField = new JTextField();
    private JButton cancelBtn = new JButton("Cancel Booking");
    private JButton closeBtn = new JButton("Close");

    /**
     * Constructs the window for canceling a booking.
     *
     * @param fbs the flight booking system instance
     */
    public CancelBookingWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        setTitle("Cancel Booking");
        setSize(300, 150);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Customer ID:"));
        add(customerIdField);
        add(new JLabel("Flight ID:"));
        add(flightIdField);
        add(cancelBtn);
        add(closeBtn);

        cancelBtn.addActionListener(this);
        closeBtn.addActionListener(e -> setVisible(false));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this booking?",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // user chose NO
            }

            try {
                int custId = Integer.parseInt(customerIdField.getText());
                int fltId = Integer.parseInt(flightIdField.getText());

                CancelBooking cmd = new CancelBooking(custId, fltId);
                cmd.execute(fbs);

                try {
                    bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.store(fbs);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Booking canceled, but saving failed: " + ex.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(this, "Booking canceled successfully!");
                setVisible(false);

            } catch (NumberFormatException nfex) {
                JOptionPane.showMessageDialog(this, "Invalid IDs.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
