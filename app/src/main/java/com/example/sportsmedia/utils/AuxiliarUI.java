package com.example.sportsmedia.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AuxiliarUI {

    /**
     * Visualiza una ventana de dialogo de confirmacion AlertDialog con un mensaje pasada
     * por parametro y pide confirmacion a usuario
     * @param mensaje de la ventana de dialogo
     * @param  activity Activity en la que se ejecutará el cuadro de dialogo
     * @return boolean : true si usuario confirmo y false en caso contrario
     */
    public static boolean ventanaDialogo(String mensaje, Activity activity){
        boolean[] confirmacion = new boolean[1];
        AlertDialog dialogo = new AlertDialog.Builder(activity).setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Hicieron click en el botón positivo, así que la acción está confirmada
                        confirmacion[0] =true;                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Hicieron click en el botón negativo, no confirmaron
                        // Simplemente descartamos el diálogo
                        confirmacion[0] =false;
                        dialog.dismiss();                    }
                })
                .setTitle("Confirmar") // El título
                .setMessage(mensaje) // El mensaje
                .create();
        dialogo.show();
        return confirmacion[0];
    }

}
