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
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;

    private Timer gameTimer;
    private Panda panda;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Bamboo> bamboos;
    private ArrayList<PowerUp> powerUps;
    private ArrayList<HealthItem> healthItems;
    private ArrayList<FallingObstacle> fallingObstacles;
    private Background background;
    private Random random;

    private boolean isGameOver = false;
    private int score = 0;
    private int obstacleSpawnTimer = 0;
    private int bambooSpawnTimer = 0;
    private int powerUpSpawnTimer = 0;
    private int healthItemSpawnTimer = 0;
    private int fallingObstacleSpawnTimer = 0;
    private int gameSpeed = 5;
    private int speedIncrementInterval = 50; // Increase speed every 50 points
    private int health = 3; // Panda's health
    private final int maxHealth = 3; // Maximum health
    private boolean isInvincible = false;
    private int invincibilityTimer = 0;

    private Image heartImage; // For health indicator

    // Tutorial variables
    private boolean inTutorial = true;
    private int tutorialSlide = 1;
    private Image tutorialImage1;
    private Image tutorialImage2;
    private Image tutorialImage3;
    private String tutorialText1 = "Help the hungry baby panda collect bamboo!";
    private String tutorialText2 = "Press Spacebar to jump. Press twice for a double jump!";
    private String tutorialText2_1 = "You have to avoid frogs, chickens and rocks!";
    private String tutorialText3 = "Collect items to gain advantages or recover health.";

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        initGame();
    }

    private void initGame() {
        panda = new Panda(100, 504);
        obstacles = new ArrayList<>();
        bamboos = new ArrayList<>();
        powerUps = new ArrayList<>();
        healthItems = new ArrayList<>();
        fallingObstacles = new ArrayList<>();
        background = new Background();
        random = new Random();

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

        loadAssets();

        gameTimer = new Timer(16, this); // Approximately 60 FPS
    }

    private void loadAssets() {
        // Load the heart image for the health indicator
        ImageIcon heartIcon = new ImageIcon(getClass().getResource("/images/minipanda/heart.png"));
        heartImage = heartIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);

        // Load tutorial images
        ImageIcon tutorialIcon1 = new ImageIcon(getClass().getResource("/images/minipanda/tutorial1.jpg"));
        tutorialImage1 = tutorialIcon1.getImage();

        ImageIcon tutorialIcon2 = new ImageIcon(getClass().getResource("/images/minipanda/tutorial2.png"));
        tutorialImage2 = tutorialIcon2.getImage();

        ImageIcon tutorialIcon3 = new ImageIcon(getClass().getResource("/images/minipanda/tutorial3.png"));
        tutorialImage3 = tutorialIcon3.getImage();
    }

    public void startGame() {
        gameTimer.start();
        if (!inTutorial) {
            SoundPlayer.playBackgroundMusic("/sounds/panda2.wav");
        }
    }

    public void endGame() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
    }


    public void startTutorial() {
        inTutorial = true;
        tutorialSlide = 1;
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inTutorial) {
            // Do nothing during tutorial
        } else if (!isGameOver) {
            panda.update();
            background.update(gameSpeed);

            handleObstacles();
            handleBamboos();
            handlePowerUps();
            handleHealthItems();
            handleFallingObstacles();
            checkCollisions();
            updateGameSpeed();

            obstacleSpawnTimer++;
            bambooSpawnTimer++;
            powerUpSpawnTimer++;
            healthItemSpawnTimer++;
            fallingObstacleSpawnTimer++;

            if (invincibilityTimer > 0) {
                invincibilityTimer--;
                if (invincibilityTimer == 0) {
                    isInvincible = false;
                    SoundPlayer.playBackgroundMusic("/sounds/panda2.wav");
                }
            }

            if (obstacleSpawnTimer >= random.nextInt(30) + 40) {
                spawnObstacle();
                obstacleSpawnTimer = 0;
            }

            if (bambooSpawnTimer >= random.nextInt(50) + 100) {
                spawnBamboo();
                bambooSpawnTimer = 0;
            }

            if (powerUpSpawnTimer >= random.nextInt(600) + 600) { // Rarer power-ups
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

        repaint();
    }

    private void spawnObstacle() {
        int x = PANEL_WIDTH;
        int y = 504; // Ground level

        // Randomly choose obstacle type
        int obstacleType = random.nextInt(2); // 0 or 1
        Obstacle obstacle;
        if (obstacleType == 0) {
            obstacle = new Obstacle(x, y, gameSpeed, ObstacleType.STATIC);
        } else {
            obstacle = new Obstacle(x, y - 50, gameSpeed, ObstacleType.MOVING);
        }

        // Ensure obstacle does not overlap with existing bamboo
        if (!isOverlapWithBamboo(x)) {
            obstacles.add(obstacle);
        } else {
            // Delay spawning if overlap would occur
            obstacleSpawnTimer = 0;
        }
    }

    private void spawnFallingObstacle() {
        int x = random.nextInt(PANEL_WIDTH - 100) + 50;
        int y = -64; // Start above the screen
        fallingObstacles.add(new FallingObstacle(x, y, gameSpeed));
    }

    private void spawnBamboo() {
        int x = PANEL_WIDTH;
        int y = 504; // Ground level

        // Ensure bamboo does not overlap with existing obstacles
        if (!isOverlapWithObstacle(x)) {
            bamboos.add(new Bamboo(x, y, gameSpeed));
        } else {
            // Delay spawning if overlap would occur
            bambooSpawnTimer = 0;
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
                iterator.remove();
            }
        }
    }

    private void handleFallingObstacles() {
        Iterator<FallingObstacle> iterator = fallingObstacles.iterator();
        while (iterator.hasNext()) {
            FallingObstacle obstacle = iterator.next();
            obstacle.update();
            if (obstacle.getY() > PANEL_HEIGHT) {
                iterator.remove();
            }
        }
    }

    private void handleBamboos() {
        Iterator<Bamboo> iterator = bamboos.iterator();
        while (iterator.hasNext()) {
            Bamboo bamboo = iterator.next();
            bamboo.update();
            if (bamboo.getX() + bamboo.getWidth() < 0) {
                iterator.remove();
            }
        }
    }

    private void handlePowerUps() {
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.update();
            if (powerUp.getX() + powerUp.getWidth() < 0) {
                iterator.remove();
            }
        }
    }

    private void handleHealthItems() {
        Iterator<HealthItem> iterator = healthItems.iterator();
        while (iterator.hasNext()) {
            HealthItem healthItem = iterator.next();
            healthItem.update();
            if (healthItem.getX() + healthItem.getWidth() < 0) {
                iterator.remove();
            }
        }
    }

    private void checkCollisions() {
        Rectangle pandaBounds = panda.getBounds();

        // Check collision with obstacles
        Iterator<Obstacle> obstacleIterator = obstacles.iterator();
        while (obstacleIterator.hasNext()) {
            Obstacle obstacle = obstacleIterator.next();
            if (pandaBounds.intersects(obstacle.getBounds())) {
                handleCollision();
                obstacleIterator.remove();
            }
        }

        // Check collision with falling obstacles
        Iterator<FallingObstacle> fallingIterator = fallingObstacles.iterator();
        while (fallingIterator.hasNext()) {
            FallingObstacle obstacle = fallingIterator.next();
            if (pandaBounds.intersects(obstacle.getBounds())) {
                handleCollision();
                fallingIterator.remove();
            }
        }

        // Check collision with bamboos
        Iterator<Bamboo> bambooIterator = bamboos.iterator();
        while (bambooIterator.hasNext()) {
            Bamboo bamboo = bambooIterator.next();
            if (pandaBounds.intersects(bamboo.getBounds())) {
                SoundPlayer.playSound("/sounds/eat.wav");
                score += 10;
                bambooIterator.remove();
            }
        }

        // Check collision with power-ups
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            if (pandaBounds.intersects(powerUp.getBounds())) {
                isInvincible = true;
                SoundPlayer.playBackgroundMusic("/sounds/invincible.wav");
                invincibilityTimer = random.nextInt(241) + 180; // Lasts for 3~7 seconds (180~420 frames at 60 FPS)
                powerUpIterator.remove();
            }
        }

        // Check collision with health items
        Iterator<HealthItem> healthItemIterator = healthItems.iterator();
        while (healthItemIterator.hasNext()) {
            HealthItem healthItem = healthItemIterator.next();
            if (pandaBounds.intersects(healthItem.getBounds())) {
                SoundPlayer.playSound("/sounds/health_item.wav");
                if (health < maxHealth) {
                    health++;
                }
                healthItemIterator.remove();
            }
        }
    }

    private void handleCollision() {
        if (!isInvincible) {
            SoundPlayer.playSound("/sounds/battle_sound.wav");
            health--;
            if (health <= 0) {
                isGameOver = true;
                gameTimer.stop();
                showGameOverDialog();
            }
        }
    }

    private void updateGameSpeed() {
        int newSpeed = 5 + (score / speedIncrementInterval) * 2; // Increase speed more aggressively
        if (newSpeed != gameSpeed) {
            gameSpeed = newSpeed;
            // Update speeds of moving objects
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
        SoundPlayer.playSound("/sounds/game_over1.wav");

        int response = JOptionPane.showConfirmDialog(
                this,
                "Game Over! Your score: " + score + "\nPlay again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
        );
        if (response == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            // Close the game window and return to the main menu
            // Get the parent frame (game window)
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose(); // Close the game window
            }

            // End the game loop
            endGame();

            // Show the main menu
            new JustMiniMain();
        }
    }


    private void restartGame() {
        initGame();
        isGameOver = false;
//        inTutorial = true;
//        tutorialSlide = 1;
        gameTimer.start();
        SoundPlayer.playBackgroundMusic("/sounds/panda2.wav");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inTutorial) {
            drawTutorial(g);
        } else {
            background.draw(g);
            panda.draw(g);

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

            drawHUD(g);

            if (isGameOver) {
                drawGameOver(g);
            }
        }
    }

    private void drawTutorial(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        if (tutorialSlide == 1) {
            // Draw first slide
            g.drawImage(tutorialImage1, (PANEL_WIDTH - 300) / 2, 100, 300, 300, null);
            drawCenteredString(g, tutorialText1, new Rectangle(0, 420, PANEL_WIDTH, 30), g.getFont());
            drawCenteredString(g, "Press Enter to continue", new Rectangle(0, 550, PANEL_WIDTH, 30), g.getFont().deriveFont(16f));
        } else if (tutorialSlide == 2) {
            // Draw second slide
            g.drawImage(tutorialImage2, (PANEL_WIDTH - 600) / 2, 100, 600, 300, null);
            drawCenteredString(g, tutorialText2, new Rectangle(0, 420, PANEL_WIDTH, 30), g.getFont());
            drawCenteredString(g, tutorialText2_1, new Rectangle(0, 450, PANEL_WIDTH, 30), g.getFont());
            drawCenteredString(g, "Press Enter to continue", new Rectangle(0, 550, PANEL_WIDTH, 30), g.getFont().deriveFont(16f));
        } else if (tutorialSlide == 3) {
            // Draw third slide
            g.drawImage(tutorialImage3, (PANEL_WIDTH - 300) / 2, 200, 300, 150, null);
            drawCenteredString(g, tutorialText3, new Rectangle(0, 420, PANEL_WIDTH, 30), g.getFont());
            drawCenteredString(g, "Press Enter to start the game", new Rectangle(0, 550, PANEL_WIDTH, 30), g.getFont().deriveFont(16f));
        }
    }

    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }

    private void drawHUD(Graphics g) {
        // Draw Score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 100, 28);

        // Draw Health using heart images
        for (int i = 0; i < health; i++) {
            g.drawImage(heartImage, 10 + i * 30, 10, null);
        }

        // Draw Invincibility Indicator
        if (isInvincible) {
            g.setColor(Color.GREEN);
            g.drawString("INVINCIBLE!", 343, 100);
        }
    }

    private void drawGameOver(Graphics g) {
        String message = "Game Over";
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        int textWidth = g.getFontMetrics().stringWidth(message);
        g.drawString(message, (PANEL_WIDTH - textWidth) / 2, PANEL_HEIGHT / 2);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (inTutorial) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                tutorialSlide++;
                if (tutorialSlide > 3) {
                    inTutorial = false;
                    startGame();
                }
                repaint();
            }
        } else {
            panda.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!inTutorial) {
            panda.keyReleased(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
