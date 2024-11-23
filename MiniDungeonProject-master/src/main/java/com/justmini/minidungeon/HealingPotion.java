package com.justmini.minidungeon;

public class HealingPotion extends Item {

    private int healAmount;

    public HealingPotion(int x, int y) {
        super(x, y, "healing potion", "/images/minidungeon/healing_potion.png");
        this.healAmount = 100;
    }

    public int getHealAmount() {
        return healAmount;
    }
}
