package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Manages loading and storing flight data to/from a text file.
 * <p>
 * Supports flights with separate seat capacities (economy/business/first)
 * and a base price, as well as a soft-deleted flag.
 */
public class FlightDataManager implements DataManager {
    private static final String FILE_NAME = "resources/data/flights.txt";
    private static final String SEPARATOR = "::";

    /**
     * Loads flight data from <code>flights.txt</code>, creating new
     * <code>Flight</code> objects and adding them to the system.
     *
     * @param fbs the flight booking system to populate
     * @throws IOException if reading the file fails
     */
    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(new FileReader(file))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(SEPARATOR);
                // Data (10 fields total):
                // 0: id
                // 1: flightNumber
                // 2: origin
                // 3: destination
                // 4: departureDate
                // 5: econCap
                // 6: bizCap
                // 7: firstCap
                // 8: basePrice
                // 9: deleted
                int id = Integer.parseInt(data[0]);
                String flightNumber = data[1];
                String origin = data[2];
                String destination = data[3];
                LocalDate departureDate = LocalDate.parse(data[4]);
                int econCap = Integer.parseInt(data[5]);
                int bizCap = Integer.parseInt(data[6]);
                int firstCap = Integer.parseInt(data[7]);
                double basePrice = Double.parseDouble(data[8]);
                boolean deleted = Boolean.parseBoolean(data[9]);

                Flight flight = new Flight(
                    id,
                    flightNumber,
                    origin,
                    destination,
                    departureDate,
                    econCap,
                    bizCap,
                    firstCap,
                    basePrice,
                    deleted
                );
                fbs.addFlight(flight);
            }
        }
    }

    /**
     * Stores flight data (including seat capacities and deletion status)
     * into <code>flights.txt</code>.
     *
     * @param fbs the flight booking system from which to gather flight data
     * @throws IOException if writing the file fails
     */
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Flight flight : fbs.getAllFlights()) {
                writer.println(
                    flight.getId() + SEPARATOR +
                    flight.getFlightNumber() + SEPARATOR +
                    flight.getOrigin() + SEPARATOR +
                    flight.getDestination() + SEPARATOR +
                    flight.getDepartureDate() + SEPARATOR +
                    flight.getEconCapacity() + SEPARATOR +
                    flight.getBusinessCapacity() + SEPARATOR +
                    flight.getFirstCapacity() + SEPARATOR +
                    flight.getBasePrice() + SEPARATOR +
                    flight.isDeleted()
                );
            }
        }
    }
}
