package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * A command that prints the static help message defined in {@link Command#HELP_MESSAGE}.
 * It does not modify the system's state in any way.
 */
public class Help implements Command {

    /**
     * Prints the help message to standard output.
     *
     * @param flightBookingSystem unused parameter in this command
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) {
        System.out.println(Command.HELP_MESSAGE);
    }
}
