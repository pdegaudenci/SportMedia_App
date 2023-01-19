package com.example.sportsmedia;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.time.LocalTime;



public class TimePickerFragment extends DialogFragment {
    /*
    OnTimeSetListener habilita al TimePickerDialog para notificar cambios en el tiempo.
     */
    private TimePickerDialog.OnTimeSetListener timeObserver  = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

        }
    };
    private int hour;
    private int minute;



    public TimePickerFragment() {
        LocalTime now = LocalTime.now();
        hour = now.getHour();
        minute = now.getMinute();
    }

    public TimePickerFragment(TimePickerDialog.OnTimeSetListener timeObserver) {
        LocalTime now = LocalTime.now();
        hour = now.getHour();
        minute = now.getMinute();
        this.timeObserver = timeObserver;
    }
@Override
    public Dialog onCreateDialog(Bundle savedInstanceState  )  {
        /*
        TimePickerDialog:
        1º parametro -> context: Contexto en el que vivirá el diálogo
        2º parametro -> themeResId: El ID del recurso de estilo que deseas aplicar al diálogo. Más adelante lo usaremos.
        3º parametro -> listener: Observador OnTimeSetListener que notificará el momento en que se seleccione una fecha.
        4º parametrohourOfDay: Representa la hora con que se inicializará el TimePicker
        5º parametro->minute: Representa los minutos iniciales del TimePicker
        6º parametro -> is24HourView: Si es modo 24 horas o AM/PM
         */
        return new TimePickerDialog(requireActivity(), timeObserver, hour, minute, false);
    }

}