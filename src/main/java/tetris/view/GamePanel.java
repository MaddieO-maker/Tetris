package tetris.view;

import tetris.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Main game panel that renders the game board, pieces, and UI.
 */
public class GamePanel extends JPanel implements MouseListener {
    private GameModel gameModel;
    private static final int CELL_SIZE = 30;
    private static final int GRID_LINE_WIDTH = 1;
    private static final Color GRID_COLOR = new Color(50, 50, 50);
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    
    // Next piece preview panel dimensions
    private static final int PREVIEW_SIZE = 100;
    private static final int PREVIEW_X = 350;
    private static final int PREVIEW_Y = 20;
    
    // Replay button bounds
    private Rectangle replayButtonBounds;
    
    public GamePanel(GameModel gameModel) {
        this.gameModel = gameModel;
        setPreferredSize(new Dimension(550, 650));
        setBackground(BACKGROUND_COLOR);
        
        // Listen to game model changes
        gameModel.addListener(() -> repaint());
        
        // Add mouse listener for replay button
        addMouseListener(this);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw game board and pieces
        drawBoard(g2d);
        
        // Draw current piece (with glow)
        drawCurrentPiece(g2d);
        
        // Draw score
        drawScore(g2d);
        
        // Draw next piece preview
        drawNextPiecePreview(g2d);
        
        // Draw end game overlay if needed
        if (gameModel.getGameState() != GameModel.GameState.PLAYING) {
            drawEndGameScreen(g2d);
        }
    }
    
    private void drawBoard(Graphics2D g2d) {
        BlockType[][] grid = gameModel.getBoard().getGrid();
        int boardX = 50;
        int boardY = 100;
        
        for (int y = 0; y < Board.HEIGHT; y++) {
            for (int x = 0; x < Board.WIDTH; x++) {
                BlockType type = grid[y][x];
                int px = boardX + x * CELL_SIZE;
                int py = boardY + y * CELL_SIZE;
                
                // Draw cell background
                g2d.setColor(BACKGROUND_COLOR);
                g2d.fillRect(px, py, CELL_SIZE, CELL_SIZE);
                
                // Draw grid lines
                g2d.setColor(GRID_COLOR);
                g2d.setStroke(new BasicStroke(GRID_LINE_WIDTH));
                g2d.drawRect(px, py, CELL_SIZE, CELL_SIZE);
                
                // Draw placed block
                if (type != BlockType.EMPTY) {
                    g2d.setColor(type.getColor());
                    g2d.fillRect(px + 2, py + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                }
            }
        }
    }
    
    private void drawCurrentPiece(Graphics2D g2d) {
        Tetromino piece = gameModel.getCurrentPiece();
        if (piece == null) return;
        
        Block[] blocks = piece.getBlocks();
        int boardX = 50;
        int boardY = 100;
        
        for (Block block : blocks) {
            int px = boardX + block.getX() * CELL_SIZE;
            int py = boardY + block.getY() * CELL_SIZE;
            
            // Draw glow effect
            g2d.setColor(new Color(255, 255, 255, 30));
            g2d.fillOval(px - 5, py - 5, CELL_SIZE + 10, CELL_SIZE + 10);
            
            // Draw piece block
            g2d.setColor(block.getType().getGlowColor());
            g2d.fillRect(px + 2, py + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            
            // Draw border
            g2d.setColor(block.getType().getColor());
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(px + 2, py + 2, CELL_SIZE - 4, CELL_SIZE - 4);
        }
    }
    
    private void drawScore(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        
        int remaining = gameModel.getPointsRemaining();
        String scoreText = "Points to Win: " + remaining;
        g2d.drawString(scoreText, 50, 80);
    }
    
    private void drawNextPiecePreview(Graphics2D g2d) {
        Tetromino nextPiece = gameModel.getNextPiece();
        if (nextPiece == null) return;
        
        // Draw preview box
        g2d.setColor(GRID_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(PREVIEW_X, PREVIEW_Y, PREVIEW_SIZE, PREVIEW_SIZE);
        
        // Draw label
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Next", PREVIEW_X + 30, PREVIEW_Y - 5);
        
        // Draw preview piece
        Block[] blocks = nextPiece.getBlocks();
        
        // Find bounds of piece to center it
        int minX = 10, maxX = -10, minY = 10, maxY = -10;
        for (Block block : blocks) {
            minX = Math.min(minX, block.getX() - nextPiece.getX());
            maxX = Math.max(maxX, block.getX() - nextPiece.getX());
            minY = Math.min(minY, block.getY() - nextPiece.getY());
            maxY = Math.max(maxY, block.getY() - nextPiece.getY());
        }
        
        int previewCellSize = 15;
        int offsetX = PREVIEW_X + PREVIEW_SIZE / 2 - (maxX + minX) * previewCellSize / 2;
        int offsetY = PREVIEW_Y + PREVIEW_SIZE / 2 - (maxY + minY) * previewCellSize / 2;
        
        for (Block block : blocks) {
            int relX = block.getX() - nextPiece.getX();
            int relY = block.getY() - nextPiece.getY();
            int px = offsetX + relX * previewCellSize;
            int py = offsetY + relY * previewCellSize;
            
            g2d.setColor(block.getType().getColor());
            g2d.fillRect(px, py, previewCellSize, previewCellSize);
            
            g2d.setColor(block.getType().getColor().darker());
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(px, py, previewCellSize, previewCellSize);
        }
    }
    
    private void drawEndGameScreen(Graphics2D g2d) {
        // Semi-transparent overlay
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        
        String message;
        if (gameModel.getGameState() == GameModel.GameState.WON) {
            message = "You Win!";
            drawConfetti(g2d);
        } else {
            message = "Try Again?";
        }
        
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2 - 50;
        g2d.drawString(message, x, y);
        
        // Draw Replay button
        int buttonWidth = 150;
        int buttonHeight = 50;
        int buttonX = (getWidth() - buttonWidth) / 2;
        int buttonY = getHeight() / 2 + 50;
        
        replayButtonBounds = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
        
        g2d.setColor(new Color(50, 150, 50));
        g2d.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
        
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        fm = g2d.getFontMetrics();
        String buttonText = "Replay";
        int textX = buttonX + (buttonWidth - fm.stringWidth(buttonText)) / 2;
        int textY = buttonY + ((buttonHeight - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(buttonText, textX, textY);
    }
    
    private void drawConfetti(Graphics2D g2d) {
        // Draw simple falling confetti squares
        long currentTime = System.currentTimeMillis();
        
        for (int i = 0; i < 50; i++) {
            // Use pseudorandom based on time and index to make it deterministic but animated
            int seed = (int) (currentTime / 50 + i);
            java.util.Random rand = new java.util.Random(seed);
            
            int x = rand.nextInt(getWidth());
            int y = (int) ((currentTime / 10 + rand.nextInt(1000)) % getHeight());
            int size = 5 + rand.nextInt(10);
            
            Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 200);
            g2d.setColor(color);
            g2d.fillRect(x, y, size, size);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameModel.getGameState() != GameModel.GameState.PLAYING) {
            if (replayButtonBounds != null && replayButtonBounds.contains(e.getPoint())) {
                gameModel.restart();
                repaint();
            }
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
}
