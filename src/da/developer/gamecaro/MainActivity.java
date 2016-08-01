package da.developer.gamecaro;

import da.developer.gamecaro.dialog.DialogExit;
import da.developer.gamecaro.sound.MusicBackground;
import da.developer.gamecaro.sound.Sound;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends MyApp implements OnClickListener {

	Button oneplayer, help, twoplayer, exit;
	public static Sound mSound;
	public static  MusicBackground musicBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // load music
        musicBackground = new MusicBackground();
        musicBackground.loadMusic(this);
 
        if (ValueControl.isMusic == true)
            musicBackground.play();
        else{
           musicBackground.pause();
           }
        
     
        setContentView(R.layout.activity_main);
//        Config.getDisplayScreen(this);
        
        exit = (Button) findViewById(R.id.bn_exit);
        exit.setOnClickListener(this);
        
        oneplayer = (Button) findViewById(R.id.bn_oneplayer);
        oneplayer.setOnClickListener(this);
        
        help = (Button) findViewById(R.id.bn_help);
        help.setOnClickListener(this);
        
        twoplayer = (Button) findViewById(R.id.bn_twoplayer);
        twoplayer.setOnClickListener(this);
        
        //sound
        mSound = new Sound();
        mSound.loadSound(this);
    }
    @Override
    public void onClick(View v) {
//        // Add sound click
        mSound.playClick();

        switch (v.getId()) {
        case R.id.bn_oneplayer:
            clickPlay();
            ValueControl.isPlayer = 1;
            CaroBoard.InitCaroBoard();
            break;
        case R.id.bn_help:
            clickHelp();
            break;
        case R.id.bn_twoplayer:
        	clickPlay();
        	ValueControl.isPlayer = 2;
        	CaroBoard.InitCaroBoard();
            break;
        case R.id.bn_exit:
            clickExit();
            break;
        }
    }
    // ------------------------------------------------------

	  public void clickPlay() {
	  try {
	      Intent intent = new Intent(this, CaroActivity.class);
	      this.startActivity(intent);
	  } catch (Exception e) {
	  }
	}
	  
	  public void clickHelp() {
		  try {
		      Intent intent = new Intent(this, help.class);
		      this.startActivity(intent);
		  } catch (Exception e) {
		  }
	}
	  
    // ------------------------------------------------------
    public void clickExit() {
        try {
            DialogExit dialogExit = new DialogExit(this);
            dialogExit.show();
        } catch (Exception e) {
        }
    }
}
    
