package tetris.test;

import tetris.model.*;

/**
 * ModelTester - Direct testing of GameModel behavior
 */
public class ModelTester {
    private static int testsRun = 0;
    private static int testsPassed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Tetris Model Tester ===\n");
        
        testMovementBoundaries();
        testScoreIncrease();
        testMultipleRowBonus();
        testPieceRotation();
        
        System.out.println("\n=== Results ===");
        System.out.println("Tests Passed: " + testsPassed + "/" + testsRun);
    }
    
    /**
     * Test 1: Players cannot move pieces beyond grid boundaries
     */
    private static void testMovementBoundaries() {
        System.out.println("Test 1: Movement Boundaries");
        GameModel model = new GameModel();
        Tetromino piece = model.getCurrentPiece();
        
        int originalX = piece.getX();
        
        // Try to move left beyond boundary (should not go below 0)
        for (int i = 0; i < 20; i++) {
            model.movePieceLeft();
        }
        boolean leftBoundary = piece.getX() >= 0;
        check("  Left boundary blocked", leftBoundary);
        
        // Reset and try moving right beyond boundary (should not exceed 9)
        model.restart();
        piece = model.getCurrentPiece();
        
        for (int i = 0; i < 20; i++) {
            model.movePieceRight();
        }
        
        // Calculate rightmost position: with grid width 10 and max piece width, 
        // pieces should not exceed right boundary
        Block[] blocks = piece.getBlocks();
        int maxX = 0;
        for (Block block : blocks) {
            maxX = Math.max(maxX, block.getX());
        }
        boolean rightBoundary = maxX < Board.WIDTH;
        check("  Right boundary blocked", rightBoundary);
    }
    
    /**
     * Test 2: Score increases when rows are cleared
     */
    private static void testScoreIncrease() {
        System.out.println("\nTest 2: Score Increase on Line Clear");
        GameModel model = new GameModel();
        Board board = model.getBoard();
        
        // Manually fill a complete row at bottom
        int rowToFill = Board.HEIGHT - 1;
        for (int x = 0; x < Board.WIDTH; x++) {
            board.setBlock(x, rowToFill, BlockType.I);
        }
        
        int scoreBefore = model.getScore();
        
        // Clear the completed row manually
        int rowsCleared = board.clearCompleteRows();
        
        int scoreAfter = model.getScore();
        
        // When 1 row is cleared, score should increase by 100
        boolean scoreIncreased = scoreAfter > scoreBefore;
        check("  Score increased after line clear", scoreIncreased);
        
        if (rowsCleared == 1) {
            boolean correctPoints = (scoreAfter - scoreBefore) == 100;
            check("  Single line clear = 100 points", correctPoints);
        }
    }
    
    /**
     * Test 3: Multiple rows cleared at once give appropriate bonus
     */
    private static void testMultipleRowBonus() {
        System.out.println("\nTest 3: Multiple Row Bonus Scoring");
        GameModel model = new GameModel();
        Board board = model.getBoard();
        
        // Fill two complete rows near bottom
        int row1 = Board.HEIGHT - 1;
        int row2 = Board.HEIGHT - 2;
        
        for (int x = 0; x < Board.WIDTH; x++) {
            board.setBlock(x, row1, BlockType.I);
            board.setBlock(x, row2, BlockType.O);
        }
        
        int scoreBefore = model.getScore();
        
        // Clear the completed rows manually
        int rowsCleared = board.clearCompleteRows();
        
        int scoreAfter = model.getScore();
        
        // If 2 rows were cleared, score should reflect multi-row bonus
        if (rowsCleared >= 2) {
            // 2 rows should give 300 points
            boolean bonusApplied = (scoreAfter - scoreBefore) == 300;
            check("  2-row bonus (300 pts) applied", bonusApplied);
        } else if (rowsCleared == 1) {
            check("  Single row cleared (expected 2)", false);
        } else {
            check("  No rows cleared (should have cleared 2)", false);
        }
    }
    
    /**
     * Test 4: Piece shape doesn't change when rotating
     */
    private static void testPieceRotation() {
        System.out.println("\nTest 4: Piece Rotation Shape Preservation");
        
        // Test with O piece (should never visually change)
        Tetromino oPiece = new Tetromino(BlockType.O, 5, 0);
        Block[] blocksBeforeRotate = oPiece.getBlocks();
        
        oPiece.rotatePiece();
        Block[] blocksAfterRotate = oPiece.getBlocks();
        
        // O piece should have same relative positions
        boolean oPiecePreserved = compareBlockShapes(blocksBeforeRotate, blocksAfterRotate);
        check("  O-piece shape preserved after rotation", oPiecePreserved);
        
        // Test with T piece - should have same number of blocks
        Tetromino tPiece = new Tetromino(BlockType.T, 5, 2);
        blocksBeforeRotate = tPiece.getBlocks();
        
        tPiece.rotatePiece();
        blocksAfterRotate = tPiece.getBlocks();
        
        // Same number of blocks (always 4 for tetrominoes)
        boolean blockCountPreserved = blocksBeforeRotate.length == blocksAfterRotate.length && blocksBeforeRotate.length == 4;
        check("  Block count preserved (always 4)", blockCountPreserved);
        
        // Verify piece is still recognizable as tetromino shape (within 3x3 or 2x3 bounds)
        boolean tPieceShapeValid = validateTPieceShape(tPiece);
        check("  T-piece maintains valid tetromino structure", tPieceShapeValid);
    }
    
    /**
     * Compare if two sets of blocks have the same relative shape
     */
    private static boolean compareBlockShapes(Block[] before, Block[] after) {
        if (before.length != after.length) return false;
        
        // For O piece, relative positions should be identical
        // Calculate relative positions from center
        double centerXBefore = 0, centerYBefore = 0;
        for (Block b : before) {
            centerXBefore += b.getX();
            centerYBefore += b.getY();
        }
        centerXBefore /= before.length;
        centerYBefore /= before.length;
        
        double centerXAfter = 0, centerYAfter = 0;
        for (Block b : after) {
            centerXAfter += b.getX();
            centerYAfter += b.getY();
        }
        centerXAfter /= after.length;
        centerYAfter /= after.length;
        
        // For O piece, should be exactly the same
        for (int i = 0; i < before.length; i++) {
            double relXBefore = before[i].getX() - centerXBefore;
            double relYBefore = before[i].getY() - centerYBefore;
            double relXAfter = after[i].getX() - centerXAfter;
            double relYAfter = after[i].getY() - centerYAfter;
            
            if (Math.abs(relXBefore - relXAfter) > 0.1 || Math.abs(relYBefore - relYAfter) > 0.1) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Validate that a T-piece maintains its structure through rotations
     */
    private static boolean validateTPieceShape(Tetromino piece) {
        Block[] blocks = piece.getBlocks();
        if (blocks.length != 4) return false;
        
        // A T piece should have 4 blocks - check they're still connected reasonably
        // (not checking exact positions since rotation changes them)
        // Just verify it's not degenerate
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        
        for (Block b : blocks) {
            minX = Math.min(minX, b.getX());
            maxX = Math.max(maxX, b.getX());
            minY = Math.min(minY, b.getY());
            maxY = Math.max(maxY, b.getY());
        }
        
        // T piece should fit in roughly 3x3 or 3x2 area depending on rotation
        int width = maxX - minX + 1;
        int height = maxY - minY + 1;
        
        return (width <= 3 && height <= 3) && (width >= 2 && height >= 2);
    }
    
    /**
     * Helper method to print Pass/Fail
     */
    private static void check(String testName, boolean passed) {
        testsRun++;
        String result = passed ? "PASS" : "FAIL";
        System.out.println(testName + ": " + result);
        if (passed) {
            testsPassed++;
        }
    }
}
