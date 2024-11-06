package com.justmini.minidungeon;

public class Player {

    private int x, y;
    private int level, exp;
    private int expToLevelUp;
    private int hp, maxHp;
    private int atk, def;
    private Bag bag;
    private Room currentRoom;

    public Player() {
        level = 1;
        exp = 0;
        expToLevelUp = 20;
        maxHp = 100;
        hp = maxHp;
        atk = 8;
        def = 4;
        bag = new Bag();
        x = 0;
        y = 4;
    }

    // getter, setter들
    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }

    public int getLevel() { return level; }
    public int getExp() { return exp; }
    public int getExpToLevelUp() { return expToLevelUp; }
    public void addExp(int exp) {
        this.exp += exp;
        checkLevelUp();
    }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getMaxHp() { return maxHp; }

    public int getAtk() { return atk; }
    public int getDef() { return def; }

    public Bag getBag() { return bag; }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public void increaseAtk(int amount) {
        this.atk += amount;
    }

    public void increaseDef(int amount) {
        this.def += amount;
    }

    // 레벨 업은 내부에서만 처리
    private void checkLevelUp() {
        while (this.exp >= expToLevelUp) {
            this.exp -= expToLevelUp;
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        expToLevelUp *= 2; // 다음 레벨 업까지 필요한 경험치 두 배로 증가
        maxHp += 10;
        atk += 2;
        def += 2;
    }
}
