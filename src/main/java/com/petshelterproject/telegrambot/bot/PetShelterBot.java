package com.petshelterproject.telegrambot.bot;

import com.petshelterproject.model.User;
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
                            execute(updateProcessor.revert(userRepository.findByChatId(chatId)));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                }
                case ("/end"): {
                    try {
                        sendHideKeyboard(chatId, messageId, "До скорой встречи!");
                    } catch (TelegramApiException e) {
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
                default:
            }

        }
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