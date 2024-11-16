package org.example.jurassic_world_concurrente;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class fluxController {

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final Map<String, Sinks.Many<String>> islaSinks = new HashMap<>();

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> handleSse() {
        return sink.asFlux().delayElements(Duration.ofSeconds(1));
    }

    @GetMapping(value = "/sse/{isla}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> handleSseForIsla(@PathVariable String isla) {
        return islaSinks.computeIfAbsent(isla, k -> Sinks.many().multicast().onBackpressureBuffer()).asFlux().delayElements(Duration.ofSeconds(1));
    }

    public void sendEvent(String data) {
        sink.tryEmitNext(data);
    }

    public void sendEventToIsla(String isla, String data) {
        islaSinks.computeIfAbsent(isla, k -> Sinks.many().multicast().onBackpressureBuffer()).tryEmitNext(data);
    }//

    public void sendEventToEnfermeria(String data) {
        sendEventToIsla("Enfermeria", data);
    }
}