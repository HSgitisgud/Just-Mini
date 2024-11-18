package com.justmini.minipanda;

import javax.swing.*;
import java.awt.*;

public class Bamboo {
    private int x, y;
    private int width = 64;
    private int height = 64;
    private int speed;
    private Image bambooImage;

    public Bamboo(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        loadImage();
    }

    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/minipanda/bamboo.png"));
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        bambooImage = scaledImage;
    }

    public void update() {
        x -= speed;
    }

    public void draw(Graphics g) {
        g.drawImage(bambooImage, x, y, null);
    }

    public Rectangle getBounds() {
        // Adjusted collision bounds
        return new Rectangle(x + 16, y + 16, 32, 32);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }
}
