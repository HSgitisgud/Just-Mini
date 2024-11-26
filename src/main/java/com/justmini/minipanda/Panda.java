package com.justmini.minipanda;

import com.justmini.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Panda {
    private int x, y; // 팬더의 현재 위치
    private int width, height; // 팬더의 크기
    private double yVelocity = 0; // 팬더의 수직 속도
    private double gravity = 0.5; // 중력 효과
    private boolean isJumping = false; // 팬더가 점프 중인지 여부
    private boolean canDoubleJump = false; // 팬더가 더블 점프 가능한지 여부

    private Image pandaImage; // 팬더의 이미지

    // 생성자: 팬더의 초기 위치를 설정하고 이미지를 로드
    public Panda(int x, int y) {
        this.x = x; // 초기 x 좌표
        this.y = y; // 초기 y 좌표
        loadImage(); // 팬더 이미지 로드
    }

    // 팬더 이미지를 로드하고 크기를 설정
    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/minipanda/panda.gif")); // 팬더 이미지 경로
        pandaImage = icon.getImage(); // 이미지 불러오기
        width = 64; // 팬더 너비 설정
        height = 64; // 팬더 높이 설정
    }

    // 팬더의 위치를 업데이트 (중력과 점프 계산)
    public void update() {
        yVelocity += gravity; // 중력을 속도에 반영
        y += yVelocity; // 팬더의 y 좌표 업데이트

        // 팬더가 땅에 닿으면 상태 초기화
        if (y >= 504) { // 땅의 높이는 504
            y = 504; // 땅에 고정
            yVelocity = 0; // 수직 속도 초기화
            isJumping = false; // 점프 상태 초기화
            canDoubleJump = false; // 더블 점프 상태 초기화
        }
    }

    // 팬더를 화면에 그리기
    public void draw(Graphics g) {
        g.drawImage(pandaImage, x, y, null); // 팬더 이미지를 현재 위치에 그리기
    }

    // 키 입력 처리 (점프 및 더블 점프)
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { // 스페이스 키를 눌렀을 때
            if (!isJumping) { // 팬더가 점프 중이 아니면
                SoundPlayer.playSound("/sounds/jump.wav");
                yVelocity = -12; // 첫 번째 점프
                isJumping = true; // 점프 상태로 변경
                canDoubleJump = true; // 더블 점프 가능 상태 설정
            } else if (canDoubleJump) { // 더블 점프 가능 상태라면
                SoundPlayer.playSound("/sounds/jump1.wav");
                yVelocity = -12; // 더블 점프
                canDoubleJump = false; // 더블 점프 사용 후 불가능 상태로 변경
            }
        }
    }

    // 키 해제 처리 (현재는 사용되지 않음)
    public void keyReleased(KeyEvent e) {
        // 현재는 특별한 동작 없음
    }

    // 팬더의 충돌 범위를 반환 (충돌 감지용)
    public Rectangle getBounds() {
        // 팬더 이미지 중심에 가까운 범위로 조정 (32x32 픽셀)
        return new Rectangle(x + 16, y + 16, 32, 32);
    }

    // 팬더의 x 좌표 반환
    public int getX() {
        return x;
    }
}