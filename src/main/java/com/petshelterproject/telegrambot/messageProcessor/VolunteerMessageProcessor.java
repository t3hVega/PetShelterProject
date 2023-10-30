package com.petshelterproject.telegrambot.messageProcessor;

import com.petshelterproject.model.Adopter;
import com.petshelterproject.model.Animal;
import com.petshelterproject.repository.AdopterRepository;
import com.petshelterproject.repository.AnimalRepository;
import com.petshelterproject.repository.VolunteerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class VolunteerMessageProcessor {
    private final Logger logger = LoggerFactory.getLogger(VolunteerMessageProcessor.class);
    private final VolunteerRepository volunteerRepository;
    private final AdopterRepository adopterRepository;
    private final AnimalRepository animalRepository;

    public VolunteerMessageProcessor(VolunteerRepository volunteerRepository,
                                     AdopterRepository adopterRepository,
                                     AnimalRepository animalRepository) {
        this.volunteerRepository = volunteerRepository;
        this.adopterRepository = adopterRepository;
        this.animalRepository = animalRepository;
    }

    public SendMessage startMenu (Long chatId) {
        logger.info("Открываем меню приветствия волонтера " + chatId);
        SendMessage startMenuMessage = new SendMessage();
        startMenuMessage.setChatId(chatId.toString());
        startMenuMessage.setText("Привет, " + volunteerRepository.findByChatId(chatId).getName() + ".");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("Изменить данные")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("Потенциальные хозяева")
        );
        List<KeyboardButton> thirdRow = List.of(
                new KeyboardButton("Отчеты потенциальных хозяев")
        );
        List<KeyboardButton> fourthRow = List.of(
                new KeyboardButton("Завершить работу")
        );
        List<List<KeyboardButton>> buttonsToAdd = List.of(
                firstRow,
                secondRow,
                thirdRow,
                fourthRow
        );
        startMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return startMenuMessage;
    }

    public SendMessage applicationFirstStage(Long chatId) {
        logger.info("Ввод имени волонтера " + chatId);
        SendMessage enterName = new SendMessage(chatId.toString(), "Привет, твои данные не были найдены, введи свое имя: ");
        return enterName;
    }

    public SendMessage applicationSecondStage(Long chatId) {
        logger.info("Ввод имени волонтера " + chatId);
        SendMessage enterName = new SendMessage(chatId.toString(), "Введи свои контактные данные: ");
        return enterName;
    }

    public SendMessage reportsMenu(Long chatId) {
        logger.info("Открываем первое меню регистрации волонтера " + chatId);
        SendMessage reportsMenuMessage = new SendMessage();
        reportsMenuMessage.setChatId(chatId.toString());
        reportsMenuMessage.setText("Отсортировать отчеты:");
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("По потенциальным хозяевам")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("По животным")
        );
        List<List<KeyboardButton>> buttonsToAdd = List.of(
                firstRow,
                secondRow
        );
        reportsMenuMessage.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return reportsMenuMessage;
    }

    public SendMessage adopterApprovalMenu(Long chatId, Long adopterChatId) {
        logger.info("Открываем меню одобрения потенциального хозяина");
        SendMessage adopterApprovalMenu = new SendMessage();
        adopterApprovalMenu.setChatId(chatId.toString());
        Adopter adopter = adopterRepository.findByChatId(adopterChatId);
        Animal animal = adopter.getAnimal();
        adopterApprovalMenu.setText("Потенциальный хозяин " + adopter.getChatId() + "\n"
        + "Имя: " + adopter.getName() + "\n"
        + "Контактные данные: " + adopter.getData() + "\n"
        + "Рейтинг: " + adopter.getScore() + "\n"
        + "Питомец: " + animal.getName() + ", возраст: " + animal.getAge() + ", " + animal.getKind().getCode() + ", примечения: " + animal.getNotes());
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("Одобрить")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("Отклонить")
        );
        List<KeyboardButton> thirdRow = List.of(
                new KeyboardButton("Продлить испытательный срок на 14 дней")
        );
        List<KeyboardButton> fourthRow = List.of(
                new KeyboardButton("Продлить испытательный срок на месяц")
        );
        List<KeyboardButton> fifthRow = List.of(
                new KeyboardButton("Вернуться в главное меню")
        );
        List<List<KeyboardButton>> buttonsToAdd = List.of(
                firstRow,
                secondRow,
                thirdRow,
                fourthRow,
                fifthRow
        );
        adopterApprovalMenu.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return adopterApprovalMenu;
    }

    public SendPhoto approvalMenu(Long chatId, String fileId, String text) {
        logger.info("Открываем меню оценки отчета");
        SendPhoto approvalMenu = new SendPhoto();
        approvalMenu.setChatId(chatId);
        approvalMenu.setCaption(text);
        approvalMenu.setPhoto(new InputFile(fileId));
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton("Одобрить отчет")
        );
        List<KeyboardButton> secondRow = List.of(
                new KeyboardButton("Отклонить отчет")
        );
        List<List<KeyboardButton>> buttonsToAdd = List.of(
                firstRow,
                secondRow
        );
        approvalMenu.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return approvalMenu;
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
