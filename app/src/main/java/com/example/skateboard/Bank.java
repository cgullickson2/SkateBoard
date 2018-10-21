package com.example.skateboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Bank {
    private String name;
    private String key;
    private Double amount;
    private List<String> members;

    public Bank(String name, String key, Double amount, List<String> members) {
        this.name = name;
        this.key = key;
        this.amount = amount;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public Double getAmount() {
        return amount;
    }

    public List<String> getMembers() {
        return members;
    }
}
