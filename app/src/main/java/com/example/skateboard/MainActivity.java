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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.skateboard.databinding.ActivityMainBinding;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    DatabaseRepository databaseRepository;
    private ObservableField<User> user = new ObservableField<>();

    public DatabaseRepository getDatabaseRepository() {
        return databaseRepository;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainActivity(this);
        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        final MyAdapter adapter = new MyAdapter(databaseRepository.bankList, databaseRepository);
        rv.setAdapter(adapter);

        getIntent().getSerializableExtra("DATABASE");

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        home fragment = new home();
//        fragmentTransaction.add(R.id.textView, fragment);
//        fragmentTransaction.commit();

        databaseRepository.signIn("gruen065@umn.edu", "redwire15");
    }
}
