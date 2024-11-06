package com.justmini.minidungeon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class TutorialPanel extends JPanel {

    private int currentSlide = 0;
    private final int totalSlides = 3;
    private Image[] tutorialImages;
    private String[] tutorialTexts;
    private MiniDungeonGame mainFrame;

    public TutorialPanel(MiniDungeonGame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Color.BLACK);

        loadTutorialContent();

        // 포커스 설정
        setFocusable(true);

        // 키 이벤트 리스너 추가
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    currentSlide++;
                    if (currentSlide >= totalSlides) {
                        // 튜토리얼 종료 후 게임 시작
                        mainFrame.startGame();
                    } else {
                        repaint();
                    }
                }
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> requestFocusInWindow()); // 컴포넌트가 표시될 때 포커스 요청
    }

    private void loadTutorialContent() {
        tutorialImages = new Image[totalSlides];
        tutorialTexts = new String[totalSlides];

        // 이미지와 텍스트 로드
        for (int i = 0; i < totalSlides; i++) {
            // 이미지 로드
            String imagePath = "/images/tutorial/tutorial" + (i + 1) + ".png";
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                tutorialImages[i] = new ImageIcon(imageUrl).getImage();
            } else {
                System.err.println("no image: " + imagePath);
            }
        }
        // 설명 텍스트 설정
        tutorialTexts[0] = "While you were sleeping, a goblin stole your armor! (Press Enter)";
        tutorialTexts[1] = "You go into the forest to seek revenge on the goblins... (Press Enter)";
        tutorialTexts[2] = "Your victory condition is to kill all monsters." + "\n" + // 이스케이프 문자 왜 안되는데~~
                "You are weak now with no armor, so fighting with goblins early on would be an unwise choice. (Press Enter)";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 현재 슬라이드의 이미지와 텍스트를 그림
        if (currentSlide < totalSlides) {
            // 이미지 그리기
            if (tutorialImages[currentSlide] != null) {
                int imageWidth = tutorialImages[currentSlide].getWidth(this);
                int imageHeight = tutorialImages[currentSlide].getHeight(this);
                int x = (getWidth() - imageWidth) / 2;
                int y = (getHeight() - imageHeight) / 2 - 50;
                g.drawImage(tutorialImages[currentSlide], x, y, this);
            }

            // 텍스트 그리기
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.PLAIN, 18));
            FontMetrics fm = g.getFontMetrics();
            String text = tutorialTexts[currentSlide];
            int textWidth = fm.stringWidth(text);
            int x = (getWidth() - textWidth) / 2;
            int y = getHeight() - 100;
            g.drawString(text, x, y);
        }
    }
}
