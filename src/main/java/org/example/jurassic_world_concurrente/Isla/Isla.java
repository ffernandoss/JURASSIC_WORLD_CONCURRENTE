package org.example.jurassic_world_concurrente.Isla;

import org.example.jurassic_world_concurrente.visitante.Visitante;

import java.util.ArrayList;
import java.util.List;

public class Isla {
    private List<Visitante> islaCarnivoros;
    private List<Visitante> islaHerbivoros;
    private List<Visitante> islaVoladores;

    public Isla() {
        this.islaCarnivoros = new ArrayList<>();
        this.islaHerbivoros = new ArrayList<>();
        this.islaVoladores = new ArrayList<>();
    }

    public List<Visitante> getIslaCarnivoros() {
        return islaCarnivoros;
    }

    public void setIslaCarnivoros(List<Visitante> islaCarnivoros) {
        this.islaCarnivoros = islaCarnivoros;
    }

    public List<Visitante> getIslaHerbivoros() {
        return islaHerbivoros;
    }

    public void setIslaHerbivoros(List<Visitante> islaHerbivoros) {
        this.islaHerbivoros = islaHerbivoros;
    }

    public List<Visitante> getIslaVoladores() {
        return islaVoladores;
    }

    public void setIslaVoladores(List<Visitante> islaVoladores) {
        this.islaVoladores = islaVoladores;
    }
}