package org.rituraj;

import java.sql.*;
import java.time.LocalDate;
import java.util.Enumeration;

public class App {

    public static void main( String[] args )  {

        // UC1: Loading the driver and setting up the connection
        String url = "jdbc:mysql://localhost:3306/payroll_service";
        String user = "root";
        String pswd = "root";
        Connection con;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded");
        }catch (ClassNotFoundException e){
            throw new IllegalStateException("Driver not found");
        }

            Enumeration<Driver> drivers =  DriverManager.getDrivers();
            if(drivers.hasMoreElements()){
                System.out.println(drivers.nextElement());
            }


        try{
            con = DriverManager.getConnection(url, user, pswd);
            System.out.println("Connection success");

            CRUD crud = new CRUD();

            // CREATING THE TABLE
//            crud.createTable(con);

            // POPULATING THE DATA
//            UC7: Adding new employee
//            crud.insertData(con);


            //UC2: Retrieve data from table as list
            // READING DATA
            System.out.println("Readind Data: ");
            crud.readData(con);

            //UC3: Update salary for Terisa
            //UC4: Used prepared statements
            // UPDATE:
            Employee e = new Employee(5,"Terisa", 55000.00, Date.valueOf("2023-04-14"));
            crud.updateSalary(con, e);

            //UC5: Retrieval of employee data who joined within a date range
            crud.getEmployeeInDateRange(con);

            //UC6: Database Functions
            crud.databaseFunctions(con);

            //UC8, UC9, UC10, UC11
//            crud.createTablePayrollDetails(con);
            crud.insertDataPayrollDetails(con);

            //UC12
            crud.deleteEmployee(con, 1);
            crud.readDataUpdated(con);

            }catch(SQLException e){
            e.printStackTrace();
        }


    }
}
