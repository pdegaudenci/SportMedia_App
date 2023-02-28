package com.example.sportsmedia.repository;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sportsmedia.HomeActivity;
import com.example.sportsmedia.adapter.ActividadesAdapter;
import com.example.sportsmedia.controller.FirebaseController;
import com.example.sportsmedia.dto.Actividad;
import com.example.sportsmedia.dto.Usuario;
import com.example.sportsmedia.fragments.ActividadesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseCRUD {
    private FirebaseController firebase;
    private static ArrayList<Actividad> misActividades=new ArrayList<>();;
    private static ArrayList<Actividad> actividades=new ArrayList<>();
    ArrayList<Actividad> mDataset=null;
    private ActividadesAdapter mAdapter;

    public FirebaseCRUD(FirebaseController firebase){
        this.firebase=firebase;

    }

    /**
     * Metodo que crea un nuevo documento de una coleccion determinado en funcion del tipo de objeto pasado por parametro.
     * @param uuii String que corresponde al identificador unica del objeto que será mapeado como documento en la coleccion
     * @param documento Object que será casteado a un objeto de la clase Actividad o la clase Usuario
     * @return boolean true si se realizó la tarea de insercción correctamente
     */
    public boolean create(String uuii, Object documento){
        boolean creado= false;
        if(documento instanceof Actividad){
            Actividad actividad = (Actividad) documento;
            creado=firebase.getReference().child(uuii).setValue(actividad).isSuccessful();
        }
        if(documento instanceof Usuario){
            Usuario usuario = (Usuario) documento;
            creado=firebase.getReference().child(uuii).setValue(usuario).isSuccessful();
        }
        return creado;
    }
    /**
     * Metodo que borra documento de una coleccion determinado en funcion del tipo de objeto pasado por parametro.
     * @param uuii String que corresponde al identificador unica del objeto que será mapeado como documento en la coleccion
     * @param documento Object que será casteado a un objeto de la clase Actividad o la clase Usuario
     * @return boolean true si se realizó la tarea de borrado correctamente
     */
    public boolean delete(String uuii, Object documento){
        boolean borrado= false;
        if(documento instanceof Actividad){
            Actividad actividad = (Actividad) documento;
            // Referencia a nodo que de la BBDD que contiene actividad
            DatabaseReference db=firebase.getReference().child("Actividades").child(actividad.getUid());
            borrado=db.removeValue().isSuccessful();
            System.out.println(borrado+"\n\n\n");

        }
        if(documento instanceof Usuario){
            Usuario usuario = (Usuario) documento;
            borrado=firebase.getReference().child(uuii).setValue(usuario).isSuccessful();
        }
        return borrado;
    }

    /**
     * Metodo que carga las actividades almacenadas en la coleccion Actividades de Firebase
     * , dentro de uno de los 2 ArrayList , discriminando si las actividad la creó
     * el usuario o fue creada por otros usuarios
     */
   public  void cargaDatosActividades(){
       firebase.getReference().addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               Actividad aux=snapshot.getValue(Actividad.class);
               // adding snapshot to our array list on below line.
               if(aux.getUsuario().equals(HomeActivity.getUserglobal().getUsername())){
                   misActividades.add(aux);
               }
               else{
                   actividades.add(aux);
               }

           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               // this method is called when new child is added
               // we are notifying our adapter and making progress bar
               // visibility as gone.

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {
               // notifying our adapter when child is removed.


           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               // notifying our adapter when child is moved.

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


    }

    public static ArrayList<Actividad> getMisActividades() {
        return misActividades;
    }

    public static void setMisActividades(ArrayList<Actividad> misActividades) {
        FirebaseCRUD.misActividades = misActividades;
    }

    public static ArrayList<Actividad> getActividades() {
        return actividades;
    }

    public static void setActividades(ArrayList<Actividad> actividades) {
        FirebaseCRUD.actividades = actividades;
    }
}
