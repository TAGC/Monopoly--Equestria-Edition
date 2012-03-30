package General;

import java.util.Random;
import java.util.Scanner;

public class Util {
	
	/*
	 * @return a boolean parameter: true if the player expresses "yes",
	 *                              false if the player expresses "no"
	 * 
	 * This method should be called when a yes/no choice is presented
	 * to a player. This method will receive input from the player
	 * expressing if they accept the choice or not
	 */
	public static boolean getResponse() {
		String response;
		Scanner input;
		
		input = new Scanner(System.in);
		response = input.next().toLowerCase();
		if (response.equals("y")) {
			return true;
		} else if (response.equals("n")) {
			return false;
		} else {
			System.out.println("Sorry, invalid response given; try again");
			return getResponse();
		}
	}
	
	/*
	 * @param upperLimit
	 *        	establishes the number of options available to the player
	 *          [1..upperLimit]
	 * 
	 * @return the option the player selects, or 0 if the player cancels
	 * 
	 * This method should be called when the player is presented with
	 * a range of options, and the player is asked to pick one of them
	 * or cancel
	 */
	public static int getOptionNum(int upperLimit) {
		Scanner input;
		String data;
		int option;
		boolean valid;
		
		input = new Scanner(System.in);
		option = 0;
		valid = false;
		do {
			try {
				data = input.nextLine();
				if (data.equals("")) {
					return 0;
				} else {
					option = Integer.parseInt(data);
				}
			} catch (Exception e) {
				System.out.println("That's not a valid number; try again");
				continue;
			}
			valid = 1 <= option && option <= upperLimit;
			if (!valid) {
				System.out.println("That's not a valid option");
			}
		} while (!valid);
		
		return option;
	}
	
	/*
	 * @param time
	 *        	the time in milliseconds to pause the execution
	 *          of the program for
	 * 
	 * Pauses the execution of the program for a
	 * specified length of time
	 */
	public static void pause(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * @param upperLimit
	 *        	establishes the range of integer values to return
	 *          a value from [0..upperLimit)
	 * 
	 * @return an integer value in the range [0..upperLimit)
	 * 
	 * Returns a random integer value. Acts exactly as 
	 * Random.nextInt(int upperLimit) would.
	 */
	public static int getRandom(int upperLimit) {
		Random gen;
		int num;
		
		gen = new Random();
		num = gen.nextInt(upperLimit);
		return num;
	}
	
	/*
	 * @return a float in the range [0..1)
	 */
	public static float getRandom() {
		Random gen;
		float num;
		
		gen = new Random();
		num = gen.nextFloat();
		return num;
	}
}
