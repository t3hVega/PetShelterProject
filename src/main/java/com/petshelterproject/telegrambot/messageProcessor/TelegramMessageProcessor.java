package com.petshelterproject.telegrambot.messageProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * Метод возвращает меню в чат к обратившемуся пользователю
     * Пока что в состоянии заглушки, требует расширения для "модульности"
     * @param chatId
     * @return
     */
    public SendMessage firstStageMenu (Long chatId) {
        logger.info("Открываем меню для чата " + chatId);
        SendMessage firstStageMenuMessage = new SendMessage();
        firstStageMenuMessage.setChatId(chatId.toString());
        firstStageMenuMessage.setText("Вас приветствует бот-ассистент для усыновления животных. \n" +
                "1. Выберите животного, которого вы хотите усыновить");
        List<KeyboardButton> firstRow = Arrays.asList(
            new KeyboardButton("\uD83D\uDC31 Кошку"),
            new KeyboardButton("\uD83D\uDC36 Собаку")
        );
        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow
        );
        firstStageMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return firstStageMenuMessage;

    }

    private ReplyKeyboardMarkup replyKeyboardMarkup(List<List<KeyboardButton>> buttonsToAdd) {
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < buttonsToAdd.size(); i++) {
            rows.add(new KeyboardRow(buttonsToAdd.get(i)));
        }
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

}
