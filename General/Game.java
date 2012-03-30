package General;
import java.util.Scanner;

import Spaces.MoonSpace;
import Spaces.Property;
import Spaces.Space;
import Spaces.Property.PropertyType;

public class Game {
	
	private static Scanner input;
	private static Player[] players;
	private static Board board;
	private static Die[] dice;
	private static final int startCash = 1500;
	
	public static void main(String[] args) {
		int numPlayers;
		boolean validNum;
		
		System.out.println("\n      M  O  N  O  P  O  L  Y");
		System.out.println("        EQUESTRIA  EDITION\n\n");
		
		input = new Scanner(System.in);
		
		System.out.println("Please enter number of players (2-4): ");
		
		validNum = false;
		do {
			try {
				numPlayers = Integer.parseInt(input.next());
			} catch (Exception e) {
				System.out.println("That is not a valid value; please try again");
				numPlayers = 0;
				continue;
			}
			
			validNum = numPlayers >= 2 && numPlayers <= 4;
			if (!validNum) {
				System.out.println("That is not a valid number of players");
			}
		} while (!validNum);
		
		players = new Player[numPlayers];
		
		for (int i = 0; i < numPlayers; i++) {
			Player player;
			player = getPlayerDetails(i+1);
			players[i] = player;
		}
		
		board = new Board(players);
		
		dice = new Die[2];
		dice[0] = new Die();
		dice[1] = new Die();
		
		//flush buffer
		input.nextLine();
		
		System.out.println("\n");
		while (!board.gameFinished()) {
			Player currentPlayer;
			
			currentPlayer = board.getCurrentPlayer();
			
			board.display();
			System.out.println();
			
			if (currentPlayer.missingTurn()) {
				System.out.println(currentPlayer.getName() + 
						" misses this turn");
				currentPlayer.setMissTurn(false);
			} else if (currentPlayer.isBanished()) {
				handleBanishment(currentPlayer);
			} else {
				System.out.println(currentPlayer.getName() + "'s turn: " +
						currentPlayer.getCash() +" bits");
				handlePlayerInput(currentPlayer);
			}
			
			System.out.println("\nPress Enter to complete your turn");
			input.nextLine();
			System.out.println("\n\n");
			board.completeTurn();
		}
		
		System.out.println("Congratulations " + winningPlayer().getName() + 
				" , you win!");
	}
	
	public static Player getPlayerDetails(int number) {
		String getName;
		String playerName;
		Player player;
		
		getName = String.format("Player %s, please enter name: ", number);
		System.out.println(getName);
		playerName = input.next();
		
		player = new Player(playerName, startCash);
		return player;
	}
	
	public static void handlePlayerInput(Player player) {
		int option;
		
		System.out.println("\nWhat would you like to do?");
		System.out.println("1) Roll the dice (completes turn)");
		System.out.println("2) Buy stables on your property");
		System.out.println("3) Sell a property");
		System.out.println("4) View your properties\n");
		
		do {
			option = Util.getOptionNum(4);
		} while (option == 0);
		
		switch(option) {
		case 1:
			movePlayer(player);
			return;
		case 2:
			buyStable(player);
			return;
		case 3:
			sellProperty(player);
			return;
		case 4:
			listProperties(player);
			return;
		default:
			return;
		}
	}
	
	public static void movePlayer(Player player) {
		rollDice(player);
		player.move(board);
	}
	
	public static void buyStable(Player player) {
		Property[] properties;
		Property property;
		int propertyCount, response, stablePrice;
		String message;
		
		propertyCount = displayProperties(player);
		
		if (propertyCount == 0) {
			System.out.println("You don't own any properties");
			handlePlayerInput(player);
			return;
		}
		
		message = String.format("Pick a number between 1 and %s " +
				"to buy stables for the associated property " +
				"or press Enter to cancel", propertyCount);
		
		System.out.println(message);
		
		response = Util.getOptionNum(propertyCount);
		if (response == 0) {
			handlePlayerInput(player);
			return;
		}
		
		properties = new Property[]{};
		properties = player.getProperties().toArray(properties);
		
		property = properties[response-1];
		
		if (property.getNumStables() == Property.MAXSTABLES) {
			System.out.println(property.getName() + " already has the " +
					"maximum number of stables");
			handlePlayerInput(player);
			return;
		}
		
		if (property.getType() != PropertyType.NORMAL) {
			System.out.println(property.getName() + " can not " +
					"support stables");
			handlePlayerInput(player);
			return;
		}
		
		int i = 0;
		for (Space space : board.getSpaces()) {
			if (space instanceof Property) {
				if (((Property)space).equals(property)) {
					break;
				}
			}
			i++;
		}
		
		stablePrice = 50 * i / (Board.BOARDSPACES / 4);
		
		Property[] groupProperties;
		groupProperties = board.getGroupProperties(property);
		
		for (Property groupProperty : groupProperties) {
			if (!groupProperty.getOwner().equals(player)) {
				System.out.println("You can not buy stables on this " +
						"property until you own every property in " +
						property.getGroup() + " group");
				handlePlayerInput(player);
				return;
			}
		}
		
		if (player.getCash() < stablePrice) {
			System.out.println("You cannot afford to place a stable " +
					"on this property");
			handlePlayerInput(player);
			return;
		}
		
		System.out.println("Are you sure you want to put an additional " +
				"stable on " + property.getName() + " for " + stablePrice +
				" bits? Y/N");
		
		if (Util.getResponse()) {
			property.purchase(player);
			System.out.println("*" + player.getName() + " purchased an " +
					"additional stable on " + property.getName() + "*");
		} else {
			System.out.println("Transaction cancelled");
		}
		
		handlePlayerInput(player);
	}
	
	public static void sellProperty(Player player) {
		Property[] properties;
		Property property;
		int propertyCount, response, sellValue;
		String message;
		propertyCount = displayProperties(player);
		
		if (propertyCount == 0) {
			System.out.println("You don't own any properties");
			handlePlayerInput(player);
			return;
		}
		
		message = String.format("Pick a number between 1 and %s " +
				"to sell the associated property", propertyCount);
		
		System.out.println(message);
		
		response = Util.getOptionNum(propertyCount);
		if (response == 0) {
			handlePlayerInput(player);
			return;
		}
		
		properties = new Property[]{};
		properties = player.getProperties().toArray(properties);
		
		property = properties[response-1];
		
		sellValue = property.getSellValue();
		
		System.out.println("Are you sure you want to sell " 
				+ property.getName() + " for " + 
				sellValue + " bits? Y/N");
		
		if (Util.getResponse()) {
			property.sell();
			System.out.println("*" + player.getName() + " sold " 
					+ property.getName() + " for " + sellValue 
					+ " bits*");
		} else {
			System.out.println("Transaction cancelled");
		}
		
		handlePlayerInput(player);
	}
	
	public static void listProperties(Player player) {
		Property[] properties;
		properties = new Property[]{};
		
		properties = player.getProperties().toArray(properties);
		
		if (properties.length == 0) {
			System.out.println("You don't own any properties");
			handlePlayerInput(player);
			return;
		}
		
		System.out.println("\nProperties you own:");
		for (Property property : properties) {
			System.out.println(property);
		}
		
		handlePlayerInput(player);
	}
	
	public static int displayProperties(Player player) {
		Property[] properties;
		properties = new Property[]{};
		
		properties = player.getProperties().toArray(properties);
		if (properties.length == 0) {
			return 0;
		}
		
		System.out.println("Properties you own:");
		for (int i = 0; i < properties.length; i++) {
			System.out.println(String.format("%s) %s", (i+1), properties[i]));
		}
		
		return properties.length;
	}
	
	public static void rollDice(Player player) {
		//roll dice
		player.setLastRoll(dice[0].Roll(), dice[1].Roll());
		
		System.out.println("Dice rolling...");
		Util.pause((long)(Util.getRandom()*3000+1000));
		System.out.println("Dice settling...");
		Util.pause((long)(Util.getRandom()*1500+500));
		
		System.out.println("*" + player.getName() + " rolled "
				+ player.getLastTotalRoll() + "*\n");
		
		Die.display(dice);
		
		System.out.println();
		if (dice[0].getLastRoll() == dice[1].getLastRoll()) {
			System.out.println("*" + player.getName() + " rolled " +
					"a double!*");
		}
		
		System.out.println();
	}
	
	public static void handleBanishment(Player player) {
		System.out.println("You're banished on the Moon.");
		
		//make sure the player is on a banish space
		while (!(board.getSpaces()[player.getBoardPosition()] 
				instanceof MoonSpace)) {
			player.setPosition((player.getBoardPosition() + 1) 
					% Board.BOARDSPACES);
		}
		
		if (player.getBanishmentAvoidChances() > 0) {
			System.out.println("You can leave the Moon by using a " +
					"banishment avoid chance. Would you like to do " +
					"this? Y/N");
			if (Util.getResponse()) {
				player.changeBanishmentAvoidChances(-1);
				player.setBanished(false);
				System.out.println("You're no longer banished. " +
						"You have "
						+ player.getBanishmentAvoidChances() + 
						" banishment avoid chances left");
				return;
			}
		}
		
		if (player.getCash() >= 50) {
			System.out.println("You can leave the Moon now by " +
					"paying 50 bits. Would you like to pay? Y/N");
			if (Util.getResponse()) {
				player.changeCash(-50);
				player.setBanished(false);
				System.out.println("You're no longer banished");
				return;
			}
		}
		
		System.out.println("You can escape from the Moon if you roll " +
				"a double. Press Enter when you want to roll");
		
		//wait for Enter key to be pressed
		input.nextLine();
		
		rollDice(player);
		
		int[] lastRolls = player.getLastRolls();
		if (lastRolls[0] == lastRolls[1]) {
			System.out.println("You escaped!");
			player.setBanished(false);
			player.move(board);
		} else {
			System.out.println("You remain on the Moon until at least " +
					"your next turn");
		}
	}
	
	public static Player winningPlayer() {
		Player bestPlayer = players[0];
		for (Player player : players) {
			if (player.getCash() > bestPlayer.getCash()) {
				bestPlayer = player;
			}
		}
		return bestPlayer;
	}
}
