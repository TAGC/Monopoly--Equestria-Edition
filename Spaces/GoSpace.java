package Spaces;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import General.Player;

public class GoSpace implements Space{
	
	private final Set<Player> players = new HashSet<Player>();
	public static final int PASSGOBONUS = 200;
	
	@Override
	public void playerLands(Player player) {
		//obsolete
	}

	@Override
	public void display(int line) {
		String display;
		String playerLine;
		
		if (players.isEmpty()) {
			playerLine = "#                                   #\n";
		} else {
			playerLine = "#     O - ";
			Iterator<Player> iter = players.iterator();
			while (iter.hasNext()) {
				playerLine += iter.next().getName() + ", ";
			}
			
			playerLine = playerLine.substring(0, playerLine.length()-2);
			while (playerLine.length() < LINEWIDTH - 1) {
				playerLine += " ";
			}
			
			while (playerLine.length() > LINEWIDTH - 1) {
				playerLine = playerLine.substring(0,
						playerLine.length()-4);
				playerLine += "...";
			}
			
			playerLine += "#\n";
		}
		
		display  = "#####################################\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += "#      @@@@@@@@@     @@@@@@@@@      #\n";
		display += "#      @             @       @      #\n";
		display += "#      @             @       @      #\n";
		display += "#      @    @@@@     @       @      #\n";
		display += "#      @       @     @       @      #\n";
		display += "#      @@@@@@@@@     @@@@@@@@@      #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += "#      Collect 200 bits as you      #\n";
		display += "#              Pass Go              #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += playerLine;
		display += "#####################################\n";
		
		String[] lines;
		lines = display.split("\n");
		System.out.print(lines[line]);
	}
	
	@Override
	public void addPlayer(Player player) {
		players.add(player);
	}

	@Override
	public void removePlayer(Player player) {
		if (players.contains(player)) {
			players.remove(player);
		}
	}
}
