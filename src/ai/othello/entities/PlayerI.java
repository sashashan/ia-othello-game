package ai.othello.entities;

public interface PlayerI {

	public abstract void setColor(Game.COLOR color);
	
	public abstract Game.COLOR getColor();
	
	public abstract Game.COLOR getAdversaryColor();

	public abstract void switchColor();

}