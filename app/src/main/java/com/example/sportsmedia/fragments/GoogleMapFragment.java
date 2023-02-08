package com.example.sportsmedia.fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportsmedia.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoogleMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoogleMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
// implementsOnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener
    private TextView txt_direccion;
    private GoogleMap nMap;
    PlacesClient placesClient;
    // Codigo que representa direccion ingresada por usuario
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    // Datos de coordenadas de la ubicacion que seleccione usuario para cargar en el mapa
    private LatLng latLong;




    private Marker marker;
    private View vista;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GoogleMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoogleMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoogleMapFragment newInstance(String param1, String param2) {
        GoogleMapFragment fragment = new GoogleMapFragment();
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
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_google_map, container, false);
        binding();
        cargarAPIGooglePlace();
        cargarMap();
        cargarListener();
        return vista;
    }


    /**
     * Metodo que Inicializa la API de Googl Places para el contexto de la aplicación con la clave API especificada en un recurso string de la carpeta values.
     */
    private void cargarAPIGooglePlace() {
        // Initialize the SDK con Api Key generada en la consola de google Cloud(Credenciales)
        if(!Places.isInitialized())
            Places.initialize(getActivity().getApplicationContext(), getString(R.string.apikey));
        // Create a new PlacesClient instance
        placesClient = Places.createClient(getActivity().getApplicationContext());
    }


    private void binding() {
        txt_direccion = vista.findViewById(R.id.txt_direccion);
        txt_direccion.setText("Inidicar direccion");

    }

    /**
     * Metodo que carga el mapa de la API de google maps en un fragment
     * Este fragment administra totalmente la creación, actualización y destrucción de los mapas en la vista.
     */
    private void cargarMap() {
        // Obtengo fragment del fichero xml fragment_google_map donde cargaré el mapa
        SupportMapFragment mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);

        // método de asociación getMapAsync()  registra la actividad como escucha.
        // Invoca al metodo onMapReady () , por lo que la Clase de este Fragment debe implementar la interfaz OnMapReadyCallback
        mapFragment.getMapAsync(this);
    }
    /**
     * Metodo que se ejecuta , una vez el mapa de la API de google Maps se ha cargado y nos permite establecer parametros de configuracion
     *
     * @param googleMap el objeto de la Clase GoogleMap que contiene el mapa cargado (obtenido con metodo getAsync)
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Inicializo el objeto nMap para acceder al mapa de Google maps
        nMap=googleMap;

        // Parametros de configuracion del mapa de la API
        UiSettings uiSettings = nMap.getUiSettings();
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        // Establezco preferencias de zoom del mapa
        uiSettings.setZoomControlsEnabled(true);

        // listener para gestionar el evento de hacer un click sobre el mapa de la API de Google Map (Debe implementar la interfaz googleMap.OnMapClickListener)
        this.nMap.setOnMapClickListener(this);
        // listener para gestionar el evento de hacer un click largo sobre el mapa de la API de Google Map (Debe implementar la interfaz GoogleMap.OnMapLongClickListener )
        this.nMap.setOnMapLongClickListener(this);
        // listener que escucha evento de hacer click sobre ventana de informacion (Se debe implementar interfaz GoogleMap.OnInfoWindowClickListener)
        this.nMap.setOnInfoWindowClickListener(this);
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

    private void cargarListener() {
        // Carga el listener del TextView para abrir el cuadro de busqueda de APi Place de google
        txt_direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             iniciarAutocompletadoMaps();
            }
        });
    }

    /**
     * Metodo que lanza una actividad que se superpone para que el usuario ingrese una direccion y usando la
     * API de Google Places se autocpmplete con resultados de direcciones.
     */
    private void iniciarAutocompletadoMaps(){

        // Definir que tipo de datos  deseamos obtener a partir del Place seleccionado : Id, nombre y direccion completa
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS_COMPONENTS);

        // DEfinir intent para generar Activity donde usuario realizará busqueda de lugar
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(getActivity().getApplicationContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);


    }

    /**
     * Metodo que recibe resultado del Autocompletado de direccion ingresada por usuario
     * @param requestCode int con  código de solicitud proporcionado originalmente a
                         *  startActivityForResult(), que le permite identificar quién es este
     *      * resultado de donde vino.
     * @param resultCode El código de resultado entero devuelto por la actividad secundaria
     *                      a través de su setResult().
     * @param data An Intent, que retorna los datos como resultado
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Proceso resultado del fragment de Autocomplete place
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            // Si el resultado devuelto por lo ingresado por el usuario en el Autocomplete Place es correcto
            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);
                System.out.println(("Place: " + place.getName() + ", " + place.getId()));
                // Seteo contenido de textview con direccion seleccionado por usuario (resultado del Autocomplete place)
                // En este caso el contenido del TextView sera el nombre del lugar seleccionado por el usuario
                txt_direccion.setText(String.format(getString(R.string.direccion_actividad)+" %s", place.getAddress()));

                // Seteo datos de coordenada de ubicacion seleccionada (Seran usados para cargar posicion en map de Google Maps)
                latLong=place.getLatLng();

                agregarMarkerMapa(latLong);
                // Guardo datos de ubicacion del lugar seleccionado
                guardarDatosUbicacion(place);

            }
            // Si el resultado devuelto por el Autocomplete Place es un error (No se pudo conectar
            // correctamente al servicio de Google Platform Map, por ejemplo)
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // Obtengo un status code del intent recibido y lo visualizo por consola
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getActivity().getApplicationContext(),"Error en la opcion de autocompletado",Toast.LENGTH_LONG).show();
                System.out.println(status.getStatusMessage());
            }
            // Si el usuario cancela la busqueda de direccion en el fragment de Autocomplete
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity().getApplicationContext(),"Busqueda cancelada",Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Metodo que agrega un marcador al mapa , en la posicion recibida por parametro . En caso de que exista ya el marcador , lo elimina y lo actualiza con la nueva
     * posicion
     * @param latLong objeto de la clase LatLng con las coordenadas (latitud y longitu) de la posicion a marcar
     */
    private void agregarMarkerMapa(LatLng latLong) {
        System.out.println(""+latLong.latitude);
        System.out.println(""+latLong.longitude);
        // Si existe un marcador en el mapa lo elimina
        if (marker!=null)
            marker.remove();
        // Agregar marcador al mapa en la posicion recibida por parametro , el cual se puede desplazar (draggable con valor true)
        marker =nMap.addMarker(new MarkerOptions().position(latLong).title("MAdrid").draggable(true));

        //La cámara es el punto de vista que refleja una cantidad de espacio o volumen en los mapas de la API de Google Maps.
        // Genera una instancia CameraUpdate que ubica el objetivo de la cámara en las nuevas coordenadas
        nMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
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
    }




}