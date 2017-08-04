package com.buttonHeck.getLucky33;

import com.buttonHeck.getLucky33.card.Card;
import com.buttonHeck.getLucky33.handler.ImageHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import static com.buttonHeck.getLucky33.Game.GAME_WIDTH;

public class Score {
    private int playerScore, aiScore;
    private int playerWinrate, aiWinrate, roundNumber;
    private Canvas gameCanvas;
    private Image score[];

    public Score(Canvas canvas) {
        playerScore = aiScore = playerWinrate = aiWinrate = roundNumber = 0;
        gameCanvas = canvas;
        score = new Image[5];
    }

    public void drawScoreBulbs() {
        for (int i = 0; i < score.length; i++) {
            score[i] = ImageHandler.getScoreImage(0);
            gameCanvas.getGraphicsContext2D().drawImage(score[i], GAME_WIDTH / 2 - score[i].getWidth() / 2, 80 + i * score[i].getHeight());
        }
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

    public int getAiScore() {
        return aiScore;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void resetScore() {
        playerScore = aiScore = 0;
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

    public boolean hasWinrateThree() {
        return playerWinrate == 3 || aiWinrate == 3;
    }

    public void renewScoreBulbs(int status) {
        score[roundNumber] = ImageHandler.getScoreImage(status < 0 ? 1 : (status > 0 ? 2 : 0));
        gameCanvas.getGraphicsContext2D().drawImage(score[roundNumber],
                GAME_WIDTH / 2 - score[roundNumber].getWidth() / 2,
                80 + roundNumber * score[roundNumber].getHeight());
    }
}
