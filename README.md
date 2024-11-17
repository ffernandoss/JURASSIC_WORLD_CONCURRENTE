# Proyecto: Jurassic World Concurrente

## Enlace del repositorio 

[Repositorio](https://github.com/ffernandoss/JURASSIC_WORLD_CONCURRENTE.git) realizado por Fernando Santamaria, Jose Antonio Oyono, Jose Daniel Martin y Hugo Sanchez


Este proyecto es una simulación concurrente de un parque de dinosaurios utilizando Spring Boot, Project Reactor, RabbitMQ, y otras tecnologías. La aplicación gestiona dinosaurios, huevos, visitantes y sus interacciones en un entorno concurrente.

## Índice
1. [Clases y Métodos](#clases-y-métodos)
    - [DinosaurioEstadoListener](#dinosaurioestadolistener)
    - [DinosaurioService](#dinosaurioservice)
    - [DinosaurioEstadoService](#dinosaurioestadoservice)
    - [FabricaDinosaurios](#fabricadinosaurios)
    - [Herbivoro](#herbivoro)
    - [Volador](#volador)
    - [ControladorDeFlujos](#controladordeflujos)
    - [EnfermeriaScheduler](#enfermeriascheduler)
    - [MasterScheduler](#masterscheduler)
    - [FabricaHuevos](#fabricahuevos)
    - [Huevo](#huevo)
    - [HuevoService](#huevoservice)
    - [Isla](#isla)
    - [IslaFlux](#islaflux)
    - [DinosaurDeathListener](#dinosaurdeathlistener)
    - [EnfermeriaListener](#enfermerialistener)
    - [RabbitMQConfig](#rabbitmqconfig)
    - [VerificarDinosauriosListener](#verificardinosaurioslistener)
    - [VisitorNotificationListener](#visitornotificationlistener)
    - [BrowserLauncher](#browserlauncher)
    - [DistribuidorVisitantes](#distribuidorvisitantes)
    - [Visitante](#visitante)
    - [VisitanteGenerator](#visitantegenerator)
    - [JurassicWorldConcurrenteApplication](#jurassicworldconcurrenteapplication)
    - [fluxController](#fluxcontroller)
2. [Interfaz Web: `index.html`](#interfaz-web-indexhtml)
3. [Tecnologías Usadas](#tecnologías-usadas)
4. [Endpoints REST](#endpoints-rest)

---

## Clases y Métodos

### DinosaurioEstadoListener
Componente que escucha mensajes de RabbitMQ para actualizar el estado de los dinosaurios.
- **`@RabbitListener(queues = "actualizarDinosaurioEstadoQueue") public void handleActualizarDinosaurioEstado(String message)`**  
  Actualiza los estados de los dinosaurios y registra logs de los estados actualizados.

---

### DinosaurioService
Servicio para gestionar dinosaurios.
- **`public synchronized void envejecerDinosaurios()`**  
  Envejece los dinosaurios y elimina los que han alcanzado su edad máxima.

- **`public synchronized void matarDinosaurio(Dinosaurio dinosaurio)`**  
  Elimina un dinosaurio y envía un mensaje a la cola `dinosaurDeathQueue`.

- **`public synchronized void generarEventoMuerteAleatoria()`**  
  Genera un evento de muerte aleatoria para un dinosaurio.

- **`public synchronized void agregarDinosaurio(Dinosaurio dinosaurio)`**  
  Agrega un dinosaurio a la lista de dinosaurios.

- **`public synchronized void suscribirDinosaurio(Dinosaurio dinosaurio)`**  
  Suscribe un dinosaurio a la lista de dinosaurios.

- **`public synchronized void desuscribirDinosaurio(Dinosaurio dinosaurio)`**  
  Desuscribe un dinosaurio de la lista de dinosaurios.

- **`public synchronized List<Dinosaurio> getDinosaurios()`**  
  Devuelve la lista de dinosaurios.

- **`public synchronized boolean existeDinosaurioDeTipo(String tipo)`**  
  Verifica si existe un dinosaurio de un tipo específico.

- **`public synchronized void suscribirDinosaurioEnfermo(Dinosaurio dinosaurio, int ticActual)`**  
  Suscribe un dinosaurio enfermo a la lista de dinosaurios enfermos.

- **`public synchronized void desuscribirDinosaurioEnfermo(Dinosaurio dinosaurio)`**  
  Desuscribe un dinosaurio enfermo de la lista de dinosaurios enfermos.

- **`public void verificarSalidaEnfermeria(int ticsTotales)`**  
  Verifica si algún dinosaurio debe salir de la enfermería.

- **`public synchronized List<Dinosaurio> getDinosauriosEnfermos()`**  
  Devuelve la lista de dinosaurios enfermos.

---

### DinosaurioEstadoService
Servicio para actualizar y gestionar el estado de los dinosaurios.
- **`public void actualizarEstados(List<Dinosaurio> dinosaurios)`**  
  Actualiza los estados de los dinosaurios y gestiona su suscripción a la enfermería.

- **`public List<DinosaurioEstado> getEstados()`**  
  Devuelve la lista de estados de los dinosaurios.

- **`public List<DinosaurioEstado> getDinosauriosEnfermos()`**  
  Devuelve la lista de dinosaurios enfermos.

- **`public void imprimirDinosauriosEnfermos()`**  
  Imprime la lista de dinosaurios enfermos.

- **`private int calcularTiempoRecuperacion(Dinosaurio dinosaurio)`**  
  Calcula el tiempo de recuperación de un dinosaurio.

---

### FabricaDinosaurios
Componente para crear dinosaurios.
- **`public Dinosaurio crearDinosaurio(String tipo)`**  
  Crea un dinosaurio del tipo especificado.

---

### Herbivoro
Clase que representa un dinosaurio herbívoro.
- **`public Herbivoro()`**  
  Constructor que inicializa un dinosaurio herbívoro.

- **`@Override public void mostrarTipo()`**  
  Muestra el tipo de dinosaurio.

- **`@Override public void cambiarEstado(boolean estaEnfermo)`**  
  Cambia el estado de enfermedad del dinosaurio.

- **`@Override public void envejecer()`**  
  Envejece el dinosaurio.

- **`@Override public void morir()`**  
  Maneja la muerte del dinosaurio.

- **`@Override public int getEdad()`**  
  Devuelve la edad del dinosaurio.

- **`@Override public void setEdad(int edad)`**  
  Establece la edad del dinosaurio.

- **`@Override public String getNombre()`**  
  Devuelve el nombre del dinosaurio.

- **`@Override public void setNombre(String nombre)`**  
  Establece el nombre del dinosaurio.

- **`@Override public boolean isEstaEnfermo()`**  
  Devuelve si el dinosaurio está enfermo.

- **`@Override public void setEstaEnfermo(boolean estaEnfermo)`**  
  Establece si el dinosaurio está enfermo.

- **`@Override public String getTipo()`**  
  Devuelve el tipo de dinosaurio.

- **`@Override public void setTipo(String tipo)`**  
  No implementado.

- **`@Override public int getMaxEdad()`**  
  Devuelve la edad máxima del dinosaurio.

- **`@Override public Long getId()`**  
  Devuelve el ID del dinosaurio.

- **`@Override public String toString()`**  
  Devuelve una representación en cadena del dinosaurio.

- **`@Override public int getTicsEnEnfermeria()`**  
  Devuelve los tics en enfermería del dinosaurio.

- **`@Override public void incrementarTicsEnEnfermeria()`**  
  Incrementa los tics en enfermería del dinosaurio.

- **`@Override public void resetTicsEnEnfermeria()`**  
  Reinicia los tics en enfermería del dinosaurio.

- **`@Override public void setTicsEnEnfermeria(int ticsEnEnfermeria)`**  
  Establece los tics en enfermería del dinosaurio.

---

### Volador
Clase que representa un dinosaurio volador.
- **`public Volador()`**  
  Constructor que inicializa un dinosaurio volador.

- **`@Override public void mostrarTipo()`**  
  Muestra el tipo de dinosaurio.

- **`@Override public void cambiarEstado(boolean estaEnfermo)`**  
  Cambia el estado de enfermedad del dinosaurio.

- **`@Override public void envejecer()`**  
  Envejece el dinosaurio.

- **`@Override public void morir()`**  
  Maneja la muerte del dinosaurio.

- **`@Override public int getEdad()`**  
  Devuelve la edad del dinosaurio.

- **`@Override public void setEdad(int edad)`**  
  Establece la edad del dinosaurio.

- **`@Override public String getNombre()`**  
  Devuelve el nombre del dinosaurio.

- **`@Override public void setNombre(String nombre)`**  
  Establece el nombre del dinosaurio.

- **`@Override public boolean isEstaEnfermo()`**  
  Devuelve si el dinosaurio está enfermo.

- **`@Override public void setEstaEnfermo(boolean estaEnfermo)`**  
  Establece si el dinosaurio está enfermo.

- **`@Override public String getTipo()`**  
  Devuelve el tipo de dinosaurio.

- **`@Override public void setTipo(String tipo)`**  
  No implementado.

- **`@Override public int getMaxEdad()`**  
  Devuelve la edad máxima del dinosaurio.

- **`@Override public Long getId()`**  
  Devuelve el ID del dinosaurio.

- **`@Override public String toString()`**  
  Devuelve una representación en cadena del dinosaurio.

- **`@Override public int getTicsEnEnfermeria()`**  
  Devuelve los tics en enfermería del dinosaurio.

- **`@Override public void incrementarTicsEnEnfermeria()`**  
  Incrementa los tics en enfermería del dinosaurio.

- **`@Override public void resetTicsEnEnfermeria()`**  
  Reinicia los tics en enfermería del dinosaurio.

- **`@Override public void setTicsEnEnfermeria(int ticsEnEnfermeria)`**  
  Establece los tics en enfermería del dinosaurio.

---

### ControladorDeFlujos
Componente para controlar los flujos de simulación.
- **`public void iniciarFlujos()`**  
  Inicia los flujos de simulación.

- **`private void iniciarFlujoAlterno()`**  
  Inicia el flujo alterno de enfermería.

- **`private void pausarFlujo(Disposable flujo)`**  
  Pausa un flujo específico.

- **`public void detenerFlujos()`**  
  Detiene todos los flujos.

---

### EnfermeriaScheduler
Componente para gestionar la enfermería de dinosaurios.
- **`public void iniciarEnfermeria()`**  
  Inicia el flujo de enfermería.

- **`public void detenerEnfermeria()`**  
  Detiene el flujo de enfermería.

---

### MasterScheduler
Componente para gestionar la simulación principal.
- **`public void iniciarSimulacion()`**  
  Inicia la simulación principal.

- **`private void guardarInformacionEnNotas()`**  
  Guarda información de la simulación en un archivo de notas.

- **`private void imprimirEstadoActual()`**  
  Imprime el estado actual de la simulación.

---

### FabricaHuevos
Componente para crear huevos de dinosaurio.
- **`public Huevo crearHuevo(String tipo)`**  
  Crea un huevo del tipo especificado.

---

### Huevo
Clase que representa un huevo de dinosaurio.
- **`public Huevo(String tipo, int periodoIncubacion)`**  
  Constructor que inicializa un huevo de dinosaurio.

- **`public void incubar()`**  
  Incuba el huevo.

- **`public Dinosaurio transformarADinosaurio()`**  
  Transforma el huevo en un dinosaurio.

- **`@Override public String toString()`**  
  Devuelve una representación en cadena del huevo.

---

### HuevoService
Servicio para gestionar huevos de dinosaurio.
- **`public void incubarHuevos()`**  
  Incuba los huevos y transforma los eclosionados en dinosaurios.

- **`public Huevo crearHuevo(String tipo)`**  
  Crea un huevo del tipo especificado.

- **`public Huevo crearHuevoAleatorio()`**  
  Crea un huevo de tipo aleatorio.

- **`public List<Huevo> getHuevos()`**  
  Devuelve la lista de huevos.

- **`public boolean existeHuevoDeTipo(String tipo)`**  
  Verifica si existe un huevo de un tipo específico.

---

### Isla
Clase que representa una isla en el parque.
- **`public Isla()`**  
  Constructor que inicializa una isla.

- **`public List<Visitante> getIslaCarnivoros()`**  
  Devuelve la lista de visitantes en la isla de carnívoros.

- **`public void setIslaCarnivoros(List<Visitante> islaCarnivoros)`**  
  Establece la lista de visitantes en la isla de carnívoros.

- **`public List<Visitante> getIslaHerbivoros()`**  
  Devuelve la lista de visitantes en la isla de herbívoros.

- **`public void setIslaHerbivoros(List<Visitante> islaHerbivoros)`**  
  Establece la lista de visitantes en la isla de herbívoros.

- **`public List<Visitante> getIslaVoladores()`**  
  Devuelve la lista de visitantes en la isla de voladores.

- **`public void setIslaVoladores(List<Visitante> islaVoladores)`**  
  Establece la lista de visitantes en la isla de voladores.

---

### IslaFlux
Clase que gestiona el flujo de visitantes en una isla.
- **`public synchronized int getTotalDinosaurios()`**  
  Devuelve el total de dinosaurios en la isla.

- **`public Mono<Void> agregarVisitante(Visitante visitante)`**  
  Agrega un visitante a la isla.

- **`public synchronized int getTotalVisitantes()`**  
  Devuelve el total de visitantes en la isla.

- **`public synchronized Visitante removeVisitante()`**  
  Elimina un visitante de la isla.

- **`public Flux<Visitante> obtenerFlujoVisitantes()`**  
  Devuelve un flujo de visitantes en la isla.

---

### DinosaurDeathListener
Componente que escucha mensajes de RabbitMQ para manejar la muerte de dinosaurios.
- **`@RabbitListener(queues = "dinosaurDeathQueue") public void handleDinosaurDeath(String tipo)`**  
  Genera un nuevo huevo tras la muerte de un dinosaurio.

---

### EnfermeriaListener
Componente que escucha mensajes de RabbitMQ para manejar la enfermería de dinosaurios.
- **`@RabbitListener(queues = "enfermeriaQueue") public void handleEnfermeriaMessage(Dinosaurio dinosaurio)`**  
  Maneja mensajes de la cola de enfermería y actualiza el estado de los dinosaurios.

---

### RabbitMQConfig
Clase de configuración para RabbitMQ.
- **`@Bean public Queue dinosaurDeathQueue()`**  
  Define una cola llamada `dinosaurDeathQueue`.

- **`@Bean public Queue verificarDinosauriosQueue()`**  
  Define una cola llamada `verificarDinosauriosQueue`.

- **`@Bean public Queue actualizarDinosaurioEstadoQueue()`**  
  Define una cola llamada `actualizarDinosaurioEstadoQueue`.

- **`@Bean public Queue enfermeriaQueue()`**  
  Define una cola llamada `enfermeriaQueue`.

- **`@Bean public MessageConverter jsonMessageConverter()`**  
  Define un convertidor de mensajes JSON.

- **`@Bean public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory)`**  
  Configura el `RabbitTemplate` con el convertidor de mensajes JSON.

- **`@Bean public Queue visitorNotificationQueue()`**  
  Define una cola llamada `visitorNotificationQueue`.

---

### VerificarDinosauriosListener
Componente que escucha mensajes de RabbitMQ para verificar la existencia de dinosaurios.
- **`@RabbitListener(queues = "verificarDinosauriosQueue") public void handleVerificarDinosaurios(String message)`**  
  Verifica si existen dinosaurios de cada tipo y genera nuevos huevos si es necesario.

---

### VisitorNotificationListener
Componente que escucha mensajes de RabbitMQ para manejar notificaciones de visitantes.
- **`@RabbitListener(queues = "visitorNotificationQueue") public void handleVisitorNotification(String islaNombre)`**  
  Maneja notificaciones de visitantes y redistribuye visitantes entre islas si es necesario.

---

### BrowserLauncher
Componente que lanza el navegador al iniciar la aplicación.
- **`@Override public void onApplicationEvent(ContextRefreshedEvent event)`**  
  Abre el navegador con la URL de la aplicación.

---

### DistribuidorVisitantes
Servicio para distribuir visitantes entre las islas.
- **`public Mono<Void> moverAIsla(Visitante visitante)`**  
  Mueve un visitante a una isla aleatoria.

- **`public IslaFlux getIslaFlux(String islaNombre)`**  
  Devuelve el flujo de visitantes de una isla específica.

- **`public Flux<Visitante> obtenerFlujosIslas()`**  
  Devuelve un flujo combinado de visitantes de todas las islas.

---

### Visitante
Clase que representa un visitante del parque.
- **`public Visitante(String nombre)`**  
  Constructor que inicializa un visitante.

- **`public Long getId()`**  
  Devuelve el ID del visitante.

- **`public void setId(Long id)`**  
  Establece el ID del visitante.

- **`public String getNombre()`**  
  Devuelve el nombre del visitante.

- **`public void setNombre(String nombre)`**  
  Establece el nombre del visitante.

- **`@Override public String toString()`**  
  Devuelve una representación en cadena del visitante.

---

### VisitanteGenerator
Clase para generar visitantes de manera continua.
- **`public Flux<Visitante> generarVisitantesContinuos()`**  
  Genera visitantes de manera continua y los distribuye entre las islas.

- **`private boolean islasLlenas()`**  
  Verifica si todas las islas están llenas.

---

### JurassicWorldConcurrenteApplication
Clase principal que inicia la aplicación.
- **`public static void main(String[] args)`**  
  Método principal para ejecutar la aplicación Spring Boot.

- **`@Override public void run(String... args)`**  
  Método que se ejecuta al iniciar la aplicación y configura la simulación inicial.

---

### fluxController
Controlador para manejar eventos SSE.
- **`@GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE) public Flux<String> handleSse()`**  
  Maneja eventos SSE generales.

- **`@GetMapping(value = "/sse/{isla}", produces = MediaType.TEXT_EVENT_STREAM_VALUE) public Flux<String> handleSseForIsla(@PathVariable String isla)`**  
  Maneja eventos SSE específicos para una isla.

- **`public void sendEvent(String data)`**  
  Envía un evento SSE general.

- **`public void sendEventToIsla(String isla, String data)`**  
  Envía un evento SSE a una isla específica.

- **`public void sendEventToEnfermeria(String data)`**  
  Envía un evento SSE a la enfermería.

---

## Interfaz Web: `index.html`
La interfaz web `index.html` proporciona una visualización de la simulación del parque de dinosaurios. Incluye elementos para mostrar el estado de los dinosaurios, huevos, visitantes y otros eventos en tiempo real.

---

## Tecnologías Usadas
- **Spring Boot**: Framework para construir aplicaciones Java.
- **Project Reactor**: Librería para programación reactiva.
- **RabbitMQ**: Sistema de mensajería para comunicación entre componentes.
- **SLF4J**: API de logging.
- **Reactor Core**: Librería para programación reactiva con soporte para flujos de datos asíncronos.
- **Java**: Lenguaje de programación principal del proyecto.

---

## Endpoints REST
- **`/sse`**: Endpoint para manejar eventos SSE generales.
- **`/sse/{isla}`**: Endpoint para manejar eventos SSE específicos para una isla.

---

Este proyecto simula un parque de dinosaurios con múltiples componentes que interactúan de manera concurrente. Utiliza tecnologías modernas para manejar la concurrencia y la comunicación entre componentes, proporcionando una experiencia de simulación rica y detallada.
