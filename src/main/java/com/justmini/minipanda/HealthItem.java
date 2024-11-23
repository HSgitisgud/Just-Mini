package com.justmini.minipanda;

import javax.swing.*;
import java.awt.*;

public class HealthItem {
    private int x, y; // 아이템의 현재 x, y 좌표
    private int width = 32; // 아이템의 너비
    private int height = 32; // 아이템의 높이
    private int speed; // 아이템의 이동 속도
    private Image healthImage; // 아이템 이미지

    // 생성자: 체력 아이템의 초기 위치와 속도를 설정
    public HealthItem(int x, int y, int speed) {
        this.x = x; // 아이템의 초기 x 좌표
        this.y = y; // 아이템의 초기 y 좌표
        this.speed = speed; // 아이템의 이동 속도
        loadImage(); // 이미지 로드
    }

    // 아이템 이미지를 로드하고 크기를 조정
    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/minipanda/health_item.png")); // 이미지 경로에서 아이템 로드
        healthImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // 이미지 크기를 설정된 너비와 높이에 맞게 조정
    }

    // 아이템을 왼쪽으로 이동
    public void update() {
        x -= speed; // x 좌표를 속도만큼 감소시켜 왼쪽으로 이동
    }

    // 아이템을 화면에 그리기
    public void draw(Graphics g) {
        g.drawImage(healthImage, x, y, null); // 현재 위치에 아이템 이미지 그리기
    }

    // 아이템의 충돌 범위를 반환 (충돌 감지를 위한 사각형 범위)
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); // 아이템의 x, y 좌표와 너비, 높이를 사용해 사각형 범위 생성
    }

    // 아이템의 속도를 설정
    public void setSpeed(int speed) {
        this.speed = speed; // 새로운 속도로 업데이트
    }

    // 아이템의 현재 x 좌표 반환
    public int getX() {
        return x;
    }

    // 아이템의 너비 반환
    public int getWidth() {
        return width;
    }
}