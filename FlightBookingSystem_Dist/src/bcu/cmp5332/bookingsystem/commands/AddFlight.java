package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Creates (adds) a new Flight with separate seat capacities for Economy, Business, and First,
 * along with a base price. Assumes the Flight constructor has 10 parameters:
 *   (id, flightNumber, origin, destination, departureDate,
 *    econCap, bizCap, firstCap, basePrice, deleted).
 */
public class AddFlight implements Command {

    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final int econCap;      // economy capacity
    private final int bizCap;       // business capacity
    private final int firstCap;     // first class capacity
    private final double basePrice; // base price for economy

    /**
     * Constructs an AddFlight command that will create a new flight with separate seat classes.
     *
     * @param flightNumber the flight number (e.g. "BA123")
     * @param origin the flight origin (e.g. "LHR")
     * @param destination the flight destination (e.g. "JFK")
     * @param departureDate the date of departure
     * @param econCap the number of economy seats
     * @param bizCap the number of business class seats
     * @param firstCap the number of first class seats
     * @param basePrice the base price (for economy seats)
     */
    public AddFlight(String flightNumber, String origin, String destination,
                     LocalDate departureDate, int econCap, int bizCap,
                     int firstCap, double basePrice) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.econCap = econCap;
        this.bizCap = bizCap;
        this.firstCap = firstCap;
        this.basePrice = basePrice;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // 1) Take a snapshot before making changes (for rollback if needed).
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        // 2) Create the new Flight in memory
        int newId = fbs.getAllFlights().size() + 1;

        // Matches the Flight(...) constructor that has 10 parameters.
        Flight flight = new Flight(
            newId,
            flightNumber,
            origin,
            destination,
            departureDate,
            econCap,
            bizCap,
            firstCap,
            basePrice,
            false // not deleted
        );

        fbs.addFlight(flight);

        // 3) Attempt to store the updated system
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            // 4) Rollback if storing fails
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store data. Changes rolled back.\n" + ex.getMessage());
        }

        System.out.println("Flight added successfully: " + flightNumber);
    }
}
