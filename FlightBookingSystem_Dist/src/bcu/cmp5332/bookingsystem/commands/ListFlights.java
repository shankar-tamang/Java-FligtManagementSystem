package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

/**
 * A command that lists all active (non-deleted) flights in the system.
 */
public class ListFlights implements Command {

    /**
     * Executes the listing of flights, printing each flight's short details
     * (e.g. flight number, origin, destination, etc.).
     *
     * @param flightBookingSystem the system from which to retrieve flights
     * @throws FlightBookingSystemException not thrown here
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        List<Flight> flights = flightBookingSystem.getFlights();
        for (Flight flight : flights) {
            System.out.println(flight.getDetailsShort());
        }
        System.out.println(flights.size() + " flight(s)");
    }
}
