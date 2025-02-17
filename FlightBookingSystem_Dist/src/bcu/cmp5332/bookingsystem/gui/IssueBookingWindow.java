package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class IssueBookingWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private FlightBookingSystem fbs;

    private JTextField customerIdField = new JTextField();
    private JTextField flightIdField = new JTextField();
    private JButton issueBtn = new JButton("Issue");
    private JButton cancelBtn = new JButton("Cancel");

    public IssueBookingWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        setTitle("Issue Booking");
        setSize(300, 150);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Customer ID:"));
        add(customerIdField);
        add(new JLabel("Flight ID:"));
        add(flightIdField);
        add(issueBtn);
        add(cancelBtn);

        issueBtn.addActionListener(this);
        cancelBtn.addActionListener(e -> setVisible(false));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int custId = Integer.parseInt(customerIdField.getText());
            int fltId = Integer.parseInt(flightIdField.getText());

            // Command
            AddBooking cmd = new AddBooking(custId, fltId);
            cmd.execute(fbs);

            // Attempt auto-save
            try {
                bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.store(fbs);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Booking issued, but saving to file failed: " + ex.getMessage(),
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(this, "Booking issued successfully!");
            setVisible(false);
        } catch (NumberFormatException nfex) {
            JOptionPane.showMessageDialog(this, "Invalid ID(s).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
