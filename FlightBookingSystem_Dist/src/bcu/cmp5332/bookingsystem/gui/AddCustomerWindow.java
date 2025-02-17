package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AddCustomerWindow extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JTextField nameText = new JTextField();
    private JTextField phoneText = new JTextField();
    private JTextField emailText = new JTextField();
    private JButton addBtn = new JButton("Add");
    private JButton cancelBtn = new JButton("Cancel");
    private FlightBookingSystem fbs;

    public AddCustomerWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        setTitle("Add Customer");
        setSize(300, 200);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Name:"));
        add(nameText);
        add(new JLabel("Phone:"));
        add(phoneText);
        add(new JLabel("Email:"));
        add(emailText);
        add(addBtn);
        add(cancelBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(e -> setVisible(false));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String name = nameText.getText().trim();
            String phone = phoneText.getText().trim();
            String email = emailText.getText().trim();

            Command addCustomerCmd = new AddCustomer(name, phone, email);
            addCustomerCmd.execute(fbs);

            // Attempt auto-save
            try {
                FlightBookingSystemData.store(fbs);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this,
                    "Customer added, but saving to file failed.\n" + ioe.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(this, "Customer added successfully!");
            setVisible(false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
