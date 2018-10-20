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

        databaseRepository.createUser("Jayden", "Lee", "jayden's email");
        databaseRepository.createUser("Jason", "Gully", "jason em");

        User user = databaseRepository.getUser();

        databaseRepository.updateCreditCard(user.getKey(), "new credit card #");
        databaseRepository.createBank("newBank", user.getKey());
    }
}
