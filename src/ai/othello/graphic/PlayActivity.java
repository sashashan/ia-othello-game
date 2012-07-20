package ai.othello.graphic;

import ai.othello.entities.Game;
import ai.othello.entities.GameI;
import ai.othello.entities.GameI.COLOR;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
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
	private int vs;

	private GameI game = new Game(8, false, 0, this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_game);

		gameType = getIntent().getIntExtra("algorithm", 0);
		// gameType == 0 --> MinMax
		// gameType == 1 --> AlphaBeta

		vs = getIntent().getIntExtra("vs", 0);
		// vs == 0 --> device vs device
		// vs == 1 --> player vs device

		// PRENDE DIMENSIONI DISPLAY PER ADATTARE IL CONTENUTO
		Display display = getWindowManager().getDefaultDisplay();
		Point pnt = new Point();
		display.getSize(pnt); // restituisce la dimensione in pixel del display

		int boardHeight = (pnt.y / 100 ) * 90; // spazio per la griglia

		int boxSize = boardHeight / 8; // calcola il lato della casella della griglia

		// CREA E SETTA LA GRIGLIA
		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setNumColumns(8);
		gridView.setColumnWidth(boxSize);

		// CREA E SETTA ImageAdapter ALLA GRIGLIA
		imageAdp = new ImageAdapter(this, boxSize, boxSize);
		gridView.setAdapter(imageAdp);

		// RIGHT LAYOUT
		LinearLayout layoutRight = (LinearLayout) findViewById(R.id.layoutRight);
		layoutRight.setMinimumHeight(pnt.y);
		layoutRight.setMinimumWidth((pnt.x-boardHeight)-42);

		darkCounter = (TextView) findViewById(R.id.playerDarkCounter); // dark Counter
		lightCounter = (TextView) findViewById(R.id.playerLightCounter); // light Counter

		darkImg = (ImageView) findViewById(R.id.imageDark); // img dark player 
		lightImg = (ImageView) findViewById(R.id.imageLight); // img light player

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

		new Thread(game).start(); // start del thread di gioco
		game.setMethod(gameType); // set algoritmo utilizzato
		if(vs==0) { 			  // set modalità di gioco
			game.setVs(false);	
		}
		else {
			game.setVs(true);
		}
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
					darkImg.setBackgroundResource(R.color.yellow);
					lightImg.setBackgroundResource(R.color.brown);
				}
				else {
					lightImg.setBackgroundResource(R.color.yellow);
					darkImg.setBackgroundResource(R.color.brown);
				}
			}
		});
	}
}