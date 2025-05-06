package org.rituraj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressBook {
    private String name;

    //UC5: Multiple Contact List
    private List<Person> contacts = new ArrayList<>();

    public AddressBook(String name) {
        this.name = name;
        this.contacts = new ArrayList<>();
    }

    public boolean addPerson(Person p) {
        // UC7: Check for duplicate person in AddressBook
        if (contacts.contains(p)) {
            return false;
        }
        contacts.add(p);
        return true;
    }

    public boolean deletePerson(String firstName) {
        // UC3: Delete person by name
        return contacts.removeIf(p -> p.getFirstName().equalsIgnoreCase(firstName));
    }

    public void editPerson(String firstName, Scanner sc) {
        // UC2: Edit person by name
        for (Person p : contacts) {
            if (p.getFirstName().equalsIgnoreCase(firstName)) {
                System.out.print("New last name: ");
                p.setLastName(sc.nextLine());
                System.out.print("New phone: ");
                p.setPhone(sc.nextLine());
                System.out.print("New email: ");
                p.setEmail(sc.nextLine());
                return;
            }
        }
        System.out.println("Person not found.");
    }

    public void printContacts() {
        contacts.forEach(System.out::println);
    }
    public List<Person> searchByCity(String city) {
        // UC7: Search person by city
        return contacts.stream().filter(p -> p.getAddress().getCity().equalsIgnoreCase(city)).collect(Collectors.toList());
    }

    public List<Person> searchByState(String state) {
        // UC7: Search person by state
        return contacts.stream().filter(p -> p.getAddress().getState().equalsIgnoreCase(state)).collect(Collectors.toList());
    }

    public void sortByName() {
        // UC9: Sort by name using lambda
        contacts.sort(Comparator.comparing(Person::getFirstName));
    }

    public void sortByCity() {
        // UC10: Sort by city
        contacts.sort(Comparator.comparing(p -> p.getAddress().getCity()));
    }

    public void sortByState() {
        // UC10: Sort by state
        contacts.sort(Comparator.comparing(p -> p.getAddress().getState()));
    }

    public void sortByZip() {
        // UC10: Sort by zip
        contacts.sort(Comparator.comparing(p -> p.getAddress().getZip()));
    }

    public long countByCity(String city) {
        // UC8: Count by city
        return contacts.stream().filter(p -> p.getAddress().getCity().equalsIgnoreCase(city)).count();
    }

    public long countByState(String state) {
        // UC8: Count by state
        return contacts.stream().filter(p -> p.getAddress().getState().equalsIgnoreCase(state)).count();
    }

    public List<Person> getContacts() {
        return contacts;
    }


}

