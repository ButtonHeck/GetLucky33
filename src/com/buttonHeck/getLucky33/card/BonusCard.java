package com.buttonHeck.getLucky33.card;

import com.buttonHeck.getLucky33.Game;
import com.buttonHeck.getLucky33.handler.ImageHandler;
import javafx.scene.image.Image;

import java.util.Random;

public class BonusCard extends Card {

    private static BonusCard[] playerCards = new BonusCard[10];
    private static BonusCard[] aiCards = new BonusCard[10];
    private static Random rnd = new Random();

    static {
        for (int i = -5; i < 0; i++) {
            playerCards[i + 5] = new BonusCard(ImageHandler.getBonusCardImage(i, true), i);
            aiCards[i + 5] = new BonusCard(ImageHandler.getAiBonusCardImage(), i);
        }
        for (int i = 1; i < 6; i++) {
            playerCards[i + 4] = new BonusCard(ImageHandler.getBonusCardImage(i, true), i);
            aiCards[i + 4] = new BonusCard(ImageHandler.getAiBonusCardImage(), i);
        }
    }

    private BonusCard(Image image, int value) {
        super(image, value);
    }

    public void setActive(boolean active) {
        setImage(ImageHandler.getBonusCardImage(nominal, active));
        setOnMouseClicked(active ? e -> Game.handleBonus(this) : null);
    }

    public static BonusCard getRandomPlayerBonusCard() {
        return playerCards[rnd.nextInt(playerCards.length)];
    }

    public static BonusCard getRandomAiBonusCard() {
        return aiCards[rnd.nextInt(aiCards.length)];
    }
}
