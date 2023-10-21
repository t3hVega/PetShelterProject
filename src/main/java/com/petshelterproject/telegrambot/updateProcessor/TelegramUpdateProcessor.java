package com.petshelterproject.telegrambot.updateProcessor;

import com.petshelterproject.model.User;
import com.petshelterproject.repository.UserRepository;
import com.petshelterproject.telegrambot.messageProcessor.TelegramMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class TelegramUpdateProcessor {

    @Autowired
    private UserRepository userRepository;
    private final TelegramMessageProcessor messageProcessor;

    public TelegramUpdateProcessor(TelegramMessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    public SendMessage revert(User user) {
        SendMessage revert = new SendMessage();
        Long chatId = user.getChatId();
        boolean isInCatShelter = user.getIsInCatShelter();
        String lastMessage = user.getLastMessage();
        revert.setChatId(chatId);
        switch (lastMessage) {
            case ("ℹ\uFE0F Узнать информацию о приюте"): {
                revert =  messageProcessor.shelterInfoMenu(chatId);
                break;
            }
            case("❓ Как забрать животное из приюта"): {
                if(isInCatShelter == true) revert = messageProcessor.catAdoptionAssistMenu(chatId);
                else revert = messageProcessor.dogAdoptionAssistMenu(chatId);
            }
            default:
        }
        return revert;
    }
}
