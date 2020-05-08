package edu.cmu.cs.cs214.hw4;

import edu.cmu.cs.cs214.hw4.core.Carcassonne;
import edu.cmu.cs.cs214.hw4.core.CarcassonneImp;
import edu.cmu.cs.cs214.hw4.gui.GameBoardPanel;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BoxLayout;
import javax.swing.WindowConstants;
import javax.swing.JTextField;

public class Main {
    private static final String RECITATION_NAME = "Carcassonne";
    private static final int MAX_PLAYER_NUM = 5;
    private static final int MIN_PLAYER_NUM = 2;

    /**
     * Main function invoking a start window
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createGameBoard);
    }

    private static void createGameBoard() {
        // Create and set-up the window.
        JFrame frame = new JFrame(RECITATION_NAME);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxlayout);
        panel.setOpaque(true);

        JLabel label = new JLabel("Enter the number of the players: ");
        panel.add(label);
        JTextField field = new JTextField();
        panel.add(field);
        JButton button = new JButton("Done");
        panel.add(button);
        JLabel error = new JLabel("error: (none)");
        error.setForeground(Color.RED);
        panel.add(error);
        button.addActionListener(e -> {
            if(field.getText().trim().length() == 0) {
                error.setText("*Please enter number");
                return;
            }
            try {
                int num = Integer.parseInt(field.getText());
                if(num < MIN_PLAYER_NUM || num > MAX_PLAYER_NUM) {
                    error.setText("*Integer must between 2-5");
                    return;
                }
                showGameBoard(num);
                frame.setVisible(false);
            } catch (NumberFormatException ex) {
                error.setText("*Integer must between 2-5");
            }
        });

        frame.setContentPane(panel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private static void showGameBoard(int num) {
        // Create and set-up the window.
        JFrame frame = new JFrame(RECITATION_NAME);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Carcassonne game = new CarcassonneImp(num);

        // Create and set up the content pane.
        GameBoardPanel gamePanel = new GameBoardPanel(game);
        gamePanel.setOpaque(true);
        frame.setContentPane(gamePanel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);

    }
}
