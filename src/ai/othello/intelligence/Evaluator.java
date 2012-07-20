package ai.othello.intelligence;

import ai.othello.entities.GameI;
import ai.othello.entities.GameI.COLOR;

// Oggetto che calcola il valore di valutazione dello stato del gioco
public class Evaluator implements EvaluatorI {
	
	@Override
	public int evaluate(GameI game) {
		int score = 0;
		// se lo stato in questione è relativo ad una partita terminata
		// allora la funzione di valutazione tiene conto soltanto della
		// differenza tra i dischi
		if (game.endCondition()) {
			score = game.getDarkDisksNum() - game.getLightDisksNum();
		}
		// Altrimenti considera anche mobilità e stabilità
		else {
			// mobilità
			int darkMobility = game.getLegalMoves(COLOR.DARK).size();
			int lightMobility = game.getLegalMoves(COLOR.LIGHT).size();
			int mobility = darkMobility - lightMobility;
			// stabilità
			int darkStability = 0;
			int lightStability = 0;
			// per ogni casella della scacchiera
			for (int i = 0; i < game.getBoardDim(); i++) {
				for (int j = 0; j < game.getBoardDim(); j++) {
					// se la casella non è vuota
					if (game.getBoard()[i][j].getColor() != COLOR.NONE) {
						// controlla che il disco in essa piazzato sia stabile.
						// Un disco viene considerato stabile se da esso si può raggiungere
						// il bordo della scacchiera lungo tutte le 4 direzioni attraversando
						// solo caselle occupate da dischi dello stesso colore
						if ( (stableDirection(game, i, j, 1, 0) || stableDirection(game, i, j, -1, 0) ) &&
							 (stableDirection(game, i, j, 0, 1) || stableDirection(game, i, j, 0, -1) ) &&
							 (stableDirection(game, i, j, 1, 1) || stableDirection(game, i, j, -1, -1) ) &&
							 (stableDirection(game, i, j, 1, -1) || stableDirection(game, i, j, -1, 1) ) ) {
						
							// incrementà la stabilità del giocatore del colore del disco considerato
							if (game.getBoard()[i][j].getColor() == COLOR.DARK) {
								darkStability++;
							}
							else {
								lightStability++;
							}
						}
					}
				}
			}
			int stability = darkStability - lightStability;
			// differenza di dischi
			int disksDiff = game.getDarkDisksNum() - game.getLightDisksNum();
			// valore di valutazione finale
			score = (100 * mobility) + (10 * stability) + (1 * disksDiff);
		}
		return score;
	}
	
	// controlla che si possa raggiungere il bordo della scacchiera attraversando
	// solo caselle del colore della pedina considerata, lungo uno verso.
	// Il verso è indicato dalla combinazione dei valori di h e v,
	// che possono assumere i valori -1, 0 e 1.
	private boolean stableDirection(GameI game, int i, int j, int v, int h) {
		
		COLOR color = game.getBoard()[i][j].getColor();
		boolean stable = true;
		while (i+v < game.getBoardDim() && j+h < game.getBoardDim() &&
			   i+v >= 0 && j+h >= 0 &&
			   stable) {
			
			if (game.getBoard()[i+v][j+h].getColor() != color) {
				stable = false;
			}
			i = i + v;
			j = j + h;
		}
		return stable;
	}

}
