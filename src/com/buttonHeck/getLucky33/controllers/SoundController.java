package com.buttonHeck.getLucky33.controllers;

import org.lwjgl.openal.AL;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundController {
    private static Sound bonusCard, buttonClicked, buttonHovered, result, startButton;

    static {
        try {
            bonusCard = new Sound(SoundController.class.getResource("/audio/bonusCard.ogg"));
            buttonClicked = new Sound(SoundController.class.getResource("/audio/buttonClicked.ogg"));
            buttonHovered = new Sound(SoundController.class.getResource("/audio/buttonHovered.ogg"));
            result = new Sound(SoundController.class.getResource("/audio/result.ogg"));
            startButton = new Sound(SoundController.class.getResource("/audio/startButton.ogg"));
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public static void bonusCard() {
        /*bonusCard.play();*/
    }

    public static void buttonClicked() {
        /*buttonClicked.play();*/
    }

    public static void buttonHovered() {
        /*buttonHovered.play((float) (Math.random() * 0.2 + 0.9), 1.0f);*/
    }

    public static void result() {
        /*result.play();*/
    }

    public static void startButton() {
        /*startButton.play();*/
    }

    public static void finish() {
        AL.destroy();
    }
}
