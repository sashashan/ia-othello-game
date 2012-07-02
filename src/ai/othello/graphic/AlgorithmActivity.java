package ai.othello.graphic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AlgorithmActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.algorithm_choice_dialog);
		
		Button bntCancel = (Button)findViewById(R.id.bntCancel); // bottone cancel dialog scelta algoritmo
		bntCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent calcel = new Intent(AlgorithmActivity.this, OthelloActivity.class);
				startActivity(calcel);
				finish();
			}
		});       
		
		Button bntPlayChoice = (Button)findViewById(R.id.bntPlayChoice); // bottone exit partita
		bntPlayChoice.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent playChoice = new Intent(AlgorithmActivity.this, PlayActivity.class);
				startActivity(playChoice);
				finish();
			}
		});   
	}

}
