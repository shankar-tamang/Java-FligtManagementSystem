package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.gui.MainWindow;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

/**
 * A command that launches the GUI version of the Flight Booking System
 * by creating a new {@link MainWindow}.
 */
public class LoadGUI implements Command {

    /**
     * Executes the command, instantiating a {@link MainWindow} to show the GUI.
     *
     * @param flightBookingSystem the system that the GUI should operate on
     * @throws FlightBookingSystemException never thrown here
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        new MainWindow(flightBookingSystem);
    }
}
