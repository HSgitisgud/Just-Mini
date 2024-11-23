package com.justmini.minidungeon;

import javax.swing.*;
import java.awt.*;

public class MiniMapPanel extends JPanel {

    private GameLogic gameLogic;

    public MiniMapPanel(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        setPreferredSize(new Dimension(200, 200));
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameLogic == null) {
            return;
        }

        boolean[] visitedRooms = gameLogic.getVisitedRooms();
        Room[] rooms = gameLogic.getMap().getRooms();

        // 현재 방 가져오기
        Room currentRoom = gameLogic.getMap().getCurrentRoom();

        int cellSize = 40;
        for (int i = 0; i < rooms.length; i++) {
            int x = (i % 3) * cellSize;
            int y = (i / 3) * cellSize;

            Room room = rooms[i];

            if (visitedRooms[i]) {
                if (room.getMonsters().isEmpty()) {
                    g.setColor(Color.GREEN); // 모든 몬스터를 처치한 방
                } else {
                    g.setColor(Color.YELLOW); // 방문했으나 몬스터가 남은 방
                }
            } else {
                g.setColor(Color.GRAY); // 방문하지 않은 방
            }

            // 방 그리기
            g.fillRect(x, y, cellSize, cellSize);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, cellSize, cellSize);

            // 현재 방에 빨간 동그라미 표시
            if (room == currentRoom) {
                g.setColor(Color.RED);
                int circleSize = cellSize / 2;
                int circleX = x + (cellSize - circleSize) / 2;
                int circleY = y + (cellSize - circleSize) / 2;
                g.fillOval(circleX, circleY, circleSize, circleSize);
            }
        }
    }
}
