package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.player.Skills;

public class MakePortal extends Dialogue {

	@Override
	public void start() {
			sendOptionsDialogue("What would you like to make?",
					"Teak Portal Frame.", "Mahogany Portal Frame", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 50) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 50 to make this.");
			} else if (player.getInventory().containsItem(8780, 4)) {
			player.portalFrame = 1;
			player.getSkills().addXp(Skills.CONSTRUCTION, 10000);
			player.setNextAnimation(new Animation(898));
			player.getInventory().deleteItem(8780, 4);
			player.getPackets().sendGameMessage("You make a Teak Portal Frame.");
			} else {
				player.out("You need 4 Teak planks to build this.");
			}
		} else if (componentId == OPTION_2) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 65) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 65 to make this.");
			} else if (player.getInventory().containsItem(8782, 4)) {
			player.portalFrame = 2;
			player.getSkills().addXp(Skills.CONSTRUCTION, 15000);
			player.setNextAnimation(new Animation(898));
			player.getInventory().deleteItem(8782, 4);
			player.getPackets().sendGameMessage("You make a Mahogany Portal Frame.");
			} else {
				player.out("You need 4 Mahogany planks to build this.");
			}
		} else if (componentId == OPTION_3) {
			end();
		}
		end();
	}

	@Override
	public void finish() {

	}

}
