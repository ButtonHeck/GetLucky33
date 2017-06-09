package com.buttonHeck.getLucky33.controllers;

import org.lwjgl.openal.AL;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundController {
    private static Sound bonusCard, buttonClicked, buttonHovered, resultSound, startSound;

    static {
        try {
            bonusCard = new Sound(SoundController.class.getResource("/audio/bonusCard.ogg"));
            buttonClicked = new Sound(SoundController.class.getResource("/audio/buttonClicked.ogg"));
            buttonHovered = new Sound(SoundController.class.getResource("/audio/buttonHovered.ogg"));
            resultSound = new Sound(SoundController.class.getResource("/audio/result.ogg"));
            startSound = new Sound(SoundController.class.getResource("/audio/startButton.ogg"));
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public static void bonusCard() {
        bonusCard.play();
    }

    static void buttonClicked() {
        buttonClicked.play((float) (Math.random() * 0.2 + 0.9), 1.0f);
    }

    static void buttonHovered() {
        buttonHovered.play((float) (Math.random() * 0.2 + 0.9), 1.0f);
    }

    public static void result() {
        resultSound.play();
    }

    public static void startSound() {
        startSound.play();
    }

    public static void finish() {
        AL.destroy();
    }
}
