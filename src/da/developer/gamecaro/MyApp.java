package da.developer.gamecaro;
import da.developer.gamecaro.util.UtilActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
public class MyApp extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilActivity.requestWindowFeature(this);
        this.getWindow().getAttributes().windowAnimations = R.style.Animations_Activity;
    }

    public void onClick(View arg0) {
    }


}
