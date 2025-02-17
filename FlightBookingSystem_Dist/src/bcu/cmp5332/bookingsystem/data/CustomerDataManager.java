package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.*;
import java.util.Scanner;

/**
 * Manages loading and storing customer data to/from a text file.
 */
public class CustomerDataManager implements DataManager {
    private static final String FILE_NAME = "resources/data/customers.txt";
    private static final String SEPARATOR = "::";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        
        try (Scanner scanner = new Scanner(new FileReader(file))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(SEPARATOR);
                // Data: id, name, phone, email, deleted
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String phone = data[2];
                String email = data[3];
                boolean deleted = Boolean.parseBoolean(data[4]);
                Customer customer = new Customer(id, name, phone, email, deleted);
                fbs.addCustomer(customer);
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Customer customer : fbs.getAllCustomers()) {
                writer.println(
                    customer.getId() + SEPARATOR +
                    customer.getName() + SEPARATOR +
                    customer.getPhone() + SEPARATOR +
                    customer.getEmail() + SEPARATOR +
                    customer.isDeleted()
                );
            }
        }
    }
}
