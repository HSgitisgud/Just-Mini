package com.justmini.minidungeon;

public class Goblin extends Monster implements Battle {

    public Goblin(int x, int y) {
        super(x, y, "goblin", 325, 74, 42, 20, "/images/minidungeon/goblin.png");
    }

    // 할 것: 배틀 메소드 오버라이딩해서 10분의 1 확률로 아이템 훔치게 만들기
//    @Override
//    public BattleResult battle(Player player, Monster monster) {
//        SoundPlayer.playSound("/sounds/battle_sound.wav");
//        // 몬스터 전투 로직 구현
//        int damageToPlayer = Math.max(0, monster.getAtk() * (1 - (player.getDef() / (100 + player.getDef()))));
//
//        player.setHp(player.getHp() - damageToPlayer);
//
//        boolean playerDefeated = player.getHp() <= 0;
//
//        // BattleResult 객체 생성하여 반환
//        return new BattleResult(0, damageToPlayer, false, playerDefeated);
//    }
}
