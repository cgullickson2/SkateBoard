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

    /**
     * Creates a user. Must have a first name, last name, and valid email.
     *
     * @param first The user's first name.
     * @param last The user's last name.
     * @param email The user's email.
     * @return a User object reference.
     */
    public User createUser(String first, String last, String email) {
        Map<String, String> userObj = new HashMap<>();
        userObj.put("firstName", first);
        userObj.put("lastName", last);
        userObj.put("email", email);

        DatabaseReference userRef = usersRef.push();
        userRef.setValue(userObj);

        return new User(
                first,
                last,
                email,
                userRef.getKey(),
                "",
                new ArrayList<String>());
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
}
