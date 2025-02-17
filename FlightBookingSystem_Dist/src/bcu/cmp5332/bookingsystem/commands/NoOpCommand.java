package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * A no-operation (NoOp) command, typically used for commands like 'help'
 * that only print information and do not modify the system.
 */
public class NoOpCommand implements Command {

    /**
     * Does nothing. Prevents null command errors in the CLI parser.
     *
     * @param fbs the flight booking system (not used)
     */
    @Override
    public void execute(FlightBookingSystem fbs) {
        // no-op
    }
}
