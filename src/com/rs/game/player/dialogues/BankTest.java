package com.rs.game.player.dialogues;

public class BankTest extends Dialogue {

	@Override
	public void start() {
		//if (!player.getPrayer().isAncientCurses())
			sendOptionsDialogue("Would you like to access your bank?",
					"Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			if (!player.getPrayer().isAncientCurses()) {
				sendDialogue(
						"You open your bank.");
				player.getBank().openBank();
			} else {
				sendDialogue(
						"You open your bank.");
				player.getBank().openBank();
			}
		} else
			end();
	}

	@Override
	public void finish() {

	}

}
