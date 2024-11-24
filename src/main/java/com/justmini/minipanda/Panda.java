package com.justmini.minipanda;

import com.justmini.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Panda {
    private int x, y;
    private int width, height;
    private double yVelocity = 0;
    private double gravity = 0.5;
    private boolean isJumping = false;
    private boolean canDoubleJump = false;

    private Image pandaImage;

    public Panda(int x, int y) {
        this.x = x;
        this.y = y;
        loadImage();
    }

    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/minipanda/panda.gif"));
        pandaImage = icon.getImage();
        width = 64;
        height = 64;
    }

    public void update() {
        yVelocity += gravity;
        y += yVelocity;

        if (y >= 504) {
            y = 504;
            yVelocity = 0;
            isJumping = false;
            canDoubleJump = false;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(pandaImage, x, y, null);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!isJumping) {
                yVelocity = -12; // Initial jump
                isJumping = true;
                canDoubleJump = true;
            } else if (canDoubleJump) {
                yVelocity = -12; // Double jump
                canDoubleJump = false;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        // Not used
    }

    public Rectangle getBounds() {
        // Adjusted collision bounds to match visible panda (32x32 pixels)
        return new Rectangle(x + 16, y + 16, 32, 32);
    }

    public int getX() {
        return x;
    }
}
