package org.rituraj;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIOService {
    private static final String CSV_FILE_PATH = "C:\\Users\\ritur\\OneDrive\\Desktop\\New folder (2)\\Person.csv";
    private static final String TEXT_FILE_PATH = "C:\\Users\\ritur\\OneDrive\\Desktop\\New folder (2)\\Person.txt";
    private static final String JSON_FILE_PATH = "C:\\Users\\ritur\\OneDrive\\Desktop\\New folder (2)\\Person.json";

    // UC: Ability to Write the Address Book with Persons Person as CSV File
    public void writePersonsToCSV(List<Person> Persons) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH))) {
            for (Person Person : Persons) {
                String[] data = {Person.getFirstName(), Person.getLastName(), Person.getPhone(),
                        Person.getEmail(), Person.getType()};
                writer.writeNext(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // UC: Ability to Read the Address Book with Persons Person as CSV File
    public List<Person> readPersonsFromCSV() {
        List<Person> Persons = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                Person Person = new Person(line[0], line[1], line[2], line[3], line[4]);
                Persons.add(Person);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return Persons;
    }

    // UC: Ability to Write the Address Book with Persons Person into a File using File IO
    public void writePersonsToTextFile(List<Person> Persons) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEXT_FILE_PATH))) {
            for (Person Person : Persons) {
                writer.write(Person.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // UC: Ability to Read the Address Book with Persons Person into a File using File IO
    public List<Person> readPersonsFromTextFile() {
        List<Person> Persons = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEXT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // simple example assumes .toString() format is consistent
                String[] fields = line.split(",");
                Person Person = new Person(fields[0], fields[1], fields[2], fields[3], fields[4]);
                Persons.add(Person);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Persons;
    }

    // UC: Ability to Write the Address Book with Persons Person as JSON File
    public void writePersonsToJSON(List<Person> Persons) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(JSON_FILE_PATH), Persons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // UC: Ability to Read the Address Book with Persons Person as JSON File
    public List<Person> readPersonsFromJSON() {
        ObjectMapper mapper = new ObjectMapper();
        List<Person> Persons = new ArrayList<>();
        try {
            Persons = mapper.readValue(new File(JSON_FILE_PATH), new TypeReference<List<Person>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Persons;
    }


}
