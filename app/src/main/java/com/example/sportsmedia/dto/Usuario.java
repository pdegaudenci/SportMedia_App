package com.example.sportsmedia.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {
    /**
     * Atriutos de usuarios
     */
    private String username,nombre,apellido,password,fechanac,email;
    private String uid;
    // Atributo que contiene a las actividades inscriptas del usuario
    private ArrayList<String> idActividades=new ArrayList<>();

    /**
     * CONSTRUCTORES
     */
    public Usuario() {

    }

    /**
     * GETTERS AND SETTERS
     */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFechanac() {
        return fechanac;
    }

    public void setFechanac(String fechanac) {
        this.fechanac = fechanac;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public ArrayList<String> getIdActividades() {
        return idActividades;
    }

    public void setIdActividades(ArrayList<String> idActividades) {
        this.idActividades = idActividades;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                '}';
    }
}
