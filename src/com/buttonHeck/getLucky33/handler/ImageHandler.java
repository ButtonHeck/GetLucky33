package com.buttonHeck.getLucky33.handler;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class ImageHandler {

    private static final int CARD_WIDTH = 32, CARD_HEIGHT = 48;

    private static BufferedImage backgroundSW, sheet;
    private static BufferedImage plainCardsSW[],
            bonusCardsActiveSW[],
            bonusCardsNonActiveSW[],
            aiBonusCardSW,
            resultFrameSW[],
            buttonSW[],
            scoreSW[];

    private static Image background,
            plainCardsImages[],
            bonusCardsImagesActive[],
            bonusCardsImagesNonActive[],
            aiBonusCardImage,
            resultFrameImages[],
            buttonImages[],
            scoreImages[];

    static {
        try {
            loadSheets();
            createCardsImageData();
            createResultFrameData();
            createButtonImageData();
            createScoreImageData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadSheets() throws IOException {
        backgroundSW = ImageIO.read(ImageHandler.class.getResource("/textures/background.png"));
        backgroundSW = getScaledBufferedImage(backgroundSW, 3);
        sheet = ImageIO.read(ImageHandler.class.getResource("/textures/sheet.png"));
        background = SwingFXUtils.toFXImage(backgroundSW, null);
    }

    private static void createCardsImageData() {
        createPlainCardsImages();
        createBonusCardsImages();
        createAIBonusCardImage();
    }

    private static void createPlainCardsImages() {
        plainCardsSW = new BufferedImage[20];
        for (int i = 0; i < plainCardsSW.length / 2; i++) {
            plainCardsSW[i] = getScaledBufferedImage(sheet.getSubimage(i * CARD_WIDTH, 0, CARD_WIDTH, CARD_HEIGHT), 2);
            plainCardsSW[i + 10] = getScaledBufferedImage(sheet.getSubimage(i * CARD_WIDTH, CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT), 2);
        }
        plainCardsImages = new Image[plainCardsSW.length];
        loadFXImages(plainCardsSW, plainCardsImages);
    }

    private static void createBonusCardsImages() {
        createActiveCardsImages();
        createNonActiveCardsImages();
    }

    private static void createActiveCardsImages() {
        bonusCardsActiveSW = new BufferedImage[10];
        for (int i = 0; i < bonusCardsActiveSW.length; i++)
            bonusCardsActiveSW[i] = getScaledBufferedImage(sheet.getSubimage(i * CARD_WIDTH, CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT), 2);
        bonusCardsImagesActive = new Image[bonusCardsActiveSW.length];
        loadFXImages(bonusCardsActiveSW, bonusCardsImagesActive);
    }

    private static void createNonActiveCardsImages() {
        bonusCardsNonActiveSW = new BufferedImage[10];
        for (int i = 0; i < 5; i++) {
            bonusCardsNonActiveSW[i] = getScaledBufferedImage(sheet.getSubimage(i * CARD_WIDTH, CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT), 2);
            bonusCardsNonActiveSW[i + 5] = getScaledBufferedImage(sheet.getSubimage((i + 5) * CARD_WIDTH, CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT), 2);
            changeColor(bonusCardsNonActiveSW[i], 0xFFBE2632, 0xFF707070);
            changeColor(bonusCardsNonActiveSW[i], 0xFFEECBC1, 0xFFCCCCCC);
            changeColor(bonusCardsNonActiveSW[i + 5], 0xFF36772F, 0xFF707070);
            changeColor(bonusCardsNonActiveSW[i + 5], 0xFFCAF39E, 0xFFCCCCCC);
        }
        bonusCardsImagesNonActive = new Image[bonusCardsNonActiveSW.length];
        loadFXImages(bonusCardsNonActiveSW, bonusCardsImagesNonActive);
    }

    private static void createAIBonusCardImage() {
        aiBonusCardSW = getScaledBufferedImage(sheet.getSubimage(8 * CARD_WIDTH, 2 * CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT), 2);
        aiBonusCardImage = SwingFXUtils.toFXImage(aiBonusCardSW, null);
    }

    private static void createResultFrameData() {
        resultFrameSW = new BufferedImage[4];
        for (int i = 0; i < resultFrameSW.length; i++)
            resultFrameSW[i] = getScaledBufferedImage(sheet.getSubimage(80, 96, 5 * CARD_WIDTH, 23), 4);
        changeColor(resultFrameSW[1], 0xFF999999, 0xFFF43955);
        changeColor(resultFrameSW[2], 0xFF999999, 0xFF50C556);
        changeColor(resultFrameSW[3], 0xFF999999, 0xFFFFFFFF);
        resultFrameImages = new Image[resultFrameSW.length];
        loadFXImages(resultFrameSW, resultFrameImages);
    }

    private static void createButtonImageData() {
        buttonSW = new BufferedImage[3];
        for (int i = 0; i < buttonSW.length; i++)
            buttonSW[i] = getScaledBufferedImage(sheet.getSubimage(0, 96, 2 * CARD_WIDTH, 32), 3);
        changeColor(buttonSW[1], 0xFFBB9EBB, 0xFFFFFFFF);
        changeColor(buttonSW[2], 0xFFBB9EBB, 0xFF704D70);
        buttonImages = new Image[buttonSW.length];
        loadFXImages(buttonSW, buttonImages);
    }

    private static void createScoreImageData() {
        scoreSW = new BufferedImage[3];
        for (int i = 0; i < scoreSW.length; i++)
            scoreSW[i] = getScaledBufferedImage(sheet.getSubimage(64, 96, 16, 16), 2);
        changeColor(scoreSW[1], 0xFFAAAAAA, 0xFFF43955);
        changeColor(scoreSW[1], 0xFFD1D0D0, 0xFFFFFFFF);
        changeColor(scoreSW[2], 0xFFAAAAAA, 0xFF50C556);
        changeColor(scoreSW[2], 0xFFD1D0D0, 0xFFFFFFFF);
        scoreImages = new Image[scoreSW.length];
        loadFXImages(scoreSW, scoreImages);
    }

    private static void loadFXImages(BufferedImage[] src, Image[] dest) {
        for (int i = 0; i < src.length; i++)
            dest[i] = SwingFXUtils.toFXImage(src[i], null);
    }

    private static void changeColor(BufferedImage image, int oldColor, int newColor) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) == oldColor) {
                    image.setRGB(x, y, newColor);
                }
            }
        }
    }

    private static BufferedImage getScaledBufferedImage(BufferedImage original, int scale) {
        int newWidth = original.getWidth() * scale;
        int newHeight = original.getHeight() * scale;
        BufferedImage result = new BufferedImage(newWidth, newHeight, original.getType());
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                int color = original.getRGB(x, y);
                for (int yOffset = 0; yOffset < scale; yOffset++) {
                    for (int xOffset = 0; xOffset < scale; xOffset++) {
                        result.setRGB(x * scale + xOffset, y * scale + yOffset, color);
                    }
                }
            }
        }
        return result;
    }

    //Getters

    public static Image getBackground() {
        return background;
    }

    public static Image getPlainCardImage(int nominal) {
        return plainCardsImages[nominal - 1];
    }

    public static Image getBonusCardImage(int nominal, boolean active) {
        if (nominal >= 1 && nominal <= 5)
            return active ? bonusCardsImagesActive[nominal + 4] : bonusCardsImagesNonActive[nominal + 4];
        else if (nominal <= -1 && nominal >= -5)
            return active
                    ? bonusCardsImagesActive[Math.abs(1 + nominal)]
                    : bonusCardsImagesNonActive[Math.abs(1 + nominal)];
        else throw new IllegalArgumentException(nominal + " is not in range [-5;-1] or [1;5]");
    }

    public static Image getAiBonusCardImage() {
        return aiBonusCardImage;
    }

    static Image getButtonImage(int status) {
        return buttonImages[status];
    }

    static Image getScoreImage(int index) {
        return scoreImages[index];
    }

    static Image getResultFrameImage(int index) {
        return resultFrameImages[index];
    }
}