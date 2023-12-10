package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The Sound class manages the game's sound effects by providing methods to load, play, and control various sound files.
 */
public class Sound {
    private MediaPlayer hitSound;
    private MediaPlayer nextLevelSound;
    private MediaPlayer minusHeartSound;
    private MediaPlayer goldBallSound;
    private MediaPlayer extraHeartSound;
    private MediaPlayer bonusSound;
    private MediaPlayer snowBallSound;
    private MediaPlayer gameOverSound;

    /**
     * Constructs a new Sound object and loads all the sound files.
     */
    public Sound() {
        // Load sound files
        hitSound = loadSound("hitblock.mp3");
        nextLevelSound = loadSound("nextlevel.mp3");
        minusHeartSound= loadSound("minusheart.mp3");
        goldBallSound = loadSound("goldball.mp3");
        extraHeartSound = loadSound("extraheart.mp3");
        bonusSound = loadSound("bonussound.mp3");
        snowBallSound = loadSound("snowball.mp3");
        gameOverSound = loadSound("gameover.mp3");
    }

    private MediaPlayer loadSound(String fileName) {
        return new MediaPlayer(new Media(getClass().getResource("/" + fileName).toString()));
    }

    /**
     * Plays the hit block sound effect.
     */
    public void playHitSound() {
        hitSound.stop();
        hitSound.play();
    }

    /**
     * Plays the next level sound effect.
     */
    public void playNextLevelSound() {
        nextLevelSound.stop();
        nextLevelSound.play();
    }

    /**
     * Plays the minus heart sound effect.
     */
    public void playMinusHeartSound(){
        minusHeartSound.stop();
        minusHeartSound.play();
    }

    /**
     * Plays the gold ball sound effect.
     */
    public void playGoldBallSound(){
        hitSound.stop();
        goldBallSound.stop();
        goldBallSound.play();
    }

    /**
     * Plays the extra heart sound effect.
     */
    public void playExtraHeartSound(){
        hitSound.stop();
        extraHeartSound.stop();
        extraHeartSound.play();
    }

    /**
     * Plays the bonus sound effect.
     */
    public void playBonusSound(){
        bonusSound.stop();
        bonusSound.play();
    }

    /**
     * Plays the snow ball sound effect.
     */
    public void playSnowBallSound(){
        snowBallSound.stop();
        snowBallSound.play();
    }

    /**
     * Plays the game over sound effect.
     */
    public void playGameOverSound(){
        gameOverSound.stop();
        gameOverSound.play();
    }

}
