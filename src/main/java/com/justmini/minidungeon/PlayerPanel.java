package com.justmini.minidungeon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PlayerPanel extends JPanel {

    private Player player;
    private int tileSize;
    private Image playerImage;
    private Image pandaImage;

    public PlayerPanel(Player player, int tileSize) {
        this.player = player;
        this.tileSize = tileSize;
        setOpaque(false);
        setLayout(null);
        setSize(tileSize * 17, tileSize * 9);

        // 플레이어 이미지 로드
        try {
            URL imageUrl = getClass().getResource("/images/minidungeon/player.png");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                playerImage = icon.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
            } else {
                System.err.println("플레이어 이미지 없음");
                playerImage = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            playerImage = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int drawX = player.getX() * tileSize;
        int drawY = player.getY() * tileSize;

        if (playerImage != null) {
            g.drawImage(playerImage, drawX, drawY, tileSize, tileSize, this);
        } else {
            // 이미지가 없을 경우 기본 도형으로 표시
            g.setColor(Color.BLUE);
            g.fillOval(drawX, drawY, tileSize, tileSize);
        }
    }
}
