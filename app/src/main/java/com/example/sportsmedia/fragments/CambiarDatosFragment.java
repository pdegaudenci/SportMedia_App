package com.example.sportsmedia.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sportsmedia.HomeActivity;
import com.example.sportsmedia.R;
import com.example.sportsmedia.controller.FirebaseController;
import com.example.sportsmedia.dto.Usuario;
import com.example.sportsmedia.repository.FirebaseCRUD;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CambiarDatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CambiarDatosFragment extends Fragment {
    private View vista;
    private FirebaseController firebase;
    private FirebaseCRUD firebaseCRUD;
    private TextView nombre;
    private TextView apellido;
    private TextView fecha_nacimiento;
    private TextView email;
    private Button btn_modificar_datos;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CambiarDatosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CambiarDatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CambiarDatosFragment newInstance(String param1, String param2) {
        CambiarDatosFragment fragment = new CambiarDatosFragment();
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
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_cambiar_datos, container, false);
        binding();
        listener();
        cargarDatosUsuario();
        return vista;
    }

    private void listener() {
        btn_modificar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<>();
                map.put("apellido", apellido.getText().toString());
                map.put("nombre", nombre.getText().toString());
                map.put("email", email.getText().toString());
                map.put("fechanac",fecha_nacimiento.getText().toString());
                firebase.getReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // making progress bar visibility as gone.

                        // adding a map to our database.
                        firebase.getReference().child(HomeActivity.getUserglobal().getUid()).updateChildren(map);
                        Intent intent=new Intent(getActivity(),HomeActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void cargarDatosUsuario() {
        Usuario usuarioActual= HomeActivity.getUserglobal();

        if(usuarioActual!=null){
            nombre.setText(usuarioActual.getNombre());
            apellido.setText(usuarioActual.getApellido());
            fecha_nacimiento.setText(usuarioActual.getFechanac());
            email.setText(usuarioActual.getEmail());
        }
    }
    private void binding() {
        nombre=vista.findViewById(R.id.edt_name_modificar);
        apellido=vista.findViewById(R.id.edt_apellido_modificar);
        fecha_nacimiento=vista.findViewById(R.id.edt_fecha_nacimiento_modificar);
        email=vista.findViewById(R.id.email_modificar);
        btn_modificar_datos=vista.findViewById(R.id.btn_modificar_datos1);

        firebase=new FirebaseController("Usuarios",getContext());
        firebaseCRUD=new FirebaseCRUD(firebase);
    }
}