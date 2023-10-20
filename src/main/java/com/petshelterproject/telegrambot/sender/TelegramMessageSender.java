package com.petshelterproject.telegrambot.sender;

import com.petshelterproject.telegrambot.alternate.PetShelterBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramMessageSender {

    private final Logger logger = LoggerFactory.getLogger(TelegramMessageSender.class);

    /**
     * Метод отправляет сообщение в чат по его id с задаваемым текстом
     * @param chatId
     * @param messageText
     */
    public SendMessage sendMessage(Long chatId, String messageText){
        logger.info("прием");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(messageText);
        return sendMessage;
    }
    public SendMessage firstStageMenu (long chat_id) {

        SendMessage firstStageMenuMessage = new SendMessage();
        firstStageMenuMessage.setChatId(chat_id);
        firstStageMenuMessage.setText("Вас приветствует бот-ассистент для усыновления животных. \n" +
                "1. Выберите животного, которого вы хотите усыновить");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton catShelterButton = new InlineKeyboardButton();
        catShelterButton.setText("\uD83D\uDC31 Кошку");
        catShelterButton.setCallbackData("ПРИЮТ ДЛЯ КОШЕК");
        InlineKeyboardButton dogShelterButton = new InlineKeyboardButton();
        dogShelterButton.setText("\uD83D\uDC36 Собаку");
        dogShelterButton.setCallbackData("ПРИЮТ ДЛЯ СОБАК");
        rowInline1.add(catShelterButton);
        rowInline1.add(dogShelterButton);

        rowsInline.add(rowInline1);

        markupInline.setKeyboard(rowsInline);
        firstStageMenuMessage.setReplyMarkup(markupInline);

        return firstStageMenuMessage;

    }

    public void send(long chatId, String s) {
    }
}
