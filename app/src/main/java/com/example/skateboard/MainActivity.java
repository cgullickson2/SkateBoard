package com.example.skateboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseRepository databaseRepository = new DatabaseRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = databaseRepository.createUser("Jayden", "Bro", "jayden's email");
        Log.d("USER FIRST NAME", user.getFirstName());

        databaseRepository.updateCreditCard(user.getKey(), "new credit card #");
    }
}
