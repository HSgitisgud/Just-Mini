package com.justmini.minidodge;

import javax.swing.*;
import java.awt.*;

public class Shuriken {
	int x, y;
	double dx, dy;
	private Image shurikenImage;
	
	
	// 플레이어 위치를 향해 이동하는 수리검 생성
	public Shuriken(int startX, int startY, int targetX, int targetY) {
		this.x = startX;
		this.y = startY;
		
		// 타겟 방향 계산을 위해 각도 구함
		double angle = Math.atan2(targetY - startY, targetX - startX);
		this.dx = Math.cos(angle) * 5;
		this.dy = Math.sin(angle) * 5;
		
		// 수리검 이미지 로드
		shurikenImage = new ImageIcon(getClass().getResource("/images/minidodge/Shuriken.png")).getImage();
	}

	public void draw(Graphics g) {
		g.drawImage(shurikenImage, x, y, 20, 20, null); // 20x20 크기로 이미지 그리기
	}

	// 특정 방향으로 이동하는 수리검 생성
	public Shuriken(int startX, int startY, String direction) {
        this.x = startX;
        this.y = startY;

        // 방향에 따라 속도 설정
        switch (direction) {
            case "UP":    dx = 0; dy = -5; break;
            case "DOWN":  dx = 0; dy = 5;  break;
            case "LEFT":  dx = -5; dy = 0; break;
            case "RIGHT": dx = 5; dy = 0;  break;
            case "UP_LEFT": dx = -5; dy = -5; break;
            case "UP_RIGHT": dx = 5; dy = -5; break;
            case "DOWN_LEFT": dx = -5; dy = 5; break;
            case "DOWN_RIGHT": dx = 5; dy = 5; break;
        }

        // 수리검 이미지 로드
        shurikenImage = new ImageIcon(getClass().getResource("/images/minidodge/Shuriken.png")).getImage();
    }

	public void move() {
		x += dx;
		y += dy;
	}

	// 수리검이 화면 밖으로 나갔는지 확인
	public boolean isOffScreen(int width, int height) {
		return x < 0 || x > width || y < 0 || y > height;
	}

	// 수리검의 충돌 판정을 위한 사각형 반환
	public Rectangle getRectangle() {
		return new Rectangle(x + 5, y + 5, 10, 10); // 수리검 중앙에 10x10 판정 사각형
	}
}
