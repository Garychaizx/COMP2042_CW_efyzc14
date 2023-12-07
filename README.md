# COMP2042_CW_efyzc14
JavaFx Lib:
[javafxLib.zip](https://github.com/Garychaizx/COMP2042_CW_efyzc14/files/13543307/javafxLib.zip)

### Compilation Instructions
To compule the code and produce the application, follow the steps below:

1. Clone the repository from [here](https://github.com/Garychaizx/COMP2042_CW_efyzc14.git)
2. Download the JavaFx library from the link above,open the main file and add the library to the project by selecting
   
   file>Project structure>Libraries
   
   and add the downloaded JavaFx library to the project.
4. Click the Run button in main class
5. Press start new game button to run the game in the program

### Implemented and Working Properly
1. Feature 1:[Snow Blocks]
   - Snow Blocks act as a penalty. When the ball hits a Snow Block, it drops a penalty icon. If the paddle collides with the penalty icon, the user incurs a penalty, causing the ball to slow down for 10 seconds.
2. Feature 2:[Game Sound]
   - Game sound effects have been implemented.Each block collision produces a distinct sound. Heart Blocks and Gold Blocks trigger specific sounds. Additionally, a unique sound notifies players when the ball hits the bottom, indicating a decrement in the heart count.
3. Feature 3:[Pause and resume]
   - Users can seamlessly control the game flow by utilizing the space bar on the keyboard to pause or resume the gameplay.
4. Feature 4:[Quit window]
   - Pressing the 'Esc' key strategically pauses the game and presents a user-friendly window. Users can conveniently choose to either quit the game ('Yes') or continue playing ('No').
5. Bonus Level
   - Level 19 will be the bonus level for the user, the level should only consists of BLOCK_CHOCO,BLOCK_STAR,BLOCK_HEART AND BLOCK_SNOW.
   - It is the last level of the game and letting the user to get as many points as the user can get.

### Implemented but Not Working Properly
1. Issue with Block-Ball Collision:
   - Description:
     - There is an observed issue with the collision mechanism between the ball and bloccks. In certain cases, when the ball collides with the very corner part of a block, it does not collide with the block and the block remains unaffected.
                  In certain cases, when the ball collide within the center of two blocks,the ball does not rebound in the corret way, it will go in straight direction.
   - Steps Taken:
     - Reviewed and adjusted collision algorithms.
     - Experimented with varying collision detection methods.
     - Investigated possible issues with the block or ball speed.
    - Curent Stautus:
      - The issue persists, added edges part for the blocks in 'checkHitToBlock()' method, to ensure that the ball not only colide with top,right,left and bottom of the blocks, it also check the edges of the block.
2. Issue with changing background when the ball is in gold status
   - Description:
     - The background style should apply the goldroot style in 'style.css'. The gold colour background cannot be applied after the background picture is added.
   - Steps Taken:
      - Remove the background picture and add the gold colour background during gold status.
   - Current Status:
      - The gold coulour background is able to show but without the background picture.

### Features not implemented
1. Feature 1[High Score Tracking]
   - Explanation: The implementation of a high score tracking system is not yet implemented as the 
