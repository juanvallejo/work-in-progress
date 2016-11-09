package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.QuestManager;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.PlayerLook;
import com.rs.utils.ShopsHandler;


public class FarmingTeleport extends Dialogue {

	int npcId = 3021;

	@Override
	public void start() {
		sendNPCDialogue(npcId, 9827, "Hello there, Which Farming Location would you like to visit?");
		}

	@Override
	public void run(int interfaceId, int componentId) {
		switch(stage) {
		case -1:
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Falador.", "Catherby", "Adrougne (In progress)", "Falador Tree Farm", "None of them.");
			break;
		case 0:
			if(componentId == OPTION_5) {
				stage = 1;
				sendPlayerDialogue(9827, "No thank you.");
				end();
			} else if(componentId == OPTION_1) {
				if (player.firstScene == false) {
					player.getCutscenesManager().play("FarmingCutScene");
				} else if (player.firstScene == true) {
				stage = 2;
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3052, 3304, 0));
				}
			} else if(componentId == OPTION_2) {
				if (player.firstScene == false) {
					player.getCutscenesManager().play("FarmingCutScene");
				} else if (player.firstScene == true) {
				stage = 2;
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2806, 3463, 0));
				}
			} else if(componentId == OPTION_3) {
			/*	if (player.firstScene == false) {
					player.getCutscenesManager().play("FarmingCutScene");
				} else if (player.firstScene == true) {
				stage = 2;
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2664, 3374, 0));*/
				//}
				player.out("Currently In Progress.");
			} else if(componentId == OPTION_4) {
				if (player.firstScene == false) {
					player.getCutscenesManager().play("FarmingCutScene");
				} else if (player.firstScene == true) {
				stage = 2;
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3005, 3377, 0));
				}
			}
			break;
		}
	}

	@Override
	public void finish() {

	}

}
