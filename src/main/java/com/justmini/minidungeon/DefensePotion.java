package com.justmini.minidungeon;

public class DefensePotion extends Item {

    private int defenseBoost;

    public DefensePotion(int x, int y) {
        super(x, y, "defense potion", "/images/defense_potion.png");
        this.defenseBoost = 2;
    }

    public int getDefenseBoost() {
        return defenseBoost;
    }
}
