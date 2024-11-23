package com.justmini.minidungeon;

public class AttackPotion extends Item {

    private int attackBoost;

    public AttackPotion(int x, int y) {
        super(x, y, "attack potion", "/images/minidungeon/attack_potion.png");
        this.attackBoost = 5;
    }

    public int getAttackBoost() {
        return attackBoost;
    }
}
