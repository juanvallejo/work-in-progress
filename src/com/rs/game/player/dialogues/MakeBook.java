package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.player.Skills;

public class MakeBook extends Dialogue {

	@Override
	public void start() {
			sendOptionsDialogue("What would you like to make?",
					"Wooden Bookcase.", "Oak Bookcase", "Mahogany Bookcase.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 4) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 4 to make this.");
			} else if (player.getInventory().containsItem(960, 2)) {
			player.bookcase = 1;
			player.getSkills().addXp(Skills.CONSTRUCTION, 400);
			player.setNextAnimation(new Animation(898));
			player.getInventory().deleteItem(960, 2);
			player.getPackets().sendGameMessage("You make a Wooden Bookcase.");
			} else {
				player.out("You need 2 planks to build this.");
			}
		} else if (componentId == OPTION_2) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 29) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 29 to make this.");
			} else if (player.getInventory().containsItem(8778, 4)) {
			player.bookcase = 2;
			player.getSkills().addXp(Skills.CONSTRUCTION, 6000);
			player.setNextAnimation(new Animation(898));
			player.getInventory().deleteItem(8778, 4);
			player.getPackets().sendGameMessage("You make a Oak Bookcase.");
			} else {
				player.out("You need 4 Oak planks to build this.");
			}
		} else if (componentId == OPTION_3) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 40) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 40 to make this.");
			} else if (player.getInventory().containsItem(8782, 4)) {
			player.bookcase = 3;
			player.getSkills().addXp(Skills.CONSTRUCTION, 10000);
			player.setNextAnimation(new Animation(898));
			player.getInventory().deleteItem(8782, 4);
			player.getPackets().sendGameMessage("You make a Mahogany Bookcase.");
			} else {
				player.out("You need 4 Mahogany planks to build this.");
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
