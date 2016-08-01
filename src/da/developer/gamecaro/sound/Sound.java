package da.developer.gamecaro.sound;

import da.developer.gamecaro.R;
import da.developer.gamecaro.ValueControl;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class Sound {

    SoundPool mSoundPool;
    Context context;
    int playHuman1 = -1, playHuman2 = -1,  click = -1;
    int gameover = -1;
    float volume = 0.5f;
 
    /**
    * Load dữ liệu
    * @param context
    */
    public void loadSound(Context context) {
        this.context = context;
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 100);
        click = mSoundPool.load(this.context, R.drawable.click, 1);     
//        playHuman2 = mSoundPool.load(this.context, R.raw.playHuman2, 1);     

        gameover = mSoundPool.load(this.context, R.drawable.gameover, 1);
    }
 
    public void offSound() {
        volume = 0;
    }
    
    /**
    * Click
    */
    public void playClick() {
        if (ValueControl.isSound) {
            new Thread(new Runnable() {             
                @Override
                public void run() {
                    mSoundPool.play(click, volume, volume, 1, 0, 1f);
                }
            }).start();         
        }
    }
 
    /**
    * Không ăn được
    */
    public void playHuman1() {
        if (ValueControl.isSound) {
            new Thread(new Runnable() {             
                @Override
                public void run() {
                    mSoundPool.play(playHuman1, volume, volume, 1, 0, 1f);
                }
            }).start();         
        }
    }
 
    /**
    * Ăn được
    */
    public void playHuman2() {
        if (ValueControl.isSound) {
            new Thread(new Runnable() {             
                @Override
                public void run() {
                    mSoundPool.play(playHuman2, volume, volume, 1, 0, 1f);
                }
            }).start();         
        }
    }
 
    /**
    * Game over
    */
    public void playGameOver() {
        if (ValueControl.isSound) {
            new Thread(new Runnable() {             
                @Override
                public void run() {
                    mSoundPool.play(gameover, volume, volume, 1, 0, 1f);
                }
            }).start();         
        }
    }
 
}
