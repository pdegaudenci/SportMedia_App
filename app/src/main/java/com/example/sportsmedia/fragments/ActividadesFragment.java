package com.example.sportsmedia.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sportsmedia.HomeActivity;
import com.example.sportsmedia.R;
import com.example.sportsmedia.adapter.ActividadesAdapter;
import com.example.sportsmedia.controller.FirebaseController;
import com.example.sportsmedia.dto.Actividad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActividadesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActividadesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ActividadesAdapter mAdapter;
    private FirebaseController firebase;
    private View vista;
    private ArrayList<Actividad> myDataSet;
    private static ArrayList<Actividad> actividades=new ArrayList<>();

    private ViewGroup grupoVistas;
    private static ArrayList<Actividad> misActividades=new ArrayList<>();
    private static ArrayList<Actividad> misInscripciones=new ArrayList<>();
    private String textoVacio;
    private TextView textView_vacio;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn_crearActividad;
    public View.OnClickListener listener;
    private String tipo="";

    public ActividadesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActividadesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActividadesFragment newInstance(String param1, String param2) {
        ActividadesFragment fragment = new ActividadesFragment();
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
            Bundle bundle=getArguments();
            tipo=bundle.getString("tipo");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflar el layout de este fragment
        vista= inflater.inflate(R.layout.fragment_actividades, container, false);
        // Binding con la interfaz recyclerView y con la BBDD firebase
        binding();
        loadData();// Asigno al recyclerView el adapter que gestiona las vistas
        mAdapter = new ActividadesAdapter(myDataSet,getContext(),tipo);
        // Asigno al recyclerView el adapter que gestiona las vistas
        mRecyclerView.setAdapter(mAdapter);
        if(mAdapter.getItemCount()==0){
            textView_vacio = (TextView) vista.findViewById(R.id.txt_vacio);
            textView_vacio.setVisibility(View.VISIBLE);
            textView_vacio.setText(textoVacio);
        }
        btn_crearActividad =vista.findViewById(R.id.btn_crearActividad2);
        if(tipo.equalsIgnoreCase("misActividades"))
        {   // Visibilizo el boton
            btn_crearActividad.setVisibility(View.VISIBLE);
            btn_crearActividad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { btn_crearActividad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentManager manager= getActivity().getSupportFragmentManager();
                            FragmentTransaction ft= manager.beginTransaction().replace(R.id.fragment, new CrearActividadFragment());
                            ft.commit();
                        }
                    });
                }
            });
        }

        return vista;
    }

    private void binding(){
        btn_crearActividad =vista.findViewById(R.id.btn_crearActividad2);
        mRecyclerView=  (RecyclerView) vista.findViewById(R.id.recycler_Misactividades);
        mRecyclerView.setHasFixedSize(true);
        // Creo un controlller donde
        firebase = new FirebaseController("Actividades",getContext());
    }
    /**
     * Metodo que asocia la fuente de datos del adapter con un ArrayList de Actividades especifico
     * Hay 3 listas posibles para ser fuente de datos: misIncripciones, actividades o misActividades
     */
    private void loadData() {
        cargaDatosActividades();

        if(tipo.equalsIgnoreCase("inscripciones")){
            textoVacio="No estas suscripto a ninguna actividad";
            myDataSet= ActividadesFragment.misInscripciones;
        }
        else if(tipo.equalsIgnoreCase("sociales"))
        {
            myDataSet= ActividadesFragment.actividades;
         textoVacio="No hay actividades disponibles";

        }
        else{
            textoVacio="No has creado ninguna actividad";
            myDataSet= ActividadesFragment.misActividades;
        }
    }

    /**
     * Metodo que solicita datos de la coleccion Actividades de firebase y los carga en cada
     * uno de los ArrayList estaticos declarados
     */
    private void cargaDatosActividades(){
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Seteo las activididades cargadas previamente
                ActividadesFragment.misActividades.clear();
                ActividadesFragment.misInscripciones.clear();
                ActividadesFragment.actividades.clear();
                // Obtengo los nodos hijos de la referencia (Documentos de la coleccion actividades)
                for (DataSnapshot element: snapshot.getChildren()){
                    // Por cada elemento , obtengo su valor y lo parseo en base a mi POJO Actividad
                    Actividad  activityAux= element.getValue(Actividad.class);
                    if(activityAux.getUsuario().equals(HomeActivity.getUserglobal().getUsername()))
                        ActividadesFragment.misActividades.add(activityAux);
                    else {
                        if(HomeActivity.getUserglobal().getIdActividades().contains(activityAux.getUid()))
                            ActividadesFragment.misInscripciones.add(activityAux);
                        else
                            ActividadesFragment.actividades.add(activityAux);  } }
                if(mAdapter.getItemCount()!=0)
                {  if(textView_vacio!=null)
                        textView_vacio.setVisibility(View.INVISIBLE);
                }
                mAdapter.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
                }

}