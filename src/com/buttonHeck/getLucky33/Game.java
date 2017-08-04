package com.buttonHeck.getLucky33;

import com.buttonHeck.getLucky33.card.BonusCard;
import com.buttonHeck.getLucky33.card.Card;
import com.buttonHeck.getLucky33.card.PlainCard;
import com.buttonHeck.getLucky33.handler.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Iterator;

import static com.buttonHeck.getLucky33.handler.CardDeckHandler.AI_DECK;
import static com.buttonHeck.getLucky33.handler.CardDeckHandler.PLAYER_DECK;

public class Game extends Application {

    //Application stuff
    public static final int GAME_WIDTH = 1020, GAME_HEIGHT = 720, WIN_SCORE = 33;

    //Game stuff
    private static Timeline delay;
    private static Stage stage;
    private static Group menuRoot, gameRoot;
    private static Scene menuScene, gameScene;
    private static ButtonHandler.Button resultFrame;
    private static CardDeckHandler cardDeckHandler;
    private static ScoreHandler scoreHandler;

    //logic & score
    private static boolean aiBonusActive = true;
    private static boolean playerPassed, aiPassed, roundEnded, gameInProgress;

    @Override
    public void start(Stage stage) throws Exception {
        Game.stage = stage;
        buildMenuTree();
        buildGameTree();
        initializeDelayTimeline();
        createWindow();
    }

    private static void buildMenuTree() {
        menuRoot = new Group();
        Text author = TextHandler.getAuthor();
        menuRoot.getChildren().addAll(TextHandler.getMenuHints());
        menuRoot.getChildren().addAll(author, ButtonHandler.getMenuStart(), ButtonHandler.getMenuExit());
    }

    private static void buildGameTree() {
        gameRoot = new Group();
        Canvas gameCanvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        gameCanvas.getGraphicsContext2D().drawImage(ImageHandler.getBackground(), 0, 0);
        scoreHandler = new ScoreHandler(gameCanvas);
        scoreHandler.drawScoreBulbs();
        gameRoot.getChildren().addAll(gameCanvas,
                ButtonHandler.getGameStart(),
                ButtonHandler.getGameTurn(),
                ButtonHandler.getGamePass(),
                ButtonHandler.getGameExit(),
                TextHandler.getPlayerScoreText(), TextHandler.getAiScoreText());
        cardDeckHandler = new CardDeckHandler(gameRoot);
        gameScene = new Scene(gameRoot, GAME_WIDTH, GAME_HEIGHT);
    }

    private static void initializeDelayTimeline() {
        KeyFrame frame = new KeyFrame(Duration.millis(250), e -> {/*nop*/});
        delay = new Timeline();
        delay.getKeyFrames().add(frame);
        delay.setCycleCount(1);
    }

    private static void createWindow() {
        menuScene = new Scene(menuRoot, GAME_WIDTH, GAME_HEIGHT, Color.web("001b5a"));
        stage.setScene(menuScene);
        stage.setResizable(false);
        stage.setTitle("Get Lucky 33");
        stage.show();
        AudioHandler.startSound();
    }

    public static void initBonusCards() {
        if (gameInProgress)
            return;
        gameInProgress = true;
        cardDeckHandler.initBonusCards();
    }

    public static void handleBonus(BonusCard card) {
        AudioHandler.bonusCard();
        cardDeckHandler.useBonusCard(PLAYER_DECK, card);
        addPlainCardPlayer(PlainCard.getPlainCardByNominal(card.getNominal()));
    }

    public static void addPlainCardPlayer(Card playerCard) {
        if (playerPassed)
            return;
        cardDeckHandler.addPlainCardPlayer(playerCard);
        int score = scoreHandler.playerScoreChanged(playerCard);
        TextHandler.setPlayerScoreText("Your score: " + score + (playerPassed ? " (Passed)" : ""));
    }

    public static void waitForAI() {
        if (aiPassed)
            return;
        delay.play();
        delay.setOnFinished(e -> {
            if (roundEnded)
                return;
            addAiCard(PlainCard.getRandomPlainCard());
            checkAI();
            if (playerPassed) {
                delay.setOnFinished(null);
                while (!roundEnded) {
                    checkScore();
                    if (roundEnded)
                        return;
                    addAiCard(PlainCard.getRandomPlainCard());
                    checkAI();
                }
            }
        });
    }

    private static void addAiCard(Card aiCard) {
        cardDeckHandler.addAiCard(aiCard);
        if (!gameRoot.getChildren().contains(aiCard)) {
            gameRoot.getChildren().add(aiCard);
            int aiScore = scoreHandler.aiScoreChanged(aiCard);
            TextHandler.setAiScoreText("Opponent score: " + aiScore + (aiPassed ? " (Passed)" : ""));
        }
    }

    private static void checkAI() {
        if (scoreHandler.aiNoNeedBonusCard(aiBonusActive)) return;
        boolean hasPlusOneCard = cardDeckHandler.aiHasPlusOneCard();
        if (scoreHandler.getAiScore() == WIN_SCORE - 1
                && playerPassed
                && scoreHandler.getPlayerScore() == WIN_SCORE - 1 && !hasPlusOneCard) {
            setAiPassed();
            return;
        } else if (aiCardFound()) return;
        decideAiStrategy();
    }

    private static boolean aiCardFound() {
        Iterator<BonusCard> cardIterator = cardDeckHandler.getAiBonusCards().iterator();
        while (cardIterator.hasNext()) {
            Card c = cardIterator.next();
            int potentialScore = scoreHandler.getAiScore() + c.getNominal();
            if (potentialScore == WIN_SCORE || potentialScore == scoreHandler.getPlayerScore() && playerPassed) {
                setAiPassed();
                cardDeckHandler.useBonusCard(AI_DECK, c);
                aiBonusActive = false;
                addAiCard(PlainCard.getPlainCardByNominal(c.getNominal()));
                return true;
            }
        }
        return false;
    }

    private static void decideAiStrategy() {
        if (scoreHandler.getAiScore() > WIN_SCORE)
            chooseCardDecreasingScore();
        if (playerPassed)
            chooseCardPlayerPassed();
    }

    private static void chooseCardDecreasingScore() {
        Card result = findSuitableDecreasingCard();
        if (result != null) {
            cardDeckHandler.useBonusCard(AI_DECK, result);
            aiBonusActive = false;
            addAiCard(PlainCard.getPlainCardByNominal(result.getNominal()));
        }
        if (scoreHandler.getAiScore() == WIN_SCORE
                || (scoreHandler.getAiScore() == WIN_SCORE - 1
                && (scoreHandler.getPlayerScore() != WIN_SCORE && !playerPassed)))
            setAiPassed();
    }

    private static Card findSuitableDecreasingCard() {
        Card result = null;
        for (Card card : cardDeckHandler.getAiBonusCards()) {
            if (card.getNominal() > 0)
                continue;
            int potentialScore = scoreHandler.getAiScore() + card.getNominal();
            if ((potentialScore >= scoreHandler.getPlayerScore() || scoreHandler.getPlayerScore() > WIN_SCORE)
                    && potentialScore <= WIN_SCORE) {
                if (result == null) {
                    result = card;
                    continue;
                }
                if (potentialScore > scoreHandler.getAiScore() + result.getNominal())
                    result = card;
            }
        }
        return result;
    }

    private static void chooseCardPlayerPassed() {
        if (scoreHandler.getAiScore() <= WIN_SCORE && scoreHandler.getAiScore() >= scoreHandler.getPlayerScore()) {
            setAiPassed();
            checkScore();
            return;
        }
        Iterator<BonusCard> it = cardDeckHandler.getAiBonusCards().iterator();
        while (it.hasNext()) {
            Card c = it.next();
            int potentialScore = scoreHandler.getAiScore() + c.getNominal();
            if (potentialScore >= scoreHandler.getPlayerScore() && potentialScore <= WIN_SCORE) {
                it.remove();
                gameRoot.getChildren().remove(c);
                aiBonusActive = false;
                addAiCard(PlainCard.getPlainCardByNominal(c.getNominal()));
                return;
            }
        }
    }

    public static void checkScore() {
        int aiScore = scoreHandler.getAiScore();
        int playerScore = scoreHandler.getPlayerScore();
        if ((playerPassed && aiPassed && aiScore <= 33 && aiScore > playerScore)
                || (playerScore > WIN_SCORE && aiScore <= WIN_SCORE)
                || (playerPassed && (playerScore <= WIN_SCORE && (aiScore > playerScore && aiScore <= WIN_SCORE))))
            showResult(-1);
        else if (playerScore > WIN_SCORE || (playerScore == WIN_SCORE && aiScore == WIN_SCORE)
                || (playerPassed && aiPassed && playerScore == aiScore))
            showResult(0);
        else if ((playerPassed && aiPassed && aiScore <= 33 && aiScore < playerScore) || aiScore > WIN_SCORE)
            showResult(1);
    }

    //-1 = you lose, 0 = draw, 1 = you win
    private static void showResult(int status) {
        cardDeckHandler.setPlayerBonusCardsActive(false);
        ButtonHandler.disableButtons();
        AudioHandler.result();
        roundEnded = true;
        aiBonusActive = false;
        scoreHandler.renewScoreBulbs(status);
        if (status != 0)
            scoreHandler.renewWinrate(status);
        resultFrame = ButtonHandler.getResultFrameWithStatus(status);
        resultFrame.setText(status == 0
                ? "Draw!"
                : (scoreHandler.getPlayerScore() + " : " + scoreHandler.getAiScore()));
        gameRoot.getChildren().add(resultFrame);
        if (scoreHandler.bestOf5Found())
            gameInProgress = false;
    }

    public static void resetGame() {
        cardDeckHandler.clearBonusCards();
        scoreHandler.drawScoreBulbs();
        resetRound();
        scoreHandler.resetWinrate();
        updatePlayerInfo();
        TextHandler.setAiScoreText("Opponent score: " + scoreHandler.getAiScore());
        gameInProgress = false;
    }

    public static void setupNextRound() {
        gameRoot.getChildren().remove(resultFrame);
        resetRound();
        updatePlayerInfo();
        TextHandler.setAiScoreText("Opponent score: " + scoreHandler.getAiScore() + (aiPassed ? " (Passed)" : ""));
        if (!gameInProgress) {
            resetGame();
            toMenu();
        }
    }

    private static void resetRound() {
        aiBonusActive = true;
        playerPassed = false;
        aiPassed = false;
        roundEnded = false;
        cardDeckHandler.clearPlainCards();
        scoreHandler.resetScore();
    }

    public static void startGame() {
        stage.setScene(gameScene);
    }

    public static void toMenu() {
        gameRoot.getChildren().remove(resultFrame);
        stage.setScene(menuScene);
    }

    public static void updatePlayerInfo() {
        TextHandler.setPlayerScoreText("Your score: " + scoreHandler.getPlayerScore() + (playerPassed ? " (Passed)" : ""));
    }

    public static void main(String[] args) {
        launch(args);
        AudioHandler.finish();
    }

    //Getters and setters

    public static void setPlayerPassed() {
        playerPassed = true;
        cardDeckHandler.setPlayerBonusCardsActive(false);
        updatePlayerInfo();
    }

    public static void setAiPassed() {
        aiPassed = true;
        TextHandler.setAiScoreText("Opponent score: " + scoreHandler.getAiScore() + " (Passed)");
    }

    public static void activateBonusCards() {
        cardDeckHandler.setPlayerBonusCardsActive(true);
        aiBonusActive = true;
    }

    public static boolean isRoundEnded() {
        return roundEnded;
    }
}