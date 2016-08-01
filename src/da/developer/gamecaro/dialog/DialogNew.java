package da.developer.gamecaro.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import da.developer.gamecaro.CaroActivity;
import da.developer.gamecaro.R;
import da.developer.gamecaro.ValueControl;
import da.developer.gamecaro.util.Util;
import da.developer.gamecaro.util.UtilDialog;

/**
* Hiện thị thông báo muốn chơi mới game không. Yes thì tạo mới bàn cờ, No giữ nguyên trạng thái
*
*/
public class DialogNew extends Dialog implements
    android.view.View.OnClickListener {
 
    Button yes, no;
    Activity activity;
 
    public DialogNew(Context context) {
        super(context);
        UtilDialog.iniDialog(this);
        activity = (Activity) context;
        setContentView(R.layout.dialog_new);
 
        // resize dialog
        RelativeLayout linearLayout1 = (RelativeLayout) findViewById(R.id.linearLayout1);
        Util.resizeDialog(linearLayout1);
 
        yes = (Button) findViewById(R.id.yes);
        yes.setOnClickListener(this);
 
        no = (Button) findViewById(R.id.no);
        no.setOnClickListener(this);
     
    }
 
    @Override
    public void onClick(View v) {
//        Menu.mSound.playClick();
 
        switch (v.getId()) {
        case R.id.yes:
            this.dismiss();
			CaroActivity.board.reset();
			ValueControl.isExit = false;
			CaroActivity.clearSprite();
			CaroActivity.listUndo.clear();
            break;
        case R.id.no:
            this.dismiss();
            break;
        default:
            break;
        }
 
    }
}
