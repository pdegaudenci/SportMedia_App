package com.example.sportsmedia.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportsmedia.HomeActivity;
import com.example.sportsmedia.R;
import com.example.sportsmedia.controller.FirebaseController;
import com.example.sportsmedia.dto.Actividad;
import com.example.sportsmedia.dto.Usuario;
import com.example.sportsmedia.utils.AuxiliarUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityDetailFragment extends Fragment {

    private TextView txt_titulo,txt_descripcion,txt_comunidad,txt_equipo,txt_fecha,txt_horaio,txt_lugar,txt_direccion,txt_inscripto;
    private View vista;
    private Button btn_borrar;
    private Button btn_suscripcion;
    private Button btn_borrar_suscripcion;
    FirebaseController firebase;
    FirebaseController firebaseUsuario;
    private Actividad actividad;
    private DatabaseReference db;
    private String tipo="";
    private Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ActivityDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivityDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityDetailFragment newInstance(String param1, String param2) {
        ActivityDetailFragment fragment = new ActivityDetailFragment();
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
        // Obtengo controlador de firebase para poder interactuar con mi BBDD
        firebase= new FirebaseController("Actividades",getContext());
        firebaseUsuario= new FirebaseController("Usuarios",getContext());

        // Gestiono evento del boton atrás del dispositivo
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                int count = getFragmentManager().getBackStackEntryCount();

                if (count == 0) {

                    getFragmentManager().popBackStack();
                } else {
                    getFragmentManager().popBackStack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * Metodo que se ejecuta al visualizar el fragment en la pantalla, vinculando la variable
     * de tipo View con el fichero xml que contiene el diseño del fragment , a través de inflate.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return View con el objeto inflado que representa al
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_activity_detail, container, false);
        // Obtengo argumentos recibidos por el fragment  y los almaceno en un objeto Bundle
        Bundle datos= getArguments();

        // Obtengo tipo de actividad provista por argumentos del fragment
        if(datos!=null)
            tipo =datos.getString("tipo");
        binding(vista);
        obtenerDetallesActividad(savedInstanceState);
        listener();
        // Referencia a nodo que de la BBDD que contiene actividad
        db=firebase.getReference().child(actividad.getUid());
        return vista;
    }

    /**
     * Obtiene parametros enviados con fragment que invocó al actual
     * a traves de un objeto Bundle y crea una nueva actividad con la actividad
     * seleccionada en el recyclerView
     * @param savedInstanceState Bundle
     */
    private void obtenerDetallesActividad(Bundle savedInstanceState) {

        Bundle datos= getArguments();
        actividad =(Actividad)datos.getSerializable("actividad");
        txt_titulo.setText(actividad.getTitulo());
        txt_descripcion.setText("Descripcion:"+actividad.getDescripcion());
        txt_comunidad.setText("Comunidad autonoma: "+actividad.getComunidad());
        txt_equipo.setText("Necesita equipo especial:"+(actividad.isEquipoEspecial()?"Si":"No"));
        txt_fecha.setText("Fecha:"+actividad.getFecha());
        txt_horaio.setText("Horario : Desde "+actividad.getHoraInicio()+" hrs. hasta "+actividad.getHoraFin()+ " hrs.");
        txt_lugar.setText("Lugar: "+actividad.getNombreLugar());
        txt_direccion.setText("Direccion: "+actividad.getDireccion());
        Integer inscriptos=actividad.getCantPersonas();
        txt_inscripto.setText("Cantidad de personas inscriptas: "+(inscriptos==null?0:inscriptos));
    }

    /**
     * Vinculacion de los elementos del fichero de diseño con los correspondientes variables del fragment
     * @param vista Representacion del layout en un objeto de la vista de la clase View
     */
    private void binding(View vista) {
        txt_titulo=vista.findViewById(R.id.txt_titulo);
        txt_descripcion = vista.findViewById(R.id.txt_descripcion);
        txt_comunidad = vista.findViewById(R.id.txt_comunidad);
        txt_equipo= vista.findViewById(R.id.txt_equipo);
        txt_fecha= vista.findViewById(R.id.txt_fecha);
        txt_horaio= vista.findViewById(R.id.txt_horario);
        txt_lugar= vista.findViewById(R.id.txt_lugar);
        txt_direccion= vista.findViewById(R.id.txt_direccion);
        txt_inscripto= vista.findViewById(R.id.txt_cantidadPersonas);
        btn_borrar=vista.findViewById(R.id.btn_borrarActividad);
        btn_suscripcion=vista.findViewById(R.id.btn_subscripcion);
        btn_borrar_suscripcion=vista.findViewById(R.id.btn_borrar_subscripcion);

        // En funcion del tipo de actividades (Actividades del usuario, sociales disponibles y actividades subscriptas por el usuario) , visualizo uno de los 3 posibles botones
        if(tipo.equals("sociales")){

            btn_suscripcion.setVisibility(View.VISIBLE);
            btn_borrar.setVisibility(View.INVISIBLE);
            btn_borrar_suscripcion.setVisibility(View.INVISIBLE);
        }
        else if(tipo.equals("inscripciones")){

            btn_borrar_suscripcion.setVisibility(View.VISIBLE);
            btn_borrar.setVisibility(View.INVISIBLE);
            btn_suscripcion.setVisibility(View.INVISIBLE);
        }
        else {

            btn_borrar.setVisibility(View.VISIBLE);
            btn_suscripcion.setVisibility(View.INVISIBLE);
            btn_borrar_suscripcion.setVisibility(View.INVISIBLE);
        }

    }

    private void listener(){
        btn_borrar_suscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uuid= actividad.getUid();
                Usuario usuario=HomeActivity.getUserglobal();
                String idUsuario=HomeActivity.getUserglobal().getUid();
                DatabaseReference referenciaActividades= firebase.getReference().child(uuid);
                DatabaseReference referencia=firebaseUsuario.getReference().child(idUsuario);
                // Mapa con los valores a actualizar del usuario
                Map<String, Object> map_usuario = new HashMap<>();
                // Mapa con los valores a actualizar de la actividad
                Map<String, Object> map_Actividad = new HashMap<>();
                // Agrego id de la actividad al ArrayList de actividades del usuario
                usuario.getIdActividades().remove(uuid);

                map_usuario.put("uid", usuario.getUid());
                map_usuario.put(" username", usuario.getUsername());
                map_usuario.put("nombre", usuario.getNombre());
                map_usuario.put("apellido", usuario.getApellido());
                map_usuario.put("password", usuario.getPassword());
                map_usuario.put("fechanac", usuario.getFechanac());
                map_usuario.put("email", usuario.getEmail());

                map_usuario.put("idActividades", usuario.getIdActividades());
                map_Actividad.put("cantPersonas",actividad.getCantPersonas()-1);
                // Actualiza la BBDD
                referencia.updateChildren(map_usuario);
                referenciaActividades.updateChildren(map_Actividad);
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_red,new HomeFragment());

            }
        });
        btn_suscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uuid= actividad.getUid();

                Usuario usuario=HomeActivity.getUserglobal();
                String idUsuario=HomeActivity.getUserglobal().getUid();
                DatabaseReference referenciaActividades= firebase.getReference().child(uuid);
                DatabaseReference referencia=firebaseUsuario.getReference().child(idUsuario);
                // Mapa con los valores a actualizar del usuario
                Map<String, Object> map_usuario = new HashMap<>();
                // Mapa con los valores a actualizar de la actividad
                Map<String, Object> map_Actividad = new HashMap<>();
                // Agrego id de la actividad al ArrayList de actividades del usuario
                usuario.getIdActividades().add(uuid);

                        /*map_usuario.put("uid", usuario.getUid());
                        map_usuario.put(" username", usuario.getUsername());
                        map_usuario.put("nombre", usuario.getNombre());
                        map_usuario.put("apellido", usuario.getApellido());
                        map_usuario.put("password", usuario.getPassword());
                        map_usuario.put("fechanac", usuario.getFechanac());
                        map_usuario.put("email", usuario.getEmail());*/

                map_usuario.put("idActividades", usuario.getIdActividades());
                map_Actividad.put("cantPersonas",actividad.getCantPersonas()+1);

                // Actualiza la BBDD de usuarios (Agrego actividad a su lista de actividades suscriptas) y Actividades
                referencia.updateChildren(map_usuario);
                referenciaActividades.updateChildren(map_Actividad);
                //Toast.makeText(getActivity().getApplicationContext(), "Actualizando..", Toast.LENGTH_SHORT).show();
                // vuelvo al fragment anterior

                // Volvemos al home
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_red,new HomeFragment());

            }
        });


        btn_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Le paso la actividad contenedora del actual fragment
                boolean borrado = AuxiliarUI.ventanaDialogo("Deseas eliminar esta actividad?",getActivity());

                if(!borrado){
                    db.removeValue();
                    Toast.makeText(getContext(),"Actividad borrada correctamente",Toast.LENGTH_LONG);

                    // vuelvo al fragment anterior
                    getFragmentManager().popBackStack();
                }
                else{
                    Toast.makeText(getContext(),"No se borro actividad",Toast.LENGTH_LONG);
                }
            }
        });
    }



}