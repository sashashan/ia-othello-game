package ai.othello.entities;


import java.util.Vector;

import ai.othello.graphic.PlayActivity;
import ai.othello.intelligence.AlphaBeta;
import ai.othello.intelligence.DecisionI;
import ai.othello.intelligence.Minimax;

// Rappresenta lo stato del gioco e mette a disposizione i metodi
// con i quali interagire col gioco stesso.
public class Game implements GameI { 

	private int boardDim = 8; // dimensione della scacchiera
	private int totalDiskNum = boardDim * boardDim; // numero di caselle della scacchiera
	private SquareI[][] board = new Square[boardDim][boardDim]; // scacchiera
	private int currentDisksNum = 0; // numero di dischi piazzati
	private int darkDisksNum = 0; // numero di dischi scuri piazzati
	private int lightDisksNum = 0; // numero di dischi chiari piazzati
	private PlayerI currentPlayer = new Player(COLOR.DARK); // colore del giocatore corrente
	private boolean vs = false; // indica se la partita si svolge computer contro computer (false)
	                            // oppure persona contro computer (true)
	private int method = 0; // algoritmo di ricerca: 0 per minimax e 1 per alpha-beta pruning
	private MoveI userMove = new Move(-1, -1); // mossa corrente
	private int deep = 1;

	private PlayActivity gui; // riferimento alla grafica

	// costruttore del gioco
	public Game(int boardDim, boolean vs, int method, PlayActivity gui) {
		this.gui = gui;
		this.boardDim = boardDim;
		this.vs = vs;
		this.method = method;
	}

	// costruttore di copia del gioco.
	//Utilizzato per clonare lo stato prima dell'analisi di ogni mossa
	public Game(GameI game) {
		boardDim = game.getBoardDim();
		totalDiskNum = game.getTotalDisksNum();
		board = new Square[boardDim][boardDim];
		for (int i = 0; i < boardDim; i++) {
			for (int j = 0; j < boardDim; j++) {
				board[i][j] = new Square(game.getBoard()[i][j]);
			}
		}
		currentDisksNum = game.getCurrentDisksNum();
		darkDisksNum = game.getDarkDisksNum();
		lightDisksNum = game.getLightDisksNum();
		currentPlayer = game.getCurrentPlayer();
		vs = game.getVs();
		method = game.getMethod();
		deep = game.getDeep();
		gui = game.getGui();
	}

	// per far partire il gioco in un thread separato rispetto alla grafica
	// che può dunque aggiornarsi durante la partita
	@Override
	public void run() {
		initialize();
		play();
	}

	// inizializzazione del gioco per l'inizio di una partita.
	// 4 dischi al centro, 2 per colore
	// gioca per primo il giocatore nero
	@Override
	public void initialize() {
		board = new Square[boardDim][boardDim];
		for (int i = 0; i < boardDim; i++) {
			for (int j = 0; j < boardDim; j++) {
				board[i][j] = new Square();
			}
		}
		currentDisksNum = 4;
		darkDisksNum = 2;
		lightDisksNum = 2;
		currentPlayer.setColor(COLOR.DARK);
		// first four disks
		board[(boardDim/2)-1][(boardDim/2)-1].setColor(COLOR.LIGHT);
		gui.setPawn((boardDim/2)-1, (boardDim/2)-1, COLOR.LIGHT);
		board[(boardDim/2)-1][boardDim/2].setColor(COLOR.DARK);
		gui.setPawn((boardDim/2)-1, boardDim/2, COLOR.DARK);
		board[boardDim/2][(boardDim/2)-1].setColor(COLOR.DARK);
		gui.setPawn(boardDim/2, (boardDim/2)-1, COLOR.DARK);
		board[boardDim/2][boardDim/2].setColor(COLOR.LIGHT);
		gui.setPawn(boardDim/2, boardDim/2, COLOR.LIGHT);

		gui.setCounter(darkDisksNum, lightDisksNum);
		gui.setPlayerTurn(currentPlayer.getColor());
	}

	// ciclo di gioco
	@Override
	public void play() {
		// creazione, in base all'algoritmo scelto, dell'agente che decide la mossa
		DecisionI ai;
		switch (method) {
		case 0:
			ai = new Minimax();
			break;
		case 1:
			ai = new AlphaBeta();
			break;	
		default:
			ai = new Minimax();
			break;
		}
		// ciclo delle mosse
		while (!endCondition()) {

			// se il giocatore corrente ha a disposizione almeno una mossa legale
			if (getLegalMoves(currentPlayer.getColor()).size() > 0) {
				
				// se deve giocare la persona (che ha per dafault colore scuro)
				if (vs && currentPlayer.getColor() == COLOR.DARK) {
					// si attende la sua interazione con la grafica
					// l'attesa continua finchè l'utente non effettua una mossa legale
					try {
						do {
							synchronized (this) {
								wait();
							}
						} while ( ! testLegal(userMove.getMoveI(), userMove.getMoveJ(), currentPlayer.getColor()));
						// applicazione della mossa legale
						applyMove(userMove, true);
					}
					catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
				
				// se deve giocare il computer
				else {
					// si delega all'agente la scelta della mossa
					// che poi viene applicata
					MoveI move = ai.getDecision(this, deep);
					applyMove(move, true);
				}
			}
			// cambio del giocatore corrente
			currentPlayer.switchColor();
			gui.setPlayerTurn(currentPlayer.getColor());
		}
	}

	// test di fine partita
	// una partita termina quando nessuno dei giocatori può eseguire mosse legali
	@Override
	public boolean endCondition() {
		if ( getLegalMoves(COLOR.DARK).size() == 0 && getLegalMoves(COLOR.LIGHT).size() == 0) {
			return true;
		}
		return false;
	}

	// ritorna un vettore contenente tutte le mosse legali per il giocatore
	// del colore dato in ingresso
	@Override
	public Vector<MoveI> getLegalMoves(COLOR color) {
		Vector<MoveI> legalMoves = new Vector<MoveI>();
		// per ogni casella della scacchiera testa se la
		// rispettiva mossa è legale
		for (int i = 0; i < boardDim; i++) {
			for (int j = 0; j < boardDim; j++) {
				if (testLegal(i, j, color)) {
					legalMoves.addElement(new Move(i, j));
				}
			}
		}
		return legalMoves;
	}
	
	// testa se la mossa data in ingresso è legale
	private boolean testLegal(int i, int j, COLOR color) {
		// test di validità delle coordinate
		if (i < 0 || i >= boardDim || j < 0 || j >= boardDim) {
			return false;
		}
		// testa che la posizione sia libera
		if (board[i][j].getColor() != COLOR.NONE) {
			return false;
		}
		// testa che la mossa produca il rovesciamento di almeno una
		// pedina dell'avversario. Si testano gli 8 versi singolarmente.
		if (testSingleDirection(i, j, -1, -1, color)) {
			return true;
		}
		if (testSingleDirection(i, j, -1, 0, color)) {
			return true;
		}
		if (testSingleDirection(i, j, -1, +1, color)) {
			return true;
		}
		if (testSingleDirection(i, j, 0, -1, color)) {
			return true;
		}
		if (testSingleDirection(i, j, 0, +1, color)) {
			return true;
		}
		if (testSingleDirection(i, j, +1, -1, color)) {
			return true;
		}
		if (testSingleDirection(i, j, +1, 0, color)) {
			return true;
		}
		if (testSingleDirection(i, j, +1, +1, color)) {
			return true;
		}
		return false;
	}

	// test del rovesciamento di almeno una pedina in un singolo verso.
	// Il verso è indicato dalla combinazione dei valori di h e v,
	// che possono assumere i valori -1, 0 e 1.
	private boolean testSingleDirection(int i, int j, int h, int v, COLOR color) {

		COLOR adversaryColor;
		if (color == COLOR.DARK)
			adversaryColor = COLOR.LIGHT;
		else
			adversaryColor = COLOR.DARK;

		boolean atLeastOne = false;
		// a partire dalla posizione della mossa si controlla che,
		// lungo il verso stabilito, esista un cammino di almeno un
		// disco del colore avversario.
		while (i+v < boardDim && j+h < boardDim &&
				i+v >= 0 && j+h >= 0 &&
				board[i+v][j+h].getColor() == adversaryColor) {

			i = i + v;
			j = j + h;
			atLeastOne = true;
		}
		// nella posizione che segue questo cammino deve essere
		// piazzato un disco del colore del giocatore corrente
		if (i+v < boardDim && j+h < boardDim &&
				i+v >= 0 && j+h >= 0 &&
				atLeastOne &&
				board[i+v][j+h].getColor() == color) {

			return true;
		}
		return false;
	}
	
	// applicazione della mossa
	@Override
	public void applyMove(MoveI move, boolean draw) {
		int i = move.getMoveI();
		int j = move.getMoveJ();
		COLOR color = currentPlayer.getColor();
		// piazzamento della pedina
		board[i][j].setColor(color);
		// incremento dei dischi totali piazzati
		currentDisksNum++;
		// incremento delle pedine del colore del giocatore corrente
		increment(color);
		// draw indica se la mossa deve avere effetti sulla grafica o meno.
		// In particolare, se una mossa è soltanto simulata dall'algoritmo
		// di ricerca per capirne l'efficacia, essa non deve avere effetti
		// grafici. Se invece è una mossa effettiva li deve avere.
		if (draw) {
			gui.setPawn(i, j, color);
			gui.setCounter(darkDisksNum, lightDisksNum);
		}

		// Per ognuno degli 8 versi si testa se la mossa produce effetti.
		// Nel caso la mossa produca effetti lungo un verso si procede con
		// la procedura che effettua il rovesciamento delle pedine
		if (testSingleDirection(i, j, -1, -1, color)) {
			switchSingleDirection(i, j, -1, -1, draw);
		}
		if (testSingleDirection(i, j, -1, 0, color)) {
			switchSingleDirection(i, j, -1, 0, draw);
		}
		if (testSingleDirection(i, j, -1, +1, color)) {
			switchSingleDirection(i, j, -1, +1, draw);
		}
		if (testSingleDirection(i, j, 0, -1, color)) {
			switchSingleDirection(i, j, 0, -1, draw);
		}
		if (testSingleDirection(i, j, 0, +1, color)) {
			switchSingleDirection(i, j, 0, +1, draw);
		}
		if (testSingleDirection(i, j, +1, -1, color)) {
			switchSingleDirection(i, j, +1, -1, draw);
		}
		if (testSingleDirection(i, j, +1, 0, color)) {
			switchSingleDirection(i, j, +1, 0, draw);
		}
		if (testSingleDirection(i, j, +1, +1, color)) {
			switchSingleDirection(i, j, +1, +1, draw);
		}
	}

	// effettua il rovesciamento delle pedine, causato da una mossa,
	// lungo in singolo verso.
	// il verso è indicato dalla combinazione dei valori di h e v,
	// che possono assumere i valori -1, 0 e 1.
	private void switchSingleDirection(int i, int j, int h, int v, boolean draw) {
		while (board[i+v][j+h].getColor() == currentPlayer.getAdversaryColor()) {
			switchDisk(i+v, j+h, draw);
			i = i + v;
			j = j + h;
		}
	}

	// effettua il rovesciamento di un singolo disco
	private void switchDisk(int i, int j, boolean draw) {
		// decrementa il numero di dischi del colore del disco che sta per
		// essere rovesciato
		decrement(board[i][j].getColor());
		// rovescia il disco
		board[i][j].switchColor();
		// mostra gli effetti del rovesciamento solo se la mossa è effettiva
		if (draw) {
			gui.setPawn(i, j, board[i][j].getColor());
		}
		// incrementa il numero di dischi del nuovo colore del disco rovesciato
		increment(board[i][j].getColor());
		// aggiorna il contatore grafico solo se la mossa è effettiva
		if (draw) {
			gui.setCounter(darkDisksNum, lightDisksNum);
		}
	}

	// incrementa il numero di dischi del colore in ingresso
	private void increment(COLOR color) {
		if (color == COLOR.DARK) {
			darkDisksNum++;
		}
		else {
			lightDisksNum++;
		}
	}

	// decrementa il numero di dischi del colore in ingresso
	private void decrement(COLOR color) {
		if (color == COLOR.DARK) {
			darkDisksNum--;
		}
		else {
			lightDisksNum--;
		}
	}

	// vari set e get
	
	@Override
	public int getBoardDim() {
		return boardDim;
	}
	
	@Override
	public SquareI[][] getBoard() {
		return board;
	}
	
	@Override
	public int getTotalDisksNum() {
		return totalDiskNum;
	}

	@Override
	public int getCurrentDisksNum() {
		return currentDisksNum;
	}

	@Override
	public int getDarkDisksNum() {
		return darkDisksNum;
	}

	@Override
	public int getLightDisksNum() {
		return lightDisksNum;
	}
	
	@Override
	public PlayerI getCurrentPlayer() {
		return currentPlayer;
	}
	
	@Override
	public boolean getVs() {
		return vs;
	}
	
	@Override
	public void setVs(boolean vs) {
		this.vs = vs;
	}

	@Override
	public int getMethod() {
		return method;
	}

	@Override
	public void setMethod(int method) {
		this.method = method;	
	}

	@Override
	public void setUserMove(int i, int j) {
		userMove.setMove(i, j);
	}
	
	@Override
	public PlayActivity getGui() {
		return gui;
	}
	
	@Override
	public int getDeep() {
		return deep;
	}
	
	@Override
	public void setDeep(int deep) {
		this.deep = deep;	
	}

}
