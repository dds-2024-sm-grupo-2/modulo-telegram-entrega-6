package ar.edu.utn.dds.k3003.clients;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramBotSender extends DefaultAbsSender {
    private final String botToken;

    public TelegramBotSender(String botToken) {
        super(new DefaultBotOptions());
        this.botToken = System.getenv("TOKEN_BOT");
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            Message sentMessage = execute(message);
            System.out.println("Message sent: " + sentMessage.getText());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}