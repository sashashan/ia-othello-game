package intelligence;

import java.util.Vector;

import ai.othello.entities.Game;
import ai.othello.entities.GameI;
import ai.othello.entities.Move;
import ai.othello.entities.MoveI;
import ai.othello.entities.GameI.COLOR;

public class AlphaBeta implements DecisionI {
	
	private int maxDepth = 7;
	private EvaluatorI evaluator = new Evaluator();
	
	@Override
	public MoveI getDecision(GameI game) {
		MoveI finalMove = new Move(-1, -1);
		Integer alpha = Integer.MIN_VALUE;
		Integer beta = Integer.MAX_VALUE;
		if (game.getCurrentPlayer().getColor() == Game.COLOR.DARK) {
			maxDecision(game, 0, finalMove, alpha, beta);
		}
		else {
			minDecision(game,0, finalMove, alpha, beta);
		}
		return finalMove;
	}
	
	private int maxDecision(GameI game, int depth, MoveI finalMove, Integer alpha, Integer beta) {
		if (depth >= maxDepth) {
			return evaluator.evaluate(game);
		}
		else {
			Vector<MoveI> legalMoves = game.getLegalMoves(COLOR.DARK);
			if (legalMoves.size() == 0) {
				return evaluator.evaluate(game);
			}
			else {
				int maxScore = Integer.MIN_VALUE;
				int bestMove = -1;
				for (int i = 0; i < legalMoves.size(); i++) {
					GameI newGame = new Game(game);
					newGame.applyMove(legalMoves.get(i), false);
					MoveI move = new Move(-1, -1);
					int score = minDecision(newGame, depth + 1, move, alpha, beta);
					if (score >= beta) {
						return score;
					}
					if (score > maxScore) {
						maxScore = score;
						bestMove = i;
					}
					if (score > alpha) {
						alpha = score;
					}
				}
				finalMove.setMove(legalMoves.get(bestMove).getMoveI(), legalMoves.get(bestMove).getMoveJ());
				return maxScore;
			}
		}
	}
	
	private int minDecision(GameI game, int depth, MoveI finalMove, Integer alpha, Integer beta) {
		if (depth >= maxDepth) {
			return evaluator.evaluate(game);
		}
		else {
			Vector<MoveI> legalMoves = game.getLegalMoves(COLOR.LIGHT);
			if (legalMoves.size() == 0) {
				return evaluator.evaluate(game);
			}
			else {
				int minScore = Integer.MAX_VALUE;
				int bestMove = -1;
				for (int i = 0; i < legalMoves.size(); i++) {
					GameI newGame = new Game(game);
					newGame.applyMove(legalMoves.get(i), false);
					MoveI move = new Move(-1, -1);
					int score = maxDecision(newGame, depth + 1, move, alpha, beta);
					if (score <= alpha) {
						return score;
					}
					if (score < minScore) {
						minScore = score;
						bestMove = i;
					}
					if (score < beta) {
						beta = score;
					}
				}
				finalMove.setMove(legalMoves.get(bestMove).getMoveI(), legalMoves.get(bestMove).getMoveJ());
				return minScore;
			}
		}
	}

}
