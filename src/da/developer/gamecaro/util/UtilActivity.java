package da.developer.gamecaro.util;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class UtilActivity {
    /**
    * Yêu cầu 1 activity không có titel và full màn hình
    * @param ac
    */
    public static void requestWindowFeature(Activity ac) {
        ac.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ac.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
