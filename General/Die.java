package General;

public class Die {
	
	private int lastRoll;
	private static final int DISPLAYLINES = 5;
	
	public int Roll() {
		int roll = Util.getRandom(6) + 1;
		lastRoll = roll;
		return roll;
	}
	
	public static void display(Die[] dice) {
		for (int i = 0; i < DISPLAYLINES; i++) {
			for (Die die : dice) {
				System.out.print("     ");
				die.display(i);
			}
			System.out.print("\n");
		}
	}
	
	public int getLastRoll() {
		return lastRoll;
	}
	
	public void display(int line) {
		String display;
		
		if (lastRoll == 1) {
			display  = "###########\n";
			display += "#         #\n";
			display += "#    0    #\n";
			display += "#         #\n";
			display += "###########\n";
		} else if (lastRoll == 2) {
			display  = "###########\n";
			display += "# 0       #\n";
			display += "#         #\n";
			display += "#       0 #\n";
			display += "###########\n";
		} else if (lastRoll == 3) {
			display  = "###########\n";
			display += "# 0       #\n";
			display += "#    0    #\n";
			display += "#       0 #\n";
			display += "###########\n";
		} else if (lastRoll == 4) {
			display  = "###########\n";
			display += "# 0     0 #\n";
			display += "#         #\n";
			display += "# 0     0 #\n";
			display += "###########\n";
		} else if (lastRoll == 5) {
			display  = "###########\n";
			display += "# 0     0 #\n";
			display += "#    0    #\n";
			display += "# 0     0 #\n";
			display += "###########\n";
		} else {
			display  = "###########\n";
			display += "# 0     0 #\n";
			display += "# 0     0 #\n";
			display += "# 0     0 #\n";
			display += "###########\n";
		}
		
		String[] lines = display.split("\n");
		System.out.print(lines[line]);
	}
}
