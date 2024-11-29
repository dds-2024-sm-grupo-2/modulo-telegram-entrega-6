package ar.edu.utn.dds.k3003.model.Controller;

import ar.edu.utn.dds.k3003.model.dtos.ColaboradorConChatDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.EntityManager;

public class TelegramController extends TelegramLongPollingBot {
    private ObjectMapper objectMapper;

    public TelegramController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void notificar(Context ctx){
        var idString = ctx.pathParam("chatID");
        Long chatID = Long.parseLong(idString);
        var msg = ctx.bodyAsClass(String.class);

        SendMessage msg2 = new SendMessage();
        msg2.setChatId(chatID);
        msg2.setText(msg);

        try {
            execute(msg2);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return null;
    }
}
