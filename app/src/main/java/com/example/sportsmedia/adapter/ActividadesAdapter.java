package com.example.sportsmedia.adapter;





import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sportsmedia.HomeActivity;
import com.example.sportsmedia.R;
import com.example.sportsmedia.dto.Actividad;
import com.example.sportsmedia.fragments.ActivityDetailFragment;

import java.util.ArrayList;
import java.util.Random;

public class ActividadesAdapter extends RecyclerView.Adapter<ActividadesAdapter.ViewHolder>{

    private ArrayList<Actividad> mDataSet;
    private Context context;

    public ActividadesAdapter(ArrayList<Actividad> myDataSet, Context cont) {
        mDataSet = myDataSet;
        System.out.println(myDataSet);
        context=cont;
        Log.d("Creacion Adapter", "Carga de los datos a utilizar por el adaptador realizada correctamente");
    }
    // ViewHolder que recibe vista que contendrá (posicion que tiene la vista cargada)
    // Es una clase interna ViewHolder, que permite obtener referencias de los componentes visuales (views)
    // de cada elemento de la lista,
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Accedo al contexto para poder lanzar actividades
        private Context context;

        public TextView txtHeader;
        public TextView lugar;
        public TextView comunidad;
        public TextView fecha;
        public TextView hora;
        public ImageView icono;

        private Actividad actividad;

        private TextView cantInscriptos;
        private Button btnDetails;


        // Cada View v es cada una de las vistas que queremos visualizar
        public ViewHolder(View v) {

            super(v);

            // Obtengo elementos de cada RecyclerView
            binding(v);

        }

        private void binding(View v) {
            context = v.getContext();
            txtHeader = (TextView) v.findViewById(R.id.header_actividad);
            lugar = (TextView) v.findViewById(R.id.lugar_actividad);
            comunidad = (TextView) v.findViewById(R.id.comunidad_actividad);
            icono = (ImageView) v.findViewById(R.id.icono_actividad);
            fecha = (TextView) v.findViewById(R.id.fecha_actividad);
            hora = (TextView) v.findViewById(R.id.hora_actividad);
            cantInscriptos = (TextView) v.findViewById(R.id.inscriptos);
            btnDetails = (Button) v.findViewById(R.id.btn_detalle);
            Log.i("Creacion ViewHolder", "Se cargaron los elementos de la vista reutilizable de cada elemento correctamente");
        }

        // Defino listeners para nuestros elementos de cada vista
        void setOnclikListener() {
            btnDetails.setOnClickListener(this);
            Log.i("Carga de listener", "Carga de manejador de evento click de boton de cada vista realizada correctamente");
        }

        @Override
        public void onClick(View view) {

            // Creo objeto bundle que almacenará informacion de la actividad y se transmitirá en la trasaccion entre fragments
            Bundle bundle= new Bundle();
            bundle.putSerializable("actividad",actividad);

            Fragment fragmento = new ActivityDetailFragment();

            // Al fragment le proporciono argumentos
            fragmento.setArguments(bundle);
            // add apila los fragmentos y eso hace que, cuando presione el boton atras se regresa al anterior fragment
            HomeActivity.manager.beginTransaction().add(R.id.fragment,fragmento).addToBackStack(null).commit();




        }

    }


    /**
     * Metodo invocado por el  layout manager para renderizar cada elemento del RecyclerView
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creamos una nueva vista , donde cada elemento de la lista se corresponda con una Cardview
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actividades_list, parent, false);
        // Aquí podemos definir tamaños, márgenes, paddings, etc
        ViewHolder vh = new ViewHolder(v);
        Log.d("Creacion ViewHolder", "Carga correcta de layout manager");
        return vh;
    }


    // Enlaza datos de la fuente de datos con viewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
// - obtenemos un elemento del dataset según su posición
        // - reemplazamos el contenido usando tales datos

       holder.actividad =mDataSet.get(position);
       System.out.println(mDataSet);
       holder.txtHeader.setText( mDataSet.get(position).getTitulo());
        Glide.with(context)
                .load("https://source.unsplash.com/category/nature/")
                .into(holder.icono);
        holder.fecha.setText("Fecha: "+mDataSet.get(position).getFecha());
        holder.hora.setText("Hora inicio: "+mDataSet.get(position).getHoraInicio()+ " Hora fin:"+mDataSet.get(position).getHoraFin());
        holder.lugar.setText("Lugar: "+mDataSet.get(position).getComunidad());
        holder.cantInscriptos.setText("Cantidad inscriptos:"+String.valueOf(mDataSet.get(position).getCantPersonas()));
        //holder.icono.setImageResource(mDataSet.get(position).getIcono());
        holder.setOnclikListener();
        Log.d("Creacion ViewHolder", "Carga exitosa de los elementos de cada vista correspondiente a los elementos activos del dataset");
    }


    /**
     *
     * @return Devuelve entero indicando la cantidad de elementos a mostrar en el RecyclerView.
     */
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
