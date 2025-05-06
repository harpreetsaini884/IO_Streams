package org.rituraj;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUD {

    public void createTable(Connection con) throws SQLException {
        String sql = "CREATE TABLE employee_payroll (id INT AUTO_INCREMENT, name VARCHAR(50), salary DOUBLE(10, 2), joining_date DATE, PRIMARY KEY (id))";
        Statement st = con.createStatement();
        int i = st.executeUpdate(sql);
        System.out.println("Table created successfully!");
    }

    public void readData(Connection con) throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee_payroll";
        Statement st = con.createStatement();
        ResultSet set =  st.executeQuery(sql);
        while(set.next()){
            Employee employee = new Employee(set.getInt(1), set.getString(2), set.getDouble(3), set.getDate(4));
            list.add(employee);
        }

        for(Employee e : list){
            System.out.println(e);
        }

    }

    public void insertData(Connection con) throws SQLException {
        String sql = "INSERT INTO employee_payroll (name, salary, joining_date) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = con.prepareStatement(sql);

        // First record
        preparedStatement.setString(1, "Alice Johnson");
        preparedStatement.setDouble(2, 75000.00);
        preparedStatement.setDate(3, Date.valueOf("2021-03-15"));
        preparedStatement.addBatch();

        // Second record
        preparedStatement.setString(1, "Bob Smith");
        preparedStatement.setDouble(2, 56000.50);
        preparedStatement.setDate(3, Date.valueOf("2020-07-01"));
        preparedStatement.addBatch();

        // Third record
        preparedStatement.setString(1, "Charlie Brown");
        preparedStatement.setDouble(2, 98000.00);
        preparedStatement.setDate(3, Date.valueOf("2023-01-12"));
        preparedStatement.addBatch();

        // Fourth record
        preparedStatement.setString(1, "Diana Rose");
        preparedStatement.setDouble(2, 63000.75);
        preparedStatement.setDate(3, Date.valueOf("2019-11-25"));
        preparedStatement.addBatch();

        int[] results = preparedStatement.executeBatch();
        System.out.println("Rows inserted: " + results.length);
    }

    public void updateSalary(Connection con, Employee e) throws SQLException {
        //used prepared statement
        String sql = "UPDATE employee_payroll SET salary=30000.00 WHERE name=?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, e.getName());

        int i = preparedStatement.executeUpdate();
        if(i > 0){
            System.out.println("Updated successfully!");
        }
    }

    public void getEmployeeInDateRange(Connection con) throws SQLException {
        String query = "SELECT * FROM employee_payroll WHERE joining_date BETWEEN ? AND ?";

        PreparedStatement st = con.prepareStatement(query);
        st.setDate(1, Date.valueOf("2020-01-01"));
        st.setDate(2, Date.valueOf("2024-01-01"));

        ResultSet set = st.executeQuery();
        while(set.next()){
            System.out.println("Id: " + set.getInt(1) + " Name: " + set.getString(2) + " Salary: " + set.getDouble(3) + " Joining Date: " + set.getDate(4));
        }
    }

    public void databaseFunctions(Connection con) throws SQLException {
        //SUM
        String sql = "SELECT SUM(salary) FROM employee_payroll WHERE gender='F' GROUP BY gender";
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery(sql);
        if(set.next()){
            System.out.println(set.getInt(1));
        }

        //count
        sql = "SELECT Gender, COUNT(*) FROM employee_payroll GROUP BY gender";
        set = st.executeQuery(sql);
        while(set.next()){
            System.out.print(set.getString(1) + " ");
            System.out.println(set.getInt(2));
        }

    }

    public void createTablePayrollDetails(Connection con) throws SQLException {
        String str = "CREATE TABLE payroll_details (\n" +
                "    id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    employee_name VARCHAR(50),  \n" +
                "    salary DOUBLE(10, 2),\n" +
                "    deduction DOUBLE(10, 2) GENERATED ALWAYS AS (salary * 0.2),\n" +
                "    taxable_pay DOUBLE(10, 2) GENERATED ALWAYS AS (salary - salary * 0.2),\n" +
                "    tax DOUBLE(10, 2) GENERATED ALWAYS AS ((salary - salary * 0.2) * 0.1),\n" +
                "    net_pay DOUBLE(10, 2) GENERATED ALWAYS AS (salary - ((salary - salary * 0.2) * 0.1))\n" +
                ");\n";

        Statement st = con.createStatement();
        st.executeUpdate(str);
    }

    public void insertDataPayrollDetails(Connection con) throws SQLException {
        con.setAutoCommit(false);
        String sql = "INSERT INTO employee_payroll (name, salary, joining_date, gender) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = con.prepareStatement(sql);

        preparedStatement.setString(1, "John Miller");
        preparedStatement.setDouble(2, 62000.00);
        preparedStatement.setDate(3, Date.valueOf("2022-12-25"));
        preparedStatement.setString(4, "M");

        int i = preparedStatement.executeUpdate();

        if(i>0){
            sql = "INSERT INTO payroll_details (employee_name, salary) VALUES (?, ?)";
            preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, "John Miller");
            preparedStatement.setDouble(2, 62000.00);
            int j = preparedStatement.executeUpdate();
            if(j > 0){
                con.commit();
            }else{
                con.rollback();
            }
        }else{
            con.rollback();
        }

    }

    public void deleteEmployee(Connection con, int id) throws SQLException {
        String query = "UPDATE employee_payroll set is_active=? where id=?";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setInt(2, id);
        preparedStatement.setBoolean(1, false);
        preparedStatement.executeUpdate();
    }

    public void readDataUpdated(Connection con) throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee_payroll where is_active=true";
        Statement st = con.createStatement();
        ResultSet set =  st.executeQuery(sql);
        while(set.next()){
            Employee employee = new Employee(set.getInt(1), set.getString(2), set.getDouble(3), set.getDate(4));
            list.add(employee);
        }

        for(Employee e : list){
            System.out.println(e);
        }

    }
}
