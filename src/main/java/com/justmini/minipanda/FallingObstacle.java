// 하늘에서 떨어지는 장애물을 구현.

package com.justmini.minipanda;

import javax.swing.*;
import java.awt.*;

public class FallingObstacle {
    private int x, y; // 장애물의 현재 위치 (x, y 좌표)
    private int width = 32; // 장애물의 너비
    private int height = 21; // 장애물의 높이
    private int speed; // 장애물이 떨어지는 속도
    private Image obstacleImage; // 장애물 이미지

    // 생성자: 장애물의 초기 위치와 속도를 설정.
    public FallingObstacle(int x, int y, int gameSpeed) {
        this.x = x; // 초기 x 좌표
        this.y = y; // 초기 y 좌표
        this.speed = gameSpeed + 2; // 게임 속도보다 약간 더 빠르게 떨어지도록 설정.
        loadImage(); // 장애물 이미지를 로드
    }

    // 장애물 이미지를 로드하는 메서드
    private void loadImage() {
        // 리소스에서 장애물 이미지를 불러옴.
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/minipanda/falling_obstacle.gif"));
        obstacleImage = icon.getImage(); // 이미지 저장
    }

    // 장애물의 위치를 업데이트하여 떨어지는 효과를 만듬.
    public void update() {
        y += speed; // y 좌표를 증가시켜 아래로 이동
    }

    // 장애물을 화면에 그리는 메서드
    public void draw(Graphics g) {
        g.drawImage(obstacleImage, x, y, null); // 지정된 위치에 장애물 이미지를 그림.
    }

    // 장애물의 충돌 영역을 반환하는 메서드
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); // 충돌 감지를 위한 사각형 영역 반환
    }

    // 장애물의 속도를 설정하는 메서드
    public void setSpeed(int gameSpeed) {
        this.speed = gameSpeed + 2; // 새로운 게임 속도에 맞춰 장애물 속도를 업데이트
    }

    // 장애물의 현재 y 좌표를 반환하는 메서드
    public int getY() {
        return y;
    }
}