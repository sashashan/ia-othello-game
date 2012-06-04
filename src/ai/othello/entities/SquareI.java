package ai.othello.entities;

public interface SquareI {
	
	public abstract void setColor(Game.COLOR color);
	
	public abstract Game.COLOR getColor();

	public abstract void switchColor();

}