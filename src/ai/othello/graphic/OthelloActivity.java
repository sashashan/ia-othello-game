package ai.othello.graphic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OthelloActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button bntPlay = (Button)findViewById(R.id.bntPlay);
		bntPlay.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent play = new Intent(OthelloActivity.this, PlayActivity.class);
				startActivity(play);
			}
		});        
        
        Button bntExit = (Button)findViewById(R.id.bntExit);
		bntExit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				System.exit(0);	
			}
		});
    }
}