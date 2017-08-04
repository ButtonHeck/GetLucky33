package com.buttonHeck.getLucky33.handler;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.buttonHeck.getLucky33.Game.GAME_HEIGHT;
import static com.buttonHeck.getLucky33.Game.GAME_WIDTH;
import static com.buttonHeck.getLucky33.HelperMethods.*;

public abstract class TextHandler {

    private static final DropShadow MENU_TEXT_FX = new DropShadow(2, 2, 2, Color.BLACK);
    private static final GaussianBlur GAME_TEXT_FX = new GaussianBlur(1);
    private static final Font FONT = new Font(32), SCORE_FONT = new Font(24);

    private static final Text[] menuHints = new Text[]{
            new Text("Get as close to 33 as you can,"),
            new Text("but do not exceed this score."),
            new Text("You may use bonus card to change your score,"),
            new Text("these card are generated randomly from game to game.")
    };
    private static final Text author = new Text("created by ButtonHeck, 2017");
    private static Text playerScoreText, aiScoreText;

    static {
        setupMenuHints();
        setupAuthorHint();
        setupScoreHints();
    }

    private static void setupMenuHints() {
        for (Text hint : menuHints) {
            hint.setFont(FONT);
            hint.setEffect(MENU_TEXT_FX);
            hint.setFill(Color.FUCHSIA);
        }
        for (int i = 0; i < 4; i++)
            setXY(menuHints[i], GAME_WIDTH / 2 - halfWidthOf(menuHints[i]), GAME_HEIGHT / 2 + i * 40);
    }

    private static void setupAuthorHint() {
        author.setFont(Font.font(24));
        author.setFill(Color.PINK);
        author.setEffect(MENU_TEXT_FX);
        setXY(author, GAME_WIDTH / 1.6, GAME_HEIGHT - 30);
    }

    private static void setupScoreHints() {
        playerScoreText = new Text("Your score: 0");
        playerScoreText.setFont(SCORE_FONT);
        playerScoreText.setEffect(GAME_TEXT_FX);
        aiScoreText = new Text("Opponent score: 0");
        aiScoreText.setFont(SCORE_FONT);
        aiScoreText.setEffect(GAME_TEXT_FX);
        setXY(playerScoreText, 30, 35);
        setXY(aiScoreText, 580, 35);
    }

    public static Text[] getMenuHints() {
        return menuHints;
    }

    public static Text getAuthor() {
        return author;
    }

    public static Text getPlayerScoreText() {
        return playerScoreText;
    }

    public static Text getAiScoreText() {
        return aiScoreText;
    }

    public static void setPlayerScoreText(String newText) {
        playerScoreText.setText(newText);
    }

    public static void setAiScoreText(String newText) {
        aiScoreText.setText(newText);
    }
}