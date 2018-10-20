package com.example.skateboard;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseRepository {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference banksRef = database.getReference("banks");
    private DatabaseReference usersRef = database.getReference("users");

    private User user;

    public DatabaseRepository() {

    }

    /**
     * Creates a user. Must have a first name, last name, and valid email.
     *
     * @param first The user's first name.
     * @param last The user's last name.
     * @param email The user's email.
     */
    public void createUser(String first, String last, String email) {
        Map<String, String> userObj = new HashMap<>();
        userObj.put("firstName", first);
        userObj.put("lastName", last);
        userObj.put("email", email);

        DatabaseReference userRef = usersRef.push();
        userRef.setValue(userObj);
        user = new User(first, last, email, userRef.getKey(), "", new ArrayList<String>());
    }

    /**
     * Updates a user's credit card.
     *
     * @param userKey The user's unique database key.
     * @param creditCardNumber The credit card number to overwrite current one.
     */
    public void updateCreditCard(String userKey, String creditCardNumber) {
        DatabaseReference userRef = usersRef.child(userKey);
        userRef.child("creditCardNumber").setValue(creditCardNumber);
    }

    /**
     * Creates a bank. Must have a bank name and a creator key.
     * Also associates the creator with the bank.
     *
     * @param bankName The name of the piggy bank to create.
     * @param creatorKey The unique database key associated with the user who created this bank.
     */
    public void createBank(String bankName, String creatorKey) {
        Map<String, String> bankObj = new HashMap<>();
        bankObj.put("name", bankName);

        DatabaseReference bankRef = banksRef.push();
        bankRef.setValue(bankObj);
        bankRef.child("amount").setValue("0.00");
        bankRef.child("members").push().setValue(creatorKey);

        usersRef.child(creatorKey).child("banks").push().setValue(bankRef.getKey());
    }

    /**
     * Associates a user with a bank.
     *
     * @param userKey The unique database key associated with the user.
     * @param bankKey The unique database key associated with the bank.
     */
    public void addUserToBank(String userKey, String bankKey) {
        banksRef.child(bankKey).child("members").push().setValue(userKey);
        usersRef.child(userKey).child("banks").push().setValue(bankKey);
    }

    public User getUser() {
        return user;
    }
}
