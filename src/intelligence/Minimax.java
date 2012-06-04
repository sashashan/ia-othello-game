package intelligence;

import java.util.Vector;
import ai.othello.entities.Game;
import ai.othello.entities.GameI;
import ai.othello.entities.GameI.COLOR;
import ai.othello.entities.Move;
import ai.othello.entities.MoveI;

public class Minimax implements MinimaxI {
	
	private int maxDepth = 4;

	@Override
	public MoveI getDecision(GameI game) {
		MoveI finalMove = new Move(-1, -1);
		Integer finalScore = new Integer(-1);
		if (game.getCurrentPlayer().getColor() == Game.COLOR.DARK) {
			maxDecision(game, 0, finalScore, finalMove);
		}
		else {
			minDecision(game,0, finalScore, finalMove);
		}
		return finalMove;
	}
	
	private void maxDecision(GameI game, int depth, Integer finalScore, MoveI finalMove) {
		if (depth >= maxDepth) {
			finalScore = evaluate(game);
		}
		else {
			Vector<MoveI> legalMoves = game.getLegalMoves(COLOR.DARK);
			if (legalMoves.size() == 0) {
				finalScore = evaluate(game);
			}
			else {
				int maxScore = Integer.MIN_VALUE;
				int bestMove = -1;
				for (int i = 0; i < legalMoves.size(); i++) {
					GameI newGame = new Game(game);
					newGame.applyMove(legalMoves.get(i), false);
					Integer score = new Integer(-1);
					MoveI move = new Move(-1, -1);
					minDecision(newGame, depth + 1, score, move); 
					if (score > maxScore) {
						maxScore = score;
						bestMove = i;
					}
				}
				finalScore = maxScore;
				finalMove.setMove(legalMoves.get(bestMove).getMoveI(), legalMoves.get(bestMove).getMoveJ());
//				finalMove = legalMoves.get(bestMove);
			}
		}
	}
	
	private void minDecision(GameI game, int depth, Integer finalScore, MoveI finalMove) {
		if (depth >= maxDepth) {
			finalScore = evaluate(game);
		}
		else {
			Vector<MoveI> legalMoves = game.getLegalMoves(COLOR.LIGHT);
			if (legalMoves.size() == 0) {
				finalScore = evaluate(game);
			}
			else {
				int minScore = Integer.MAX_VALUE;
				int bestMove = -1;
				for (int i = 0; i < legalMoves.size(); i++) {
					GameI newGame = new Game(game);
					newGame.applyMove(legalMoves.get(i), false);
					Integer score = new Integer(-1);
					MoveI move = new Move(-1, -1);
					minDecision(newGame, depth + 1, score, move); 
					if (score < minScore) {
						minScore = score;
						bestMove = i;
					}
				}
				finalScore = minScore;
				finalMove.setMove(legalMoves.get(bestMove).getMoveI(), legalMoves.get(bestMove).getMoveJ());
				//finalMove = legalMoves.get(bestMove);
			}
		}
	}
	
	private int evaluate(GameI game) {
		int score = 0;
		if (game.endCondition()) {
			// disks difference
			score = game.getDarkDisksNum() - game.getLightDisksNum();
		}
		else {
			// mobility
			int darkMobility = game.getLegalMoves(COLOR.DARK).size();
			int lightMobility = game.getLegalMoves(COLOR.LIGHT).size();
			int mobility = darkMobility - lightMobility;
			// disks difference
			int disksDiff = game.getDarkDisksNum() - game.getLightDisksNum();
			// total score
			score = (100 * mobility) + (1 * disksDiff);
		}
		return score;
	}
	
}
