package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.player.Skills;
import com.rs.utils.ShopsHandler;

public class StatLog extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an option.",
				"In-Game Highscores.", "Drop Log.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
            player.getTemporaryAttributtes().put("highscores", true);
            player.getPackets().sendRunScript(109, "Enter Player's Name:");
		end();
		} else if (componentId == OPTION_2) {
            player.getTemporaryAttributtes().put("drop_log", true);
            player.getPackets().sendRunScript(109, "Enter Player's Name:");
		end();
		} else if (componentId == OPTION_3) {
		end();
		}
		end();
	}

	

	@Override
	public void finish() {

	}

}
