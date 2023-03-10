package com.example.sportsmedia.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sportsmedia.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BienvenidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BienvenidoFragment extends Fragment {

    private View vista;
    private Button btn_ir_actividades;
    private Button btn_sociales;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BienvenidoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BienvenidoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BienvenidoFragment newInstance(String param1, String param2) {
        BienvenidoFragment fragment = new BienvenidoFragment();
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
        vista= inflater.inflate(R.layout.fragment_bienvenido, container, false);
        binding();
        listener();
        return vista;
    }

    private void listener() {
        btn_ir_actividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                Fragment fragment = new ActividadesFragment();
                bundle.putString("tipo","misActividades");
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment , fragment).addToBackStack(null)
                        .commit();
            }
        });
        btn_sociales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new RedFragment();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment , fragment).addToBackStack(null)
                        .commit();
            }
        });
    }

    private void binding() {
        btn_sociales=vista.findViewById(R.id.btn_ir_sociales);
        btn_ir_actividades=vista.findViewById(R.id.btn_ir_Misactividades);
    }
}