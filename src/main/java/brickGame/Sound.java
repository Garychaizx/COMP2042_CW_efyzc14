package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
    private MediaPlayer hitSound;
    private MediaPlayer nextLevelSound;
    private MediaPlayer minusHeartSound;
    private MediaPlayer goldBallSound;
    private MediaPlayer extraHeartSound;
    private MediaPlayer bonusSound;
    private MediaPlayer snowBallSound;
    private MediaPlayer gameOverSound;
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

    public void playHitSound() {
        hitSound.stop();
        hitSound.play();
    }
    public void playNextLevelSound() {
        nextLevelSound.stop();
        nextLevelSound.play();
    }
    public void playMinusHeartSound(){
        minusHeartSound.stop();
        minusHeartSound.play();
    }
    public void playGoldBallSound(){
        hitSound.stop();
        goldBallSound.stop();
        goldBallSound.play();
    }
    public void playExtraHeartSound(){
        hitSound.stop();
        extraHeartSound.stop();
        extraHeartSound.play();
    }
    public void playBonusSound(){
        bonusSound.stop();
        bonusSound.play();
    }
    public void playSnowBallSound(){
        snowBallSound.stop();
        snowBallSound.play();
    }
    public void playGameOverSound(){
        gameOverSound.stop();
        gameOverSound.play();
    }

    // Additional methods for other sounds if needed

    public void setVolume(double volume) {
        hitSound.setVolume(volume);
    }

    public void setBalance(double balance) {
        hitSound.setBalance(balance);
    }
}
