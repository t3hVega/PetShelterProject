package com.petshelterproject.telegrambot.alternate;

import com.petshelterproject.telegrambot.configuration.BotConfig;
import com.petshelterproject.telegrambot.sender.TelegramMessageSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class PetShelterBot extends TelegramLongPollingBot {
    private BotConfig botConfig;
    private TelegramMessageSender messageSender;
    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String updateMessageText = update.getMessage().getText();
            if (updateMessageText.equals("/start")) {
                try {
                    execute(messageSender.firstStageMenu(chatId));
                } catch (TelegramApiException e) {
                }
            }

        } else if (update.getCallbackQuery().getData().equals("ПРИЮТ ДЛЯ КОШЕК")
                || update.getCallbackQuery().getData().equals("ПРИЮТ ДЛЯ СОБАК")) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            try {
                execute(messageSender.sendMessage(chatId, "Адрес приюта будет передан в скором времени"));
            } catch (TelegramApiException e) {
            }
        }
    }


}