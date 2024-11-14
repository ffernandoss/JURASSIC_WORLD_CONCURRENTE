// src/main/java/org/example/jurassic_world_concurrente/Dinosaurios/DinosaurioEstado.java
package org.example.jurassic_world_concurrente.Dinosaurios;

public class DinosaurioEstado {
    private Dinosaurio dinosaurio;
    private boolean estaEnfermo;
    private int temperatura;

    public DinosaurioEstado(Dinosaurio dinosaurio) {
        this.dinosaurio = dinosaurio;
        this.estaEnfermo = Math.random() < 0.5; // Randomly set to true or false
        this.temperatura = (int) (Math.random() * 41);
    }

    public void actualizarEstado() {
        this.estaEnfermo = Math.random() < 0.5; // Randomly set to true or false
        this.temperatura = (int) (Math.random() * 41);
    }

    @Override
    public String toString() {
        return String.format("%s {temperatura-> %d, enfermo-> %b}",
                dinosaurio.getNombre(), temperatura, estaEnfermo);
    }

    public Dinosaurio getDinosaurio() {
        return dinosaurio;
    }

    public void setDinosaurio(Dinosaurio dinosaurio) {
        this.dinosaurio = dinosaurio;
    }

    public boolean isEstaEnfermo() {
        return estaEnfermo;
    }

    public void setEstaEnfermo(boolean estaEnfermo) {
        this.estaEnfermo = estaEnfermo;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }
}