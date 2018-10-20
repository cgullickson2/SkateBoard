package com.example.skateboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Bank {
    private String name;
    private Map<String, Double> memberAmounts;

    public Bank(String name, String creatorId) {
        this.name = name;
        this.memberAmounts.put(creatorId, 0.0);
    }

    public String getName() {
        return name;
    }

    public List<String> getMembers() {
        List<String> list = new ArrayList<>();
        list.addAll(memberAmounts.keySet());
        return list;
    }

    public Double getAmount() {
        Double total = 0.0;
        for (Double amount : memberAmounts.values()) {
            total += amount;
        }
        return total;
    }
}
