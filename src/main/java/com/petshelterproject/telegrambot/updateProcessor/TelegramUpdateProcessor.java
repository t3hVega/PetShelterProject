package com.petshelterproject.telegrambot.updateProcessor;

import com.petshelterproject.model.User;
import com.petshelterproject.repository.UserRepository;
import com.petshelterproject.telegrambot.messageProcessor.TelegramMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramUpdateProcessor {

    @Autowired
    private UserRepository userRepository;
    private final TelegramMessageProcessor messageProcessor;

    public TelegramUpdateProcessor(TelegramMessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    public SendMessage revert(Long chatId) {
        SendMessage revert = new SendMessage();
        boolean isInCatShelter = userRepository.findByChatId(chatId).getIsInCatShelter();
        String lastMessage = userRepository.findByChatId(chatId).getLastMessage();
        revert.setChatId(chatId);
        switch (lastMessage) {
            case ("\uD83D\uDC31 Кошку"): {
                revert = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC31 Кошку");
                break;
            }
            case ("\uD83D\uDC36 Собаку"): {
                revert = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC36 Собаку");
                break;
            }
            case ("ℹ\uFE0F Узнать информацию о приюте"): {
                revert =  messageProcessor.shelterInfoMenu(chatId);
                break;
            }
            case("❓ Как забрать животное из приюта"): {
                if(isInCatShelter) revert = messageProcessor.catAdoptionAssistMenu(chatId);
                else revert = messageProcessor.dogAdoptionAssistMenu(chatId);
                break;
            }
            case("Введены данные"): {
                revert =  messageProcessor.shelterInfoMenu(chatId);
                userStatusUpdate(chatId, isInCatShelter,"ℹ\uFE0F Узнать информацию о приюте");
                break;
            }
            case("\uD83D\uDC3E Прислать отчет о питомце"): {
                revert = messageProcessor.reportMenu(chatId);
                break;
            }
            default:
        }
        return revert;
    }

    public SendMessage back(Long chatId) {
        SendMessage back = new SendMessage();
        boolean isInCatShelter = userRepository.findByChatId(chatId).getIsInCatShelter();
        String lastMessage = userRepository.findByChatId(chatId).getLastMessage();
        back.setChatId(chatId);
        switch (lastMessage) {
            case("❓ Как забрать животное из приюта"): {
                if(isInCatShelter) {
                    back = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC31 Кошку");
                    userStatusUpdate(chatId, isInCatShelter, "\uD83D\uDC31 Кошку");
                }
                else {
                    back = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC36 Собаку");
                    userStatusUpdate(chatId, isInCatShelter, "\uD83D\uDC36 Собаку");
                }
            }
            case("ℹ\uFE0F Узнать информацию о приюте"): {
                if(isInCatShelter) {
                    back = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC31 Кошку");
                    userStatusUpdate(chatId, isInCatShelter, "\uD83D\uDC31 Кошку");
                }
                else {
                    back = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC36 Собаку");
                    userStatusUpdate(chatId, isInCatShelter, "\uD83D\uDC36 Собаку");
                }
            }
            case("\uD83D\uDC3E Прислать отчет о питомце"): {
                if(isInCatShelter) {
                    back = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC31 Кошку");
                    userStatusUpdate(chatId, isInCatShelter, "\uD83D\uDC31 Кошку");
                }
                else {
                    back = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC36 Собаку");
                    userStatusUpdate(chatId, isInCatShelter, "\uD83D\uDC36 Собаку");
                }
                break;
            }
            default:
        }
        return back;
    }

    private void userStatusUpdate (Long chatId, boolean isInCatShelter, String lastMessage) {
        User user = new User(chatId, isInCatShelter, lastMessage);
        userRepository.save(user);
    }

}
