package com.example.sportsmedia;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sportsmedia.models.Actividad;
import com.example.sportsmedia.models.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
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



        binding();
        initFirebaseService();
        listeners();
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
                Actividad actividad = validarCampos();
                if (actividad!=null) {
                    registroActividad(actividad);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
                setearEdt();
            }
        });

    }

    private void showStartTimePicker() {
        showDialog();
    }


    private void showEndTimePicker() {
        showDialog();
    }

    /**
     * Metodo que visualiza el TimePicker
     */
    private void showDialog() {
        new TimePickerFragment().show(getSupportFragmentManager(), "time-picker");
    }
    private void setearEdt() {
    }

    private void registroActividad(Actividad actividad) {
    }

    private Actividad validarCampos() {
        return new Actividad();
    }

    public void initFirebaseService(){
        FirebaseApp.initializeApp(this);
        firebase = FirebaseDatabase.getInstance();
        reference= firebase.getReference();
    }
}