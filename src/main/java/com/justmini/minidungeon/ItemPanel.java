package com.justmini.minidungeon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ItemPanel extends JPanel {

    private List<Item> items;
    private int tileSize;

    public ItemPanel(List<Item> items, int tileSize) {
        this.items = items;
        this.tileSize = tileSize;

        setOpaque(false);
        setLayout(null);
        setSize(tileSize * 17, tileSize * 9);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 각 아이템 그리기
        for (Item item : items) {
            int drawX = item.getX() * tileSize;
            int drawY = item.getY() * tileSize;

            if (item.getImage() != null) {
                g.drawImage(
                        item.getImage(),
                        drawX, drawY, tileSize, tileSize, this
                );
            } else {
                // 이미지가 없을 경우 기본 도형으로 표시
                g.setColor(Color.YELLOW);
                g.fillRect(drawX, drawY, tileSize, tileSize);
            }
        }
    }

    public void updateItems(List<Item> items) {
        this.items = items;
        repaint();
    }
}
