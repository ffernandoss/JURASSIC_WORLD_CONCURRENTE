package org.example.jurassic_world_concurrente.Dinosaurios;

public class DinosaurioEstado {
    private Dinosaurio dinosaurio;
    private boolean estaEnfermo;
    private int temperatura;

    public DinosaurioEstado(Dinosaurio dinosaurio) {
        this.dinosaurio = dinosaurio;
        this.temperatura = (int) (Math.random() * 41);
        this.estaEnfermo = this.temperatura >= 30; // Set to true if temperature is 30 or more
    }

    public void actualizarEstado() {
        this.temperatura = (int) (Math.random() * 41);
        this.estaEnfermo = this.temperatura >= 30; // Set to true if temperature is 30 or more
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