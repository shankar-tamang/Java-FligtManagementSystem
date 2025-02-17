package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.*;
import bcu.cmp5332.bookingsystem.gui.MainWindow;
import bcu.cmp5332.bookingsystem.model.SeatType; // If used anywhere
import bcu.cmp5332.bookingsystem.model.FoodOption; // If used anywhere

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
                if (!scanner.hasNext())
                    throw new IllegalArgumentException("Usage: addcustomer [name] [phone] [email]");
                String name = scanner.next();
                String phone = scanner.next();
                String email = scanner.next();
                return new AddCustomer(name, phone, email);

            case "listcustomers":
                return new ListCustomers();

            /**
             * Updated addflight to expect:
             * addflight [flightNumber] [origin] [destination] [date] [econCap] [bizCap] [firstCap] [basePrice]
             */
            case "addflight":
                if (!scanner.hasNext())
                    throw new IllegalArgumentException(
                        "Usage: addflight [flightNumber] [origin] [destination] [date] [econCap] [bizCap] [firstCap] [basePrice]");
                String flightNumber = scanner.next();
                String origin = scanner.next();
                String destination = scanner.next();
                String dateStr = scanner.next();

                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Usage: addflight ... [econCap] [bizCap] [firstCap] [basePrice]");
                }
                int econCap = scanner.nextInt();

                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Usage: addflight ... [econCap] [bizCap] [firstCap] [basePrice]");
                }
                int bizCap = scanner.nextInt();

                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Usage: addflight ... [econCap] [bizCap] [firstCap] [basePrice]");
                }
                int firstCap = scanner.nextInt();

                if (!scanner.hasNextDouble()) {
                    throw new IllegalArgumentException("Usage: addflight ... [econCap] [bizCap] [firstCap] [basePrice]");
                }
                double basePrice = scanner.nextDouble();

                LocalDate departureDate;
                try {
                    departureDate = LocalDate.parse(dateStr);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD.");
                }
                return new AddFlight(flightNumber, origin, destination,
                    departureDate, econCap, bizCap, firstCap, basePrice);

            case "listflights":
                return new ListFlights();

            case "deleteflight":
                if (!scanner.hasNextInt())
                    throw new IllegalArgumentException("Usage: deleteflight [flight id]");
                int flightId = scanner.nextInt();
                return new DeleteFlight(flightId);

            case "deletecustomer":
                if (!scanner.hasNextInt())
                    throw new IllegalArgumentException("Usage: deletecustomer [customer id]");
                int customerId = scanner.nextInt();
                return new DeleteCustomer(customerId);

            case "showflight":
                if (!scanner.hasNextInt())
                    throw new IllegalArgumentException("Usage: showflight [flight id]");
                int showFlightId = scanner.nextInt();
                return new ShowFlight(showFlightId);

            case "showcustomer":
                if (!scanner.hasNextInt())
                    throw new IllegalArgumentException("Usage: showcustomer [customer id]");
                int showCustId = scanner.nextInt();
                return new ShowCustomer(showCustId);

            /**
             * Updated addbooking usage if needed. For now, just ignoring seat/food if not used in CLI.
             */
            case "addbooking":
                if (!scanner.hasNextInt())
                    throw new IllegalArgumentException("Usage: addbooking [customer id] [flight id]");
                int custIdForBooking = scanner.nextInt();
                if (!scanner.hasNextInt())
                    throw new IllegalArgumentException("Usage: addbooking [customer id] [flight id]");
                int flightIdForBooking = scanner.nextInt();

                // We pass null seatType, null foodOption if not specifying from CLI.
                return new AddBooking(custIdForBooking, flightIdForBooking, null, null);

            case "cancelbooking":
                if (!scanner.hasNextInt())
                    throw new IllegalArgumentException("Usage: cancelbooking [customer id] [flight id]");
                int custIdForCancel = scanner.nextInt();
                if (!scanner.hasNextInt())
                    throw new IllegalArgumentException("Usage: cancelbooking [customer id] [flight id]");
                int flightIdForCancel = scanner.nextInt();
                return new CancelBooking(custIdForCancel, flightIdForCancel);

            case "editbooking":
                if (!scanner.hasNextInt())
                    throw new IllegalArgumentException("Usage: editbooking [booking id] [new flight id]");
                int bookingId = scanner.nextInt();
                if (!scanner.hasNextInt())
                    throw new IllegalArgumentException("Usage: editbooking [booking id] [new flight id]");
                int newFlightId = scanner.nextInt();
                return new EditBooking(bookingId, newFlightId);

            case "listbookings":
                if (!scanner.hasNext()) {
                    // No subcommand provided, list all bookings
                    return new ListBookings();
                } else {
                    String sub = scanner.next().toLowerCase();
                    switch (sub) {
                        case "flight":
                            if (!scanner.hasNextInt())
                                throw new IllegalArgumentException("Usage: listbookings flight [flight id]");
                            int flightIdForBookings = scanner.nextInt();
                            return new ListBookingsFlight(flightIdForBookings);
                        case "date":
                            if (!scanner.hasNext())
                                throw new IllegalArgumentException("Usage: listbookings date [YYYY-MM-DD]");
                            String dateParam = scanner.next();
                            LocalDate parsedDate;
                            try {
                                parsedDate = LocalDate.parse(dateParam);
                            } catch (DateTimeParseException e) {
                                throw new IllegalArgumentException("Invalid date. Use YYYY-MM-DD.");
                            }
                            return new ListBookingsDate(parsedDate);
                        case "customer":
                            if (!scanner.hasNextInt())
                                throw new IllegalArgumentException("Usage: listbookings customer [customer id]");
                            int custIdForBookings = scanner.nextInt();
                            return new ListBookingsCustomer(custIdForBookings);
                        default:
                            throw new IllegalArgumentException("Unknown listbookings subcommand: " + sub);
                    }
                }

            case "loadgui":
                return new LoadGUI();

            case "help":
                printHelp();
                return new NoOpCommand();

            case "exit":
                System.exit(0);

            default:
                throw new IllegalArgumentException("Unknown command: " + command);
        }
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("listflights                               print all upcoming flights (only future flights are shown)");
        System.out.println("listcustomers                             print all customers");
        System.out.println("addflight [flightNumber] [origin] [destination] [date] [econCap] [bizCap] [firstCap] [basePrice]");
        System.out.println("addcustomer [name] [phone] [email]");
        System.out.println("deleteflight [flight id]                  soft-delete a flight");
        System.out.println("deletecustomer [customer id]              soft-delete a customer");
        System.out.println("showflight [flight id]                    show detailed flight information");
        System.out.println("showcustomer [customer id]                show detailed customer information");
        System.out.println("addbooking [customer id] [flight id]      add a new booking (dynamic pricing applied)");
        System.out.println("cancelbooking [customer id] [flight id]   cancel a booking (cancellation fee applied)");
        System.out.println("editbooking [booking id] [new flight id]  update a booking (rebooking fee applied)");
        System.out.println("listbookings                              list all bookings");
        System.out.println("   listbookings flight [flight id]        list bookings for a specific flight");
        System.out.println("   listbookings date [yyyy-mm-dd]         list bookings made on a specific date");
        System.out.println("   listbookings customer [cust id]        list bookings for a specific customer");
        System.out.println("loadgui                                   loads the GUI version of the app");
        System.out.println("help                                      prints this help message");
        System.out.println("exit                                      exits the program");
    }
}
