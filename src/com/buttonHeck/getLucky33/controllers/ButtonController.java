package com.buttonHeck.getLucky33.controllers;

import com.buttonHeck.getLucky33.Game;
import com.buttonHeck.getLucky33.cards.PlainCard;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.buttonHeck.getLucky33.Game.GAME_HEIGHT;
import static com.buttonHeck.getLucky33.Game.GAME_WIDTH;
import static com.buttonHeck.getLucky33.HelperMethods.*;

public abstract class ButtonController {

    private static Button menuStart, menuExit,
            gameStart, gameTurn, gamePass, gameExit, resultFrame;

    static {
        menuStart = new Button(GAME_WIDTH / 4, 100, "Go", true);
        menuExit = new Button(GAME_WIDTH / 1.8, 100, "Exit", true);

        gameStart = new Button(20, GAME_HEIGHT - 120, "Start", true);
        gameTurn = new Button(xOf(gameStart) + widthOf(gameStart) + 72, GAME_HEIGHT - 120, "Turn", false);
        gamePass = new Button(xOf(gameTurn) + widthOf(gameTurn) + 72, GAME_HEIGHT - 120, "Pass", false);
        gameExit = new Button(xOf(gamePass) + widthOf(gamePass) + 72, GAME_HEIGHT - 120, "Exit", true);

        resultFrame = new Button(GAME_WIDTH / 2 - ImageController.getResultFrameImage(0).getWidth() / 2,
                GAME_HEIGHT / 2 - ImageController.getResultFrameImage(0).getHeight() / 2, "", true);
        resultFrame.body.setImage(ImageController.getResultFrameImage(0));
        resultFrame.setOnMouseClicked(e -> {
            SoundController.buttonClicked();
            Game.setupNextRound();
            gameStart.setActive(true);
        });
    }

    public static class Button extends Group {
        private static Font FONT = new Font(42);
        private static GaussianBlur textBlur = new GaussianBlur(1);
        private ImageView body;
        private Text text;
        private boolean active;

        Button(double x, double y, String txt, boolean active) {
            body = new ImageView(ImageController.getButtonImage(this.active ? 0 : 2));
            text = new Text(txt);
            text.setFont(FONT);
            text.setEffect(textBlur);
            setX(this, x);
            setY(this, y);
            setX(text, xOf(body) + halfWidthOf(body) - halfWidthOf(text));
            setY(text, yOf(body) + halfHeightOf(body) + halfHeightOf(text) - 10);
            setActive(active);
            getChildren().addAll(body, text);
        }

        void setActive(boolean active) {
            this.active = active;
            renewActionListeners();
            body.setImage(ImageController.getButtonImage(active ? 0 : 2));
        }

        private void renewActionListeners() {
            setOnMouseEntered(e -> {
                if (!active)
                    return;
                SoundController.buttonHovered();
                body.setImage(ImageController.getButtonImage(1));
            });
            setOnMouseExited(e -> {
                if (!active)
                    return;
                body.setImage(ImageController.getButtonImage(0));
            });
            setOnMouseClicked(e -> {
                if (!active)
                    return;
                SoundController.buttonClicked();
                if (this == gameStart) {
                    gameStart.setActive(false);
                    gameTurn.setActive(true);
                    gamePass.setActive(true);
                    if (!Game.isInProgress())
                        Game.initBonusCards();
                    Game.setPlayerBonusCardsActive(true);
                    Game.activateAiBonusCards();
                    Game.addPlainCardPlayer(PlainCard.getRandomPlainCard());
                    Game.waitForAI();
                } else if (this == gameTurn) {
                    Game.setPlayerBonusCardsActive(true);
                    Game.activateAiBonusCards();
                    Game.checkScore();
                    if (Game.isRoundEnded())
                        return;
                    if (!Game.isPlayerPassed())
                        Game.addPlainCardPlayer(PlainCard.getRandomPlainCard());
                    if (!Game.isAiPassed())
                        Game.waitForAI();
                } else if (this == gamePass) {
                    gamePass.setActive(false);
                    gameTurn.setActive(false);
                    Game.setPlayerPassed();
                    Game.updatePlayerInfo();
                    Game.checkScore();
                    if (!Game.isAiPassed())
                        Game.waitForAI();
                } else if (this == gameExit) {
                    Game.toMenu();
                    Game.resetGame();
                } else if (this == menuStart) {
                    gameStart.setActive(true);
                    gameTurn.setActive(false);
                    gamePass.setActive(false);
                    Game.startGame();
                } else if (this == menuExit) {
                    SoundController.finish();
                    System.exit(0);
                }
            });
        }
    }

    public static Button getMenuStart() {
        return menuStart;
    }

    public static Button getMenuExit() {
        return menuExit;
    }

    public static Button getGameStart() {
        return gameStart;
    }

    public static Button getGameTurn() {
        return gameTurn;
    }

    public static Button getGamePass() {
        return gamePass;
    }

    public static Button getGameExit() {
        return gameExit;
    }

    public static Button getResultFrame(int status) {
        resultFrame.text.setText(status == 0 ? "Draw!" : (Game.getPlayerScore() + " : " + Game.getAiScore()));
        setX(resultFrame.text, halfWidthOf(resultFrame) - halfWidthOf(resultFrame.text));
        resultFrame.setOnMouseEntered(e -> {
            SoundController.buttonHovered();
            resultFrame.body.setImage(ImageController.getResultFrameImage(status < 0 ? 1 : (status > 0 ? 2 : 3)));
        });
        resultFrame.setOnMouseExited(e -> resultFrame.body.setImage(ImageController.getResultFrameImage(0)));
        return resultFrame;
    }
}
