package com.justmini.minidungeon;

import com.justmini.main.JustMiniMain;
import com.justmini.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MiniDungeonGame extends JFrame {

    private GameLogic gameLogic;
    private Player player;

    // UI Components
    private JLabel roomNameLabel;
    private JTextArea combatLogTextArea;
    private JPanel gamePanel; // 현재 Room을 표시
    private JLabel playerStatsLabel;
    private JList<String> itemList;
    private MiniMapPanel miniMapPanel; // 3x3 그리드 맵
    private JLabel miniMapLabel;
    private TutorialPanel tutorialPanel;
    private KeyAdapter keyAdapter;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MiniDungeonGame().setVisible(true);
        });
    }

    public MiniDungeonGame() {
        System.out.println("Initializing MiniDungeonGame...");
        // 튜토리얼 UI 시작
        initTutorialUI();
        setVisible(true);
        System.out.println("MiniDungeonGame initialized.");
        System.out.println();
    }

    private void initTutorialUI() {
        // 튜토리얼 패널 추가
        tutorialPanel = new TutorialPanel(this);
        tutorialPanel.setPreferredSize(new Dimension(1350, 705));
        setContentPane(tutorialPanel); // Content Pane을 튜토리얼 패널로 설정

        setTitle("Mini Dungeon");
        setSize(1350, 705);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // 프레임 포커스 설정
        setFocusable(true);
        requestFocusInWindow();

        // 창 닫기 이벤트 처리 추가
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 현재 게임 창 닫기
                dispose();

                // 메인 화면 표시
                new JustMiniMain();
            }
        });
    }

    private void initGameUI() {
        // 레이아웃 설정
        setLayout(new BorderLayout());

        // 상단 패널: 방 이름, 전투 로그 등
        JPanel topPanel = new JPanel(new BorderLayout());
        roomNameLabel = new JLabel("Room number");
        topPanel.add(roomNameLabel, BorderLayout.WEST);

        combatLogTextArea = new JTextArea(5, 30);
        combatLogTextArea.setEditable(false);
        combatLogTextArea.setFocusable(false);
        JScrollPane logScrollPane = new JScrollPane(combatLogTextArea);
        topPanel.add(logScrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // 중앙 패널: 게임 화면
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.BLACK); // 배경색 설정
        add(gamePanel, BorderLayout.CENTER);

        // 우측 패널: 플레이어 정보, 아이템 리스트, 미니맵
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(250, 800));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        rightPanel.add(Box.createVerticalStrut(16)); // 16 픽셀의 수직 간격 추가

        playerStatsLabel = new JLabel("Player stats");
        rightPanel.add(playerStatsLabel);

        rightPanel.add(Box.createVerticalStrut(16)); // 16 픽셀의 수직 간격 추가

        itemList = new JList<>();
        itemList.setFocusable(false);
        JScrollPane itemScrollPane = new JScrollPane(itemList);
        itemScrollPane.setPreferredSize(new Dimension(200, 150));
        rightPanel.add(itemScrollPane);

        rightPanel.add(Box.createVerticalStrut(16)); // 16 픽셀의 수직 간격 추가

        miniMapLabel = new JLabel("Map");
        rightPanel.add(miniMapLabel);

        miniMapPanel = new MiniMapPanel(null);
        miniMapPanel.setPreferredSize(new Dimension(120, 120));
        rightPanel.add(miniMapPanel);

        add(rightPanel, BorderLayout.EAST);

        setTitle("Mini Dungeon");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1350, 705);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void startGame() {
        // 튜토리얼 패널 제거
        remove(tutorialPanel);

        // 게임 UI 초기화
        initGameUI();

        SoundPlayer.playBackgroundMusic("/sounds/ruins.wav");

        // 플레이어 및 게임 로직 초기화
        player = new Player();
        gameLogic = new GameLogic(player, this);

        // 미니맵 패널에 게임 로직 설정
        miniMapPanel.setGameLogic(gameLogic);

        // 게임 화면에 현재 방의 패널 추가
        gamePanel.add(gameLogic.getCurrentRoom().getRoomPanel(), BorderLayout.CENTER);

        updateRoomName(gameLogic.getCurrentRoom().getName());
        updatePlayerStats();
        updateItemList();

        // 게임 화면 갱신
        revalidate();
        repaint();

        // 게임 키 이벤트 리스너 추가
        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gameLogic.handlePlayerInput(e.getKeyCode());
            }
        };
        addKeyListener(keyAdapter);
        setFocusable(true);
        requestFocusInWindow();
    }

    public void stopGame() {
        if (keyAdapter != null) {
            removeKeyListener(keyAdapter);
            keyAdapter = null;
        }
    }

    // UI 업데이트 메소드들
    public void updateRoomName(String roomName) {
        roomNameLabel.setText("Room number: " + roomName + " ");
    }

    public void updateCombatLog(String message) {
        combatLogTextArea.append(message + "\n");
    }

    public void updatePlayerStats() {
        playerStatsLabel.setText(
                "<html>Level: " + player.getLevel() +
                        "<br>EXP: " + player.getExp() + "/" + player.getExpToLevelUp() +
                        "<br>HP: " + player.getHp() + "/" + player.getMaxHp() +
                        "<br>ATK: " + player.getAtk() +
                        "<br>DEF: " + player.getDef() + "</html>"
        );
    }

    public void updateItemList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        List<Item> items = player.getBag().getItems();
        for (int i = 0; i < items.size(); i++) {
            String itemName = items.get(i).getName();
            model.addElement("[" + i + "] " + itemName);
        }
        itemList.setModel(model);
    }


    public void updateMiniMap() {
        miniMapPanel.repaint();
    }

    public void setGamePanel(JLayeredPane roomPanel) {
        gamePanel.removeAll();
        gamePanel.add(roomPanel, BorderLayout.CENTER);
        gamePanel.revalidate();
        gamePanel.repaint();
    }
}
