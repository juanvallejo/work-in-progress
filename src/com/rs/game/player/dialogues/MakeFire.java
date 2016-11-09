package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.player.Skills;

public class MakeFire extends Dialogue {

	@Override
	public void start() {
			sendOptionsDialogue("What would you like to make?",
					"Clay Fireplace.", "Limestone Fireplace.", "Marble Fireplace.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 3) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 3 to make this.");
			} else if (player.getInventory().containsItem(1761, 4)) {
			player.firePlace = 1;
			player.getSkills().addXp(Skills.CONSTRUCTION, 200);
			player.setNextAnimation(new Animation(898));
			player.getInventory().deleteItem(1761, 4);
			player.getPackets().sendGameMessage("You make a Clay Fireplace.");
			} else {
				player.out("You need 2 pieces of soft clay to build this.");
			}
		} else if (componentId == OPTION_2) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 33) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 33 to make this.");
			} else if (player.getInventory().containsItem(3420, 2)) {
			player.firePlace = 2;
			player.getSkills().addXp(Skills.CONSTRUCTION, 8000);
			player.setNextAnimation(new Animation(898));
			player.getInventory().deleteItem(3420, 2);
			player.getPackets().sendGameMessage("You make a Limestone Fireplace.");
			} else {
				player.out("You need 2 Limestone bricks to build this.");
			}
		} else if (componentId == OPTION_3) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 63) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 63 to make this.");
			} else if (player.getInventory().containsItem(8786, 1)) {
			player.firePlace = 3;
			player.getSkills().addXp(Skills.CONSTRUCTION, 15000);
			player.setNextAnimation(new Animation(898));
			player.getInventory().deleteItem(8786, 1);
			player.getPackets().sendGameMessage("You make a Marble Fireplace.");
			} else {
				player.out("You need 1 Marble block to build this.");
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
