package intelligence;

import ai.othello.entities.GameI;
import ai.othello.entities.MoveI;

public interface DecisionI {

	public abstract MoveI getDecision(GameI game);
}
