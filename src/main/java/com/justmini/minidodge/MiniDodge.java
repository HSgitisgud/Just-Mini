package com.justmini.minidodge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MiniDodge extends JFrame implements ActionListener {
	private int playerX, playerY; // 플레이어 위치 변수
	private Timer timer;
	private ArrayList<Shuriken> shurikens;
	private boolean gameOver = false;
	private long startTime;

	private Image playerImage;
	private boolean movingUp = false, movingDown = false, movingLeft = false, movingRight = false;

	private JButton startButton;
	private JPanel instructionPanel;
	private GamePanel gamePanel;
	private PatternManager patternManager;

	public MiniDodge() {
		setTitle("Bamboo Master - Shuriken Dodge");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		shurikens = new ArrayList<>();
		patternManager = new PatternManager(shurikens, getWidth(), getHeight());

		instructionPanel = createInstructionPanel();
		add(instructionPanel);

		gamePanel = new GamePanel();
		gamePanel.setBounds(0, 0, 600, 600);
		add(gamePanel);

		// 플레이어 이미지 로드
		playerImage = new ImageIcon(getClass().getResource("/images/minidodge/player.png")).getImage();

		timer = new Timer(1000 / 60, this);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				// 키 입력에 따른 이동 방향 설정
				if (key == KeyEvent.VK_LEFT)
					movingLeft = true;
				if (key == KeyEvent.VK_RIGHT)
					movingRight = true;
				if (key == KeyEvent.VK_UP)
					movingUp = true;
				if (key == KeyEvent.VK_DOWN)
					movingDown = true;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				// 키 해제에 따라 이동 방향 해제
				if (key == KeyEvent.VK_LEFT)
					movingLeft = false;
				if (key == KeyEvent.VK_RIGHT)
					movingRight = false;
				if (key == KeyEvent.VK_UP)
					movingUp = false;
				if (key == KeyEvent.VK_DOWN)
					movingDown = false;
			}
		});

		setFocusable(true);
		setVisible(true);

		startTime = System.currentTimeMillis(); // 게임 시작 시간 기록
	}

	// 게임 설명 패널 생성 메서드
	private JPanel createInstructionPanel() {
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 600, 600);
		panel.setLayout(null);

		JLabel instructionLabel = new JLabel("<html>게임 설명: 수리검을 피하면서 최대한 오래 생존하세요!<br>방향키로 플레이어를 조작합니다.</html>",
				SwingConstants.CENTER);
		instructionLabel.setBounds(50, 100, 500, 100);
		instructionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		panel.add(instructionLabel);

		startButton = new JButton("게임 시작");
		startButton.setBounds(250, 300, 100, 50);
		startButton.addActionListener(e -> startGame());
		panel.add(startButton);

		return panel;
	}

	// 게임 시작 설정 메서드
	private void startGame() {
		instructionPanel.setVisible(false);
		gamePanel.hideGameOver(); // 게임 오버 UI 숨기기
		shurikens.clear();
		gameOver = false;

		// 플레이어 위치를 중앙으로 설정
		playerX = (getWidth() - 30) / 2;
		playerY = (getHeight() - 30) / 2;

		startTime = System.currentTimeMillis();
		timer.start();
		requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameOver)
			return;

		// 패턴 및 랜덤 수리검 생성 호출
		patternManager.spawnPattern(playerX, playerY);
		patternManager.spawnRandomShuriken(playerX, playerY);

		// 플레이어 이동 처리
		if (movingLeft)
			playerX -= 3;
		if (movingRight)
			playerX += 3;
		if (movingUp)
			playerY -= 3;
		if (movingDown)
			playerY += 3;

		// 플레이어 위치가 화면 밖으로 나가지 않도록 제한
		playerX = Math.max(0, Math.min(getWidth() - 30, playerX));
		playerY = Math.max(0, Math.min(getHeight() - 30, playerY));

		// 수리검 충돌 및 화면 밖으로 나간 수리검 제거
		for (int i = 0; i < shurikens.size(); i++) {
			Shuriken shuriken = shurikens.get(i);
			shuriken.move();
			if (shuriken.isOffScreen(getWidth(), getHeight())) {
				shurikens.remove(i);
				i--;
			}
			if (new Rectangle(playerX + 8, playerY + 8, 8, 8).intersects(shuriken.getRectangle())) {
				endGame(System.currentTimeMillis()); // 충돌 시 게임 종료
			}
		}

		gamePanel.repaint();
	}

	// 게임 종료 메서드
	private void endGame(long currentTime) {
		gameOver = true;
		timer.stop(); // 타이머 중지
		int finalScore = (int) ((currentTime - startTime) / 1000.0 * 100);
		gamePanel.displayGameOver(finalScore); // 초기 화면 설명 패널 표시
	}

	public static void main(String[] args) {
		MiniDodge game = new MiniDodge();
		game.instructionPanel.setVisible(true);
	}

	private class GamePanel extends JPanel {
		private JButton retryButton;
		private JLabel gameOverLabel;
		private JLabel scoreLabel;

		public GamePanel() {
			setLayout(null);

			gameOverLabel = new JLabel("Game Over!", SwingConstants.CENTER);
			gameOverLabel.setFont(new Font("Arial", Font.BOLD, 24));
			gameOverLabel.setBounds(150, 100, 300, 50);
			gameOverLabel.setVisible(false);
			add(gameOverLabel);

			scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
			scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
			scoreLabel.setBounds(150, 180, 300, 50);
			scoreLabel.setVisible(false); // 점수 표시
			add(scoreLabel);

			retryButton = new JButton("다시하기");
			retryButton.setBounds(250, 300, 100, 50);
			retryButton.setVisible(false); // 게임 오버 시 나타나는 버튼
			retryButton.addActionListener(e -> startGame()); // 다시 시작 클릭 시 startGame 호출
			add(retryButton);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (instructionPanel.isVisible()) // 설명 패널이 보이면 게임 화면을 그리지 않음
				return;

			long currentTime = System.currentTimeMillis();
			double elapsedTime = (currentTime - startTime) / 1000.0; // 경과 시간 계산
			int score = (int) (elapsedTime * 100); // 경과 시간 기반으로 점수 계산

			if (!gameOver) {
				g.setFont(new Font("Arial", Font.BOLD, 18));
				g.setColor(Color.BLACK);
				g.drawString("Score: " + score, getWidth() - 120, 50); // 화면에 점수 표시

				g.drawImage(playerImage, playerX, playerY, 30, 30, null); // 플레이어 이미지 그리기

				for (Shuriken shuriken : shurikens) {
					shuriken.draw(g); // 수리검 그리기
				}
			}
		}

		// 게임 오버 화면 표시 메서드
		public void displayGameOver(int score) {
			gameOverLabel.setVisible(true);
			scoreLabel.setText("Score: " + score);
			scoreLabel.setVisible(true);
			retryButton.setVisible(true);
		}

		// 게임 오버 화면 숨기기 메서드
		public void hideGameOver() {
			gameOverLabel.setVisible(false);
			scoreLabel.setVisible(false);
			retryButton.setVisible(false);
		}
	}
}
