package de.unistuttgart.iste.sqa.pse.sheet06.homework.exercise2;

import de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame;

/**
 * @author Silas Klein
 * @author Yves Gassmann
 * @author Georgi Bozhilski
 * @version 03-12-2021
 * 
 * This class lets paule run through a maze. While doing that he picks
 * up grains and ends his run at the exit with every grain.
 */
public class AmazingPauleHamsterGameB extends SimpleHamsterGame {

	/**
	 * Creates a new AmazingPauleHamsterGameB.<br>
	 * Do not modify!
	 */
	public AmazingPauleHamsterGameB() {
		this.loadTerritoryFromResourceFile("/territories/AmazingPauleBTerritory.ter");
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
	 * 
	 * @requires: game.getTerritory().getTotalGrainCount() == 1;
	 * @ensures: game.getTerritory().getTotalGrainCount() == 0;
	 * 
	 *           Precondition 2: Paule has no grains in his mouth Postcondition 2:
	 *           Paules mouth is not empty
	 */
	void passTheMaze() {
		// solve the maze here
		int totalGrains = game.getTerritory().getTotalGrainCount();

		// Paules run ends if he has picked up the grain
		while (totalGrains > 0) {

			// Loop gets terminated when paule has picked up the grain
			if (paule.grainAvailable()) {
				paule.pickGrain();
				totalGrains--;

				if (totalGrains == 0) {
					return;
				}
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
