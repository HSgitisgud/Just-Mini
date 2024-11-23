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

        // 타이머나 스레드가 있다면 여기서 종료 처리
        // 예를 들어, Timer 객체가 있다면 timer.stop(); 호출
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
                        return; // 게임이 끝났으므로 메서드 종료
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

                    mainFrame.updateCombatLog(monster.getName() + " deals " + result.getDamageToPlayer() + " damage to player"
                            + ". Now player has " + player.getHp() + " HP");

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

    // 배틀 메소드인데 ConcurrentModificationException도 그렇고, battle_sound.wav 씹히는 문제도 그렇고 다 여기서 문제 생기는듯
    // 일단 movePlayer랑 moveMonsters 두개에서 이거 참조하는데 거기서 concurrency 문제 생기는 걸로 보이고, 그거의 연장선상에서 소리도 씹히는 걸로 보임.
    // 정확히는 몬스터 리스트가 동시에 삭제되려고 해서 생기는 문제네.
    // 이런 경우 기본적인 해결 방안으로 thread 동기화 맞춰주는 방식을 쓸 수 있겠지만 그럼 어디서부터 손대야 하는지도 모르겠다.
    // 내가 생각하는 해결 방안은 게임 로직에서 배틀 메소드 없애버리고, 배틀 인터페이스를 만든 다음
    // 플레이어, 몬스터 각 클래스들이 그거 implement 받아서 각자 다른 방식으로 공격하게 하는 것이 옳은 해결법으로 보임.
    // 몬스터 리스트 건드는 거는 플레이어의 공격에서만 일어나도록 하면 되겠고.
    // 몰라. 일단 작동하니 내비두고 나중에 누구한테 물어보던지 하자. 11/16
    // CopyOnWriteArrayList 쓰면 된다해서 써봤는데 해결 안되는디? 그냥 인터페이스 만들어야겠다. 만들면 주석 지우고.

    //Battle 인터페이스 만들어서 이제 이건 레거시인데 열심히 만든거라 뭔가 지우기 아깝네ㅋㅋ 이래서 레거시 코드가 남는구나.
//    private void battle(Monster monster) {
//        if (!gameRunning) return; // 게임이 종료되었으면 메소드 종료
//
//        SoundPlayer.playSound("/sounds/battle_sound.wav");
//
//        // 전투 로직 구현. 롤 방식으로 방어력 계산 바꿔봄. 11/15
//        int damageToMonster = (int) Math.max(0, player.getAtk() * (1 - ((double) monster.getDef() / (100 + monster.getDef()))));
//        int damageToPlayer = (int) Math.max(0, monster.getAtk() * (1 - ((double) player.getDef() / (100 + player.getDef()))));
//        // 다 좋은데 밸런스 잡기가 너무 어렵네 기존 (atk - def) 방식은 def 망겜이 되고, 롤 방식은 def가 큰 의미를 가지지 못하고...
//        // 일단 롤 방식이니깐 대충 플레이어 공방체를 가렌 1렙 능력치로 설정해보면 좋을듯, 적은 정글몹 능력치로 설정해보고
//
//        monster.setHp(monster.getHp() - damageToMonster);
//        player.setHp(player.getHp() - damageToPlayer);
//
//        mainFrame.updateCombatLog("Player deals " + damageToMonster + " damage " + "to a " + monster.getName()
//                + ". Now " + monster.getName() + " has " + monster.getHp() + " HP");
//        mainFrame.updateCombatLog("The " + monster.getName() + " deals " + damageToPlayer + " damage to the player");
//
//        if (monster.getHp() <= 0) {
//            currentRoom.removeMonster(monster);
//            allMonsters.remove(monster);
//            mainFrame.updateCombatLog("You've killed the " + monster.getName() + "!");
//            int previousLevel = player.getLevel();
//            player.addExp(monster.getExp());
//
//            // 레벨 업 여부 확인
//            if (player.getLevel() > previousLevel) {
//                mainFrame.updateCombatLog("Level up! Current Level: " + player.getLevel());
//                SoundPlayer.playSound("/sounds/level_up.wav");
//            }
//
//            mainFrame.updatePlayerStats();
//            currentRoom.updateMonsterPanel();
//
//            // 모든 몬스터를 처치했는지 확인
//            if (allMonsters.isEmpty()) {
//                gameWon();
//            }
//        }
//
//        if (player.getHp() <= 0) {
//            mainFrame.updateCombatLog("The Player is down... Game Over!");
//            // 게임 종료 처리
//            SoundPlayer.playSound("/sounds/game_over.wav");
//            JOptionPane.showMessageDialog(mainFrame, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
//
//            // 게임 로직 종료
//            stopGame();
//
//            // 게임 프레임에서 이벤트 리스너 제거
//            mainFrame.stopGame();
//
//            // 현재 게임 창 닫기
//            mainFrame.dispose();
//            // 메인 화면 표시
//            new JustMiniMain();
//        }
//    }

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
