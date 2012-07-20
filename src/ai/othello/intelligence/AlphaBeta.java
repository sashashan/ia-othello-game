package ai.othello.intelligence;

import java.util.Vector;

import ai.othello.entities.Game;
import ai.othello.entities.GameI;
import ai.othello.entities.Move;
import ai.othello.entities.MoveI;
import ai.othello.entities.GameI.COLOR;

//Algoritmo di ricerca con avversari alpha-beta pruning
public class AlphaBeta implements DecisionI {

	private int maxDepth = 7; // limite alla profondità dell'albero di ricerca
	private EvaluatorI evaluator = new Evaluator(); // oggetto che valuta gli stati

	// sceglie la mossa da effettuare e la restituisce
	@Override
	public MoveI getDecision(GameI game) {
		// inizializza la mossa con un valore nullo
		MoveI finalMove = new Move(-1, -1);
		// inizializza i valori alpha e beta
		Integer alpha = Integer.MIN_VALUE;
		Integer beta = Integer.MAX_VALUE;
		// a seconda del colore del giocatore sceglie di massimizzare
		// o minimizzare la funzione di valutazione
		if (game.getCurrentPlayer().getColor() == Game.COLOR.DARK) {
			maxDecision(game, 0, finalMove, alpha, beta);
		}
		else {
			minDecision(game,0, finalMove, alpha, beta);
		}
		return finalMove;
	}

	// Algoritmo per MAX
	private int maxDecision(GameI game, int depth, MoveI finalMove, Integer alpha, Integer beta) {
		// se è stato raggiunto il livello massimo valuta lo stato
		if (depth >= maxDepth) {
			return evaluator.evaluate(game);
		}
		// Altrimenti
		else {
			// se non si possono effettuare mosse legali
			Vector<MoveI> legalMoves = game.getLegalMoves(COLOR.DARK);
			// se non si possono effettuare mosse legali
			if (legalMoves.size() == 0) {
				Vector<MoveI> adversaryLegalMoves = game.getLegalMoves(COLOR.LIGHT);
				// se nemmeno l'avversario può effettuare mosse legali
				// allora lo stato viene valutato
				if (adversaryLegalMoves.size() == 0) {
					return evaluator.evaluate(game);
				}
				// altrimenti l'agente simula un passaggio
				else {
					MoveI move = new Move(-1, -1);
					GameI newGame = new Game(game);
					int score = minDecision(newGame, depth + 1, move, alpha, beta);
					finalMove.setMove(-1, -1); // mossa nulla
					return score;
				}
			}
			else {
				// calcola la mossa che produce il punteggio maggiore
				int maxScore = Integer.MIN_VALUE;
				int bestMove = -1;
				for (int i = 0; i < legalMoves.size(); i++) {
					// copia lo stato del gioco
					GameI newGame = new Game(game);
					// applica la mossa
					newGame.applyMove(legalMoves.get(i), false);
					// calcola la mossa successiva dell'avversario
					MoveI move = new Move(-1, -1);
					int score = minDecision(newGame, depth + 1, move, alpha, beta);
					// se il punteggio è maggiore di quello del miglior valore per MIN
					// trovato al di fuori del cammino corrente, allora pota
					if (score >= beta) {
						return score;
					}
					// controlla se la mossa è la migliore
					if (score > maxScore) {
						maxScore = score;
						bestMove = i;
					}
					// aggiorna alpha se il punteggio è maggiore
					if (score > alpha) {
						alpha = score;
					}
				}
				// setta la mossa finale e ritorna il suo punteggio
				finalMove.setMove(legalMoves.get(bestMove).getMoveI(), legalMoves.get(bestMove).getMoveJ());
				return maxScore;
			}
		}
	}

	// Algoritmo per MIN
	private int minDecision(GameI game, int depth, MoveI finalMove, Integer alpha, Integer beta) {
		// se è stato raggiunto il livello massimo valuta lo stato
		if (depth >= maxDepth) {
			return evaluator.evaluate(game);
		}
		// Altrimenti
		else {
			// se non si possono effettuare mosse legali
			Vector<MoveI> legalMoves = game.getLegalMoves(COLOR.LIGHT);
			// se non si possono effettuare mosse legali
			if (legalMoves.size() == 0) {
				Vector<MoveI> adversaryLegalMoves = game.getLegalMoves(COLOR.DARK);
				// se nemmeno l'avversario può effettuare mosse legali
				// allora lo stato viene valutato
				if (adversaryLegalMoves.size() == 0) {
					return evaluator.evaluate(game);
				}
				// altrimenti l'agente simula un passaggio
				else {
					MoveI move = new Move(-1, -1);
					GameI newGame = new Game(game);
					int score = maxDecision(newGame, depth + 1, move, alpha, beta);
					finalMove.setMove(-1, -1); // mossa nulla
					return score;
				}
			}
			else {
				// calcola la mossa che produce il punteggio minore
				int minScore = Integer.MAX_VALUE;
				int bestMove = -1;
				for (int i = 0; i < legalMoves.size(); i++) {
					// copia lo stato del gioco
					GameI newGame = new Game(game);
					// applica la mossa
					newGame.applyMove(legalMoves.get(i), false);
					// calcola la mossa successiva dell'avversario
					MoveI move = new Move(-1, -1);
					int score = maxDecision(newGame, depth + 1, move, alpha, beta);
					// se il punteggio è minore di quello del miglior valore per MAX
					// trovato al di fuori del cammino corrente, allora pota
					if (score <= alpha) {
						return score;
					}
					// controlla se la mossa è la migliore
					if (score < minScore) {
						minScore = score;
						bestMove = i;
					}
					// aggiorna beta se il punteggio è minore
					if (score < beta) {
						beta = score;
					}
				}
				// setta la mossa finale e ritorna il suo punteggio
				finalMove.setMove(legalMoves.get(bestMove).getMoveI(), legalMoves.get(bestMove).getMoveJ());
				return minScore;
			}
		}
	}

}
