// src/main/java/org/example/jurassic_world_concurrente/Dinosaurios/Carnivoro.java
package org.example.jurassic_world_concurrente.Dinosaurios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Carnivoro implements Dinosaurio {
    private static final Logger logger = LoggerFactory.getLogger(Carnivoro.class);
    private static long idCounter = 0;
    private Long id;
    private int edad;
    private String nombre;
    private boolean estaEnfermo;
    private final int maxEdad = 25;
    private int ticsEnEnfermeria;

    public Carnivoro() {
        this.id = generateId();
        this.nombre = "Carnivoro_" + id;
        logger.info("Dinosaurio creado: {}, tipo: {}, edad: {}", nombre, getTipo(), edad);
    }

    private synchronized long generateId() {
        return idCounter++;
    }

    @Override
    public void mostrarTipo() {
        logger.info("Soy un dinosaurio carnívoro.");
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
        return "Carnivoro";
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

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public int getTicsEnEnfermeria() {
        return ticsEnEnfermeria;
    }

    @Override
    public void incrementarTicsEnEnfermeria() {
        this.ticsEnEnfermeria++;
    }

    @Override
    public void resetTicsEnEnfermeria() {
        this.ticsEnEnfermeria = 0;
    }

    @Override
    public void setTicsEnEnfermeria(int ticsEnEnfermeria) {
        this.ticsEnEnfermeria = ticsEnEnfermeria;
    }
}