package com.justmini.minidodge;

import java.util.ArrayList;
import java.util.Random;

public class Pattern1 implements Pattern {
    private Random random = new Random();

    @Override
    public void spawnShurikens(ArrayList<Shuriken> arrows, int playerX, int playerY, int width, int height) {
        int numArrows = 8;
        int gap = 65;

        int side = random.nextInt(4); // 0: 상단, 1: 하단, 2: 좌측, 3: 우측

        switch (side) {
            case 0: // 위에서 아래로
                int startYTop = 0;
                for (int i = 0; i < numArrows; i++) {
                    int startX = i * gap + (width - (numArrows * gap)) / 2;
                    arrows.add(new Shuriken(startX, startYTop, "DOWN"));
                }
                break;

            case 1: // 아래에서 위로
                int startYBottom = height;
                for (int i = 0; i < numArrows; i++) {
                    int startX = i * gap + (width - (numArrows * gap)) / 2;
                    arrows.add(new Shuriken(startX, startYBottom, "UP"));
                }
                break;

            case 2: // 좌에서 우로
                int startXLeft = 0;
                for (int i = 0; i < numArrows; i++) {
                    int startY = i * gap + (height - (numArrows * gap)) / 2;
                    arrows.add(new Shuriken(startXLeft, startY, "RIGHT"));
                }
                break;

            case 3: // 우에서 좌로
                int startXRight = width;
                for (int i = 0; i < numArrows; i++) {
                    int startY = i * gap + (height - (numArrows * gap)) / 2;
                    arrows.add(new Shuriken(startXRight, startY, "LEFT"));
                }
                break;
        }
    }
}



