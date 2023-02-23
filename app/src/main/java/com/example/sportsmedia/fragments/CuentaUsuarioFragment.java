package com.example.sportsmedia.fragments;

import android.os.Bundle;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CuentaUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CuentaUsuarioFragment extends Fragment {

private TextView nombre;
private TextView apellido;
private TextView fecha_nacimiento;
private TextView email;
private Button btn_modificar_datos1;
private Button btn_editar_pass;
private View vista;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CuentaUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CuentaUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CuentaUsuarioFragment newInstance(String param1, String param2) {
        CuentaUsuarioFragment fragment = new CuentaUsuarioFragment();
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
        vista= inflater.inflate(R.layout.fragment_cuenta_usuario, container, false);
        binding();
        listener();
        cargarDatosUsuario();
        return vista;
    }

    private void listener() {

        btn_modificar_datos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager=getChildFragmentManager();
                manager.beginTransaction().replace(R.id.fragment,new CambiarDatosFragment()).addToBackStack(null)
                        .commit();
            }
        });
    }


    private void binding() {
        nombre=vista.findViewById(R.id.edt_name_vista);
        apellido=vista.findViewById(R.id.edt_apellido_vista);
        fecha_nacimiento=vista.findViewById(R.id.edt_fecha_nacimiento);
        email=vista.findViewById(R.id.email);
        btn_modificar_datos1=(Button)vista.findViewById(R.id.btn_modificar_datos);
       btn_editar_pass=vista.findViewById(R.id.btn_modificar_password);

    }

    private void cargarDatosUsuario() {
        Usuario usuarioActual= HomeActivity.getUserglobal();

        if(usuarioActual!=null){
            System.out.println("\n\nENTRAAAAAAAA\n\n");
            nombre.setText(usuarioActual.getNombre());
            apellido.setText(usuarioActual.getApellido());
            fecha_nacimiento.setText(usuarioActual.getFechanac());
            email.setText(usuarioActual.getEmail());
        }
    }

}