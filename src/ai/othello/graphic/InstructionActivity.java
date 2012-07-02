package ai.othello.graphic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InstructionActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instruction);
		
		Button bntTornBack = (Button)findViewById(R.id.bntTornBack); // bottone  per tornare al menu
		bntTornBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent tornBack = new Intent(InstructionActivity.this, OthelloActivity.class);
				startActivity(tornBack);
				finish();
			}
		}); 
		
	}

}
