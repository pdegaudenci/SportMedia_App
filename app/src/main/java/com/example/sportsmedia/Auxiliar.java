package com.example.sportsmedia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Auxiliar {
    public static Date obtenerFechaActual(String format) {
        return new Date(System.currentTimeMillis());

    }

    public static boolean validarRangoFechas(String fechaInicio, String fechaFin) {
        boolean fechaValida = true;
        String aux = null;
        try {
            // formatear fechas
            SimpleDateFormat parseador = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaActual = Auxiliar.obtenerFechaActual("dd/mm/yyyy");
            Date fechaTarget = parseador.parse(fechaInicio);

            int mlsDia = 86400000;
            int dias = (int) ((fechaTarget.getTime() - fechaActual.getTime()) / mlsDia);
            if (dias < mlsDia)
                fechaValida = false;

        } catch (ParseException ex) {
            Logger.getLogger("logger").info("ERROR en la conversion de fecha (String to Date)");
        }
        return fechaValida;
    }

    public static boolean validarHorario(String horaInicio, String horaFin) {
        boolean horarioValida = true;
        // Eliminar caracteres que no sea numericos
        horaInicio=horaInicio.replaceAll("[a-zA-Z]", "").trim();
        horaFin=horaFin.replaceAll("[a-zA-Z]", "").trim();

        if (horaInicio != null && horaFin != null && horaInicio.contains(":") && horaFin.contains(":")) {
            String[] inicio = horaInicio.split(":");
            String[] fin = horaFin.split(":");
            // Eliminar caracteres que no sea numericos
            inicio[0]=inicio[0].replaceAll("[a-zA-Z]", "").trim();
            inicio[1]=inicio[1].replaceAll("[a-zA-Z]", "").trim();
            fin[0]=fin[0].replaceAll("[a-zA-Z]", "").trim();
           fin[1]=fin[1].replaceAll("[a-zA-Z]", "").trim();
            if (Integer.parseInt(inicio[0]) > Integer.parseInt(fin[0]))
                horarioValida = false;
            else if (Integer.parseInt(inicio[0]) == Integer.parseInt(fin[0])) {
                if (Integer.parseInt(inicio[1]) > Integer.parseInt(fin[1]))
                    horarioValida = false;
            }
        }
        return horarioValida;

    }

    public static Long calcularDiferenciaMinutos(String horaInicio, String horaFin) {
        Long diferencia=null;

        if (horaInicio != null && horaFin != null && horaInicio.contains(":") && horaFin.contains(":")) {
            try {
                horaFin=horaFin.trim();
                horaInicio=horaInicio.trim();
                SimpleDateFormat parseador = new SimpleDateFormat("HH:mm");
                Date horarioInicio = parseador.parse(horaInicio);
                Date horarioFin = parseador.parse(horaFin);

                int mlsMinuto = 60000;
                diferencia =(long) ((horarioFin.getTime() - horarioInicio.getTime())/mlsMinuto);

            } catch (ParseException e) {
                Logger.getLogger("logger").info("ERROR en calculo de minutos en diferencia horaria "+Auxiliar.class);
            }
        }
        return diferencia;
    }
}
