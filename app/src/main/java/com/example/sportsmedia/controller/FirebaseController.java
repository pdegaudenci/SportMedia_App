package com.example.sportsmedia.controller;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseController {
    private  FirebaseDatabase firebase;
    private DatabaseReference reference;

    public FirebaseController(String database, Context context){
        this.reference=initFirebase(database,context);
    }


    private DatabaseReference initFirebase(String database, Context context) {
        FirebaseApp.initializeApp(context);
        //Instancia a la base de datos
        firebase = FirebaseDatabase.getInstance();
        //Referencia al nodo que queremos acceder
        reference= firebase.getReference(database);
        return reference;
    }

    public  FirebaseDatabase getFirebase() {
        return firebase;
    }

    public  void setFirebase(FirebaseDatabase firebase) {
        this.firebase = firebase;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public  void setReference(DatabaseReference reference) {
        this.reference = reference;
    }
}
