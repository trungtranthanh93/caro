package da.developer.gamecaro.util;

import da.developer.gamecaro.R;
import android.app.Dialog;
import android.view.Window;

public class UtilDialog {

    /**
    * Không hiện thị titel của dialog và đặt background là 1 bức ảnh null, đồng thời đặt hiệu ứng
    * khi xuất hiện và tắt dialog
    * @param dialog
    */
    public static void iniDialog(Dialog dialog){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_null);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animations_Dialog;
    }
}
