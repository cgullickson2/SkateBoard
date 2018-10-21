package com.example.skateboard;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.skateboard.databinding.ActivityMainBinding;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    DatabaseRepository databaseRepository = new DatabaseRepository();
    private ObservableBoolean loggedIn = new ObservableBoolean(false);

    public ObservableBoolean loggedIn() { return loggedIn; }

    public DatabaseRepository getDatabaseRepository() {
        return databaseRepository;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainActivity(this);

        databaseRepository.signIn("gruen065@umn.edu", "redwire15");

        databaseRepository.getUser().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (databaseRepository.getUser() == null) {
                    loggedIn.set(false);
                } else {
                    loggedIn.set(true);
                }
            }
        });
    }
}
