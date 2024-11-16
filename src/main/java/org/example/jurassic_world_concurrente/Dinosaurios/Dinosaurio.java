package org.example.jurassic_world_concurrente.Dinosaurios;

public interface Dinosaurio {
    void mostrarTipo();
    void cambiarEstado(boolean estaEnfermo);
    void envejecer();
    void morir();
    int getEdad();
    void setEdad(int edad);
    String getNombre();
    void setNombre(String nombre);
    boolean isEstaEnfermo();
    void setEstaEnfermo(boolean estaEnfermo);
    String getTipo();
    void setTipo(String tipo);
    int getMaxEdad();
    Long getId(); // Add this method

    /// //
    int getTicsEnEnfermeria();
    void incrementarTicsEnEnfermeria();
    void resetTicsEnEnfermeria();
    void setTicsEnEnfermeria(int ticsEnEnfermeria);

}