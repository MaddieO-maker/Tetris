package tetris.model;

import java.awt.*;

/**
 * Enum representing the 7 tetromino types with their colors.
 */
public enum BlockType {
    I(new Color(0, 255, 255)),      // Cyan
    O(new Color(255, 255, 0)),      // Yellow
    T(new Color(128, 0, 128)),      // Purple
    S(new Color(0, 255, 0)),        // Green
    Z(new Color(255, 0, 0)),        // Red
    J(new Color(0, 0, 255)),        // Blue
    L(new Color(255, 165, 0)),      // Orange
    EMPTY(new Color(0, 0, 0));      // Black (empty cell)
    
    private final Color color;
    
    BlockType(Color color) {
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }
    
    public Color getGlowColor() {
        // Brighter version for glowing effect
        return new Color(
            Math.min(color.getRed() + 100, 255),
            Math.min(color.getGreen() + 100, 255),
            Math.min(color.getBlue() + 100, 255)
        );
    }
}
