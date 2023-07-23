package org.example;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args)  {

        TelegramBotsApi botsApi = null;

        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);

        }




        try {
            botsApi.registerBot(new MyBot()); // Register and start your bot
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

}