package com.petshelterproject.telegrambot.sender;

import jdk.internal.icu.lang.UCharacterDirection;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TelegramMessageProcessor<buttonsToAdd> {

    private final Logger logger = LoggerFactory.getLogger(TelegramMessageProcessor.class);
    private UCharacterDirection chatId;

    /**
     * Метод отправляет сообщение в чат по его id с задаваемым текстом
     *
     * @param chatId
     * @param messageText
     */
    public SendMessage sendMessage(Long chatId, String messageText) {
        logger.info("Идет отправка сообщения в чат " + chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(messageText);
        return sendMessage;
    }

    /**
     * Метод возвращает меню в чат к обратившемуся пользователю
     * Пока что в состоянии заглушки, требует расширения для "модульности"
     *
     * @return
     */
    public SendMessage firstStageMenu() {
        logger.info("Открываем меню для чата " + chatId);
        SendMessage firstStageMenuMessage = new SendMessage();
        firstStageMenuMessage.setChatId(chatId.toString());
        firstStageMenuMessage.setText("Вас приветствует бот-ассистент для усыновления животных. \n" +
                "1. Выберите животного, которого вы хотите усыновить");
        firstStageMenuMessage.setReplyMarkup(replyKeyboardMarkup());
        return firstStageMenuMessage;

    }

    @Bean
    public ReplyKeyboardMarkup replyKeyboardMarkup(List<List<KeyboardButton>> buttonsToAdd) {
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
    private List<KeyboardButton> firstRow;

    private ReplyKeyboard replyKeyboardMarkup(buttonsToAdd) {
        return firstStageMenu().getReplyMarkup();
    }

    public SendMessage secondStageMenu (Long chatId, String reply) {
        logger.info("Открываем меню приюта для чата " + chatId);
        SendMessage secondStageMenuMessage = new SendMessage();
        secondStageMenuMessage.setChatId(chatId.toString());
        if (reply.equals("\uD83D\uDC36 Собаку")) {
            secondStageMenuMessage.setText("Вас приветствует приют для собак, выберите нужную вам опцию");
        } else secondStageMenuMessage.setText("Вас приветствует приют для кошек, выберите нужную вам опцию");

        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("ℹ\uFE0F Узнать информацию о приюте")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("\uD83D\uDC3E Прислать отчет о питомце"),
                new KeyboardButton("❓ Как забрать животное из приюта")
        );
        List<KeyboardButton> thirdRow = List.of(
                new KeyboardButton("✋ Позвать волонтера")
        );

        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow,
                thirdRow
        );
        secondStageMenuMessage.setReplyMarkup(replyKeyboardMarkup());
        return secondStageMenuMessage;
    }

    public SendMessage shelterInfoMenu (Long chatId) {
        logger.info("Открываем меню информации о приюте для чата " + chatId);
        SendMessage shelterInfoMenuMessage = new SendMessage();
        shelterInfoMenuMessage.setChatId(chatId.toString());
        shelterInfoMenuMessage.setText("Вас приветствует приют для собак, выберите нужную вам опцию");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("\uD83D\uDD57 Расписание работы приюта, адрес и схема проезда.")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("\uD83D\uDE98 Контактные данные охраны для оформления пропуска на машину")
        );
        List<KeyboardButton> thirdRow = List.of(
                new KeyboardButton("\uD83D\uDED1 Общие рекомендации о технике безопасности на территории приюта")

        );
        List<KeyboardButton> fourthRow = List.of(
                new KeyboardButton("\uD83D\uDCDD Внести контактные данные для связи"),
                new KeyboardButton("✋ Позвать волонтера")
        );

        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow,
                thirdRow,
                fourthRow
        );
        shelterInfoMenuMessage.setReplyMarkup(replyKeyboardMarkup());
        return shelterInfoMenuMessage;
    }

    public SendMessage dogAdoptionAssistMenu (Long chatId) {
        logger.info("Открываем меню - как взять животное из приюта " + chatId);
        SendMessage dogAdoptionAssistMenuMessage = new SendMessage();
        dogAdoptionAssistMenuMessage.setChatId(chatId.toString());
        dogAdoptionAssistMenuMessage.setText("Списки причин за и против , выберите нужную вам опцию");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("\uD83D\uDD57 Список рекомендаций по транспортировке животного")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("\uD83D\uDE98 Правила знакомства с животным до того, как забрать его из приюта")
        );
        List<KeyboardButton> thirdRow = List.of(
                new KeyboardButton("\uD83D\uDED1 Список документов, необходимых для того, чтобы взять животное из приюта")
        );
        List<KeyboardButton> fourthRow = List.of(
                new KeyboardButton("\uD83D\uDD57 Список рекомендаций по обустройству дома для взрослого животного")
        );
        List<KeyboardButton> fifthRow = List.of(
                new KeyboardButton("\uD83D\uDE98 Список рекомендаций по обустройству дома для щенка")
        );
        List<KeyboardButton> sixthRow = List.of(
                new KeyboardButton("\uD83D\uDED1 Список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)")
        );
        List<KeyboardButton> seventhRow = List.of(
                new KeyboardButton("\uD83D\uDE98 список причин, почему могут отказать и не дать забрать собаку из приюта  ")
        );
        List<KeyboardButton> eighthRow = List.of(
                new KeyboardButton("\uD83D\uDCDD Принять и записать контактные данные для связи"),
                new KeyboardButton("✋ Позвать волонтера")
        );

        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow,
                thirdRow,
                fourthRow,
                fifthRow,
                sixthRow,
                seventhRow,
                eighthRow
        );
        dogAdoptionAssistMenuMessage.setReplyMarkup(replyKeyboardMarkup());
        return dogAdoptionAssistMenuMessage;
    }


    private List<KeyboardButton> firstRow;

    ReplyKeyboard replyKeyboardMarkup(buttonsToAdd) {
        return firstStageMenu().getReplyMarkup();
    }

    public SendMessage secondStageMenu (Long chatId, String reply) {
        logger.info("Открываем меню приюта для чата " + chatId);
        SendMessage secondStageMenuMessage = new SendMessage();
        secondStageMenuMessage.setChatId(chatId.toString());
        if (reply.equals("\uD83D\uDC36 Собаку")) {
            secondStageMenuMessage.setText("Вас приветствует приют для собак, выберите нужную вам опцию");
        } else secondStageMenuMessage.setText("Вас приветствует приют для кошек, выберите нужную вам опцию");

        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("ℹ\uFE0F Узнать информацию о приюте")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("\uD83D\uDC3E Прислать отчет о питомце"),
                new KeyboardButton("❓ Как забрать животное из приюта")
        );
        List<KeyboardButton> thirdRow = List.of(
                new KeyboardButton("✋ Позвать волонтера")
        );

        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow,
                thirdRow
        );
        secondStageMenuMessage.setReplyMarkup(replyKeyboardMarkup());
        return secondStageMenuMessage;
    }

    public SendMessage shelterInfoMenu (Long chatId) {
        logger.info("Открываем меню информации о приюте для чата " + chatId);
        SendMessage shelterInfoMenuMessage = new SendMessage();
        shelterInfoMenuMessage.setChatId(chatId.toString());
        shelterInfoMenuMessage.setText("Вас приветствует приют для собак, выберите нужную вам опцию");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("\uD83D\uDD57 Расписание работы приюта, адрес и схема проезда.")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("\uD83D\uDE98 Контактные данные охраны для оформления пропуска на машину")
        );
        List<KeyboardButton> thirdRow = List.of(
                new KeyboardButton("\uD83D\uDED1 Общие рекомендации о технике безопасности на территории приюта")

        );
        List<KeyboardButton> fourthRow = List.of(
                new KeyboardButton("\uD83D\uDCDD Внести контактные данные для связи"),
                new KeyboardButton("✋ Позвать волонтера")
        );

        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow,
                thirdRow,
                fourthRow
        );
        shelterInfoMenuMessage.setReplyMarkup(replyKeyboardMarkup());
        return shelterInfoMenuMessage;
    }

    public SendMessage dogAdoptionAssistMenu (Long chatId) {
        logger.info("Открываем меню - как взять животное из приюта " + chatId);
        SendMessage dogAdoptionAssistMenuMessage = new SendMessage();
        dogAdoptionAssistMenuMessage.setChatId(chatId.toString());
        dogAdoptionAssistMenuMessage.setText("Списки причин за и против , выберите нужную вам опцию");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("\uD83D\uDD57 Список рекомендаций по транспортировке животного")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("\uD83D\uDE98 Правила знакомства с животным до того, как забрать его из приюта")
        );
        List<KeyboardButton> thirdRow = List.of(
                new KeyboardButton("\uD83D\uDED1 Список документов, необходимых для того, чтобы взять животное из приюта")
        );
        List<KeyboardButton> fourthRow = List.of(
                new KeyboardButton("\uD83D\uDD57 Список рекомендаций по обустройству дома для взрослого животного")
        );
        List<KeyboardButton> fifthRow = List.of(
                new KeyboardButton("\uD83D\uDE98 Список рекомендаций по обустройству дома для котенка")
        );
        List<KeyboardButton> sixthRow = List.of(
                new KeyboardButton("\uD83D\uDED1 Список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)")
        );
        List<KeyboardButton> seventhRow = List.of(
                new KeyboardButton("\uD83D\uDE98 список причин, почему могут отказать и не дать забрать кошку из приюта  ")
        );
        List<KeyboardButton> eighthRow = List.of(
                new KeyboardButton("\uD83D\uDCDD Принять и записать контактные данные для связи"),
                new KeyboardButton("✋ Позвать волонтера")
        );

        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow,
                thirdRow,
                fourthRow,
                fifthRow,
                sixthRow,
                seventhRow,
                eighthRow
        );
        dogAdoptionAssistMenuMessage.setReplyMarkup(replyKeyboardMarkup());
        return dogAdoptionAssistMenuMessage;
    }


}

