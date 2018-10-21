package com.example.skateboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    DatabaseRepository databaseRepository = new DatabaseRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseRepository.inviteUserByEmail("jasonmgru@gmail.com", "1010");
    }
}
