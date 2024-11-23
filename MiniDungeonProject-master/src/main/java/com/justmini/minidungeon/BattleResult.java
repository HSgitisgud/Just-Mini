package com.justmini.minidungeon;

public class BattleResult {
    private int damageToMonster;
    private int damageToPlayer;
    private boolean monsterDefeated;
    private boolean playerDefeated;

    // 생성자 및 getter, setter 추가
    public BattleResult(int damageToMonster, int damageToPlayer, boolean monsterDefeated, boolean playerDefeated) {
        this.damageToMonster = damageToMonster;
        this.damageToPlayer = damageToPlayer;
        this.monsterDefeated = monsterDefeated;
        this.playerDefeated = playerDefeated;
    }

    // Getter 메서드들
    public int getDamageToMonster() {
        return damageToMonster;
    }

    public int getDamageToPlayer() {
        return damageToPlayer;
    }

    public boolean isMonsterDefeated() {
        return monsterDefeated;
    }

    public boolean isPlayerDefeated() {
        return playerDefeated;
    }
}
