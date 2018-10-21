package com.example.skateboard;

import android.databinding.Observable;
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

        databaseRepository.signIn("gruen065@umn.edu", "redwire15");

        databaseRepository.getUser().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                databaseRepository.giveMoneyToBank(5.0, "-LPJDHAHH7IE4edaXjSi");
            }
        });
    }
}
