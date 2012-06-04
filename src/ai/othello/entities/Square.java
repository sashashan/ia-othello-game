package ai.othello.entities;

public class Square implements SquareI {
	
	private Game.COLOR color = Game.COLOR.NONE;
	
	public Square() {
		
	}
	
	public Square(SquareI square) {
		color = square.getColor();
	}
	
	@Override
	public void setColor(Game.COLOR color) {
		this.color = color;
	}
	
	@Override
	public Game.COLOR getColor() {
		return color;
	}
	
	@Override
	public void switchColor() {
		if (color == Game.COLOR.DARK) {
			color = Game.COLOR.LIGHT;
		}
		else if (color == Game.COLOR.LIGHT) {
			color = Game.COLOR.DARK;
		}
	}
}