package org.example.jurassic_world_concurrente.Huevos;

import org.example.jurassic_world_concurrente.Dinosaurios.*;

public class Huevo {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getPeriodoIncubacion() {
        return periodoIncubacion;
    }

    public void setPeriodoIncubacion(int periodoIncubacion) {
        this.periodoIncubacion = periodoIncubacion;
    }

    public int getTiempoIncubacion() {
        return tiempoIncubacion;
    }

    public void setTiempoIncubacion(int tiempoIncubacion) {
        this.tiempoIncubacion = tiempoIncubacion;
    }

    private Long id;
    private String tipo;
    private String estado;
    private int periodoIncubacion;
    private int tiempoIncubacion;

    public Huevo(String tipo, int periodoIncubacion) {
        this.tipo = tipo;
        this.periodoIncubacion = periodoIncubacion;
        this.estado = "Incubando";
        this.tiempoIncubacion = 0;
    }

    // Getters and Setters

    public void incubar() {
        if (tiempoIncubacion < periodoIncubacion) {
            tiempoIncubacion++;
        } else {
            estado = "Eclosionado";
        }
    }

    public Dinosaurio transformarADinosaurio() {
        Dinosaurio dinosaurio;
        switch (tipo.toLowerCase()) {
            case "carnivoro":
                dinosaurio = new Carnivoro();
                dinosaurio.setNombre("Carnivoro_" + id);
                break;
            case "herbivoro":
                dinosaurio = new Herbivoro();
                dinosaurio.setNombre("Herbivoro_" + id);
                break;
            case "volador":
                dinosaurio = new Volador();
                dinosaurio.setNombre("Volador_" + id);
                break;
            default:
                throw new IllegalArgumentException("Tipo de dinosaurio no soportado.");
        }
        dinosaurio.setEdad(0);
        return dinosaurio;
    }
}