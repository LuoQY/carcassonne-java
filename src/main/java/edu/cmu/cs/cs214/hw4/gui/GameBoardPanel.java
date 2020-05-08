package edu.cmu.cs.cs214.hw4.gui;

import edu.cmu.cs.cs214.hw4.core.Coordinate;
import edu.cmu.cs.cs214.hw4.core.Direction;
import edu.cmu.cs.cs214.hw4.core.Component;
import edu.cmu.cs.cs214.hw4.core.Carcassonne;
import edu.cmu.cs.cs214.hw4.core.GameChangeListener;
import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.Tile;
import edu.cmu.cs.cs214.hw4.core.Construction;

import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BoxLayout;
import javax.swing.WindowConstants;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameBoardPanel extends JPanel implements GameChangeListener {

    private final Carcassonne game;
    private JButton[][] squares;
    private int borderX;
    private int borderY;
    private int centerX;
    private int centerY;
    private static final int LENGTH_INCR = 6;
    private JLabel currentPlayerLabel;
    private JButton start;
    private JButton newTileBtn;
    private BufferedImage pendingImage;
    private BufferedImage curTileImage;
    private JButton curTileBtn;
    private JButton submitBtn;
    private JLabel scoresLabel;
    private JLabel meepleLabel;

    private static final int MEEPLE_SIZE = 14;
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 750;
    private static final int BORDER_INITIAL_WIDTH = 9;
    private static final int BORDER_INITIAL_HEIGHT = 9;
    private static final int DRAW_MEEPLE_DISTANCE = 5;
    private static final int ROTATE_DEGREE = 90;

    /**
     * Construction of GameBoardPanel.
     * @param g implementation of Carcassonne
     */
    public GameBoardPanel(Carcassonne g) {

        borderX = BORDER_INITIAL_WIDTH;
        borderY = BORDER_INITIAL_HEIGHT;
        centerX = BORDER_INITIAL_WIDTH / 2;
        centerY = BORDER_INITIAL_HEIGHT / 2;

        game = g;
        game.addGameChangeListener(this);
        squares = new JButton[borderX][borderY];

        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.PAGE_START);


        add(createStartPanel(), BorderLayout.WEST);
        add(createSidePanel(), BorderLayout.EAST);
        add(createTileBoardPanel(), BorderLayout.CENTER);
        add(createScoreBoardPanel(), BorderLayout.PAGE_END);
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

    }

    private JPanel createStartPanel() {
        JPanel panel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxlayout);

        JLabel label = new JLabel(game.getPlayerOrder());
        panel.add(label);

        meepleLabel = new JLabel(game.getMeepleNum());
        panel.add(meepleLabel);
        start = new JButton("Start Game!");
        start.addActionListener(e -> {
            start.setEnabled(false);
            game.start();
            JButton startTile = squares[centerX][centerY];  // put in the center
            ImageIcon icon = new ImageIcon("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileD.png");
            startTile.setIcon(icon);
            startTile.setEnabled(true);
            placeNewTile();
            game.getNewTile();
        });

        panel.add(start);
        return panel;

    }

    @Override
    public void updateMeepleNum() {
        meepleLabel.setText(game.getMeepleNum());
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        JLabel title = new JLabel("Current Player: ");
        currentPlayerLabel = new JLabel("");
        panel.add(title);
        panel.add(currentPlayerLabel);
        return panel;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxlayout);
        JLabel nextLabel = new JLabel("Next Tile:");
        panel.add(nextLabel);
        newTileBtn = new JButton();
        panel.add(newTileBtn);
        JButton rotateBtn = new JButton("rotate");
        rotateBtn.addActionListener(e -> {
            if(pendingImage != null) {
                game.rotateTile();
                //pendingImage = rotateImage(pendingImage);
                newTileBtn.setIcon(new ImageIcon(pendingImage));
            }
        });
        panel.add(rotateBtn);

        JButton discardBtn = new JButton("discard");
        discardBtn.addActionListener(e -> {
            if(pendingImage != null) {
                if(submitBtn.isEnabled()) {
                    notificationDialog("You didn't put a meeple! See the right side.");
                } else {
                    game.endRound();
                    game.getNewTile();
                    game.checkGameEnd();
                }
            }
        });

        panel.add(discardBtn);

        JLabel label = new JLabel("Where to put meeple: ");
        ButtonGroup group = new ButtonGroup();
        JRadioButton leftBtn = new JRadioButton("left");
        leftBtn.setActionCommand("left");
        JRadioButton rightBtn = new JRadioButton("right");
        rightBtn.setActionCommand("right");
        JRadioButton upBtn = new JRadioButton("up");
        upBtn.setActionCommand("up");
        JRadioButton downBtn = new JRadioButton("down");
        downBtn.setActionCommand("down");
        JRadioButton centerBtn = new JRadioButton("center");
        centerBtn.setActionCommand("center");
        JRadioButton noneBtn = new JRadioButton("do nothing");
        noneBtn.setActionCommand("none");
        group.add(leftBtn);
        group.add(rightBtn);
        group.add(upBtn);
        group.add(downBtn);
        group.add(centerBtn);
        group.add(noneBtn);
        submitBtn = new JButton("submit");
        submitBtn.setEnabled(false);
        submitBtn.addActionListener(e -> {
            Direction dir = null;
            if (group.getSelection() == null) return;
            switch (group.getSelection().getActionCommand()) {
                case "left":
                    dir = Direction.LEFT;
                    break;
                case "right":
                    dir = Direction.RIGHT;
                    break;
                case "up":
                    dir = Direction.UP;
                    break;
                case "down":
                    dir = Direction.DOWN;
                    break;
                case "center":
                    dir = Direction.CENTER;
                    break;
                default:
            }
            if (game.putMeeple(dir)) { // succeed to put meeple
                submitBtn.setEnabled(false);
            }
        });
        add(panel, label, leftBtn, rightBtn, upBtn, downBtn, centerBtn, noneBtn, submitBtn);
        return panel;
    }

    @Override
    public void roundEnded(List<Coordinate> list) {
        for(Coordinate coord: list) {
            int x = coord.getX() + centerX;
            int y = coord.getY() + centerY;
            squares[x][y].setDisabledIcon(squares[x][y].getIcon());
        }
    }

    @Override
    public void drawMeeple( Direction dir) {
        int posX = 0;
        int posY = 0;
        switch (dir) {
            case LEFT:
                posX = DRAW_MEEPLE_DISTANCE;
                posY = curTileImage.getHeight() / 2 - MEEPLE_SIZE / 2;
                break;
            case RIGHT:
                posX = curTileImage.getWidth() - MEEPLE_SIZE - DRAW_MEEPLE_DISTANCE;
                posY = curTileImage.getHeight()/ 2 - MEEPLE_SIZE / 2;
                break;
            case UP:
                posX = curTileImage.getWidth() / 2 - MEEPLE_SIZE / 2;
                posY = DRAW_MEEPLE_DISTANCE;
                break;
            case DOWN:
                posX = curTileImage.getWidth() / 2 - MEEPLE_SIZE / 2;
                posY = curTileImage.getHeight() - MEEPLE_SIZE - DRAW_MEEPLE_DISTANCE;
                break;
            case CENTER:
                posX = curTileImage.getWidth() / 2 - MEEPLE_SIZE / 2;
                posY = curTileImage.getHeight() / 2 - MEEPLE_SIZE / 2;
                break;
            default:
                return;
        }
        BufferedImage copy = new BufferedImage(curTileImage.getWidth(), curTileImage.getHeight(), curTileImage.getType());
        Graphics g = copy.getGraphics();
        g.drawImage(curTileImage, 0, 0, null);
        g.setColor(game.getCurrentPlayer().getColor());
        g.fillRect(posX, posY, MEEPLE_SIZE, MEEPLE_SIZE);
        curTileBtn.setDisabledIcon(new ImageIcon(copy));
    }


    private void add (JPanel panel, java.awt.Component...comp) {
        for(java.awt.Component c: comp)
            panel.add(c);
    }

    private void placeNewTile() {
        Set<Coordinate> availPos = game.getAvailablePos();
        for(Coordinate coordinate: availPos) {
            // check squares
            checkAndExtendSquares(coordinate.getX() + centerX, coordinate.getY() + centerY);

            JButton btn = squares[coordinate.getX() + centerX][coordinate.getY() + centerY];
            btn.setText("+");
            btn.setEnabled(true);
            int x = coordinate.getX();
            int y = coordinate.getY();
            if(btn.getActionListeners().length == 0) {
                btn.addActionListener(e -> {
                    if(submitBtn.isEnabled()) {
                        notificationDialog("You didn't put a meeple! See the right side.");
                        return;
                    }
                    // determine if the position is legal
                    if(!game.putTile(x, y)) {
                        System.out.println("False to place tile");
                        return;
                    }
                    btn.setText("");
                    btn.setIcon(newTileBtn.getIcon());
                    btn.setDisabledIcon(newTileBtn.getIcon());
                    btn.setEnabled(false);
                    submitBtn.setEnabled(true);
                    curTileImage = pendingImage;
                    curTileBtn = btn;
                    game.getNewTile();
                    placeNewTile();
                });
            }
        }

    }

    @Override
    public void switchPendingImage(Tile tile) {
        if(tile == null)
            newTileBtn.setIcon(null);
        else {
            pendingImage = getTileImage(tile);
            if (pendingImage != null)
                newTileBtn.setIcon(new ImageIcon(pendingImage));
        }
    }

    private JScrollPane createTileBoardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(borderY, borderX));
        for(int i = squares[0].length - 1; i >= 0; i--) {
            for(int j = 0; j < squares.length; j++) {
                if(squares[j][i] == null) {
                    squares[j][i] = new JButton();
                    squares[j][i].setEnabled(false);
                    //squares[i][j].setSize(10, 10);
                }

                panel.add(squares[j][i]);
            }
        }
        return new JScrollPane(panel);
    }

    private JPanel createScoreBoardPanel() {
        JPanel panel = new JPanel();
        JLabel title = new JLabel("Scoreboard: ");
        scoresLabel = new JLabel(game.getScoreBoard());
        panel.add(title);
        panel.add(scoresLabel);
        return panel;
    }

    @Override
    public void updateScoreboard() {
        scoresLabel.setText(game.getScoreBoard());
    }

    @Override
    public void currentPlayerChanged(Player player) {
        currentPlayerLabel.setText(player.toString());
        currentPlayerLabel.setForeground(player.getColor());
    }

    @Override
    public void gameEnded(Map<Player, Integer> scoreboard) {
        JFrame frame = (JFrame) SwingUtilities.getRoot(this);
        StringBuilder res = new StringBuilder("Final Score:\n\n");
        int maxScore = 0;
        List<Player> winner = new ArrayList<>();
        for(Player p: scoreboard.keySet()) {
            res.append(p).append(" ").append(scoreboard.get(p)).append("\n");
            if(scoreboard.get(p) > maxScore) {
                winner.clear();
                maxScore = scoreboard.get(p);
                winner.add(p);
            } else if(scoreboard.get(p) == maxScore) {
                winner.add(p);
            }
        }
            res.append("\n").append("-------------------\n").append("Winner(s): ").append(winner).append(" !!!");
        showDialog(frame, res.toString(), "Scoreboard");
    }

    /**
     * A dialogue box to print a string.
     * @param str the string to be shown
     */
    public void notificationDialog(String str) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Display the window.
        frame.pack();
        frame.setVisible(true);

        showDialog(frame, str, "Warning");
    }

    private static void showDialog(java.awt.Component component, String message, String title) {
        JOptionPane.showMessageDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private BufferedImage getTileImage(Tile tile) {
        if(tile == null) {
            System.out.println("tile is null");
            return null;
        }
        BufferedImage img = null;
        try {
            if(tile.equals(new Tile(new Component(), new Component(), new Component(),
                    new Component(Construction.ROAD), new Component(Construction.MONASTERY), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileA.png"));
            }
            else if(tile.equals(new Tile(new Component(), new Component(), new Component(),
                    new Component(), new Component(Construction.MONASTERY), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileB.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), true))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileC.png"));
            }
            else if(tile.equals(new Tile(new Component(), new Component(Construction.CITY),
                    new Component(Construction.ROAD), new Component(Construction.ROAD),
                    new Component(Construction.ROAD), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileD.png"));
            }
            else if(tile.equals(new Tile(new Component(), new Component(), new Component(Construction.CITY),
                    new Component(), new Component(), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileE.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(), new Component(), new Component(Construction.CITY), true))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileF.png"));
            }
            else if(tile.equals(new Tile(new Component(), new Component(), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(Construction.CITY), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileG.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(), new Component(), new Component(), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileH.png"));
            }
            else if(tile.equals(new Tile(new Component(), new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), new Component(), false))){
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileI.png"));
            }
            else if(tile.equals(new Tile(new Component(), new Component(Construction.ROAD),
                    new Component(Construction.CITY), new Component(Construction.ROAD), new Component(), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileJ.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.ROAD), new Component(Construction.CITY),
                    new Component(Construction.ROAD), new Component(), new Component(), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileK.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.ROAD), new Component(Construction.CITY),
                    new Component(Construction.ROAD), new Component(Construction.ROAD),
                    new Component(Construction.INTERSECTION), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileL.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), true))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileM.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileN.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), true))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileO.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileP.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), true))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileQ.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(),
                    new Component(Construction.CITY), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileR.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), true))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileS.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.CITY), new Component(Construction.CITY),
                    new Component(Construction.CITY), new Component(Construction.ROAD),
                    new Component(Construction.CITY), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileT.png"));
            }
            else if(tile.equals(new Tile(new Component(), new Component(), new Component(Construction.ROAD),
                    new Component(Construction.ROAD), new Component(Construction.ROAD), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileU.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.ROAD), new Component(), new Component(),
                    new Component(Construction.ROAD), new Component(), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileV.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.ROAD), new Component(Construction.ROAD),
                    new Component(), new Component(Construction.ROAD),
                    new Component(Construction.VILLAGE), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileW.png"));
            }
            else if(tile.equals(new Tile(new Component(Construction.ROAD), new Component(Construction.ROAD),
                    new Component(Construction.ROAD), new Component(Construction.ROAD),
                    new Component(Construction.VILLAGE), false))) {
                img = ImageIO.read(new File("./src/main/java/edu/cmu/cs/cs214/hw4/gui/pictures/tileX.png"));
            } else {
                throw new RuntimeException("No such tile");
            }
        } catch (IOException e) {
            System.out.println("cannot find tile picture file");
        }
        return img;
    }

    /**
     * rotate the tile image
     */
    public void rotateImage() {
        BufferedImage image = pendingImage;
        double radians = Math.toRadians(ROTATE_DEGREE);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.round(image.getWidth() * cos + image.getHeight() * sin);
        int newHeight = (int) Math.round(image.getWidth() * sin + image.getHeight() * cos);

        BufferedImage rotate = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotate.createGraphics();

        int x = (newWidth - image.getWidth()) / 2;
        int y = (newHeight - image.getHeight()) / 2;

        AffineTransform at = new AffineTransform();
        at.setToRotation(radians, x + (image.getWidth() / 2.0), y + (image.getHeight() / 2.0));
        at.translate(x, y);
        g2d.setTransform(at);

        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        pendingImage = rotate;
    }

    /**
     * Extend the length of the squares
     */
    private void checkAndExtendSquares (int x, int y) {
        JButton[][] newSquare = null;
        if(x < 0) {
            newSquare = new JButton[squares.length + LENGTH_INCR][squares[0].length];
            for(int i = 0; i < squares.length; i++) {
                for(int j = 0; j < squares[i].length; j++) {
                    newSquare[i + LENGTH_INCR][j] = squares[i][j];
                }
            }
            centerX += LENGTH_INCR;
            borderX += LENGTH_INCR;
        } else if(x >= squares.length) {
            newSquare = new JButton[squares.length + LENGTH_INCR][squares[0].length];
            for(int i = 0; i < squares.length; i++) {
                for(int j = 0; j < squares[i].length; j++) {
                    newSquare[i][j] = squares[i][j];
                }
            }
            borderX += LENGTH_INCR;
        }
        if(y < 0) {
            newSquare = new JButton[squares.length][squares[0].length + LENGTH_INCR];
            for(int i = 0; i < squares.length; i++) {
                for(int j = 0; j < squares[i].length; j++) {
                    newSquare[i][j + LENGTH_INCR] = squares[i][j];
                }
            }
            centerY += LENGTH_INCR;
            borderY += LENGTH_INCR;
        } else if(y >= squares[0].length) {
            newSquare = new JButton[squares.length][squares[0].length + LENGTH_INCR];
            for(int i = 0; i < squares.length; i++) {
                for(int j = 0; j < squares[i].length; j++) {
                    newSquare[i][j] = squares[i][j];
                }
            }
            borderY += LENGTH_INCR;
        }
        if(newSquare != null) {
            squares = newSquare;
            BorderLayout layout = (BorderLayout) this.getLayout();
            this.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            this.add(createTileBoardPanel(), BorderLayout.CENTER);
        }
    }
}
