package com.rs.game.player.dialogues;

import java.util.ArrayList;

import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.minigames.Flower;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class FlowerGame extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an Option",
				"Pick the flowers.", "Leave the flowers.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1 && componentId == OPTION_1) {
				Flower.PickFlowers();
				end();
				return;
		} else {
			end();
		}
	}

	@Override
	public void finish() {

	}
}