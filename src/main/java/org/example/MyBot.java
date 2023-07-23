package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            String callbackQueryId = callbackQuery.getId();
            Long chatId = callbackQuery.getMessage().getChatId();

            if (callbackData.equals("/uzbek")) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Uzbek");
                sendMessage(message);
                uzbek(callbackQueryId);
            } else if (callbackData.equals("/russian")) {
                rus(callbackQueryId);
            } else {
                System.out.println("Unknown callback data: " + callbackData);
            }
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                start(chatId);
            } else {
                List<String> musicsPath = fileSearch(messageText);
                sendMusic(chatId, musicsPath);

            }
        }else if (update.hasMessage()){
            Audio audio = update.getMessage().getAudio();
            GetFile getFile = new GetFile (audio.getFileId());


            try {
                File file = execute(getFile);

                String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + file.getFilePath();
                URL url = new URL(fileUrl);
                InputStream inputStream = url.openStream();


                String fileName = audio.getFileName();
                String performer = audio.getPerformer();
                String savePath = "C:\\Users\\Admin\\Desktop\\audios\\"+ fileName;
                FileOutputStream outputStream = new FileOutputStream(savePath);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);


                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer,0,1024 )) !=-1){
                    bufferedOutputStream.write(buffer,0,bytesRead);
                }
                inputStream.close();
                bufferedOutputStream.close();
                outputStream.close();

                System.out.println("Audio file saved to:" + savePath);




            } catch (TelegramApiException e) {
                throw new RuntimeException(e);

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }

    public List<String> fileSearch(String musicName){
        String folderPath = "C:\\Users\\Admin\\Desktop\\audios";
        java.io.File folder =  new java.io.File(folderPath);
        List<String> musicsAbsolutePath = new ArrayList<>();


        if (folder.exists() && folder.isDirectory()) {
            List<java.io.File> musics = List.of(folder.listFiles());

            if (musics != null){
                for (java.io.File music : musics){
                    if (music.isFile()){

                        if (music.getName().toLowerCase().contains(musicName.toLowerCase())){
                            String musicAbsolutePath = music.getAbsolutePath();
                            musicsAbsolutePath.add(musicAbsolutePath);

                        }

                    }else {
                        System.out.println("It is not file");

                    }
                }
            }


        }else {
            System.out.println("cannot file folder " + folderPath);

        }

        return musicsAbsolutePath;

    }

    public void sendMusic (Long chatId, List<String> musicsAbsolutePath){

        for (String path : musicsAbsolutePath){
            SendAudio sendAudio = new SendAudio();
            sendAudio.setChatId(chatId);
            sendAudio.setAudio(new InputFile(new java.io.File(path)));



            try{
                execute(sendAudio);
            }catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }

    }

    public void uzbek(String queryId) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(queryId);
        answer.setText("Uzbek");

        try {
            execute(answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void rus(String queryId) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(queryId);
        answer.setText("Russian");

        try {
            execute(answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hello, how are you? Choose a language");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Uzbek");
        button1.setCallbackData("/uzbek");
        row.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Russian");
        button2.setCallbackData("/russian");
        row.add(button2);

        buttons.add(row);
        markup.setKeyboard(buttons);
        message.setReplyMarkup(markup);

        sendMessage(message);
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "t.me/boom_sevinch_bot";
    }

    @Override
    public String getBotToken() {
        return "6025735998:AAGL1fw7uHYabog4lWHClS7lDKjxpkEOj9I";
    }
}