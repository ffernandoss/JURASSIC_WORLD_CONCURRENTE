// src/main/java/org/example/jurassic_world_concurrente/Visitantes/Visitante.java
package org.example.jurassic_world_concurrente.Visitantes;

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

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Visitante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}