package com.petshelterproject.telegrambot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TelegramMessageSender {

    private final Logger logger = LoggerFactory.getLogger(TelegramMessageSender.class);

    private final TelegramBot telegramBot;

    public TelegramMessageSender(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Метод отправляет сообщение в чат по его id с задаваемым текстом
     * @param chatId
     * @param messageText
     */
    public void send (Long chatId, String messageText) {
        SendMessage message = new SendMessage(chatId, messageText);
        SendResponse response = telegramBot.execute(message);

        if(!response.isOk()) {
            logger.error("Отправка сообщения завершилась ошибкой, response = {}", response);
        } else {
            logger.info("Отправка сообщения успешно завершена, chatId = {}", chatId);
        }
    }

}
