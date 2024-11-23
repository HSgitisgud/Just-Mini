package com.justmini.minidungeon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public abstract class Item {

    protected int x;
    protected int y;
    protected String name;
    protected Image image;

    public Item(int x, int y, String name, String imagePath) {
        this.x = x;
        this.y = y;
        this.name = name;

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
