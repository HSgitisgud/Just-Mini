package com.justmini.minipanda;

import com.justmini.main.JustMiniMain;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MiniPanda {
    private JFrame frame; // 게임 창 (프레임)

    public MiniPanda() {
        // 게임 창 초기화 및 표시
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Mini Panda"); // "Mini Panda"라는 제목의 창 생성
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창 닫힘 설정 (현재 창만 닫힘)
            frame.setResizable(false); // 창 크기 변경 불가

            GamePanel gamePanel = new GamePanel(); // 게임 화면을 표시할 GamePanel 객체 생성
            frame.add(gamePanel); // GamePanel을 창에 추가
            frame.pack(); // GamePanel 크기에 맞게 창 크기 조정
            frame.setLocationRelativeTo(null); // 창을 화면 중앙에 위치
            frame.setVisible(true); // 창 표시

            gamePanel.startGame(); // 게임 시작

            // 창 닫힘 이벤트 처리
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // 현재 게임 창 닫기
                    frame.dispose();

                    // 게임 루프 종료
                    gamePanel.endGame();

                    // 메인 메뉴 표시
                    new JustMiniMain();
                }
            });
        });
    }

    // 선택적인 main 메서드: 게임을 독립적으로 실행할 때 사용
    public static void main(String[] args) {
        new MiniPanda(); // MiniPanda 인스턴스 생성하여 게임 실행
    }
}