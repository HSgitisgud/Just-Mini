package com.justmini.minipanda;

import javax.swing.*;
import java.awt.*;

public class PowerUp {
    private int x, y; // Power-Up의 현재 위치
    private int width = 32; // Power-Up의 너비
    private int height = 32; // Power-Up의 높이
    private int speed; // Power-Up의 이동 속도
    private Image powerUpImage; // Power-Up의 이미지

    // 생성자: Power-Up의 초기 위치와 속도 설정
    public PowerUp(int x, int y, int speed) {
        this.x = x; // 초기 x 좌표
        this.y = y; // 초기 y 좌표
        this.speed = speed; // 이동 속도 설정
        loadImage(); // Power-Up 이미지 로드
    }

    // Power-Up의 이미지를 로드하고 크기를 조정
    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/minipanda/powerup.png")); // Power-Up 이미지 경로
        powerUpImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // 이미지 크기 조정
    }

    // Power-Up의 위치를 업데이트 (왼쪽으로 이동)
    public void update() {
        x -= speed; // x 좌표를 속도만큼 감소시켜 Power-Up을 왼쪽으로 이동
    }

    // Power-Up을 화면에 그리기
    public void draw(Graphics g) {
        g.drawImage(powerUpImage, x, y, null); // 현재 위치에 Power-Up 이미지 그리기
    }

    // Power-Up의 충돌 범위를 반환 (충돌 감지를 위한 사각형)
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); // Power-Up의 x, y 좌표와 너비, 높이를 사용해 사각형 생성
    }

    // Power-Up의 속도를 설정
    public void setSpeed(int speed) {
        this.speed = speed; // 새로운 속도로 업데이트
    }

    // Power-Up의 현재 x 좌표 반환
    public int getX() {
        return x;
    }

    // Power-Up의 너비 반환
    public int getWidth() {
        return width;
    }
}