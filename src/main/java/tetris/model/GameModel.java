package tetris.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main game model. Manages game state, score, pieces, and game flow.
 */
public class GameModel {
    public enum GameState {
        PLAYING, WON, LOST
    }
    
    private Board board;
    private Tetromino currentPiece;
    private Tetromino nextPiece;
    private int score;
    private int pointsToWin = 500;
    private GameState gameState;
    private Random random;
    
    private List<GameModelListener> listeners = new ArrayList<>();
    
    public GameModel() {
        this.board = new Board();
        this.random = new Random();
        this.score = 0;
        this.gameState = GameState.PLAYING;
        
        // Generate first two pieces
        this.nextPiece = generateRandomPiece();
        spawnNextPiece();
    }
    
    /**
     * Spawn the next piece. If it can't fit, game is lost.
     */
    public void spawnNextPiece() {
        currentPiece = nextPiece;
        nextPiece = generateRandomPiece();
        
        // Check if current piece can fit at spawn position
        if (!canPlacePiece(currentPiece)) {
            gameState = GameState.LOST;
            notifyListeners();
        }
        notifyListeners();
    }
    
    /**
     * Generate a random tetromino piece at center top of board.
     */
    private Tetromino generateRandomPiece() {
        BlockType[] types = {
            BlockType.I, BlockType.O, BlockType.T, 
            BlockType.S, BlockType.Z, BlockType.J, BlockType.L
        };
        BlockType randomType = types[random.nextInt(types.length)];
        return new Tetromino(randomType, Board.WIDTH / 2 - 1, 0);
    }
    
    /**
     * Try to move current piece left.
     */
    public void movePieceLeft() {
        if (gameState != GameState.PLAYING || currentPiece == null) return;
        
        currentPiece.setX(currentPiece.getX() - 1);
        if (!canPlacePiece(currentPiece)) {
            currentPiece.setX(currentPiece.getX() + 1);
        }
        notifyListeners();
    }
    
    /**
     * Try to move current piece right.
     */
    public void movePieceRight() {
        if (gameState != GameState.PLAYING || currentPiece == null) return;
        
        currentPiece.setX(currentPiece.getX() + 1);
        if (!canPlacePiece(currentPiece)) {
            currentPiece.setX(currentPiece.getX() - 1);
        }
        notifyListeners();
    }
    
    /**
     * Try to move current piece down.
     */
    public void movePieceDown() {
        if (gameState != GameState.PLAYING || currentPiece == null) return;
        
        currentPiece.setY(currentPiece.getY() + 1);
        if (!canPlacePiece(currentPiece)) {
            currentPiece.setY(currentPiece.getY() - 1);
            landPiece();
        }
        notifyListeners();
    }
    
    /**
     * Try to rotate current piece.
     */
    public void rotatePiece() {
        if (gameState != GameState.PLAYING || currentPiece == null) return;
        
        int originalRotation = currentPiece.getRotation();
        currentPiece.setRotation(originalRotation + 1);
        
        if (!canPlacePiece(currentPiece)) {
            currentPiece.setRotation(originalRotation);
        }
        notifyListeners();
    }
    
    /**
     * Check if a piece can be placed at its current position.
     */
    private boolean canPlacePiece(Tetromino piece) {
        Block[] blocks = piece.getBlocks();
        for (Block block : blocks) {
            if (board.isOccupied(block.getX(), block.getY())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Land the current piece (place it on board and spawn next).
     */
    private void landPiece() {
        Block[] blocks = currentPiece.getBlocks();
        for (Block block : blocks) {
            board.setBlock(block.getX(), block.getY(), block.getType());
        }
        
        // Clear complete rows
        int rowsCleared = board.clearCompleteRows();
        if (rowsCleared > 0) {
            addScore(rowsCleared);
        }
        
        // Check win condition
        if (score >= pointsToWin) {
            gameState = GameState.WON;
            notifyListeners();
            return;
        }
        
        // Spawn next piece
        spawnNextPiece();
    }
    
    /**
     * Add score based on rows cleared.
     * 1 line = 100, 2 lines = 300, 3 lines = 500, 4 lines = 800
     */
    private void addScore(int rows) {
        int points = 0;
        switch (rows) {
            case 1: points = 100; break;
            case 2: points = 300; break;
            case 3: points = 500; break;
            case 4: points = 800; break;
        }
        score += points;
        notifyListeners();
    }
    
    /**
     * Restart the game.
     */
    public void restart() {
        board.clear();
        score = 0;
        gameState = GameState.PLAYING;
        nextPiece = generateRandomPiece();
        spawnNextPiece();
    }
    
    // Getters
    public Board getBoard() {
        return board;
    }
    
    public Tetromino getCurrentPiece() {
        return currentPiece;
    }
    
    public Tetromino getNextPiece() {
        return nextPiece;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getPointsRemaining() {
        return Math.max(0, pointsToWin - score);
    }
    
    public GameState getGameState() {
        return gameState;
    }
    
    // Observer pattern
    public interface GameModelListener {
        void onGameStateChanged();
    }
    
    public void addListener(GameModelListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners() {
        for (GameModelListener listener : listeners) {
            listener.onGameStateChanged();
        }
    }
}
