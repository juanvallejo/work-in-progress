package com.rs.game.player.content;

import com.rs.cores.CoresManager;
import com.rs.game.World;
import com.rs.game.player.Player;

import java.util.Random;
import java.util.TimerTask;
/**
 * @Author: Apache Ah64, Thanks Apache :)
 */
public class TriviaBot {
	
	private static String questions [][] = {
		{"How many altars are there at ::altars?", "3"},
		{"What colour is the summoning obelisk at ::home?", "blue"},
		{"In what year was RuneScape established?", "2001"},
		{"How many items are in the vote shop?", "9"},
		{"How many portals are there at clan wars?", "3"},
		{"Is a tomato a fruit or a vegetable?", "apple"},
		{"How many barrows brothers are on each hill?", "5"},
		{"What is north of ::home?", "wilderness"},
		{"How many cooking ranges are there at ::home?", "1"},
		{"In what year was the Duplication Glitch in RuneScape?", "2003"},
		{"How many Bankers are there at ::home", "4"},
		{"What is the first ancient spell?", "smoke rush"},
		{"What is the most powerful curse?", "turmoil"},
		{"How much of a percentage does a dragon dagger special requires?", "25"},
		{"What color is the donator sign?", "Green"},
		{"What's the hardest level to get max level in?", "dungeoneering"},
		{"What is the best free to play armour?", "Rune"},
		{"Where do you get Zeals at?", "Soul wars"},
		{"Figure out this anagram: 'El Strut'", "turtles"},
		{"What is the name of the new firecape?", "TokHaar-Kal"}
	};
	
	public static int questionid = -1;
	public static int round = 0;
	public static boolean victory = false;

	public TriviaBot() {
		//TODO
	}
	
	public static void Run() {
		int randomNumber = RandomQuestion();
		questionid = randomNumber;
		victory = false;
		for(Player participant : World.getPlayers()) {
			
			if(participant != null) {
				participant.getPackets().sendGameMessage("<col=56A5EC>Here's some trivia for you! " + questions[randomNumber][0] + "</col>");
			}
				
		}
	}
	
	public static void sendRoundWinner(String winner, Player player) {
		for(Player participant : World.getPlayers()) {
			if(participant == null)
				continue;
				victory = true;
				player.TriviaPoints += 1;
				participant.getPackets().sendGameMessage("<col=56A5EC>[Trivia] "+winner+" gave the correct answer and is now on "+player.TriviaPoints+" Trivia Points.</col>");
		}
	}
	
	public static void verifyAnswer(final Player player, String answer) {
		if(victory) {
			player.getPackets().sendGameMessage("That round has already been won, wait for the next round.");
		} else if(questions[questionid][1].equalsIgnoreCase(answer)) {
			round++;
			sendRoundWinner(player.getDisplayName(), player);
		} else {
			player.getPackets().sendGameMessage("That answer wasn't correct, please try it again.");
		}
	}
	
	public static int RandomQuestion() {
		int random = 0;
		Random rand = new Random();
		random = rand.nextInt(questions.length);
		return random;
	}
	
	public static boolean TriviaArea(final Player participant) {
		if(participant.getX() >= 2630 && participant.getX() <= 2660 && participant.getY() >= 9377 && participant.getY() <= 9400) {
			return true;
		}
		return false;
	}
}
