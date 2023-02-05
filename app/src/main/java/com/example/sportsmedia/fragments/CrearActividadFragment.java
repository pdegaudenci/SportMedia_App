package com.example.sportsmedia.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sportsmedia.Auxiliar;
import com.example.sportsmedia.HomeActivity;
import com.example.sportsmedia.R;
import com.example.sportsmedia.dto.Actividad;
import com.example.sportsmedia.controller.FirebaseController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Clase para crear una Actividad e insertarla en firebase storage. La clase hereda de {@link Fragment
 * Use the {@link CrearActividadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearActividadFragment extends Fragment {
    private FirebaseController firebase;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
    private TextView txt_duration;
    private TextView startTime;
    private TextView endTime;
    private EditText titulo;
    private EditText descripcion;
    private TextView edt_fecha;
    private Button btnCreate;
    private Spinner feedbackSpinner;
    private View vista;

    Long duration=null;
    CheckBox chk_equipo;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CrearActividadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearActividadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearActividadFragment newInstance(String param1, String param2) {
        CrearActividadFragment fragment = new CrearActividadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment -- Retorna la vista del fichero xml del fragment, mediante el cual accederemos para hacer el binding
        vista=inflater.inflate(R.layout.fragment_crear_actividad, container, false);

        // Inicializo el servicio de firebase
        firebase = new FirebaseController("Actividades",getActivity().getApplicationContext());

        // Vinculo elementos del formulario del registro del fichero de recurso xml con la capa logica
        binding();

        // Carga de los listeners de los handlers de cada elemento vinvulado
        listeners();
        return vista;
    }

    /**
     * Obtener elementos graficos de la vista xml a través de su id.
     */
    public void binding()
    {
        titulo= (EditText) vista.findViewById(R.id.edt_titulo_actividad);
        descripcion=(EditText) vista.findViewById(R.id.edt_descripcion_actividad);

        // Seleccion de fecha y hora de inicio y fin de actividad
        edt_fecha = (TextView) vista.findViewById((R.id.edt_fecha_actividad));
        endTime = (TextView) vista.findViewById(R.id.to_time);
        endTime.setText(LocalTime.now().plusHours(2).format(formatter));
        startTime =(TextView)  vista.findViewById(R.id.from_time);
        startTime.setText(LocalTime.now().format(formatter));

        // Lista desplegable (Spinner) con valor de comunidad autonoma
        feedbackSpinner = (Spinner) vista.findViewById(R.id.SpinnerFeedbackType);

        chk_equipo =(CheckBox) vista.findViewById(R.id.chk_equipo);

        txt_duration = (TextView) vista.findViewById(R.id.duration);
        btnCreate =(Button) vista.findViewById(R.id.btn_crearActividad);
    }

    /**
     * Metodo que carga los listener que gestionarán los eventos de cada elemento de la vista
     */
    public void listeners(){

        edt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.edt_fecha_actividad:
                        showDatePickerDialog();
                        break;
                }
            }
        });
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
                String horaInicio= startTime.getText().toString();
                String horaFin= startTime.getText().toString();
                // En caso de que sea un horario correcto, se calcula diferencia en minutos y se seta el TextView corrspondiente con la duracion
                if(Auxiliar.validarHorario(horaInicio,horaFin)){
                    duration=Auxiliar.calcularDiferenciaMinutos(horaInicio,horaFin);
                    setearDuracion(txt_duration,duration);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Horario de fin / inicio incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Boton de creacion de actividad
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Actividad actividad = validarCampos();
                if (actividad!=null) {

                    registroActividad(actividad);
                    FragmentManager manager= getActivity().getSupportFragmentManager();

                    FragmentTransaction ft= manager.beginTransaction().replace(R.id.fragment, new CrearActividadFragment());
                    ft.commit();
                }
                else{

                }

            }
        });

    }
    /**
     * Visualiza el TimePicker para indicar hora de inicio de la actividad
     */
    private void showStartTimePicker() {
        showDialog(startTime);
    }

    /**
     * Visualiza el TimePicker para indicar hora de fin de la actividad
     */
    private void showEndTimePicker() {
        showDialog(endTime);


    }

    /**
     * Metodo que visualiza un fragment que contiene un  TimePicker para seleccion de hora y minutos
     */
    private void showDialog(TextView txt) {

        final Calendar getDate = Calendar.getInstance();
        // Instancia objeto timePickerDialog(Muestra seleccion de horas y minutos)
        // Como argumento en su constructor recibe, la actividad actual y una interfaz anonima,
        // para establecer un listener, y sobreescribir el metodo OnTimeSet a fin de guardar,
        // los datos seleccionados por el usuario (hora y minutos)
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //getDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                //getDate.set(Calendar.MINUTE, minute);
                //SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm a");
                //timeformat.format(getDate.getTime());
                final String horarioSeleccioando;
                if(hourOfDay < 10)
                    horarioSeleccioando= "0"+ hourOfDay + ":" + (minute);
                else
                    horarioSeleccioando=hourOfDay + ":" + (minute);
                txt.setText(horarioSeleccioando);
            }
        }, getDate.get(Calendar.HOUR_OF_DAY), getDate.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }



    /**
     * Metodo que carga un fragment que contiene un DatePicker para seleccionar la fecha de la actividad y
     * setear el valor del TextView correspondiente.
     */
    private void showDatePickerDialog() {
        // Instancia DatePickerFragment a traves de su clase Factory y se pasa por parametro un listener para
        // cuando el usuario seleccione la fecha , lo setee en su correspondiente TextView
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                edt_fecha.setText(selectedDate);

            }
        });
        // Visualizo fragment que contiene DatePicker
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }


    /**
     * Metodo que obtiene los datos de los elementos vinculados de la vista , los valida e instancia un Objeto de la
     * clase Actividad
     * @return Actividad objeto de la Clase Actividad creado y validado
     */
    private Actividad validarCampos() {
        Actividad actividad = null;

        // Obtencion de los valores de cada elemento de la vista
        String titulo=this.titulo.getText().toString();
        String descripcion=this.descripcion.getText().toString();
        String comunidad=feedbackSpinner.getSelectedItem().toString();
        String fecha = edt_fecha.getText().toString();
        boolean equipoEspecial=this.chk_equipo.isChecked();
        String horaInicio=this.startTime.getText().toString();
        String horaFin=this.endTime.getText().toString();;
        String usuario=HomeActivity.getUserglobal().getUsername();

        // Si algun campo obligatorio no es llenado , visualiza un mensaje de tipo Toast
        if(titulo.equals("")||descripcion.equals("")||comunidad.equals("")||horaInicio.equals("")|| horaFin.equals("") )
            Toast.makeText(getActivity().getApplicationContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
        // En caso de que la fecha seleccionada sea menor a la actual, lo indica al usuario mediante mensaje Toast
        if(!Auxiliar.validarRangoFechas(fecha,null))
            Toast.makeText(getActivity().getApplicationContext(), "La fecha de actividad debe ser posterior a la fecha actual", Toast.LENGTH_SHORT).show();
        // Valida que el horario de inicio de la actividad sea menor al horario de fin de la actividad
        if(!Auxiliar.validarHorario(horaInicio,horaFin))
            Toast.makeText(getActivity().getApplicationContext(), "El horario de inicio debe ser menor al horario de fin", Toast.LENGTH_SHORT).show();


        // Se instancia objeto de la clase Actividad y se setean sus atributos. con los valores de los elementos de la vista
        else{
            // Instacia objeto de la clase Actividad y seta sus atributos, a través de los setters.
            actividad = new Actividad();
            actividad.setUid(UUID.randomUUID().toString());
            actividad.setUsuario(usuario);
            actividad.setTitulo(titulo);
            actividad.setDescripcion(descripcion);
            actividad.setComunidad(comunidad);
            actividad.setEquipoEspecial(equipoEspecial);
            actividad.setDuracion(duration);

        }
        return actividad;

    }

    /**
     * Metodo que setea el textview con la duracion de una actividad en formato de horas y minutos
      */
    private void setearDuracion(TextView txt_duration, Long duration) {
        String formato = "%02d:%02d";
        long horasReales = TimeUnit.MINUTES.toHours(duration);
        long minutosReales = TimeUnit.MINUTES.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(duration));
        txt_duration.setText(String.format(formato, horasReales, minutosReales));
    }


    /**
     * Metodo que agrega un documento  a la coleccion Actividades en firebase storage
     * @param actividad Actividad que debe ser insertada e la BBDD de firebase
     * @return boolean true si la actividad fue insertada en la coleccion correctamente
     */
    private boolean registroActividad(Actividad actividad) {
        // Realiza la insercion, devuelve un objeto de la clase Task y se invoca a su metodo isSucccesful para evaluar si se realizó con exito la operacion
        boolean correcta= firebase.getReference().child("Actividades").child(actividad.getUid()).setValue(actividad).isSuccessful();
        return correcta;
    }
}