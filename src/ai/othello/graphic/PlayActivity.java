package ai.othello.graphic;
import ai.othello.entities.Game;
import ai.othello.entities.GameI;
import ai.othello.entities.GameI.COLOR;
import android.R.string;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PlayActivity extends Activity {

	private ImageAdapter imageAdp;
	
	private TextView darkCounter;
	private TextView lightCounter;
	
	private GameI game = new Game(8,this);

	/** */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_game);

		//		Intent value_grid = getIntent();
		//		value_grid.getIntExtra("grid_dimension", 8);

		// PRENDE DIMENSIONI DISPLAY PER ADATTARE IL CONTENUTO
		Display display = getWindowManager().getDefaultDisplay();
		Point pnt = new Point();
		display.getSize(pnt); // restituisce la dimensione in pixel del display

		Log.d("X", new Integer(pnt.x).toString()); // width = larghezza = x
		Log.d("Y", new Integer(pnt.y).toString()); // height = altezza = y

		int boardHeight = (pnt.y / 100 ) * 100; // spazio per la griglia
		Log.d("BOARDHEIGHT", new Integer(boardHeight).toString());

		int boxSize = boardHeight / 8; // calcola il lato della casella della griglia
		Log.d("LATO CASELLA", new Integer(boxSize).toString());

		// Crea e setta la griglia
		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setNumColumns(8);
		gridView.setColumnWidth(boxSize);
		
		// Crea e associa ImageAdapter
		imageAdp = new ImageAdapter(this, boxSize, boxSize);
		gridView.setAdapter(imageAdp);
		
		darkCounter = (TextView) findViewById(R.id.playerDarkCounter);
		lightCounter = (TextView) findViewById(R.id.playerLightCounter);
				
		gridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Toast.makeText(PlayActivity.this, "" + position, Toast.LENGTH_SHORT).show();
				// INSERIRE CIO CHE DEVE FARE LA CASELLA CLICCATA				
			}
		});
		
		new Thread(game).start();
		
	}
	
	public void setPawn(final int x, final int y, final COLOR color) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				imageAdp.setPawn(x*8+y, color);
				
			}
		});
	}
	
	public void setCounter(int darkC, int lightC ) {
		darkCounter.setText(Integer.toString(darkC));
		lightCounter.setText(Integer.toString(lightC));
	}
}