package Spaces;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import General.Player;

public class MoonSpace implements Space {
	
	private final Set<Player> players = new HashSet<Player>();
	
	@Override
	public void playerLands(Player player) {
		String outcome;
		
		outcome = String.format("You're on the Moon, but just visiting");
		
		System.out.println(outcome);
	}

	@Override
	public void display(int line, boolean showPlayers) {
		String display;
		String playerLineFree;
		String playerLineBanished;
		
		int banished = 0;
		for (Player player : players) {
			if (player.getBanished() > 0) banished++;
		}
		
		if (players.size() - banished == 0 || !showPlayers) {
			playerLineFree = "#                                   #\n";
		} else {
			playerLineFree = "#     O - ";
			Iterator<Player> iter = players.iterator();
			Player nextPlayer;
			while (iter.hasNext()) {
				nextPlayer = iter.next();
				if (nextPlayer.getBanished() > 0) continue;
				
				playerLineFree += nextPlayer.getName() + ", ";
			}
			
			playerLineFree = playerLineFree.substring(0, 
					playerLineFree.length()-2);
			while (playerLineFree.length() < LINEWIDTH - 1) {
				playerLineFree += " ";
			}
			
			while (playerLineFree.length() > LINEWIDTH - 1) {
				playerLineFree = playerLineFree.substring(0,
						playerLineFree.length()-4);
				playerLineFree += "...";
			}
			
			playerLineFree += "#\n";
		}
		
		if (banished == 0 || !showPlayers) {
			playerLineBanished = "#       #                           #\n";
		} else {
			playerLineBanished = "#       #     O - ";
			Iterator<Player> iter = players.iterator();
			Player nextPlayer;
			while (iter.hasNext()) {
				nextPlayer = iter.next();
				if (nextPlayer.getBanished() == 0) continue;
				
				playerLineBanished += nextPlayer.getName() + ", ";
			}
			
			playerLineBanished = playerLineBanished.substring(0, 
					playerLineBanished.length()-2);
			while (playerLineBanished.length() < LINEWIDTH - 1) {
				playerLineBanished += " ";
			}
			
			while (playerLineBanished.length() > LINEWIDTH - 1) {
				playerLineBanished = playerLineFree.substring(0,
						playerLineBanished.length()-4);
				playerLineBanished += "...";
			}
			
			playerLineBanished += "#\n";
		}
		
		display  = "#####################################\n";
		display += playerLineFree;
		display += "#               J U S T             #\n";
		display += "#                                   #\n";
		display += "#       #############################\n";
		display += "#       #                           #\n";
		display += "#       #                           #\n";
		display += "#   V   #                           #\n";
		display += "#   I   #                           #\n";
		display += "#   S   #            O N            #\n";
		display += "#   I   #                           #\n";
		display += "#   T   #                           #\n";
		display += "#   I   #          M O O N          #\n";
		display += "#   N   #                           #\n";
		display += "#   G   #                           #\n";
		display += "#       #                           #\n";
		display += playerLineBanished;
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
