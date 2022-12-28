package com.example.sportsmedia.models;

public class Usuario {
    String username,nombre,apellido,password,fechanac,email;
    int uid;

    public Usuario() {

    }

    public Usuario(int id, String nombre, String apellido, String username, String password, String fechanac, String email){
        this.uid = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.password =password;
        this.fechanac=fechanac;
        this.email=email;
    }

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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                '}';
    }
}
