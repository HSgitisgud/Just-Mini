package com.justmini.minidungeon;

import com.justmini.main.JustMiniMain;
import com.justmini.util.SoundPlayer;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GameLogic {

    private Player player;
    private Map map;
    private MiniDungeonGame mainFrame;
    private Room currentRoom;
    private List<Monster> allMonsters; // 모든 몬스터를 저장하는 리스트
    private boolean gameRunning = true; // 게임 실행 상태를 나타내는 플래그

    public GameLogic(Player player, MiniDungeonGame mainFrame) {
        this.player = player;
        this.mainFrame = mainFrame;
        this.map = new Map(this);
        this.currentRoom = map.getCurrentRoom();
        // 모든 몬스터 수집
        collectAllMonsters();
    }

    public Map getMap() {
        return map;
    }

    private void collectAllMonsters() {
        allMonsters = new ArrayList<>();
        for (Room room : map.getRooms()) {
            allMonsters.addAll(room.getMonsters());
        }
    }

    // 게임 종료 메소드
    public void stopGame() {
        gameRunning = false; // 게임 실행 상태를 false로 설정
    }

    public void handlePlayerInput(int keyCode) {
        if (!gameRunning) return; // 게임이 종료되었으면 메소드 종료

        Direction direction = null;
        switch (keyCode) {
            case KeyEvent.VK_UP:
                direction = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                direction = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                direction = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                direction = Direction.RIGHT;
                break;
            default:
                // 숫자 키 처리
                if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
                    int index = keyCode - KeyEvent.VK_0;
                    useItem(index);
                }
                return; // 방향 이동이 아니므로 메소드 종료
        }
        if (direction != null) {
            movePlayer(direction);
        }
    }

    public void movePlayer(Direction direction) {
        if (!gameRunning) return; // 게임이 종료되었으면 메소드 종료

        int newX = player.getX() + direction.dx;
        int newY = player.getY() + direction.dy;

        if (currentRoom.isValidPosition(newX, newY)) {
            // 이동하려는 위치에 몬스터가 있는지 확인
            Monster monster = currentRoom.getMonsterAt(newX, newY);
            if (monster != null) {
                // 몬스터가 있으면 이동하지 않고 전투 발생
                mainFrame.updateCombatLog("You attacked the monster!");

                BattleResult result = player.battle(player, monster); // 전투 수행

                mainFrame.updateCombatLog("Player deals " + result.getDamageToMonster() + " damage to " + monster.getName()
                        + ". Now " + monster.getName() + " has " + monster.getHp() + " HP");

                // 몬스터가 죽었는지 확인
                if (result.isMonsterDefeated()) {
                    currentRoom.removeMonster(monster);
                    allMonsters.remove(monster);
                    mainFrame.updateCombatLog("You've killed the " + monster.getName() + "!");
                    SoundPlayer.playSound("/sounds/monster_grunt.wav");
                    int previousLevel = player.getLevel();
                    player.addExp(monster.getExp());

                    // 레벨 업 여부 확인
                    if (player.getLevel() > previousLevel) {
                        mainFrame.updateCombatLog("Level up! Current Level: " + player.getLevel());
                        SoundPlayer.playSound("/sounds/level_up.wav");
                    }

                    mainFrame.updatePlayerStats();
                    currentRoom.updateMonsterPanel();

                    // 모든 몬스터를 처치했는지 확인
                    if (allMonsters.isEmpty()) {
                        gameWon();
                        return; // 게임이 끝났으므로 메소드 종료
                    }
                }
            } else {
                // 몬스터가 없으면 이동
                player.setPosition(newX, newY);
                currentRoom.updatePlayerPosition();
                // 아이템 습득 및 기타 이벤트 체크
                checkForEvents();
            }
        } else {
            // 방 경계를 넘어가는 경우
            moveToNextRoom(direction);
        }

        moveMonsters(); // 매 입력별 몬스터 움직임 추가

        mainFrame.updatePlayerStats();
    }

    public void moveMonsters() {
        if (!gameRunning) return; // 게임이 종료되었으면 메소드 종료

        List<Monster> monsters = currentRoom.getMonsters();

        for (Monster monster : monsters) {
            // 플레이어와의 거리 계산
            int dx = Math.abs(monster.getX() - player.getX());
            int dy = Math.abs(monster.getY() - player.getY());

            if (dx <= 3 && dy <= 3) {
                // 플레이어에게 다가가기 위한 방향 계산
                int moveX = 0;
                int moveY = 0;

                if (dx > dy) {
                    // 가로로 이동
                    if (monster.getX() < player.getX()) {
                        moveX = 1;
                    } else if (monster.getX() > player.getX()) {
                        moveX = -1;
                    }
                } else {
                    // 세로로 이동
                    if (monster.getY() < player.getY()) {
                        moveY = 1;
                    } else if (monster.getY() > player.getY()) {
                        moveY = -1;
                    }
                }

                // 이동하려는 위치가 유효한지 확인
                int newX = monster.getX() + moveX;
                int newY = monster.getY() + moveY;

                if (currentRoom.isValidPosition(newX, newY) && !isOccupiedByMonster(newX, newY) && !isPlayerPosition(newX, newY)) {
                    monster.setPosition(newX, newY);
                }
                // 플레이어 위치에 도달한 경우 전투 처리
                else if (isPlayerPosition(newX, newY)) {
                    mainFrame.updateCombatLog("You are attacked by the monster!");
                    BattleResult result = monster.battle(player, monster);

                    if (result.getDamageToPlayer() == 0 && result.getDamageToMonster() == 0) {
                        mainFrame.updateCombatLog("The slime seems confused and doesn't attack!");
                    } else {
                        mainFrame.updateCombatLog(monster.getName() + " deals " + result.getDamageToPlayer() + " damage to Player"
                                + ". Now Player has " + player.getHp() + " HP");
                    }

                    if (player.getHp() <= 0) {
                        mainFrame.updateCombatLog("The Player is down... Game Over!");
                        mainFrame.updatePlayerStats();
                        // 게임 종료 처리
                        if (!gameRunning) return;
                        SoundPlayer.playSound("/sounds/game_over.wav");
                        JOptionPane.showMessageDialog(mainFrame, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

                        // 게임 로직 종료
                        stopGame();

                        // 게임 프레임에서 이벤트 리스너 제거
                        mainFrame.stopGame();

                        // 현재 게임 창 닫기
                        mainFrame.dispose();
                        // 메인 화면 표시
                        new JustMiniMain();
                    } else if (player.getHp() <= 200) {
                        mainFrame.updateCombatLog("Low HP! You are in danger!");
                        SoundPlayer.playSound("/sounds/player_grunt2.wav");
                    }

                    // 전투 후 상태 업데이트
                    mainFrame.updatePlayerStats();
                    currentRoom.updateMonsterPanel();
                }
            }
        }
        currentRoom.updateMonsterPanel();
    }

    private boolean isOccupiedByMonster(int x, int y) {
        for (Monster m : currentRoom.getMonsters()) {
            if (m.getX() == x && m.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlayerPosition(int x, int y) {
        return player.getX() == x && player.getY() == y;
    }

    private void useItem(int index) {
        if (!gameRunning) return; // 게임이 종료되었으면 메소드 종료

        List<Item> items = player.getBag().getItems();
        if (index >= 0 && index < items.size()) {
            Item item = items.get(index);
            // 아이템 사용 로직 구현
            if (item instanceof HealingPotion) {
                HealingPotion potion = (HealingPotion) item;
                player.setHp(Math.min(player.getHp() + potion.getHealAmount(), player.getMaxHp()));
                mainFrame.updateCombatLog("You used a " + item.getName() + " to restore health");
                SoundPlayer.playSound("/sounds/gulp_sound.wav");
            } else if (item instanceof AttackPotion) {
                AttackPotion potion = (AttackPotion) item;
                player.increaseAtk(potion.getAttackBoost());
                mainFrame.updateCombatLog("You used a " + item.getName() + " to increase your attack power");
                SoundPlayer.playSound("/sounds/gulp_sound.wav");
            } else if (item instanceof DefensePotion) {
                DefensePotion potion = (DefensePotion) item;
                player.increaseDef(potion.getDefenseBoost());
                mainFrame.updateCombatLog("You used a " + item.getName() + " to increase your defense");
                SoundPlayer.playSound("/sounds/gulp_sound.wav");
            } else {
                mainFrame.updateCombatLog("You can't use a " + item.getName());
                SoundPlayer.playSound("/sounds/negative.wav");
                return;
            }
            // 아이템 사용 후 가방에서 제거
            items.remove(index);
            mainFrame.updateItemList();
            mainFrame.updatePlayerStats();
        } else {
            mainFrame.updateCombatLog("There are no items at that index");
            SoundPlayer.playSound("/sounds/negative.wav");
        }
    }

    private void checkForEvents() {
        if (!gameRunning) return; // 게임이 종료되었으면 메소드 종료

        // 아이템 습득
        Item item = currentRoom.getItemAt(player.getX(), player.getY());
        if (item != null) {
            if (player.getBag().addItem(item)) {
                currentRoom.removeItem(item);
                SoundPlayer.playSound("/sounds/obtain.wav");
                mainFrame.updateCombatLog("You obtained a " + item.getName());
                mainFrame.updateItemList();
                currentRoom.updateItemPanel();
            } else {
                mainFrame.updateCombatLog("The bag is full and you cannot obtain the item");
                SoundPlayer.playSound("/sounds/negative.wav");
            }
        }
    }

    private void gameWon() {
        if (!gameRunning) return; // 게임이 종료되었으면 메소드 종료

        // 게임 로직 종료
        stopGame();

        SoundPlayer.playSound("/sounds/game_won.wav");

        mainFrame.updateCombatLog("Congratulations! Revenge has been achieved on all goblins!");
        // 게임 종료 처리
        JOptionPane.showMessageDialog(mainFrame, "Congratulations! Revenge has been achieved on all goblins!", "Game Won", JOptionPane.INFORMATION_MESSAGE);
        // 현재 게임 창 닫기
        mainFrame.dispose();
        // 메인 화면 표시
        new JustMiniMain();
    }

    private void moveToNextRoom(Direction direction) {
        if (!gameRunning) return; // 게임이 종료되었으면 메소드 종료

        if (map.moveToAdjacentRoom(direction)) {
            currentRoom = map.getCurrentRoom();
            player.setCurrentRoom(currentRoom);
            // 플레이어 위치 설정 (반대쪽에서 진입)
            switch (direction) {
                case UP:
                    player.setPosition(player.getX(), currentRoom.getHeight() - 1);
                    break;
                case DOWN:
                    player.setPosition(player.getX(), 0);
                    break;
                case LEFT:
                    player.setPosition(currentRoom.getWidth() - 1, player.getY());
                    break;
                case RIGHT:
                    player.setPosition(0, player.getY());
                    break;
            }
            mainFrame.setGamePanel(currentRoom.getRoomPanel());
            mainFrame.updateRoomName(currentRoom.getName());
            mainFrame.updateMiniMap();
        } else {
            mainFrame.updateCombatLog("You can't go further");
            SoundPlayer.playSound("/sounds/negative.wav");
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public boolean[] getVisitedRooms() {
        return map.getVisitedRooms();
    }

    // 방향 enum
    public enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        public final int dx;
        public final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }
}
