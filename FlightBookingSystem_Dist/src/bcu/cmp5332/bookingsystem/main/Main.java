package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The entry point of the Flight Booking System console application.
 * <p>
 * This class loads the system data, starts a console loop for reading commands,
 * and exits upon user request or error. The current state is saved upon exit.
 */
public class Main {

    /**
     * Main method for running the Flight Booking System in console mode.
     * Loads data, reads commands from stdin, and saves upon exit.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        FlightBookingSystem fbs;

        try {
            // Load system state
            fbs = FlightBookingSystemData.load();
            System.out.println("Flight Booking System loaded successfully.");
        } catch (IOException ex) {
            System.err.println("Error loading system data: " + ex.getMessage());
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        System.out.println("Welcome to the Flight Booking System!");
        System.out.println("Type 'help' to see the list of available commands.");

        try {
            while (true) {
                System.out.print("> ");
                input = reader.readLine().trim();

                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                try {
                    Command command = CommandParser.parse(input);
                    command.execute(fbs);
                } catch (Exception ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
            }

            // Save system state
            FlightBookingSystemData.store(fbs);
            System.out.println("System state saved. Goodbye!");

        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
