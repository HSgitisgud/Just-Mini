package com.justmini.minipanda;

import javax.swing.*;
import java.awt.*;

public class Obstacle {
    private int x, y; // 장애물의 현재 위치
    private int width = 64; // 장애물의 너비
    private int height = 64; // 장애물의 높이
    private int speed; // 장애물이 이동하는 속도
    private Image obstacleImage; // 장애물의 이미지
    private ObstacleType type; // 장애물의 유형 (STATIC 또는 MOVING)
    private int initialY; // 장애물의 초기 Y 좌표 (움직이는 장애물의 기준점)
    private int movementRange = 100; // 장애물이 이동할 수 있는 범위
    private int direction = 1; // 장애물 이동 방향 (1: 아래로, -1: 위로)

    // 생성자: 장애물의 초기 위치, 속도 및 유형 설정
    public Obstacle(int x, int y, int speed, ObstacleType type) {
        this.x = x; // 초기 x 좌표
        this.y = y; // 초기 y 좌표
        this.initialY = y; // 초기 y 좌표 저장 (움직이는 장애물의 기준점으로 사용)
        this.speed = speed; // 이동 속도
        this.type = type; // 장애물 유형 설정
        loadImage(); // 장애물 이미지 로드
    }

    // 장애물의 이미지를 로드하고 크기를 조정
    private void loadImage() {
        String imagePath = "/images/minipanda/stone.png"; // 기본 이미지 경로 (정적인 장애물)
        if (type == ObstacleType.MOVING) { // 장애물이 움직이는 경우
            imagePath = "/images/minipanda/moving_obstacle.png"; // 움직이는 장애물의 이미지 경로
        }
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath)); // 이미지 로드
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // 크기 조정
        obstacleImage = scaledImage; // 이미지 설정
    }

    // 장애물의 위치를 업데이트
    public void update() {
        x -= speed; // 장애물을 왼쪽으로 이동
        if (type == ObstacleType.MOVING) { // 움직이는 장애물인 경우
            y += direction * 2; // 방향에 따라 위아래로 이동
            // 이동 범위를 벗어나면 방향 반전
            if (y > initialY + movementRange || y < initialY - movementRange) {
                direction *= -1; // 방향을 반대로 설정
            }
        }
    }

    // 장애물을 화면에 그리기
    public void draw(Graphics g) {
        g.drawImage(obstacleImage, x, y, null); // 장애물 이미지 그리기
    }

    // 장애물의 충돌 범위를 반환 (충돌 감지를 위한 사각형)
    public Rectangle getBounds() {
        // 충돌 범위를 조정하여 이미지 크기보다 약간 작게 설정
        return new Rectangle(x + 16, y + 16, 32, 32);
    }

    // 장애물의 속도를 설정
    public void setSpeed(int speed) {
        this.speed = speed; // 새로운 속도로 업데이트
    }

    // 장애물의 현재 x 좌표 반환
    public int getX() {
        return x;
    }

    // 장애물의 너비 반환
    public int getWidth() {
        return width;
    }
}