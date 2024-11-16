package org.example.jurassic_world_concurrente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/isla_voladores")
    public String islaVoladores() {
        return "isla_voladores.html";
    }

    @GetMapping("/isla_carnivoros")
    public String islaCarnivoros() {
        return "isla_carnivoros.html";
    }

    @GetMapping("/isla_herbivoros")
    public String islaHerbivoros() {
        return "isla_herbivoros.html";
    }

    @GetMapping("/enfermeria")
    public String enfermeria() {
        return "enfermeria.html";
    }

    @GetMapping("/prueba")
    public String prueba() {
        return "prueba.html";
    }
}