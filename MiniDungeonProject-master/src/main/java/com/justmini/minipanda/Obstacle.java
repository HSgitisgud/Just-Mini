package com.justmini.minipanda;

import javax.swing.*;
import java.awt.*;

public class Obstacle {
    private int x, y;
    private int width = 64;
    private int height = 64;
    private int speed;
    private Image obstacleImage;
    private ObstacleType type;
    private int initialY;
    private int movementRange = 100;
    private int direction = 1;

    public Obstacle(int x, int y, int speed, ObstacleType type) {
        this.x = x;
        this.y = y;
        this.initialY = y;
        this.speed = speed;
        this.type = type;
        loadImage();
    }

    private void loadImage() {
        String imagePath = "/images/minipanda/stone.png";
        if (type == ObstacleType.MOVING) {
            imagePath = "/images/minipanda/moving_obstacle.png";
        }
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        obstacleImage = scaledImage;
    }

    public void update() {
        x -= speed;
        if (type == ObstacleType.MOVING) {
            // Move up and down
            y += direction * 2;
            if (y > initialY + movementRange || y < initialY - movementRange) {
                direction *= -1;
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(obstacleImage, x, y, null);
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
