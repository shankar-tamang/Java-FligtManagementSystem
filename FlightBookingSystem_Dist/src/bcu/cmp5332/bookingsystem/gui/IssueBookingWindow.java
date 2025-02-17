package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.SeatType;
import bcu.cmp5332.bookingsystem.model.FoodOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class IssueBookingWindow extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final FlightBookingSystem fbs;

    private JTextField customerIdField = new JTextField();
    private JTextField flightIdField = new JTextField();

    // seat type & food combos
    private JComboBox<String> seatTypeCombo = new JComboBox<>(new String[] {
        "ECONOMY","BUSINESS","FIRST"
    });
    private JComboBox<String> foodCombo = new JComboBox<>(new String[] {
        "NO_MEAL","VEGETARIAN","CHICKEN","BEEF"
    });

    private JButton issueBtn = new JButton("Issue");
    private JButton cancelBtn = new JButton("Cancel");

    public IssueBookingWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        setTitle("Issue Booking");
        setSize(400, 250);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Customer ID:"));
        add(customerIdField);

        add(new JLabel("Flight ID:"));
        add(flightIdField);

        add(new JLabel("Seat Type:"));
        add(seatTypeCombo);

        add(new JLabel("Food Option:"));
        add(foodCombo);

        add(issueBtn);
        add(cancelBtn);

        issueBtn.addActionListener(this);
        cancelBtn.addActionListener(e -> setVisible(false));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == issueBtn) {
            try {
                int custId = Integer.parseInt(customerIdField.getText());
                int fltId = Integer.parseInt(flightIdField.getText());
                SeatType seatType = SeatType.valueOf((String) seatTypeCombo.getSelectedItem());
                FoodOption foodOpt = FoodOption.valueOf((String) foodCombo.getSelectedItem());

                // check if flight is full
                Flight flight = fbs.getFlightById(fltId);
                if (flight.isFull(seatType)) {
                    JOptionPane.showMessageDialog(this,
                        "Cannot issue booking. " + seatType + " seats are full!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                AddBooking cmd = new AddBooking(custId, fltId, seatType, foodOpt);
                cmd.execute(fbs);

                try {
                    bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.store(fbs);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Booking issued, but saving failed: " + ex.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(this, "Booking issued successfully!");
                setVisible(false);

            } catch (NumberFormatException nfex) {
                JOptionPane.showMessageDialog(this, "Invalid IDs.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
