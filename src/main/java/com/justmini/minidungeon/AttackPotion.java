package com.justmini.minidungeon;

public class AttackPotion extends Item {

    private int attackBoost;

    public AttackPotion(int x, int y) {
        super(x, y, "attack potion", "/images/attack_potion.png");
        this.attackBoost = 2;
    }

    public int getAttackBoost() {
        return attackBoost;
    }
}
