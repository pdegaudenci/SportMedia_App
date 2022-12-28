package com.example.sportsmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase firebase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initFirebaseService();
    }

    public void initFirebaseService(){
        FirebaseApp.initializeApp(this);
        firebase = FirebaseDatabase.getInstance();
        reference= firebase.getReference();
    }
}