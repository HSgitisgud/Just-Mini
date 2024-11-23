package com.justmini.minidungeon;

import javax.swing.*;
import java.awt.*;

public class TerrainPanel extends JPanel {

    private Tile[][] tiles;
    private int tileSize;

    public TerrainPanel(Tile[][] tiles, int tileSize) {
        this.tiles = tiles;
        this.tileSize = tileSize;

        int width = tiles[0].length * tileSize;
        int height = tiles.length * tileSize;
        setPreferredSize(new Dimension(width, height));
        setSize(width, height);
        setOpaque(false);
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 타일 그리기
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                Tile tile = tiles[row][col];
                int drawX = col * tileSize;
                int drawY = row * tileSize;

                if (tile.getImage() != null) {
                    g.drawImage(
                            tile.getImage(),
                            drawX, drawY, tileSize, tileSize, this
                    );
                } else {
                    // 기본 배경색으로 채우기
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(drawX, drawY, tileSize, tileSize);
                }

                // 격자선 그리기
                g.setColor(Color.BLACK);
                g.drawRect(drawX, drawY, tileSize, tileSize);
            }
        }
    }
}
