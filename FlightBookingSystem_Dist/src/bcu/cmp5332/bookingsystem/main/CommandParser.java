package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.*;
import bcu.cmp5332.bookingsystem.gui.MainWindow;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CommandParser {
    public static Command parse(String input) throws Exception {
        Scanner scanner = new Scanner(input);
        if (!scanner.hasNext()) {
            throw new IllegalArgumentException("Invalid command.");
        }

        String command = scanner.next().toLowerCase();
        switch (command) {
            case "addcustomer":
                if (!scanner.hasNext()) {
                    throw new IllegalArgumentException("Usage: addcustomer [name] [phone] [email]");
                }
                String name = scanner.next();
                String phone = scanner.next();
                String email = scanner.next();
                return new AddCustomer(name, phone, email);

            case "listcustomers":
                return new ListCustomers();

            case "addflight":
                if (!scanner.hasNext()) {
                    throw new IllegalArgumentException(
                        "Usage: addflight [flightNumber] [origin] [destination] [date] [capacity] [price]"
                    );
                }
                String flightNumber = scanner.next();
                String origin = scanner.next();
                String destination = scanner.next();
                String dateStr = scanner.next();
                int capacity = scanner.nextInt();
                double price = scanner.nextDouble();

                try {
                    LocalDate departureDate = LocalDate.parse(dateStr);
                    return new AddFlight(flightNumber, origin, destination, departureDate, capacity, price);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD.");
                }

            case "listflights":
                return new ListFlights();

            case "addbooking":
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Usage: addbooking [customer id] [flight id]");
                }
                int customerId = scanner.nextInt();
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Usage: addbooking [customer id] [flight id]");
                }
                int flightId = scanner.nextInt();
                return new AddBooking(customerId, flightId);

            case "cancelbooking":
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Usage: cancelbooking [customer id] [flight id]");
                }
                int custId = scanner.nextInt();
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Usage: cancelbooking [customer id] [flight id]");
                }
                int fltId = scanner.nextInt();
                return new CancelBooking(custId, fltId);

            case "showflight":
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Usage: showflight [flight id]");
                }
                int showFlightId = scanner.nextInt();
                return new ShowFlight(showFlightId);

            case "showcustomer":
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Usage: showcustomer [customer id]");
                }
                int showCustId = scanner.nextInt();
                return new ShowCustomer(showCustId);

            case "editbooking":
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Usage: editbooking [booking id] [new flight id]");
                }
                int bookingId = scanner.nextInt();
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Usage: editbooking [booking id] [new flight id]");
                }
                int newFlightId = scanner.nextInt();
                return new EditBooking(bookingId, newFlightId);

            // ---------------------------
            // NEW: LISTBOOKINGS
            // ---------------------------
            case "listbookings":
                if (!scanner.hasNext()) {
                    // No subcommand => list ALL
                    return new ListBookings();
                } else {
                    String sub = scanner.next().toLowerCase();
                    switch (sub) {
                        case "flight":
                            if (!scanner.hasNextInt()) {
                                throw new IllegalArgumentException("Usage: listbookings flight [flight id]");
                            }
                            int flId = scanner.nextInt();
                            return new ListBookingsFlight(flId);

                        case "date":
                            if (!scanner.hasNext()) {
                                throw new IllegalArgumentException("Usage: listbookings date [YYYY-MM-DD]");
                            }
                            String dateParam = scanner.next();
                            LocalDate parsedDate;
                            try {
                                parsedDate = LocalDate.parse(dateParam);
                            } catch (DateTimeParseException e) {
                                throw new IllegalArgumentException("Invalid date. Use YYYY-MM-DD.");
                            }
                            return new ListBookingsDate(parsedDate);

                        case "customer":
                            if (!scanner.hasNextInt()) {
                                throw new IllegalArgumentException("Usage: listbookings customer [customer id]");
                            }
                            int custID = scanner.nextInt();
                            return new ListBookingsCustomer(custID);

                        default:
                            throw new IllegalArgumentException("Unknown listbookings subcommand: " + sub);
                    }
                }

            case "help":
                printHelp();
                return new NoOpCommand();

            case "loadgui":
                return new LoadGUI();

            case "exit":
                System.exit(0);

            default:
                throw new IllegalArgumentException("Unknown command: " + command);
        }
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("listflights                               print all flights");
        System.out.println("listcustomers                             print all customers");
        System.out.println("addflight [flightNumber] [origin] [destination] [date] [capacity] [price]");
        System.out.println("addcustomer [name] [phone] [email]");
        System.out.println("showflight [flight id]                    show flight details");
        System.out.println("showcustomer [customer id]                show customer details");
        System.out.println("addbooking [customer id] [flight id]      add a new booking");
        System.out.println("cancelbooking [customer id] [flight id]   cancel a booking");
        System.out.println("editbooking [booking id] [new flight id]  update a booking");
        System.out.println("listbookings                              list all bookings");
        System.out.println("   listbookings flight [flight id]        list bookings for a flight");
        System.out.println("   listbookings date [yyyy-mm-dd]         list bookings by booking date");
        System.out.println("   listbookings customer [cust id]        list bookings for a particular customer");
        System.out.println("loadgui                                   loads the GUI version of the app");
        System.out.println("help                                      prints this help message");
        System.out.println("exit                                      exits the program");
    }
}
