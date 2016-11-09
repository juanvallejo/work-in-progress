package com.rs.game.player.dialogues;

public class HouseTeleport extends Dialogue {

	@Override
	public void start() {
			sendOptionsDialogue("What would you like to do?",
					"Go to your house.", "Go to your house (Building mode).", "Go to a friend's house.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			if (player.hasHouse == true) {
			player.getInterfaceManager().sendInterface(399);
			player.getControlerManager().startControler("HouseControler");
		} else {
			player.getDialogueManager().startDialogue("SimpleMessage", "You must purchase a house in order to do this, You can buy a property at the Camelot Estate Agents.");
		}
		} else if (componentId == OPTION_2) {
			if (player.hasHouse == true) {
			player.getInterfaceManager().sendInterface(399);
			player.getControlerManager().startControler("HouseCon");
			player.sm("In order to build an altar you must click on the portal.");
		} else {
			player.getDialogueManager().startDialogue("SimpleMessage", "You must purchase a house in order to do this, You can buy a property at the Camelot Estate Agents.");
		}
		} else if (componentId == OPTION_3) {
            player.getTemporaryAttributtes().put("teleto_house", true);
            player.getPackets().sendRunScript(109, "Enter Friends Name:");
		} else if (componentId == OPTION_4) {
			end();
		}
			//end();
	}

	@Override
	public void finish() {

	}

}
