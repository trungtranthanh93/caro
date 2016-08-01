package da.developer.gamecaro.dialog;

import da.developer.gamecaro.CaroActivity;
import da.developer.gamecaro.MainActivity;
import da.developer.gamecaro.MySharedPreferences;
import da.developer.gamecaro.R;
import da.developer.gamecaro.ValueControl;
import da.developer.gamecaro.util.Util;
import da.developer.gamecaro.util.UtilDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class DialogSound extends Dialog implements
								android.view.View.OnClickListener {
	
		Button yes;
		Activity activity;
		CheckBox checkBox_music, checkBox_sound;
		
		MySharedPreferences mySharedPreferences;

	public DialogSound(Context context) {
		super(context);
		UtilDialog.iniDialog(this);
		activity = (Activity) context;
		setContentView(R.layout.dialog_sound);
	
		mySharedPreferences = new MySharedPreferences(context);
		mySharedPreferences.getIsMusic();
		mySharedPreferences.getIsSound();
		
		//resize dialog
		RelativeLayout linearLayout1 = (RelativeLayout)findViewById(R.id.linearLayout1);
		Util.resizeDialog(linearLayout1);
		
		yes = (Button) findViewById(R.id.yes);
		yes.setOnClickListener(this);
		
		checkBox_music = (CheckBox) findViewById(R.id.checkBox_music);
		checkBox_music.setOnClickListener(this);
		checkBox_music.setChecked(ValueControl.isMusic);
		
		checkBox_sound = (CheckBox) findViewById(R.id.checkBox_sound);
		checkBox_sound.setOnClickListener(this);
		checkBox_sound.setChecked(ValueControl.isSound);
	}

	@Override
	public void onClick(View v) {
	MainActivity.mSound.playClick();
//	MainActivity.musicBackground.play();
	
	switch (v.getId()) {
		case R.id.yes:
		    this.dismiss();
		    break;
		case R.id.checkBox_music:
			ValueControl.isMusic = checkBox_music.isChecked();
		    if(ValueControl.isMusic){
		        MainActivity.musicBackground.resume();
		    }else{
		    	MainActivity.musicBackground.pause();
		    }
		    mySharedPreferences.updateIsMusic(ValueControl.isMusic);
		    break;
		case R.id.checkBox_sound:
			ValueControl.isSound = checkBox_sound.isChecked();
		    mySharedPreferences.updateIsSound(ValueControl.isSound);
		    break;
		
		default:
		    break;
	}
	
	}

}