//할 것: 타이틀 아이콘 바꾸기.

package com.justmini.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.justmini.minidungeon.MiniDungeonGame;
import com.justmini.minidodge.MiniDodge;
import com.justmini.util.SoundPlayer;
import com.justmini.minipangpang.MiniPangPang;
import com.justmini.minipanda.MiniPanda;

public class JustMiniMain extends JFrame {
    public JustMiniMain() {
        System.out.println("Initializing JustMiniMain...");
        setTitle("Just Mini");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // 화면 중앙에 표시
        setResizable(false); // 절대 좌표 쓸 거니깐 전체화면 못하게

        // 배경 패널 설정
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null); // 절대 위치를 사용

        // 타이틀 이미지 라벨 추가
        ImageIcon titleIcon = new ImageIcon(getClass().getResource("/images/mainmenu/Just_Mini.png"));
        int titleWidth = 175;
        int titleHeight = 50;
        Image titleImage = titleIcon.getImage();
        Image scaledTitleImage = titleImage.getScaledInstance(titleWidth, titleHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledTitleIcon = new ImageIcon(scaledTitleImage);
        JLabel titleLabel = new JLabel(scaledTitleIcon);
        int titleX = (800 - titleWidth) / 2; // 중앙에 위치하도록
        int titleY = 100;
        titleLabel.setBounds(titleX, titleY, titleWidth, titleHeight);
        backgroundPanel.add(titleLabel);

        // 버튼 크기
        int buttonWidth = 170;
        int buttonHeight = 45;

        // dungeonButton 생성
        ImageIcon dungeonIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/dungeon_button.png"));
        ImageIcon dungeonRolloverIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/dungeon_button_selected.png"));
        ImageIcon dungeonPressedIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/dungeon_button_clicked.png"));

        // 버튼 이미지 스케일링
        Image dungeonImage = dungeonIcon.getImage();
        Image scaledDungeonImage = dungeonImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledDungeonIcon = new ImageIcon(scaledDungeonImage);

        Image dungeonRolloverImage = dungeonRolloverIcon.getImage();
        Image scaledDungeonRolloverImage = dungeonRolloverImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledDungeonRolloverIcon = new ImageIcon(scaledDungeonRolloverImage);

        Image dungeonPressedImage = dungeonPressedIcon.getImage();
        Image scaledDungeonPressedImage = dungeonPressedImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledDungeonPressedIcon = new ImageIcon(scaledDungeonPressedImage);

        JButton dungeonButton = new JButton();
        dungeonButton.setIcon(scaledDungeonIcon);
        dungeonButton.setRolloverIcon(scaledDungeonRolloverIcon);
        dungeonButton.setPressedIcon(scaledDungeonPressedIcon);
        dungeonButton.setBorderPainted(false);
        dungeonButton.setContentAreaFilled(false);
        dungeonButton.setFocusPainted(false);
        dungeonButton.setOpaque(false);

        // dodgeButton 생성
        ImageIcon dodgeIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/dodge_button.png"));
        ImageIcon dodgeRolloverIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/dodge_button_selected.png"));
        ImageIcon dodgePressedIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/dodge_button_clicked.png"));

        Image dodgeImage = dodgeIcon.getImage();
        Image scaledDodgeImage = dodgeImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledDodgeIcon = new ImageIcon(scaledDodgeImage);

        Image dodgeRolloverImage = dodgeRolloverIcon.getImage();
        Image scaledDodgeRolloverImage = dodgeRolloverImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledDodgeRolloverIcon = new ImageIcon(scaledDodgeRolloverImage);

        Image dodgePressedImage = dodgePressedIcon.getImage();
        Image scaledDodgePressedImage = dodgePressedImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledDodgePressedIcon = new ImageIcon(scaledDodgePressedImage);

        JButton dodgeButton = new JButton();
        dodgeButton.setIcon(scaledDodgeIcon);
        dodgeButton.setRolloverIcon(scaledDodgeRolloverIcon);
        dodgeButton.setPressedIcon(scaledDodgePressedIcon);
        dodgeButton.setBorderPainted(false);
        dodgeButton.setContentAreaFilled(false);
        dodgeButton.setFocusPainted(false);
        dodgeButton.setOpaque(false);

        // pangpang 버튼
        ImageIcon pangpangIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/pangpang_button.png"));
        ImageIcon pangpangRolloverIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/pangpang_button_selected.png"));
        ImageIcon pangpangPressedIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/pangpang_button_clicked.png"));

        Image pangpangImage = pangpangIcon.getImage();
        Image scaledPangPangImage = pangpangImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledPangPangIcon = new ImageIcon(scaledPangPangImage);

        Image pangpangRolloverImage = pangpangRolloverIcon.getImage();
        Image scaledPangPangRolloverImage = pangpangRolloverImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledPangPangRolloverIcon = new ImageIcon(scaledPangPangRolloverImage);

        Image pangpangPressedImage = pangpangPressedIcon.getImage();
        Image scaledPangPangPressedImage = pangpangPressedImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledPangPangPressedIcon = new ImageIcon(scaledPangPangPressedImage);

        JButton pangpangButton = new JButton();
        pangpangButton.setIcon(scaledPangPangIcon);
        pangpangButton.setRolloverIcon(scaledPangPangRolloverIcon);
        pangpangButton.setPressedIcon(scaledPangPangPressedIcon);
        pangpangButton.setBorderPainted(false);
        pangpangButton.setContentAreaFilled(false);
        pangpangButton.setFocusPainted(false);
        pangpangButton.setOpaque(false);

        // panda 버튼
        ImageIcon pandaIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/panda_button.png"));
        ImageIcon pandaRolloverIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/panda_button_selected.png"));
        ImageIcon pandaPressedIcon = new ImageIcon(getClass().getResource("/images/mainmenu/buttons/panda_button_clicked.png"));

        Image pandaImage = pandaIcon.getImage();
        Image scaledPandaImage = pandaImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledPandaIcon = new ImageIcon(scaledPandaImage);

        Image pandaRolloverImage = pandaRolloverIcon.getImage();
        Image scaledPandaRolloverImage = pandaRolloverImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledPandaRolloverIcon = new ImageIcon(scaledPandaRolloverImage);

        Image pandaPressedImage = pandaPressedIcon.getImage();
        Image scaledPandaPressedImage = pandaPressedImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledPandaPressedIcon = new ImageIcon(scaledPandaPressedImage);

        JButton pandaButton = new JButton();
        pandaButton.setIcon(scaledPandaIcon);
        pandaButton.setRolloverIcon(scaledPandaRolloverIcon);
        pandaButton.setPressedIcon(scaledPandaPressedIcon);
        pandaButton.setBorderPainted(false);
        pandaButton.setContentAreaFilled(false);
        pandaButton.setFocusPainted(false);
        pandaButton.setOpaque(false);

        // 근데 이 버튼 만드는 거 좀 더 똑똑하게 어떻게 못하나? 보일러 플레이트 너무 꼴보기 싫네...
        // 그리고 awt 이미지 스케일링 이것도 좀 별로네. 원본은 픽셀아트 느낌인데 다 뭉개놔서 이상해진다

        // 버튼 위치 설정
        int gap = 20; // 버튼 간 간격
        int totalButtonWidth = buttonWidth * 4 + gap * 3; // 애셋 그대로 썼더니 버튼마다 애매하게 크기 달라서 정렬 좀 안맞네. 직접 만들걸
        int startX = (800 - totalButtonWidth) / 2 - 7; // 버튼 시작 X 좌표. -7ㅋㅋ 총 28px정도 차이나서 28/4.
        int y = 400; // 버튼 Y 좌표

        dungeonButton.setBounds(startX, y, buttonWidth, buttonHeight);
        dodgeButton.setBounds(startX + (buttonWidth + gap), y, buttonWidth, buttonHeight);
        pangpangButton.setBounds(startX + 2 * (buttonWidth + gap), y, buttonWidth, buttonHeight);
        pandaButton.setBounds(startX + 3 * (buttonWidth + gap), y, buttonWidth, buttonHeight);

        // 버튼을 배경 패널에 추가
        backgroundPanel.add(dungeonButton);
        backgroundPanel.add(dodgeButton);
        backgroundPanel.add(pangpangButton);
        backgroundPanel.add(pandaButton);

        // 버튼 액션 리스너 추가
        dungeonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("/sounds/button1.wav");
                // Mini Dungeon 게임 실행
                new MiniDungeonGame();
                dispose(); // 현재 메인 화면 닫기
            }
        });

        dodgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("/sounds/button1.wav");
                new MiniDodge();
                dispose();
            }
        });

        pangpangButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("/sounds/button1.wav");
                new MiniPangPang();
                dispose();
            }
        });

        pandaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("/sounds/button1.wav");
                new MiniPanda();
                dispose();
            }
        });

        // 배경 패널을 프레임에 추가
        setContentPane(backgroundPanel);
        setVisible(true);
        SoundPlayer.playBackgroundMusic("/sounds/main_theme.wav");
        System.out.println("JustMiniMain initialized.");
        System.out.println();
    }

    public static void main(String[] args) {
        new JustMiniMain();
    }
}
