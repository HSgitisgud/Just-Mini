package com.justmini.minipanda;

import javax.swing.*;
import java.awt.*;

public class PowerUp {
    private int x, y;
    private int width = 32;
    private int height = 32;
    private int speed;
    private Image powerUpImage;

    public PowerUp(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        loadImage();
    }

    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/minipanda/powerup.png"));
        powerUpImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void update() {
        x -= speed;
    }

    public void draw(Graphics g) {
        g.drawImage(powerUpImage, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
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
