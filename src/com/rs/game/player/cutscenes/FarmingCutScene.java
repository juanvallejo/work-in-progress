package com.rs.game.player.cutscenes;

import java.util.ArrayList;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.cutscenes.actions.CutsceneAction;
import com.rs.game.player.cutscenes.actions.LookCameraAction;
import com.rs.game.player.cutscenes.actions.PosCameraAction;

public class FarmingCutScene extends Cutscene {

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
		player.setNextWorldTile(new WorldTile(3052, 3304, 0));
		actionsList.add(new LookCameraAction(51, 53, 1000 , 6, 6, 10));
		
		actionsList.add(new PosCameraAction(52, 49, 3000, 6, 6, 25));
		player.getPackets().sendConfigByFile(708, 0);
		player.changeAllotment(player);
		player.getDialogueManager().startDialogue("SimpleMessage", "This is one of the farming locations located around the world. To start farming you must have a rake in your inventory, simply click the farming allotment to start raking.");
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

}