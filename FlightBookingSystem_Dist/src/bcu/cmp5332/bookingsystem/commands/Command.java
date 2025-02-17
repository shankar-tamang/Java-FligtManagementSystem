package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Represents an executable command that operates on the {@link FlightBookingSystem}.
 * <p>
 * Each command typically modifies or queries the system (e.g., adding flights, deleting customers).
 * The {@code HELP_MESSAGE} provides a brief reference of all supported CLI commands.
 */
public interface Command {

    /**
     * A static help message listing the core commands available in the CLI.
     */
    public static final String HELP_MESSAGE = "Commands:\n"
        + "\tlistflights                               print all flights\n"
        + "\tlistcustomers                             print all customers\n"
        + "\taddflight                                 add a new flight\n"
        + "\taddcustomer                               add a new customer\n"
        + "\tshowflight [flight id]                    show flight details\n"
        + "\tshowcustomer [customer id]                show customer details\n"
        + "\taddbooking [customer id] [flight id]      add a new booking\n"
        + "\tcancelbooking [customer id] [flight id]   cancel a booking\n"
        + "\teditbooking [booking id] [flight id]      update a booking\n"
        + "\tloadgui                                   loads the GUI version of the app\n"
        + "\thelp                                      prints this help message\n"
        + "\texit                                      exits the program";

    /**
     * Executes this command on the given {@link FlightBookingSystem}.
     *
     * @param flightBookingSystem the system on which to run this command
     * @throws FlightBookingSystemException if an error occurs during execution
     */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException;
    
}
