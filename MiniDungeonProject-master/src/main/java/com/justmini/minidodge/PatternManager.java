package com.justmini.minidodge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatternManager {
    private ArrayList<Shuriken> shurikens;
    private int screenWidth, screenHeight;
    private long lastPatternTime;
    private long gameStartTime;
    private Random random;
    private List<Pattern> patterns;
    private int currentPatternIndex;

    public PatternManager(ArrayList<Shuriken> shurikens, int screenWidth, int screenHeight) {
        this.shurikens = shurikens;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.lastPatternTime = System.currentTimeMillis();
        this.gameStartTime = System.currentTimeMillis();
        this.random = new Random();

        // 패턴 리스트 초기화
        this.patterns = new ArrayList<>();
        this.patterns.add(new Pattern1());
        this.patterns.add(new Pattern2());
        this.currentPatternIndex = 0;
    }

    public void spawnPattern(int playerX, int playerY) {
        long currentTime = System.currentTimeMillis();

        // 게임 시작 후 5초가 지나야 패턴 생성
        if (currentTime - gameStartTime < 5000) {
            return;
        }

        // 패턴 간격 시간 체크
        if (currentTime - lastPatternTime >= 5000) {
            // 현재 패턴 실행
            Pattern currentPattern = patterns.get(currentPatternIndex);
            currentPattern.spawnShurikens(shurikens, playerX, playerY, screenWidth, screenHeight);

            // 패턴 인덱스 갱신 (패턴을 순환)
            currentPatternIndex = (currentPatternIndex + 1) % patterns.size();
            lastPatternTime = currentTime;
        }
    }

    public void spawnRandomShuriken(int playerX, int playerY) {
        // 일정 확률로 랜덤 수리검 생성
        if (random.nextInt(30) > 26) {
            int side = random.nextInt(4);
            int startX = 0, startY = 0;
            switch (side) {
                case 0: startX = random.nextInt(screenWidth); startY = 0; break;
                case 1: startX = random.nextInt(screenWidth); startY = screenHeight; break;
                case 2: startX = 0; startY = random.nextInt(screenHeight); break;
                case 3: startX = screenWidth; startY = random.nextInt(screenHeight); break;
            }
            shurikens.add(new Shuriken(startX, startY, playerX, playerY));
        }
    }
}
