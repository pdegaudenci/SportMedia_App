package com.example.sportsmedia.dto;

import java.io.Serializable;

public class Actividad implements Serializable {
    private String uid;
    private String titulo;
    private String descripcion;
    private String comunidad;
    private boolean equipoEspecial;
    private String horaInicio;
    private String horaFin;

    private String duracion;
    private String fecha;
    private String usuario;
    private double latitud;
    private double longitud;
    private String nombreLugar;
    private String direccion;

    private boolean activada=true;

    private Integer cantPersonas=0;

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public void setNombreLugar(String nombreLugar) {
        this.nombreLugar = nombreLugar;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    public Actividad(){

    }
    public Actividad(String titulo, String descripcion, String fecha, String comunidad, boolean equipoEspecial, String horaInicio, String horaFin, String usuario,String duracion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha=fecha;
        this.comunidad = comunidad;
        this.equipoEspecial = equipoEspecial;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.usuario=usuario;
        this.duracion=duracion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getComunidad() {
        return comunidad;
    }

    public void setComunidad(String comunidad) {
        this.comunidad = comunidad;
    }

    public boolean isEquipoEspecial() {
        return equipoEspecial;
    }

    public void setEquipoEspecial(boolean equipoEspecial) {
        this.equipoEspecial = equipoEspecial;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public boolean isActivada() {
        return activada;
    }

    public void setActivada(boolean activada) {
        this.activada = activada;
    }

    public Integer getCantPersonas() {
        return cantPersonas;
    }

    public void setCantPersonas(Integer cantPersonas) {
        this.cantPersonas = cantPersonas;
    }

    @Override
    public String toString() {
        return "Actividad{" +
                "uid='" + uid + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", comunidad='" + comunidad + '\'' +
                ", equipoEspecial=" + equipoEspecial +
                ", horaInicio='" + horaInicio + '\'' +
                ", horaFin='" + horaFin + '\'' +
                ", duracion='" + duracion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", usuario='" + usuario + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", nombreLugar='" + nombreLugar + '\'' +
                ", direccion='" + direccion + '\'' +
                ", activada=" + activada +
                ", cantPersonas=" + cantPersonas +
                '}';
    }
}
