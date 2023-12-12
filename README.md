# COMP2042_CW_efyzc14
JavaFx Lib:
[javafxLib.zip](https://github.com/Garychaizx/COMP2042_CW_efyzc14/files/13543307/javafxLib.zip)

### Compilation Instructions
To compile the code and produce the application, follow the steps below:
1. Download the source code zip folder named "ChaiZeXuan_IntelliJ_19" from zip folder "COMP2024ChaiZeXuan".
2. Unzip and extract the folder to your prefer folder.
3. Open Intellij IDEA Community
4. Make sure your IntelliJ IDE has the correct SDK cersion(Oracle OpenJDK version 21.0.1).If the version is not correct follow the step below

       file -> Project Structure -> SDKs -> JDK home path -> browse-> Download JDK -> Download Oracle OpenJDK version 21.0.1
   
6. Download the JavaFx library from the link above,open the main file and add the library to the project by selecting
   
       file -> Project structure -> Libraries
   
   and add the downloaded JavaFx library to the project.
7. Click the Run button in main class
8. Press start new game button to run the game in the program

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
3. Issue with the delay of pedal movement
   - Description:
      - The pedal will have a slight delay while user start to control it.
   - Steps Taken:
      - refactored the code related to pedal and try to modify the code related to pedal
   - Current Status:
      - The issues persists but it does not affect much on the game.

### Features not implemented
1. Feature 1 [High Score Tracking]
   - Description : Show the highest score of the user everytime the game ended.
   - Explaination: The implementation of a high score tracking system is not yet implemented as the highest score need to be saved in every game but when the user start a new game the highest score will also be deleted.
2. Feature 2 [Multiple Ball Ability]
   - Description : Multiple Ball Ability should be activated when the ball hit certain special blocks.
   - Explaination: The implementaion of multiple ball ability is not implemented as the difficulty of handling each ball. There is a problem when handling the collision
                    detection between multiple balls and other game elements. Ensuring that each ball reacts appropriately to collisions without causing unexpected behavior can be intricate.
3. Feature 3 [Customisable control]
   - Description : Let the user to bind their keys to control the game.
   - Explaination: The implementation of the customisable control setting is not implemented because of the challenges of input handling and conflict resolution. User might accidentally bound the same key to                         different actions, this will cause the game crash.
  
### New Java Classes
1. Ball Class
   - Ball class consists the codes related to ball, example: xball, yball, ball radius, speed of the ball and the directions of the ball.
2. Break Class
   - Break class onsists of the codes related to paddle, example : xbreak, ybreak, width of paddle, height of paddle and the center of paddle.
3. Model Class
    - Model class is the bridge between the control and the view. A model calss typically represents a game object or an entity that encapsulates both the data and the logic associated with that object.
4. View Class
   - View class coordinate the appearance of the GUI. It decide where all the controls and displays go. It consists of the codes that setting up all the game object and game state.
5. Controller Class
   - Controller class collects together all the controls in the GUI. Whenever the user activates a control, it calls a method in the model class to change its state appropriately.
6. Sound Class
    - Sound class consists of the sounds for each specific situation and the methods to play all sounds.
  
### Modified Java Classes
1. Block Class
   - Changes: Modify the checkHitToBlock method to check the colllision of ball with edges of the blocks as the ball is not colliding with the ball at first.
2. Bonus Class
   - Changes: Added a penalty to the game.
3. GameEngine Class
   - Changes: Change thread to timeline as the score showing before changing to timeline is lagging everytime the ball hit the blocks.
4. Main Class
   - Changes: Refactored the class into model class, view class and controller class, this is to improve the understandability of the code and makes the code more maintainable.

### Unexpected Problems
1. The game was not able to run all the levels at first, it is solved by changing the thread in the code to timeline.
2. The game was not able to save at first, it is solved by changing the path from 'D:' to 'C:'.
