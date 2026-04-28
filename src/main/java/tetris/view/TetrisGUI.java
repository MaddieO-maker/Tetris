package tetris.view;

import tetris.model.GameModel;
import tetris.controller.GameController;

import javax.swing.*;
import java.awt.*;

/**
 * Main GUI container for the Tetris game.
 */
public class TetrisGUI extends JPanel {
    private GameModel gameModel;
    private GameController gameController;
    private GamePanel gamePanel;
    
    public TetrisGUI() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        
        // Create model
        gameModel = new GameModel();
        
        // Create game panel (view)
        gamePanel = new GamePanel(gameModel);
        add(gamePanel, BorderLayout.CENTER);
        
        // Create controller
        gameController = new GameController(gameModel, gamePanel);
        
        // Set up keyboard input
        setFocusable(true);
        addKeyListener(gameController);
        
        // Request focus
        SwingUtilities.invokeLater(() -> requestFocus());
        
        // Start game loop
        startGameLoop();
    }
    
    /**
     * Start the game loop timer.
     */
    private void startGameLoop() {
        // Game piece gravity timer
        Timer gravityTimer = new Timer(100, e -> {
            if (gameModel.getGameState() == GameModel.GameState.PLAYING) {
                gameModel.movePieceDown();
            }
            gamePanel.repaint();
        });
        gravityTimer.start();
        
        // Confetti animation timer
        Timer repaintTimer = new Timer(30, e -> {
            if (gameModel.getGameState() != GameModel.GameState.PLAYING) {
                gamePanel.repaint();
            }
        });
        repaintTimer.start();
    }
}
