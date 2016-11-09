package com.rs.game.player.controlers;

import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.construction.House;
import com.rs.game.player.content.construction.Furniture.FurnitureObjects;
import com.rs.game.player.content.construction.House.Room;
import com.rs.game.player.content.construction.House.RoomReference;
import com.rs.game.player.content.construction.PlayerHouseSaving;
import com.rs.game.player.content.construction.loadHouse;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class HouseControler extends Controler {
	
	/**
	 * This controller loads the preview mode.
	 */
	
	private House house;
	public int[] boundChuncks;
	
	@Override
	public void start() {
		house = new House();
		boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8); 
		house.constructHouse(boundChuncks, false, player);
		player.setNextWorldTile(new WorldTile(boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 34, 0));
		World.spawnObject(new WorldObject(13405, 10, 0, boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 35, 0), true);
		if (player.hasBoughtDemon) {
		World.spawnNPC(4243, new WorldTile(boundChuncks[0] * 8 + 43, boundChuncks[1] * 8 + 35, 0), -1, true, true);
		}
		player.isOwner = true;
		player.hasLocked = false;
		player.inBuildMode = false;
		player.checkObjects(boundChuncks);
		player.refreshObjects(boundChuncks);
		
	}
	
	boolean remove = true;
	/**
	 * return process normally
	 */
	@Override
	public boolean processObjectClick5(WorldObject object) {
		house.previewRoom(player, boundChuncks, new RoomReference(Room.PARLOUR, 4, 5, 0, 0), remove = !remove);
		return true;
	}

}
