package ai.othello.entities;

import intelligence.AlphaBeta;
import intelligence.DecisionI;
import intelligence.Minimax;

import java.util.Vector;

import ai.othello.graphic.PlayActivity;


public class Game implements GameI { 
			
	private int boardDim = 8;
	private int totalDiskNum = boardDim * boardDim;
	private SquareI[][] board = new Square[boardDim][boardDim];
	private int currentDisksNum = 0;
	private int darkDisksNum = 0;
	private int lightDisksNum = 0;
	private PlayerI currentPlayer = new Player(COLOR.DARK);
	private boolean vs = false;
	private int method = 0;
	private MoveI userMove = new Move(-1, -1);
	
	private PlayActivity gui;
	
	public Game(int boardDim, boolean vs, int method, PlayActivity gui) {
		this.gui = gui;
		this.boardDim = boardDim;
		this.vs = vs;
		this.method = method;
	}
	
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
		gui = game.getGui();
	}
	
	@Override
	public void run() {
		initialize();
		play();
	}

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
	
	@Override
	public void play() {
		// creation of the ai
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
		// game cicle
		while (!endCondition()) {
			
			if (getLegalMoves(currentPlayer.getColor()).size() > 0) {
				// user play
				if (vs && currentPlayer.getColor() == COLOR.DARK) {
					try {
						do {
							synchronized (this) {
								wait();
							}
						} while ( ! testLegal(userMove.getMoveI(), userMove.getMoveJ(), currentPlayer.getColor()));
						applyMove(userMove, true);
					}
					catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
				// computer play
				else {
					MoveI move = ai.getDecision(this);
					applyMove(move, true);
				}
			}
			currentPlayer.switchColor();
			gui.setPlayerTurn(currentPlayer.getColor());
		}
	}
	
	@Override
	public boolean endCondition() {
		if ( getLegalMoves(COLOR.DARK).size() == 0 && getLegalMoves(COLOR.LIGHT).size() == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public void applyMove(MoveI move, boolean draw) {
		int i = move.getMoveI();
		int j = move.getMoveJ();
		COLOR color = currentPlayer.getColor();
		board[i][j].setColor(color);
		currentDisksNum++;
		increment(color);
		if (draw) {
			gui.setPawn(i, j, color);
			gui.setCounter(darkDisksNum, lightDisksNum);
		}
		
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
	
	private void switchSingleDirection(int i, int j, int h, int v, boolean draw) {
		while (board[i+v][j+h].getColor() == currentPlayer.getAdversaryColor()) {
			switchDisk(i+v, j+h, draw);
			i = i + v;
			j = j + h;
		}
	}
	
	private void switchDisk(int i, int j, boolean draw) {
		decrement(board[i][j].getColor());
		board[i][j].switchColor();
		if (draw) {
			gui.setPawn(i, j, board[i][j].getColor());
		}
		increment(board[i][j].getColor());
		if (draw) {
			gui.setCounter(darkDisksNum, lightDisksNum);
		}
	}
	
	@Override
	public Vector<MoveI> getLegalMoves(COLOR color) {
		Vector<MoveI> legalMoves = new Vector<MoveI>();
		for (int i = 0; i < boardDim; i++) {
			for (int j = 0; j < boardDim; j++) {
				if (testLegal(i, j, color)) {
					legalMoves.addElement(new Move(i, j));
				}
			}
		}
		return legalMoves;
	}
	
	private boolean testLegal(int i, int j, COLOR color) {
		// valid coordinates
		if (i < 0 || i >= boardDim || j < 0 || j >= boardDim) {
			return false;
		}
		// square is free
		if (board[i][j].getColor() != COLOR.NONE) {
			return false;
		}
		// it produce at least a switch
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
	
	private boolean testSingleDirection(int i, int j, int h, int v, COLOR color) {
		
		COLOR adversaryColor;
		if (color == COLOR.DARK)
			adversaryColor = COLOR.LIGHT;
		else
			adversaryColor = COLOR.DARK;
		
		boolean atLeastOne = false;
		while (i+v < boardDim && j+h < boardDim &&
			   i+v >= 0 && j+h >= 0 &&
			   board[i+v][j+h].getColor() == adversaryColor) {
			
			i = i + v;
			j = j + h;
			atLeastOne = true;
		}
		if (i+v < boardDim && j+h < boardDim &&
			i+v >= 0 && j+h >= 0 &&
			atLeastOne &&
			board[i+v][j+h].getColor() == color) {
			
			return true;
		}
		return false;
	}
	
	private void increment(COLOR color) {
		if (color == COLOR.DARK) {
			darkDisksNum++;
		}
		else {
			lightDisksNum++;
		}
	}
	
	private void decrement(COLOR color) {
		if (color == COLOR.DARK) {
			darkDisksNum--;
		}
		else {
			lightDisksNum--;
		}
	}
	
	@Override
	public void setUserMove(int i, int j) {
		userMove.setMove(i, j);
	}
	
	@Override
	public SquareI[][] getBoard() {
		return board;
	}
	
	@Override
	public int getBoardDim() {
		return boardDim;
	}
	
	@Override
	public PlayerI getCurrentPlayer() {
		return currentPlayer;
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
	public PlayActivity getGui() {
		return gui;
	}
	
	@Override
	public boolean getVs() {
		return vs;
	}
	
	@Override
	public int getMethod() {
		return method;
	}
}
