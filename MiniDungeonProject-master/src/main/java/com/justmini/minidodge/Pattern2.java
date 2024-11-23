package com.justmini.minidodge;

import java.util.ArrayList;

public class Pattern2 implements Pattern {
    @Override
    public void spawnShurikens(ArrayList<Shuriken> shurikens, int playerX, int playerY, int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;

        // 각 위치에서의 시작 좌표와 방향
        Object[][] positions = {
            { centerX, 0, "DOWN" },                     // 12시에서 아래로
            { width, 0, "DOWN_LEFT" },                  // 1시에서 왼쪽 아래로 (7시 방향)
            { width, centerY, "LEFT" },                 // 3시에서 왼쪽으로
            { width, height, "UP_LEFT" },               // 5시에서 왼쪽 위로 (11시 방향)
            { centerX, height, "UP" },                  // 6시에서 위로
            { 0, height, "UP_RIGHT" },                  // 7시에서 오른쪽 위로 (1시 방향)
            { 0, centerY, "RIGHT" },                    // 9시에서 오른쪽으로
            { 0, 0, "DOWN_RIGHT" }                      // 11시에서 오른쪽 아래로 (5시 방향)
        };

        // 각 위치에서 수리검을 생성하여 지정된 방향으로 발사
        for (Object[] pos : positions) {
            int startX = (int) pos[0];
            int startY = (int) pos[1];
            String direction = (String) pos[2];

            shurikens.add(new Shuriken(startX, startY, direction));
        }
    }
}

