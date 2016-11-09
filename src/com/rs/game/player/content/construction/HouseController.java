package com.rs.game.player.content.construction;

import com.rs.game.RegionBuilder;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.content.construction.House;
import com.rs.game.player.content.construction.Room;
import com.rs.game.player.content.construction.RoomReference;
import com.rs.game.player.content.construction.PlayerHouseSaving;
import com.rs.game.player.controlers.Controler;

@SuppressWarnings("unused")
public class HouseController extends Controler {

	private House house;
	private int[] boundChuncks;

	@Override
	public void start() {
		/*
		 * player.setHouseController(this); house = new House(player); if
		 * (player.hasBeenToHouse()) { player.setNextWorldTile(new
		 * WorldTile(player.getHouseX(), player.getHouseY(), 0));
		 * player.getPackets().sendGameMessage("Welcome to your house!");
		 * player.getPackets().sendGameMessage(
		 * "Cons. is still buggy so, don't stress we're fixing it!!");
		 * house.constructHouse(player.getBoundChuncks(), player.isBuildMode());
		 * //player.getPlayerHouseSaving().loadPlayerHouse(player); } else if
		 * (!player.hasBeenToHouse()) { boundChuncks =
		 * RegionBuilder.findEmptyChunkBound(8, 8); for (int i = 0; i <
		 * boundChuncks.length; i++) { System.out.println(boundChuncks[i]);
		 * player.setBoundChuncks(boundChuncks);
		 * System.out.println("Setting bound chuncks");
		 * System.out.println(player.getBoundChuncks()[0]);
		 * System.out.println(player.getBoundChuncks()[1]); }
		 * house.constructHouse(boundChuncks, player.isBuildMode()); //
		 * player.setNextWorldTile(new WorldTile(boundChuncks[0]*8 + 35,
		 * boundChuncks[1]*8 + 35,0)); player.setNextWorldTile(new
		 * WorldTile(boundChuncks[0]*8 + 35, boundChuncks[1]*8 + 35,1));
		 * player.getPackets().sendGameMessage("Welcome to your house!");
		 * player.getPackets().sendGameMessage(
		 * "Cons. is still buggy so, don't stress we're fixing it!!");
		 * player.setHouseX(boundChuncks[0]*8 + 35);
		 * player.setHouseY(boundChuncks[1]*8 + 35);
		 * player.setBeenToHouse(true); player.setPlace(0);
		 * PlayerHouseSaving.init(player);
		 */
		// }
	}

	boolean remove = true;

	/**
	 * return process normaly
	 */

	public boolean processObjectClick5(WorldObject object) {
		player.getInterfaceManager().sendInterface(402);
		return true;
	}

}
