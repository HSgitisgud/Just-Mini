package com.justmini.minipanda;

import com.justmini.main.JustMiniMain;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MiniPanda {
    private JFrame frame;

    public MiniPanda() {
        // Initialize and display the game window
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Mini Panda");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setResizable(false);

            GamePanel gamePanel = new GamePanel();
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            gamePanel.startGame();

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // Close the current game window
                    frame.dispose();

                    // End the game loop
                    gamePanel.endGame();

                    // Show the main menu
                    new JustMiniMain();
                }
            });
        });
    }

    // Optional main method if you still want to run the game standalone
    public static void main(String[] args) {
        new MiniPanda();
    }
}
