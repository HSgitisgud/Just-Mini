package com.justmini.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.justmini.minidungeon.MiniDungeonGame;
import com.justmini.minidodge.MiniDodge;
// import com.justmini.minipangpang.MiniPangPangGame;
// import com.justmini.minipanda.MiniPandaGame;

public class JustMiniMain extends JFrame {
    public JustMiniMain() {
        System.out.println("JustMiniMain initializing");
        setTitle("Just Mini");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // 원하는 크기로 설정
        setLocationRelativeTo(null); // 화면 중앙에 표시

        // 배경 패널 설정
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null); // 절대 위치를 사용

        // 타이틀 라벨 추가
        JLabel titleLabel = new JLabel("Just Mini");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(200, 50, 400, 60); // 위치와 크기 설정
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(titleLabel);

        // 버튼 생성 및 추가
        JButton dungeonButton = new JButton("Mini Dungeon");
        JButton dodgeButton = new JButton("Mini Dodge");
        JButton pangpangButton = new JButton("Mini PangPang");
        JButton pandaButton = new JButton("Mini Panda");

        // 버튼 위치와 크기 설정
        int buttonWidth = 150;
        int buttonHeight = 50;
        int gap = 20; // 버튼 간 간격
        int startX = (800 - (buttonWidth * 4 + gap * 3)) / 2; // 버튼 시작 X 좌표
        int y = 400; // 버튼 Y 좌표

        dungeonButton.setBounds(startX, y, buttonWidth, buttonHeight);
        dodgeButton.setBounds(startX + (buttonWidth + gap), y, buttonWidth, buttonHeight);
        pangpangButton.setBounds(startX + 2 * (buttonWidth + gap), y, buttonWidth, buttonHeight);
        pandaButton.setBounds(startX + 3 * (buttonWidth + gap), y, buttonWidth, buttonHeight);

        backgroundPanel.add(dungeonButton);
        backgroundPanel.add(dodgeButton);
        backgroundPanel.add(pangpangButton);
        backgroundPanel.add(pandaButton);

        // 버튼 액션 리스너 추가
        dungeonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mini Dungeon 게임 실행
                new MiniDungeonGame();
                dispose(); // 현재 메인 화면 닫기
            }
        });

        dodgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mini Dodge 게임 실행
                new MiniDodge();
                dispose();
            }
        });

        pangpangButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mini PangPang 게임 실행
                // new MiniPangPangGame();
                dispose();
            }
        });

        pandaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mini Panda 게임 실행
                // new MiniPandaGame();
                dispose();
            }
        });

        // 배경 패널을 프레임에 추가
        setContentPane(backgroundPanel);
        setVisible(true);
        System.out.println("JustMiniMain initialized");
    }

    public static void main(String[] args) {
        new JustMiniMain();
    }
}
