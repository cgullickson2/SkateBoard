package com.example.skateboard;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        home fragment = new home();
//        fragmentTransaction.add(R.id.textView, fragment);
//        fragmentTransaction.commit();

        databaseRepository.signIn("gruen065@umn.edu", "redwire15");

        //Set username for this profile
//        String firstName = databaseRepository.getUser().get().getFirstName();
//        Resources res = getResources();
//        String format = String.format(res.getString(R.string.userName), firstName);

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
