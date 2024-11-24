package com.justmini.minipangpang;

import com.justmini.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ButtonClickListener implements ActionListener {
    private JButton button;
    private int row;
    private int col;
    private MiniPangPang miniPangPang;
    private static JButton lastClickedButton = null; // 마지막으로 클릭된 버튼 추적
    private static ArrayList<JButton> adjacentButtons = new ArrayList<>(); // 인접한 버튼들 저장

    public ButtonClickListener(JButton button, int row, int col, MiniPangPang miniPangPang) {
        this.button = button;
        this.row = row;
        this.col = col;
        this.miniPangPang = miniPangPang;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SoundPlayer.playSound("/sounds/button2.wav");

        // 이전에 강조된 버튼과 인접한 버튼들의 스타일 초기화
        if (miniPangPang.selectedButton == null) {
            if (lastClickedButton != null) {
                lastClickedButton.setBorder(null);
                lastClickedButton.setBackground(Color.WHITE);
            }
            for (JButton adjButton : adjacentButtons) {
                adjButton.setBackground(Color.WHITE);
            }
            adjacentButtons.clear();
        }

        // 현재 클릭된 버튼 강조
        button.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        button.setBackground(new Color(255, 200, 200)); // 옅은 빨간색 배경
        lastClickedButton = button;

        // 첫 번째 버튼 선택
        if (miniPangPang.selectedButton == null) {
            miniPangPang.selectedButton = button;
            miniPangPang.selectedRow = row;
            miniPangPang.selectedCol = col;

            // 인접한 버튼들 강조
            highlightAdjacentButtons();
        } else {
            // 두 번째 버튼 클릭 시

            // 같은 버튼을 두 번 클릭한 경우
            if (miniPangPang.selectedButton == button) {
                resetSelection();
                return;
            }

            // 인접한 버튼인지 확인
            if (adjacentButtons.contains(button)) {
                // 인접한 버튼이면 교환 수행
                Icon tempIcon = button.getIcon();
                button.setIcon(miniPangPang.selectedButton.getIcon());
                miniPangPang.selectedButton.setIcon(tempIcon);

                // 교환 후 매칭 검사
                if (miniPangPang.checkMatchesAtPositions(
                        row, col,
                        miniPangPang.selectedRow, miniPangPang.selectedCol)) {
                    // 매칭 발생 시 점수 업데이트
                    miniPangPang.updateScoreLabel();
                } else {
                    // 매칭이 없으면 교환 취소
                    tempIcon = button.getIcon();
                    button.setIcon(miniPangPang.selectedButton.getIcon());
                    miniPangPang.selectedButton.setIcon(tempIcon);
                }
            }

            // 선택 상태 초기화
            resetSelection();
        }
    }

    private void highlightAdjacentButtons() {
        int[][] directions = {
                {-1, 0}, // 위
                {1, 0},  // 아래
                {0, -1}, // 왼쪽
                {0, 1}   // 오른쪽
        };

        for (int[] dir : directions) {
            int newRow = miniPangPang.selectedRow + dir[0];
            int newCol = miniPangPang.selectedCol + dir[1];

            if (newRow >= 0 && newRow < miniPangPang.GRID_SIZE && newCol >= 0 && newCol < miniPangPang.GRID_SIZE) {
                JButton adjButton = miniPangPang.buttons[newRow][newCol];
                adjButton.setBackground(new Color(255, 220, 220)); // 더 옅은 빨간색 배경
                adjacentButtons.add(adjButton);
            }
        }
    }

    private void resetSelection() {
        // 선택된 버튼 스타일 초기화
        if (miniPangPang.selectedButton != null) {
            miniPangPang.selectedButton.setBorder(null);
            miniPangPang.selectedButton.setBackground(Color.WHITE);
        }

        // 인접한 버튼들 스타일 초기화
        for (JButton adjButton : adjacentButtons) {
            adjButton.setBackground(Color.WHITE);
        }
        adjacentButtons.clear();

        // 현재 버튼 스타일 초기화
        if (button != miniPangPang.selectedButton) {
            button.setBorder(null);
            button.setBackground(Color.WHITE);
        }

        miniPangPang.selectedButton = null;
        miniPangPang.selectedRow = -1;
        miniPangPang.selectedCol = -1;
        lastClickedButton = null;
    }
}
