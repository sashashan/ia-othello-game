package ai.othello.graphic;

import android.app.AlertDialog;
import android.content.Context;

public class AlgorithmChoiceDiagol extends AlertDialog {

	final CharSequence[] items = {"Min-Max Algorithm", "Alpha-Beta Algorithm"};
	
	protected AlgorithmChoiceDiagol(Context context) {
		super(context);
		
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("CHOICE ALGORITHM");
//		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int item) {
//				Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
//			}
//		});
//		final AlertDialog alert = builder.create();
//		alert.show();
	}

	
}

