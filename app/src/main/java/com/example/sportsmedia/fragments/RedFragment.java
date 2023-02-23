package com.example.sportsmedia.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sportsmedia.HomeActivity;
import com.example.sportsmedia.LoginActivity;
import com.example.sportsmedia.R;
import com.example.sportsmedia.adapter.ActividadesAdapter;
import com.example.sportsmedia.controller.FirebaseController;
import com.example.sportsmedia.dto.Actividad;
import com.example.sportsmedia.dto.Usuario;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RedFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ActividadesAdapter mAdapter;
    private FirebaseController firebase;
    private View vista;
    private ArrayList<Actividad> misActividades;

    private Toolbar toolbar;

    private static ArrayList<Actividad> myDataSet=new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RedFragment newInstance(String param1, String param2) {
        RedFragment fragment = new RedFragment();
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
        vista=inflater.inflate(R.layout.fragment_red, container, false);
        // Binding con la interfaz recyclerView y con la BBDD firebase
        //binding();
        setToolbar(vista);

        return vista;
    }
private void binding(){
    toolbar = vista.findViewById(R.id.toolbar_social);
}


    /**
     * Visualiza toolbar en la parte superior del fragment con las opciones del menu
     * y asigna un listener para gestionar el evento al hacer click sobre cada item del toolbar
     *
     * @param view
     */
    public void setToolbar(View view){
        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.toolbar_social);
        //((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_social);
        Menu menu = toolbar.getMenu();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment fragment=null;
                String tipo="";
                boolean transaccion=false;
                switch (item.getItemId()){
                    //Aquí pones tantos 'case' como items tengas en el menú
                    case R.id.disponibles_item:
                        tipo="sociales";
                        transaccion=true;
                        break;
                    case R.id.suscripciones_item:
                        tipo="inscripciones";
                        transaccion=true;
                        break;

                    case R.id.filtrar_item:
                        tipo="filtrar";
                        transaccion=true;
                        break;
                }
                if(transaccion && !tipo.equalsIgnoreCase("filtrar")){
                    // Reutilizacion del fragment ActividadesFragment, el cual reutiliza recyclerView
                    // Y el adapter se conectara a una u otra fuente de datos  en funcion de valor del
                    // objeto Bundle pasado por parametro
                    fragment =new ActividadesFragment();
                    cargarFragment(fragment,tipo);
                    item.setChecked(true);
                }
                else if(tipo.equalsIgnoreCase("filtrar"))
                {
                    fragment =new FiltrarFragment();
                    cargarFragment(fragment,tipo);
                    item.setChecked(true);
                }
                return true;
            }
        });
    }

    /**
     * Metodo que carga un fragmento debajo del toolbar y le proporciona informacion a traves del objeto
     * bundle.
     * @param fragmento
     * @param argumento
     */
    public void cargarFragment(Fragment fragmento,String argumento){
        Bundle bundle = new Bundle();
        bundle.putString("tipo",argumento);
        fragmento.setArguments(bundle);
        if(fragmento!=null)
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_red,fragmento).addToBackStack(null).commit();
}

}