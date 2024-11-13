package org.example.jurassic_world_concurrente.Visitantes;

public class Visitante {
    private Long id;
    private String nombre;
    private String ubicacion;
    private int tiempoEnParque; // Nuevo campo

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getTiempoEnParque() {
        return tiempoEnParque;
    }

    public void setTiempoEnParque(int tiempoEnParque) {
        this.tiempoEnParque = tiempoEnParque;
    }
}