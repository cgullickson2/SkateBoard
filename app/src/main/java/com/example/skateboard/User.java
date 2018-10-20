package com.example.skateboard;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String creditCardNumber;
    private String key;
    private ArrayList<String> banks;

    public User(String firstName, String lastName, String email, String key,
                String creditCardNumber, ArrayList<String> banks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.creditCardNumber = creditCardNumber;
        this.banks = banks;
        this.key = key;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public ArrayList<String> getBanks() {
        return banks;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
