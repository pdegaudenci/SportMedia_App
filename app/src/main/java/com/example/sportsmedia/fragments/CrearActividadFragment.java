package com.example.sportsmedia.fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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

import com.example.sportsmedia.utils.Auxiliar;
import com.example.sportsmedia.HomeActivity;
import com.example.sportsmedia.R;
import com.example.sportsmedia.dto.Actividad;
import com.example.sportsmedia.controller.FirebaseController;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Clase para crear una Actividad e insertarla en firebase storage. La clase hereda de {@link Fragment
 * Use the {@link CrearActividadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearActividadFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private FirebaseController firebase;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private TextView txt_duration;
    private TextView startTime;
    private TextView endTime;
    private EditText titulo;
    private EditText descripcion;
    private TextView edt_fecha;
    private Button btnCreate;
    private Spinner feedbackSpinner;
    private View vista;

    private static double latitud;
    private static double longitud;

    private static HashMap<String, String> mapDatos = new HashMap<String, String>();
    private TextView txt_direccion;
    private GoogleMap nMap;
    PlacesClient placesClient;
    private boolean direccionOk=false;
    // Codigo que representa direccion ingresada por usuario
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    // Datos de coordenadas de la ubicacion que seleccione usuario para cargar en el mapa
    private LatLng latLong;
    private SupportMapFragment mapFragment;

    private Marker marker;


    Long duration = null;
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
        vista = inflater.inflate(R.layout.fragment_crear_actividad, container, false);

        // Inicializo el servicio de firebase
        firebase = new FirebaseController("Actividades", getActivity().getApplicationContext());
        // Vinculo elementos del formulario del registro del fichero de recurso xml con la capa logica
        binding();
        // Carga de los listeners de los handlers de cada elemento vinvulado
        listeners();
        cargarMap();
        // Inflate the layout for this fragment
        cargarAPIGooglePlace();
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

        // Lista desplegable (Spinner) con valor de comunidad autonoma que será seteado al ingresar direccion
        feedbackSpinner = (Spinner) vista.findViewById(R.id.SpinnerFeedbackType);


        chk_equipo =(CheckBox) vista.findViewById(R.id.chk_equipo);

        txt_duration = (TextView) vista.findViewById(R.id.duration);
        btnCreate =(Button) vista.findViewById(R.id.btn_crearActividad);
        txt_direccion = vista.findViewById(R.id.txt_direccion);
        txt_direccion.setText("Haz click aqui para ingresar direccion");

    }

    /**
     * Metodo que renderiza mapa de GoogleMaps en un fragment
     */
    private void cargarMap() {
        // Obtengo fragment del fichero xml fragment_google_map donde cargaré el mapa
        // mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map1);
        mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_map1, mapFragment)
                .commit();
        if (mapFragment != null) {
            //// check permissions to access resources /////////
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity().getApplicationContext(), "Sin permisos suficientes", Toast.LENGTH_LONG).show();
                // Solicito al usuario de la aplicacion que otorge permisos para acceder a ubicacion
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.INTERNET}, 5622);

            }
            // método de asociación getMapAsync()  registra la actividad como escucha.
            // Invoca al metodo onMapReady () , por lo que la Clase de este Fragment debe implementar la interfaz OnMapReadyCallback
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Metodo que se ejecuta , una vez el mapa de la API de google Maps se ha cargado y nos permite establecer parametros
     * de configuracion.
     * @param googleMap el objeto de la Clase GoogleMap que contiene el mapa cargado (obtenido con metodo getAsync)
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (googleMap != null) {

            // Inicializo el objeto nMap para acceder al mapa de Google maps
            nMap = googleMap;
            // Establezco valores minimos y maximo de de zoom
            nMap.setMinZoomPreference(0.3f);
            nMap.setMaxZoomPreference(14.0f);
            // Parametros de configuracion de los controles del mapa de la API
            UiSettings uiSettings = nMap.getUiSettings();
            // Establezco preferencias de zoom del mapa
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setZoomGesturesEnabled(true);
            uiSettings.setScrollGesturesEnabled(true);
            uiSettings.setTiltGesturesEnabled(true);

            // Verificacion de permisos para obtner posicion actual
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.INTERNET}, 5622);
            }  LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // Envio solicitud de permisos al usuario para que habilite acceso a su ubicacion y los servicios de ubicacion de google play services
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.INTERNET}, 5622);
                    }
                    nMap.setMyLocationEnabled(true);
                }
            });
            nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.502,-3.683),6));

            // listener para gestionar el evento de hacer un click sobre el mapa de la API de Google Map (Debe implementar la interfaz googleMap.OnMapClickListener)
            nMap.setOnMapClickListener(this);
            // listener para gestionar el evento de hacer un click largo sobre el mapa de la API de Google Map (Debe implementar la interfaz GoogleMap.OnMapLongClickListener )
            nMap.setOnMapLongClickListener(this);
            // listener que escucha evento de hacer click sobre ventana de informacion (Se debe implementar interfaz GoogleMap.OnInfoWindowClickListener)
            nMap.setOnInfoWindowClickListener(this);
        }        else {
            System.err.println("ERROR MAPA NULO");
        }

    }



    @Override
    public void onMapClick(@NonNull LatLng latLng) {


    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

    }

    /**
     * Inicializa una instancia cliente de Places para interactuar con la API Google Places
     */
    private void cargarAPIGooglePlace() {
        // Initialize the SDK con Api Key generada en la consola de google Cloud(Credenciales)
        if(!Places.isInitialized())
            Places.initialize(getActivity().getApplicationContext(), getString(R.string.apikey));
        // Creo instancia de PlaceClient para realizar solicitudes a la API
        placesClient = Places.createClient(getActivity().getApplicationContext());
    }

    /**
     * Metodo que define los campos de interes al seleccionar una direccion ingresada por el usuario
     */
    private void iniciarAutocompletadoMaps(){

        // Definir que tipo de datos  deseamos obtener a partir del Place seleccionado , a fin de guardar esos datos en
        // en el storage o usarlos para visualizar ciertos puntos dato en el maps de Google maps
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG,Place.Field.ADDRESS);

        // DEfinir intent para generar Activity donde usuario realizará busqueda de lugar
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(getActivity().getApplicationContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);


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
                String horaFin= endTime.getText().toString();
                Logger.getLogger("logger").info("Hora inicio:"+horaInicio+"/hora fin:"+horaFin);
                // En caso de que sea un horario correcto, se calcula diferencia en minutos y se seta el TextView corrspondiente con la duracion
                if(Auxiliar.validarHorario(horaInicio,horaFin)){
                    duration=Auxiliar.calcularDiferenciaMinutos(horaInicio,horaFin);
                    Logger.getLogger("logger").info("Duracion de la activad en minutos:"+duration);
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
                    // Verifica que se haya insertado correctamente la actividad en firebase storage
                    if(registroActividad(actividad))
                        Toast.makeText(getActivity().getApplicationContext(), "Actividad creada correctamente", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity().getApplicationContext(), "Error al crear la actividad", Toast.LENGTH_SHORT).show();
                    // Una vez creada la actividad se invoca al fragment de actividades para que se acargado
                    FragmentManager manager= getActivity().getSupportFragmentManager();
                    Bundle bundle=new Bundle();
                    bundle.putString("tipo","misActividades");
                    ActividadesFragment actividadesFragment=new ActividadesFragment();
                    actividadesFragment.setArguments(bundle);
                    FragmentTransaction ft= manager.beginTransaction().replace(R.id.fragment, actividadesFragment);
                    ft.commit();
                }                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Error en algun campo de la actividad", Toast.LENGTH_SHORT).show();
                }            }
        });
        // Abrira una ventana de autocompletado de direcciones
        txt_direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarAutocompletadoMaps();
            }
        });

    }

    /**
     * Metodo que recibe resultado del Autocompletado de direccion ingresada por usuario
     * @param requestCode int con  código de solicitud proporcionado originalmente a startActivityForResult(), que le permite identificar quién es este resultado de donde vino.
     * @param resultCode El código de resultado entero devuelto por la actividad secundaria a través de su setResult().
     * @param data An Intent, que retorna los datos como resultado
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Proceso resultado del fragment de Autocomplete place
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            // Si el resultado devuelto por lo ingresado por el usuario en el Autocomplete Place es correcto
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                // Seteo contenido de textview con direccion seleccionado por usuario (resultado del Autocomplete place).En este caso el contenido del TextView sera el nombre del lugar seleccionado por el usuario
                txt_direccion.setText(String.format("Direccion de la actividad : %s",place.getAddress().toString()));
                // Seteo datos de coordenada de ubicacion seleccionada (Seran usados para cargar posicion en map de Google Maps)
                latLong=place.getLatLng();
                agregarMarkerMapa(latLong);
                // Guardo datos de ubicacion del lugar seleccionado
                guardarDatosUbicacion(place);
            }// Si el resultado devuelto por el Autocomplete Place es un error (No se pudo conectar correctamente al servicio de Google Platform Map, por ejemplo)
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // Obtengo un status code del intent recibido y lo visualizo por consola
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getActivity().getApplicationContext(),"Error en la opcion de autocompletado",Toast.LENGTH_LONG).show();
                System.out.println(status.getStatusMessage());            }
            // Si el usuario cancela la busqueda de direccion en el fragment de Autocomplete
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity().getApplicationContext(),"Busqueda cancelada",Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        String duracion=txt_duration.getText().toString();
        // ---------------- VALIDACIONES -------------------------
        // Si algun campo obligatorio no es llenado , visualiza un mensaje de tipo Toast
        if(titulo.equals("")||descripcion.equals("")||horaInicio.equals("")|| horaFin.equals("") )
            Toast.makeText(getActivity().getApplicationContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
        if (!direccionOk)
            Toast.makeText(getActivity().getApplicationContext(), "Debe indicar una direccion", Toast.LENGTH_SHORT).show();
        // En caso de que la fecha seleccionada sea menor a la actual, lo indica al usuario mediante mensaje Toast
        if(!Auxiliar.validarRangoFechas(fecha,null))
            Toast.makeText(getActivity().getApplicationContext(), "La fecha de actividad debe ser posterior a la fecha actual", Toast.LENGTH_SHORT).show();
        // Valida que el horario de inicio de la actividad sea menor al horario de fin de la actividad
        if(!Auxiliar.validarHorario(horaInicio,horaFin))
            Toast.makeText(getActivity().getApplicationContext(), "El horario de inicio debe ser menor al horario de fin", Toast.LENGTH_SHORT).show();
        // Se instancia objeto de la clase Actividad y se setean sus atributos. con los valores de los elementos de la vista
        else{
            HashMap <String, String> mapa = CrearActividadFragment.getMapDatos();
          // Instacia objeto de la clase Actividad y seta sus atributos, a través de los setters.
            actividad = new Actividad();

            actividad.setUid(UUID.randomUUID().toString());
            actividad.setUsuario(usuario);
            actividad.setTitulo(titulo);
            actividad.setFecha(fecha);
            actividad.setHoraInicio(horaInicio);
            actividad.setHoraFin(horaFin);
            actividad.setDescripcion(descripcion);
            actividad.setComunidad(comunidad);
            actividad.setEquipoEspecial(equipoEspecial);
            actividad.setDuracion(duracion);
            actividad.setDireccion(mapa.get("direccion"));
            actividad.setNombreLugar(mapa.get("nombre"));
            actividad.setLongitud(CrearActividadFragment.getLongitud());
            actividad.setLatitud(CrearActividadFragment.getLatitud());

        }
        return actividad;

    }

    /**
     * Teniendo en cuenta que el formato de direccion devuelto por google Places es : Nombre de calle , numero, CP Provincia, Pais
     * Extraigo solo el dato de provincia para setear el correspondiente atributo de la instancia de la clase Actividad
     * @param direccion
     * @return
     */
    private void obtenerProvinvia(String direccion) {
        String provincia="";
        boolean provinciaEncontrada=false;
        // Voy diviendo String en arrays en funcion de delimitadores y obtengo el elemento correspondiente a la provincia
        provincia = direccion.split(",")[2].split(" ")[2].trim();
        feedbackSpinner.setEnabled(false);

        String[] elementos= getResources().getStringArray(R.array.lista_comunidades);

        // Recorro mi array de comunidades autonomas para comparar cada elemento con la variable provincia
        for(int i=0;i<elementos.length;i++){
            System.out.println("Provincia de direccion:"+provincia);
            System.out.println("Provincia de array:"+elementos[i]);
            System.out.println("Provincia de spinner:"+(String)feedbackSpinner.getItemAtPosition(i));
            if(elementos[i].equalsIgnoreCase((String)feedbackSpinner.getItemAtPosition(i))&&
                    elementos[i].equalsIgnoreCase(provincia))
            {
                feedbackSpinner.setSelection(i);
                provinciaEncontrada=true;
                // Valido correctamente campo de direccion
                direccionOk=true;
                break;
            }
        }
        if(!provinciaEncontrada)
            txt_direccion.setText("Direccion fuera de españa, haz click aqui para ingresar una direccion valida");

    }

    /**
     * Metodo que setea el textview con la duracion de una actividad en formato de XX horas y XX minutos
      */
    private void setearDuracion(TextView txt_duration, Long duration) {
        long horasReales = TimeUnit.MINUTES.toHours(duration);
        long minutosReales = TimeUnit.MINUTES.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(duration));
        System.out.println("Horas:"+horasReales+" minutos: "+minutosReales);
        txt_duration.setText(String.format("La duracion es %02d horas y %02d minutos", horasReales , minutosReales));
    }


    /**
     * Metodo que agrega un documento  a la coleccion Actividades en firebase storage
     * @param actividad Actividad que debe ser insertada e la BBDD de firebase
     * @return boolean true si la actividad fue insertada en la coleccion correctamente
     */
    private boolean registroActividad(Actividad actividad) {
        // Realiza la insercion, devuelve un objeto de la clase Task y se invoca a su metodo isSucccesful para evaluar si se realizó con exito la operacion
        boolean correcta= firebase.getReference().child(actividad.getUid().toString()).setValue(actividad).isSuccessful();

        return correcta;
    }

    public static double getLatitud() {
        return latitud;
    }

    public static void setLatitud(double latitud) {
        CrearActividadFragment.latitud = latitud;
    }

    public static double getLongitud() {
        return longitud;
    }

    public static void setLongitud(double longitud) {
        CrearActividadFragment.longitud = longitud;
    }

    public static HashMap<String, String> getMapDatos() {
        return mapDatos;
    }

    public static void setMapDatos(HashMap<String, String> mapDatos) {
        CrearActividadFragment.mapDatos = mapDatos;
    }
    /**
     * Metodo que agrega un marcador al mapa , en la posicion recibida por parametro . En caso de que exista ya el marcador , lo elimina y lo actualiza con la nueva posicion
     * @param latLong objeto de la clase LatLng con las coordenadas (latitud y longitu) de la posicion a marcar
     */
    private void agregarMarkerMapa(LatLng latLong) {
        // Si existe un marcador en el mapa lo elimina
        if (marker!=null)
            marker.remove();
        // Agregar marcador al mapa en la posicion recibida por parametro , el cual se puede desplazar (draggable con valor true)
        marker =nMap.addMarker(new MarkerOptions().position(latLong).title("Posicion Actividad").draggable(false));
        //La cámara es el punto de vista que refleja una cantidad de espacio o volumen en los mapas de la API de Google Maps.Genera una instancia CameraUpdate que ubica
        // el objetivo de la cámara en las nuevas coordenadas
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong,5));
    }
    /**
     * Metodo que guarda datos de ubicacion y relativos a la direccion seleccionada por el usuario
     * @param place objeto de la clase Place
     */
    private void guardarDatosUbicacion(Place place) {
        // Obtengo los datos de latitud y longitud, los cuales serán guardados en la coleccion de Actividades de firebase storage al momento de crear una nueva actividad
        CrearActividadFragment.setLatitud(place.getLatLng().latitude);
        CrearActividadFragment.setLongitud(place.getLatLng().longitude);
        String name=place.getName();
        String idPlace=place.getId();
        String direccion=place.getAddress();
        HashMap <String, String> map = new HashMap <String, String> ();
        map.put("nombre",name);
        map.put("idPlace",idPlace);
        map.put("direccion",direccion);
        CrearActividadFragment.setMapDatos(map);
    }


}