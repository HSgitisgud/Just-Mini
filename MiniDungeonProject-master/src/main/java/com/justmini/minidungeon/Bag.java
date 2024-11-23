package com.justmini.minidungeon;

import java.util.ArrayList;
import java.util.List;

public class Bag {

    private List<Item> items;
    private int capacity;

    public Bag() {
        items = new ArrayList<>();
        capacity = 10; // 최대 아이템 개수
    }

    public boolean addItem(Item item) {
        if (items.size() < capacity) {
            items.add(item);
            return true;
        }
        return false;
    }

    public List<Item> getItems() {
        return items;
    }
}
