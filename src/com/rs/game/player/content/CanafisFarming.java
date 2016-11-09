package com.rs.game.player.content;

import com.rs.game.Animation;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class CanafisFarming {
	
	private static final int RAKE = 5341, WEEDS = 6055, DIBBER = 5343, SPADE = 952;
	
	private enum Seeds {

		  POTATO(5318, 4, "Potato", 6, new int[] { 8, 9 }, 1, 1942);

		  private int index;
		  private int stages;
		  private String crop;
		  private int config;
		  private int[] experience;
		  private int level;
		  private int item;

		  private Seeds(int index, int stages, String crop, int config, int[] experience, int level, int item) {
		   this.index = index;
		   this.stages = stages;
		   this.crop = crop;
		   this.config = config;
		   this.experience = experience;
		   this.level = level;
		   this.item = item;
		  }

		  public int index() {
		   return index;
		  }
		  public int stages() {
			   return stages;
		  }
		  public String crop() {
			   return crop;
		  }
		  public int config() {
			   return config;
		  }
		  public int[] experience() {
			   return experience;
		  }
		  public int level() {
			   return level;
		  }
		  public int item() {
			   return item;
		  }
		  
		  public void checks() {
			  switch (index) {
			  case 5318:
				  
				  break;
				  
			  }
		  }
		 
	}



	
	
	public static void useRakeCanA(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1) || player.rake == true) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.lock(6);
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 1);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 2);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
				} else if (loop == 6) {	
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 3);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
					player.out("You successfully clear all the weeds.");
					player.mustRakeAA = true;

				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}

}
