package intelligence;

import ai.othello.entities.GameI;
import ai.othello.entities.MoveI;

public interface AlphaBetaI {

	public abstract MoveI getDecision(GameI game);
}
