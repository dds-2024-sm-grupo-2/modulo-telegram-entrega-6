package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.clients.ColaboradoresProxy;
import ar.edu.utn.dds.k3003.clients.HeladerasProxy;
import ar.edu.utn.dds.k3003.clients.ViandasProxy;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javassist.bytecode.ParameterAnnotationsAttribute;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Bot extends TelegramLongPollingBot {

    private static ColaboradoresProxy fachadaColaboradores;

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()){
            var chat_id = update.getMessage().getChatId();
            var username = update.getMessage().getChat().getUserName();
            var message_received = update.getMessage().getText();

            var mensaje_dividido = message_received.split(" ");

            switch (mensaje_dividido[0]){
                case "/start":
                    //Hacer logica para que mande un mensaje de bienvenida
                    return;
                case "/datos":
                    var id_colaborador = Long.parseLong(mensaje_dividido[1]);
                    ColaboradorDTO colaboradorDTO = fachadaColaboradores.getColab(id_colaborador);

                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("DATOS DEL COLABORADOR: \n" + colaboradorDTO.toString());

                    try{
                        execute(msg);
                    }catch (TelegramApiException e) {
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
    public String getBotToken(){
        return "8048605483:AAEz5Bomzvm6QW2F-v6Cm_X7dYNjWo3Pplg";
    }

    public static void main(String[] args) throws TelegramApiException {

        var objectMapper = createObjectMapper();
        fachadaColaboradores = new ColaboradoresProxy(objectMapper);

        // Se crea un nuevo Bot API
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
        // Se registra el bot
            telegramBotsApi.registerBot(new Bot());
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
