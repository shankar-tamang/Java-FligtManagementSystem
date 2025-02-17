package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.Snapshot;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

import java.io.IOException;

/**
 * Issues a new booking, factoring seat type and food option.
 */
public class AddBooking implements Command {
    private final int customerId;
    private final int flightId;
    private final SeatType seatType;
    private final FoodOption foodOption;

    public AddBooking(int customerId, int flightId, SeatType seatType, FoodOption foodOption) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.seatType = seatType;
        this.foodOption = foodOption;
    }

    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        Snapshot backup = FlightBookingSystemData.createSnapshot(fbs);

        try {
            Customer customer = fbs.getCustomerById(customerId);
            Flight flight = fbs.getFlightById(flightId);

            if (flight.isFull(seatType)) {
                throw new FlightBookingSystemException("No seats available in " + seatType + " class.");
            }

            double base = flight.getBasePrice();
            double seatMultiplier = 1.0;
            switch (seatType) {
                case BUSINESS: seatMultiplier = 1.5; break;
                case FIRST:    seatMultiplier = 2.0; break;
                default:       seatMultiplier = 1.0; break;
            }

            double finalPrice = base * seatMultiplier;

            Booking booking = new Booking(customer, flight, fbs.getSystemDate());
            booking.setSeatType(seatType);
            booking.setFoodOption(foodOption);
            booking.setBookingPrice(finalPrice);

            customer.addBooking(booking);
            flight.addPassenger(customer, seatType);

            FlightBookingSystemData.store(fbs);

            System.out.println("Booking added with seat=" + seatType + ", food=" + foodOption + ", price=$" + finalPrice);
        } catch (IOException ex) {
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Failed to store. Rolled back.\n" + ex.getMessage());
        } catch (Exception ex) {
            FlightBookingSystemData.restoreFromSnapshot(fbs, backup);
            throw new FlightBookingSystemException("Error: " + ex.getMessage());
        }
    }
}
