# Tetris
A Tetris Game

Prompt 1: I want to build Tetris in java with Swing using MVC. Before we write code, interview me. Ask me one question at a time about gameplay, controls, win and loss conditions, and what should be on screen, After about 8 questions, summarize what I told you as a one-page spec organized by Model, View, and Controller.

AI Question 1: 
What should the basic gameplay loop be? Specifically:
Should tetromino pieces fall automatically at a consistent speed, with players rotating/moving them before they land?
Or do you want players to manually drop pieces?
Should there be a "next piece" preview?

Answer 1: Pieces will fall automatically while players rotate and move them before they land. If the player is satisfied with where they want piece to land, they can press the "down" key to make it drop quickly. Show a "next piece" preview in the top right corner.

AI Question 2: 
What controls do you want for player input?
Left/Right arrow keys to move pieces horizontally?
Up arrow to rotate, or a different key?
Any other controls (hard drop, hold piece, pause)?

Answer 2: Left and Right arrows will move pieces horizontally across grid. Space bar will rotate pieces.

AI Question 3: 
What happens when pieces land?1
When a horizontal line is completely filled, should it be cleared (removed)?
Should clearing lines increase a score? If so, how should scoring work (points per line, bonus for clearing multiple lines at once)?
Should the game get harder over time (pieces fall faster)?

Answer 3: When a horizontal line is completely filled, clear that line. Clearing lines will increase score. Clearing one line at a time scores 100 points, two lines at once score 300 points, three lines at once score 500. and four lines at once score 800.

AI Question 4:
Does difficulty increase during gameplay?
Should the pieces fall faster as the player clears more lines or after a certain time?
Or should the fall speed stay constant throughout the game? 

Answer 4: Fall speed stays consistent throughout game.

AI Question 5: 
When does the game end (lose condition)?
Should the game end when a new piece spawns but cannot fit in the play area (pieces stacked to the top)?
Or a different loss condition?

Answer 5: The game will end (lose) when a piece spawns but cannot fit in play area.

AI Question 6: 
What should be displayed on screen?
Main play grid/game board in the center?
Score display and next piece preview (top right, as mentioned)?
Any other information like level, lines cleared count, game-over status, or restart button?

Answer 6: Play grid will be a 10x16 grid in the center. Above grid in top right corner will be the "Next Piece" preview. Directly above grid will be the score count. Players will win game when they score 1000 points. Have the score count down from 1000 so players know remaining score left to complete game.

AI Question 7: 
What should happen when the player wins or loses?
Should the game display a "Game Over" or "You Win!" message?
Should there be a button to restart the game?
Should the game just stop (no restart option)?

Answer 7: When a player completes all 100 points, display a screen that says "You Win!" with colorful confetti dropping from the top of the screen. When a player loses, display a "Try Again?" screen. Both win and loss screens should have a Replay button underneath their messages.

AI Question 8: 
What about tetromino colors and styling?
Should each of the 7 standard Tetris pieces (I, O, T, S, Z, J, L) have different colors?
Any other visual styling preferences (grid background color, piece borders, animation effects)?

Answer 8: Eash Tetris shape should be a different color and be consistent by shape (I, O, T, S, Z, J, L). Background should be black with dark gray grid lines. Falling piece should be glowing until it is placed.

Prompt 2: Have pieces fall at half the current speed.
SUCCESS

Prompt 3: Bottom row of grid is falling off the screen. Edit so the screen is a little bigger to compensate for the size of the grid.
SUCCESS

Prompt 4: Have pieces fall slower still.
SUCCESS

Prompt 5: Create a ModelTester.java file with the main method. It should create a GameModel, call its methods directly, and print Pass or Fail for each check. Write tests for these behaviors: Players cannot move pieces beyond the grids boundaries, Score consistently moves down when rows are cleared, when multiple rows are cleared at once players get more points, and the original shape of the piece doesn't change when rotating.
Test 2 Failed - Inifinite Loop Found

Prompt 6 - Fix the inifinite loop in test 2 and run rest of tests
RATE LIMIT REACHED
