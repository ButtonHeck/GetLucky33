package com.buttonHeck.getLucky33.handler;

import org.lwjgl.openal.AL;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public abstract class AudioHandler {
    private static Sound bonusCard, buttonClicked, buttonHovered, resultSound, startSound;

    static {
        try {
            bonusCard = new Sound(AudioHandler.class.getResource("/audio/bonusCard.ogg"));
            buttonClicked = new Sound(AudioHandler.class.getResource("/audio/buttonClicked.ogg"));
            buttonHovered = new Sound(AudioHandler.class.getResource("/audio/buttonHovered.ogg"));
            resultSound = new Sound(AudioHandler.class.getResource("/audio/result.ogg"));
            startSound = new Sound(AudioHandler.class.getResource("/audio/startButton.ogg"));
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
