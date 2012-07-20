package ai.othello.intelligence;

// interfaccia per entrambi gli algoritmi di decisione minimax e alpha-beta pruning
import ai.othello.entities.GameI;
import ai.othello.entities.MoveI;

public interface DecisionI {

	public abstract MoveI getDecision(GameI game);
}
