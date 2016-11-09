package com.rs.game.player.dialogues;

import com.rs.game.player.Skills;

public class MakeAltar extends Dialogue {

	@Override
	public void start() {
			sendOptionsDialogue("What would you like to do?",
					"Mahogany altar (Costs 2M).", "Marble altar (costs 10M).", "Gilded altar (Costs 20M).", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 60) {
				player.out("You need at least 60 Construction to build a Mahogany Altar.");
			} else if (player.getSkills().getLevel(Skills.CONSTRUCTION) >= 60 && player.getInventory().containsItem(995, 2000000)) {
				player.altar = 1;
				player.out("You build a Mahogany Altar.");
				player.getInventory().deleteItem(995, 2000000);
			} else {
				player.out("You need 2M coins in your inventory to build this.");
			}
		} else if (componentId == OPTION_2) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 70) {
				player.out("You need at least 70 Construction to build a Marble Altar.");
			} else if (player.getSkills().getLevel(Skills.CONSTRUCTION) >= 70 && player.getInventory().containsItem(995, 10000000)) {
						player.altar = 2;
						player.out("You build a Marble Altar.");
						player.getInventory().deleteItem(995, 10000000);
					} else {
						player.out("You need 10M coins in your inventory to build this.");
					}
		} else if (componentId == OPTION_3) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 75) {
				player.out("You need at least 75 Construction to build a Gilded Altar.");
			} else if (player.getSkills().getLevel(Skills.CONSTRUCTION) >= 75 && player.getInventory().containsItem(995, 20000000)) {
				player.altar = 3;
				player.out("You build a Gilded Altar.");
				player.getInventory().deleteItem(995, 20000000);
			} else {
				player.out("You need 20M coins in your inventory to build this.");
			}
		} else if (componentId == OPTION_4) {
			end();
		}
			end();
	}

	@Override
	public void finish() {

	}

}
