package com.example.sportsmedia.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sportsmedia.R;
import com.example.sportsmedia.dto.Actividad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityDetailFragment extends Fragment {

    private TextView txt_titulo,txt_descripcion,txt_comunidad,txt_equipo,txt_fecha,txt_horaio,txt_lugar,txt_direccion,txt_inscripto;
    private View vista;

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

        // Gestiono evento del boton atrás del dispositivo
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                int count = getFragmentManager().getBackStackEntryCount();

                if (count == 0) {

                    getFragmentManager().popBackStack();
                } else {
                    getFragmentManager().popBackStack();//No se porqué puse lo mismo O.o
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_activity_detail, container, false);
        binding(vista);
        obtenerDetallesActividad(savedInstanceState);
        return vista;
    }

    private void obtenerDetallesActividad(Bundle savedInstanceState) {
        Bundle datos= getArguments();
        Actividad actividad =(Actividad)datos.getSerializable("actividad");
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
    }


}