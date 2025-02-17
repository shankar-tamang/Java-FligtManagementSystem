package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Manages loading and storing booking data to/from a text file,
 * including seat type, food option, and any fees or dynamic pricing.
 */
public class BookingDataManager implements DataManager {

    private static final String FILE_NAME = "resources/data/bookings.txt";
    private static final String SEPARATOR = "::";

    /**
     * Loads booking data from <code>bookings.txt</code>, including seat type,
     * food option, booking price, and fees. Creates and links <code>Booking</code>
     * objects to the appropriate <code>Customer</code> and <code>Flight</code>.
     *
     * @param fbs the flight booking system to populate with bookings
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
                // Data: customerId, flightId, bookingDate, bookingId,
                //       seatType, foodOption, bookingPrice, fee
                int custId = Integer.parseInt(data[0]);
                int fltId = Integer.parseInt(data[1]);
                LocalDate bookingDate = LocalDate.parse(data[2]);
                int bookingId = Integer.parseInt(data[3]);
                SeatType seatType = SeatType.valueOf(data[4]);
                FoodOption foodOpt = FoodOption.valueOf(data[5]);
                double bookingPrice = Double.parseDouble(data[6]);
                double fee = Double.parseDouble(data[7]);

                Customer customer = fbs.getCustomerById(custId);
                Flight flight = fbs.getFlightById(fltId);

                Booking booking = new Booking(customer, flight, bookingDate);
                // If needed, we could forcibly set bookingId, but typically the
                // booking object auto-assigns a new ID. For advanced usage, you might
                // store it if you want to ensure IDs match the file. 
                //
                booking.setSeatType(seatType);
                booking.setFoodOption(foodOpt);
                booking.setBookingPrice(bookingPrice);
                booking.setFee(fee);

                // Add to the customer’s list
                customer.addBooking(booking);
                // Also add passenger to the flight, specifying seat type if your flight logic allows it
                flight.addPassenger(customer, seatType);
            }
        }
    }

    /**
     * Stores booking data (including seat type, food option, booking price, and fee)
     * to <code>bookings.txt</code> for each active customer.
     *
     * @param fbs the flight booking system to retrieve booking info from
     * @throws IOException if writing the file fails
     */
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Customer customer : fbs.getAllCustomers()) {
                for (Booking b : customer.getBookings()) {
                    writer.println(
                        customer.getId() + SEPARATOR +
                        b.getFlight().getId() + SEPARATOR +
                        b.getBookingDate() + SEPARATOR +
                        b.getBookingId() + SEPARATOR +
                        b.getSeatType() + SEPARATOR +
                        b.getFoodOption() + SEPARATOR +
                        b.getBookingPrice() + SEPARATOR +
                        b.getFee()
                    );
                }
            }
        }
    }
}
