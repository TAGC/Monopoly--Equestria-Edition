package Spaces;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import Cards.EventCard;
import Cards.EventCard.EventCardType;
import General.Board;
import General.Player;
import General.Util;

public class ChanceSpace implements Space {
	
	private final Set<Player> players = new HashSet<Player>();
	
	@Override
	public void playerLands(Player player) {
		Scanner input;
		EventCard card;
		
		drawCard();
		System.out.println();
		
		input = new Scanner(System.in);
		System.out.println("Please press Enter to draw a Chance card");
		
		input.nextLine();
		
		System.out.println("Drawing...");
		Util.pause((long)(Util.getRandom()*2000 + 500));
		
		card = Board.getEventCard(EventCardType.CHANCE);
		card.applyToPlayer(player);
	}
	
	private void drawCard() {
		
		String cardString;
		
		cardString  = "#########################\n";
		cardString += "#                       #\n";
		cardString += "#      C H A N C E      #\n";
		cardString += "#                       #\n";
		cardString += "#          @@@@         #\n";
		cardString += "#        @@    @@       #\n";
		cardString += "#       @        @      #\n";
		cardString += "#      @          @     #\n";
		cardString += "#                @      #\n";
		cardString += "#              @@       #\n";
		cardString += "#             @         #\n";
		cardString += "#            @          #\n";
		cardString += "#                       #\n";
		cardString += "#            @          #\n";
		cardString += "#                       #\n";
		cardString += "#                       #\n";
		cardString += "#########################\n";
		
		System.out.println(cardString);
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
		display += "#   @@@@ @  @  @@  @  @ @@@@ @@@@   #\n";
		display += "#   @    @  @ @  @ @@ @ @    @      #\n";
		display += "#   @    @@@@ @  @ @@@@ @    @@@    #\n";
		display += "#   @    @  @ @@@@ @ @@ @    @      #\n";
		display += "#   @@@@ @  @ @  @ @  @ @@@@ @@@@   #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
		display += "#        Draw a Chance Card         #\n";
		display += "#                                   #\n";
		display += "#                                   #\n";
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