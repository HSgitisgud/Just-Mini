package com.justmini.minidungeon;

import com.justmini.util.SoundPlayer;

public class Goblin extends Monster {

    public Goblin(int x, int y) {
        super(x, y, "goblin", 325, 74, 42, 20, "/images/minidungeon/goblin.png");
    }

    //할 것: battle() 오버라이드해서 10분의 1 확률로 플레이어 아이템 훔치게 하기
//    @Override
//    public BattleResult battle(Player player, Monster monster) {
//        SoundPlayer.playSound("/sounds/battle_sound2.wav");
//
//        // 방어력 비율 계산
//        double defenseRatio = (double) player.getDef() / (100 + player.getDef());
//
//        // 데미지 계산
//        int damageToPlayer = (int) Math.max(0, monster.getAtk() * (1 - defenseRatio));
//
//        player.setHp(player.getHp() - damageToPlayer);
//
//        boolean playerDefeated = player.getHp() <= 0;
//
//        // BattleResult 객체 생성하여 반환
//        return new BattleResult(0, damageToPlayer, false, playerDefeated);
//    }
}
