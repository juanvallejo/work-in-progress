package com.rs.game.player;

import com.rs.game.Animation;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class CathFarming {

	private static final int RAKE = 5341, WEEDS = 6055;
	
	public static void useRakeCA(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1)) {
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
					player.mustRakeCA = true;
					player.unlock();
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	
	public static void useRakeCB(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1)) {
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
					player.mustRakeCB = true;
					player.unlock();
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	public static void useRakeCH(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1)) {
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
					player.mustRakeCH = true;
					player.unlock();
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	public static void useRakeCF(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1)) {
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
					player.mustRakeCF = true;
					player.unlock();
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	
	
	public static void plantPotatoCA(final Player player, final int configId) {
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
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Catherby, allotment A.</col>");
					player.canHarvestCA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some potato seeds.");
			}
	}
	
	public static void plantPotatoCB(final Player player, final int configId) {
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
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Catherby, allotment B.</col>");
					player.canHarvestCB = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some potato seeds.");
			}
	}
	
	public static void plantMelonCA(final Player player, final int configId) {
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
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Catherby, allotment A.</col>");
					player.canHarvestCA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some melon seeds.");
			}
	}
	
	public static void plantMelonCB(final Player player, final int configId) {
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
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Catherby, allotment B.</col>");
					player.canHarvestCB = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some melon seeds.");
			}
	}
	
	public static void plantSweetCA(final Player player, final int configId) {
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
					player.out("<col=FF0000>[Notice] Your Sweetcorn has fully grown at Catherby, Allotment A.</col>");
					player.canHarvestCA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a Sweetcorn Seed.");
			}
	}
	
	public static void plantSweetCB(final Player player, final int configId) {
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
					player.out("<col=FF0000>[Notice] Your Sweetcorn has fully grown at Catherby, Allotment B.</col>");
					player.canHarvestCB = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a Sweetcorn Seed.");
			}
	}
	
	public static void plantGuamC(final Player player, final int configId) {
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
					player.out("<col=FF0000>[Notice] Your herbs are fully grown at Catherby.</col>");
					player.canHarvestHerbCA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some guam seeds.");
			}
	}
	
	
	public static void plantSnapC(final Player player, final int configId) {
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
					player.out("<col=FF0000>[Notice] Your herbs are fully grown at Catherby.</col>");
					player.canHarvestHerbCA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some snapdragon seeds.");
			}
	}
	
	public static void plantTorstolC(final Player player, final int configId) {
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
					player.out("<col=FF0000>[Notice] Your herbs are fully grown at Catherby.</col>");
					player.canHarvestHerbCA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some Torstol seeds.");
			}
	}
	
	public static void plantGoldC(final Player player, final int configId) {
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
					player.out("<col=FF0000>[Notice] Your Marigolds are fully grown at Catherby.</col>");
					player.canHarvestFlowerCA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some Marigold seeds.");
			}
	}
	
	public static void plantLilyC(final Player player, final int configId) {
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
					player.out("<col=FF0000>[Notice] Your White Lily's have fully grown at Catherby.</col>");
					player.canHarvestFlowerCA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some White Lily seeds.");
			}
	}
}
