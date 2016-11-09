package com.rs.game.player;

import java.util.Timer;

import com.rs.game.Animation;
import com.rs.game.player.content.Burying;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class ArdyFarming {

	private static final int RAKE = 5341, WEEDS = 6055;
	
	
	public static void useRakeAA(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1) || player.rake == true) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
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
					player.unlock();
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	
	public static void useRakeAB(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1) || player.rake == true) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
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
					player.mustRakeAB = true;
					player.unlock();
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	public static void useRakeAH(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1) || player.rake == true) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
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
					player.mustRakeAH = true;
					player.unlock();
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	public static void useRakeAF(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1) || player.rake == true) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
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
					player.mustRakeAF = true;
					player.unlock();
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	
	
	
	public static void plantPotatoAA(final Player player, final int configId) {
		if (player.getInventory().containsItem(5318, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2292));
					player.getPackets().sendConfigByFile(configId, 6);
					player.getInventory().deleteItem(5318, 1);
				} else if (loop == 15) {
					player.getPackets().sendConfigByFile(configId, 7);
				} else if (loop == 30) {	
					player.getPackets().sendConfigByFile(configId, 10);
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Ardougne, allotment A.</col>");
					player.canHarvestAA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some potato seeds.");
			}
	}
	
	public static void plantPotatoAB(final Player player, final int configId) {
		if (player.getInventory().containsItem(5318, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2292));
					player.getPackets().sendConfigByFile(configId, 6);
					player.getInventory().deleteItem(5318, 1);
				} else if (loop == 150) {
					player.getPackets().sendConfigByFile(configId, 7);
				} else if (loop == 300) {	
					player.getPackets().sendConfigByFile(configId, 10);
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Ardougne, allotment B.</col>");
					player.canHarvestAB = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some potato seeds.");
			}
	}
	
	public static void plantMelonAA(final Player player, final int configId) {
		if (player.getInventory().containsItem(5321, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2292));
					player.getPackets().sendConfigByFile(configId, 52);
					player.getInventory().deleteItem(5321, 1);
				} else if (loop == 15) {
					player.getPackets().sendConfigByFile(configId, 56);
				} else if (loop == 30) {	
					player.getPackets().sendConfigByFile(configId, 60);
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Ardougne, allotment A.</col>");
					player.canHarvestAA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some melon seeds.");
			}
	}
	
	public static void plantMelonAB(final Player player, final int configId) {
		if (player.getInventory().containsItem(5321, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2292));
					player.getPackets().sendConfigByFile(configId, 52);
					player.getInventory().deleteItem(5321, 1);
				} else if (loop == 150) {
					player.getPackets().sendConfigByFile(configId, 56);
				} else if (loop == 300) {	
					player.getPackets().sendConfigByFile(configId, 60);
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Ardougne, allotment B.</col>");
					player.canHarvestCB = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some melon seeds.");
			}
	}
	
	public static void plantSweetAA(final Player player, final int configId) {
		if (player.getInventory().containsItem(5320, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 34);
					player.getInventory().deleteItem(5320, 1);
				} else if (loop == 15) {
					player.getPackets().sendConfigByFile(configId, 37);
				} else if (loop == 30) {	
					player.getPackets().sendConfigByFile(configId, 40);
					player.out("<col=FF0000>[Notice] Your Sweetcorn has fully grown at Ardougne, Allotment A.</col>");
					player.canHarvestAA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a Sweetcorn Seed.");
			}
	}
	
	public static void plantSweetAB(final Player player, final int configId) {
		if (player.getInventory().containsItem(5320, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 34);
					player.getInventory().deleteItem(5320, 1);
				} else if (loop == 150) {
					player.getPackets().sendConfigByFile(configId, 37);
				} else if (loop == 300) {	
					player.getPackets().sendConfigByFile(configId, 40);
					player.out("<col=FF0000>[Notice] Your Sweetcorn has fully grown at Ardougne, Allotment B.</col>");
					player.canHarvestAB = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a Sweetcorn Seed.");
			}
	}
	
	public static void plantGuamA(final Player player, final int configId) {
		if (player.getInventory().containsItem(5291, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 4);
					player.getInventory().deleteItem(5291, 1);
				} else if (loop == 150) {
					player.getPackets().sendConfigByFile(configId, 6);
				} else if (loop == 300) {	
					player.getPackets().sendConfigByFile(configId, 8);
					player.out("<col=FF0000>[Notice] Your herbs are fully grown at Ardougne.</col>");
					player.canHarvestHerbAA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some guam seeds.");
			}
	}
	
	
	public static void plantSnapA(final Player player, final int configId) {
		if (player.getInventory().containsItem(5300, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 4);
					player.getInventory().deleteItem(5300, 1);
				} else if (loop == 150) {
					player.getPackets().sendConfigByFile(configId, 6);
				} else if (loop == 300) {	
					player.getPackets().sendConfigByFile(configId, 8);
					player.out("<col=FF0000>[Notice] Your herbs are fully grown at Ardougne.</col>");
					player.canHarvestHerbAA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some snapdragon seeds.");
			}
	}
	
	public static void plantTorstolA(final Player player, final int configId) {
		if (player.getInventory().containsItem(5304, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 4);
					player.getInventory().deleteItem(5304, 1);
				} else if (loop == 150) {
					player.getPackets().sendConfigByFile(configId, 6);
				} else if (loop == 300) {	
					player.getPackets().sendConfigByFile(configId, 8);
					player.out("<col=FF0000>[Notice] Your herbs are fully grown at Ardougne.</col>");
					player.canHarvestHerbAA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some Torstol seeds.");
			}
	}
	
	public static void plantGoldAA(final Player player, final int configId) {
		if (player.getInventory().containsItem(5096, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 8);
					player.getInventory().deleteItem(5096, 1);
				} else if (loop == 150) {
					player.getPackets().sendConfigByFile(configId, 10);
				} else if (loop == 300) {	
					player.getPackets().sendConfigByFile(configId, 12);
					player.out("<col=FF0000>[Notice] Your Marigolds are fully grown at Ardougne.</col>");
					player.canHarvestFlowerAF = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some Marigold seeds.");
			}
	}
	
	public static void plantLilyAA(final Player player, final int configId) {
		if (player.getInventory().containsItem(14589, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 37);
					player.getInventory().deleteItem(14589, 1);
				} else if (loop == 150) {
					player.getPackets().sendConfigByFile(configId, 39);
				} else if (loop == 300) {	
					player.getPackets().sendConfigByFile(configId, 41);
					player.out("<col=FF0000>[Notice] Your White Lily's have fully grown at Ardougne.</col>");
					player.canHarvestFlowerAF = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some White Lily seeds.");
			}
	}
}
