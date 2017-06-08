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

public class ButtonController {

    private static Button menuStartButton, menuExitButton,
            gameStartButton, gameTurnButton, gamePassButton, gameExitButton,
            resultFrame;

    static {
        menuStartButton = new Button(GAME_WIDTH / 4, 100, "Go", true);
        menuExitButton = new Button(GAME_WIDTH / 1.8, 100, "Exit", true);

        gameStartButton = new Button(20, GAME_HEIGHT - 120, "Start", true);
        gameTurnButton = new Button(xOf(gameStartButton) + widthOf(gameStartButton) + 72, GAME_HEIGHT - 120, "Turn", false);
        gamePassButton = new Button(xOf(gameTurnButton) + widthOf(gameTurnButton) + 72, GAME_HEIGHT - 120, "Pass", false);
        gameExitButton = new Button(xOf(gamePassButton) + widthOf(gamePassButton) + 72, GAME_HEIGHT - 120, "Exit", true);

        resultFrame = new Button(GAME_WIDTH / 2 - ImageController.getResultFrameImage(0).getWidth() / 2, GAME_HEIGHT / 2, "", true);
    }

    public static class Button extends Group {
        private static Font FONT = new Font(42);
        private static GaussianBlur textBlur = new GaussianBlur(1);
        private ImageView image;
        private Text text;
        private boolean active;

        Button(double x, double y, String txt, boolean active) {
            image = new ImageView(ImageController.getButtonImage(this.active ? 0 : 2));
            text = new Text(txt);
            text.setFont(FONT);
            text.setEffect(textBlur);
            setX(this, x);
            setY(this, y);
            setX(text, xOf(image) + halfWidthOf(image) - halfWidthOf(text));
            setY(text, yOf(image) + halfHeightOf(image) + halfHeightOf(text) - 10);
            setActive(active);
            getChildren().addAll(image, text);
        }

        void setActive(boolean active) {
            this.active = active;
            renewActionListeners();
            image.setImage(ImageController.getButtonImage(active ? 0 : 2));
        }

        private void renewActionListeners() {
            setOnMouseEntered(e -> {
                if (!active)
                    return;
                image.setImage(ImageController.getButtonImage(1));
                SoundController.buttonHovered();
            });
            setOnMouseExited(e -> {
                if (!active)
                    return;
                image.setImage(ImageController.getButtonImage(0));
            });
            setOnMouseClicked(e -> {
                if (!active)
                    return;
                SoundController.buttonClicked();
                if (this == gameStartButton) {
                    gameStartButton.setActive(false);
                    gameTurnButton.setActive(true);
                    gamePassButton.setActive(true);
                    if (!Game.isGameStarted()) {
                        Game.initBonusCards();
                        Game.addPlainCards();
                    }
                } else if (this == gameTurnButton) {
                    Game.setPlayerBonusCardsActive(true);
                    Game.setAiBonusCardsActive(true);
                    Game.checkScore();
                    if (!Game.isPlayerPassed())
                        Game.addPlainCardPlayer(PlainCard.getRandomCard());
                    if (!Game.isAiPassed())
                        Game.addPlainCardAI();
                } else if (this == gamePassButton) {
                    gamePassButton.setActive(false);
                    gameTurnButton.setActive(false);
                    Game.setPlayerPassed(true);
                    Game.updatePlayerInfo();
                    Game.checkScore();
                } else if (this == gameExitButton) {
                    Game.toMenu();
                    Game.resetObjects();
                } else if (this == menuStartButton) {
                    gameStartButton.setActive(true);
                    gameTurnButton.setActive(false);
                    gamePassButton.setActive(false);
                    Game.startGame();
                } else if (this == menuExitButton) {
                    SoundController.finish();
                    System.exit(0);
                }
            });
        }
    }

    public static Button getMenuStartButton() {
        return menuStartButton;
    }

    public static Button getMenuExitButton() {
        return menuExitButton;
    }

    public static Button getGameStartButton() {
        return gameStartButton;
    }

    public static Button getGameTurnButton() {
        return gameTurnButton;
    }

    public static Button getGamePassButton() {
        return gamePassButton;
    }

    public static Button getGameExitButton() {
        return gameExitButton;
    }

    public static Button getResultFrame(int status) {
        resultFrame.setOnMouseEntered(e -> {
            SoundController.buttonHovered();
            resultFrame.image.setImage(ImageController.getResultFrameImage(status < 0 ? 1 : (status > 0 ? 2 : 3)));
        });
        resultFrame.setOnMouseExited(e -> resultFrame.image.setImage(ImageController.getResultFrameImage(0)));
        resultFrame.setOnMouseClicked(e -> {
            SoundController.buttonClicked();
            Game.nextRound();
        });
        return resultFrame;
    }
}
