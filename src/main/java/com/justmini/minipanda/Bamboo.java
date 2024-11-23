// 대나무는 화면에서 이동하고 플레이어가 먹을 수 있는 아이템으로 구현한 코드.

package com.justmini.minipanda;

import javax.swing.*;
import java.awt.*;

public class Bamboo {
    private int x, y; // 대나무의 현재 위치 (x, y 좌표)
    private int width = 64; // 대나무의 너비
    private int height = 64; // 대나무의 높이
    private int speed; // 대나무가 이동하는 속도
    private Image bambooImage; // 대나무 이미지

    // 생성자: 대나무의 초기 위치와 속도를 설정.
    public Bamboo(int x, int y, int speed) {
        this.x = x; // 초기 x 좌표
        this.y = y; // 초기 y 좌표
        this.speed = speed; // 이동 속도
        loadImage(); // 대나무 이미지를 로드.
    }

    // 대나무 이미지를 로드하는 메서드
    private void loadImage() {
        // 리소스에서 대나무 이미지를 불러옴.
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/minipanda/bamboo.png"));
        // 이미지를 설정된 크기로 스케일링.
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        bambooImage = scaledImage;
    }

    // 대나무의 위치를 업데이트하여 이동 효과를 만든.
    public void update() {
        x -= speed; // 대나무를 왼쪽으로 이동.
    }

    // 대나무를 화면에 그리는 메서드
    public void draw(Graphics g) {
        g.drawImage(bambooImage, x, y, null); // 지정된 위치에 대나무 이미지를 그림.
    }

    // 대나무의 충돌 영역을 반환하는 메서드
    public Rectangle getBounds() {
        // 대나무의 충돌 영역을 약간 작게 조정하여 좀 더 정확한 충돌 감지를 제공.
        return new Rectangle(x + 16, y + 16, 32, 32); // 대나무 중앙 부분의 영역
    }

    // 대나무의 이동 속도를 설정하는 메서드
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // 대나무의 현재 x 좌표를 반환하는 메서드
    public int getX() {
        return x;
    }

    // 대나무의 너비를 반환하는 메서드
    public int getWidth() {
        return width;
    }
}