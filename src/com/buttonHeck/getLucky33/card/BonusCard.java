package com.buttonHeck.getLucky33.cards;

import com.buttonHeck.getLucky33.Game;
import com.buttonHeck.getLucky33.controllers.ImageController;
import javafx.scene.image.Image;

import java.util.Random;

public class BonusCard extends Card {

    private static BonusCard[] playerCards = new BonusCard[10];
    private static BonusCard[] aiCards = new BonusCard[10];
    private static Random rnd = new Random();

    static {
        for (int i = -5; i < 0; i++) {
            playerCards[i + 5] = new BonusCard(ImageController.getBonusCardImage(i, true), i);
            aiCards[i + 5] = new BonusCard(ImageController.getAiBonusCardImage(), i);
        }
        for (int i = 1; i < 6; i++) {
            playerCards[i + 4] = new BonusCard(ImageController.getBonusCardImage(i, true), i);
            aiCards[i + 4] = new BonusCard(ImageController.getAiBonusCardImage(), i);
        }
    }

    private BonusCard(Image image, int value) {
        super(image, value);
    }

    public void setActive(boolean active) {
        setImage(ImageController.getBonusCardImage(nominal, active));
        if (active)
            setOnMouseClicked(e -> Game.handleBonus(this));
        else
            setOnMouseClicked(e -> {/*nop*/});
    }

    public static BonusCard getRandomPlayerBonusCard() {
        return playerCards[rnd.nextInt(playerCards.length)];
    }

    public static BonusCard getRandomAiBonusCard() {
        return aiCards[rnd.nextInt(aiCards.length)];
    }

    //don't need to setActive for aiCards - we don't interact with it, meanwhile ai uses flags in Game for activation
    public static void resetCards() {
        for (BonusCard c : playerCards)
            c.setActive(true);
    }
}
