package Cards;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import General.Board;
import General.Die;
import General.Player;
import General.Util;
import Spaces.GoSpace;
import Spaces.Property;
import Spaces.Property.PropertyGroup;
import Spaces.Property.PropertyType;
import Spaces.Space;

public class EventCard {
	
	private String          name;
	private String          message;
	private EventCardType   type;
	private CardEffect      effect;
	private Board           board;
	
	public static enum CardEffect 
	{ CHANCE_1,
	  CHANCE_2,
	  CHANCE_3,
	  CHANCE_4,
	  CHANCE_5,
	  CHANCE_6,
	  CHANCE_7,
	  CHANCE_8,
	  CHANCE_9,
	  CHANCE_10,
	  CHANCE_11,
	  CHANCE_12,
	  CHANCE_13,
	  CHANCE_14,
	  CHANCE_15,
	  CHANCE_16,
	  
	  HARMONY_1,
	  HARMONY_2,
	  HARMONY_3,
	  HARMONY_4,
	  HARMONY_5,
	  HARMONY_6,
	  HARMONY_7,
	  HARMONY_8,
	  HARMONY_9,
	  HARMONY_10,
	  HARMONY_11,
	  HARMONY_12,
	  HARMONY_13,
	  HARMONY_14,
	  HARMONY_15,
	  HARMONY_16
	  };
	
	public static enum EventCardType
	{ HARMONY, CHANCE };
	
	public EventCard(String name, String message, 
			CardEffect effect,
			EventCardType type) {
		
		this.name       = name;
		this.message    = message;
		this.effect     = effect;
		this.type       = type;
	}
	
	public void addBoard(Board board) {
		this.board = board;
	}
	
	public void applyToPlayer(Player player) {
		System.out.println(name + "\n");
		System.out.println(message);
		
		switch(effect) {
		case CHANCE_1:
			swapProperties(player);
			return;
		case CHANCE_2:
			player.setMissTurn(true);
			return;
		case CHANCE_3:
			applyPoisonjoke(player);
			return;
		case CHANCE_4:
			player.changeCash(500);
			return;
		case CHANCE_5:
			drawHarmonyCard(player);
			return;
		case CHANCE_6:
			payForDresses(player);
			return;
		case CHANCE_7:
			goToSugarcubeCorner(player);
			return;
		case CHANCE_8:
			payNextPlayerAndReroll(player);
			return;
		case CHANCE_9:
			player.changeCash(150);
			return;
		case CHANCE_10:
			player.banish();
			return;
		case CHANCE_11:
			player.changeBanishmentAvoidChances(1);
			return;
		case CHANCE_12:
			receiveBitsOrDrawHarmony(player);
			return;
		case CHANCE_13:
			player.setMissTurn(true);
			return;
		case CHANCE_14:
			transferBitsBetweenPlayers(player, 50, false);
			return;
		case CHANCE_15:
			player.changeCash(100);
			return;
		case CHANCE_16:
			payLibraryDamages(player);
			return;
			
		case HARMONY_1:
			player.changeBanishmentAvoidChances(1);
			return;
		case HARMONY_2:
			changeCashOnStables(player, 50, false);
			return;
		case HARMONY_3:
			player.changeCash(400);
			return;
		case HARMONY_4:
			getPaidOrReceiveAppleAcres(player);
			return;
		case HARMONY_5:
			changeAllPlayerCash(100, false);
			return;
		case HARMONY_6:
			advanceToStationAndPay(player);
			return;
		case HARMONY_7:
			player.changeCash(100);
			return;
		case HARMONY_8:
			if (player.getLastTotalRoll() % 2 == 0) {
				//last total roll value was even
				player.changeCash(-150);
			} else {
				//last total roll value was odd
				player.changeCash(600);
			}
			return;
		case HARMONY_9:
			player.changeCash(100);
			player.setMissTurn(true);
			return;
		case HARMONY_10:
			int playerPosition;
			playerPosition = player.getBoardPosition();
			player.setPosition(playerPosition - 2*player.getLastTotalRoll());
			return;
		case HARMONY_11:
			player.changeCash(50);
			return;
		case HARMONY_12:
			player.banish();
			return;
		case HARMONY_13:
			receiveOnApplejackProperty(player);
			return;
		case HARMONY_14:
			player.setExtraTurn(true);
			return;
		case HARMONY_15:
			int change = changeCashPerPropertyGroup(player);
			System.out.println("Your wealth changed by " + change + " bits");
			return;
		case HARMONY_16:
			player.setMissTurn(true);
			return;
		default:
			return;
		}
		
	}
	
	/*
	 * @param player
	 *        	the player whose property is getting swapped
	 *          with another player's
	 * 
	 * If possible, swaps a property of the player's with the property
	 * of another player's of equal or lower worth
	 */
	private void swapProperties(Player player) {
		Iterator<Property> self_iter, other_iter;
		List<Property> self_properties, other_properties;
		Property selfProperty, otherProperty;
		self_properties = player.getProperties();
		int selfValue, otherValue;
		String message;
		
		for (Player a_player : board.getPlayers()) {
			if (!player.equals(a_player)) {
				other_properties = a_player.getProperties();
				
				self_iter  = self_properties.iterator();
				while (self_iter.hasNext()) {
					selfProperty = self_iter.next();
					selfValue = selfProperty.getValue();
					
					other_iter = other_properties.iterator();
					while (other_iter.hasNext()) {
						otherProperty = other_iter.next();
						otherValue = otherProperty.getValue();
						
						if (selfValue >= otherValue) {
							selfProperty.setOwner(a_player);
							otherProperty.setOwner(player);
							message = String.format("%s swapped %s with %s " +
									"for %s", player.getName(),
									selfProperty.getName(), 
									a_player.getName(),
									otherProperty.getName());
							System.out.println(message);
							return;
						}
					}
				}
			}
		}
		
		System.out.println("No swaps were possible, so this " +
				"card has no effect");
	}
	
	/*
	 * @param player
	 *        	the player the poisonjoke effect is
	 *          applied to
	 * 
	 * The player experiences a randomly chosen effect
	 * depending on their next die roll
	 */
	private void applyPoisonjoke(Player player) {
		Die die;
		int rollValue;
		Scanner input;
		die = new Die();
		input = new Scanner(System.in);
		
		System.out.println("Press Enter to roll the die");
		input.nextLine();
		
		System.out.println("Die rolling...");
		Util.pause((long)(Util.getRandom()*3000 + 1000));
		System.out.println("Die settling...");
		Util.pause((long)(Util.getRandom()*1500 + 500));
		rollValue = die.Roll();
		System.out.println("You rolled " + rollValue);
		
		int deltaPosition, playerPosition, i;
		int difFromCarousel;
		playerPosition = player.getBoardPosition();
		
		switch(rollValue) {
		case 1:
			System.out.println("You've shrunk in size! You lose " +
					"50 bits but get another turn");
			player.changeCash(-50);
			player.setExtraTurn(true);
			return;
		case 2:
			System.out.println("Your voice has deepened! You move " +
					"back 10 spaces, but gain 100 bits");
			player.setPosition(playerPosition - 10);
			player.changeCash(100);
			return;
		case 3:
			System.out.println("Your tongue has swollen! You end up " +
					"on another nearby space");
			do {
				deltaPosition = Util.getRandom(11) - 5;
			} while (deltaPosition != 0);
			
			player.setPosition(playerPosition + deltaPosition);
			return;
		case 4:
			System.out.println("Your wings have gone all wonky! " +
					"Move forward 8 spaces but lose 20 bits");
			player.setPosition(playerPosition + 8);
			player.changeCash(-20);
			return;
		case 5:
			System.out.println("You're suffering from excessive hair " +
					"growth! You gain 10 bits for every space past " +
					"the Carousel Boutique you are, but miss a turn");
			
			i = board.findPropertyIndex("Carousel Boutique");
			
			difFromCarousel = playerPosition - i;
			if (difFromCarousel < 0) {
				difFromCarousel = Board.BOARDSPACES + difFromCarousel;
			}
			
			player.changeCash(difFromCarousel * 10);
			player.setMissTurn(true);
			return;
		case 6:
			System.out.println("Your horn is wonky! You teleport to " +
					"another position on the board");
			do {
				deltaPosition = Util.getRandom(40);
			} while (deltaPosition != 0);
			
			player.setPosition(playerPosition + deltaPosition);
			return;
		default:
			return;
		}
	}
	
	/*
	 * @param player
	 *        	the player that draws the Harmony card
	 * 
	 * The player is made to draw a Harmony card
	 */
	private void drawHarmonyCard(Player player) {
		EventCard card;
		Scanner input;
		
		input = new Scanner(System.in);
		System.out.println("Press Enter to draw the card");
		input.nextLine();
		
		System.out.println("Drawing...");
		Util.pause((long)(Util.getRandom()*2000+500));
		
		card = Board.getEventCard(EventCardType.HARMONY);
		card.applyToPlayer(player);
		return;
	}
	
	/*
	 * @param player
	 *        	the player ordering the dresses
	 * 
	 * If another player owns Carousel Boutique,
	 * this player pays 150 bits to that owner.
	 * Otherwise this player pays 100 bits to the
	 * bank
	 */
	private void payForDresses(Player player) {
		Player owner;
		Property property;
		
		owner = null;
		for (Space space : board.getSpaces()) {
			if (space instanceof Property) {
				property = (Property)space;
				if (property.getName().equals("Carousel Boutique")) {
					owner = property.getOwner();
					break;
				}
			}
		}
		
		if (owner == null) {
			player.changeCash(-100);
		} else if (!player.equals(owner)) {
			int amount = 150;
			player.changeCash(-amount);
			owner.changeCash(amount);
		}
	}
	
	/*
	 * @param player
	 *        	the player that's moved
	 * 
	 * The player is moved such that they end up
	 * on Sugarcube Corner. If they Pass Go, they
	 * gain PASSGOBONUS bits.
	 */
	private void goToSugarcubeCorner(Player player) {
		int playerPosition, propertyIndex;
		
		playerPosition = player.getBoardPosition();
		propertyIndex = board.findPropertyIndex("Sugarcube Corner");
		if (propertyIndex < playerPosition) {
			player.changeCash(GoSpace.PASSGOBONUS);
		}
		
		player.setPosition(propertyIndex);
	}
	
	/*
	 * @param player
	 *        	the player that pays and rerolls
	 * 
	 * Transfers 50 bits from the player to the person
	 * that plays after them. Gives them an extra turn
	 */
	private void payNextPlayerAndReroll(Player player) {
		Player nextPlayer;
		Player[] players;
		players = board.getPlayers();
		int amount;
		
		
		nextPlayer = null;
		for (int i = 0; i < players.length; i++) {
			if (players[i].equals(player)) {
				nextPlayer = players[(i+1) % players.length];
			}
		}
		
		amount = 50;
		nextPlayer.changeCash(amount);
		player.changeCash(-amount);
		player.setExtraTurn(true);
	}
	
	/*
	 * @param player
	 *        	the player that receives bits or draws a Harmony card
	 *        
	 * The player either receives 25 bits from every other player
	 * or draws a Harmony card
	 */
	private void receiveBitsOrDrawHarmony(Player player) {
		int amount, total;
		
		amount = 25;
		System.out.println("Would you prefer to gain the bits?");
		if (Util.getResponse()) {
			total = transferBitsBetweenPlayers(player, amount, true);
			System.out.println("You gained " + total + " bits in total");
		} else {
			drawHarmonyCard(player);
		}
	}
	
	/*
	 * @param player
	 *        	the player paying others
	 * 
	 * @param amount
	 *        	the amount payed to other players
	 * 
	 * @param receiving
	 *        	a boolean parameter specifying whether
	 *          the player is receiving the money or
	 *          paying it out
	 * 
	 * @return the total amount payed or received
	 * 
	 * The player pays to or receives from the other players
	 * the amount specified
	 */
	private int transferBitsBetweenPlayers(Player player, int amount, 
			boolean receiving) {
		int total;
		
		amount *= (receiving) ? 1 : -1; 
		total = 0;
		for (Player a_player : board.getPlayers()) {
			if (!a_player.equals(player)) {
				a_player.changeCash(-amount);
				player.changeCash(amount);
				total += Math.abs(amount);
			}
		}
		
		return total;
	}
	
	/*
	 * @param player
	 *        	the player whose wealth is affected
	 * 
	 * @param amount
	 *        	the amount of cash changed per stable
	 *          the player owns
	 *   
	 * @param receiving
	 *        	a boolean parameter specifying whether
	 *          the player is receiving cash based on
	 *          the number of stables they own, or paying it
	 * 
	 * @return the total amount of cash change
	 * 
	 * The player pays or receives cash based on how many stables
	 * they own across all properties they own
	 */
	private int changeCashOnStables(Player player, int amount, 
			boolean receiving) {
		
		Iterator<Property> iter;
		int total;
		
		amount *= (receiving) ? 1 : -1;
		total = 0;
		iter = player.getProperties().iterator();
		while (iter.hasNext()) {
			total += iter.next().getNumStables() * amount;
		}
		
		player.changeCash(total);
		return Math.abs(total);
	}
	
	/*
	 * @param player
	 *        	the player paying for damages
	 * 
	 * The player loses 50 bits to the owner if the Tree Library
	 * is owned, or 75 bits to the bank if it is not owned
	 */
	private void payLibraryDamages(Player player) {
		Space space;
		Property library;
		Player owner;
		
		space = board.getSpaces()[board.findPropertyIndex("Tree Library")];
		if (space instanceof Property) {
			library = (Property)space;
		} else {
			return;
		}
		
		owner = library.getOwner();
		if (owner == null) {
			player.changeCash(-75);
		} else if (!owner.equals(player)) {
			int amount = 50;
			owner.changeCash(amount);
			player.changeCash(-amount);
		}
	}
	
	/*
	 * @param player
	 *        	the player getting paid or receiving the property
	 *        
	 * The player receives 50 bits if Sweet Apple Acres is owned
	 * or receives the property if it is not yet owned
	 */
	private void getPaidOrReceiveAppleAcres(Player player) {
		Space space;
		Property appleAcres;
		Player owner;
		
		space = board.getSpaces()[board.findPropertyIndex(
				"Sweet Apple Acres")];
		if (space instanceof Property) {
			appleAcres = (Property)space;
		} else {
			return;
		}
		
		owner = appleAcres.getOwner();
		if (owner == null) {
			appleAcres.setOwner(player);
		} else if (!owner.equals(player)) {
			int amount = 50;
			owner.changeCash(-amount);
			player.changeCash(amount);
		}
	}
	
	/*
	 * @param amount
	 *        	the amount all players' wealth is adjusted by
	 * 
	 * @param receiving
	 *        	a boolean parameter specifying whether the players
	 *          are paying or receiving from the bank
	 *          
	 * @return the total amount paid or received
	 */
	private int changeAllPlayerCash(int amount, boolean receiving) {
		int total;
		
		amount *= (receiving) ? 1 : -1;
		total = 0;
		for (Player player : board.getPlayers()) {
			player.changeCash(amount);
			total += amount;
		}
		
		return Math.abs(total);
	}
	
	/*
	 * @param player
	 *        	the player advancing to the station
	 */
	private void advanceToStationAndPay(Player player) {
		int playerPosition, i;
		Space space;
		Property property;
		Player owner;
		
		playerPosition = player.getBoardPosition();
		i = playerPosition;
		property = null;
		while(true) {
			space = board.getSpaces()[i];
			if (space instanceof Property) {
				property = (Property)space;
				if (property.getType() == PropertyType.STATION) {
					break;
				}
			}
		}
		
		owner = property.getOwner();
		if (owner == null || owner.equals(player)) {
			return;
		} else if (!owner.equals(player)) {
			property.playerLands(player);
			property.playerLands(player);
		}
	}
	
	/*
	 * @param player
	 *        	the player paying or receiving based on the groups
	 *          of the property they own
	 * 
	 * @return the total change in the player's wealth
	 * 
	 * The player pays 25 bits for every APPLEJACK and RAINBOW_DASH
	 * property they own, but gain 50 bits for every TWILIGHT_SPARKLE
	 * property they own
	 */
	private int changeCashPerPropertyGroup(Player player) {
		PropertyGroup group;
		int total;
		total = 0;
		
		for (Property property : player.getProperties()) {
			group = property.getGroup();
			if (group == null) continue;
			
			if (group.equals(PropertyGroup.APPLEJACK)
		     || group.equals(PropertyGroup.RAINBOW_DASH)) {
				total -= 25;
			} else if (group.equals(PropertyGroup.TWILIGHT_SPARKLE)) {
				total += 50;
			}
		}
		
		player.changeCash(total);
		return total;
	}
	
	/*
	 * @param player
	 *        	the player receiving money
	 *        
	 * The player receives 25 bits default and an additional
	 * 50 bits for every APPLEJACK property they own
	 */
	private void receiveOnApplejackProperty(Player player) {
		int total;
		total = 25;
		
		for (Property property : player.getProperties()) {
			if (property.getGroup().equals(PropertyGroup.APPLEJACK)) {
				total += 50;
			}
		}
		
		player.changeCash(total);
	}
	
	public String getName() {
		return name;
	}
	
	public EventCardType getType() {
		return type;
	}
}
