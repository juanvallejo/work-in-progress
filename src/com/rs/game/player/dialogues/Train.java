package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.WorldTile;
import com.rs.game.player.Skills;
import com.rs.game.player.content.Magic;
import com.rs.utils.ShopsHandler;

public class Train extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an option.",
				"Rock Crabs.", "Yaks.", "Ape's (Ape Atoll)", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2707, 3715, 0));
		end();
		} else if (componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2322, 3804, 0));
		end();
		} else if (componentId == OPTION_3) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2793, 2786, 0));
		end();
		} else if (componentId == OPTION_4) {
			end();
		}
		end();
	}

	

	@Override
	public void finish() {

	}

}
