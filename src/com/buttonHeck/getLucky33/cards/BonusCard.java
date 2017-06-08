package com.buttonHeck.getLucky33.cards;

import com.buttonHeck.getLucky33.Game;
import com.buttonHeck.getLucky33.controllers.ImageController;
import javafx.scene.image.Image;

import java.util.Random;

public class BonusCard extends Card {

    private static BonusCard[] playerCards = new BonusCard[10];
    private static BonusCard[] aiCards = new BonusCard[10];
    private static Random random = new Random();

    static {
        for (int i = -5; i < 0; i++) {
            playerCards[i + 5] = new BonusCard(ImageController.getBonusCardImage(i, true), i, true);
            aiCards[i + 5] = new BonusCard(ImageController.getAiBonusCardImage(), i, true);
        }
        for (int i = 1; i < 6; i++) {
            playerCards[i + 4] = new BonusCard(ImageController.getBonusCardImage(i, true), i, true);
            aiCards[i + 4] = new BonusCard(ImageController.getAiBonusCardImage(), i, true);
        }
    }

    private boolean active;

    private BonusCard(Image image, int value, boolean active) {
        super(image, value);
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        setImage(ImageController.getBonusCardImage(nominal, active));
        if (active) {
            setOnMouseClicked(e -> {
                Game.bonusEvent(this);
            });
        } else {
            setOnMouseClicked(e -> {/*nop*/});
        }
    }

    public static BonusCard getRandomPlayerBonusCard() {
        return playerCards[random.nextInt(playerCards.length)];
    }

    public static BonusCard getRandomAiBonusCard() {
        return aiCards[random.nextInt(aiCards.length)];
    }

    public static void resetCards() {
        for (BonusCard playerCard : playerCards) {
            playerCard.setActive(true);
        }
    }
}
