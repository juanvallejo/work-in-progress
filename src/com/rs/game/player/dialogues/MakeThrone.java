package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.player.Skills;

public class MakeThrone extends Dialogue {

	@Override
	public void start() {
			sendOptionsDialogue("What would you like to make?",
					"Oak Throne.", "Crystal Throne.", "Demonic Throne.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
		if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 60) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 60 to make this.");
		} else {
			player.throne = 1;
			player.getSkills().addXp(Skills.CONSTRUCTION, 20);
			player.setNextAnimation(new Animation(898));
			player.getPackets().sendGameMessage("You make a Oak Throne.");
		}
		} else if (componentId == OPTION_2) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 95) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 95 to make this.");
		} else {
			player.throne = 2;
			player.getSkills().addXp(Skills.CONSTRUCTION, 50);
			player.setNextAnimation(new Animation(898));
			player.getPackets().sendGameMessage("You make a Crystal Throne.");
		}
		} else if (componentId == OPTION_3) {
			if (player.getSkills().getLevel(Skills.CONSTRUCTION) < 99) {
				player.getPackets().sendGameMessage(
						"You need a construction level of at least 99 to make this.");
		} else {
			player.throne = 3;
			player.getSkills().addXp(Skills.CONSTRUCTION, 100);
			player.setNextAnimation(new Animation(898));
			player.getPackets().sendGameMessage("You make a Demonic Throne.");
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
