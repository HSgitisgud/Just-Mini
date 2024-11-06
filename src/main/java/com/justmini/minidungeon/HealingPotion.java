package com.justmini.minidungeon;

public class HealingPotion extends Item {

    private int healAmount;

    public HealingPotion(int x, int y) {
        super(x, y, "healing potion", "/images/healing_potion.png");
        this.healAmount = 50;
    }

    public int getHealAmount() {
        return healAmount;
    }
}
