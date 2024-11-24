package com.justmini.minidungeon;

import com.justmini.util.SoundPlayer;
import java.util.Random;

public class Slime extends Monster{

    public Slime(int x, int y) {
        super(x, y, "slime", 141, 39, 20, 10, "/images/minidungeon/slime.png");
    }

    @Override
    public BattleResult battle(Player player, Monster monster) {

        Random random = new Random();
        int chance = random.nextInt(5); // 0부터 4까지의 난수 생성

        if (chance == 0) {
            // 공격하지 않음
            SoundPlayer.playSound("/sounds/slime_idle.wav"); // 슬라임이 공격하지 않을 때의 사운드
            // 전투 로그에 표시할 메시지 반환
            return new BattleResult(0, 0, false, false);
        } else {
            // 일반적인 공격 로직
            SoundPlayer.playSound("/sounds/battle_sound2.wav");

            // 방어력 비율 계산
            double defenseRatio = (double) player.getDef() / (100 + player.getDef());

            // 데미지 계산
            int damageToPlayer = (int) Math.max(0, getAtk() * (1 - defenseRatio));

            player.setHp(player.getHp() - damageToPlayer);

            boolean playerDefeated = player.getHp() <= 0;

            // BattleResult 객체 생성하여 반환
            return new BattleResult(0, damageToPlayer, false, playerDefeated);
        }
    }
}

