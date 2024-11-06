package com.justmini.minidungeon;

public class Map {

    private Room[] rooms;
    private int currentRoomIndex;
    private GameLogic gameLogic;
    private boolean[] visitedRooms;

    public Map(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        rooms = new Room[9];
        visitedRooms = new boolean[9];

        // 방 생성
        for (int i = 0; i < 9; i++) {
            rooms[i] = new Room(i, gameLogic);
        }
        currentRoomIndex = 0; // 시작 방 인덱스 0번
        visitedRooms[currentRoomIndex] = true;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public Room getCurrentRoom() {
        return rooms[currentRoomIndex];
    }

    public boolean moveToAdjacentRoom(GameLogic.Direction direction) {
        int nextIndex = getAdjacentRoomIndex(currentRoomIndex, direction);
        if (nextIndex != -1) {
            currentRoomIndex = nextIndex;
            visitedRooms[currentRoomIndex] = true;
            return true;
        }
        return false;
    }

    public boolean[] getVisitedRooms() {
        return visitedRooms;
    }

    private int getAdjacentRoomIndex(int index, GameLogic.Direction direction) {
        int row = index / 3;
        int col = index % 3;

        switch (direction) {
            case UP:
                if (row > 0) return (row - 1) * 3 + col;
                break;
            case DOWN:
                if (row < 2) return (row + 1) * 3 + col;
                break;
            case LEFT:
                if (col > 0) return row * 3 + (col - 1);
                break;
            case RIGHT:
                if (col < 2) return row * 3 + (col + 1);
                break;
        }
        return -1;
    }
}
