package com.buttonHeck.getLucky33.handler;

import com.buttonHeck.getLucky33.Game;
import com.buttonHeck.getLucky33.card.Card;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import static com.buttonHeck.getLucky33.Game.GAME_WIDTH;
import static com.buttonHeck.getLucky33.Game.WIN_SCORE;

public class ScoreHandler {
    private int playerScore, aiScore;
    private int playerWinrate, aiWinrate, roundNumber;
    private Canvas gameCanvas;
    private Image score[];

    public ScoreHandler(Canvas canvas) {
        playerScore = aiScore = playerWinrate = aiWinrate = roundNumber = 0;
        gameCanvas = canvas;
        score = new Image[5];
    }

    public void drawScoreBulbs() {
        for (int i = 0; i < score.length; i++) {
            score[i] = ImageHandler.getScoreImage(0);
            gameCanvas.getGraphicsContext2D().drawImage(score[i],
                    GAME_WIDTH / 2 - score[i].getWidth() / 2,
                    80 + i * score[i].getHeight());
        }
    }

    public void renewScoreBulbs(int status) {
        score[roundNumber] = ImageHandler.getScoreImage(status < 0 ? 1 : (status > 0 ? 2 : 0));
        gameCanvas.getGraphicsContext2D().drawImage(score[roundNumber],
                GAME_WIDTH / 2 - score[roundNumber].getWidth() / 2,
                80 + roundNumber * score[roundNumber].getHeight());
    }

    public int playerScoreChanged(Card playerCard) {
        playerScore += playerCard.getNominal();
        if (playerScore < 0)
            playerScore = 0;
        return playerScore;
    }

    public int aiScoreChanged(Card aiCard) {
        aiScore += aiCard.getNominal();
        return aiScore;
    }

    public void resetWinrate() {
        playerWinrate = aiWinrate = 0;
        roundNumber = 0;
    }

    public void renewWinrate(int status) {
        ++roundNumber;
        if (status < 0)
            ++aiWinrate;
        else
            ++playerWinrate;
    }

    public boolean aiNoNeedBonusCard(boolean aiBonusActive) {
        if (aiScore <= WIN_SCORE - 10 || !aiBonusActive)
            return true;
        if (aiScore == WIN_SCORE) {
            Game.setAiPassed();
            return true;
        }
        return false;
    }

    public boolean bestOf5Found() {
        return playerWinrate == 3 || aiWinrate == 3;
    }

    public int getAiScore() {
        return aiScore;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void resetScore() {
        playerScore = aiScore = 0;
    }
}