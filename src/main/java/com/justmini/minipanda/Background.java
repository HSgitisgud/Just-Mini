// 배경이 스크롤되어 움직이는 효과를 구현, 무한 반복

package com.justmini.minipanda;

import javax.swing.*;
import java.awt.*;

public class Background {
    private Image backgroundImage; // 배경 이미지
    private int x1, x2; // 배경 이미지를 그릴 두 위치의 x 좌표
    private int imageWidth; // 배경 이미지의 너비

    // 생성자: 배경 이미지를 초기화, 두 이미지를 나란히 배치.
    public Background() {
        loadImage(); // 배경 이미지를 로드
        x1 = 0; // 첫 번째 배경 이미지의 x 좌표는 0
        x2 = imageWidth; // 두 번째 배경 이미지는 첫 번째 이미지 바로 옆에 위치
    }

    // 배경 이미지를 로드하는 메서드
    private void loadImage() {
        // 이미지 리소스를 불러온다.
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/minipanda/background.png"));
        backgroundImage = icon.getImage(); // 배경 이미지 저장
        imageWidth = backgroundImage.getWidth(null); // 이미지의 너비를 가져옵니다.
    }

    // 배경의 위치를 업데이트하여 스크롤 효과.
    public void update(int gameSpeed) {
        // 게임 속도에 비례하여 배경 속도를 설정.
        int bgSpeed = gameSpeed / 2; // 배경은 게임 속도의 절반 속도로 움직입니다.

        // 두 배경 이미지를 왼쪽으로 이동
        x1 -= bgSpeed;
        x2 -= bgSpeed;

        // 첫 번째 이미지가 화면 왼쪽 끝을 지나면 오른쪽 끝으로 이동
        if (x1 + imageWidth <= 0) {
            x1 = x2 + imageWidth;
        }

        // 두 번째 이미지가 화면 왼쪽 끝을 지나면 오른쪽 끝으로 이동
        if (x2 + imageWidth <= 0) {
            x2 = x1 + imageWidth;
        }
    }

    // 배경을 화면에 그리는 메서드
    public void draw(Graphics g) {
        // 첫 번째 배경 이미지를 그림.
        g.drawImage(backgroundImage, x1, 0, null);
        // 두 번째 배경 이미지를 그림.
        g.drawImage(backgroundImage, x2, 0, null);
    }
}