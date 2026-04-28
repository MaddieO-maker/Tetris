package tetris.model;

/**
 * Represents the 10x16 game board.
 */
public class Board {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 16;
    
    private BlockType[][] grid;
    
    public Board() {
        grid = new BlockType[HEIGHT][WIDTH];
        clear();
    }
    
    /**
     * Clear the board (fill with EMPTY).
     */
    public void clear() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                grid[y][x] = BlockType.EMPTY;
            }
        }
    }
    
    /**
     * Check if a block at (x, y) is occupied.
     */
    public boolean isOccupied(int x, int y) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return true; // Out of bounds is considered occupied
        }
        return grid[y][x] != BlockType.EMPTY;
    }
    
    /**
     * Place a block at (x, y).
     */
    public void setBlock(int x, int y, BlockType type) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            grid[y][x] = type;
        }
    }
    
    /**
     * Get the block type at (x, y).
     */
    public BlockType getBlock(int x, int y) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return BlockType.EMPTY;
        }
        return grid[y][x];
    }
    
    /**
     * Check if a row is complete.
     */
    public boolean isRowComplete(int row) {
        for (int x = 0; x < WIDTH; x++) {
            if (grid[row][x] == BlockType.EMPTY) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Clear completed rows and return the number of rows cleared.
     */
    public int clearCompleteRows() {
        int rowsCleared = 0;
        
        for (int y = HEIGHT - 1; y >= 0; y--) {
            if (isRowComplete(y)) {
                removeRow(y);
                y++; // Check same row again since rows shifted down
                rowsCleared++;
            }
        }
        return rowsCleared;
    }
    
    /**
     * Remove a row and shift rows above down.
     */
    private void removeRow(int row) {
        for (int y = row; y > 0; y--) {
            for (int x = 0; x < WIDTH; x++) {
                grid[y][x] = grid[y - 1][x];
            }
        }
        // Clear top row
        for (int x = 0; x < WIDTH; x++) {
            grid[0][x] = BlockType.EMPTY;
        }
    }
    
    /**
     * Get the grid (for view).
     */
    public BlockType[][] getGrid() {
        return grid;
    }
}
