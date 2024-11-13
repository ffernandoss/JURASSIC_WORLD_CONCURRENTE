package org.example.jurassic_world_concurrente.Dinosaurios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Herbivoro implements Dinosaurio {
    private static final Logger logger = LoggerFactory.getLogger(Herbivoro.class);
    private static long idCounter = 0;
    private Long id;
    private int edad;
    private String nombre;
    private boolean estaEnfermo;
    private final int maxEdad = 10;

    public Herbivoro() {
        this.id = generateId();
        this.nombre = "Herbivoro_" + id;
        logger.info("Dinosaurio creado: {}, tipo: {}, edad: {}", nombre, getTipo(), edad);
    }

    private synchronized long generateId() {
        return idCounter++;
    }

    @Override
    public void mostrarTipo() {
        logger.info("Soy un dinosaurio herbívoro.");
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
        logger.info("{} ha muerto.", nombre);
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

    @Override
    public Long getId() {
        return id;
    }
}