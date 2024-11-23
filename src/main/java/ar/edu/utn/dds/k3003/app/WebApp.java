package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.clients.ColaboradoresProxy;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

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
                    //Hacer logica para que mande un mensaje de bienvenida
                    return;
                case "/datos":
                    var id_colaborador = Long.parseLong(mensaje_dividido[1]);
                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(id_colaborador);

                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("DATOS DEL COLABORADOR: \n" + colaboradorDTO.toString());

                    try {
                        execute(msg);
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
        return "8048605483:AAEz5Bomzvm6QW2F-v6Cm_X7dYNjWo3Pplg";
    }

    public static void main(String[] args) throws TelegramApiException {

        var objectMapper = createObjectMapper();
        fachadaColaboradores = new ColaboradoresProxy(objectMapper);

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

/*    public static void main(String[] args) {


        final var registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        registry.config().commonTags("app", "metrics-sample");

        var env = System.getenv();
        var objectMapper = createObjectMapper();
        var fachada = new Fachada();

        fachada.setViandasProxy(new ViandasProxy(objectMapper));
        fachada.setHeladerasProxy(new HeladerasProxy(objectMapper));
        fachada.setColaboradoresProxy(new ColaboradoresProxy(objectMapper));
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
    }
*/