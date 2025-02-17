package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.IOException;

public interface DataManager {
    void loadData(FlightBookingSystem fbs) throws IOException;
    void storeData(FlightBookingSystem fbs) throws IOException;
}