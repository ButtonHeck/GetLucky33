package com.buttonHeck.getLucky33;

import com.buttonHeck.getLucky33.cards.BonusCard;
import com.buttonHeck.getLucky33.cards.Card;
import com.buttonHeck.getLucky33.cards.PlainCard;
import com.buttonHeck.getLucky33.controllers.ButtonController;
import com.buttonHeck.getLucky33.controllers.ImageController;
import com.buttonHeck.getLucky33.controllers.SoundController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;

import static com.buttonHeck.getLucky33.HelperMethods.*;

public class Game extends Application {

    private static Timeline delay;
    public static final int GAME_WIDTH = 1020, GAME_HEIGHT = 720;
    public static final int WINNING_SCORE = 33;
    private static Stage stage;
    private static Group menuRoot, gameRoot;
    private static Canvas gameCanvas;
    private static Scene menuScene, gameScene;
    private static final Font FONT = new Font(32), SCORE_FONT = new Font(24);
    private static Text hints[];
    private static ArrayList<BonusCard> playerBonusCards = new ArrayList<>();
    private static ArrayList<Card> playerPlainCards = new ArrayList<>();
    private static ArrayList<BonusCard> aiBonusCards = new ArrayList<>();
    private static ArrayList<Card> aiPlainCards = new ArrayList<>();
    private static double playerCardsXOffset = 0, playerCardsYOffset = 0, aiCardsXOffset = 0, aiCardsYOffset = 0;
    private static int playerScore = 0, aiScore = 0;
    private static int playerWinrate = 0, aiWinrate = 0;
    private static int roundNumber = 0;
    private static Text playerScoreText, aiScoreText;
    private static boolean playerBonusActive = true, aiBonusActive = true;
    private static final GaussianBlur TEXT_BLUR = new GaussianBlur(1);
    private static boolean playerPassed, aiPassed, roundEnded = false, gameStarted = false;
    private static Image score[];
    private static ButtonController.Button resultFrame;

    @Override
    public void start(Stage stage) throws Exception {
        Game.stage = stage;
        createMenuObjects();
        createGameObjects();
        initializeDelayTimeline();
        createWindow();
    }

    private void createMenuObjects() {
        menuRoot = new Group();
        hints = new Text[4];
        hints[0] = new Text("Get as close to 33 as you can,");
        hints[1] = new Text("but do not exceed this score.");
        hints[2] = new Text("You may use bonus cards to change your score,");
        hints[3] = new Text("these cards are generated randomly from game to game.");
        hints[0].setFill(Color.FUCHSIA);
        hints[1].setFill(Color.FUCHSIA);
        hints[2].setFill(Color.HOTPINK);
        hints[3].setFill(Color.HOTPINK);
        hints[0].setFont(FONT);
        hints[1].setFont(FONT);
        hints[2].setFont(FONT);
        hints[3].setFont(FONT);
        hints[0].setEffect(TEXT_BLUR);
        hints[1].setEffect(TEXT_BLUR);
        hints[2].setEffect(TEXT_BLUR);
        hints[3].setEffect(TEXT_BLUR);
        setX(hints[0], GAME_WIDTH / 2 - halfWidthOf(hints[0]));
        setX(hints[1], GAME_WIDTH / 2 - halfWidthOf(hints[1]));
        setX(hints[2], GAME_WIDTH / 2 - halfWidthOf(hints[2]));
        setX(hints[3], GAME_WIDTH / 2 - halfWidthOf(hints[3]));
        setY(hints[0], GAME_HEIGHT / 2);
        setY(hints[1], GAME_HEIGHT / 2 + 40);
        setY(hints[2], GAME_HEIGHT / 2 + 80);
        setY(hints[3], GAME_HEIGHT / 2 + 120);
        menuRoot.getChildren().addAll(hints[0], hints[1], hints[2], hints[3],
                ButtonController.getMenuStartButton(), ButtonController.getMenuExitButton());
    }

    private static void createGameObjects() {
        gameRoot = new Group();
        gameCanvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        gameCanvas.getGraphicsContext2D().drawImage(ImageController.getBackground(), 0, 0);
        score = new Image[5];
        drawScoreBulbs();
        playerScoreText = new Text("Your score: " + playerScore);
        playerScoreText.setFont(SCORE_FONT);
        playerScoreText.setEffect(TEXT_BLUR);
        aiScoreText = new Text("Opponent score: " + aiScore);
        aiScoreText.setFont(SCORE_FONT);
        aiScoreText.setEffect(TEXT_BLUR);
        setX(playerScoreText, 30);
        setY(playerScoreText, 35);
        setX(aiScoreText, 580);
        setY(aiScoreText, 35);
        gameRoot.getChildren().addAll(gameCanvas,
                ButtonController.getGameStartButton(),
                ButtonController.getGameTurnButton(),
                ButtonController.getGamePassButton(),
                ButtonController.getGameExitButton(),
                playerScoreText, aiScoreText);
        gameScene = new Scene(gameRoot, GAME_WIDTH, GAME_HEIGHT);
    }

    private static void drawScoreBulbs() {
        for (int i = 0; i < score.length; i++) {
            score[i] = ImageController.getScoreImages()[0];
            gameCanvas.getGraphicsContext2D().drawImage(score[i], GAME_WIDTH / 2 - score[i].getWidth() / 2, 80 + i * score[i].getHeight());
        }
    }

    private void initializeDelayTimeline() {
        KeyFrame delayFrame = new KeyFrame(Duration.millis(250), e -> {/*nop*/});
        delay = new Timeline();
        delay.getKeyFrames().add(delayFrame);
        delay.setCycleCount(1);
    }

    private void createWindow() {
        menuScene = new Scene(menuRoot, GAME_WIDTH, GAME_HEIGHT, Color.web("001b5a"));
        stage.setScene(menuScene);
        stage.setResizable(false);
        stage.setTitle("Get Lucky 33 alpha");
        stage.show();
        SoundController.startSound();
    }

    public static void initBonusCards() {
        gameStarted = true;
        for (BonusCard playerBonusCard : playerBonusCards)
            gameRoot.getChildren().remove(playerBonusCard);
        for (BonusCard playerBonusCard : playerBonusCards)
            gameRoot.getChildren().remove(playerBonusCard);
        playerBonusCards.clear();
        aiBonusCards.clear();
        for (int i = 0; i < 4; ) {
            BonusCard randomCard = BonusCard.getRandomPlayerBonusCard();
            if (playerBonusCards.contains(randomCard)) continue;
            playerBonusCards.add(randomCard);
            randomCard.setOnMouseClicked(e -> {
                bonusEvent(randomCard);
            });
            setX(randomCard, i * 75 + 72);
            setY(randomCard, 430);
            gameRoot.getChildren().add(randomCard);
            ++i;
        }
        for (int i = 0; i < 4; ) {
            BonusCard randomAiCard = BonusCard.getRandomAiBonusCard();
            if (aiBonusCards.contains(randomAiCard)) continue;
            aiBonusCards.add(randomAiCard);
            setX(randomAiCard, GAME_WIDTH - 160 - i * 75);
            setY(randomAiCard, 430);
            gameRoot.getChildren().add(randomAiCard);
            ++i;
        }
    }

    public static void bonusEvent(BonusCard randomCard) {
        SoundController.bonusCard();
        playerBonusCards.remove(randomCard);
        gameRoot.getChildren().remove(randomCard);
        addPlainCardPlayer(PlainCard.getPlainCardByNominal(randomCard.getNominal()));
        setPlayerBonusCardsActive(false);
    }

    public static void setPlayerBonusCardsActive(boolean active) {
        playerBonusActive = active;
        for (BonusCard card : playerBonusCards)
            card.setActive(active);
    }

    public static void addPlainCards() {
        addPlainCardPlayer(PlainCard.getRandomCard());
        addPlainCardAI();
    }

    public static void addPlainCardPlayer(Card playerCard) {
        playerPlainCards.add(playerCard);
        setX(playerCard, playerCardsXOffset++ * 72 + 26);
        setY(playerCard, 72 + playerCardsYOffset * heightOf(playerCard));
        if (playerCardsXOffset == 6) {
            playerCardsXOffset = 0;
            playerCardsYOffset += 1.04;
        }
        gameRoot.getChildren().add(playerCard);
        playerScore += playerCard.getNominal();
        if (playerScore < 0)
            playerScore = 0;
        updatePlayerInfo();
    }

    public static void updatePlayerInfo() {
        playerScoreText.setText("Your score: " + playerScore + (playerPassed ? " (Passed)" : ""));
    }

    private static void updateAiInfo() {
        aiScoreText.setText("Opponent score: " + aiScore + (aiPassed ? " (Passed)" : ""));
    }

    public static void addPlainCardAI() {
        delay();
        delay.setOnFinished(e -> {
            aiTurn();
            if (playerPassed) {
                delay.setOnFinished(nop -> {/*nop*/});
                while (!roundEnded) {
                    checkScore();
                    aiTurn();
                }
            }
        });
    }

    private static void aiTurn() {
        if (roundEnded)
            return;
        PlainCard aiCard = PlainCard.getRandomCard();
        addAiCard(aiCard);
        checkAI();
    }

    private static void addAiCard(Card aiCard) {
        aiPlainCards.add(aiCard);
        setX(aiCard, aiCardsXOffset++ * 72 + 574);
        setY(aiCard, 72 + aiCardsYOffset * heightOf(aiCard));
        if (aiCardsXOffset == 6) {
            aiCardsXOffset = 0;
            aiCardsYOffset += 1.04;
        }
        if (!gameRoot.getChildren().contains(aiCard)) {
            gameRoot.getChildren().add(aiCard);
            aiScore += aiCard.getNominal();
        }
        updateAiInfo();
    }

    private static void checkAI() {
        System.out.println("CHECK AI");
        for (BonusCard card : aiBonusCards)
            System.out.println(card.getNominal());
        if (aiScore <= WINNING_SCORE - 10 || !aiBonusActive)
            return;
        if (aiScore == WINNING_SCORE) {
            setAiPassed(true);
            return;
        }
        boolean hasPlusOneCard = false;
        for (BonusCard card : aiBonusCards) {
            if (card.getNominal() == 1) {
                hasPlusOneCard = true;
                break;
            }
        }
        if (aiScore == WINNING_SCORE - 1 && playerPassed && playerScore == WINNING_SCORE - 1 && !hasPlusOneCard) {
            setAiPassed(true);
            return;
        } else {
            Iterator<BonusCard> cardIterator = aiBonusCards.iterator();
            while (cardIterator.hasNext()) {
                Card c = cardIterator.next();
                int potentialScore = aiScore + c.getNominal();
                if (potentialScore == WINNING_SCORE || potentialScore == playerScore && playerPassed) {
                    setAiPassed(true);
                    aiBonusCards.remove(c);
                    gameRoot.getChildren().remove(c);
                    aiBonusActive = false;
                    addAiCard(PlainCard.getPlainCardByNominal(c.getNominal()));
                    return;
                }
            }
        }
        if (aiScore > WINNING_SCORE)
            chooseCardDecreasingScore();
        if (playerPassed)
            chooseCardPlayerPassed();
    }

    private static void chooseCardDecreasingScore() {
        Card result = findMostSuitableCard();
        if (result != null) {
            aiBonusCards.remove(result);
            gameRoot.getChildren().remove(result);
            aiBonusActive = false;
            addAiCard(PlainCard.getPlainCardByNominal(result.getNominal()));
        }
        if (aiScore == WINNING_SCORE || (aiScore == WINNING_SCORE - 1 && (playerScore != 33 && !playerPassed)))
            setAiPassed(true);
    }

    private static Card findMostSuitableCard() {
        Card result = null;
        for (Card card : aiBonusCards) {
            if (card.getNominal() > 0)
                continue;
            int potentialScore = aiScore + card.getNominal();
            if ((potentialScore >= playerScore || playerScore > WINNING_SCORE) && potentialScore <= WINNING_SCORE) {
                if (result == null) {
                    result = card;
                    continue;
                }
                if (potentialScore > aiScore + result.getNominal())
                    result = card;
            }
        }
        return result;
    }

    private static void chooseCardPlayerPassed() {
        if (aiScore <= WINNING_SCORE && aiScore >= playerScore) {
            setAiPassed(true);
            checkScore();
            return;
        }
        Iterator<BonusCard> cardIterator = aiBonusCards.iterator();
        while (cardIterator.hasNext()) {
            Card c = cardIterator.next();
            int potentialScore = aiScore + c.getNominal();
            if (potentialScore >= playerScore && potentialScore <= WINNING_SCORE) {
                aiBonusCards.remove(c);
                gameRoot.getChildren().remove(c);
                aiBonusActive = false;
                addAiCard(PlainCard.getPlainCardByNominal(c.getNominal()));
                return;
            }
        }
    }

    public static void resetObjects() {
        for (BonusCard playerBonusCard : playerBonusCards)
            gameRoot.getChildren().remove(playerBonusCard);
        for (Card playerPlainCard : playerPlainCards)
            gameRoot.getChildren().removeAll(playerPlainCard);
        for (BonusCard playerBonusCard : aiBonusCards)
            gameRoot.getChildren().remove(playerBonusCard);
        for (Card aiPlainCard : aiPlainCards)
            gameRoot.getChildren().removeAll(aiPlainCard);
        drawScoreBulbs();
        playerCardsXOffset = playerCardsYOffset = aiCardsXOffset = aiCardsYOffset = 0;
        playerScore = aiScore = 0;
        playerWinrate = aiWinrate = 0;
        roundNumber = 0;
        playerScoreText.setText("Your score: " + playerScore);
        aiScoreText.setText("Opponent score: " + aiScore);
        playerBonusActive = true;
        aiBonusActive = true;
        playerPassed = false;
        aiPassed = false;
        roundEnded = false;
        gameStarted = false;
        BonusCard.resetCards();
    }


    public static void startGame() {
        stage.setScene(gameScene);
    }

    public static void toMenu() {
        if (gameRoot.getChildren().contains(resultFrame))
            gameRoot.getChildren().remove(resultFrame);
        stage.setScene(menuScene);
    }

    private static void delay() {
        delay.play();
    }

    public static void checkScore() {
        if ((playerPassed && aiPassed && aiScore <= 33 && aiScore > playerScore)
                || (playerScore > WINNING_SCORE && aiScore <= WINNING_SCORE)
                || (playerPassed && (playerScore <= WINNING_SCORE && (aiScore > playerScore && aiScore <= WINNING_SCORE))))
            prepareForNextRound(-1);
        else if ((playerScore > WINNING_SCORE && aiScore > WINNING_SCORE)
                || (playerScore == WINNING_SCORE && aiScore == WINNING_SCORE)
                || (playerPassed && aiPassed && playerScore == aiScore))
            prepareForNextRound(0);
        else if ((playerPassed && aiPassed && aiScore <= 33 && aiScore < playerScore)
                || (playerScore <= WINNING_SCORE && aiScore > WINNING_SCORE))
            prepareForNextRound(1);
    }

    //-1 = you lose, 0 = draw, 1 = you win
    private static void prepareForNextRound(int status) {
        SoundController.result();
        roundEnded = true;
        playerBonusActive = aiBonusActive = false;
        score[roundNumber] = ImageController.getScoreImages()[status < 0 ? 1 : (status > 0 ? 2 : 0)];
        gameCanvas.getGraphicsContext2D().drawImage(score[roundNumber],
                GAME_WIDTH / 2 - score[roundNumber].getWidth() / 2,
                80 + roundNumber * score[roundNumber].getHeight());
        if (status < 0) {
            aiWinrate++;
            roundNumber++;
        } else if (status > 0) {
            playerWinrate++;
            roundNumber++;
        }
        resultFrame = ButtonController.getResultFrame(status);
        gameRoot.getChildren().add(resultFrame);
        if (playerWinrate == 3 || aiWinrate == 3)
            gameStarted = false;
    }

    //todo: duplicating code with Game.resetObjects()
    public static void nextRound() {
        gameRoot.getChildren().remove(resultFrame);
        playerCardsXOffset = playerCardsYOffset = aiCardsXOffset = aiCardsYOffset = 0;
        playerScore = aiScore = 0;
        for (Card playerPlainCard : playerPlainCards)
            gameRoot.getChildren().removeAll(playerPlainCard);
        for (Card aiPlainCard : aiPlainCards)
            gameRoot.getChildren().removeAll(aiPlainCard);
        playerPlainCards.clear();
        aiPlainCards.clear();
        playerBonusActive = true;
        aiBonusActive = true;
        playerPassed = false;
        aiPassed = false;
        roundEnded = false;
        updatePlayerInfo();
        updateAiInfo();
        if (!gameStarted) {
            resetObjects();
            toMenu();
        }
    }

    public static void main(String[] args) {
        launch(args);
        SoundController.finish();
    }

    //Getters and setters

    public static boolean isPlayerPassed() {
        return playerPassed;
    }

    public static void setPlayerPassed(boolean playerPassed) {
        Game.playerPassed = playerPassed;
        setPlayerBonusCardsActive(!playerPassed);
        updatePlayerInfo();
    }

    public static boolean isAiPassed() {
        return aiPassed;
    }

    private static void setAiPassed(boolean passed) {
        aiPassed = passed;
        updateAiInfo();
    }

    public static void setAiBonusCardsActive(boolean aiBonusActive) {
        Game.aiBonusActive = aiBonusActive;
    }

    public static boolean isGameStarted() {
        return gameStarted;
    }

    public static boolean isRoundEnded() {
        return roundEnded;
    }

    public static int getAiScore() {
        return aiScore;
    }

    public static int getPlayerScore() {
        return playerScore;
    }

}
