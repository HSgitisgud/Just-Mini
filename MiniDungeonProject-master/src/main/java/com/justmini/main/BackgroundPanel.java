package com.justmini.main;

import javax.swing.*;
import java.awt.*;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {
        // 배경 이미지 로드
        backgroundImage = new ImageIcon(getClass().getResource("/images/mainmenu/spare_background.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 이미지 그리기
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
