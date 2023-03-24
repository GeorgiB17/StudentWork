package de.unistuttgart.iste.sqa.pse.sheet06.homework.exercise2;

import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame;

/**
 * @author Silas Klein
 * @author Yves Gassmann
 * @author Georgi Bozhilski
 * @version 03-12-2021
 * 
 * This class lets Paule run through a maze using the left hand rule
 */
public class AmazingPauleHamsterGame extends SimpleHamsterGame {

	/**
	 * Creates a new AmazingPauleHamsterGame.<br>
	 * Do not modify!
	 */
	public AmazingPauleHamsterGame() {
		this.loadTerritoryFromResourceFile("/territories/AmazingPauleTerritory.ter");
		this.displayInNewGameWindow();
		game.startGame();
	}

	/**
	 * Ignore this method.<br>
	 * Put your code in passTheMaze()!
	 */
	@Override
	protected void run() {
		passTheMaze();
	}

	/**
	 * This method lets paule run throught the maze until he has found the grain
	 * @requires: game.getTerritory().getTotalGrainCount() == 1;
	 * @ensures: game.getTerritory().getTotalGrainCount() == 0;
	 * 
	 * Precondition 2: Paule has no grains in his mouth
	 * Postcondition 2: Paules mouth is not empty
	 */
	void passTheMaze() {
		// solve the maze here
		
		// Paules run ends if he has picked up the grain
		while (paule.mouthEmpty()) {

			// Loop gets terminated when paule has picked up the grain
			if (paule.grainAvailable()) {
				paule.pickGrain();
				break;
			}

			paule.turnLeft();

			// Paule moves if the front is clear after left turn
			if (paule.frontIsClear()) {
				paule.move();
			} else {
				// Paule does a right turn if left is blocked
				paule.turnLeft();
				paule.turnLeft();
				paule.turnLeft();

				// Paule moves if the front is clear after first right turn
				if (paule.frontIsClear()) {
					paule.move();
				} else {
					// Paule does a right turn if left is blocked
					paule.turnLeft();
					paule.turnLeft();
					paule.turnLeft();

					// Paule moves if the front is clear after second right turn
					if (paule.frontIsClear()) {
						paule.move();
					} else {
						// Paule does a right turn if left is blocked.
						paule.turnLeft();
						paule.turnLeft();
						paule.turnLeft();

						// Paule moves if the front is clear after third right turn. Scenario: dead end
						if (paule.frontIsClear()) {
							paule.move();
						}
					}
				}
			}
		}
	}
}
