package com.justmini.minipanda;

import javax.swing.*;
import java.awt.*;

public class FallingObstacle {
    private int x, y;
    private int width = 32;
    private int height = 21;
    private int speed;
    private Image obstacleImage;

    public FallingObstacle(int x, int y, int gameSpeed) {
        this.x = x;
        this.y = y;
        this.speed = gameSpeed + 2; // Falls faster than ground obstacles
        loadImage();
    }

    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/minipanda/falling_obstacle.gif"));
        obstacleImage = icon.getImage();
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics g) {
        g.drawImage(obstacleImage, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setSpeed(int gameSpeed) {
        this.speed = gameSpeed + 2;
    }

    public int getY() {
        return y;
    }
}
