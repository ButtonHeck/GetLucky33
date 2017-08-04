package com.buttonHeck.getLucky33.handler;

import com.buttonHeck.getLucky33.card.BonusCard;
import com.buttonHeck.getLucky33.card.Card;
import javafx.scene.Group;

import java.util.ArrayList;

import static com.buttonHeck.getLucky33.Game.GAME_WIDTH;
import static com.buttonHeck.getLucky33.HelperMethods.*;

public class CardDeckHandler {

    public static final boolean PLAYER_DECK = true;
    public static final boolean AI_DECK = false;

    private Group gameRoot;
    private ArrayList<BonusCard> playerBonusCards = new ArrayList<>();
    private ArrayList<Card> playerPlainCards = new ArrayList<>();
    private ArrayList<BonusCard> aiBonusCards = new ArrayList<>();
    private ArrayList<Card> aiPlainCards = new ArrayList<>();
    private double playerCardsXOffset = 0, playerCardsYOffset = 0, aiCardsXOffset = 0, aiCardsYOffset = 0;

    public CardDeckHandler(Group gameRoot) {
        this.gameRoot = gameRoot;
    }

    public void initBonusCards() {
        initPlayerBonusCards();
        initAiBonusCards();
    }

    private void initPlayerBonusCards() {
        for (int i = 0; i < 4; ) {
            BonusCard card = BonusCard.getRandomPlayerBonusCard();
            if (tryAddBonusCard(card, PLAYER_DECK)) continue;
            setXY(card, i * 75 + 72, 430);
            gameRoot.getChildren().add(card);
            ++i;
        }
    }

    private void initAiBonusCards() {
        for (int i = 0; i < 4; ) {
            BonusCard aiCard = BonusCard.getRandomAiBonusCard();
            if (tryAddBonusCard(aiCard, AI_DECK)) continue;
            setXY(aiCard, GAME_WIDTH - 160 - i * 75, 430);
            gameRoot.getChildren().add(aiCard);
            ++i;
        }
    }

    private boolean tryAddBonusCard(BonusCard card, boolean isPlayerDeck) {
        if (isPlayerDeck) {
            if (playerBonusCards.contains(card)) return true;
            playerBonusCards.add(card);
            return false;
        } else {
            if (aiBonusCards.contains(card)) return true;
            aiBonusCards.add(card);
            return false;
        }
    }

    public void clearBonusCards() {
        gameRoot.getChildren().removeAll(playerBonusCards);
        gameRoot.getChildren().removeAll(aiBonusCards);
        playerBonusCards.clear();
        aiBonusCards.clear();
    }

    public void clearPlainCards() {
        gameRoot.getChildren().removeAll(playerPlainCards);
        gameRoot.getChildren().removeAll(aiPlainCards);
        playerPlainCards.clear();
        aiPlainCards.clear();
        playerCardsXOffset = playerCardsYOffset = aiCardsXOffset = aiCardsYOffset = 0;
    }

    public void addAiCard(Card aiCard) {
        aiPlainCards.add(aiCard);
        setXY(aiCard, aiCardsXOffset++ * 72 + 574, 72 + aiCardsYOffset * heightOf(aiCard));
        if (aiCardsXOffset == 6) {
            aiCardsXOffset = 0;
            aiCardsYOffset += 1.04;
        }
    }

    public void addPlainCardPlayer(Card card) {
        playerPlainCards.add(card);
        setXY(card, playerCardsXOffset++ * 72 + 26, 72 + playerCardsYOffset * heightOf(card));
        if (playerCardsXOffset == 6) {
            playerCardsXOffset = 0;
            playerCardsYOffset += 1.04;
        }
        gameRoot.getChildren().add(card);
    }

    public void setPlayerBonusCardsActive(boolean active) {
        playerBonusCards.forEach(card -> card.setActive(active));
    }

    public boolean aiHasPlusOneCard() {
        boolean hasPlusOneCard = false;
        for (BonusCard card : aiBonusCards) {
            if (card.getNominal() == 1) {
                hasPlusOneCard = true;
                break;
            }
        }
        return hasPlusOneCard;
    }

    public void useBonusCard(boolean isPlayerDeck, Card card) {
        gameRoot.getChildren().remove(card);
        if (isPlayerDeck) {
            playerBonusCards.remove(card);
            setPlayerBonusCardsActive(false);
        } else
            aiBonusCards.remove(card);
    }

    public ArrayList<BonusCard> getAiBonusCards() {
        return aiBonusCards;
    }
}