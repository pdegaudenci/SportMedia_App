package com.example.sportsmedia.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Spinner;

import android.widget.Toast;

import com.example.sportsmedia.R;
import com.example.sportsmedia.controller.FirebaseController;
import com.example.sportsmedia.dto.Actividad;
import com.example.sportsmedia.repository.FirebaseCRUD;
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

import java.time.LocalTime;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltrarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltrarFragment  extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {
    private View vista;
    private GoogleMap nMap;
    private LatLng latLong;
    private SupportMapFragment mapFragment;
    private FirebaseController firebase;
    private FirebaseCRUD firebaseCRUD;

    private Spinner feedbackSpinner;
    private String[] provincias;
    private ArrayList<LatLng> listaCoordenadas=new ArrayList<>();

    private Marker marker;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FiltrarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FiltrarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FiltrarFragment newInstance(String param1, String param2) {
        FiltrarFragment fragment = new FiltrarFragment();
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

        vista=inflater.inflate(R.layout.fragment_filtrar, container, false);
        cargarCoordenadas();
        // Inflate the layout for this fragment
        // Vinculo elementos del formulario del registro del fichero de recurso xml con la capa logica
        binding();
        firebaseCRUD.cargaDatosActividades();
        // Carga de los listeners de los handlers de cada elemento vinvulado
        listeners();
        cargarMap();
        // Inflate the layout for this fragment

        return vista;
    }

    private void cargarCoordenadas() {
       /* Andalucía: 37.3666 -5.983
        Asturias: 43.333 -6
        Aragón: 41 -1
        Islas Baleares: 39.30 3
        Canarias: 28.4666 -16.25
        Cantabria: 43.333 -4
        Castilla la Mancha:39.8667 -4.017
        Castilla y León: 41.3833 -4.45
        Cataluña: 41.81667 1.4667
        Extremadura: 39.2 -6.15
        La Rioja: 42.25 -2.5
         Madrid: 40.5 -3.666

        Murcia: 38 -1.8333
        Navarra: 42.8166 -1.65
        Galicia: 42.75 -7.88333

        País Vasco: 42.989 -2.628
        Valencia: 39.5 -0.75*/
        listaCoordenadas.add(new LatLng(37.3666, -5.983));
        listaCoordenadas.add(new LatLng(43.333, -6));
        listaCoordenadas.add(new LatLng(41, -1));

        listaCoordenadas.add(new LatLng(39.30, 3));
        listaCoordenadas.add(new LatLng(28.4666, -16.25));
        listaCoordenadas.add(new LatLng(43.333, -4));

        listaCoordenadas.add(new LatLng(39.8667, -4.017));
        listaCoordenadas.add(new LatLng(41.3833 ,-4.45));
        listaCoordenadas.add(new LatLng(41.81667, 1.4667));

        listaCoordenadas.add(new LatLng(39.2 ,-6.15));
        listaCoordenadas.add(new LatLng(42.25 ,-2.5));
        listaCoordenadas.add(new LatLng(40.5 ,-3.666));

        listaCoordenadas.add(new LatLng(38, -1.8333));
        listaCoordenadas.add(new LatLng(42.8166, -1.65));
        listaCoordenadas.add(new LatLng(42.75 ,-7.88333));


        listaCoordenadas.add(new LatLng(42.989, -2.628));
        listaCoordenadas.add(new LatLng(42.8166, -1.65));
        listaCoordenadas.add(new LatLng(39.5 ,-0.75));
    }

    /*
    Carga listener para seleccion de un elemento de la lista , y el zoom de la camara se posiciona en
    esa comunidad autonoma seleccionada.
     */
    private void listeners() {

        feedbackSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LatLng latLng= listaCoordenadas.get(position);
                // Cargo el mapa de un lugar detrminado dado por la variable latLng y con un nivel de zoom predeterminado
                nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nada fue seleccionado
            }
        });
    }

    public void binding()
    {
        // Inicializo el servicio de firebase
        firebase = new FirebaseController("Actividades", getActivity().getApplicationContext());
        firebaseCRUD=new FirebaseCRUD(firebase);
        // Lista desplegable (Spinner) con valor de comunidad autonoma que sera condicion de filtro
        feedbackSpinner = (Spinner) vista.findViewById(R.id.filtrar_comunidad);

        provincias=getResources().getStringArray(R.array.lista_comunidades);
    }

    private void cargarMap() {
        // Obtengo fragment del fichero xml fragment_google_map donde cargaré el mapa
        mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_filtro);
        //mapFragment = SupportMapFragment.newInstance();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_filtro, mapFragment)
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap=googleMap;
        firebaseCRUD.cargaDatosActividades();

        // Establezco valores minimos y maximo de de zoom
        nMap.setMinZoomPreference(0.3f);
        nMap.setMaxZoomPreference(20.0f);

        // Parametros de configuracion de los controles del mapa de la API
        UiSettings uiSettings = nMap.getUiSettings();
        // Establezco preferencias de zoom del mapa
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabledDuringRotateOrZoom(true);

        // Cargo el mapa de un lugar detrminado
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.502,-3.683),6));
        Circle circle = nMap.addCircle(new CircleOptions()
                .center(new LatLng(40.502,-3.683))
                .radius(10000)
                .strokeColor(Color.RED)
                .fillColor(Color.WHITE));
        // Verificacion de permisos para obtner posicion actual
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.INTERNET}, 5622);
        }
        LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.INTERNET}, 5622);
                }
                nMap.setMyLocationEnabled(true);
            }
        });

        cargarMarkers(FirebaseCRUD.getActividades());


        // listener para gestionar el evento de hacer un click sobre el mapa de la API de Google Map (Debe implementar la interfaz googleMap.OnMapClickListener)
        nMap.setOnMapClickListener(this);
        // listener para gestionar el evento de hacer un click largo sobre el mapa de la API de Google Map (Debe implementar la interfaz GoogleMap.OnMapLongClickListener )
        nMap.setOnMapLongClickListener(this);
        // listener que escucha evento de hacer click sobre ventana de informacion (Se debe implementar interfaz GoogleMap.OnInfoWindowClickListener)
        nMap.setOnInfoWindowClickListener(this);


    }

    /**
     * Carga un conjunto de markers al mapa, correspondientes a cada una de las actividades disponibles
     *
     * @param actividades
     */
    private void cargarMarkers(ArrayList<Actividad> actividades) {

        for (Actividad actividad:actividades){
            LatLng latLng =new LatLng(actividad.getLatitud(),actividad.getLongitud());
            nMap.addMarker(new MarkerOptions().position(latLng).title(actividad.getNombreLugar()));
        }


    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}