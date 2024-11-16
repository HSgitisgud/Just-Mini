package com.justmini.util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {

    private static Map<String, Clip> soundCache = new HashMap<>();
    private static Clip backgroundClip;

    // 배경 음악 재생 메서드
    public static void playBackgroundMusic(String soundFilePath) {
        try {
            if (backgroundClip != null && backgroundClip.isRunning()) {
                backgroundClip.stop();
                backgroundClip.close();
            }

            URL soundUrl = SoundPlayer.class.getResource(soundFilePath);
            if (soundUrl == null) {
                System.err.println("No sound file at: " + soundFilePath);
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioInputStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // 배경 음악 반복 재생
            backgroundClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // 배경 음악 중단 메서드
    public static void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;
        }
    }

    // 사운드 재생 메서드
    public static void playSound(String soundFilePath) {
        try {
            Clip clip = soundCache.get(soundFilePath);
            if (clip == null) {
                // 리소스에서 사운드 파일 로드
                URL soundUrl = SoundPlayer.class.getResource(soundFilePath);
                if (soundUrl == null) {
                    System.err.println("No sound file at: " + soundFilePath);
                    return;
                }

                // 오디오 입력 스트림 생성
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);

                // Clip 생성 및 열기
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                // 사운드 캐시에 저장
                soundCache.put(soundFilePath, clip);
            }

            // 특정 사운드가 재생될 때 배경 음악 중단
            if (soundFilePath.equals("/sounds/battle_sound1.wav")) {
                stopBackgroundMusic();
            }

            else if (soundFilePath.equals("/sounds/game_over.wav")) {
                stopBackgroundMusic();
            }

            else if (soundFilePath.equals("/sounds/game_won.wav")) {
                stopBackgroundMusic();
            }

            else if (soundFilePath.equals("/sounds/button1.wav")) {
                stopBackgroundMusic();
            }

            else if (soundFilePath.equals("/sounds/ruins.wav")) {
                stopBackgroundMusic();
            }

            else if (soundFilePath.equals("/sounds/ninja.wav")) {
                stopBackgroundMusic();
            }

            else if (soundFilePath.equals("/sounds/main_theme.wav")) {
                stopBackgroundMusic();
            }


            clip.setFramePosition(0);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
