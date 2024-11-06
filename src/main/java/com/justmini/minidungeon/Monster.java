package com.justmini.minidungeon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public abstract class Monster {

    protected int x;
    protected int y;
    protected String name;
    protected int hp, atk, def;
    protected int exp;
    protected Image image;

    public Monster(int x, int y, String name, int hp, int atk, int def, int exp, String imagePath) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.exp = exp;

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

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getExp() {
        return exp;
    }
}
