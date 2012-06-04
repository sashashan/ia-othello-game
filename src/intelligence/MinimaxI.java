package intelligence;

import ai.othello.entities.GameI;
import ai.othello.entities.MoveI;

public interface MinimaxI {

	public abstract MoveI getDecision(GameI game);
}
