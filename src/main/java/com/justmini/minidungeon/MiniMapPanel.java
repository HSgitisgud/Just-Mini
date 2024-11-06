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

            g.fillRect(x, y, cellSize, cellSize);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, cellSize, cellSize);
        }
    }
}
