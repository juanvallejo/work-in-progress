package com.rs.game.minigames;

import com.rs.game.player.Player;
import com.rs.game.Animation;
import com.rs.utils.Utils;

/**
 * Represents the chest on which the key is used.
 * 
 */

public class CrystalChest {
	
	private static final int[] CHEST_REWARDS = { 989, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1175, 1540, 1201, 493, 7952, 7952, 7952, 7952, 5327, 5327, 5327, 5327, 536, 536, 536, 1263, 1263, 1263, 1263, 1615, 1615, 1615, 1615, 6585, 6585, 1595, 1595, 1595, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1175, 1540, 1201, 493, 7952, 7952, 7952, 7952, 5327, 5327, 5327, 5327, 536, 536, 536, 1263, 1263, 1263, 1263, 1615, 1615, 1615, 1615, 6585, 6585, 1595, 1595, 1595, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1175, 1540, 1201, 493, 7952, 7952, 7952, 7952, 5327, 5327, 5327, 5327, 536, 536, 536, 1263, 1263, 1263, 1263, 1615, 1615, 1615, 1615, 6585, 6585, 1595, 1595, 1595, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1175, 1540, 1201, 493, 7952, 7952, 7952, 7952, 5327, 5327, 5327, 5327, 536, 536, 536, 1263, 1263, 1263, 1263, 1615, 1615, 1615, 1615, 6585, 6585, 1595, 1595, 1595, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1175, 1540, 1201, 493, 7952, 7952, 7952, 7952, 5327, 5327, 5327, 5327, 536, 536, 536, 1263, 1263, 1263, 1263, 1615, 1615, 1615, 1615, 6585, 6585, 1595, 1595, 1595, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1175, 1540, 1201, 493, 7952, 7952, 7952, 7952, 5327, 5327, 5327, 5327, 536, 536, 536, 1263, 1263, 1263, 1263, 1615, 1615, 1615, 1615, 6585, 6585, 1595, 1595, 1595, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1514, 13834, 13839, 13964, 16933, 763, 1171, 1175, 1540, 1201, 493, 7952, 7952, 7952, 7952, 5327, 5327, 5327, 5327, 536, 536, 536, 1263, 1263, 1263, 1263, 1615, 1615, 1615, 1615, 6585, 6585, 1595, 1595, 1595 };
	public static final int[] KEY_HALVES = { 985, 987 };
	public static final int KEY = 989;
	public static final int Animation = 881;
	
	/**
	 * Represents the key being made.
	 * Using tooth halves.
	 */
	public static void makeKey(Player p){
		if (p.getInventory().containsItem(toothHalf(), 1) 
			&& p.getInventory().containsItem(loopHalf(), 1)){
			p.getInventory().deleteItem(toothHalf(), 1);
			p.getInventory().deleteItem(loopHalf(), 1);
			p.getInventory().addItem(KEY, 1);
			p.getPackets().sendGameMessage("You succesfully make a crytal key.");
		}
	}
	
	/**
	 * If the player can open the chest.
	 */
	public static boolean canOpen(Player p){
		if(p.getInventory().containsItem(KEY, 1)){
			return true;
		}else{
			p.getPackets().sendGameMessage("This chest is locked you need a crystal key to open it.");
			return false;
		}
	}
	
	/**
	 * When the player searches the chest.
	 */
	public static void searchChest(final Player p){
		if (canOpen(p)){
			p.getPackets().sendGameMessage("You unlock the chest with your key.");
			p.setNextAnimation(new Animation(Animation));
			p.getInventory().addItem(995, Utils.random(300000));
			p.getInventory().deleteItem(989, 1);
			p.getInventory().addItem(CHEST_REWARDS[Utils.random(getLength() - 1)], 1);
			p.getPackets().sendGameMessage("You find some treasure in the chest.");
			p.chest++;
			p.resetWalkSteps();
		}
	}
	
	public static int getLength() {
		return CHEST_REWARDS.length;
	}
	
	/**
	 * Represents the toothHalf of the key.
	 */
	public static int toothHalf(){
		return KEY_HALVES[0];
	}
	
	/**
	 * Represent the loop half of the key.
	 */
	public static int loopHalf(){
		return KEY_HALVES[1];
	}
	
}