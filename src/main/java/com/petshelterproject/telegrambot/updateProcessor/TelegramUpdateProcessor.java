package com.petshelterproject.telegrambot.updateProcessor;

import com.petshelterproject.model.User;
import com.petshelterproject.repository.UserRepository;
import com.petshelterproject.telegrambot.messageProcessor.UserMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TelegramUpdateProcessor {

    @Autowired
    private UserRepository userRepository;
    private final UserMessageProcessor messageProcessor;

    public TelegramUpdateProcessor(UserMessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    public SendMessage revert(Long chatId) {
        SendMessage revert = new SendMessage();
        boolean isInCatShelter = userRepository.findByChatId(chatId).isInCatShelter();
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
            case ("Прислан отчет"): {
                if (userRepository.findByChatId(chatId).isInCatShelter()) {
                    revert = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC31 Кошку");
                } else {
                    revert = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC36 Собаку");
                }
                break;
            }
            case ("Одобрить"): {
                revert = enterData(chatId, "Поздравляем, теперь вы являетесь полноправным хозяином вашего нового питомца", "Вернуться в меню");
                break;
            }
            case ("Отклонить"): {
                revert = enterData(chatId, "К сожалению, вашу кандидатуру хозяина отклонили, но это не повод отчаиваться." +
                        " Через время, которое вам укажут наши волонтеры, вы сможете снова попробовать себя в роли хозяина питомца." +
                        "В следующий раз внимательнее отнеситесь к заполнению отчетов и конечно же больше уделяйте внимания своему маленькому другу", "Вернуться в меню");
                break;
            }
            case ("Продлить испытательный срок на 14 дней"): {
                revert = enterData(chatId, "Волонтеры приняли решение продлить ваш испытательный срок на две недели", "Вернуться в меню");
                break;
            }
            case ("Продлить испытательный срок на месяц"): {
                revert = enterData(chatId, "Волонтеры приняли решение продлить ваш испытательный срок на месяц", "Вернуться в меню");
                break;
            }
            default:
        }
        return revert;
    }

    public SendMessage back(Long chatId) {
        SendMessage back = new SendMessage();
        boolean isInCatShelter = userRepository.findByChatId(chatId).isInCatShelter();
        String lastMessage = userRepository.findByChatId(chatId).getLastMessage();
        back.setChatId(chatId);
        switch (lastMessage) {
            case("❓ Как забрать животное из приюта"),
                    ("ℹ\uFE0F Узнать информацию о приюте"),
                    ("🐾 Прислать отчет о питомце"),
                    ("Прислан отчет"),
                    ("Не закреплен питомец"),
                    ("Введено имя"),
                    ("\uD83D\uDCDD Внести контактные данные для связи"): {
                if(isInCatShelter) {
                    back = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC31 Кошку");
                    userStatusUpdate(chatId, true, "\uD83D\uDC31 Кошку");
                }
                else {
                    back = messageProcessor.secondStageMenu(chatId, "\uD83D\uDC36 Собаку");
                    userStatusUpdate(chatId, false, "\uD83D\uDC36 Собаку");
                }
                break;
            }
            default:
        }
        return back;
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
