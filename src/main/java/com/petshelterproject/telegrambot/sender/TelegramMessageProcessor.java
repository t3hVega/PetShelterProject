package com.petshelterproject.telegrambot.sender;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramMessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(TelegramMessageProcessor.class);

    /**
     * Метод отправляет сообщение в чат по его id с задаваемым текстом
     * @param chatId
     * @param messageText
     */
    public SendMessage sendMessage(Long chatId, String messageText){
        logger.info("Идет отправка сообщения в чат " + chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(messageText);
        return sendMessage;
    }
    public SendMessage firstStageMenu (Long chatId) {
        logger.info("Открываем меню для чата " + chatId);
        SendMessage firstStageMenuMessage = new SendMessage();
        firstStageMenuMessage.setChatId(chatId.toString());
        firstStageMenuMessage.setText("Вас приветствует бот-ассистент для усыновления животных. \n" +
                "1. Выберите животного, которого вы хотите усыновить");
        firstStageMenuMessage.setReplyMarkup(replyKeyboardMarkup());
        return firstStageMenuMessage;

    }

    @Bean
    public ReplyKeyboardMarkup replyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboardRows());

        return replyKeyboardMarkup;
    }

    @Bean
    public @NonNull List<KeyboardRow> keyboardRows() {
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow(keyboardButtons()));
        return rows;

    }

    @Bean
    public List<KeyboardButton> keyboardButtons() {
        List<KeyboardButton> buttons = new ArrayList<>();
        buttons.add(new KeyboardButton("\uD83D\uDC31 Кошку"));
        buttons.add(new KeyboardButton("\uD83D\uDC36 Собаку"));
        return buttons;

    }

}
