package com.buttonHeck.getLucky33.cards;

import com.buttonHeck.getLucky33.controllers.ImageController;
import javafx.scene.image.Image;

import java.util.Random;

public class PlainCard extends Card {

    private static PlainCard cards[];
    private static Random random;

    static {
        random = new Random();
        cards = new PlainCard[20];
        for (int i = 0; i < 10; i++)
            cards[i] = new PlainCard(ImageController.getPlainCardImage(i + 1), i + 1);
        for (int i = 0; i < 5; i++) {
            cards[i + 10] = new PlainCard(ImageController.getBonusCardImage(-1 - i, true), -1 - i);
            cards[i + 15] = new PlainCard(ImageController.getBonusCardImage(i + 1, true), i + 1);
        }
    }

    private PlainCard(Image image, int value) {
        super(image, value);
    }

    public static PlainCard getRandomCard() {
        return cards[random.nextInt(10)].copy();
    }

    public static Card getPlainCardByNominal(int nominal) {
        if (nominal >= 1 && nominal <= 5) {
            return cards[14 + nominal].copy();
        } else {
            return cards[9 + Math.abs(nominal)].copy();
        }
    }

    private PlainCard copy() {
        return new PlainCard(this.getImage(), nominal);
    }
}
