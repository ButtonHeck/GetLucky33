package com.buttonHeck.getLucky33.card;

import com.buttonHeck.getLucky33.handler.ImageHandler;
import javafx.scene.image.Image;

import java.util.Random;

public class PlainCard extends Card {

    private static PlainCard cards[];
    private static Random rnd;

    static {
        rnd = new Random();
        cards = new PlainCard[20];
        for (int i = 0; i < 10; i++)
            cards[i] = new PlainCard(ImageHandler.getPlainCardImage(i + 1), i + 1);
        for (int i = 0; i < 5; i++) {
            cards[i + 10] = new PlainCard(ImageHandler.getBonusCardImage(-1 - i, true), -1 - i);
            cards[i + 15] = new PlainCard(ImageHandler.getBonusCardImage(i + 1, true), i + 1);
        }
    }

    private PlainCard(Image image, int value) {
        super(image, value);
    }

    public static PlainCard getRandomPlainCard() {
        return cards[rnd.nextInt(10)].copy();
    }

    public static Card getPlainCardByNominal(int nominal) {
        return nominal >= 1 && nominal <= 5 ?
                cards[14 + nominal].copy() :
                cards[9 + Math.abs(nominal)].copy();
    }

    private PlainCard copy() {
        return new PlainCard(this.getImage(), nominal);
    }
}
