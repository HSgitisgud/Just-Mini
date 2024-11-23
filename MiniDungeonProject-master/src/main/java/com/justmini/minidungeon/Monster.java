package com.justmini.minidungeon;

import com.justmini.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public abstract class Monster implements Battle {

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

    @Override
    public BattleResult battle(Player player, Monster monster) {
        SoundPlayer.playSound("/sounds/battle_sound2.wav");

        // 방어력 비율 계산
        double defenseRatio = (double) player.getDef() / (100 + player.getDef());

        // 데미지 계산
        int damageToPlayer = (int) Math.max(0, monster.getAtk() * (1 - defenseRatio));

        player.setHp(player.getHp() - damageToPlayer);

        boolean playerDefeated = player.getHp() <= 0;

        // BattleResult 객체 생성하여 반환
        return new BattleResult(0, damageToPlayer, false, playerDefeated);
    }
}
