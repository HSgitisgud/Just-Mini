package com.justmini.minipanda;

import javax.swing.*;
import java.awt.*;

public class Background {
    private Image backgroundImage;
    private int x1, x2;
    private int imageWidth;

    public Background() {
        loadImage();
        x1 = 0;
        x2 = imageWidth;
    }

    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/minipanda/background.png"));
        backgroundImage = icon.getImage();
        imageWidth = backgroundImage.getWidth(null);
    }

    public void update(int gameSpeed) {
        // Adjust background speed proportionally to the game speed
        int bgSpeed = gameSpeed / 2;

        x1 -= bgSpeed;
        x2 -= bgSpeed;

        if (x1 + imageWidth <= 0) {
            x1 = x2 + imageWidth;
        }

        if (x2 + imageWidth <= 0) {
            x2 = x1 + imageWidth;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, x1, 0, null);
        g.drawImage(backgroundImage, x2, 0, null);
    }
}
