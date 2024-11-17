package org.example.jurassic_world_concurrente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BrowserLauncher implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(BrowserLauncher.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String url = "http://localhost:8080/prueba.html"; // URL de la página index

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", url);
            processBuilder.start();
            logger.info("Navegador abierto con la URL: {}", url);
        } catch (IOException e) {
            logger.error("Error al abrir el navegador: ", e);
        }
    }
}