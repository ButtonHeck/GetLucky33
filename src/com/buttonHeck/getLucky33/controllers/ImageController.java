package com.buttonHeck.getLucky33.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageController {

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
        createSwingImages();
        createFXImages();
    }

    private static void createSwingImages() {
        try {
            backgroundSW = ImageIO.read(ImageController.class.getResource("/textures/background.png"));
            backgroundSW = getScaledBufferedImage(backgroundSW, 3);
            sheet = ImageIO.read(ImageController.class.getResource("/textures/sheet.png"));
            createCardsImageData();
            createResultFrameData();
            createButtonImageData();
            createScoreImageData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createCardsImageData() {
        plainCardsSW = new BufferedImage[20];
        for (int i = 0; i < plainCardsSW.length / 2; i++) {
            plainCardsSW[i] = getScaledBufferedImage(sheet.getSubimage(i * CARD_WIDTH, 0, CARD_WIDTH, CARD_HEIGHT), 2);
            plainCardsSW[i + 10] = getScaledBufferedImage(sheet.getSubimage(i * CARD_WIDTH, CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT), 2);
        }
        bonusCardsActiveSW = new BufferedImage[10];
        bonusCardsNonActiveSW = new BufferedImage[10];
        for (int i = 0; i < bonusCardsActiveSW.length; i++) {
            bonusCardsActiveSW[i] = getScaledBufferedImage(sheet.getSubimage(i * CARD_WIDTH, CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT), 2);
            bonusCardsNonActiveSW[i] = getScaledBufferedImage(sheet.getSubimage(i * CARD_WIDTH, CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT), 2);
        }
        for (int i = 0; i < 5; i++) {
            changeColor(bonusCardsNonActiveSW[i], 0xFFBE2632, 0xFF707070);
            changeColor(bonusCardsNonActiveSW[i], 0xFFEECBC1, 0xFFCCCCCC);
        }
        for (int i = 5; i < bonusCardsNonActiveSW.length; i++) {
            changeColor(bonusCardsNonActiveSW[i], 0xFF36772F, 0xFF707070);
            changeColor(bonusCardsNonActiveSW[i], 0xFFCAF39E, 0xFFCCCCCC);
        }
        aiBonusCardSW = getScaledBufferedImage(sheet.getSubimage(8 * CARD_WIDTH, 2 * CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT), 2);
    }

    private static void createResultFrameData() {
        resultFrameSW = new BufferedImage[4];
        for (int i = 0; i < resultFrameSW.length; i++) {
            resultFrameSW[i] = getScaledBufferedImage(sheet.getSubimage(80, 96, 5 * CARD_WIDTH, 23), 4);
        }
        changeColor(resultFrameSW[1], 0xFF999999, 0xFFF43955);
        changeColor(resultFrameSW[2], 0xFF999999, 0xFF50C556);
        changeColor(resultFrameSW[3], 0xFF999999, 0xFFFFFFFF);
    }

    private static void createButtonImageData() {
        buttonSW = new BufferedImage[3];
        for (int i = 0; i < buttonSW.length; i++) {
            buttonSW[i] = getScaledBufferedImage(sheet.getSubimage(0, 96, 2 * CARD_WIDTH, 32), 3);
        }
        changeColor(buttonSW[1], 0xFFBB9EBB, 0xFFFFFFFF);
        changeColor(buttonSW[2], 0xFFBB9EBB, 0xFF704D70);
    }

    private static void createScoreImageData() {
        scoreSW = new BufferedImage[3];
        for (int i = 0; i < scoreSW.length; i++) {
            scoreSW[i] = getScaledBufferedImage(sheet.getSubimage(64, 96, 16, 16), 2);
        }
        changeColor(scoreSW[1], 0xFFAAAAAA, 0xFFF43955);
        changeColor(scoreSW[1], 0xFFD1D0D0, 0xFFFFFFFF);
        changeColor(scoreSW[2], 0xFFAAAAAA, 0xFF50C556);
        changeColor(scoreSW[2], 0xFFD1D0D0, 0xFFFFFFFF);
    }

    private static void createFXImages() {
        background = SwingFXUtils.toFXImage(backgroundSW, null);
        plainCardsImages = new Image[plainCardsSW.length];
        for (int i = 0; i < plainCardsImages.length; i++) {
            plainCardsImages[i] = SwingFXUtils.toFXImage(plainCardsSW[i], null);
        }
        bonusCardsImagesActive = new Image[bonusCardsActiveSW.length];
        bonusCardsImagesNonActive = new Image[bonusCardsNonActiveSW.length];
        for (int i = 0; i < bonusCardsImagesActive.length; i++) {
            bonusCardsImagesActive[i] = SwingFXUtils.toFXImage(bonusCardsActiveSW[i], null);
            bonusCardsImagesNonActive[i] = SwingFXUtils.toFXImage(bonusCardsNonActiveSW[i], null);
        }
        aiBonusCardImage = SwingFXUtils.toFXImage(aiBonusCardSW, null);
        resultFrameImages = new Image[resultFrameSW.length];
        for (int i = 0; i < resultFrameImages.length; i++) {
            resultFrameImages[i] = SwingFXUtils.toFXImage(resultFrameSW[i], null);
        }
        buttonImages = new Image[buttonSW.length];
        for (int i = 0; i < buttonImages.length; i++) {
            buttonImages[i] = SwingFXUtils.toFXImage(buttonSW[i], null);
        }
        scoreImages = new Image[scoreSW.length];
        for (int i = 0; i < scoreImages.length; i++) {
            scoreImages[i] = SwingFXUtils.toFXImage(scoreSW[i], null);
        }
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

    public static Image getBackground() {
        return background;
    }

    public static Image getAiBonusCardImage() {
        return aiBonusCardImage;
    }

    public static Image getPlainCardImage(int nominal) {
        return plainCardsImages[nominal - 1];
    }

    public static Image getBonusCardImage(int nominal, boolean active) {
        if (nominal >= 1 && nominal <= 5)
            return active ? bonusCardsImagesActive[nominal + 4] : bonusCardsImagesNonActive[nominal + 4];
        else if (nominal <= -1 && nominal >= -5)
            return active ? bonusCardsImagesActive[Math.abs(1 + nominal)] : bonusCardsImagesNonActive[Math.abs(1 + nominal)];
        else throw new IllegalArgumentException(nominal + " is not in range [-5;-1] or [1;5]");
    }

    static Image getButtonImage(int status) {
        return buttonImages[status];
    }

    public static Image[] getScoreImages() {
        return scoreImages;
    }

    static Image getResultFrameImage(int index) {
        return resultFrameImages[index];
    }
}
