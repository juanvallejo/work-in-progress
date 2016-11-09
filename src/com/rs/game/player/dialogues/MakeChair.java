package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.player.Skills;

public class MakeChair extends Dialogue {

	@Override
	public void start() {
			sendOptionsDialogue("What would you like to make?",
					"Crude Wooden Chair.", "Rocking Chair", "Oak Armchair.", "Teak Armchair.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			if (player.getInventory().containsItem(960, 2)) {
			player.chair = 1;
			player.getSkills().addXp(Skills.CONSTRUCTION, 100);
			player.setNextAnimation(new Animation(898));
			player.getPackets().sendGameMessage("You make a crude wooden chair.");
			} else {
				player.out("You need 2 planks to make this.");
			}
		} else if (componentId == OPTION_2) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 14) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 14 to make this.");
			} else if (player.getInventory().containsItem(960, 2)) {
			player.chair = 2;
			player.getSkills().addXp(Skills.CONSTRUCTION, 1000);
			player.setNextAnimation(new Animation(898));
			player.getInventory().deleteItem(960, 3);
			player.getPackets().sendGameMessage("You make a rocking chair.");
			} else {
				player.out("You need 3 planks to make this.");
			}
		} else if (componentId == OPTION_3) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 26) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 26 to make this.");
			} else if (player.getInventory().containsItem(8778, 2)) {
			player.chair = 3;
			player.getSkills().addXp(Skills.CONSTRUCTION, 2000);
			player.setNextAnimation(new Animation(898));
			player.getInventory().deleteItem(8778, 2);
			player.getPackets().sendGameMessage("You make a Oak armchair.");
			} else {
				player.out("You need 2 oak planks to make this.");
			}
		} else if (componentId == OPTION_4) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 35) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 35 to make this.");
			} else if (player.getInventory().containsItem(8780, 2)) {
			player.chair = 4;
			player.getSkills().addXp(Skills.CONSTRUCTION, 4000);
			player.setNextAnimation(new Animation(898));
			player.getInventory().deleteItem(8780, 2);
			player.getPackets().sendGameMessage("You make a Teak armchair.");
			} else {
				player.out("You need 2 teak planks to make this.");
			}
		} else if (componentId == OPTION_5) {
			end();
		}
			end();
	}

	@Override
	public void finish() {

	}

}
