package de.unistuttgart.iste.sqa.pse.sheet06.homework.exercise1;

import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame;

/**
 * @author Silas Klein
 * @author Yves Gassmann
 * @author Georgi Bozhilski
 * @version 03-12-2021
 * 
 * With this class Paule runs in a spiral and puts grains down.
 * This works if the territory is a square or rectangle.
 * The spiral is laid with 1 field space between the grain fields.
 */
public class PainterPauleHamsterGameB extends SimpleHamsterGame {

	/**
	 * Creates a new PainterPauleHamsterGameB.<br>
	 * Do not modify!
	 */
	public PainterPauleHamsterGameB() {
		this.loadTerritoryFromResourceFile("/territories/PainterPauleBTerritory.ter");
		this.displayInNewGameWindow();
		game.startGame();
	}

	/**
	 * Put the hamster code into this method.<br>
	 * Solve the task in this method and NOT in the constructor!
	 */
	@Override
	protected void run() {
		// get the number of rows and columns minus the walls
		int rowCount = game.getTerritory().getTerritorySize().getRowCount() - 2;
		int colCount = game.getTerritory().getTerritorySize().getColumnCount() - 2;

		paule.putGrain();

		// Paule walks along the walls
		for (int i = 0; i < 3; i++) {

			if (paule.getDirection() == Direction.NORTH || paule.getDirection() == Direction.SOUTH) {
				runAndDrop(rowCount);
			} else {
				runAndDrop(colCount);
			}
		}

		rowCount -= 2;
		colCount -= 2;

		/**
		 * As long as you can substract 2 fields(field with grain and space) from the
		 * row number or column number, Paule will move and drop
		 * @ensures rowCount < 1
		 * @ensures colCount < 1
		 */
		while (rowCount >= 1 || colCount >= 1) {

			if (paule.getDirection() == Direction.NORTH || paule.getDirection() == Direction.SOUTH) {

				runAndDrop(rowCount);
				rowCount -= 2;

			} else {

				runAndDrop(colCount);
				colCount -= 2;
			}
		}
	}

	/**
	 * For a number of fields, at least 1 Paule has to go and drop grains
	 * 
	 * @param fields = number of fields
	 * @requires fields > 0
	 * @ensures old(game.getTerritory().getTotalGrainCount()) <
	 *          game.getTerritory().getTotalGrainCount()
	 */
	private void runAndDrop(int fields) {

		// Verifies that fields is bigger than 0
		if (fields < 1) {
			return;
		}

		// 0 < counter < fields
		for (int counter = 1; counter < fields; counter++) {

			if (paule.frontIsClear() && !paule.mouthEmpty()) {
				paule.move();
				paule.putGrain();
			} else {
				return;
			}
		}

		paule.turnLeft();
	}
}
