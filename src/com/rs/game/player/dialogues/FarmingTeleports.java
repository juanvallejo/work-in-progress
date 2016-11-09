package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.QuestManager;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.PlayerLook;
import com.rs.utils.ShopsHandler;


public class FarmingTeleports extends Dialogue {

	int npcId = 7557;

	@Override
	public void start() {
		sendNPCDialogue(npcId, 9827, "Hello there, I can teleport you to the catherby farming location, Would you like to go?");
		}

	@Override
	public void run(int interfaceId, int componentId) {
		switch(stage) {
		case -1:
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes Please.", "No thank you.");
			break;
		case 0:
			if(componentId == OPTION_2) {
				stage = 1;
				sendPlayerDialogue(9827, "No thank you.");
				end();
			}else {
				stage = 2;
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2816, 3465, 0));
			}
			break;
		}
	}

	@Override
	public void finish() {

	}

}
