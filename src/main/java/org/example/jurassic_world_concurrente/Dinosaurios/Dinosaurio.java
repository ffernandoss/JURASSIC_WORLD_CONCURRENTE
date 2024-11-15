package org.example.jurassic_world_concurrente.Dinosaurios;

public interface Dinosaurio {
    // Métodos para obtener y establecer el nombre
    String getNombre();
    void setNombre(String nombre);

    // Métodos para obtener y establecer el tipo
    String getTipo();
    void setTipo(String tipo);

    // Métodos relacionados con la edad
    int getEdad();
    void setEdad(int edad);
    int getMaxEdad();

    // Métodos relacionados con el estado de salud
    boolean isEstaEnfermo();
    void setEstaEnfermo(boolean estaEnfermo);

    // Métodos para manejar los tics en la enfermería
    int getTicsEnEnfermeria();
    void incrementarTicsEnEnfermeria();
    void resetTicsEnEnfermeria();
    void setTicsEnEnfermeria(int ticsEnEnfermeria);

    // Métodos adicionales para funcionalidad específica
    void mostrarTipo();
    void cambiarEstado(boolean estaEnfermo);
    void envejecer();
    void morir();

    // Método para obtener un identificador único
    Long getId();
}
