package com.justmini.minidungeon;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Room {

    private int index;
    private String name;
    private TerrainPanel terrainPanel;
    private ItemPanel itemPanel;
    private MonsterPanel monsterPanel;
    private PlayerPanel playerPanel;
    private JLayeredPane roomPanel;
    private GameLogic gameLogic;

    private List<Item> items;
    private List<Monster> monsters;
    private int tileSize = 64;
    private int verticalTiles = 9;
    private int horizontalTiles = 17;

    public Room(int index, GameLogic gameLogic) {
        this.index = index;
        this.name = " " + index;
        this.gameLogic = gameLogic;

        // 타일 생성
//        Tile[][] tiles = new Tile[verticalTiles][horizontalTiles];
//        for (int y = 0; y < verticalTiles; y++) {
//            for (int x = 0; x < horizontalTiles; x++) {
//                tiles[y][x] = new Tile(x, y, true, "/images/minidungeon/grass_tile.png");
//            }
//        }
        Tile[][] tiles = new Tile[verticalTiles][horizontalTiles];
        for (int y = 0; y < verticalTiles; y++) {
            for (int x = 0; x < horizontalTiles; x++) {
                String imagePath;

                boolean isTopEdge = (y == 0);
                boolean isBottomEdge = (y == verticalTiles - 1);
                boolean isLeftEdge = (x == 0);
                boolean isRightEdge = (x == horizontalTiles - 1);

                // 코너 타일 처리
                if (isTopEdge && isLeftEdge) {
                    imagePath = "/images/minidungeon/grass_top_left.png";
                } else if (isTopEdge && isRightEdge) {
                    imagePath = "/images/minidungeon/grass_top_right.png";
                } else if (isBottomEdge && isLeftEdge) {
                    imagePath = "/images/minidungeon/grass_bottom_left.png";
                } else if (isBottomEdge && isRightEdge) {
                    imagePath = "/images/minidungeon/grass_bottom_right.png";
                }
                // 가장자리 타일 처리
                else if (isTopEdge) {
                    imagePath = "/images/minidungeon/grass_top.png";
                } else if (isBottomEdge) {
                    imagePath = "/images/minidungeon/grass_bottom.png";
                } else if (isLeftEdge) {
                    imagePath = "/images/minidungeon/grass_left.png";
                } else if (isRightEdge) {
                    imagePath = "/images/minidungeon/grass_right.png";
                }
                // 내부 타일
                else {
                    imagePath = "/images/minidungeon/dirt_tile.png";
                }

                tiles[y][x] = new Tile(x, y, true, imagePath);
            }
        }
        terrainPanel = new TerrainPanel(tiles, tileSize);

        // 아이템 생성
        items = generateRandomItems();
        itemPanel = new ItemPanel(items, tileSize);

        // 몬스터 생성
        monsters = generateRandomMonsters();
        monsterPanel = new MonsterPanel(monsters, tileSize);

        // 플레이어 패널 생성
        playerPanel = new PlayerPanel(gameLogic.getPlayer(), tileSize);

        // 방 패널 구성
        roomPanel = new JLayeredPane();
        int roomWidth = tiles[0].length * tileSize;
        int roomHeight = tiles.length * tileSize;
        roomPanel.setPreferredSize(new Dimension(roomWidth, roomHeight));
        roomPanel.setSize(roomWidth, roomHeight);
        roomPanel.setBackground(Color.DARK_GRAY);

        // 각 패널 위치 설정
        terrainPanel.setBounds(0, 0, roomWidth, roomHeight);
        itemPanel.setBounds(0, 0, roomWidth, roomHeight);
        monsterPanel.setBounds(0, 0, roomWidth, roomHeight);
        playerPanel.setBounds(0, 0, roomWidth, roomHeight);

        // 패널 추가 (레이어 순서 설정)
        roomPanel.add(terrainPanel, Integer.valueOf(1));
        roomPanel.add(itemPanel, Integer.valueOf(2));
        roomPanel.add(monsterPanel, Integer.valueOf(3));
        roomPanel.add(playerPanel, Integer.valueOf(4));
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public JLayeredPane getRoomPanel() {
        return roomPanel;
    }

    public String getName() {
        return name;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < horizontalTiles && y >= 0 && y < verticalTiles;
    }

    public Item getItemAt(int x, int y) {
        for (Item item : items) {
            if (item.getX() == x && item.getY() == y) {
                return item;
            }
        }
        return null;
    }

    public void removeItem(Item item) {
        items.remove(item);
        itemPanel.updateItems(items);
    }

    public Monster getMonsterAt(int x, int y) {
        for (Monster monster : monsters) {
            if (monster.getX() == x && monster.getY() == y) {
                return monster;
            }
        }
        return null;
    }

    public void removeMonster(Monster monster) {
        monsters.remove(monster);
        monsterPanel.updateMonsters(monsters);
    }

    public void updatePlayerPosition() {
        playerPanel.repaint();
    }

    public void updateItemPanel() {
        itemPanel.repaint();
    }

    public void updateMonsterPanel() {
        monsterPanel.repaint();
    }

    // 랜덤 아이템 생성
    private List<Item> generateRandomItems() {
        List<Item> itemList = new ArrayList<>();
        Random rand = new Random();
        int itemCount = rand.nextInt(3); // 0에서 2개의 아이템 생성

        for (int i = 0; i < itemCount; i++) {
            int x = rand.nextInt(horizontalTiles);
            int y = rand.nextInt(verticalTiles);
            // 아이템 종류 랜덤 선택
            int itemType = rand.nextInt(3);
            Item item;
            switch (itemType) {
                case 0:
                    item = new HealingPotion(x, y);
                    break;
                case 1:
                    item = new AttackPotion(x, y);
                    break;
                case 2:
                    item = new DefensePotion(x, y);
                    break;
                default:
                    item = new HealingPotion(x, y);
            }
            itemList.add(item);
        }
        return itemList;
    }

    // 랜덤 몬스터 생성
    private List<Monster> generateRandomMonsters() {
        List<Monster> monsterList = new ArrayList<>();
        Random rand = new Random();
        int monsterCount = rand.nextInt(3) + 1; // 1에서 3개의 몬스터 생성

        for (int i = 0; i < monsterCount; i++) {
            int x = rand.nextInt(horizontalTiles);
            int y = rand.nextInt(verticalTiles);
            // 몬스터 종류 랜덤 선택
            int monsterType = rand.nextInt(2);
            Monster monster;
            switch (monsterType) {
                case 0:
                    monster = new Slime(x, y);
                    break;
                case 1:
                    monster = new Goblin(x, y);
                    break;
                default:
                    monster = new Slime(x, y);
            }
            monsterList.add(monster);
        }
        return monsterList;
    }

    public int getWidth() {
        return horizontalTiles;
    }

    public int getHeight() {
        return verticalTiles;
    }
}
