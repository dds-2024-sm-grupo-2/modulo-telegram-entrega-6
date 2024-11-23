package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.clients.ColaboradoresProxy;
import ar.edu.utn.dds.k3003.controller.IncidenteController;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO;
import ar.edu.utn.dds.k3003.model.dtos.FormasDeColaborarDTO;
import ar.edu.utn.dds.k3003.model.enums.MisFormasDeColaborar;
import io.javalin.Javalin;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import java.util.*;

import io.javalin.micrometer.MicrometerPlugin;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmHeapPressureMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class WebApp extends TelegramLongPollingBot {

    private static ColaboradoresProxy fachadaColaboradores;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            var chat_id = update.getMessage().getChatId();
            var message_received = update.getMessage().getText();

            var mensaje_dividido = message_received.split(" ");

            switch (mensaje_dividido[0]) {
                case "/start":
                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("BIENVENIDO AL CHATBOT DEL TP DE DISEÑO - 2024\n " +
                            "PARA CONTINUAR, UTILIZA ALGUNO DE LOS SIGUEINTES COMANDOS: \n" +
                            "/datos {colabID} \n" +
                            "/cambiarFormas {colabID} {[formas]}");
                    try {
                        execute(msg);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                case "/datos":
                    var id_colaborador = Long.parseLong(mensaje_dividido[1]);
                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(id_colaborador);

                    SendMessage msg1 = new SendMessage();
                    msg1.setChatId(chat_id);
                    msg1.setText("DATOS DEL COLABORADOR: \n"
                            + "ID: " +  colaboradorDTO.getId()
                            + "\nNOMBRE: " + colaboradorDTO.getNombre()
                            + "\nPUNTOS: " + colaboradorDTO.getPuntos()
                            + "\nDINERO_DONADO: " + colaboradorDTO.getDineroDonado()
                            + "\nHELADERAS_REPARADAS: " + colaboradorDTO.getHeladerasReparadas()
                            //+ "\nFORMAS_DE_COLABORAR: " + colaboradorDTO.getFormas(); TODO: ARREGLAR COMO PRINTEA UNA LISTA
                            );

                    try {
                        execute(msg1);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                case "/cambiarFormas":
                    var id = Long.parseLong(mensaje_dividido[1]);
                    var formasSTR = mensaje_dividido[2];
                    String sinCorchetes = formasSTR.replace("[", "").replace("]", "").trim();
                    System.out.println(sinCorchetes);
                    // Divide por comas y convierte a una lista
                    String[] formas = sinCorchetes.split(",");
                    List<MisFormasDeColaborar> formasLista = new ArrayList<>();

                    System.out.println(formas);

                    for(String forma: formas){
                        formasLista.add(MisFormasDeColaborar.valueOf(forma.toUpperCase()));
                    }

                    System.out.println(formasLista);

                    FormasDeColaborarDTO formasDeColaborarDTO = new FormasDeColaborarDTO();

                    formasDeColaborarDTO.setFormas(formasLista);

                    fachadaColaboradores.cambiarFormas(id, formasDeColaborarDTO);

                    SendMessage msg2 = new SendMessage();
                    msg2.setChatId(chat_id);
                    msg2.setText("Formas cambiadas correctamente");

                    try {
                        execute(msg2);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
            }

        }
    }
    @Override
    public String getBotUsername() {
        return "DiSis24Bot";
    }

    @Override
    public String getBotToken() {
        return "8048605483:AAHi8jiQ7cSJfNsmGZZhV261K1zAhXjg5YI";
    }

    public static void main(String[] args) throws TelegramApiException {

        final var registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        registry.config().commonTags("app", "metrics-sample");

        var env = System.getenv();
        var objectMapper = createObjectMapper();
        var fachada = new Fachada();


        fachadaColaboradores = new ColaboradoresProxy(objectMapper);
        fachada.setColaboradoresProxy(fachadaColaboradores);

        fachada.setRegistry(registry);

        var port = Integer.parseInt(env.getOrDefault("PORT", "8080"));

        try (var jvmGcMetrics = new JvmGcMetrics();
             var jvmHeapPressureMetrics = new JvmHeapPressureMetrics()) {
            jvmGcMetrics.bindTo(registry);
            jvmHeapPressureMetrics.bindTo(registry);
        }
        new JvmMemoryMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new FileDescriptorMetrics().bindTo(registry);

        final var micrometerPlugin = new MicrometerPlugin(config -> config.registry = registry);

        var incidentesController = new IncidenteController(fachada);

        var app = Javalin.create(config -> { config.registerPlugin(micrometerPlugin); }).start(port);

        // Home
        app.get("/", ctx -> ctx.result("Modulo Frontend - Diseño de Sistemas K3003 - UTN FRBA"));

        // Maquetacion de los endpoints de incidentes
        app.post("/incidentes", incidentesController::agregar);
        app.get("/incidentes/{id}", incidentesController::obtener);
        app.patch("/incidentes/{id}", incidentesController::actualizar);

        // Endpoint para eliminar todos los incidentes
        app.delete("/incidentes", incidentesController::eliminar);

        // Endpoint para obtener las métricas
        app.get("/metrics", ctx -> {
            var auth = ctx.header("Authorization");
            if (auth != null && auth.equals("Bearer " + env.get("TOKEN"))) {
                ctx.contentType("text/plain; version=0.0.4")
                        .result(registry.scrape());
            } else {
                ctx.status(401).json("{\"error\": \"Unauthorized access\"}");
            }
        });

        // Manejador global para todas las excepciones
        app.exception(Exception.class, (e, ctx) -> {

            // Header del content type
            ctx.contentType("application/json");

            // Codigo de estado HTTP
            if (e instanceof TrasladoNoAsignableException) {
                ctx.status(400); // Bad Request
            } else if (e instanceof NoSuchElementException) {
                ctx.status(404); // Not Found
            } else {
                ctx.status(500); // Internal Server Error
            }

            // Cuerpo de la respuesta
            ctx.result("{\"error\": \"" + e.getMessage() + "\"}");
        });

        // Se crea un nuevo Bot API
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            // Se registra el bot
            telegramBotsApi.registerBot(new WebApp());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);
        return objectMapper;
    }
}