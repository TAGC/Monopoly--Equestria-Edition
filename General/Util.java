package General;

import java.util.Random;
import java.util.Scanner;

public class Util {
	
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
	
	public static void pause(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static int getRandom(int upperLimit) {
		Random gen;
		int num;
		
		gen = new Random();
		num = gen.nextInt(upperLimit);
		return num;
	}
	
	public static float getRandom() {
		Random gen;
		float num;
		
		gen = new Random();
		num = gen.nextFloat();
		return num;
	}
}
