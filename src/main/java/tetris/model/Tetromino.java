package tetris.model;

/**
 * Represents a tetromino piece with position and rotation state.
 */
public class Tetromino {
    private BlockType type;
    private int x; // Grid column (0-9)
    private int y; // Grid row (0-15)
    private int rotation; // 0, 1, 2, 3
    
    // Shape definitions: blocks relative to piece center
    // Each rotation has 4 blocks (x, y offsets from piece center)
    private static final int[][][][] SHAPES = {
        // I piece
        {
            {{-1, 0}, {0, 0}, {1, 0}, {2, 0}},      // 0°
            {{0, -1}, {0, 0}, {0, 1}, {0, 2}},      // 90°
            {{-1, 0}, {0, 0}, {1, 0}, {2, 0}},      // 180°
            {{0, -1}, {0, 0}, {0, 1}, {0, 2}}       // 270°
        },
        // O piece
        {
            {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
            {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
            {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
            {{0, 0}, {1, 0}, {0, 1}, {1, 1}}
        },
        // T piece
        {
            {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},      // 0°
            {{0, -1}, {-1, 0}, {0, 0}, {0, 1}},     // 90°
            {{-1, 0}, {0, 0}, {1, 0}, {0, -1}},     // 180°
            {{0, -1}, {1, 0}, {0, 0}, {0, 1}}       // 270°
        },
        // S piece
        {
            {{0, 0}, {1, 0}, {-1, 1}, {0, 1}},      // 0°
            {{0, -1}, {0, 0}, {1, 0}, {1, 1}},      // 90°
            {{0, 0}, {1, 0}, {-1, 1}, {0, 1}},      // 180°
            {{0, -1}, {0, 0}, {1, 0}, {1, 1}}       // 270°
        },
        // Z piece
        {
            {{-1, 0}, {0, 0}, {0, 1}, {1, 1}},      // 0°
            {{1, -1}, {0, 0}, {1, 0}, {0, 1}},      // 90°
            {{-1, 0}, {0, 0}, {0, 1}, {1, 1}},      // 180°
            {{1, -1}, {0, 0}, {1, 0}, {0, 1}}       // 270°
        },
        // J piece
        {
            {{-1, 0}, {0, 0}, {1, 0}, {1, 1}},      // 0°
            {{0, -1}, {0, 0}, {0, 1}, {-1, 1}},     // 90°
            {{-1, -1}, {-1, 0}, {0, 0}, {1, 0}},    // 180°
            {{1, -1}, {0, -1}, {0, 0}, {0, 1}}      // 270°
        },
        // L piece
        {
            {{1, 0}, {0, 0}, {-1, 0}, {-1, 1}},     // 0°
            {{0, -1}, {0, 0}, {0, 1}, {1, 1}},      // 90°
            {{1, -1}, {1, 0}, {0, 0}, {-1, 0}},     // 180°
            {{-1, -1}, {0, -1}, {0, 0}, {0, 1}}     // 270°
        }
    };
    
    public Tetromino(BlockType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotation = 0;
    }
    
    public BlockType getType() {
        return type;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getRotation() {
        return rotation;
    }
    
    public void setRotation(int rotation) {
        this.rotation = rotation % 4;
    }
    
    /**
     * Rotate the piece by 90 degrees.
     */
    public void rotatePiece() {
        this.rotation = (this.rotation + 1) % 4;
    }
    
    /**
     * Get the blocks that make up this piece at current position/rotation.
     */
    public Block[] getBlocks() {
        int shapeIndex = type.ordinal();
        int[][] offsets = SHAPES[shapeIndex][rotation];
        Block[] blocks = new Block[4];
        
        for (int i = 0; i < 4; i++) {
            int blockX = x + offsets[i][0];
            int blockY = y + offsets[i][1];
            blocks[i] = new Block(blockX, blockY, type);
        }
        return blocks;
    }
    
    /**
     * Get the blocks that would result from a rotation.
     */
    public Block[] getBlocksAfterRotation() {
        int shapeIndex = type.ordinal();
        int nextRotation = (rotation + 1) % 4;
        int[][] offsets = SHAPES[shapeIndex][nextRotation];
        Block[] blocks = new Block[4];
        
        for (int i = 0; i < 4; i++) {
            int blockX = x + offsets[i][0];
            int blockY = y + offsets[i][1];
            blocks[i] = new Block(blockX, blockY, type);
        }
        return blocks;
    }
}
