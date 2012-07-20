package ai.othello.entities;

// Rappresenta un giocatore. In particolare serve a indicare il giocatore corrente
public class Player implements PlayerI {
	
	// colore del giocatore
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
	
	// restituisce il colore dell'avversario
	@Override
	public Game.COLOR getAdversaryColor() {
		if (color == Game.COLOR.DARK) {
			return Game.COLOR.LIGHT;
		}
		else {
			return Game.COLOR.DARK;
		}
	}
	
	// inverte il colore. Utile per cambiare il giocatore corrente
	// dopo una mossa
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
