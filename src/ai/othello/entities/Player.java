package ai.othello.entities;

public class Player implements PlayerI {
		
	private Game.COLOR color;
	
	public Player(Game.COLOR color) {
		this.color = color;
	}

	@Override
	public Game.COLOR getColor() {
		return color;
	}
	
	@Override
	public void setColor(Game.COLOR color) {
		this.color = color;
	}
	
	@Override
	public Game.COLOR getAdversaryColor() {
		if (color == Game.COLOR.DARK) {
			return Game.COLOR.LIGHT;
		}
		else {
			return Game.COLOR.DARK;
		}
	}
	
	@Override
	public void switchColor() {
		if (color == Game.COLOR.DARK) {
			color = Game.COLOR.LIGHT;
		}
		else {
			color = Game.COLOR.DARK;
		}
	}
}
