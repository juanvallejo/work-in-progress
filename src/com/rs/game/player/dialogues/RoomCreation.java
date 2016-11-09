package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.player.Skills;
import com.rs.game.player.content.construction.House;
import com.rs.game.player.content.construction.RoomReference;
import com.rs.game.player.content.construction.House.Room;
import com.rs.utils.ShopsHandler;

public class RoomCreation extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an option.",
				"Parlour.", "Kitchen", "Dining Room.", "Bedroom.", "Never Mind");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			player.conRoom = 1;
		} else if (componentId == OPTION_2) {
			player.conRoom = 2;
		} else if (componentId == OPTION_3) {
			player.conRoom = 3;
		} else if (componentId == OPTION_4) {
			player.conRoom = 4;
		} else if (componentId == OPTION_5) {
			end();
		}
		end();
	}

	

	@Override
	public void finish() {

	}

}
