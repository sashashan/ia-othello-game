package ai.othello.intelligence;

import ai.othello.entities.GameI;

public interface EvaluatorI {

	public abstract int evaluate(GameI game);
}
