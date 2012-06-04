package ai.othello.entities;

public class Move implements MoveI {

	private int i = 0;
	private int j = 0;
	
	public Move(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	@Override
	public int getMoveI() {
		return i;
	}
	
	@Override
	public int getMoveJ() {
		return j;
	}
	
	@Override
	public void setMove(int i, int j) {
		this.i = i;
		this.j = j;	
	}
}
