package com.example.skateboard;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DatabaseRepository {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference banksRef = database.getReference("banks");
    private DatabaseReference usersRef = database.getReference("users");
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private User user;

    /**
     * Creates a user. Must have a first name, last name, and valid email.
     *
     * @param first The user's first name.
     * @param last The user's last name.
     * @param email The user's email.
     */
    public void createUser(final String first, final String last, final String email, final String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, String> userObj = new HashMap<>();
                    userObj.put("firstName", first);
                    userObj.put("lastName", last);
                    userObj.put("email", email);
                    FirebaseUser u = auth.getCurrentUser();
                    DatabaseReference userRef = usersRef.child(u.getUid());
                    userRef.setValue(userObj);
                    user = new User(
                            first,
                            last,
                            email,
                            u.getUid(),
                            null,
                            null
                    );
                } else {
                    Log.e("ERROR", "Couldn't authenticate user!");
                }
            }
        });
    }

    /**
     * Signs in user with email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     */
    public void signIn(final String email, final String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DatabaseReference userRef = usersRef.child(auth.getCurrentUser().getUid());
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("ERROR", "Couldn't identify user in database!");
                        }
                    });
                } else {
                    Log.e("ERROR", "Couldn't sign in user!");
                }
            }
        });
    }

    public void signOut() {
        auth.signOut();
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
