package com.rs.game.minigames;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.player.Player;
import com.rs.utils.Utils;


public class Flower {
	
	private final static int[] FLOWERS = {2980, 2986, 2987, 2988, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2982, 2982, 2982, 2982, 2982, 2982, 2982, 2982,
	2983, 2983, 2983, 2983, 2983, 2983, 2983, 2983, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2982, 2982, 2982, 2982, 2982, 2982, 2982, 2982,
	2983, 2983, 2983, 2983, 2983, 2983, 2983, 2983, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2985  };
	
	public static void PlantFlower(Player player) {
		final WorldTile tile = new WorldTile(player);
		 WorldObject Flower4 = new WorldObject(FLOWERS[Utils.random(FLOWERS.length)], 10, 0, player);
		if (!World.canMoveNPC(player.getPlane(), player.getX(), player.getY(),
				1)
				|| World.getRegion(player.getRegionId()).getSpawnedObject(
						player) != null
						|| player.getControlerManager().getControler() instanceof DuelArena || player.getControlerManager().getControler() instanceof DuelControler) { // contains
			player.getPackets().sendGameMessage("You can't Plant a flower here.");
		} else {
	if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
		if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
			if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
				player.addWalkSteps(player.getX(), player.getY() - 1, 1);
	player.getInventory().deleteItem(299, 1);
	//World.spawnTemporaryObject(Flower4, 10, false);
	World.spawnTempGroundObject(Flower4, 0, 10000);
	player.getDialogueManager().startDialogue("FlowerGame");
	player.setNextAnimation(new Animation(827));
		}
	}

	public static void PickFlowers() {
		
	}

}