package Spaces;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import General.Player;

public class TaxSpace implements Space {
	
	private final Set<Player> players = new HashSet<Player>();
	
	int taxValue;
	String name;
	String taxMessage;
	
	public TaxSpace(String name, int taxValue, String taxMessage) {
		this.name       = name;
		this.taxValue   = taxValue;
		this.taxMessage = taxMessage;
	}
	
	@Override
	public void playerLands(Player player) {
		player.changeCash(-taxValue);
		
		String outcome;
		outcome = String.format("%s (%s bits)",
				taxMessage, taxValue);
		System.out.println(outcome);
	}

	@Override
	public void display(int line, boolean showPlayers) {
		String display;
		String playerLine;
		
		if (players.isEmpty() || !showPlayers) {
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
		display += "#    @@@@@@     @@@    @     @      #\n";
		display += "#    @     @   @   @    @   @       #\n";
		display += "#    @@@@@@   @     @    @@@        #\n";
		display += "#    @        @@@@@@@    @          #\n";
		display += "#    @        @     @   @           #\n";
		display += "#                                   #\n";
		display += "#    @@@@@@@    @@@    @     @      #\n";
		display += "#       @      @   @    @   @       #\n";
		display += "#       @     @     @    @@@        #\n";
		display += "#       @     @@@@@@@   @   @       #\n";
		display += "#       @     @     @  @     @      #\n";
		display += "#                                   #\n";
		display += "#     Celestia needs the money      #\n";
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
