package com.buttonHeck.getLucky33.cards;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Card extends ImageView{

    protected int nominal;

    protected Card(Image image, int nominal) {
        super(image);
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }
}
