package com.petshelterproject.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.petshelterproject.telegrambot.sender.TelegramMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private TelegramBot telegramBot;
    private TelegramMessageSender telegramMessageSender;

    public TelegramBotUpdatesListener (
            TelegramBot telegramBot,
            TelegramMessageSender telegramMessageSender
    ) {
        this.telegramBot = telegramBot;
        this.telegramMessageSender = telegramMessageSender;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Message message = update.message();
            String text = update.message().text();
            long chatId = update.message().chat().id();
            switch (text) {
                case "/placeholder1":
                    telegramMessageSender.send(chatId, "Заглушка 1");
                    break;
                case "/placeholder2":
                    telegramMessageSender.send(chatId, "Заглушка 2");
                    break;
                default:

            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
