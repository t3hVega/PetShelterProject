package com.petshelterproject.telegrambot.bot;

import com.petshelterproject.model.*;
import com.petshelterproject.repository.*;
import com.petshelterproject.telegrambot.configuration.BotConfig;
import com.petshelterproject.telegrambot.messageProcessor.UserMessageProcessor;
import com.petshelterproject.telegrambot.messageProcessor.VolunteerMessageProcessor;
import com.petshelterproject.telegrambot.updateProcessor.TelegramUpdateProcessor;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PetShelterBot extends TelegramLongPollingBot {

    private final Logger logger = LoggerFactory.getLogger(PetShelterBot.class);
    private BotConfig botConfig;
    private UserMessageProcessor userMessageProcessor;
    private VolunteerMessageProcessor volunteerMessageProcessor;
    private TelegramUpdateProcessor updateProcessor;
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdopterRepository adopterRepository;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private ReportPhotoRepository reportPhotoRepository;

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
            Long chatId = update.getMessage().getChatId();
            Integer messageId = update.getMessage().getMessageId();
            String updateMessageText = update.getMessage().getText();

            switch (updateMessageText) {
                case ("/start"): {
                    if(userRepository.findByChatId(chatId) == null) {
                            setUserActivity(chatId, true);
                        try {
                            execute(userMessageProcessor.firstStageMenu(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        setUserActivity(chatId, true);
                        try {
                            execute(updateProcessor.revert(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                }
                case("/start-as-volunteer"): {
                    if(userRepository.findByChatId(chatId) != null && userRepository.findByChatId(chatId).isActive()) {
                        try {
                            execute(userMessageProcessor.forceClosureMenu(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        if (volunteerRepository.findByChatId(chatId) == null) {
                            setVolunteerActivity(chatId, true);
                            volunteerStatusUpdate(chatId, "/start-as-volunteer");
                            logger.info("Ввод имени волонтера " + chatId);
                            try {
                                execute(volunteerMessageProcessor.applicationFirstStage(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            setVolunteerActivity(chatId, true);
                            volunteerStatusUpdate(chatId, "Главное меню");
                            try {
                                execute(volunteerMessageProcessor.startMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
                }
                default:
            }

            if (volunteerRepository.findByChatId(chatId) != null && volunteerRepository.findByChatId(chatId).isActive()) {
                LocalDate currentDate = LocalDate.from(LocalDate.now().atStartOfDay());
                Set<Long> dueUsers = reportRepository
                        .findAll()
                        .stream()
                        .filter(report -> report.getDate().toEpochDay() + 2 <= currentDate.toEpochDay())
                        .map(Report::getChatId)
                        .collect(Collectors.toSet());
                if(!volunteerRepository.findByChatId(chatId).isReminderActive() &&
                        volunteerRepository.findByChatId(chatId).getName() != null &&
                        volunteerRepository.findByChatId(chatId).getData() != null) {
                    if (!dueUsers.isEmpty()) {
                        try {
                            execute(enterData(chatId, "Прошло два дня с последнего отчета пользователей " + dueUsers.toString() + ", свяжись с ними.", "Принято"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
                        volunteer.setReminderActive(true);
                        volunteerRepository.save(volunteer);
                    } else {
                        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
                        volunteer.setReminderActive(false);
                        volunteerRepository.save(volunteer);
                    }
                }

                switch (updateMessageText) {
                    case ("Изменить данные"): {
                        volunteerStatusUpdate(chatId, updateMessageText);
                        try {
                            execute(enterData(chatId, "Изменить имя:", "Завершить изменение данных"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    case ("Отчеты потенциальных хозяев"): {
                        volunteerStatusUpdate(chatId, updateMessageText);
                        try {
                            execute(volunteerMessageProcessor.reportsMenu(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    case ("Завершить работу"), ("/end"): {
                        setVolunteerActivity(chatId, false);
                        try {
                            sendHideKeyboard(chatId, messageId, "До встречи, " +
                                    volunteerRepository.findByChatId(chatId).getName() + ".");
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    case ("Завершить изменение данных"), ("Вернуться в главное меню"), ("Принято"): {
                        try {
                            execute(volunteerMessageProcessor.startMenu(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        volunteerStatusUpdate(
                                chatId,
                                "Главное меню");
                        break;
                    }
                    case ("По потенциальным хозяевам"), ("Потенциальные хозяева"): {
                        try {
                            execute(enterData(chatId, "Введите Chat ID хозяина:", "Вернуться в главное меню"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        volunteerStatusUpdate(chatId, updateMessageText);
                        break;
                    }
                    case ("По животным"), ("Животные"): {
                        try {
                            execute(enterData(chatId, "Введите ID животного:", "Вернуться в главное меню"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        volunteerStatusUpdate(chatId, updateMessageText);
                        break;
                    }
                    case ("Одобрить отчет"): {
                        Long reportId = volunteerRepository.findByChatId(chatId).getReviewedReportId();
                        Report report = reportRepository.findById(reportId).get();
                        report.setApproved(true);
                        Long adopterChatId = report.getChatId();
                        reportRepository.save(report);
                        Adopter adopter = adopterRepository.findByChatId(adopterChatId);
                        Integer score = adopter.getScore() + 1;
                        adopter.setScore(score);
                        adopterRepository.save(adopter);
                        try {
                            execute(enterData(chatId, "Благодарим за рассмотрение отчета:", "Вернуться в главное меню"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        volunteerStatusUpdate(chatId, updateMessageText);
                        break;
                    }
                    case ("Отклонить отчет"): {
                        Long reportId = volunteerRepository.findByChatId(chatId).getReviewedReportId();
                        Report report = reportRepository.findById(reportId).get();
                        report.setApproved(false);
                        Long adopterChatId = report.getChatId();
                        reportRepository.save(report);
                        Adopter adopter = adopterRepository.findByChatId(adopterChatId);
                        Integer score = adopter.getScore() - 1;
                        adopter.setScore(score);
                        adopterRepository.save(adopter);
                        try {
                            execute(enterData(chatId, "Благодарим за рассмотрение отчета:", "Вернуться в главное меню"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        volunteerStatusUpdate(chatId, updateMessageText);
                        break;
                    }
                    case ("Одобрить"), ("Отклонить"), ("Продлить испытательный срок на 14 дней"), ("Продлить испытательный срок на месяц"): {
                        Long chatIdToSearch = volunteerRepository.findByChatId(chatId).getChatIdToSearch();
                        User user = userRepository.findByChatId(chatIdToSearch);
                        user.setLastMessage(updateMessageText);
                        userRepository.save(user);
                        try {
                            execute(enterData(chatId, "Благодарим за вынесение решения по хозяину", "Вернуться в главное меню"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        volunteerStatusUpdate(chatId, "Вынесено решение");
                    }
                    case ("Оставить сообщение хозяину"): {
                        Long chatIdToSearch = volunteerRepository.findByChatId(chatId).getChatIdToSearch();
                        User user = userRepository.findByChatId(chatIdToSearch);
                        user.setLastMessage(updateMessageText);
                        userRepository.save(user);
                        try {
                            execute(enterData(chatId, "Ввести сообщение для хозяина:", "Вернуться в главное меню"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        volunteerStatusUpdate(chatId, updateMessageText);
                    }
                    default:
                }

                switch (volunteerRepository.findByChatId(chatId).getLastMessage()) {
                    case ("/start-as-volunteer"): {
                        if (!(updateMessageText.equals("/start-as-volunteer"))) {
                            Volunteer volunteer = volunteerRepository.findByChatId(chatId);
                            volunteer.setName(updateMessageText);
                            volunteerRepository.save(volunteer);
                            logger.info("Ввод данных волонтера " + chatId);
                            try {
                                execute(volunteerMessageProcessor.applicationSecondStage(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            volunteerStatusUpdate(
                                    chatId,
                                    "Введено имя");
                        }
                        break;
                    }
                    case ("Изменить данные"): {
                        if (!(updateMessageText.equals("Изменить данные"))) {
                            Volunteer volunteer = volunteerRepository.findByChatId(chatId);
                            volunteer.setName(updateMessageText);
                            volunteerRepository.save(volunteer);
                            logger.info("Ввод данных волонтера " + chatId);
                            try {
                                execute(enterData(chatId, "Изменить контактные данные:", "Завершить изменение данных"));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            volunteerStatusUpdate(
                                    chatId,
                                    "Введено имя");
                        }
                        break;
                    }
                    case ("Введено имя"): {
                        if (!(updateMessageText.equals("Введено имя"))) {
                            Volunteer volunteer = volunteerRepository.findByChatId(chatId);
                            volunteer.setData(updateMessageText);
                            volunteerRepository.save(volunteer);
                            try {
                                execute(volunteerMessageProcessor.startMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            volunteerStatusUpdate(
                                    chatId,
                                    "Главное меню");
                        }
                        break;
                    }
                    case ("По потенциальным хозяевам"), ("Введите Chat ID хозяина:"): {
                        if (!(updateMessageText.equals("Введите Chat ID хозяина") || updateMessageText.equals("По потенциальным хозяевам"))) {
                            Long chatIdToSearch = Long.parseLong(updateMessageText);
                            if (adopterRepository.findByChatId(chatIdToSearch) == null) {
                                try {
                                    execute(enterData(chatId, "Введите Chat ID хозяина:", "Вернуться в главное меню"));
                                } catch (TelegramApiException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Volunteer volunteer = volunteerRepository.findByChatId(chatId);
                                volunteer.setChatIdToSearch(Long.valueOf(updateMessageText));
                                volunteer.setLastMessage("Введен Chat ID хозяина");
                                volunteerRepository.save(volunteer);
                                try {
                                    execute(enterData(chatId, "Введите дату отчета в формате \"дд.мм.гг\" ", "Вернуться в главное меню"));
                                } catch (TelegramApiException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        break;
                    }
                    case ("По животным"), ("Введите ID животного:"): {
                        if (!(updateMessageText.equals("Введите ID животного:") || updateMessageText.equals("По потенциальным хозяевам"))) {
                            Long animalIdToSearch = Long.parseLong(updateMessageText);
                            if (animalRepository.findById(animalIdToSearch) == null) {
                                try {
                                    execute(enterData(chatId, "Введите ID животного:", "Вернуться в главное меню"));
                                } catch (TelegramApiException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Volunteer volunteer = volunteerRepository.findByChatId(chatId);
                                volunteer.setAnimalIdToSearch(Long.valueOf(updateMessageText));
                                volunteer.setLastMessage("Введен ID животного");
                                volunteerRepository.save(volunteer);
                                try {
                                    execute(enterData(chatId, "Введите дату отчета в формате \"дд.мм.гг\"", "Вернуться в главное меню"));
                                } catch (TelegramApiException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        break;
                    }
                    case ("Введен Chat ID хозяина"): {
                        if (!(updateMessageText.equals("Введите дату отчета в формате \"дд.мм.гг\""))) {
                            LocalDate localDate = LocalDate.parse(updateMessageText, dateTimeFormatter);
                            Volunteer volunteer = volunteerRepository.findByChatId(chatId);
                            Long chatIdToSearch = volunteer.getChatIdToSearch();
                            List<Report> reports = reportRepository.findAllByChatId(chatIdToSearch);
                            Report report = reports
                                    .stream()
                                    .filter(report1 -> report1.getDate().equals(localDate))
                                    .findAny()
                                    .get();
                            volunteer.setReviewedReportId(report.getId());
                            volunteerRepository.save(volunteer);
                            String fileId = report.getReportPhoto().getFileId();
                            String text = report.getText();
                            try {
                                execute(volunteerMessageProcessor.approvalMenu(chatId, fileId, text));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    }
                    case ("Введен ID животного"): {
                        if (!(updateMessageText.equals("Введите дату отчета в формате \"дд.мм.гг\""))) {
                            LocalDate localDate = LocalDate.parse(updateMessageText, dateTimeFormatter);
                            Long animalIdToSearch = volunteerRepository.findByChatId(chatId).getAnimalIdToSearch();
                            List<Report> reports = reportRepository.findAllByAnimalId(animalIdToSearch);
                            Report report = reports
                                    .stream()
                                    .filter(report1 -> report1.getDate().equals(localDate))
                                    .findAny()
                                    .get();
                            String fileId = report.getReportPhoto().getFileId();
                            String text = report.getText();
                            try {
                                execute(volunteerMessageProcessor.approvalMenu(chatId, fileId, text));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    }
                    case ("Потенциальные хозяева"): {
                        if (!(updateMessageText.equals("Введите Chat ID хозяина:") || (updateMessageText.equals("Потенциальные хозяева")))) {
                            logger.info("Вводим Chat ID хозяина");
                            Long chatIdToSearch = Long.parseLong(updateMessageText);
                            if (adopterRepository.findByChatId(chatIdToSearch) == null) {
                                try {
                                    execute(enterData(chatId, "Введите Chat ID хозяина:", "Вернуться в главное меню"));
                                } catch (TelegramApiException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Volunteer volunteer = volunteerRepository.findByChatId(chatId);
                                volunteer.setChatIdToSearch(chatIdToSearch);
                                volunteer.setLastMessage("Введен Chat ID хозяина");
                                volunteerRepository.save(volunteer);
                                try {
                                    execute(volunteerMessageProcessor.adopterApprovalMenu(chatId, chatIdToSearch));
                                } catch (TelegramApiException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        break;
                    }
                    case ("Оставить сообщение хозяину"): {
                        if (!updateMessageText.equals("Оставить сообщение хозяину") && !updateMessageText.equals("Ввести сообщение для хозяина:")) {
                            Volunteer volunteer = volunteerRepository.findByChatId(chatId);
                            Long chatIdToSearch = volunteer.getChatIdToSearch();
                            Adopter adopter = adopterRepository.findByChatId(chatIdToSearch);
                            adopter.setLastMessage(updateMessageText);
                            adopterRepository.save(adopter);
                            volunteer.setLastMessage("Введено сообщение хозяину");
                            volunteerRepository.save(volunteer);
                            try {
                                execute(enterData(chatId, "Сообщение отправлено", "Вернуться в главное меню"));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    default:
                }
            }

            if(userRepository.findByChatId(chatId) != null && userRepository.findByChatId(chatId).isActive()) {
                switch (updateMessageText) {
                    case ("/end"), ("❌ Нет"): {
                        setUserActivity(chatId, false);
                        try {
                            sendHideKeyboard(chatId, messageId, "До скорой встречи!");
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    case ("Вернуться в меню"): {
                        try {
                            execute(updateProcessor.back(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    case ("✋ Позвать волонтера"): {
                        if (volunteerRepository.findAll().isEmpty()) {
                            try {
                                execute(enterData(chatId, "Все волонтеры сейчас заняты, с вами свяжутся в ближайшее время", "Вернуться в меню"));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Volunteer volunteer = volunteerRepository.findAll().stream().findAny().get();
                            try {
                                execute(enterData(chatId, "Привет, на связи " + volunteer.getName() +
                                        ", мои контактные данные - " + volunteer.getData(), "Вернуться в меню"));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    }
                    case ("\uD83D\uDD57 Расписание работы приюта, адрес и схема проезда."): {
                        if (userRepository.findByChatId(chatId).isInCatShelter()) {
                            try {
                               execute(enterData(chatId, "Пн-Вс: C 07.30 - 20.00\n" + "Адрес: 141021, Россия, г. Мытищи, ул. Лётная, 32 лит. А", "Вернуться в меню"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                execute(enterData(chatId, "Пн-Вс: C 07.30 - 20.00\n" + "Адрес: 141021, Россия, г. Мытищи, ул. Лётная, 32 лит. Б", "Вернуться в меню"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    }

                    case ("\uD83D\uDE98 Контактные данные охраны для оформления пропуска на машину"): {
                        if (userRepository.findByChatId(chatId).isInCatShelter()) {
                            try {
                                execute(enterData(chatId, "Телефон охраны: +7 495 555 5555", "Вернуться в меню"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                execute(enterData(chatId, "Телефон охраны: +7 495 666 6666.  ", "Вернуться в меню"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    }

                    case ("\uD83D\uDED1 Общие рекомендации о технике безопасности на территории приюта"): {
                        if (userRepository.findByChatId(chatId).isInCatShelter()) {
                            try {
                                execute(enterData(chatId, "Как у каждой организации, у нашего приюта так же есть свои правила- это правила посещения приюта и поведения на территории приюта.\n" +
                                        " Пожалуйста, прочтите и запомните их! Эти простые правила помогут и вам, и нам!\n" +
                                        " Если вы едете в приют впервые, прочтите, пожалуйста, дополнительную инструкцию.\n" +
                                        "Перед поездкой позвоните в приют (+7 495 444 4444) и предупредите о своем приезде.\n" +
                                        "Посмотреть приют и пообщаться с животными можно в субботу и воскресенье с 11 до 16 часов.\n" +
                                        "Берите с собой сменную одежду и одевайтесь по погоде .Обязательно зарегистрируйтесь в журнале посещений!", "Вернуться в меню"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                execute(enterData(chatId, "Как у каждой организации, у нашего приюта так же есть свои правила- это правила посещения приюта и поведения на территории приюта.\n" +
                                        " Пожалуйста, прочтите и запомните их! Эти простые правила помогут и вам, и нам!\n" +
                                        " Если вы едете в приют впервые, прочтите, пожалуйста, дополнительную инструкцию.\n" +
                                        "Перед поездкой позвоните в приют (+7 495 333 3333) и предупредите о своем приезде.\n" +
                                        "Посмотреть приют и пообщаться с животными можно в субботу и воскресенье с 11 до 16 часов.\n" +
                                        "Берите с собой сменную одежду и одевайтесь по погоде .Обязательно зарегистрируйтесь в журнале посещений!  ", "Вернуться в меню"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    }

                    case ("\uD83E\uDDF0 Рекомендации по транспортировке животного"): {
                        try {
                            execute(enterData(chatId, "https://tass.ru/obschestvo/11810901", "Вернуться в меню"));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    case ("Правила знакомства с животным до того, как забрать его из приюта"): {
                        if (userRepository.findByChatId(chatId).isInCatShelter()) {
                            try {
                                execute(enterData(chatId, "Большинство приютов только приветствуют, когда к ним приезжают в гости и помогают с уходом за животными.\n" +
                                        "Поездите несколько раз туда и заодно познакомитесь с местными обитателями.\n" +
                                        "Понаблюдайте за ними: у кого какой характер, повадки и особенности поведения.\n" +
                                        "При выборе учитывайте и свой собственный распорядок дня: не стоит брать того котика, который не сможет ждать вас с работы, если вы постоянно на ней пропадаете.", "Вернуться в меню"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                execute(enterData(chatId, "Позвольте животному познакомиться с контейнером для перевозки и автомобилем, в котором пройдёт дорога домой.\n" +
                                        "Заранее приучите собаку к наморднику - для этого, посещая приют, вкладывайте лакомства в этот аксессуар, используйте его как миску.\n" +
                                        "Через некоторое время можно начать надевать намордник на собаку перед началом прогулки, и снимать через некоторое время.", "Вернуться в меню"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    }

                    case ("\uD83D\uDDD2 Документы, необходимые для того, чтобы взять животное из приюта"): {
                        try {
                            execute(enterData(chatId, "Помимо вашего паспорта для того, чтобы забрать питомца нужно подготовить для него свой ветеринарный паспорт,\n" +
                                    "куда будут занесены прививки, необходимые для животного прежде, чем его можно будет забрать домой", "Вернуться в меню"));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    case ("Рекомендации по обустройству дома для взрослого животного"): {
                        if (userRepository.findByChatId(chatId).isInCatShelter()) {
                            try {
                                execute(enterData(chatId, "https://bfba.ru/chelovek-i-koshka/podgotovka-doma-k-poyavleniyu-koshki.html", "Вернуться в меню"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                execute(enterData(chatId, "https://www.ozon.ru/club/article/uhod-za-zhivotnymi-sovety-veterinarov-i-chek-listy-4711/ - раздел \"обустройство пространства\"", "Вернуться в меню"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    }

                    case ("Рекомендации по обустройству дома для котенка"): {
                        try {
                            execute(enterData(chatId, "https://www.ozon.ru/club/article/uhod-za-zhivotnymi-sovety-veterinarov-i-chek-listy-4711/ - раздел \"обустройство пространства\"", "Вернуться в меню"));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    case ("Рекомендации по обустройству дома для щенка"): {
                        try {
                            execute(enterData(chatId, "https://www.ozon.ru/club/article/chto-nuzhno-dlya-schenka-spisok-pokupok-i-vazhnye-rekomendatsii-6718/", "Вернуться в меню"));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    case ("\uD83E\uDDBD Рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)"): {
                        try {
                            execute(enterData(chatId, "Кошки с ограниченными возможностями, независимо от того, отсутствует ли у них конечность или имеются какие-либо заболевания, заслуживают любви и внимания. \n" +
                                    "Просто потому, что они могут быть менее подвижными, чем кошки с четырьмя лапами, они, скорее всего, будут демонстрировать ответную любовь за то, что вы дали им шанс. \n" +
                                    "И хотя вам может потребоваться некоторое время, чтобы привыкнуть к ним, им тоже нужна любящая семья и кров, как и всем остальным. \n" +
                                    "Итак, если вы планируете завести новую кошку, не отворачивайтесь от той, которая нуждается в небольшом дополнительном уходе,\n" +
                                    "— скоро вы сможете обнаружить, что она более ласковая и любящая, чем вы когда-либо могли себе представить, и она может быть именно такой, о какой вы всегда мечтали.  ", "Вернуться в меню"));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    case ("\uD83D\uDEAB Причины, почему могут отказать и не дать забрать кошку из приюта"),
                            ("\uD83D\uDEAB Причины, почему могут отказать и не дать забрать собаку из приюта"): {
                        try {
                            execute(enterData(chatId,"Существует ряд причин, по которым чаще всего отказывают желающим «усыновить» домашнего любимца.  \n" +
                                    "\n" +
                                    "Большое количество животных дома\n" +
                                    "\n" +
                                    "В конце 2018 года в Российской Федерации был принят закон «Об ответственном обращении с животными». \n" +
                                    "В нем установлено ограничение на количество питомцев, содержащихся в одном домовладении. \n" +
                                    "При этом точное число не названо — в каждом случае вопрос решается индивидуально, исходя из материальных возможностей и жилищных условий человека. \n" +
                                    "Важно, чтобы хозяин обеспечивал правильное кормление и своевременное медицинское обслуживание всем своим животным. \n" +
                                    "Также, согласно санитарно-эпидемиологическим правилам, запрещено скопление большого числа птиц и млекопитающих на небольшой территории — это повышает риск возникновения инфекционных болезней. \n" +
                                    "Нестабильные отношения в семье Любые перемены — это стресс для животного. Кошки и собаки привыкают к людям, скучают после вынужденной разлуки с хозяевами и даже отказываются от еды. \n" +
                                    "Если семейная пара планирует развод, не стоит брать питомца, чтобы потом не обрекать его на страдания. \n" +
                                    "Случается и так, что после расставания ни муж, ни жена не изъявляют желания заботиться о животном. \n" +
                                    "В этом случае ответственные владельцы подыскивают ему новую семью, а безответственные выгоняют домашнего любимца на улицу. \n" +
                                    "Поскольку работники приютов не способны предугадать, каким будет развитие ситуации, они предпочитают отдавать питомцев только людям, находящимся в крепком и счастливом браке.  \n" +
                                    "\n" +
                                    "Наличие маленьких детей\n" +
                                    "\n" +
                                    "Семьям, где есть дети до 5 лет, сложно взять животное из приюта сразу по двум причинам. \n" +
                                    "Во-первых, есть риск, что собака или кошка будет проявлять агрессию по отношению к маленькому человеку. \n" +
                                    "Во-вторых, дети сами часто провоцируют конфликт, дергая зверька за хвост и уши или бросая в него тяжелые предметы. \n" +
                                    "Происходит это по недосмотру и с молчаливого попустительства взрослых людей, но в итоге виноватым оказывается животное. \n" +
                                    "Если ребенок получит царапины или укусы, родители, вместо того, чтобы искать контакт с питомцем и воспитывать своего отпрыска, предпочтут избавиться от хвостатого «источника опасности».\n" +
                                    "\n" +
                                    "Съемное жилье\n" +
                                    "\n" +
                                    "Не все домовладельцы разрешают арендаторам заводить животных. Отчасти это связано с опасениями, что кот или собака испортят мебель, обои и деревянные части интерьера. \n" +
                                    "Кроме того, в комнатах остается неприятный запах меток и шерсти, устранение которого потребует дополнительных трат. Если хозяин жилья обнаружит обман со стороны квартиранта, \n" +
                                    "то, скорее всего, заявит о желании немедленно расторгнуть договор. После этого и люди, и их питомец окажутся на улице. А поскольку найти съемную жилплощадь, \n" +
                                    "где будут рады присутствию мурлыкающего или лающего создания, не так уж просто, судьба животного будет незавидной: оно снова станет ничейным и бездомным."
                                    , "Вернуться в меню"));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    case ("Cоветы кинолога по первичному общению с собакой"): {
                        try {
                            execute(enterData(chatId, "https://www.novochag.ru/home/pets/kak-nauchit-sobaku-slushatsya-sovety-kinologov-i-sekrety-dressirovki/", "Вернуться в меню"));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    case ("Кинологи, которых мы рекомендуем"): {
                        try {
                            execute(enterData(chatId, "Станислав - @t3hVega\n" +
                                    "Тимур - @kanva1984\n" +
                                    "Сергей - @Anglichaninya", "Вернуться в меню"));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    case ("✅ Да, желаю"): {
                        setUserActivity(chatId, false);
                        if (volunteerRepository.findByChatId(chatId) == null) {
                            setVolunteerActivity(chatId, true);
                            volunteerStatusUpdate(chatId, "Регистрация");
                            try {
                                execute(volunteerMessageProcessor.applicationFirstStage(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            volunteerStatusUpdate(chatId, "Главное меню");
                            try {
                                execute(volunteerMessageProcessor.startMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    }
                    case ("❌ Нет, не желаю"), ("⬅\uFE0F Вернуться в предыдущее меню"), ("❌ Нет, вернуться в предыдущее меню"): {
                        try {
                            execute(updateProcessor.back(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    case ("\uD83D\uDC31 Кошку"): {
                        try {
                            execute(userMessageProcessor.secondStageMenu(chatId, "\uD83D\uDC31 Кошку"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        userStatusUpdate(
                                chatId,
                                true,
                                updateMessageText);
                        break;
                    }

                    case ("\uD83D\uDC36 Собаку"): {
                        try {
                            execute(userMessageProcessor.secondStageMenu(chatId, "\uD83D\uDC36 Собаку"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        userStatusUpdate(
                                chatId,
                                false,
                                updateMessageText);
                        break;
                    }

                    case ("ℹ\uFE0F Узнать информацию о приюте"): {
                        try {
                            execute(userMessageProcessor.shelterInfoMenu(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        userStatusUpdate(
                                chatId,
                                userRepository.findByChatId(chatId).isInCatShelter(),
                                updateMessageText);
                        break;
                    }

                    case ("❓ Как забрать животное из приюта"): {
                        if (userRepository.findByChatId(chatId).isInCatShelter()) {
                            try {
                                execute(userMessageProcessor.catAdoptionAssistMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        } else
                            try {
                                execute(userMessageProcessor.dogAdoptionAssistMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        userStatusUpdate(
                                chatId,
                                userRepository.findByChatId(chatId).isInCatShelter(),
                                updateMessageText);
                        break;
                    }

                    case ("\uD83D\uDC3E Прислать отчет о питомце"): {
                        if(adopterRepository.findByChatId(chatId) == null) {
                            try {
                                execute(userMessageProcessor.forcedApplyMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(
                                    chatId,
                                    userRepository.findByChatId(chatId).isInCatShelter(),
                                    "Форсированная регистрация");
                            break;
                        }
                        if (adopterRepository.findByChatId(chatId).getAnimal() == null) {
                            try {
                                execute(enterData(chatId, "За вами не закреплен питомец, дождитесь пока волонтеры сделают это", ("⬅\uFE0F Вернуться в предыдущее меню")));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(
                                    chatId,
                                    userRepository.findByChatId(chatId).isInCatShelter(),
                                    "Не закреплен питомец");
                            break;
                        }
                        if(reportRepository.findAll().isEmpty()) {
                            try {
                                execute(userMessageProcessor.reportMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(
                                    chatId,
                                    userRepository.findByChatId(chatId).isInCatShelter(),
                                    "Отправка отчета");
                            break;
                        }
                        if (reportRepository
                                .findAll()
                                .stream()
                                .findAny()
                                .get()
                                .getDate()
                                .equals(LocalDate.from(LocalDate.now().atStartOfDay()))){
                            try {
                                execute(enterData(chatId, "За данный день уже внесен отчет", ("⬅\uFE0F Вернуться в предыдущее меню")));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(
                                    chatId,
                                    userRepository.findByChatId(chatId).isInCatShelter(),
                                    "Прислан отчет");
                        } else {
                            try {
                                execute(userMessageProcessor.reportMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(
                                    chatId,
                                    userRepository.findByChatId(chatId).isInCatShelter(),
                                    "Отправка отчета");
                        }
                        break;
                    }

                    case ("\uD83D\uDCDD Внести контактные данные для связи"): {
                        userStatusUpdate(
                                chatId,
                                userRepository.findByChatId(chatId).isInCatShelter(),
                                updateMessageText);
                        if(adopterRepository.findByChatId(chatId) == null) {
                            try {
                                execute(enterData(chatId, "Введите имя:", "⬅\uFE0F Вернуться в предыдущее меню"));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            try {
                                execute(userMessageProcessor.adopterAlreadyExistsMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    }

                    case ("✅ Да, записать мои контактные данные для связи") : {
                        try {
                            execute(enterData(chatId, "Введите имя:", "⬅\uFE0F Вернуться в предыдущее меню"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }

                    case ("✅ Да"): {
                        if (userRepository.findByChatId(chatId).isInCatShelter()) {
                            try {
                                execute(userMessageProcessor.secondStageMenu(chatId, "\uD83D\uDC31 Кошку"));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(
                                    chatId,
                                    true,
                                    "\uD83D\uDC31 Кошку");
                            break;
                        }
                        else {
                            try {
                                execute(userMessageProcessor.secondStageMenu(chatId, "\uD83D\uDC36 Собаку"));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(
                                    chatId,
                                    false,
                                    "\uD83D\uDC36 Собаку");
                            break;
                        }
                    }

                    default:
                }

                switch (userRepository.findByChatId(chatId).getLastMessage()) {
                    case ("Форсированная регистрация"), ("\uD83D\uDCDD Внести контактные данные для связи"): {
                        if (!updateMessageText.equals(userRepository.findByChatId(chatId).getLastMessage())
                                && !updateMessageText.equals("🐾 Прислать отчет о питомце")
                                && !updateMessageText.equals("✅ Да, записать мои контактные данные для связи")) {
                            Adopter adopter = new Adopter();
                            adopter.setChatId(chatId);
                            adopter.setName(updateMessageText);
                            adopter.setScore(0);
                            adopterRepository.save(adopter);
                            try {
                                execute(enterData(chatId, "Введите контактные данные:", "⬅\uFE0F Вернуться в предыдущее меню"));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(
                                    chatId,
                                    userRepository.findByChatId(chatId).isInCatShelter(),
                                    "Введено имя");
                        }
                        break;
                    }
                    case ("Введено имя"): {
                        if (!(updateMessageText.equals(adopterRepository.findByChatId(chatId).getName()))) {
                            adopterRepository.findByChatId(chatId);
                            Adopter adopter = adopterRepository.findByChatId(chatId);
                            adopter.setData(updateMessageText);
                            adopterRepository.save(adopter);
                            try {
                                execute(userMessageProcessor.applicationSuccessfulMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(
                                    chatId,
                                    userRepository.findByChatId(chatId).isInCatShelter(),
                                    "Введены данные");
                        }
                        break;
                    }
                    case ("Отправка отчета"): {
                        if (!updateMessageText.equals("\uD83D\uDC3E Прислать отчет о питомце")) {
                            Report report = new Report();
                            report.setChatId(chatId);
                            report.setDate(LocalDate.from(LocalDate.now().atStartOfDay()));
                            report.setText(updateMessageText);
                            report.setAnimal(adopterRepository.findByChatId(chatId).getAnimal());
                            reportRepository.save(report);
                            try {
                                execute(enterData(chatId, "Прикрепите фотографию к отчету:", "Отменить заполнение отчета"));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(chatId,
                                    userRepository.findByChatId(chatId).isInCatShelter(),
                                    "Прислан текст отчета без фотографии");
                        }
                        break;
                    }
                    case ("Прислана фотография отчета без текста"): {
                        if(!updateMessageText.equals("Введите текст отчета:")) {
                            Report report = reportRepository
                                    .findAll()
                                    .stream()
                                    .filter(report1 -> report1.getText() == null)
                                    .findAny()
                                    .get();
                            report.setText(updateMessageText);
                            reportRepository.save(report);
                            try {
                                execute(userMessageProcessor.thanksMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(chatId,
                                    userRepository.findByChatId(chatId).isInCatShelter(),
                                    "Прислан отчет");
                        }
                        break;
                    }
                }
            }

        }

        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            Long chatId = update.getMessage().getChatId();
            String updateCaptionText = update.getMessage().getCaption();
            switch (userRepository.findByChatId(chatId).getLastMessage()) {
                case ("Отправка отчета"): {
                    ReportPhoto reportPhoto = convertToReportPhoto(update);
                    reportPhotoRepository.save(reportPhoto);
                    Report report = new Report();
                    report.setChatId(chatId);
                    report.setDate(LocalDate.from(LocalDate.now().atStartOfDay()));
                    report.setReportPhoto(reportPhoto);
                    report.setAnimal(adopterRepository.findByChatId(chatId).getAnimal());
                    reportRepository.save(report);
                    if(updateCaptionText == null) {
                        try {
                            execute(enterData(chatId, "Введите текст отчета:", "Отменить заполнение отчета"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        userStatusUpdate(chatId,
                                userRepository.findByChatId(chatId).isInCatShelter(),
                                "Прислана фотография отчета без текста");
                    } else if(!updateCaptionText.equals("\uD83D\uDC3E Прислать отчет о питомце")) {
                        report.setText(updateCaptionText);
                        reportRepository.save(report);
                        try {
                            execute(userMessageProcessor.thanksMenu(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        userStatusUpdate(chatId,
                                userRepository.findByChatId(chatId).isInCatShelter(),
                                "Прислан отчет");
                    }
                    break;
                }
                case ("Прислан текст отчета без фотографии"): {
                    Report report = reportRepository
                            .findAll()
                            .stream()
                            .filter(report1 -> report1.getReportPhoto() == null)
                            .findAny()
                            .get();
                    ReportPhoto reportPhoto = convertToReportPhoto(update);
                    reportPhotoRepository.save(reportPhoto);
                    report.setReportPhoto(reportPhoto);
                    reportRepository.save(report);
                    try {
                        execute(userMessageProcessor.thanksMenu(chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    userStatusUpdate(chatId,
                            userRepository.findByChatId(chatId).isInCatShelter(),
                            "Прислан отчет");
                    break;
                }
            }
        }
    }

    private SendMessage enterData(Long chatId, String text, String buttonText) {
        SendMessage enterData = new SendMessage();
        enterData.setChatId(chatId);
        enterData.setText(text);
        List<KeyboardButton> firstRow = List.of(
                new KeyboardButton(buttonText)
        );
        List<List<KeyboardButton>> buttonsToAdd = Arrays.asList(
                firstRow
        );
        enterData.setReplyMarkup(replyKeyboardMarkup(buttonsToAdd));
        return enterData;
    }

    private void userStatusUpdate (Long chatId, boolean inCatShelter, String lastMessage) {
        User user = userRepository.findByChatId(chatId);
        user.setInCatShelter(inCatShelter);
        user.setLastMessage(lastMessage);
        userRepository.save(user);
    }

    private void volunteerStatusUpdate (Long chatId, String lastMessage) {
        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
        volunteer.setLastMessage(lastMessage);
        volunteerRepository.save(volunteer);
    }

    private void setUserActivity(Long chatId, boolean active) {
        if (userRepository.findByChatId(chatId) == null) {
            User user = new User();
            user.setChatId(chatId);
            user.setActive(true);
            userRepository.save(user);
        } else if (active) {
            User user = userRepository.findByChatId(chatId);
            user.setActive(true);
            userRepository.save(user);
        } else {
            User user = userRepository.findByChatId(chatId);
            user.setActive(false);
            userRepository.save(user);
        }
    }

    private ReportPhoto convertToReportPhoto(Update update) {
        List<PhotoSize> photos = update.getMessage().getPhoto();
        String photoId = photos
                .stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)
                .getFileId();
        int photoWidth = photos
                .stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)
                .getWidth();
        int photoHeight = photos
                .stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)
                .getHeight();
        return new ReportPhoto(photoId, photoWidth, photoHeight);
    }

    private void setVolunteerActivity(Long chatId, boolean active) {
        if (volunteerRepository.findByChatId(chatId) == null) {
            Volunteer volunteer = new Volunteer();
            volunteer.setChatId(chatId);
            volunteer.setActive(true);
            volunteerRepository.save(volunteer);
        } else if (active) {
            Volunteer volunteer = volunteerRepository.findByChatId(chatId);
            volunteer.setActive(true);
            volunteerRepository.save(volunteer);
        } else {
            Volunteer volunteer = volunteerRepository.findByChatId(chatId);
            volunteer.setActive(false);
            volunteerRepository.save(volunteer);
        }
    }

    private void sendHideKeyboard(Long chatId, Integer messageId, String replyText) throws TelegramApiException {
        logger.info("Скрываем меню");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyToMessageId(messageId);
        sendMessage.setText(replyText);
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove(true, true);
        sendMessage.setReplyMarkup(replyKeyboardRemove);
        execute(sendMessage);
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