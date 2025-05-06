package org.rituraj;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUD {
    private final Connection con;

    public CRUD(Connection con) {
        this.con = con;
    }

    // UC1: Load all persons from DB
    public List<Person> loadAllContacts() {
        List<Person> people = new ArrayList<>();
        String query = "SELECT p.id, p.fname, p.lname, \n" +
                "                   a.address_id, a.street, a.city, a.state, a.zip,\n" +
                "                   e.email_add, ph.phone_number, pt.value\n" +
                "            FROM person p\n" +
                "            JOIN person_address pa ON p.id = pa.person_id\n" +
                "            JOIN address a ON a.address_id = pa.address_id\n" +
                "            JOIN email e ON p.id = e.person_id\n" +
                "            JOIN phone ph ON p.id = ph.person_id\n" +
                "            JOIN person_type pt ON p.id = pt.person_id";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Address addr = new Address(
                        rs.getInt("address_id"),
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("zip")
                );

                Person p = new Person(
                        rs.getInt("id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        addr,
                        rs.getString("email_add"),
                        rs.getString("phone_number"),
                        rs.getString("value")
                );
                people.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    // Insert a new person into DB
    public void insertPerson(Person p) {
        try {
            con.setAutoCommit(false); // transaction start

            // Insert Address
            String addrSql = "INSERT INTO address (street, city, state, zip) VALUES (?, ?, ?, ?)";
            PreparedStatement addrStmt = con.prepareStatement(addrSql, Statement.RETURN_GENERATED_KEYS);
            addrStmt.setString(1, p.getAddress().getStreet());
            addrStmt.setString(2, p.getAddress().getCity());
            addrStmt.setString(3, p.getAddress().getState());
            addrStmt.setString(4, p.getAddress().getZip());
            addrStmt.executeUpdate();
            ResultSet addrRs = addrStmt.getGeneratedKeys();
            addrRs.next();
            int addressId = addrRs.getInt(1);

            // Insert Person
            String personSql = "INSERT INTO person (fname, lname) VALUES (?, ?)";
            PreparedStatement personStmt = con.prepareStatement(personSql, Statement.RETURN_GENERATED_KEYS);
            personStmt.setString(1, p.getFirstName());
            personStmt.setString(2, p.getLastName());
            personStmt.executeUpdate();
            ResultSet personRs = personStmt.getGeneratedKeys();
            personRs.next();
            int personId = personRs.getInt(1);

            // Link person_address
            String linkSql = "INSERT INTO person_address (person_id, address_id) VALUES (?, ?)";
            PreparedStatement linkStmt = con.prepareStatement(linkSql);
            linkStmt.setInt(1, personId);
            linkStmt.setInt(2, addressId);
            linkStmt.executeUpdate();

            // Insert email
            String emailSql = "INSERT INTO email (person_id, email_add) VALUES (?, ?)";
            PreparedStatement emailStmt = con.prepareStatement(emailSql);
            emailStmt.setInt(1, personId);
            emailStmt.setString(2, p.getEmail());
            emailStmt.executeUpdate();

            // Insert phone
            String phoneSql = "INSERT INTO phone (person_id, phone_number) VALUES (?, ?)";
            PreparedStatement phoneStmt = con.prepareStatement(phoneSql);
            phoneStmt.setInt(1, personId);
            phoneStmt.setString(2, p.getPhone());
            phoneStmt.executeUpdate();

            // Insert type
            String typeSql = "INSERT INTO person_type (person_id, value) VALUES (?, ?)";
            PreparedStatement typeStmt = con.prepareStatement(typeSql);
            typeStmt.setInt(1, personId);
            typeStmt.setString(2, p.getType());
            typeStmt.executeUpdate();

            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback(); // rollback if anything fails
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    // Update email, phone, and address by person ID
    public void updatePerson(int personId, String newEmail, String newPhone, Address newAddress) {
        try {
            con.setAutoCommit(false);

            // Update email
            String emailSql = "UPDATE email SET email_add = ? WHERE person_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(emailSql)) {
                stmt.setString(1, newEmail);
                stmt.setInt(2, personId);
                stmt.executeUpdate();
            }

            // Update phone
            String phoneSql = "UPDATE phone SET phone_number = ? WHERE person_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(phoneSql)) {
                stmt.setString(1, newPhone);
                stmt.setInt(2, personId);
                stmt.executeUpdate();
            }

            // Update address
            String addrSql = "UPDATE address a\n" +
                    "                JOIN person_address pa ON a.address_id = pa.address_id\n" +
                    "                SET a.street = ?, a.city = ?, a.state = ?, a.zip = ?\n" +
                    "                WHERE pa.person_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(addrSql)) {
                stmt.setString(1, newAddress.getStreet());
                stmt.setString(2, newAddress.getCity());
                stmt.setString(3, newAddress.getState());
                stmt.setString(4, newAddress.getZip());
                stmt.setInt(5, personId);
                stmt.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    // Delete person and all references
    public void deletePerson(int personId) {
        try {
            con.setAutoCommit(false);

            // Delete email, phone, type first
            String[] deleteTables = {"email", "phone", "person_type", "person_address"};
            for (String table : deleteTables) {
                try (PreparedStatement stmt = con.prepareStatement("DELETE FROM " + table + " WHERE person_id = ?")) {
                    stmt.setInt(1, personId);
                    stmt.executeUpdate();
                }
            }

            // Delete person
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM person WHERE id = ?")) {
                stmt.setInt(1, personId);
                stmt.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

}}
