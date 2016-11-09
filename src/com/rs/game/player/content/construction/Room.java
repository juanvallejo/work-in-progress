package com.rs.game.player.content.construction;

import com.rs.game.RegionBuilder;
//import com.rs.net.decoders.handlers.RoomReference;

public enum Room {
	// Tele to 1884 5106, do coords, get rx and ry
	GARDEN(232, 633, "Garden", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), PARLOUR(232, 639,
			"Parlour", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), BEDROOM(238, 639,
			"Bedroom", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), DININGROOM(236, 639,
			"Dining room", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), WorkShop(232, 637,
			"Workshop", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), Chapel(234, 637,
			"Chapel", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), QUESTHALL1(237, 638,
			"Quest hall 1", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), QUESTHALL2(239, 638,
			"Quest hall 2", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), SKILLHALL1(233, 638,
			"Skill hall 1", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), SKILLHALL2(235, 638,
			"Skill hall 2", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), PORTALROOM(233, 636,
			"Portal room", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), KITCHEN(234, 639,
			"Kitchen", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), THRONEROOM(238, 637,
			"Throne room", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), GAMESROOM(237, 636,
			"Games room", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), STUDY(236, 637, "Study",
			false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), BOXINGROOM(235, 636,
			"Combat room", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), FANCYGARDEN(234, 633,
			"Formal garden", false, RegionBuilder.NORTH, RegionBuilder.EAST,
			RegionBuilder.SOUTH, RegionBuilder.WEST), COSTUMEROOM(238, 633,
			"Costume room", false), OUBLIETTE(238, 635, "Oubliette", false,
			RegionBuilder.NORTH, RegionBuilder.EAST, RegionBuilder.SOUTH,
			RegionBuilder.WEST), DUNGEONPIT(237, 634, "Dungeon - Pit", true,
			RegionBuilder.NORTH, RegionBuilder.EAST, RegionBuilder.SOUTH,
			RegionBuilder.WEST), DUNGEONCORRIDOR(236, 635,
			"Dungeon - Corridor", true, RegionBuilder.NORTH,
			RegionBuilder.EAST, RegionBuilder.SOUTH, RegionBuilder.WEST), DUNGEONJUNCTION(
			240, 635, "Dungeon - Junction", true, RegionBuilder.NORTH,
			RegionBuilder.EAST, RegionBuilder.SOUTH, RegionBuilder.WEST), DUNGEONSTAIRS(
			242, 635, "Dungeon - Stairs", true, RegionBuilder.NORTH,
			RegionBuilder.EAST, RegionBuilder.SOUTH, RegionBuilder.WEST), TREASUREROOM(
			239, 636, "Treasure room", true, RegionBuilder.NORTH,
			RegionBuilder.EAST, RegionBuilder.SOUTH, RegionBuilder.WEST), MENAGERIE(
			247, 634, "Menagerie", false, RegionBuilder.NORTH,
			RegionBuilder.EAST, RegionBuilder.SOUTH, RegionBuilder.WEST);

	private int chunkX, chunkY;
	private String name;
	private boolean showRoof;
	@SuppressWarnings("unused")
	private int[] doorDirs;

	private Room(int chunkX, int chunkY, String name, boolean showRoof,
			int... doorDirs) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.name = name;
		this.showRoof = showRoof;
		this.doorDirs = doorDirs;
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkY() {
		return chunkY;
	}

	public String getName() {
		return name;
	}

	public boolean isShowRoof() {
		return showRoof;
	}

	public void add(RoomReference roomReference) {
		// TODO Auto-generated method stub
		
	}

}
