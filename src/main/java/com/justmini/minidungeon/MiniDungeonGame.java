package com.justmini.minidungeon;

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MiniDungeonGame().setVisible(true);
        });
    }

    public MiniDungeonGame() {
        // 튜토리얼 UI 시작
        initTutorialUI();
    }

    private void initTutorialUI() {
        // 튜토리얼 패널 추가
        tutorialPanel = new TutorialPanel(this);
        tutorialPanel.setPreferredSize(new Dimension(1350, 705));
        setContentPane(tutorialPanel); // Content Pane을 튜토리얼 패널로 설정

        setTitle("MiniDungeon");
        setSize(1350, 705);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 프레임 포커스 설정
        setFocusable(true);
        requestFocusInWindow();
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
        // 도대체 왜 정렬이 안되냐고!!!
        // rightPanel.add(Box.createHorizontalStrut(60)); // 왜 스탯이 움직이는데 미니맵이 움직이라고!!!
        // 박스 레이아웃 구성이 대체 어떻게 돼있는 거야

        miniMapLabel = new JLabel("Map");
        rightPanel.add(miniMapLabel);

        miniMapPanel = new MiniMapPanel(null);
        miniMapPanel.setPreferredSize(new Dimension(120, 120));
        rightPanel.add(miniMapPanel);

        add(rightPanel, BorderLayout.EAST);

        setTitle("MiniDungeon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 705);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void startGame() {
        // 튜토리얼 패널 제거
        remove(tutorialPanel);

        // 게임 UI 초기화
        initGameUI();

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
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gameLogic.handlePlayerInput(e.getKeyCode());
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    // UI 업데이트 메서드들
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
