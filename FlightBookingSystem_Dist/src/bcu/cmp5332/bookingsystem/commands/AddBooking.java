package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

import java.io.IOException;

/**
 * A command that creates (issues) a new Booking, potentially specifying seat type
 * and food option. It verifies seat capacity before proceeding.
 */
public class AddBooking implements Command {
    private final int customerId;
    private final int flightId;
    private final SeatType seatType;
    private final FoodOption foodOption;

    /**
     * Constructs an <code>AddBooking</code> command with optional seat type
     * and food option. If either is null, a default might be used or handled.
     *
     * @param customerId the ID of the customer making the booking
     * @param flightId   the ID of the flight
     * @param seatType   the seat type (ECONOMY, BUSINESS, FIRST) or null
     * @param foodOption the food option (NO_MEAL, VEGETARIAN, etc.) or null
     */
    public AddBooking(int customerId, int flightId, SeatType seatType, FoodOption foodOption) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.seatType = seatType;
        this.foodOption = foodOption;
    }

    /**
     * Executes the booking creation, adding the booking to both the
     * <code>Customer</code> and the <code>Flight</code>. Also sets booking price
     * based on seat type (and any other dynamic logic you want).
     *
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if the customer or flight is invalid,
     *                                      the flight is full, or storing fails
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        // 1) Take snapshot for rollback
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        try {
            // 2) Validate
            Customer customer = fbs.getCustomerById(customerId);
            Flight flight = fbs.getFlightById(flightId);

            if (flight.isFull(seatType)) {
                throw new FlightBookingSystemException("No seats available in " + seatType + " class.");
            }

            // Example simple seat-based pricing
            double base = flight.getBasePrice();
            double seatMultiplier = 1.0;
            if (seatType == SeatType.BUSINESS) {
                seatMultiplier = 1.5;
            } else if (seatType == SeatType.FIRST) {
                seatMultiplier = 2.0;
            }
            double finalPrice = base * seatMultiplier;

            // 3) Create the booking object
            Booking booking = new Booking(customer, flight, fbs.getSystemDate());
            booking.setSeatType(seatType);
            booking.setFoodOption(foodOption);
            booking.setBookingPrice(finalPrice);

            // 4) Link booking with customer/flight
            customer.addBooking(booking);
            flight.addPassenger(customer, seatType);

            // 5) Attempt to store updated system
            FlightBookingSystemData.store(fbs);

            System.out.println("Booking added with seat=" + seatType + ", food=" + foodOption + ", price=$" + finalPrice);

        } catch (IOException ex) {
            // rollback
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store. Rolled back.\n" + ex.getMessage());

        } catch (Exception ex) {
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Error: " + ex.getMessage());
        }
    }
}
