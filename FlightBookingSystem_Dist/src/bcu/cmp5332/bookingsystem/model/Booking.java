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

    // Additional fields
    private SeatType seatType = SeatType.ECONOMY;  
    private FoodOption foodOption = FoodOption.NO_MEAL;

    private double bookingPrice = 0.0;
    private double fee = 0.0;

    public Booking(Customer customer, Flight flight, LocalDate bookingDate) {
        this.bookingId = nextBookingId++;
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
    }

    public int getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public FoodOption getFoodOption() {
        return foodOption;
    }

    public void setFoodOption(FoodOption foodOption) {
        this.foodOption = foodOption;
    }

    public double getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getDetailsShort() {
        return "Booking #" + bookingId +
               " | Flight: " + flight.getFlightNumber() +
               " | Seat: " + seatType +
               " | Food: " + foodOption +
               " | Price: $" + bookingPrice +
               " | Fee: $" + fee;
    }
}
