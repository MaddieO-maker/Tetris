package tetris.controller;

import tetris.model.GameModel;
import tetris.view.GamePanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Controller handling user input and game logic.
 */
public class GameController implements KeyListener {
    private GameModel gameModel;
    private GamePanel gamePanel;
    private boolean[] keysPressed = new boolean[256];
    
    public GameController(GameModel gameModel, GamePanel gamePanel) {
        this.gameModel = gameModel;
        this.gamePanel = gamePanel;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keysPressed[keyCode] = true;
        
        // Handle input based on key pressed
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                gameModel.movePieceLeft();
                break;
            case KeyEvent.VK_RIGHT:
                gameModel.movePieceRight();
                break;
            case KeyEvent.VK_DOWN:
                gameModel.movePieceDown();
                break;
            case KeyEvent.VK_SPACE:
                gameModel.rotatePiece();
                break;
            case KeyEvent.VK_R:
                // Restart game with 'R' key
                if (gameModel.getGameState() != GameModel.GameState.PLAYING) {
                    gameModel.restart();
                    gamePanel.repaint();
                }
                break;
        }
        
        gamePanel.repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keysPressed[keyCode] = false;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
