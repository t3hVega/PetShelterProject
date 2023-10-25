package com.petshelterproject.telegrambot.bot;

import com.petshelterproject.model.Adopter;
import com.petshelterproject.model.ReportPhoto;
import com.petshelterproject.model.User;
import com.petshelterproject.repository.AdopterRepository;
import com.petshelterproject.repository.ReportPhotoRepository;
import com.petshelterproject.repository.ReportRepository;
import com.petshelterproject.repository.UserRepository;
import com.petshelterproject.telegrambot.configuration.BotConfig;
import com.petshelterproject.telegrambot.messageProcessor.TelegramMessageProcessor;
import com.petshelterproject.telegrambot.updateProcessor.TelegramUpdateProcessor;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
public class PetShelterBot extends TelegramLongPollingBot {

    private final Logger logger = LoggerFactory.getLogger(PetShelterBot.class);
    private BotConfig botConfig;
    private TelegramMessageProcessor messageProcessor;
    private TelegramUpdateProcessor updateProcessor;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdopterRepository adopterRepository;
    @Autowired
    private ReportRepository reportRepository;
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
                        try {
                            execute(messageProcessor.firstStageMenu(chatId));
                        } catch (TelegramApiException e) {
                        }
                    }
                    else {
                        try {
                            execute(updateProcessor.revert(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                }
                case ("/end"), ("❌ Нет"): {
                    try {
                        sendHideKeyboard(chatId, messageId, "До скорой встречи!");
                    } catch (TelegramApiException e) {
                    }
                    break;
                }
                case ("⬅\uFE0F Вернуться в предыдущее меню"), ("❌ Нет, вернуться в предыдущее меню"): {
                    try {
                        execute(updateProcessor.back(chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

                case ("\uD83D\uDC31 Кошку"): {
                    try {
                        execute(messageProcessor.secondStageMenu(chatId, "\uD83D\uDC31 Кошку"));
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
                        execute(messageProcessor.secondStageMenu(chatId, "\uD83D\uDC36 Собаку"));
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
                        execute(messageProcessor.shelterInfoMenu(chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    userStatusUpdate(
                            chatId,
                            userRepository.findByChatId(chatId).getIsInCatShelter(),
                            updateMessageText);
                    break;
                }
                case ("❓ Как забрать животное из приюта"): {
                    if (userRepository.findByChatId(chatId).getIsInCatShelter() == true) {
                        try {
                            execute(messageProcessor.catAdoptionAssistMenu(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else
                        try {
                            execute(messageProcessor.dogAdoptionAssistMenu(chatId));
                        } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                        }
                    userStatusUpdate(
                            chatId,
                            userRepository.findByChatId(chatId).getIsInCatShelter(),
                            updateMessageText);
                    break;
                }
                case ("\uD83D\uDC3E Прислать отчет о питомце"): {
                    if(adopterRepository.findByChatId(chatId) == null) {
                        try {
                            execute(messageProcessor.forcedApplyMenu(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            execute(messageProcessor.reportMenu(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    userStatusUpdate(
                            chatId,
                            userRepository.findByChatId(chatId).getIsInCatShelter(),
                            updateMessageText);
                    break;
                }
                case ("\uD83D\uDCDD Принять и записать контактные данные для связи"): {
                    if(adopterRepository.findByChatId(chatId) != null) {
                        try {
                            execute(messageProcessor.applicationMenuFirstStage(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        try {
                            execute(messageProcessor.adopterAlreadyExistsMenu(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    userStatusUpdate(
                            chatId,
                            userRepository.findByChatId(chatId).getIsInCatShelter(),
                            updateMessageText);
                    break;
                }
                case ("✅ Да, записать мои контактные данные для связи") : {
                    try {
                        execute(messageProcessor.applicationMenuFirstStage(chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    userStatusUpdate(
                            chatId,
                            userRepository.findByChatId(chatId).getIsInCatShelter(),
                            updateMessageText);
                    break;
                }
                case ("✅ Да"): {
                    if (userRepository.findByChatId(chatId).getIsInCatShelter()) {
                        try {
                            execute(messageProcessor.secondStageMenu(chatId, "\uD83D\uDC31 Кошку"));
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
                            execute(messageProcessor.secondStageMenu(chatId, "\uD83D\uDC36 Собаку"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        userStatusUpdate(
                                chatId,
                                true,
                                "\uD83D\uDC36 Собаку");
                        break;
                    }
                }
                default:
            }

            if(userRepository.findByChatId(chatId) != null) {
                switch (userRepository.findByChatId(chatId).getLastMessage()) {
                    case ("✅ Да, записать мои контактные данные для связи"), ("\uD83D\uDCDD Принять и записать контактные данные для связи"): {
                        if (!(updateMessageText.equals(userRepository.findByChatId(chatId).getLastMessage()))) {
                            Adopter adopter = new Adopter();
                            adopter.setChatId(chatId);
                            adopter.setName(updateMessageText);
                            adopterRepository.save(adopter);
                            try {
                                execute(messageProcessor.applicationMenuSecondStage(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(
                                    chatId,
                                    userRepository.findByChatId(chatId).getIsInCatShelter(),
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
                                execute(messageProcessor.applicationSuccessfulMenu(chatId));
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            userStatusUpdate(
                                    chatId,
                                    userRepository.findByChatId(chatId).getIsInCatShelter(),
                                    "Введены данные");
                        }
                        break;
                    }
                }
            }

        }
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            Long chatId = update.getMessage().getChatId();
            Integer messageId = update.getMessage().getMessageId();
            String updateCaptionText = update.getMessage().getCaption();
        }
    }

    private static ReportPhoto getReportPhoto(Update update, int i) {
        PhotoSize photo = update.getMessage().getPhoto().get(i);
        return new ReportPhoto(
                photo.getFileId(),
                photo.getFileUniqueId(),
                photo.getWidth(),
                photo.getHeight(),
                photo.getFileSize(),
                photo.getFilePath()
        );
    }

    private void userStatusUpdate (Long chatId, boolean isInCatShelter, String lastMessage) {
        User user = new User(chatId, isInCatShelter, lastMessage);
        userRepository.save(user);
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

}