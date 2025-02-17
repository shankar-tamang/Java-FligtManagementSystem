package bcu.cmp5332.bookingsystem.main;

/**
 * <code>FlightBookingSystemException</code> is a custom exception used
 * to indicate errors or invalid operations within the flight booking system,
 * such as failed data storage or invalid booking actions.
 */
public class FlightBookingSystemException extends Exception {

    /**
     * Constructs a new <code>FlightBookingSystemException</code> with the specified detail message.
     *
     * @param message the detail message
     */
    public FlightBookingSystemException(String message) {
        super(message);
    }
}
