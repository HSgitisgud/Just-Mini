package com.justmini.minidungeon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MonsterPanel extends JPanel {

    private List<Monster> monsters;
    private int tileSize;

    public MonsterPanel(List<Monster> monsters, int tileSize) {
        this.monsters = monsters;
        this.tileSize = tileSize;

        setOpaque(false);
        setLayout(null);
        setSize(tileSize * 17, tileSize * 9);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 각 몬스터 그리기
        for (Monster monster : monsters) {
            int drawX = monster.getX() * tileSize;
            int drawY = monster.getY() * tileSize;

            if (monster.getImage() != null) {
                g.drawImage(
                        monster.getImage(),
                        drawX, drawY, tileSize, tileSize, this
                );
            } else {
                // 이미지가 없을 경우 기본 도형으로 표시
                g.setColor(Color.RED);
                g.fillRect(drawX, drawY, tileSize, tileSize);
            }
        }
    }

    public void updateMonsters(List<Monster> monsters) {
        this.monsters = monsters;
        repaint();
    }
}
