package Spaces;

import General.Player;

public class GoToMoonSpace implements Space {
	
	@Override
	public void playerLands(Player player) {
		player.setBanished(true);
		
		String outcome;
		outcome = String.format("*%s is banished to the Moon!*",
				player.getName());
		System.out.println(outcome);
	}

	@Override
	public void display(int line, boolean showPlayers) {
		String display;
		
		display  = "#####################################\n";
		display += "#                                   #\n";
		display += "#   @@@@@  @@@@@     @@@@@  @@@@@   #\n";
		display += "#   @      @   @       @    @   @   #\n";
		display += "#   @ @@@  @   @       @    @   @   #\n";
		display += "#   @   @  @   @       @    @   @   #\n";
		display += "#   @@@@@  @@@@@       @    @@@@@   #\n";
		display += "#                                   #\n";
		display += "#     @@ @@  @@@@@  @@@@@  @@  @    #\n";
		display += "#     @ @ @  @   @  @   @  @@  @    #\n";
		display += "#     @   @  @   @  @   @  @ @ @    #\n";
		display += "#     @   @  @   @  @   @  @  @@    #\n";
		display += "#     @   @  @@@@@  @@@@@  @  @@    #\n";
		display += "#                                   #\n";
		display += "#  Go to the Moon - do not Pass Go  #\n";
		display += "#      Do not collect 200 bits      #\n";
		display += "#                                   #\n";
		display += "#####################################\n";
		
		String[] lines;
		lines = display.split("\n");
		System.out.print(lines[line]);
	}
	
	@Override
	public void addPlayer(Player player) {
		//obsolete
	}

	@Override
	public void removePlayer(Player player) {
		//obsolete
	}
	
}
