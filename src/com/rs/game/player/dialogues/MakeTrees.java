package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.player.Skills;

public class MakeTrees extends Dialogue {

	@Override
	public void start() {
			sendOptionsDialogue("What would you like to make?",
					"Oak Tree.", "Willow Tree.", "Yew Tree.", "Magic Tree", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
		if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 15) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 15 to make this.");
		} else {
			player.tree = 1;
			player.getSkills().addXp(Skills.CONSTRUCTION, 100);
			player.setNextAnimation(new Animation(2272));
			player.getPackets().sendGameMessage("You plant a Oak Tree.");
		}
		} else if (componentId == OPTION_2) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 30) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 30 to make this.");
		} else {
			player.tree = 2;
			player.getSkills().addXp(Skills.CONSTRUCTION, 1000);
			player.setNextAnimation(new Animation(2272));
			player.getPackets().sendGameMessage("You plant a Willow Tree.");
		}
		} else if (componentId == OPTION_3) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 60) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 60 to make this.");
		} else {
			player.tree = 3;
			player.getSkills().addXp(Skills.CONSTRUCTION, 1500);
			player.setNextAnimation(new Animation(2272));
			player.getPackets().sendGameMessage("You plant a Yew Tree.");
		}
		} else if (componentId == OPTION_4) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 75) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 75 to make this.");
		} else {
			player.tree = 4;
			player.getSkills().addXp(Skills.CONSTRUCTION, 1800);
			player.setNextAnimation(new Animation(2272));
			player.getPackets().sendGameMessage("You plant a Magic Tree.");
			}
		}
			end();
	}

	@Override
	public void finish() {

	}

}
