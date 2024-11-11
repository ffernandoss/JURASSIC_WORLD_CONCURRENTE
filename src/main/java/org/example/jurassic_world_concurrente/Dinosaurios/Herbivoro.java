package org.example.jurassic_world_concurrente.Dinosaurios;

public class Herbivoro implements Dinosaurio {
    private int edad;
    private String nombre;
    private boolean estaEnfermo;
    private final int maxEdad = 40;

    @Override
    public void mostrarTipo() {
        System.out.println("Soy un dinosaurio herbívoro.");
    }

    @Override
    public void cambiarEstado(boolean estaEnfermo) {
        this.estaEnfermo = estaEnfermo;
    }

    @Override
    public void envejecer() {
        if (edad < maxEdad) {
            edad++;
        } else {
            morir();
        }
    }

    @Override
    public void morir() {
        System.out.println(nombre + " ha muerto.");
    }

    @Override
    public int getEdad() {
        return edad;
    }

    @Override
    public void setEdad(int edad) {
        this.edad = edad;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean isEstaEnfermo() {
        return estaEnfermo;
    }

    @Override
    public void setEstaEnfermo(boolean estaEnfermo) {
        this.estaEnfermo = estaEnfermo;
    }

    @Override
    public String getTipo() {
        return "Herbivoro";
    }

    @Override
    public void setTipo(String tipo) {
        // No se necesita implementar
    }

    @Override
    public int getMaxEdad() {
        return maxEdad;
    }
}