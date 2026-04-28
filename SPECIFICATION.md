# Tetris Game Specification

## MODEL (Game Logic & Data)
**Game Grid & State**
- 10x16 play grid storing placed block data
- Current active piece and next piece preview
- Game states: PLAYING, WON, LOST
- Score tracker (0–1000, counting down from 1000)

**Piece Management**
- 7 tetromino types (I, O, T, S, Z, J, L), each with unique color
- Each piece has position (x, y), rotation state, and collision bounds
- Piece spawns at top center; if cannot fit → game over (LOST)
- Gravity: pieces fall automatically at constant speed

**Line Clearing & Scoring**
- After each piece lands, check for complete rows
- Clear complete rows; shift rows above downward
- Scoring system: 1 line = 100 pts | 2 lines = 300 pts | 3 lines = 500 pts | 4 lines = 800 pts
- Add points to running score
- Win condition: score ≥ 1000 points

---

## VIEW (User Interface)
**Layout**
- Main 10x16 game grid in center of window, black background with dark gray grid lines
- Score display above grid (showing countdown: "Points to Win: 1000 - [current_score]")
- Next piece preview in top right corner

**Visual Styling**
- Each tetromino type rendered in consistent unique color
- Falling/active pieces display with glow effect until placed
- Placed pieces display solid (no glow)
- Game over screens overlay center of screen

**End Game Screens**
- **Win Screen**: Display "You Win!" message with colorful confetti falling from top; include Replay button
- **Loss Screen**: Display "Try Again?" message; include Replay button

---

## CONTROLLER (User Input & Game Flow)
**Player Input**
- **Left/Right Arrow Keys**: Move active piece left or right (validate within bounds and no collision)
- **Space Bar**: Rotate active piece 90° clockwise (validate against placed blocks)
- **Down Arrow**: Soft drop—accelerate piece fall until landing

**Game Loop**
1. Render current game state (grid, active piece, next piece, score)
2. Process user input and update piece position/rotation
3. Apply gravity: move active piece down each tick
4. Check collision at new position:
   - If collision detected below, piece lands (becomes part of grid)
   - Spawn next piece; if cannot fit → trigger loss
5. Check for complete rows; clear them and update score
6. Check win condition (score ≥ 1000) → trigger win
7. Return to step 1

**Restart Functionality**
- Replay button resets all game state and returns to PLAYING mode
