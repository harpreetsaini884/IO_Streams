package org.rituraj;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;
import java.util.Scanner;

public class AddressBookMain {

    //UC6: Map to store multiple address books
    private Map<String, AddressBook> bookMap;
    private Scanner sc;
    private CRUD crud;
    private FileIOService fileService;
    private DatabaseService dbService;
    private Connection con;

    public AddressBookMain(Connection con) {
        this.bookMap = new HashMap<>();
        this.sc = new Scanner(System.in);
        this.crud = new CRUD(con);
        this.fileService = new FileIOService();
        this.dbService = new DatabaseService();
        this.con = con;


        AddressBook dbBook = new AddressBook("Default");
        List<Person> dbPeople = crud.loadAllContacts();
        for (Person p : dbPeople) dbBook.addPerson(p);
        bookMap.put("Default", dbBook);
    }

    public void run() {
        while (true) {
            System.out.println("1. Add AddressBook\n" +
                    "                2. Add Contact\n" +
                    "                3. Edit Contact\n" +
                    "                4. Delete Contact\n" +
                    "                5. View Contacts\n" +
                    "                6. Search by City\n" +
                    "                7. Search by State\n" +
                    "                8. Count by City/State\n" +
                    "                9. Sort by Name\n" +
                    "                10. Sort by City/State/Zip\n" +
                    "11. Write to JSON File\n" +
                    "12. Read from JSON File\n" +
                    "13. Write to CSV File\n" +
                    "14. Read from CSV File\n" +
                    "15. Write to txt File\n" +
                    "16. Get Contacts Added in a Period\n" +
                    "17.Update DB Contact\n" +
                    "18. Add new person to db\n" +
                    "19. Retrieve all person from\n" +
                    "                0. Exit");

            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> addAddressBook();
                case 2 -> addContact();
                case 3 -> editContact();
                case 4 -> deleteContact();
                case 5 -> printContacts();
                case 6 -> searchByCity();
                case 7 -> searchByState();
                case 8 -> countByCityState();
                case 9 -> sortByName();
                case 10 -> sortByOther();
                case 11 -> writeAllPersonsToJSON();
                case 12 -> readAllPersonsFromJSON();
                case 13 -> exportAllPersonsToCSV();
                case 14 -> exportAllPersonsToJSON();
                case 15 -> writeAllPersonsToJSON();
                case 16 -> {
                    // UC: Get contacts added in a period
                    System.out.print("From Date (yyyy-mm-dd): ");
                    LocalDate from = LocalDate.parse(sc.nextLine());
                    System.out.print("To Date (yyyy-mm-dd): ");
                    LocalDate to = LocalDate.parse(sc.nextLine());
                    List<Person> list = dbService.getPersonsAddedInPeriod(con, from, to);
                    list.forEach(System.out::println);
                }
                case 17 -> {
                    // UC: Update Person Information in DB
                    System.out.print("Enter first name: ");
                    String fname = sc.nextLine();
                    System.out.print("New email: ");
                    String newEmail = sc.nextLine();
                    System.out.print("New phone: ");
                    String newPhone = sc.nextLine();
                    dbService.updatePersonInDB(con, fname, newEmail, newPhone);
                }
                case 18 -> {
                    // UC: Add a new person to the database
                    System.out.print("Enter first name: ");
                    String firstName = sc.nextLine();
                    System.out.print("Enter last name: ");
                    String lastName = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = sc.nextLine();
                    System.out.print("Enter street address: ");
                    String address = sc.nextLine();
                    System.out.print("Enter person type: ");
                    String type = sc.nextLine();

                    Person person = new Person(firstName, lastName, address, type, phone);
                    dbService.addPersonToDB(con, person);
                }
                case 19 -> {
                    // UC: Retrieve all persons from the database
                    List<Person> persons = dbService.getAllPersonsFromDB(con);
                    persons.forEach(System.out::println);
                }

                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    //UC1
    private void addAddressBook() {
        System.out.print("Enter AddressBook name: ");
        String name = sc.nextLine();
        bookMap.putIfAbsent(name, new AddressBook(name));
        System.out.println("Created.");
    }

    //UC2
    private void addContact() {
        System.out.print("Enter AddressBook name: ");
        String name = sc.nextLine();
        AddressBook book = bookMap.get(name);
        if (book == null) {
            System.out.println("AddressBook not found.");
            return;
        }

        System.out.print("First name: ");
        String fname = sc.nextLine();
        System.out.print("Last name: ");
        String lname = sc.nextLine();
        System.out.print("Street: ");
        String street = sc.nextLine();
        System.out.print("City: ");
        String city = sc.nextLine();
        System.out.print("State: ");
        String state = sc.nextLine();
        System.out.print("Zip: ");
        String zip = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Phone: ");
        String phone = sc.nextLine();
        System.out.print("Type (family/friends): ");
        String type = sc.nextLine();

        Address addr = new Address(street, city, state, zip);
        Person p = new Person(fname, lname, addr, email, phone, type);

        if (book.addPerson(p)) {
            System.out.println("Contact added.");
        } else {
            System.out.println("Duplicate contact!");
        }
    }

    //UC3
    private void editContact() {
        System.out.print("Enter AddressBook name: ");
        String name = sc.nextLine();
        AddressBook book = bookMap.get(name);
        if (book != null) {
            System.out.print("Enter first name of person to edit: ");
            book.editPerson(sc.nextLine(), sc);
        }
    }

    private void deleteContact() {
        System.out.print("Enter AddressBook name: ");
        String name = sc.nextLine();
        AddressBook book = bookMap.get(name);
        if (book != null) {
            System.out.print("Enter first name of person to delete: ");
            if (book.deletePerson(sc.nextLine())) {
                System.out.println("Deleted.");
            } else {
                System.out.println("Not found.");
            }
        }
    }

      private void printContacts() {
        for (String bookName : bookMap.keySet()) {
            System.out.println("\n--- " + bookName + " ---");
            bookMap.get(bookName).printContacts();
        }
    }

    private void searchByCity() {
        System.out.print("City: ");
        String city = sc.nextLine();
        bookMap.values().forEach(book -> {
            List<Person> list = book.searchByCity(city);
            if (!list.isEmpty()) {
                System.out.println("In book: " + book);
                list.forEach(System.out::println);
            }
        });
    }

    private void searchByState() {
        System.out.print("State: ");
        String state = sc.nextLine();
        bookMap.values().forEach(book -> {
            List<Person> list = book.searchByState(state);
            if (!list.isEmpty()) {
                System.out.println("In book: " + book);
                list.forEach(System.out::println);
            }
        });
    }

    private void countByCityState() {
        System.out.print("City or State: ");
        String val = sc.nextLine();
        long count = bookMap.values().stream()
                .mapToLong(book -> book.countByCity(val) + book.countByState(val))
                .sum();
        System.out.println("Count: " + count);
    }

    private void sortByName() {
        bookMap.values().forEach(AddressBook::sortByName);
        System.out.println("Sorted by name.");
    }

    private void sortByOther() {
        System.out.println("1. City\n2. State\n3. Zip");
        int ch = Integer.parseInt(sc.nextLine());
        for (AddressBook book : bookMap.values()) {
            switch (ch) {
                case 1 -> book.sortByCity();
                case 2 -> book.sortByState();
                case 3 -> book.sortByZip();
            }
        }
    }

    private void writeAllPersonsToJSON() {
        List<Person> allPersons = new ArrayList<>();
        for (AddressBook book : bookMap.values()) {
            allPersons.addAll(book.getContacts());
        }
        fileService.writePersonsToJSON(allPersons);
        System.out.println("All contacts written to JSON file.");
    }

    private void readAllPersonsFromJSON() {
        List<Person> persons = fileService.readPersonsFromJSON();
        AddressBook fileBook = new AddressBook("FromJSON");
        for (Person p : persons) fileBook.addPerson(p);
        bookMap.put("FromJSON", fileBook);
        System.out.println("Contacts read from JSON file into AddressBook 'FromJSON'.");
    }

    // Export all contacts to CSV file
    private void exportAllPersonsToCSV() {
        List<Person> allPersons = new ArrayList<>();
        for (AddressBook book : bookMap.values()) {
            allPersons.addAll(book.getContacts());
        }
        fileService.writePersonsToCSV(allPersons);
        System.out.println("All contacts exported to CSV file.");
    }

    // Export all contacts to JSON file (alternative to case 11)
    private void exportAllPersonsToJSON() {
        List<Person> allPersons = new ArrayList<>();
        for (AddressBook book : bookMap.values()) {
            allPersons.addAll(book.getContacts());
        }
        fileService.writePersonsToJSON(allPersons);
        System.out.println("All contacts exported to JSON file.");
    }

    // UC: Ability to Write the Address Book with Persons Person into a File using File IO
    public void writePersonsToTextFile(List<Person> Persons) {
        List<Person> allPersons = new ArrayList<>();
        for (AddressBook book : bookMap.values()) {
            allPersons.addAll(book.getContacts());
        }
        fileService.writePersonsToTextFile(allPersons);
        System.out.println("All contacts exported to txt file.");
    }


}
