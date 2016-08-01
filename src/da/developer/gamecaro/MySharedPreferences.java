package da.developer.gamecaro;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    public final String PREFS_NAME = "MyPrefs";
 
    SharedPreferences my_share;
    SharedPreferences.Editor editor;
 
    public MySharedPreferences(Context mContext){
        my_share = mContext.getSharedPreferences(PREFS_NAME, 0);
        editor = my_share.edit();
    }
    //----------------------------Âm thanh--------------------------
    /**
    * Mặc định là mở âm thanh
    */
    public void getIsSound(){
    	ValueControl.isSound = my_share.getBoolean("isSound", true);
    }
    //----------------------------Nhạc nền--------------------------
    /**
    * Mặc định là nhạc nền mở
    */
    public void getIsMusic(){
    	ValueControl.isMusic = my_share.getBoolean("isMusic", true);
    }
    
    //------------------------------UPDATE---------------------------
    public void updateIsSound(boolean isSound){
    	ValueControl.isSound = isSound;
        editor.putBoolean("isSound", isSound);
        editor.commit();

    }
 
    public void updateIsMusic(boolean isMusic){
    	ValueControl.isMusic = isMusic;
        editor.putBoolean("isMusic", isMusic);
        editor.commit();
    }
}