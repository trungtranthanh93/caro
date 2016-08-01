package da.developer.gamecaro.dialog;

import da.developer.gamecaro.R;
import da.developer.gamecaro.util.UtilDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
* Hiện thị thông báo muốn thoát hay không. Yes thì thoát khỏi game, No thì tiếp tục
*
*/
public class DialogExit extends Dialog implements
    android.view.View.OnClickListener {
 
    Button yes, no;
    Activity activity;
 
    public DialogExit(Context context) {
        super(context);
        UtilDialog.iniDialog(this);
        activity = (Activity) context;
        setContentView(R.layout.dialog_exit);
 
        // resize dialog
//        RelativeLayout linearLayout1 = (RelativeLayout) findViewById(R.id.linearLayout1);
//        Util.resizeDialog(linearLayout1);
 
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
            activity.finish();
            break;
        case R.id.no:
            this.dismiss();
            break;
        default:
            break;
        }
 
    }
 
}
