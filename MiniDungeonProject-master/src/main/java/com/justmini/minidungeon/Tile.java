package com.justmini.minidungeon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Tile {

    private int x;
    private int y;
    private boolean isWalkable;
    private Image image;

    public Tile(int x, int y, boolean isWalkable, String imagePath) {
        this.x = x;
        this.y = y;
        this.isWalkable = isWalkable;

        // 이미지 로드
        if (imagePath != null && !imagePath.isEmpty()) {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                this.image = icon.getImage();
            } else {
                System.err.println("no image: " + imagePath);
                this.image = null;
            }
        } else {
            this.image = null;
        }
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public Image getImage() {
        return image;
    }
}
