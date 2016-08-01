package da.developer.gamecaro;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class help extends MyApp implements OnClickListener {
	
	Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.activity_help);
 //       Config.getDisplayScreen(this);
        
        back = (Button) findViewById(R.id.button_back);
        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.button_back:
            clickBack();
            break;
        }
    }
   
	  public void clickBack() {
	  try {
			System.exit(0);
            finish();
	  } catch (Exception e) {
	  }
	}
}
