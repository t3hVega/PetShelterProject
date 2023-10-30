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
public class UserMessageProcessor {
    private final Logger logger = LoggerFactory.getLogger(UserMessageProcessor.class);

    /**
     * Метод возвращает меню в чат к обратившемуся пользователю
     * Пока что в состоянии заглушки, требует расширения для "модульности"
     * @param chatId
     * @return firstStageMenuMessage
     */
    public SendMessage firstStageMenu (Long chatId) {
        logger.info("Открываем меню приветствия для чата " + chatId);
        SendMessage firstStageMenuMessage = new SendMessage();
        firstStageMenuMessage.setChatId(chatId.toString());
        firstStageMenuMessage.setText("Вас приветствует бот-ассистент для усыновления животных. \n" +
                "1. Выберите животного, которого вы хотите усыновить");
        List<KeyboardButton> firstRow = Arrays.asList(
                new KeyboardButton("\uD83D\uDC31 Кошку"),
                new KeyboardButton("\uD83D\uDC36 Собаку")
        );
        List<List<KeyboardButton>> buttonsToAdd = List.of(
                firstRow
        );
        firstStageMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return firstStageMenuMessage;

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
        secondStageMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return secondStageMenuMessage;
    }

    public SendMessage shelterInfoMenu (Long chatId) {
        logger.info("Открываем меню информации о приюте для чата " + chatId);
        SendMessage shelterInfoMenuMessage = new SendMessage();
        shelterInfoMenuMessage.setChatId(chatId.toString());
        shelterInfoMenuMessage.setText("Выберите нужную вам опцию");
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
        List<KeyboardButton> fifthRow = List.of(
                new KeyboardButton("⬅\uFE0F Вернуться в предыдущее меню")
        );

        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow,
                thirdRow,
                fourthRow,
                fifthRow
        );
        shelterInfoMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return shelterInfoMenuMessage;
    }


    public SendMessage catAdoptionAssistMenu (Long chatId) {
        logger.info("Открываем меню - как взять животное из приюта " + chatId);
        SendMessage catAdoptionAssistMenuMessage = new SendMessage();
        catAdoptionAssistMenuMessage.setChatId(chatId.toString());
        catAdoptionAssistMenuMessage.setText("Выберите нужную вам опцию");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("\uD83E\uDDF0 Рекомендации по транспортировке животного"),
                new KeyboardButton("Правила знакомства с животным до того, как забрать его из приюта")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("\uD83D\uDDD2 Документы, необходимые для того, чтобы взять животное из приюта")
        );
        List<KeyboardButton> thirdRow = List.of(
                new KeyboardButton("Рекомендации по обустройству дома для взрослого животного"),
                new KeyboardButton("Рекомендации по обустройству дома для котенка")
        );
        List<KeyboardButton> fourthRow = List.of(
                new KeyboardButton("\uD83E\uDDBD Рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)")
        );
        List<KeyboardButton> fifthRow = List.of(
                new KeyboardButton("\uD83D\uDEAB Причины, почему могут отказать и не дать забрать кошку из приюта  ")
        );
        List<KeyboardButton> sixthRow = List.of(
                new KeyboardButton("\uD83D\uDCDD Принять и записать контактные данные для связи"),
                new KeyboardButton("✋ Позвать волонтера")
        );
        List<KeyboardButton> seventhRow = List.of(
                new KeyboardButton("⬅\uFE0F Вернуться в предыдущее меню")
        );

        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow,
                thirdRow,
                fourthRow,
                fifthRow,
                sixthRow,
                seventhRow
        );
        catAdoptionAssistMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return catAdoptionAssistMenuMessage;
    }

    public SendMessage dogAdoptionAssistMenu (Long chatId) {
        logger.info("Открываем меню - как взять животное из приюта " + chatId);
        SendMessage catAdoptionAssistMenuMessage = new SendMessage();
        catAdoptionAssistMenuMessage.setChatId(chatId.toString());
        catAdoptionAssistMenuMessage.setText("Выберите нужную вам опцию");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("\uD83E\uDDF0 Рекомендации по транспортировке животного"),
                new KeyboardButton("Правила знакомства с животным до того, как забрать его из приюта")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("\uD83D\uDDD2 Документы, необходимые для того, чтобы взять животное из приюта")
        );
        List<KeyboardButton> thirdRow = List.of(
                new KeyboardButton("Рекомендации по обустройству дома для взрослого животного"),
                new KeyboardButton("Рекомендации по обустройству дома для щенка")
        );
        List<KeyboardButton> fourthRow = List.of(
                new KeyboardButton("\uD83E\uDDBD Рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)")
        );
        List<KeyboardButton> fifthRow = List.of(
                new KeyboardButton("Cоветы кинолога по первичному общению с собакой")
        );
        List<KeyboardButton> sixthRow = List.of(
                new KeyboardButton("\uD83D\uDEAB Причины, почему могут отказать и не дать забрать собаку из приюта")
        );
        List<KeyboardButton> seventhRow = List.of(
                new KeyboardButton("Кинологи, которых мы рекомендуем"),
                new KeyboardButton("✋ Позвать волонтера")
        );
        List<KeyboardButton> eigthRow = List.of(
                new KeyboardButton("⬅\uFE0F Вернуться в предыдущее меню")
        );

        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow,
                thirdRow,
                fourthRow,
                fifthRow,
                sixthRow,
                seventhRow,
                eigthRow
        );
        catAdoptionAssistMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return catAdoptionAssistMenuMessage;
    }

    public SendMessage forcedApplyMenu(Long chatId) {
        logger.info("Открываем форсированное меню регистрации для чата " + chatId);
        SendMessage forcedApplyMenuMessage = new SendMessage();
        forcedApplyMenuMessage.setChatId(chatId.toString());
        forcedApplyMenuMessage.setText("Вы не были найдены в базе потенциальных хозяинов. Не желаете передать данные для связи с вами?");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("✅ Да, записать мои контактные данные для связи")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("❌ Нет, вернуться в предыдущее меню")
        );

        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow
        );
        forcedApplyMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return forcedApplyMenuMessage;
    }

    public SendMessage adopterAlreadyExistsMenu(Long chatId) {
        logger.info("Открываем меню для уже существующего пользователя " + chatId);
        SendMessage adopterAlreadyExistsMenuMessage = new SendMessage();
        adopterAlreadyExistsMenuMessage.setChatId(chatId.toString());
        adopterAlreadyExistsMenuMessage.setText("Вы уже зарегистрированы. Желаете изменить данные?");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("✅ Да, изменить мои данные")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("❌ Нет, вернуться в предыдущее меню")
        );

        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow
        );
        adopterAlreadyExistsMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return adopterAlreadyExistsMenuMessage;
    }

    public SendMessage applicationSuccessfulMenu(Long chatId) {
        logger.info("Открываем меню для зарегистрированного пользователя " + chatId);
        SendMessage applicationSuccesfulMenuMessage = new SendMessage();
        applicationSuccesfulMenuMessage.setChatId(chatId.toString());
        applicationSuccesfulMenuMessage.setText("Поздравляем с регистрацией, скоро мы с вами свяжемся, желаете продолжить работу с ботом?");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("✅ Да")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("❌ Нет")
        );
        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow
        );
        applicationSuccesfulMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return applicationSuccesfulMenuMessage;
    }

    public SendMessage reportMenu(Long chatId) {
        logger.info("Открываем меню заполнения отчета о питомце для чата " + chatId);
        SendMessage reportMenuMessage = new SendMessage();
        reportMenuMessage.setChatId(chatId.toString());
        reportMenuMessage.setText("Не забудьте прикрепить фотографию к отчету!");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("⬅\uFE0F Вернуться в предыдущее меню")
        );
        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow
        );
        reportMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return reportMenuMessage;
    }

    public SendMessage forceClosureMenu(Long chatId) {
        logger.info("Открываем меню закрытия пользовательского чата " + chatId);
        SendMessage forceClosureMenuMessage = new SendMessage();
        forceClosureMenuMessage.setChatId(chatId.toString());
        forceClosureMenuMessage.setText("Для волонтерской работы необходимо завершить основной режим работы." +
                " Вы желаете прекратить работу с ботом и начать волонтерскую работу?");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("✅ Да, желаю")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("❌ Нет, не желаю")
        );
        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow
        );
        forceClosureMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return forceClosureMenuMessage;
    }

    public SendMessage thanksMenu(Long chatId) {
        logger.info("Открываем благодарное меню для чата " + chatId);
        SendMessage thanksMenuMessage = new SendMessage();
        thanksMenuMessage.setChatId(chatId.toString());
        thanksMenuMessage.setText("Благодарим за отчет! Желаете продолжить работу с ботом?");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("✅ Да")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("❌ Нет")
        );
        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow,
                secondRow
        );
        thanksMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return thanksMenuMessage;
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