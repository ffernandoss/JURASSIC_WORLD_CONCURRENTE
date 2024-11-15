// src/main/java/org/example/jurassic_world_concurrente/Visitante.java
package org.example.jurassic_world_concurrente.visitante;

public class Visitante {
    private static long idCounter = 0;
    private Long id;
    private String nombre;

    public Visitante(String nombre) {
        this.id = generateId();
        this.nombre = nombre;
    }

    private synchronized long generateId() {
        return idCounter++;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "Visitante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}