package org.example.jurassic_world_concurrente.FlujoMaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ControladorDeFlujos {

    private static final int TICS_POR_FLUJO = 1; // 1 tic por flujo
    private Disposable masterFlowDisposable;
    private Disposable otherFlowDisposable;
    private final AtomicBoolean masterFlowRunning = new AtomicBoolean(false);
    private final AtomicBoolean otherFlowRunning = new AtomicBoolean(false);

    @Autowired
    private MasterScheduler masterScheduler;

    @Autowired
    private EnfermeriaScheduler otroScheduler; // Tu otro flujo

    // Inicia el flujo master y luego el flujo alterno (de enfermería)
    public void iniciarFlujos() {
        masterFlowDisposable = Flux.interval(Duration.ofSeconds(1)) // Intervalo de un segundo por tic
                .doOnNext(tic -> {
                    if (!otherFlowRunning.get() && !masterFlowRunning.get()) {
                        masterFlowRunning.set(true);
                        // Ejecutar un tic del flujo master
                        masterScheduler.iniciarSimulacion();
                        pausarFlujo(masterFlowDisposable); // Pausar flujo master
                        masterFlowRunning.set(false);

                        // Ahora iniciar el flujo alterno (de enfermería) con el mismo tic
                        otherFlowDisposable = Flux.interval(Duration.ofSeconds(1))
                                .doOnNext(t -> {
                                    if (!masterFlowRunning.get() && !otherFlowRunning.get()) {
                                        otherFlowRunning.set(true);
                                        // Ejecutar un tic del flujo alterno
                                        otroScheduler.iniciarEnfermeria();
                                        pausarFlujo(otherFlowDisposable); // Pausar después del tic
                                        otherFlowRunning.set(false);
                                    }
                                })
                                .subscribeOn(Schedulers.parallel())
                                .subscribe();
                    }
                })
                .subscribeOn(Schedulers.parallel())
                .subscribe();
    }

    // Método para pausar el flujo
    private void pausarFlujo(Disposable flujo) {
        if (flujo != null && !flujo.isDisposed()) {
            flujo.dispose(); // Detener el flujo actual
        }
    }

    // Detener todos los flujos
    public void detenerFlujos() {
        if (masterFlowDisposable != null && !masterFlowDisposable.isDisposed()) {
            masterFlowDisposable.dispose();
        }
        if (otherFlowDisposable != null && !otherFlowDisposable.isDisposed()) {
            otherFlowDisposable.dispose();
        }
    }
}
