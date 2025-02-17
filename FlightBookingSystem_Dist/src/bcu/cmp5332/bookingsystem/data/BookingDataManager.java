package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Manages loading and storing booking data to/from a text file.
 * Extended to store seatType and foodOption as well.
 */
public class BookingDataManager implements DataManager {

    private static final String FILE_NAME = "resources/data/bookings.txt";
    private static final String SEPARATOR = "::";

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
                // forcibly set the ID if you prefer or just rely on nextBookingId
                // but usually we don't set bookingId manually unless you changed the code
                booking.setSeatType(seatType);
                booking.setFoodOption(foodOpt);
                booking.setBookingPrice(bookingPrice);
                booking.setFee(fee);

                customer.addBooking(booking);
                // add passenger with seatType if needed
                flight.addPassenger(customer, seatType);
            }
        }
    }

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
