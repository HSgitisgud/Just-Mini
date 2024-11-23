// 게임의 주요 로직과 화면 렌더링을 담당하는 패널.
// 팬더와 장애물, 아이템의 상태를 업데이트, 충돌 확인, 튜토리얼과 게임 플레이를 전환.

package com.justmini.minipanda;

import com.justmini.main.JustMiniMain;
import com.justmini.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    // 게임 화면 크기 설정
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;

    private Timer gameTimer; // 게임 루프를 실행하기 위한 타이머 (16ms 간격으로 호출)
    private Panda panda; // 플레이어 캐릭터인 팬더 객체
    private ArrayList<Obstacle> obstacles; // 장애물 객체 리스트
    private ArrayList<Bamboo> bamboos; // 대나무 아이템 리스트
    private ArrayList<PowerUp> powerUps; // 강화 아이템 리스트
    private ArrayList<HealthItem> healthItems; // 체력 회복 아이템 리스트
    private ArrayList<FallingObstacle> fallingObstacles; // 떨어지는 장애물 리스트
    private Background background; // 스크롤링 배경
    private Random random; // 난수 생성기

    // 게임 상태 변수
    private boolean isGameOver = false; // 게임 오버 상태 여부
    private int score = 0; // 플레이어의 점수
    private int health = 3; // 팬더의 현재 체력
    private final int maxHealth = 3; // 팬더의 최대 체력
    private boolean isInvincible = false; // 팬더가 무적 상태인지 여부
    private int invincibilityTimer = 0; // 무적 상태 지속 시간
    private int gameSpeed = 5; // 게임 진행 속도
    private int speedIncrementInterval = 50; // 50점마다 속도 증가

    // 아이템 및 장애물 생성 타이머
    private int obstacleSpawnTimer = 0;
    private int bambooSpawnTimer = 0;
    private int powerUpSpawnTimer = 0;
    private int healthItemSpawnTimer = 0;
    private int fallingObstacleSpawnTimer = 0;

    private Image heartImage; // 체력 표시를 위한 하트 이미지

    // 튜토리얼 관련 변수
    private boolean inTutorial = true; // 현재 튜토리얼 진행 여부
    private int tutorialSlide = 1; // 튜토리얼의 현재 슬라이드 번호
    private Image tutorialImage1, tutorialImage2, tutorialImage3; // 튜토리얼 이미지
    private String tutorialText1 = "Help the hungry baby panda collect bamboo!";
    private String tutorialText2 = "Press Spacebar to jump. Press twice for a double jump!";
    private String tutorialText2_1 = "You have to avoid frogs, chickens and rocks!";
    private String tutorialText3 = "Collect items to gain advantages or recover health.";

    // 생성자: 패널 초기화
    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT)); // 패널 크기 설정
        setBackground(Color.WHITE); // 배경색 설정
        setFocusable(true); // 패널이 키보드 입력을 받을 수 있도록 설정
        addKeyListener(this); // 키 이벤트 리스너 추가
        initGame(); // 게임 초기화 메서드 호출
    }

    // 게임 초기화
    private void initGame() {
        panda = new Panda(100, 504); // 팬더 생성 (x=100, y=504)
        obstacles = new ArrayList<>(); // 장애물 리스트 초기화
        bamboos = new ArrayList<>(); // 대나무 리스트 초기화
        powerUps = new ArrayList<>(); // 강화 아이템 리스트 초기화
        healthItems = new ArrayList<>(); // 체력 회복 아이템 리스트 초기화
        fallingObstacles = new ArrayList<>(); // 떨어지는 장애물 리스트 초기화
        background = new Background(); // 배경 초기화
        random = new Random(); // 난수 생성기 초기화

        // 상태 변수 초기화
        gameSpeed = 5;
        obstacleSpawnTimer = 0;
        bambooSpawnTimer = 0;
        powerUpSpawnTimer = 0;
        healthItemSpawnTimer = 0;
        fallingObstacleSpawnTimer = 0;
        score = 0;
        health = maxHealth;
        isInvincible = false;
        invincibilityTimer = 0;

        loadAssets(); // 리소스(이미지) 로드

        // 게임 타이머 설정 (16ms 간격으로 actionPerformed 호출)
        gameTimer = new Timer(16, this);
    }

    // 리소스 로드 메서드
    private void loadAssets() {
        // 체력을 나타내는 하트 이미지 로드
        ImageIcon heartIcon = new ImageIcon(getClass().getResource("/images/minipanda/heart.png"));
        heartImage = heartIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);

        // 튜토리얼 이미지 로드
        tutorialImage1 = new ImageIcon(getClass().getResource("/images/minipanda/tutorial1.jpg")).getImage();
        tutorialImage2 = new ImageIcon(getClass().getResource("/images/minipanda/tutorial2.png")).getImage();
        tutorialImage3 = new ImageIcon(getClass().getResource("/images/minipanda/tutorial3.png")).getImage();
    }

    // 게임 시작
    public void startGame() {
        gameTimer.start(); // 타이머 시작
        if (!inTutorial) { // 튜토리얼이 아닐 경우
            SoundPlayer.playBackgroundMusic("/sounds/panda2.wav"); // 배경 음악 재생
        }
    }

    // 게임 종료
    public void endGame() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop(); // 타이머 정지
        }
    }

    // 튜토리얼 시작
    public void startTutorial() {
        inTutorial = true; // 튜토리얼 모드 활성화
        tutorialSlide = 1; // 첫 번째 슬라이드로 시작
        repaint(); // 화면 갱신
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inTutorial) {
            // 튜토리얼 중에는 아무 작업도 하지 않음.
        } else if (!isGameOver) {
            panda.update(); // 팬더의 위치 및 상태 업데이트
            background.update(gameSpeed); // 배경 업데이트

            // 게임 요소 업데이트
            handleObstacles();
            handleBamboos();
            handlePowerUps();
            handleHealthItems();
            handleFallingObstacles();
            checkCollisions();
            updateGameSpeed();

            // 아이템 및 장애물 생성 타이머 증가
            obstacleSpawnTimer++;
            bambooSpawnTimer++;
            powerUpSpawnTimer++;
            healthItemSpawnTimer++;
            fallingObstacleSpawnTimer++;

            // 무적 상태 처리
            if (invincibilityTimer > 0) {
                invincibilityTimer--;
                if (invincibilityTimer == 0) {
                    isInvincible = false; // 무적 상태 해제
                    SoundPlayer.playBackgroundMusic("/sounds/panda2.wav");
                }
            }

            // 아이템 및 장애물 생성 조건 확인
            if (obstacleSpawnTimer >= random.nextInt(30) + 40) {
                spawnObstacle();
                obstacleSpawnTimer = 0;
            }

            if (bambooSpawnTimer >= random.nextInt(50) + 100) {
                spawnBamboo();
                bambooSpawnTimer = 0;
            }

            if (powerUpSpawnTimer >= random.nextInt(600) + 600) {
                spawnPowerUp();
                powerUpSpawnTimer = 0;
            }

            if (healthItemSpawnTimer >= random.nextInt(300) + 300) {
                spawnHealthItem();
                healthItemSpawnTimer = 0;
            }

            if (fallingObstacleSpawnTimer >= random.nextInt(100) + 200) {
                spawnFallingObstacle();
                fallingObstacleSpawnTimer = 0;
            }
        }

        repaint(); // 화면 다시 그리기
    }

    private void spawnObstacle() {
        int x = PANEL_WIDTH;
        int y = 504; // Ground level

        // 장애물 유형을 랜덤으로 선택
        int obstacleType = random.nextInt(2); // 0 or 1
        Obstacle obstacle;
        if (obstacleType == 0) {
            obstacle = new Obstacle(x, y, gameSpeed, ObstacleType.STATIC);
        } else {
            obstacle = new Obstacle(x, y - 50, gameSpeed, ObstacleType.MOVING);
        }

        // 장애물이 대나무와 겹치지 않도록 확인
        if (!isOverlapWithBamboo(x)) {
            obstacles.add(obstacle);
        } else {
            obstacleSpawnTimer = 0; // 겹치면 생성 딜레이 초기화
        }
    }

    private void spawnFallingObstacle() {
        int x = random.nextInt(PANEL_WIDTH - 100) + 50;
        int y = -64; // 화면 위쪽에서 시작
        fallingObstacles.add(new FallingObstacle(x, y, gameSpeed));
    }

    private void spawnBamboo() {
        int x = PANEL_WIDTH;
        int y = 504; // Ground level

        // 대나무가 장애물과 겹치지 않도록 확인
        if (!isOverlapWithObstacle(x)) {
            bamboos.add(new Bamboo(x, y, gameSpeed));
        } else {
            bambooSpawnTimer = 0; // 겹치면 생성 딜레이 초기화
        }
    }

    private void spawnPowerUp() {
        int x = PANEL_WIDTH;
        int y = random.nextInt(200) + 300; // Random height
        powerUps.add(new PowerUp(x, y, gameSpeed));
    }

    private void spawnHealthItem() {
        int x = PANEL_WIDTH;
        int y = random.nextInt(200) + 300; // Random height
        healthItems.add(new HealthItem(x, y, gameSpeed));
    }

    private boolean isOverlapWithObstacle(int x) {
        for (Obstacle obstacle : obstacles) {
            if (Math.abs(obstacle.getX() - x) < obstacle.getWidth()) {
                return true;
            }
        }
        return false;
    }

    private boolean isOverlapWithBamboo(int x) {
        for (Bamboo bamboo : bamboos) {
            if (Math.abs(bamboo.getX() - x) < bamboo.getWidth()) {
                return true;
            }
        }
        return false;
    }

    private void handleObstacles() {
        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            obstacle.update();
            if (obstacle.getX() + obstacle.getWidth() < 0) {
                iterator.remove(); // 화면 밖으로 나간 장애물 제거
            }
        }
    }

    private void handleFallingObstacles() {
        Iterator<FallingObstacle> iterator = fallingObstacles.iterator();
        while (iterator.hasNext()) {
            FallingObstacle obstacle = iterator.next();
            obstacle.update();
            if (obstacle.getY() > PANEL_HEIGHT) {
                iterator.remove(); // 화면 아래로 사라진 장애물 제거
            }
        }
    }

    private void handleBamboos() {
        Iterator<Bamboo> iterator = bamboos.iterator();
        while (iterator.hasNext()) {
            Bamboo bamboo = iterator.next();
            bamboo.update();
            if (bamboo.getX() + bamboo.getWidth() < 0) {
                iterator.remove(); // 화면 밖으로 나간 대나무 제거
            }
        }
    }

    private void handlePowerUps() {
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.update();
            if (powerUp.getX() + powerUp.getWidth() < 0) {
                iterator.remove(); // 화면 밖으로 나간 강화 아이템 제거
            }
        }
    }

    private void handleHealthItems() {
        Iterator<HealthItem> iterator = healthItems.iterator();
        while (iterator.hasNext()) {
            HealthItem healthItem = iterator.next();
            healthItem.update();
            if (healthItem.getX() + healthItem.getWidth() < 0) {
                iterator.remove(); // 화면 밖으로 나간 체력 아이템 제거
            }
        }
    }

    private void checkCollisions() {
        Rectangle pandaBounds = panda.getBounds();

        // 장애물과의 충돌 체크
        Iterator<Obstacle> obstacleIterator = obstacles.iterator();
        while (obstacleIterator.hasNext()) {
            Obstacle obstacle = obstacleIterator.next();
            if (pandaBounds.intersects(obstacle.getBounds())) {
                handleCollision();
                obstacleIterator.remove();
            }
        }

        // 떨어지는 장애물과의 충돌 체크
        Iterator<FallingObstacle> fallingIterator = fallingObstacles.iterator();
        while (fallingIterator.hasNext()) {
            FallingObstacle obstacle = fallingIterator.next();
            if (pandaBounds.intersects(obstacle.getBounds())) {
                handleCollision();
                fallingIterator.remove();
            }
        }

        // 대나무와의 충돌 체크
        Iterator<Bamboo> bambooIterator = bamboos.iterator();
        while (bambooIterator.hasNext()) {
            Bamboo bamboo = bambooIterator.next();
            if (pandaBounds.intersects(bamboo.getBounds())) {
                SoundPlayer.playSound("/sounds/eat.wav");
                score += 10; // 점수 증가
                bambooIterator.remove();
            }
        }

        // 강화 아이템과의 충돌 체크
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            if (pandaBounds.intersects(powerUp.getBounds())) {
                isInvincible = true; // 무적 상태 활성화
                SoundPlayer.playBackgroundMusic("/sounds/invincible.wav");
                invincibilityTimer = random.nextInt(241) + 180; // 3~7초 지속
                powerUpIterator.remove();
            }
        }

        // 체력 아이템과의 충돌 체크
        Iterator<HealthItem> healthItemIterator = healthItems.iterator();
        while (healthItemIterator.hasNext()) {
            HealthItem healthItem = healthItemIterator.next();
            if (pandaBounds.intersects(healthItem.getBounds())) {
                SoundPlayer.playSound("/sounds/health_item.wav");
                if (health < maxHealth) {
                    health++; // 체력 증가
                }
                healthItemIterator.remove();
            }
        }
    }

    private void handleCollision() {
        if (!isInvincible) { // 팬더가 무적 상태가 아니면 충돌 처리
            SoundPlayer.playSound("/sounds/battle_sound.wav"); // 충돌 사운드 재생
            health--; // 체력 감소
            if (health <= 0) { // 체력이 0이 되면
                isGameOver = true; // 게임 오버 상태로 설정
                gameTimer.stop(); // 게임 루프 종료
                showGameOverDialog(); // 게임 오버 화면 표시
            }
        }
    }

    private void updateGameSpeed() {
        // 점수를 기반으로 게임 속도를 계산 (점수가 올라갈수록 속도가 증가)
        int newSpeed = 5 + (score / speedIncrementInterval) * 2; 
        if (newSpeed != gameSpeed) { // 새로운 속도가 기존 속도와 다르면 업데이트
            gameSpeed = newSpeed;
            
            // 모든 움직이는 객체의 속도를 새 속도로 설정
            for (Obstacle obstacle : obstacles) {
                obstacle.setSpeed(gameSpeed);
            }
            for (Bamboo bamboo : bamboos) {
                bamboo.setSpeed(gameSpeed);
            }
            for (PowerUp powerUp : powerUps) {
                powerUp.setSpeed(gameSpeed);
            }
            for (HealthItem healthItem : healthItems) {
                healthItem.setSpeed(gameSpeed);
            }
            for (FallingObstacle obstacle : fallingObstacles) {
                obstacle.setSpeed(gameSpeed);
            }
        }
    }

    private void showGameOverDialog() {
        SoundPlayer.playSound("/sounds/game_over1.wav"); // 게임 오버 사운드 재생

        // 게임 오버 메시지를 표시하고 재시작 여부를 묻는 다이얼로그
        int response = JOptionPane.showConfirmDialog(
            this,
            "Game Over! Your score: " + score + "\nPlay again?", // 점수 표시
            "Game Over",
            JOptionPane.YES_NO_OPTION
        );

        if (response == JOptionPane.YES_OPTION) { // 예를 선택하면
            restartGame(); // 게임 재시작
        } else {
            // 게임 창 닫기 및 메인 메뉴로 복귀
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose(); // 게임 창 닫기
            }

            endGame(); // 게임 루프 종료
            new JustMiniMain(); // 메인 메뉴로 복귀
        }
    }

    private void restartGame() {
        initGame(); // 게임 상태 초기화
        isGameOver = false; // 게임 오버 상태 해제
        gameTimer.start(); // 게임 루프 재시작
        SoundPlayer.playBackgroundMusic("/sounds/panda2.wav"); // 배경 음악 재생
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inTutorial) { 
            drawTutorial(g); // 튜토리얼 화면 그리기
        } else {
            background.draw(g); // 배경 그리기
            panda.draw(g); // 팬더 그리기

            // 게임 요소들 그리기
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(g);
            }
            for (FallingObstacle obstacle : fallingObstacles) {
                obstacle.draw(g);
            }
            for (Bamboo bamboo : bamboos) {
                bamboo.draw(g);
            }
            for (PowerUp powerUp : powerUps) {
                powerUp.draw(g);
            }
            for (HealthItem healthItem : healthItems) {
                healthItem.draw(g);
            }

            drawHUD(g); // 점수와 체력 표시

            if (isGameOver) {
                drawGameOver(g); // 게임 오버 화면 그리기
            }
        }
    }

    private void drawTutorial(Graphics g) {
        g.setColor(Color.BLACK); // 배경색 검정
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT); // 전체 화면 검정색으로 채움

        g.setColor(Color.WHITE); // 텍스트 색상 흰색
        g.setFont(new Font("Arial", Font.BOLD, 24)); // 폰트 설정

        // 튜토리얼 슬라이드별 내용 표시
        if (tutorialSlide == 1) {
            g.drawImage(tutorialImage1, (PANEL_WIDTH - 300) / 2, 100, 300, 300, null);
            drawCenteredString(g, tutorialText1, new Rectangle(0, 420, PANEL_WIDTH, 30), g.getFont());
            drawCenteredString(g, "Press Enter to continue", new Rectangle(0, 550, PANEL_WIDTH, 30), g.getFont().deriveFont(16f));
        } else if (tutorialSlide == 2) {
            g.drawImage(tutorialImage2, (PANEL_WIDTH - 600) / 2, 100, 600, 300, null);
            drawCenteredString(g, tutorialText2, new Rectangle(0, 420, PANEL_WIDTH, 30), g.getFont());
            drawCenteredString(g, tutorialText2_1, new Rectangle(0, 450, PANEL_WIDTH, 30), g.getFont());
            drawCenteredString(g, "Press Enter to continue", new Rectangle(0, 550, PANEL_WIDTH, 30), g.getFont().deriveFont(16f));
        } else if (tutorialSlide == 3) {
            g.drawImage(tutorialImage3, (PANEL_WIDTH - 300) / 2, 200, 300, 150, null);
            drawCenteredString(g, tutorialText3, new Rectangle(0, 420, PANEL_WIDTH, 30), g.getFont());
            drawCenteredString(g, "Press Enter to start the game", new Rectangle(0, 550, PANEL_WIDTH, 30), g.getFont().deriveFont(16f));
        }
    }

    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2; // 텍스트의 x 좌표 계산
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent(); // 텍스트의 y 좌표 계산
        g.setFont(font);
        g.drawString(text, x, y); // 텍스트 그리기
    }

    private void drawHUD(Graphics g) {
        // 점수 표시
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 100, 28);

        // 체력 표시 (하트 이미지)
        for (int i = 0; i < health; i++) {
            g.drawImage(heartImage, 10 + i * 30, 10, null);
        }

        // 무적 상태 표시
        if (isInvincible) {
            g.setColor(Color.GREEN);
            g.drawString("INVINCIBLE!", 343, 100);
        }
    }

    private void drawGameOver(Graphics g) {
        String message = "Game Over"; // 게임 오버 메시지
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        int textWidth = g.getFontMetrics().stringWidth(message); // 텍스트 너비 계산
        g.drawString(message, (PANEL_WIDTH - textWidth) / 2, PANEL_HEIGHT / 2); // 화면 중앙에 텍스트 표시
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (inTutorial) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                tutorialSlide++; // 다음 튜토리얼 슬라이드로 이동
                if (tutorialSlide > 3) { // 모든 슬라이드가 끝나면
                    inTutorial = false;
                    startGame(); // 게임 시작
                }
                repaint();
            }
        } else {
            panda.keyPressed(e); // 팬더의 키 입력 처리
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!inTutorial) {
            panda.keyReleased(e); // 팬더의 키 입력 해제 처리
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // 사용되지 않음
    }
}
