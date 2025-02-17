package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;

/**
 * A generic interface for loading and storing data related
 * to the <code>FlightBookingSystem</code>.
 * <p>
 * Implementations of <code>DataManager</code> deal with specific
 * aspects (e.g. flights, customers, bookings) and handle I/O with
 * the relevant text files.
 */
public interface DataManager {

    /**
     * Loads data from the relevant storage (text files, etc.) into the given
     * <code>FlightBookingSystem</code> instance.
     *
     * @param fbs the flight booking system to populate
     * @throws IOException if loading from the file fails
     */
    void loadData(FlightBookingSystem fbs) throws IOException;

    /**
     * Stores the current system data (e.g. flights, customers, bookings)
     * back into the relevant storage (text files).
     *
     * @param fbs the flight booking system whose data will be saved
     * @throws IOException if writing to the file fails
     */
    void storeData(FlightBookingSystem fbs) throws IOException;
}
