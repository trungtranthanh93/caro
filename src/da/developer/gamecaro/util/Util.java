package da.developer.gamecaro.util;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class Util {

    /**
    * Lấy ngẫu nhiên 1 giá trị từ min đến max
    * @param min
    * @param max
    * @return
    */
    public static int getRandomIndex(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
    /**
    * Hiện thị 1 message
    * @param mContext
    * @param txt
    */
    public static void showToast(Context mContext, String txt) {
        Toast.makeText(mContext, txt, Toast.LENGTH_SHORT).show();
    }
    /**
    * Resize dialog
    * @param v
    */
    public static void resizeDialog(View v) {
    }
}
