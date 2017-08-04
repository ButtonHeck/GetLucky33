package com.buttonHeck.getLucky33.handler;

import com.buttonHeck.getLucky33.Game;
import com.buttonHeck.getLucky33.card.PlainCard;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.buttonHeck.getLucky33.Game.GAME_HEIGHT;
import static com.buttonHeck.getLucky33.Game.GAME_WIDTH;
import static com.buttonHeck.getLucky33.HelperMethods.*;

public abstract class ButtonHandler {

    private static Button menuStart, menuExit,
            gameStart, gameTurn, gamePass, gameExit, resultFrame;

    static {
        initializeMenuButtons();
        initializeGameButtons();
        initializeResultFrame();
    }

    private static void initializeMenuButtons() {
        menuStart = new Button(GAME_WIDTH / 4, 100, "Go", true);
        menuExit = new Button(GAME_WIDTH / 1.8, 100, "Exit", true);
    }

    private static void initializeGameButtons() {
        gameStart = new Button(20, GAME_HEIGHT - 120, "Start", true);
        gameTurn = new Button(xOf(gameStart) + widthOf(gameStart) + 72, GAME_HEIGHT - 120, "Turn", false);
        gamePass = new Button(xOf(gameTurn) + widthOf(gameTurn) + 72, GAME_HEIGHT - 120, "Pass", false);
        gameExit = new Button(xOf(gamePass) + widthOf(gamePass) + 72, GAME_HEIGHT - 120, "Exit", true);
    }

    private static void initializeResultFrame() {
        resultFrame = new Button(GAME_WIDTH / 2 - ImageHandler.getResultFrameImage(0).getWidth() / 2,
                GAME_HEIGHT / 2 - ImageHandler.getResultFrameImage(0).getHeight() / 2,
                "XX : XX", true);
        resultFrame.body.setImage(ImageHandler.getResultFrameImage(0));
        resultFrame.setOnMouseClicked(e -> {
            AudioHandler.buttonClicked();
            Game.setupNextRound();
            gameStart.setActive(true);
        });
    }

    public static void disableButtons() {
        gameTurn.setActive(false);
        gamePass.setActive(false);
    }

    public static class Button extends Group {
        private static final Font FONT = new Font(42);
        private static final GaussianBlur TEXT_BLUR = new GaussianBlur(1);
        private static final DropShadow SHADOW = new DropShadow(8, Color.BLACK);
        private ImageView body;
        private Text text;
        private boolean active;

        Button(double x, double y, String txt, boolean active) {
            body = new ImageView(ImageHandler.getButtonImage(this.active ? 0 : 2));
            text = new Text(txt);
            text.setFont(FONT);
            text.setEffect(TEXT_BLUR);
            setXY(this, x, y);
            setX(text, xOf(body) + halfWidthOf(body) - halfWidthOf(text));
            setY(text, yOf(body) + halfHeightOf(body) + halfHeightOf(text) - 10);
            setActive(active);
            getChildren().addAll(body, text);
            setEffect(SHADOW);
        }

        void setActive(boolean active) {
            this.active = active;
            renewActionListeners();
            body.setImage(ImageHandler.getButtonImage(active ? 0 : 2));
        }

        public void setText(String newText) {
            text.setText(newText);
        }

        private void renewActionListeners() {
            renewMouseHoverListeners();
            setOnMouseClicked(e -> {
                if (!active)
                    return;
                AudioHandler.buttonClicked();
                if (this == gameStart)
                    initializeGameStartButtonEvent();
                else if (this == gameTurn)
                    initializeGameTurnEvent();
                else if (this == gamePass)
                    initializeGamePassEvent();
                else if (this == gameExit)
                    initializeGameExitEvent();
                else if (this == menuStart)
                    initializeMenuStartEvent();
                else if (this == menuExit)
                    initializeMenuExitEvent();
            });
        }

        private void renewMouseHoverListeners() {
            setOnMouseEntered(e -> {
                if (!active)
                    return;
                AudioHandler.buttonHovered();
                body.setImage(ImageHandler.getButtonImage(1));
            });
            setOnMouseExited(e -> {
                if (!active)
                    return;
                body.setImage(ImageHandler.getButtonImage(0));
            });
        }

        private void initializeGameStartButtonEvent() {
            gameStart.setActive(false);
            gameTurn.setActive(true);
            gamePass.setActive(true);
            Game.initBonusCards();
            Game.activateBonusCards();
            Game.addPlainCardPlayer(PlainCard.getRandomPlainCard());
            Game.waitForAI();
        }

        private static void initializeGameTurnEvent() {
            Game.activateBonusCards();
            Game.checkScore();
            if (Game.isRoundEnded())
                return;
            Game.addPlainCardPlayer(PlainCard.getRandomPlainCard());
            Game.waitForAI();
        }

        private void initializeGamePassEvent() {
            gamePass.setActive(false);
            gameTurn.setActive(false);
            Game.setPlayerPassed();
            Game.updatePlayerInfo();
            Game.checkScore();
            Game.waitForAI();
        }

        private static void initializeGameExitEvent() {
            Game.toMenu();
            Game.resetGame();
        }

        private void initializeMenuStartEvent() {
            gameStart.setActive(true);
            gameTurn.setActive(false);
            gamePass.setActive(false);
            Game.startGame();
        }
    }

    private static void initializeMenuExitEvent() {
        AudioHandler.finish();
        System.exit(0);
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

    public static Button getResultFrameWithStatus(int status) {
        setX(resultFrame.text, halfWidthOf(resultFrame) - halfWidthOf(resultFrame.text));
        resultFrame.setOnMouseEntered(e -> {
            AudioHandler.buttonHovered();
            resultFrame.body.setImage(ImageHandler.getResultFrameImage(status < 0 ? 1 : (status > 0 ? 2 : 3)));
        });
        resultFrame.setOnMouseExited(e -> resultFrame.body.setImage(ImageHandler.getResultFrameImage(0)));
        return resultFrame;
    }
}