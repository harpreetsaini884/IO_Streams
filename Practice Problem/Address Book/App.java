package org.rituraj;

import java.sql.*;
import java.time.LocalDate;
import java.util.Enumeration;

public class App {

    public static void main(String[] args) {
        // UC1: Loading the driver and connection
        String url = "jdbc:mysql://localhost:3306/address_book_service";
        String user = "root";
        String pswd = "root";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded");

            Enumeration<Driver> drivers = DriverManager.getDrivers();
            if (drivers.hasMoreElements()) {
                System.out.println("Driver info: " + drivers.nextElement());
            }

            Connection con = DriverManager.getConnection(url, user, pswd);
            System.out.println("Connection success");

            AddressBookMain controller = new AddressBookMain(con);
            controller.run();

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
