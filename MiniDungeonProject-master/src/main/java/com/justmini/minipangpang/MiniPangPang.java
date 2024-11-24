package com.justmini.minipangpang;

import com.justmini.main.JustMiniMain;
import com.justmini.util.SoundPlayer;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class MiniPangPang extends JFrame {
    private static final int GRID_SIZE = 6;
    private static final String[] IMAGE_PATHS = {
            "/images/minipangpang/Jelly_1.png",
            "/images/minipangpang/Jelly_2.png",
            "/images/minipangpang/Jelly_3.png",
            "/images/minipangpang/Jelly_4.png",
            "/images/minipangpang/Jelly_5.png",
            "/images/minipangpang/Jelly_6.png"
    };
    private static final String SMOKE_IMAGE_PATH = "/images/minipangpang/smoke.png";
    private static final int BUTTON_SIZE = 80; // 원하는 버튼 크기로 설정

    private JButton[][] buttons = new JButton[GRID_SIZE][GRID_SIZE];
    private ImageIcon[] icons = new ImageIcon[IMAGE_PATHS.length];
    private ImageIcon smokeIcon;

    // 현재 선택된 버튼과 그 위치
    public JButton selectedButton = null;
    public int selectedRow = -1;
    public int selectedCol = -1;
    
    // 점수와 관련된 필드
    private int score = 0;
    private JLabel scoreLabel;
    
    // 타이머 관련된 필드
    private JLabel timerLabel;
    private int timerCount = 60; // 타이머 시작 시간 (초단위)
    private Timer timer; // 타이머 객체
    
    private boolean gameStarted = false; // 게임 시작 여부
    
    private JPanel instructionPanel; // 게임 설명 패널

    // 게임 창 초기화
    public MiniPangPang() {
        setTitle("Mini PangPang");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout()); // BorderLayout 설정
        setLocationRelativeTo(null);
        setResizable(false);

        loadIcons(); // 젤리 이미지 로드
        loadSmokeIcon(); // 연기 이미지 로드

        instructionPanel = createInstructionPanel(); //설명 패널 생성
        add(instructionPanel); // 설명 패널 추가
        setVisible(true); // 창 표시

        // 창 닫기 이벤트 처리 추가
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (gameStarted) {
                    timer.stop(); // 게임 중인 경우 타이머 중지
                }

                // 현재 게임 창 닫기
                dispose();

                // 메인 화면 표시
                new JustMiniMain();
            }
        });
    }

    // 젤리 아이콘 로드
    private void loadIcons() {
        try {
            for (int i = 0; i < IMAGE_PATHS.length; i++) {
                URL imageUrl = getClass().getResource(IMAGE_PATHS[i]);
                if (imageUrl != null) {
                    ImageIcon originalIcon = new ImageIcon(imageUrl);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH);
                    icons[i] = new ImageIcon(scaledImage);
                } else {
                    System.out.println("Image not found: " + IMAGE_PATHS[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "이미지를 불러오는 데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 연기 아이콘 로드
    private void loadSmokeIcon() {
        try {
            URL imageUrl = getClass().getResource(SMOKE_IMAGE_PATH);
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH);
                smokeIcon = new ImageIcon(scaledImage);
            } else {
                System.out.println("Image not found: " + SMOKE_IMAGE_PATH);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "연기 이미지를 불러오는 데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 점수 라벨을 업데이트하는 메서드
    public void updateScoreLabel() {
        scoreLabel.setText("Score: " + score + " ");
    }

    // 아이콘을 랜덤으로 가져오는 메서드
    public ImageIcon getRandomIcon() {
        return icons[(int) (Math.random() * icons.length)];
    }

    // 그리드를 리필하는 메서드
    public void refillGrid() {
        for (int j = 0; j < GRID_SIZE; j++) { // 열 단위로 처리 (비어있는 slot을 채우고 아이콘을 이동한다.)
            int emptySlots = 0;
            for (int i = GRID_SIZE - 1; i >= 0; i--) {
                if (!buttons[i][j].isEnabled() || buttons[i][j].getIcon() == null) {
                    emptySlots++;
                } else if (emptySlots > 0) {
                    buttons[i + emptySlots][j].setIcon(buttons[i][j].getIcon());
                    buttons[i + emptySlots][j].setEnabled(true);
                    buttons[i][j].setIcon(null);
                    buttons[i][j].setEnabled(false);
                }
            }
            
            // 제일 위쪽 비어있는 슬롯에 새 아이콘을 추가
            for (int i = 0; i < emptySlots; i++) {
                buttons[i][j].setIcon(getRandomIcon());
                buttons[i][j].setEnabled(true);
            }
        }

        // 리필 후 매칭 검사
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkMatches();
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // 전체 그리드에서 매칭을 검사하는 메서드
    public void checkMatches() {
        boolean isMatched = false;
        Set<JButton> matchedButtonsSet = new HashSet<>();

        // 가로 매칭 검사
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE - 2; j++) {
                Icon icon1 = buttons[i][j].getIcon();
                Icon icon2 = buttons[i][j + 1].getIcon();
                Icon icon3 = buttons[i][j + 2].getIcon();

                if (icon1 != null && icon1.equals(icon2) && icon2.equals(icon3) &&
                        buttons[i][j].isEnabled() && buttons[i][j + 1].isEnabled() && buttons[i][j + 2].isEnabled()) {
                    matchedButtonsSet.add(buttons[i][j]);
                    matchedButtonsSet.add(buttons[i][j + 1]);
                    matchedButtonsSet.add(buttons[i][j + 2]);
                    isMatched = true;
                }
            }
        }

        // 세로 매칭 검사
        for (int j = 0; j < GRID_SIZE; j++) {
            for (int i = 0; i < GRID_SIZE - 2; i++) {
                Icon icon1 = buttons[i][j].getIcon();
                Icon icon2 = buttons[i + 1][j].getIcon();
                Icon icon3 = buttons[i + 2][j].getIcon();

                if (icon1 != null && icon1.equals(icon2) && icon2.equals(icon3) &&
                        buttons[i][j].isEnabled() && buttons[i + 1][j].isEnabled() && buttons[i + 2][j].isEnabled()) {
                    matchedButtonsSet.add(buttons[i][j]);
                    matchedButtonsSet.add(buttons[i + 1][j]);
                    matchedButtonsSet.add(buttons[i + 2][j]);
                    isMatched = true;
                }
            }
        }

        if (isMatched) {
            // 주먹구구식 사운드 재생 미쳤다...
            // yay 하나만 해시맵에 넣으면 해당 사운드 재생이 완전히 완료되지 않은 상태에서 참조가 일어날 때 frame 0부터 다시 재생하니깐 씹힘.
            // 그래서 같은 파일을 다른 이름으로 두개 넣었는데 분명히 이것보다 더 좋은 해결책이 있을듯...
            SoundPlayer.playSound("/sounds/yay1.wav");
            System.out.println("check for sound1");
            List<JButton> matchedButtons = new ArrayList<>(matchedButtonsSet);

            // 매칭된 버튼의 아이콘을 연기 이미지로 변경
            for (JButton button : matchedButtons) {
                button.setIcon(smokeIcon);
            }

            // 일정 시간 후에 버튼 비활성화 및 그리드 리필
            Timer timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (JButton button : matchedButtons) {
                        button.setEnabled(false);
                        button.setIcon(null);
                    }
                    score += matchedButtons.size();
                    updateScoreLabel();
                    refillGrid();
                    ((Timer) e.getSource()).stop();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    // 교환된 위치에서만 매칭을 검사하는 메서드
    public boolean checkMatchesAtPositions(int row1, int col1, int row2, int col2) {
        boolean isMatched = false;
        Set<JButton> matchedButtonsSet = new HashSet<>();

        // 교환된 두 위치 검사
        int[][] positions = {{row1, col1}, {row2, col2}};

        for (int[] pos : positions) {
            int i = pos[0];
            int j = pos[1];
            Icon icon = buttons[i][j].getIcon();

            if (icon == null) continue;

            // 가로 매칭 검사
            List<JButton> horizontalMatch = new ArrayList<>();
            horizontalMatch.add(buttons[i][j]);

            // 왼쪽 검사
            int k = j - 1;
            while (k >= 0 && buttons[i][k].getIcon() != null && buttons[i][k].getIcon().equals(icon)) {
                horizontalMatch.add(buttons[i][k]);
                k--;
            }
            // 오른쪽 검사
            k = j + 1;
            while (k < GRID_SIZE && buttons[i][k].getIcon() != null && buttons[i][k].getIcon().equals(icon)) {
                horizontalMatch.add(buttons[i][k]);
                k++;
            }
            if (horizontalMatch.size() >= 3) {
                matchedButtonsSet.addAll(horizontalMatch);
                isMatched = true;
            }

            // 세로 매칭 검사
            List<JButton> verticalMatch = new ArrayList<>();
            verticalMatch.add(buttons[i][j]);

            // 위쪽 검사
            k = i - 1;
            while (k >= 0 && buttons[k][j].getIcon() != null && buttons[k][j].getIcon().equals(icon)) {
                verticalMatch.add(buttons[k][j]);
                k--;
            }
            // 아래쪽 검사
            k = i + 1;
            while (k < GRID_SIZE && buttons[k][j].getIcon() != null && buttons[k][j].getIcon().equals(icon)) {
                verticalMatch.add(buttons[k][j]);
                k++;
            }
            if (verticalMatch.size() >= 3) {
                matchedButtonsSet.addAll(verticalMatch);
                isMatched = true;
            }
        }

        if (isMatched) {
            SoundPlayer.playSound("/sounds/yay.wav");
            System.out.println("check for sound");
            List<JButton> matchedButtons = new ArrayList<>(matchedButtonsSet);

            // 매칭된 버튼의 아이콘을 연기 이미지로 변경
            for (JButton button : matchedButtons) {
                button.setIcon(smokeIcon);
            }

            // 일정 시간 후에 버튼 비활성화 및 그리드 리필
            Timer timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (JButton button : matchedButtons) {
                        button.setEnabled(false);
                        button.setIcon(null);
                    }
                    score += matchedButtons.size();
                    updateScoreLabel();
                    refillGrid();
                    ((Timer) e.getSource()).stop();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }

        return isMatched;
    }

    // 게임 설명 패널 생성 메소드
    private JPanel createInstructionPanel() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 600, 600);
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.BLACK);

        // 이미지 로드 및 크기 조정
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/minipangpang/instruction.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // 이미지 JLabel 생성 및 중앙 배치
        JLabel instructionImageLabel = new JLabel(scaledIcon);
        instructionImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionImageLabel.setVerticalAlignment(SwingConstants.CENTER);

        // 텍스트 JLabel 생성
        JLabel instructionLabel = new JLabel(
                "<html><div style='text-align: center;'>Save the jellies from jelly eater!<br><br>" +
                        "Place three jellies vertically or horizontally to release the jellies!<br>" +
                        "You can only swap the place of adjacent jellies in four directions.<br>" +
                        "Top, Bottom, Left, Right.<br><br>" +
                        "(Press Enter to start the game)</div></html>",
                SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setVerticalAlignment(SwingConstants.TOP);

        // 텍스트 패널 생성
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.BLACK);
        textPanel.setLayout(new BorderLayout());
        textPanel.add(instructionLabel, BorderLayout.CENTER);
        textPanel.setPreferredSize(new Dimension(600, 200));

        // 패널에 컴포넌트 추가
        panel.add(instructionImageLabel, BorderLayout.CENTER);
        panel.add(textPanel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!gameStarted) {
                        startGame();
                        panel.setVisible(false);
                        gameStarted = true;
                    } else {
                        return;
                    }
                }
            }
        });
        // 패널 생성 및 이미지와 텍스트 추가
        return panel;
    }

    private void startGame() {
        initializeGame(); // 게임 초기화
    }

    // 게임 화면 설정
    private void initializeGame() {
        SoundPlayer.playBackgroundMusic("/sounds/jelly_theme.wav");

        JPanel topPanel = new JPanel();
        scoreLabel = new JLabel("Score: " + score + " ");
        timerLabel = new JLabel("Time: " + timerCount + "  ");

        JButton restartButton = new JButton("End session");
        restartButton.addActionListener(e -> endGame());

        topPanel.add(scoreLabel);
        topPanel.add(timerLabel);
        topPanel.add(restartButton);

        add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        gridPanel.setBackground(Color.LIGHT_GRAY);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                button.setMinimumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                button.setMaximumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

                button.setBackground(Color.WHITE);
                button.setOpaque(true);
                button.setBorderPainted(false);
                button.setIcon(getRandomIcon());
                button.addActionListener(new ButtonClickListener(button, i, j, this));
                buttons[i][j] = button;
                gridPanel.add(button);
            }
        }

        add(gridPanel, BorderLayout.CENTER);
        startTimer(); // 타이머 시작
        revalidate();
        repaint();
    }

    // 진행 시간
    private void startTimer() {
        // javax.swing.Timer 사용
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerCount--;
                timerLabel.setText("Time: " + timerCount + "  ");
                if (timerCount <= 0) {
                    endGame();  // 게임 종료 처리
                }
            }
        });
        timer.start();
    }

    private void endGame() {
        SoundPlayer.playSound("/sounds/notification.wav");
        timer.stop();
        JOptionPane.showMessageDialog(MiniPangPang.this, "Game Over! Your score: " + score);

        // 그리드 비활성화
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setEnabled(false);
            }
        }

        // 종료 후 재시작할 수 있는 버튼 제공
        int restartOption = JOptionPane.showConfirmDialog(this, "The game is over. Do you want to restart?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (restartOption == JOptionPane.YES_OPTION) {
            resetGame();  // 게임을 재설정하는 메서드
        } else {
            dispose();  // 게임을 종료
            new JustMiniMain();
        }
    }

    private void resetGame() {
        SoundPlayer.playBackgroundMusic("/sounds/jelly_theme.wav");

        // 게임 상태 초기화
        score = 0;
        timerCount = 60;
        scoreLabel.setText("Score: " + score + " ");
        timerLabel.setText("Time: " + timerCount + "  ");

        // 버튼을 다시 활성화하고 새로운 아이콘으로 초기화
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setEnabled(true);
                buttons[i][j].setIcon(getRandomIcon());
            }
        }

        // 타이머 새로 시작
        startTimer();
    }

    // 일시정지 메소드
    private void pauseGame() {
        if (timer != null) {
            timer.stop();
        }
    }

    // 다시 시작 메소드
    private void resumeGame() {
        if (timer != null) {
            timer.start();
        }
    }

    public static void main(String[] args) {
        // UI 초기화 코드
        new MiniPangPang(); // JFrame 인스턴스 생성
    }
}
