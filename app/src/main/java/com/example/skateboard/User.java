package com.example.skateboard;

import java.util.ArrayList;
import java.util.Collection;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String creditCardNumber;
    private String key;
    private Double balance;
    private Collection<String> banks;

    public User(String firstName, String lastName, String email, String key,
                String creditCardNumber, Double balance, Collection<String> banks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.creditCardNumber = creditCardNumber;
        this.banks = banks;
        this.balance = balance;
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

    public Collection<String> getBanks() {
        return banks;
    }

    public String getKey() {
        return key;
    }

    public Double getBalance() {
        return balance;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
