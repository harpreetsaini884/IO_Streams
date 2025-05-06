package org.rituraj;


import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class DatabaseService {

    // UC: Ability to Add new Person to the Address Book Database
    public void addPersonToDB(Connection con, Person Person) {
        try {
            con.setAutoCommit(false);

            PreparedStatement personStmt = con.prepareStatement(
                    "INSERT INTO person(first_name, last_name) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            personStmt.setString(1, Person.getFirstName());
            personStmt.setString(2, Person.getLastName());
            personStmt.executeUpdate();

            ResultSet keys = personStmt.getGeneratedKeys();
            if (keys.next()) {
                int personId = keys.getInt(1);

                PreparedStatement addressStmt = con.prepareStatement(
                        "INSERT INTO address(street) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
                addressStmt.setString(1, String.valueOf(Person.getAddress()));
                addressStmt.executeUpdate();

                ResultSet addressKeys = addressStmt.getGeneratedKeys();
                if (addressKeys.next()) {
                    int addressId = addressKeys.getInt(1);

                    PreparedStatement personAddressStmt = con.prepareStatement(
                            "INSERT INTO person_address(person_id, address_id) VALUES (?, ?)"
                    );
                    personAddressStmt.setInt(1, personId);
                    personAddressStmt.setInt(2, addressId);
                    personAddressStmt.executeUpdate();
                }

                PreparedStatement emailStmt = con.prepareStatement(
                        "INSERT INTO email(person_id, email_add) VALUES (?, ?)"
                );
                emailStmt.setInt(1, personId);
                emailStmt.setString(2, Person.getEmail());
                emailStmt.executeUpdate();

                PreparedStatement phoneStmt = con.prepareStatement(
                        "INSERT INTO phone(person_id, phone_number) VALUES (?, ?)"
                );
                phoneStmt.setInt(1, personId);
                phoneStmt.setString(2, Person.getPhone());
                phoneStmt.executeUpdate();

                PreparedStatement typeStmt = con.prepareStatement(
                        "INSERT INTO person_type(person_id, value) VALUES (?, ?)"
                );
                typeStmt.setInt(1, personId);
                typeStmt.setString(2, Person.getType());
                typeStmt.executeUpdate();

                con.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UC: Ability to Retrieve all Entries from the DB
    public List<Person> getAllPersonsFromDB(Connection con) {
        List<Person> Persons = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT p.id, p.first_name, p.last_name, ph.phone_number, e.email_add, a.street, pt.value " +
                     "FROM person p " +
                     "JOIN phone ph ON p.id = ph.person_id " +
                     "JOIN email e ON p.id = e.person_id " +
                     "JOIN person_address pa ON p.id = pa.person_id " +
                     "JOIN address a ON pa.address_id = a.address_id " +
                     "JOIN person_type pt ON p.id = pt.person_id")
        ) {
            while (rs.next()) {
                Person Person = new Person(
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("email_add"),
                        rs.getString("street"),
                        rs.getString("value")
                );
                Persons.add(Person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Persons;
    }


    // UC: Ability to Retrieve Persons from the Database that were added in a particular period
    public List<Person> getPersonsAddedInPeriod(Connection con, LocalDate from, LocalDate to) {
        List<Person> Persons = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT p.id, p.first_name, p.last_name, ph.phone_number, e.email_add, a.street, pt.value, p.created_date " +
                        "FROM person p " +
                        "JOIN phone ph ON p.id = ph.person_id " +
                        "JOIN email e ON p.id = e.person_id " +
                        "JOIN person_address pa ON p.id = pa.person_id " +
                        "JOIN address a ON pa.address_id = a.address_id " +
                        "JOIN person_type pt ON p.id = pt.person_id " +
                        "WHERE p.created_date BETWEEN ? AND ?")
        ) {
            stmt.setDate(1, Date.valueOf(from));
            stmt.setDate(2, Date.valueOf(to));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Person Person = new Person(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email_add"),
                        rs.getString("street"),
                        rs.getString("value")
                );
                Persons.add(Person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Persons;
    }

    // UC: Ability to update Person Information in memory and sync with DB
    public void updatePersonInDB(Connection con, String firstName, String newEmail, String newPhone) {
        try {
            PreparedStatement personStmt = con.prepareStatement("SELECT id FROM person WHERE first_name = ?");
            personStmt.setString(1, firstName);
            ResultSet rs = personStmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");

                PreparedStatement updateEmail = con.prepareStatement("UPDATE email SET email_add=? WHERE person_id=?");
                updateEmail.setString(1, newEmail);
                updateEmail.setInt(2, id);
                updateEmail.executeUpdate();

                PreparedStatement updatePhone = con.prepareStatement("UPDATE phone SET phone_number=? WHERE person_id=?");
                updatePhone.setString(1, newPhone);
                updatePhone.setInt(2, id);
                updatePhone.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
