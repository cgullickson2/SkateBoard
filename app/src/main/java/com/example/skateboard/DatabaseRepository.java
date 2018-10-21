package com.example.skateboard;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class DatabaseRepository {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference banksRef = database.getReference("banks");
    private DatabaseReference usersRef = database.getReference("users");
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private ObservableField<User> user = new ObservableField<User>();
    ObservableArrayList<Bank> bankList = new ObservableArrayList<>();

    private ObservableField<String> error = new ObservableField<String>();

    public DatabaseRepository() {
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (auth.getUid() != null) {
                    database.getReference("users/"+auth.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String first = dataSnapshot.child("firstName").getValue(String.class);
                            String last = dataSnapshot.child("lastName").getValue(String.class);
                            String email = dataSnapshot.child("email").getValue(String.class);
                            String creditCardNumber = dataSnapshot.child("creditCardNumber").getValue(String.class);
                            String balance = dataSnapshot.child("balance").getValue(String.class);

                            user.set(new User(
                                    first,
                                    last,
                                    email,
                                    auth.getUid(),
                                    creditCardNumber,
                                    Double.parseDouble(balance),
                                    bankList
                            ));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            error.set(databaseError.getMessage());
                        }
                    });
                    database.getReference("users/"+auth.getUid()+"/banks").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String bankKey = dataSnapshot.getValue(String.class);
                            DatabaseReference ref = banksRef.child(bankKey);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    HashMap<String, Object> obj = (HashMap<String, Object>) dataSnapshot.getValue();
                                    Collection<String> memberKeys = ((HashMap<String, String>) obj.get("members")).values();
                                    ArrayList<String> members = new ArrayList<>(memberKeys);
                                    final Bank bank = new Bank(
                                            (String) obj.get("name"),
                                            dataSnapshot.getKey(),
                                            Double.parseDouble((String) obj.get("amount")),
                                            members
                                    );
                                    for (int i=0; i<bankList.size(); i++) {
                                        if (bankList.get(i).getKey().equals(bank.getKey())) {
                                            bankList.set(i, bank);
                                            return;
                                        }
                                    }
                                    bankList.add(bank);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

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
                    userObj.put("balance", "10.0");
                    FirebaseUser u = auth.getCurrentUser();
                    DatabaseReference userRef = usersRef.child(u.getUid());
                    userRef.setValue(userObj);
                    database.getReference("emailToUid/"+email.replace('.', ':')).setValue(u.getUid());
                } else {
                    error.set("Error creating account!");
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
                if (!task.isSuccessful()) {
                    error.set("Couldn't sign in user!");
                }
            }
        });
    }

    /**
     * Signs out the current user.
     */
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
     */
    public void createBank(String bankName) {
        if (auth.getUid() == null) {
            error.set("User is not signed in yet, please wait.");
            return;
        }

        Map<String, String> bankObj = new HashMap<>();
        bankObj.put("name", bankName);

        DatabaseReference bankRef = banksRef.push();
        bankRef.setValue(bankObj);
        bankRef.child("amount").setValue("0.00");
        bankRef.child("members").push().setValue(auth.getUid());

        usersRef.child(auth.getUid()).child("banks").push().setValue(bankRef.getKey());
    }

    /**
     * Associates a user with a bank.
     *
     * @param bankKey The unique database key associated with the bank.
     */
    public void addUserToBank(String bankKey) {
        if (auth.getUid() == null) {
            error.set("No user is signed in!");
            return;
        }
        banksRef.child(bankKey).child("members").push().setValue(auth.getUid());
        usersRef.child(auth.getUid()).child("banks").push().setValue(bankKey);
    }

    /**
     * Invites a user to a bank, given the bank ID and the email of the user.
     *
     * @param targetEmail The user's email.
     * @param bankKey The key of the bank.
     */
    public void inviteUserByEmail(String targetEmail, final String bankKey) {
        database.getReference("emailToUid").child(targetEmail.replace('.', ':')).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uid = dataSnapshot.getValue(String.class);
                if (uid == null) {
                    error.set("That email is not associated with a user ID.");
                    return;
                }
                usersRef.child(uid).child("invites").push().setValue(bankKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                error.set("Couldn't find uid for that email!");
            }
        });
    }

    /**
     * Give a certain amount of money to the bank from your account.
     *
     * @param amount The amount to give.
     * @param bankKey The unique key associated with the bank you want to put money into.
     */
    public void addFundsToBank(final Double amount, final String bankKey) {
        if (auth.getUid() == null || user.get() == null) {
            error.set("There is no user logged in!");
            return;
        }

        if (user.get().getBalance() >= amount) {
            banksRef.child(bankKey).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Double newBalance = user.get().getBalance() - amount;
                    usersRef.child(auth.getUid()).child("balance").setValue(newBalance.toString());
                    String bankAmount = dataSnapshot.getValue(String.class);
                    Double newBankAmount = amount + Double.parseDouble(bankAmount);
                    banksRef.child(bankKey).child("amount").setValue(newBankAmount.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("ERROR", "cancelled.");
                }
            });
        } else {
            error.set("Not enough funds in your account.");
            return;
        }
    }

    /**
     * Give a certain amount of money to the bank from your account.
     *
     * @param amount The amount to give.
     * @param bankKey The unique key associated with the bank you want to put money into.
     */
    public void subtractFundsFromBank(final Double amount, final String bankKey) {
        if (auth.getUid() == null || user.get() == null) {
            error.set("There is no user logged in!");
            return;
        }

        if (user.get().getBalance() >= amount) {
            banksRef.child(bankKey).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Double newBalance = user.get().getBalance() + amount;
                    usersRef.child(auth.getUid()).child("balance").setValue(newBalance.toString());
                    String bankAmount = dataSnapshot.getValue(String.class);
                    Double newBankAmount = Double.parseDouble(bankAmount) - amount;
                    banksRef.child(bankKey).child("amount").setValue(newBankAmount.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("ERROR", "cancelled.");
                }
            });
        } else {
            error.set("Not enough funds in your account.");
            return;
        }
    }

    public ObservableField<User> getUser() {
        return user;
    }
}
