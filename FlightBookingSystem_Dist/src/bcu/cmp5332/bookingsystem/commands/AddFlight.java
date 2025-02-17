package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;
import java.time.LocalDate;

/**
 * A command to create a new Flight with separate seat capacities (economy, business, first)
 * plus a base price. The flight is initially marked as not deleted.
 */
public class AddFlight implements Command {

    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final int econCap;
    private final int bizCap;
    private final int firstCap;
    private final double basePrice;

    /**
     * Constructs an <code>AddFlight</code> command.
     *
     * @param flightNumber  the flight number (e.g., "BA123")
     * @param origin        the origin airport/code
     * @param destination   the destination airport/code
     * @param departureDate the date the flight departs
     * @param econCap       economy seat capacity
     * @param bizCap        business seat capacity
     * @param firstCap      first class seat capacity
     * @param basePrice     the base price for economy
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

    /**
     * Executes the addition of a new flight, storing it in the system. If storing
     * fails, the system is rolled back to a previous snapshot.
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if saving fails or other errors occur
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // 1) Take a snapshot
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        // 2) Create a new flight with the next available ID
        int newId = fbs.getAllFlights().size() + 1;
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
            false
        );

        fbs.addFlight(flight);

        // 3) Attempt to store
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            // 4) Rollback on failure
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store data. Changes rolled back.\n" + ex.getMessage());
        }

        System.out.println("Flight added successfully: " + flightNumber);
    }
}
