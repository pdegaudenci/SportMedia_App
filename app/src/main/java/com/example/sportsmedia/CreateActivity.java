package com.example.sportsmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sportsmedia.models.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CreateActivity extends AppCompatActivity {
    DateTimeFormatter formatter;
    TextView duration;
    TextView startTime;
    TextView endTime;
    TextView titulo;
    TextView descripcion;
    Button btnCreate;
    Spinner feedbackSpinner;

    FirebaseDatabase firebase;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");


        binding();
        initFirebaseService();
        listeners();
    }



    private void showStartTimePicker() {

    }


    private void showEndTimePicker() {

    }
    /**
     * Obtener elementos graficos de la vista xml a través de su id.
     */
    public void binding()
    {
        titulo= (EditText) findViewById(R.id.edt_titulo_actividad);
        descripcion=(EditText) findViewById(R.id.edt_descripcion_actividad);

        endTime = findViewById(R.id.to_time);
        endTime.setText(LocalTime.now().plusHours(2).format(formatter));
        startTime = findViewById(R.id.from_time);
        startTime.setText(LocalTime.now().format(formatter));
        feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        String feedbackType = feedbackSpinner.getSelectedItem().toString();
        duration = findViewById(R.id.duration);
        btnCreate =(Button) findViewById(R.id.btn_crearActividad);
    }
    public void listeners(){

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        showStartTimePicker();


            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showEndTimePicker();


            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario = validarCampos();
                if (usuario!=null) {
                    registroUsuario(usuario);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                setearEdt();
            }
        });

    }
    public void initFirebaseService(){
        FirebaseApp.initializeApp(this);
        firebase = FirebaseDatabase.getInstance();
        reference= firebase.getReference();
    }
}