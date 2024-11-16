package org.example.jurassic_world_concurrente.Dinosaurios;

public class DinosaurioEstado {
    private Dinosaurio dinosaurio;
    private boolean estaEnfermo;
    private int temperatura;

    public DinosaurioEstado(Dinosaurio dinosaurio) {
        this.dinosaurio = dinosaurio;
        this.temperatura = (int) (Math.random() * 41);
        if (dinosaurio.getMaxEdad() - dinosaurio.getEdad() > 3) {
            this.estaEnfermo = this.temperatura >= 38; // Set to true if temperature is 38 or more
        } else {
            this.estaEnfermo = false; // Cannot be sick if 3 or less days to max age
        }
    }

    public void actualizarEstado() {
        this.temperatura = (int) (Math.random() * 41);
        if (dinosaurio.getMaxEdad() - dinosaurio.getEdad() > 3) {
            this.estaEnfermo = this.temperatura >= 38; // Set to true if temperature is 38 or more
        } else {
            this.estaEnfermo = false; // Cannot be sick if 3 or less days to max age
        }
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
        if (dinosaurio.getMaxEdad() - dinosaurio.getEdad() > 3) {
            this.estaEnfermo = estaEnfermo;
        } else {
            this.estaEnfermo = false; // Cannot be sick if 3 or less days to max age
        }
    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }
}