package ai.othello.graphic;

import ai.othello.entities.Game;
import ai.othello.entities.GameI;
import ai.othello.entities.GameI.COLOR;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PlayActivity extends Activity {

	private ImageAdapter imageAdp;

	private TextView darkCounter;
	private TextView lightCounter;

	private ImageView darkImg;
	private ImageView lightImg;

	private int gameType;

	private GameI game = new Game(8, false, 0, this);

	/** */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_game);

		gameType = getIntent().getIntExtra("algorithm", 0);
		
		// gameType == 0 --> MinMax
		// gameType == 1 --> AlphaBeta

		// PRENDE DIMENSIONI DISPLAY PER ADATTARE IL CONTENUTO
		Display display = getWindowManager().getDefaultDisplay();
		Point pnt = new Point();
		display.getSize(pnt); // restituisce la dimensione in pixel del display

		Log.d("X", Integer.valueOf(pnt.x).toString()); // width = larghezza = x
		Log.d("Y", Integer.valueOf(pnt.y).toString()); // height = altezza = y

		int boardHeight = (pnt.y / 100 ) * 90; // spazio per la griglia
		Log.d("BOARDHEIGHT", Integer.valueOf(boardHeight).toString());

		int boxSize = boardHeight / 8; // calcola il lato della casella della griglia
		Log.d("LATO CASELLA", Integer.valueOf(boxSize).toString());

		// CREA E SETTA LA GRIGLIA
		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setNumColumns(8);
		gridView.setColumnWidth(boxSize);

		// CREA E SETTA ImageAdapter ALLA GRIGLIA
		imageAdp = new ImageAdapter(this, boxSize, boxSize);
		gridView.setAdapter(imageAdp);

		// RIGHT LAYOUT
		LinearLayout layoutRight = (LinearLayout) findViewById(R.id.layoutRight);
		Log.d("dim altezza layout", Integer.toString(boardHeight));
		layoutRight.setMinimumHeight(pnt.y);
		Log.d("dim larghezza layout", Integer.toString(pnt.x-boardHeight));
		layoutRight.setMinimumWidth((pnt.x-boardHeight)-42);

		darkCounter = (TextView) findViewById(R.id.playerDarkCounter); // dark Counter
		lightCounter = (TextView) findViewById(R.id.playerLightCounter); // light Counter

		darkImg = (ImageView) findViewById(R.id.imageDark); // img player dark
		lightImg = (ImageView) findViewById(R.id.imageLight); // img player light

		gridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				synchronized (game) {
					int i = position / game.getBoardDim();
					int j = position % game.getBoardDim();
					game.setUserMove(i, j);
					game.notifyAll();
				}
			}
		});

		Button bntExitGame = (Button)findViewById(R.id.bntExitGame); // bottone exit partita
		bntExitGame.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent exit = new Intent(PlayActivity.this, OthelloActivity.class);
				startActivity(exit);
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});        

		// START GAME
		new Thread(game).start(); // start del thread di gioco
		game.setMethod(gameType);
	}

	public void setPawn(final int x, final int y, final COLOR color) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				imageAdp.setPawn(x*8+y, color);

			}
		});
	}

	public void setCounter(final int darkC, final int lightC ) { 
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Log.d("DARK", Integer.toString(darkC));
				Log.d("LIGHT", Integer.toString(lightC));
				darkCounter.setText(Integer.toString(darkC));
				lightCounter.setText(Integer.toString(lightC));
			}
		});

	}

	public void setPlayerTurn(final COLOR color) { 
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(color.toString() == "DARK") {
					Log.d("dark", color.toString());
					darkImg.setBackgroundResource(R.color.yellow);
					lightImg.setBackgroundResource(R.color.grey);
				}
				else {
					Log.d("light", color.toString());
					lightImg.setBackgroundResource(R.color.yellow);
					darkImg.setBackgroundResource(R.color.grey);
				}

			}
		});

	}
}