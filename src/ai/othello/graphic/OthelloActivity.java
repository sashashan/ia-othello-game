package ai.othello.graphic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
				
				AlertDialog.Builder b = new AlertDialog.Builder(OthelloActivity.this);
				b.setTitle("Choose Algorithm");
				b.setSingleChoiceItems(new String[]{
						getString(R.string.minmaxText),
						getString(R.string.alphabetaText)
					}, -1, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent play = new Intent(OthelloActivity.this, PlayActivity.class);
						play.putExtra("algorithm", which);
						startActivity(play);
						finish();
					}
				});
				b.setNegativeButton(R.string.cancel, null);
				b.show();
			}
		});        

		Button bntInst = (Button)findViewById(R.id.bntInstruction);
		bntInst.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent inst = new Intent(OthelloActivity.this, InstructionActivity.class);
				startActivity(inst);
			}
		}); 

		Button bntExit = (Button)findViewById(R.id.bntExit);
		bntExit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}
}