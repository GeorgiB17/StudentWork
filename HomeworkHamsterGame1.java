package de.unistuttgart.iste.sqa.pse.sheet02.homework;

/**
 * Describe the class HomeworkHamsterGame here.
 * 
 * @author (Your name)
 * @version (a version number or a date)
 */
public class HomeworkHamsterGame extends InternalHomeworkHamsterGame {

	/*
	 * Confused Paule! Help Paule to walk through the territory and to bring all the
	 * grains into his cave. In the process Paule should collect all grains on the
	 * field and deposit them in his cave (lower left corner - row 6 * column 2 and
	 * indexing starts with 0).
	 */
	@Override
	protected void hamsterRun() {
		// EXERCISE 2:
		// Help Paule to walk through the territory and to bring all the grains
		// into his cave.
       
		
		// Amount of Paules grains in her mouth
		int numberGrain=0;

		// Walk to the first grain
		paule.move();
		paule.move();
		paule.move();
		paule.move();
		paule.move();

		// Pick up the first grain
		paule.pickGrain();
		numberGrain++;
		paule.write("I have " + numberGrain +" grain in my mouth.");
		

		// Turn in the direction of the second grain
		paule.turnLeft();
		paule.turnLeft();
		paule.turnLeft();

		// Move to the second grain
		paule.move();
		paule.move();
		paule.move();
		paule.move();
		paule.move();
	
		// Pick up the second grain
		paule.pickGrain();
		numberGrain++;
		paule.write("I have " + numberGrain +" grain in my mouth.");

		// Add further steps (comment + code) to collect all grains!
		// Get to the third grain
		paule.turnLeft();
		paule.turnLeft();
		paule.move();
		paule.move();
		paule.move(); 
		paule.turnLeft();
		paule.move();
		paule.move();
		paule.move();
		
		// Pick up the third grain
		paule.pickGrain();	
		
		// Add one grain in Paules mouth 
		numberGrain++;
		paule.write("I have " + numberGrain +" grains in my mouth.");
		
		// Get to the fourth grain
		paule.turnLeft();
		paule.move();
		
		// Right turn 
		paule.turnLeft();
		paule.turnLeft();
		paule.turnLeft();
		paule.move();
		paule.move();
		
		// Add one grain in Paules mouth 
		paule.pickGrain();
		numberGrain++;
		paule.write("I have " + numberGrain +" grains in my mouth.");
		
		// Get to Paules cave
		paule.turnLeft();
		paule.turnLeft();
		paule.move();
		paule.move();
		paule.move();
		
		// Right turn
		paule.turnLeft();
		paule.turnLeft();
		paule.turnLeft();
		
		paule.move();
		paule.move();
		
		// Right turn
		paule.turnLeft();
		paule.turnLeft();
		paule.turnLeft();
		
		paule.move();
		paule.move();
		paule.move();
		
		//Paule picks up all the grains in his cave
		paule.pickGrain();
		numberGrain++;
		paule.write("I have " + numberGrain +" grains in my mouth.");
		paule.pickGrain();
		numberGrain++;
		paule.write("I have " + numberGrain +" grains in my mouth.");
		paule.pickGrain();
		numberGrain++;
		paule.write("I have " + numberGrain +" grains in my mouth.");
		
		
	
		
		// Number of grains in Paules mouth decreases
	
		
		// Paule informs you about the amount of grains in her mouth
		// Paule leaves all his grains in the cave
		while (paule.mouthEmpty() == false) {
            paule.putGrain();
            numberGrain--;

            if (numberGrain < 1) {
                paule.write("I got " + numberGrain + " grains.");
            }
            else {
                paule.write("I got " + numberGrain + " grain.");
            }
        }
	}
}
