package bcu.cmp5332.bookingsystem.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a booking made by a customer for a specific flight.
 */
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int nextBookingId = 1;

    private final int bookingId;
    private final Customer customer;
    private Flight flight;
    private LocalDate bookingDate;

    /**
     * The seat class (ECONOMY, BUSINESS, or FIRST) for this booking.
     * Defaults to ECONOMY if not otherwise specified.
     */
    private SeatType seatType = SeatType.ECONOMY;

    /**
     * The selected food option for this booking, e.g. NO_MEAL, CHICKEN, etc.
     * Defaults to NO_MEAL if not specified.
     */
    private FoodOption foodOption = FoodOption.NO_MEAL;

    /**
     * The final price for this booking, including any dynamic pricing.
     * Defaults to 0.0 until the booking is priced.
     */
    private double bookingPrice = 0.0;

    /**
     * Any additional fee applied (e.g. cancellation or rebooking fee).
     * Defaults to 0.0 if no extra fee is needed.
     */
    private double fee = 0.0;

    /**
     * Constructs a new <code>Booking</code>, assigning a unique booking ID automatically.
     *
     * @param customer     the customer making the booking
     * @param flight       the flight being booked
     * @param bookingDate  the date on which the booking is made
     */
    public Booking(Customer customer, Flight flight, LocalDate bookingDate) {
        this.bookingId = nextBookingId++;
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
    }

    /**
     * Returns the unique booking ID assigned to this booking.
     *
     * @return the booking ID
     */
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Returns the customer who made this booking.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the <code>Flight</code> associated with this booking.
     *
     * @return the flight
     */
    public Flight getFlight() {
        return flight;
    }

    /**
     * Sets a different <code>Flight</code> for this booking (e.g. for rebooking).
     *
     * @param flight the new flight
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    /**
     * Returns the date on which this booking was created.
     *
     * @return the booking date
     */
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    /**
     * Sets a different booking date (if needed).
     *
     * @param bookingDate the new booking date
     */
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * Returns the seat class (ECONOMY, BUSINESS, or FIRST) chosen for this booking.
     *
     * @return the seat type
     */
    public SeatType getSeatType() {
        return seatType;
    }

    /**
     * Sets or changes the seat type for this booking (e.g. from ECONOMY to BUSINESS).
     *
     * @param seatType the new seat type
     */
    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    /**
     * Returns the selected food option for this booking.
     *
     * @return the food option
     */
    public FoodOption getFoodOption() {
        return foodOption;
    }

    /**
     * Sets or changes the food option for this booking (e.g. NO_MEAL, CHICKEN, BEEF).
     *
     * @param foodOption the new food option
     */
    public void setFoodOption(FoodOption foodOption) {
        this.foodOption = foodOption;
    }

    /**
     * Returns the final price for this booking, including any dynamic or seat-class adjustments.
     *
     * @return the booking price
     */
    public double getBookingPrice() {
        return bookingPrice;
    }

    /**
     * Sets the final price for this booking (often after dynamic pricing is calculated).
     *
     * @param bookingPrice the new booking price
     */
    public void setBookingPrice(double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    /**
     * Returns any additional fee for cancellation or rebooking.
     *
     * @return the fee amount
     */
    public double getFee() {
        return fee;
    }

    /**
     * Sets an extra fee for cancellation or rebooking.
     *
     * @param fee the fee amount
     */
    public void setFee(double fee) {
        this.fee = fee;
    }

    /**
     * Returns a short string describing the booking details:
     * booking ID, flight number, seat type, food option, price, and fee.
     *
     * @return the short details string
     */
    public String getDetailsShort() {
        return "Booking #" + bookingId +
               " | Flight: " + flight.getFlightNumber() +
               " | Seat: " + seatType +
               " | Food: " + foodOption +
               " | Price: $" + bookingPrice +
               " | Fee: $" + fee;
    }
}
