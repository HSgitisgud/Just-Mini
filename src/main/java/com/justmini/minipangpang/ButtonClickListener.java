package com.justmini.minipangpang;

import com.justmini.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonClickListener implements ActionListener {
    private JButton button;
    private int row;
    private int col;
    private MiniPangPang miniPangPang;
    private static JButton lastClickedButton = null; // 마지막으로 클릭된 버튼 추적

    public ButtonClickListener(JButton button, int row, int col, MiniPangPang miniPangPang) {
        this.button = button;
        this.row = row;
        this.col = col;
        this.miniPangPang = miniPangPang;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SoundPlayer.playSound("/sounds/button2.wav");

        if (lastClickedButton != null) {
            // 이전 클릭된 버튼 스타일 초기화
            lastClickedButton.setBorder(null);
            lastClickedButton.setBackground(Color.WHITE);
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
        } else {
            // 두 번째 버튼 클릭 시

            // 같은 버튼을 두 번 클릭한 경우
            if (miniPangPang.selectedButton == button) {
                // 선택 초기화
                miniPangPang.selectedButton.setBorder(null);
                miniPangPang.selectedButton.setBackground(Color.WHITE);
                miniPangPang.selectedButton = null;
                miniPangPang.selectedRow = -1;
                miniPangPang.selectedCol = -1;
                lastClickedButton = null;
                return;
            }

            // 돌(고정된 돌 아이콘)인지 확인
//            Icon fixedStoneIcon = miniPangPang.getFixedStoneIcon();
//            if (button.getIcon().equals(fixedStoneIcon) || miniPangPang.selectedButton.getIcon().equals(fixedStoneIcon)) {
//                // 돌은 교환할 수 없음
//                JOptionPane.showMessageDialog(null, "돌은 교환할 수 없습니다.");
//                // 선택 상태 초기화
//                miniPangPang.selectedButton.setBorder(null);
//                miniPangPang.selectedButton.setBackground(Color.WHITE);
//
//                button.setBorder(null); // 두 번째로 클릭한 버튼의 테두리 초기화
//                button.setBackground(Color.WHITE); // 두 번째로 클릭한 버튼의 배경색 초기화
//
//                miniPangPang.selectedButton = null;
//                miniPangPang.selectedRow = -1;
//                miniPangPang.selectedCol = -1;
//                lastClickedButton = null;
//                return;
//            }

            // 인접한 버튼인지 확인
            int rowDiff = Math.abs(row - miniPangPang.selectedRow);
            int colDiff = Math.abs(col - miniPangPang.selectedCol);

            if ((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)) {
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
                    //JOptionPane.showMessageDialog(null, "No Matches.");
                }
            } else {
                // 인접하지 않으면 교환 불가
                JOptionPane.showMessageDialog(null, "You can only swap the adjacent jellies in four directions.");
            }

            // 선택 상태 초기화
            miniPangPang.selectedButton.setBorder(null);
            miniPangPang.selectedButton.setBackground(Color.WHITE);

            button.setBorder(null); // 두 번째로 클릭한 버튼의 테두리 초기화
            button.setBackground(Color.WHITE); // 두 번째로 클릭한 버튼의 배경색 초기화

            miniPangPang.selectedButton = null;
            miniPangPang.selectedRow = -1;
            miniPangPang.selectedCol = -1;
            lastClickedButton = null;
        }
    }
}