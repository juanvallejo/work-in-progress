package com.rs.game.player.content.construction;

import com.rs.game.player.Player;

@SuppressWarnings("unused")
public class RoomConstruction {

	private Player p;
	
	public RoomConstruction(Player player) {
		this.p = player;
	}
	
	public void buildRoom(Room r, int componentId) {
		if (p.conRoom == 1) {
			p.getRooms().add(new RoomReference(r, p.getRoomX() - 1, p.getRoomY(), p.getPlane(), getRoomRotationToBuild()));
		    p.getHouse().constructHouse(p.getBoundChuncks(), true, p);
		    p.getHouse().refresh(p, p.getBoundChuncks());
		    p.out("Bulding.");
		}
	}
	
	public int getRoomRotationToBuild() {
		switch (p.getLocalX()) {
		case 55:
			return 1;
		case 48:
			return 3;
		}
		switch (p.getLocalY()) {
		case 55:
			return 0;
		case 48:
			return 2;
		}
		return 0;
	}

	/*
	 * private Player p;
	 * 
	 * public RoomConstruction(Player player) { this.p = player; }
	 * 
	 * public void addRoom(Player player, int componentId) { if (componentId <
	 * 93 || componentId > 115) {
	 * player.getPackets().sendGameMessage("This room is currently disabled.");
	 * } switch (componentId) { case 93:
	 * System.out.println(player.getDirection()); if (isBuildingLeft()) {
	 * player.getRooms().add(new RoomReference(Room.PARLOUR, player.getRoomX() -
	 * 1, player.getRoomY(), player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.PARLOUR, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new RoomReference(Room.PARLOUR,
	 * player.getRoomX(), player.getRoomY() + 1, player.getPlane(),
	 * getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.PARLOUR, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 94: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.GARDEN, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.GARDEN, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new RoomReference(Room.GARDEN,
	 * player.getRoomX(), player.getRoomY() + 1, player.getPlane(),
	 * getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new RoomReference(Room.GARDEN,
	 * player.getRoomX(), player.getRoomY() - 1, player.getPlane(),
	 * getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 95: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.KITCHEN, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.KITCHEN, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new RoomReference(Room.KITCHEN,
	 * player.getRoomX(), player.getRoomY() + 1, player.getPlane(),
	 * getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.KITCHEN, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 96: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.DININGROOM, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.DININGROOM, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.DININGROOM, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.DININGROOM, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 97: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.WorkShop, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.WorkShop, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new RoomReference(Room.WorkShop,
	 * player.getRoomX(), player.getRoomY() + 1, player.getPlane(),
	 * getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.WorkShop, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 98: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.BEDROOM, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.BEDROOM, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new RoomReference(Room.BEDROOM,
	 * player.getRoomX(), player.getRoomY() + 1, player.getPlane(),
	 * getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.BEDROOM, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 99: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.SKILLHALL1, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.SKILLHALL1, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.SKILLHALL1, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.SKILLHALL1, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 100: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.GAMESROOM, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.GAMESROOM, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.GAMESROOM, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.GAMESROOM, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 101: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.BOXINGROOM, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.BOXINGROOM, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.BOXINGROOM, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.BOXINGROOM, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 102: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.QUESTHALL1, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.QUESTHALL1, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.QUESTHALL1, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.QUESTHALL1, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 103: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.MENAGERIE, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.MENAGERIE, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.MENAGERIE, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.MENAGERIE, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 104: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.STUDY, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new RoomReference(Room.STUDY,
	 * player.getRoomX() + 1, player.getRoomY(), player.getPlane(),
	 * getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new RoomReference(Room.STUDY,
	 * player.getRoomX(), player.getRoomY() + 1, player.getPlane(),
	 * getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new RoomReference(Room.STUDY,
	 * player.getRoomX(), player.getRoomY() - 1, player.getPlane(),
	 * getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 105: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.COSTUMEROOM, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.COSTUMEROOM, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.COSTUMEROOM, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.COSTUMEROOM, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 106: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.Chapel, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.Chapel, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new RoomReference(Room.Chapel,
	 * player.getRoomX(), player.getRoomY() + 1, player.getPlane(),
	 * getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new RoomReference(Room.Chapel,
	 * player.getRoomX(), player.getRoomY() - 1, player.getPlane(),
	 * getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 107: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.PORTALROOM, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.PORTALROOM, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.PORTALROOM, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.PORTALROOM, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 108: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.FANCYGARDEN, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.FANCYGARDEN, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.FANCYGARDEN, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.FANCYGARDEN, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 109: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.THRONEROOM, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.THRONEROOM, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.THRONEROOM, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.THRONEROOM, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 110: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.OUBLIETTE, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.OUBLIETTE, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.OUBLIETTE, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.OUBLIETTE, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 111: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONCORRIDOR, player.getRoomX() - 1,
	 * player.getRoomY(), player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONCORRIDOR, player.getRoomX() + 1,
	 * player.getRoomY(), player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONCORRIDOR, player.getRoomX(), player.getRoomY()
	 * + 1, player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONCORRIDOR, player.getRoomX(), player.getRoomY()
	 * - 1, player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 112: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONJUNCTION, player.getRoomX() - 1,
	 * player.getRoomY(), player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONJUNCTION, player.getRoomX() + 1,
	 * player.getRoomY(), player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONJUNCTION, player.getRoomX(), player.getRoomY()
	 * + 1, player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONJUNCTION, player.getRoomX(), player.getRoomY()
	 * - 1, player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 113: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONSTAIRS, player.getRoomX() - 1,
	 * player.getRoomY(), player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONSTAIRS, player.getRoomX() + 1,
	 * player.getRoomY(), player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONSTAIRS, player.getRoomX(), player.getRoomY() +
	 * 1, player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONSTAIRS, player.getRoomX(), player.getRoomY() -
	 * 1, player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 114: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONPIT, player.getRoomX() - 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONPIT, player.getRoomX() + 1, player.getRoomY(),
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONPIT, player.getRoomX(), player.getRoomY() + 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.DUNGEONPIT, player.getRoomX(), player.getRoomY() - 1,
	 * player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * case 115: if (isBuildingLeft()) { player.getRooms().add(new
	 * RoomReference(Room.TREASUREROOM, player.getRoomX() - 1,
	 * player.getRoomY(), player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingRight()) { player.getRooms().add(new
	 * RoomReference(Room.TREASUREROOM, player.getRoomX() + 1,
	 * player.getRoomY(), player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingUp()) { player.getRooms().add(new
	 * RoomReference(Room.TREASUREROOM, player.getRoomX(), player.getRoomY() +
	 * 1, player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } else if
	 * (isBuildingDown()) { player.getRooms().add(new
	 * RoomReference(Room.TREASUREROOM, player.getRoomX(), player.getRoomY() -
	 * 1, player.getPlane(), getRoomRotationToBuild()));
	 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
	 * player.getHouse().refresh(player, player.getBoundChuncks()); } break;
	 * default: if (player.getRights() == 2) {
	 * player.getPackets().sendGameMessage(String.valueOf(componentId)); }
	 * break; } }
	 * 
	 * public int getRoomRotationToBuild() { switch (p.getLocalX()) { case 55:
	 * return 1; case 48: return 3; } switch (p.getLocalY()) { case 55: return
	 * 0; case 48: return 2; } return 0; }
	 * 
	 * public boolean doesRoomExist(int roomx, int roomy) { for (RoomReference
	 * room : p.getRooms()) { if (room.getX() == roomx && room.getY() == roomy)
	 * { return true; } } return false; }
	 * 
	 * public boolean isBuildingLeft() { if (p.getLocalX() == 48) { return true;
	 * } else { return false; } }
	 * 
	 * public boolean isBuildingRight() { if (p.getLocalX() == 55) { return
	 * true; } else { return false; } }
	 * 
	 * public boolean isBuildingUp() { if (p.getLocalY() == 55) { return true; }
	 * else { return false; } }
	 * 
	 * public boolean isBuildingDown() { if (p.getLocalY() == 48) { return true;
	 * } else { return false; } }
	 */
}
