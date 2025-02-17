package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * This is a No-Operation Command (NoOpCommand).
 * It is used for commands like 'help' that only print information
 * but do not modify the system.
 */
public class NoOpCommand implements Command {
    @Override
    public void execute(FlightBookingSystem fbs) {
        // Does nothing; prevents null command errors
    }
}
