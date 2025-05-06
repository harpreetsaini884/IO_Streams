package org.rituraj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private Address address;
    private String email;
    private String phone;
    private String type;


    public Person(String firstName, String lastName, Address address, String email, String phone, String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public Person(String firstName, String lastName, String email, String phone, String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }
}

