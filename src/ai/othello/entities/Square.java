package ai.othello.entities;

// Rappresenta una singola casella della scacchiera 
public class Square implements SquareI {
	
	// il colore esprime se la casella è occupata da un disco chiaro
	// oppure scuro. Se la casella è vuota il colore è NONE
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
	
	// inverte il colore del disco posizionato in essa
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