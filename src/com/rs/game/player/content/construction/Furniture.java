package com.rs.game.player.content.construction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.utils.ItemSetsKeyGenerator;
import com.rs.utils.Logger;

@SuppressWarnings("unused")
public class Furniture {

	private static House house;
	private static final int FURNITURE_ITEMS = ItemSetsKeyGenerator
			.generateKey();

	public Furniture(House house) {
		Furniture.house = house;
	}

	public static enum FurnitureObjects {
		// Chairs
		CRUDECHAIR(13581, 8309, 1, 66, new Item(960, 1)), WOODENCHAIR(13582,
				8310, 8, 96, new Item(960, 1)), ROCKINGCHAIR(13583, 8311, 14,
				96, new Item(960, 1)), OAKCHAIR(13584, 8312, 19, 120, new Item(
				8778, 1)), OAKARMCHAIR(13585, 8313, 26, 180, new Item(8778, 1)), TEAKCHAIR(
				13586, 8314, 35, 180, new Item(8780, 1)), MAHOGANYCHAIR(13587,
				8315, 60, 280, new Item(8782, 1)),
		// Fireplaces
		CLAYFIREPLACE(13609, 8325, 3, 30, new Item(1763, 3)), STONEFIREPLACE(
				13611, 8326, 33, 40, new Item(3211, 2)), MARBLEFIREPLACE(13613,
				8327, 66, 500, new Item(8786, 1)),
		// Curtains
		TORNCURTAINS(13603, 8322, 2, 132, new Item(960, 2)), CURTAINS(13604,
				8323, 18, 225, new Item(8778, 3)), OPULENTCURTAINS(13605, 8324,
				40, 315, new Item(8780, 3)),
		// Beds
		NORMALBED(13148, 1, 20, 117, new Item(960, 3)), OAKBED(13149, 1, 30,
				210, new Item(8778, 3)), LARGEOAKBED(13150, 1, 34, 330,
				new Item(8778, 5)), TEAKBED(13151, 1, 40, 300,
				new Item(8780, 3)), LARGETEAKBED(13152, 1, 45, 480, new Item(
				8780, 5)), POSTER(13153, 1, 53, 450, new Item(8782, 3)), GILDEDPOSTER(
				13154, 1, 60, 1330, new Item(8782, 5)),
		// Wardrobe
		SHOEBOX(13155, 1, 20, 58, new Item(995, 20000)), OAKDRAWERS(13156, 1,
				27, 120, new Item(995, 50000)), OAKWARDROBE(13157, 1, 39, 180,
				new Item(995, 10000)), TEAKDRAWERS(13158, 1, 51, 180, new Item(
				995, 100000)), TEAKWARDROBE(13159, 1, 63, 270, new Item(995,
				500000)), MAHOGANYWARDROBE(13160, 1, 75, 420, new Item(995,
				500000)), GILDEDWARDROBE(13161, 1, 87, 720, new Item(995,
				500000)),
		// Dresser
		SHAVINGSTAND(13162, 1, 21, 30, new Item(995, 20000)), OAKSHAVINGSTAND(
				13163, 1, 29, 61, new Item(995, 50000)), OAKDRESSER(13164, 1,
				37, 121, new Item(995, 10000)), TEAKDRESSER(13165, 1, 46, 181,
				new Item(995, 100000)), FANCYTEAKDRESSER(13166, 1, 56, 182,
				new Item(995, 500000)), MAHOGANYDRESSER(13167, 1, 64, 281,
				new Item(995, 500000)), GILDEDDRESSER(13168, 1, 74, 582,
				new Item(995, 500000)),
		// Clocks
		OAKCLOCK(13169, 8325, 25, 142, new Item(1763, 3)), TEAKCLOCK(13170,
				8326, 55, 202, new Item(3211, 2)), GILDEDCLOCK(13171, 8327, 86,
				602, new Item(8786, 1)),
		// KitchenTables
		WOODENKITCHENTABLE(13169, 8325, 12, 87, new Item(1763, 3)), OAKKITCHENTABLE(
				13170, 8326, 32, 180, new Item(3211, 2)), TEAKKITCHENTABLE(
				13171, 8327, 52, 270, new Item(8786, 1)),
		// KitchenLarder
		WOODENLARDER(13169, 8325, 9, 228, new Item(1763, 3)), OAKLARDER(13170,
				8326, 33, 480, new Item(3211, 2)), TEAKLARDER(13171, 8327, 43,
				750, new Item(8786, 1)),
		// KitchenSink
		PAD(13169, 8325, 7, 100, new Item(1763, 3)), PAT(13170, 8326, 27, 200,
				new Item(3211, 2)), SINK(13171, 8327, 47, 300,
				new Item(8786, 1)),
		// KitchenShelf
		SA(13162, 1, 6, 87, new Item(995, 20000)), SB(13163, 1, 12, 147,
				new Item(995, 50000)), SC(13164, 1, 23, 147, new Item(995,
				10000)), OA(13165, 1, 34, 240, new Item(995, 100000)), OB(
				13166, 1, 45, 240, new Item(995, 500000)), TEAKA(13167, 1, 56,
				330, new Item(995, 500000)), TEAKB(13168, 1, 67, 930, new Item(
				995, 500000)),
		// KitchenStove
		FP(13162, 1, 9, 40, new Item(995, 20000)), FWH(13163, 1, 11, 60,
				new Item(995, 50000)), FWP(13164, 1, 17, 80, new Item(995,
				10000)), SO(13165, 1, 24, 80, new Item(995, 100000)), LO(13166,
				1, 29, 100, new Item(995, 500000)), SR(13167, 1, 34, 120,
				new Item(995, 500000)), FR(13168, 1, 42, 160, new Item(995,
				500000)),
		// KitchenBarrel
		BB(13162, 1, 7, 87, new Item(995, 20000)), CB(13163, 1, 12, 91,
				new Item(995, 50000)), ALB(13164, 1, 18, 184, new Item(995,
				10000)), GAB(13165, 1, 25, 184, new Item(995, 100000)), DBB(
				13166, 1, 36, 224, new Item(995, 500000)), CDB(13167, 1, 48,
				224, new Item(995, 500000)),
		// KitchenCatBasket
		CAT1(13162, 1, 5, 15, new Item(995, 20000)), CAT2(13163, 1, 19, 58,
				new Item(995, 50000)), CAT3(13164, 1, 33, 58, new Item(995,
				10000)),
		// Stairs
		OAKSTAIRS(13497, 1, 27, 680, new Item(8778, 10)), TEAKSTAIRS(13499, 1,
				48, 980, new Item(8780, 10)), SPIRAL(13503, 1, 67, 1040,
				new Item(8780, 10)), MARBLESTAIRCASE(13501, 1, 82, 3200,
				new Item(8782, 10)), MARBLESPIRAL(13505, 1, 97, 4400, new Item(
				8782, 15)),
		// Bookcase
		WOODENBOOKCASE(13597, 1, 4, 115, new Item(960, 4)), OAKBOOKCASE(13598,
				1, 29, 180, new Item(960, 3)), MAHOGANYBOOKCASE(13599, 1, 40,
				420, new Item(960, 3)),
		// centerpieceGarden
		ROCK(13406, 1, 5, 100, new Item(995, 20000)), POND(13407, 1, 10, 10,
				new Item(995, 50000)), IMPSTATUE(13408, 1, 15, 150, new Item(
				995, 10000)), GAZEBO(13477, 1, 65, 1200, new Item(995, 100000)), DUNGENTRANCE(
				13409, 1, 70, 500, new Item(995, 500000)), SMALLFOUNTAIN(13478,
				1, 71, 500, new Item(995, 500000)), LARGEFOUNTAIN(13479, 1, 75,
				1000, new Item(995, 500000)), POSHFOUNTAIN(13480, 1, 81, 1500,
				new Item(995, 500000)),
		// RegularGarden
		DEADTREE(13418, 1, 5, 31, new Item(960, 4)), NICETREE(13419, 1, 10, 44,
				new Item(960, 4)), OAKTREE(13420, 1, 15, 70, new Item(960, 4)), WILLOWTREE(
				13421, 30, 4, 100, new Item(960, 4)), MAPLETREE(135423, 45, 4,
				122, new Item(960, 4)), YEWTREE(13422, 1, 60, 141, new Item(
				960, 4)), MAGICTREE(13424, 1, 75, 223, new Item(960, 4)),
		// SmallPlant1
		PLANT(13431, 1, 1, 31, new Item(960, 4)), SMALLFERN(13432, 6, 4, 70,
				new Item(960, 4)), FERN(13425, 1, 12, 100, new Item(960, 4)),
		// SmallPlant2
		DOCKLEAF(13434, 1, 1, 31, new Item(960, 4)), THISTLE(13435, 1, 6, 70,
				new Item(960, 4)), REEDS(13436, 1, 12, 100, new Item(960, 4)), BUSH(
				13426, 1, 6, 31, new Item(960, 4)), TALLPLANT(13427, 1, 12,
				100, new Item(960, 4)), SHORTPLANT(13428, 1, 1, 31, new Item(
				960, 4)), LARGELEAFBUSH(13429, 1, 6, 70, new Item(960, 4)), HUGEPLANT(
				13430, 1, 12, 100, new Item(960, 4)),
		// FormalGarden
		SUNFLOWER(13443, 1, 66, 70, new Item(960, 4)), MARIGOLDS(13444, 1, 71,
				100, new Item(960, 4)), ROSES(13445, 1, 76, 122, new Item(960,
				4)), ROSEMARY(13440, 1, 66, 70, new Item(960, 4)), DAFFODILS(
				13438, 1, 71, 100, new Item(960, 4)), BLUEBELLS(13439, 1, 76,
				122, new Item(960, 4)),
		// Hedge
		THORNYHEDGE(13459, 1, 56, 70, new Item(960, 4)), NICEHEDGE(13462, 1,
				60, 100, new Item(960, 4)), SMALLBOXHEDGE(13464, 1, 64, 122,
				new Item(960, 4)), TOPIARYHEDGE(13466, 1, 68, 141, new Item(
				960, 4)), FANCYHEDGE(13468, 1, 72, 158, new Item(960, 4)), TALLFANCYHEDGE(
				13471, 1, 76, 223, new Item(960, 4)), TALLBOXHEDGE(13474, 1,
				80, 316, new Item(960, 4)),
		// Fencing
		BOUNDARYSTONES(13449, 1, 55, 100, new Item(960, 4)), WOODENFENCE(13450,
				1, 59, 280, new Item(960, 4)), STONEWALL(13451, 1, 63, 200,
				new Item(960, 4)), IRONRAILS(13452, 1, 67, 220,
				new Item(960, 4)), PICKETFENCE(13453, 1, 71, 640, new Item(960,
				4)), GARDENFENCE(13454, 1, 75, 940, new Item(960, 4)), MARBLEWALL(
				13455, 1, 79, 4000, new Item(960, 4));
		// DungeonDoors

		int objectId;
		int itemId;
		int levelRequired;
		double xp;
		Item itemsRequired;
		static FurnitureObjects furniture;

		/**
		 * 
		 * @param objId
		 * @param itemId
		 * @param level
		 * @param xp
		 * @param itemsRequired
		 */
		private FurnitureObjects(int objId, int itemId, int level, double xp,
				Item itemsRequired) {
			this.objectId = objId;
			this.itemId = itemId;
			this.levelRequired = level;
			this.xp = xp;
			this.itemsRequired = itemsRequired;
		}

		public static FurnitureObjects getFurniture() {
			return furniture;
		}

		public int getId() {
			return objectId;
		}

		public int getItemId() {
			return itemId;
		}

		public double getXP() {
			return xp;
		}

		public int getLevel() {
			return levelRequired;
		}

		public Item getItems() {
			return itemsRequired;
		}

	}

	public static void sitInChair(WorldObject o, Player player) {
		int emote = 0;
		switch (o.getId()) {
		case 13584:
			emote = 4081;
			break;
		case 13587:// mag.chair
			switch (o.getRotation()) {
			case 1:
				emote = 4088;
				break;
			case 4:
				emote = 4088;
				break;
			default:
				emote = 4087;
				break;
			}
			break;
		case 13583:// rockingchair
		case 1:
			emote = 4079;
			break;
		case 4:
			emote = 4079;
			break;
		case 13665:
			emote = 4111;
			break;
		case 13670:
			emote = 4116;
			break;
		case 13671:
			emote = 4117;
			break;
		default:
			emote = 4080;
			break;
		}

		WorldTile tile = new WorldTile(o.getX(), o.getY(), 0);
		player.setNextWorldTile(tile);
		player.setNextAnimation(new Animation(emote));
	}

	public static void lightFireplace(WorldObject o, Player player) {
		int oldX, oldY, oldFace, oldType, oldId, oldPlane;
		oldX = o.getX();
		oldY = o.getY();
		oldFace = o.getRotation();
		oldType = o.getType();
		oldId = o.getId();
		oldPlane = o.getPlane();
		if (player.getInventory().containsItems(new int[] { 590, 1511 },
				new int[] { 1, 1 })) {
			World.spawnObject(new WorldObject(oldId + 1, oldType, oldFace,
					oldX, oldY, oldPlane), true);
		} else {
			player.getPackets().sendGameMessage(
					"You need a tinderbox and logs to light the fireplace!");
		}
	}

	public static void removeObject(WorldObject o) {
		World.spawnObject(
				new WorldObject(-1, o.getType(), o.getRotation(), o.getX(), o
						.getY(), o.getPlane()), false);
	}

	public static void save(WorldObject o, Player player) {
	//	player.getConObjectsToBeLoaded().add(o);
	}

	public static void createFurniturePiece(WorldObject o, Player player,
			String furniture) {
		WorldObject object = null;
		WorldObject bottomStair = null;
		WorldObject topStair = null;
		switch (furniture) {
		case "shoe box":
			object = new WorldObject(FurnitureObjects.SHOEBOX.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.SHOEBOX.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak drawers":
			object = new WorldObject(FurnitureObjects.OAKDRAWERS.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OAKDRAWERS.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak wardrobe":
			object = new WorldObject(FurnitureObjects.OAKWARDROBE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OAKWARDROBE.getXP());
			player.getSkills().refresh(22);
			break;
		case "teak drawers":
			object = new WorldObject(FurnitureObjects.TEAKDRAWERS.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TEAKDRAWERS.getXP());
			player.getSkills().refresh(22);
			break;
		case "mahogany wardrobe":
			object = new WorldObject(FurnitureObjects.MAHOGANYWARDROBE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.MAHOGANYWARDROBE.getXP());
			player.getSkills().refresh(22);
			break;
		case "gilded wardrobe":
			object = new WorldObject(FurnitureObjects.GILDEDWARDROBE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.GILDEDWARDROBE.getXP());
			player.getSkills().refresh(22);
			break;
		case "shaving stand":
			object = new WorldObject(FurnitureObjects.SHAVINGSTAND.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.SHAVINGSTAND.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak shaving stand":
			object = new WorldObject(FurnitureObjects.OAKSHAVINGSTAND.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.OAKSHAVINGSTAND.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak dresser":
			object = new WorldObject(FurnitureObjects.OAKDRESSER.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OAKDRESSER.getXP());
			player.getSkills().refresh(22);
			break;
		case "teak dresser":
			object = new WorldObject(FurnitureObjects.TEAKDRESSER.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TEAKDRESSER.getXP());
			player.getSkills().refresh(22);
			break;
		case "fancy teak dresser":
			object = new WorldObject(FurnitureObjects.FANCYTEAKDRESSER.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.FANCYTEAKDRESSER.getXP());
			player.getSkills().refresh(22);
			break;
		case "mahogany dresser":
			object = new WorldObject(FurnitureObjects.MAHOGANYDRESSER.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.MAHOGANYDRESSER.getXP());
			player.getSkills().refresh(22);
			break;
		case "gilded dresser":
			object = new WorldObject(FurnitureObjects.GILDEDDRESSER.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills()
					.addXp(22, FurnitureObjects.GILDEDDRESSER.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak clock":
			object = new WorldObject(FurnitureObjects.OAKCLOCK.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OAKCLOCK.getXP());
			player.getSkills().refresh(22);
			break;
		case "teak clock":
			object = new WorldObject(FurnitureObjects.TEAKCLOCK.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TEAKCLOCK.getXP());
			player.getSkills().refresh(22);
			break;
		case "gilded clock":
			object = new WorldObject(FurnitureObjects.GILDEDCLOCK.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.GILDEDCLOCK.getXP());
			player.getSkills().refresh(22);
			break;
		case "wooden kitchen table":
			object = new WorldObject(
					FurnitureObjects.WOODENKITCHENTABLE.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.WOODENKITCHENTABLE.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak kitchen table":
			object = new WorldObject(FurnitureObjects.OAKKITCHENTABLE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.OAKKITCHENTABLE.getXP());
			player.getSkills().refresh(22);
			break;
		case "teak kitchen table":
			object = new WorldObject(FurnitureObjects.TEAKKITCHENTABLE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.TEAKKITCHENTABLE.getXP());
			player.getSkills().refresh(22);
			break;
		case "wooden larder":
			object = new WorldObject(FurnitureObjects.WOODENLARDER.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.WOODENLARDER.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak larder":
			object = new WorldObject(FurnitureObjects.OAKLARDER.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OAKLARDER.getXP());
			player.getSkills().refresh(22);
			break;
		case "teak larder":
			object = new WorldObject(FurnitureObjects.TEAKLARDER.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TEAKLARDER.getXP());
			player.getSkills().refresh(22);
			break;
		case "pad":
			object = new WorldObject(FurnitureObjects.PAD.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.PAD.getXP());
			player.getSkills().refresh(22);
			break;
		case "pat":
			object = new WorldObject(FurnitureObjects.PAT.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.PAT.getXP());
			player.getSkills().refresh(22);
			break;
		case "sink":
			object = new WorldObject(FurnitureObjects.SINK.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.SINK.getXP());
			player.getSkills().refresh(22);
			break;
		case "sa":
			object = new WorldObject(FurnitureObjects.SA.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.SA.getXP());
			player.getSkills().refresh(22);
			break;
		case "sb":
			object = new WorldObject(FurnitureObjects.SB.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.SB.getXP());
			player.getSkills().refresh(22);
			break;
		case "sc":
			object = new WorldObject(FurnitureObjects.SC.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.SC.getXP());
			player.getSkills().refresh(22);
			break;
		case "oa":
			object = new WorldObject(FurnitureObjects.OA.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OA.getXP());
			player.getSkills().refresh(22);
			break;
		case "ob":
			object = new WorldObject(FurnitureObjects.OB.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OB.getXP());
			player.getSkills().refresh(22);
			break;
		case "teaka":
			object = new WorldObject(FurnitureObjects.TEAKA.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TEAKA.getXP());
			player.getSkills().refresh(22);
			break;
		case "teakb":
			object = new WorldObject(FurnitureObjects.TEAKB.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TEAKB.getXP());
			player.getSkills().refresh(22);
			break;
		case "fp":
			object = new WorldObject(FurnitureObjects.FP.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.FP.getXP());
			player.getSkills().refresh(22);
			break;
		case "fwh":
			object = new WorldObject(FurnitureObjects.FWH.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.FWH.getXP());
			player.getSkills().refresh(22);
			break;
		case "fwp":
			object = new WorldObject(FurnitureObjects.FWP.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.FWP.getXP());
			player.getSkills().refresh(22);
			break;
		case "so":
			object = new WorldObject(FurnitureObjects.SO.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.SO.getXP());
			player.getSkills().refresh(22);
			break;
		case "lo":
			object = new WorldObject(FurnitureObjects.LO.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.LO.getXP());
			player.getSkills().refresh(22);
			break;
		case "sr":
			object = new WorldObject(FurnitureObjects.SR.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.SR.getXP());
			player.getSkills().refresh(22);
			break;
		case "fr":
			object = new WorldObject(FurnitureObjects.FR.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.FR.getXP());
			player.getSkills().refresh(22);
			break;
		case "bb":
			object = new WorldObject(FurnitureObjects.BB.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.CB.getXP());
			player.getSkills().refresh(22);
			break;
		case "cb":
			object = new WorldObject(FurnitureObjects.CB.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.CB.getXP());
			player.getSkills().refresh(22);
			break;
		case "alb":
			object = new WorldObject(FurnitureObjects.ALB.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.ALB.getXP());
			player.getSkills().refresh(22);
			break;
		case "gab":
			object = new WorldObject(FurnitureObjects.GAB.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.GAB.getXP());
			player.getSkills().refresh(22);
			break;
		case "dbb":
			object = new WorldObject(FurnitureObjects.DBB.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.DBB.getXP());
			player.getSkills().refresh(22);
			break;
		case "cdb":
			object = new WorldObject(FurnitureObjects.CDB.getId(), o.getType(),
					o.getRotation(), o.getX(), o.getY(), o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.CDB.getXP());
			player.getSkills().refresh(22);
			break;
		case "cat1":
			object = new WorldObject(FurnitureObjects.CAT1.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.CAT1.getXP());
			player.getSkills().refresh(22);
			break;
		case "cat2":
			object = new WorldObject(FurnitureObjects.CAT2.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.CAT2.getXP());
			player.getSkills().refresh(22);
			break;
		case "cat3":
			object = new WorldObject(FurnitureObjects.CAT3.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.CAT3.getXP());
			player.getSkills().refresh(22);
			break;
		case "dead tree":
			object = new WorldObject(FurnitureObjects.DEADTREE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.DEADTREE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.DEADTREE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "nice tree":
			object = new WorldObject(FurnitureObjects.NICETREE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.NICETREE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.NICETREE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "oak tree":
			object = new WorldObject(FurnitureObjects.OAKTREE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OAKTREE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.OAKTREE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "willow tree":
			object = new WorldObject(FurnitureObjects.WILLOWTREE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.WILLOWTREE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.WILLOWTREE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "maple tree":
			object = new WorldObject(FurnitureObjects.MAPLETREE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.MAPLETREE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.MAPLETREE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "yew tree":
			object = new WorldObject(FurnitureObjects.YEWTREE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.YEWTREE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.YEWTREE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "magic tree":
			object = new WorldObject(FurnitureObjects.MAGICTREE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.MAGICTREE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.MAGICTREE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "plant":
			object = new WorldObject(FurnitureObjects.PLANT.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.PLANT.getXP());
			player.getSkills().addXp(19, FurnitureObjects.PLANT.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "small fern":
			object = new WorldObject(FurnitureObjects.SMALLFERN.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.SMALLFERN.getXP());
			player.getSkills().addXp(19, FurnitureObjects.SMALLFERN.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "fern":
			object = new WorldObject(FurnitureObjects.FERN.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.FERN.getXP());
			player.getSkills().addXp(19, FurnitureObjects.FERN.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "dock leaf":
			object = new WorldObject(FurnitureObjects.DOCKLEAF.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.DOCKLEAF.getXP());
			player.getSkills().addXp(19, FurnitureObjects.DOCKLEAF.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "thistle":
			object = new WorldObject(FurnitureObjects.THISTLE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.THISTLE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.THISTLE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "reeds":
			object = new WorldObject(FurnitureObjects.REEDS.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.REEDS.getXP());
			player.getSkills().addXp(19, FurnitureObjects.REEDS.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "bush":
			object = new WorldObject(FurnitureObjects.BUSH.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.BUSH.getXP());
			player.getSkills().addXp(19, FurnitureObjects.BUSH.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "tall plant":
			object = new WorldObject(FurnitureObjects.TALLPLANT.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TALLPLANT.getXP());
			player.getSkills().addXp(19, FurnitureObjects.TALLPLANT.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "short plant":
			object = new WorldObject(FurnitureObjects.SHORTPLANT.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.SHORTPLANT.getXP());
			player.getSkills().addXp(19, FurnitureObjects.SHORTPLANT.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "large leaf bush":
			object = new WorldObject(FurnitureObjects.LARGELEAFBUSH.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills()
					.addXp(22, FurnitureObjects.LARGELEAFBUSH.getXP());
			player.getSkills()
					.addXp(19, FurnitureObjects.LARGELEAFBUSH.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "huge plant":
			object = new WorldObject(FurnitureObjects.HUGEPLANT.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.HUGEPLANT.getXP());
			player.getSkills().addXp(19, FurnitureObjects.HUGEPLANT.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "sunflower":
			object = new WorldObject(FurnitureObjects.SUNFLOWER.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.SUNFLOWER.getXP());
			player.getSkills().addXp(19, FurnitureObjects.SUNFLOWER.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "marigolds":
			object = new WorldObject(FurnitureObjects.MARIGOLDS.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.MARIGOLDS.getXP());
			player.getSkills().addXp(19, FurnitureObjects.MARIGOLDS.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "roses":
			object = new WorldObject(FurnitureObjects.ROSES.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.ROSES.getXP());
			player.getSkills().addXp(19, FurnitureObjects.ROSES.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "rosemary":
			object = new WorldObject(FurnitureObjects.ROSEMARY.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.ROSEMARY.getXP());
			player.getSkills().addXp(19, FurnitureObjects.ROSEMARY.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "daffodils":
			object = new WorldObject(FurnitureObjects.DAFFODILS.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.DAFFODILS.getXP());
			player.getSkills().addXp(19, FurnitureObjects.DAFFODILS.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "blue bells":
			object = new WorldObject(FurnitureObjects.BLUEBELLS.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.BLUEBELLS.getXP());
			player.getSkills().addXp(19, FurnitureObjects.BLUEBELLS.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "thorny hedge":
			object = new WorldObject(FurnitureObjects.THORNYHEDGE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.THORNYHEDGE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.THORNYHEDGE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "nice hedge":
			object = new WorldObject(FurnitureObjects.NICEHEDGE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.NICEHEDGE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.NICEHEDGE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "small box hedge":
			object = new WorldObject(FurnitureObjects.SMALLBOXHEDGE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills()
					.addXp(22, FurnitureObjects.SMALLBOXHEDGE.getXP());
			player.getSkills()
					.addXp(19, FurnitureObjects.SMALLBOXHEDGE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "topiary hedge":
			object = new WorldObject(FurnitureObjects.TOPIARYHEDGE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TOPIARYHEDGE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.TOPIARYHEDGE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "fancy hedge":
			object = new WorldObject(FurnitureObjects.FANCYHEDGE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.FANCYHEDGE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.FANCYHEDGE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "tall fancy hedge":
			object = new WorldObject(FurnitureObjects.TALLFANCYHEDGE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.TALLFANCYHEDGE.getXP());
			player.getSkills().addXp(19,
					FurnitureObjects.TALLFANCYHEDGE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "tall box hedge":
			object = new WorldObject(FurnitureObjects.TALLBOXHEDGE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TALLBOXHEDGE.getXP());
			player.getSkills().addXp(19, FurnitureObjects.TALLBOXHEDGE.getXP());
			player.getSkills().refresh(22);
			player.getSkills().refresh(19);
			break;
		case "boundary stones":
			object = new WorldObject(FurnitureObjects.BOUNDARYSTONES.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.BOUNDARYSTONES.getXP());
			player.getSkills().refresh(22);
			break;
		case "wooden fence":
			object = new WorldObject(FurnitureObjects.WOODENFENCE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.WOODENFENCE.getXP());
			player.getSkills().refresh(22);
			break;
		case "stone wall":
			object = new WorldObject(FurnitureObjects.STONEWALL.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.STONEWALL.getXP());
			player.getSkills().refresh(22);
			break;
		case "iron rails":
			object = new WorldObject(FurnitureObjects.IRONRAILS.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.IRONRAILS.getXP());
			player.getSkills().refresh(22);
			break;
		case "picket fence":
			object = new WorldObject(FurnitureObjects.PICKETFENCE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.PICKETFENCE.getXP());
			player.getSkills().refresh(22);
			break;
		case "garden fence":
			object = new WorldObject(FurnitureObjects.GARDENFENCE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.GARDENFENCE.getXP());
			player.getSkills().refresh(22);
			break;
		case "marble wall":
			object = new WorldObject(FurnitureObjects.MARBLEWALL.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.MARBLEWALL.getXP());
			player.getSkills().refresh(22);
			break;
		case "pond":
			object = new WorldObject(FurnitureObjects.POND.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.POND.getXP());
			player.getSkills().refresh(22);
			break;
		case "gazebo":
			object = new WorldObject(FurnitureObjects.GAZEBO.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.GAZEBO.getXP());
			player.getSkills().refresh(22);
			break;
		case "dung entrance":
			object = new WorldObject(FurnitureObjects.DUNGENTRANCE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.DUNGENTRANCE.getXP());
			player.getSkills().refresh(22);
			break;
		case "rock":
			object = new WorldObject(FurnitureObjects.ROCK.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.ROCK.getXP());
			player.getSkills().refresh(22);
			break;
		case "small fountain":
			object = new WorldObject(FurnitureObjects.SMALLFOUNTAIN.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills()
					.addXp(22, FurnitureObjects.SMALLFOUNTAIN.getXP());
			player.getSkills().refresh(22);
			break;
		case "large fountain":
			object = new WorldObject(FurnitureObjects.LARGEFOUNTAIN.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills()
					.addXp(22, FurnitureObjects.LARGEFOUNTAIN.getXP());
			player.getSkills().refresh(22);
			break;
		case "posh fountain":
			object = new WorldObject(FurnitureObjects.POSHFOUNTAIN.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.POSHFOUNTAIN.getXP());
			player.getSkills().refresh(22);
			break;
		case "wooden book case":
			object = new WorldObject(FurnitureObjects.WOODENBOOKCASE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.WOODENBOOKCASE.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak book case":
			object = new WorldObject(FurnitureObjects.OAKBOOKCASE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OAKBOOKCASE.getXP());
			player.getSkills().refresh(22);
			break;
		case "mahogany book case":
			object = new WorldObject(FurnitureObjects.MAHOGANYBOOKCASE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.MAHOGANYBOOKCASE.getXP());
			player.getSkills().refresh(22);
			break;
		case "crude chair":
			object = new WorldObject(FurnitureObjects.CRUDECHAIR.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.CRUDECHAIR.getXP());
			player.getSkills().refresh(22);
			break;
		case "wooden chair":
			object = new WorldObject(FurnitureObjects.WOODENCHAIR.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.WOODENCHAIR.getXP());
			player.getSkills().refresh(22);
			break;
		case "rocking chair":
			object = new WorldObject(FurnitureObjects.ROCKINGCHAIR.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.ROCKINGCHAIR.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak chair":
			object = new WorldObject(FurnitureObjects.OAKCHAIR.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OAKCHAIR.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak armchair":
			object = new WorldObject(FurnitureObjects.OAKARMCHAIR.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OAKARMCHAIR.getXP());
			player.getSkills().refresh(22);
			break;
		case "teak chair":
			object = new WorldObject(FurnitureObjects.TEAKCHAIR.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			World.spawnObject(object, true);
			save(object, player);
			player.getSkills().addXp(22, FurnitureObjects.TEAKCHAIR.getXP());
			player.getSkills().refresh(22);
			break;
		case "mahogany chair":
			object = new WorldObject(FurnitureObjects.MAHOGANYCHAIR.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			;
			World.spawnObject(object, true);
			player.getSkills()
					.addXp(22, FurnitureObjects.MAHOGANYCHAIR.getXP());
			player.getSkills().refresh(22);
			break;
		case "clay fireplace":
			object = new WorldObject(FurnitureObjects.CLAYFIREPLACE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills()
					.addXp(22, FurnitureObjects.CLAYFIREPLACE.getXP());
			player.getSkills().refresh(22);
			break;
		case "stone fireplace":
			object = new WorldObject(FurnitureObjects.STONEFIREPLACE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.STONEFIREPLACE.getXP());
			player.getSkills().refresh(22);
			break;
		case "torn curtains":
			object = new WorldObject(FurnitureObjects.TORNCURTAINS.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TORNCURTAINS.getXP());
			player.getSkills().refresh(22);
			break;
		case "curtains":
			object = new WorldObject(FurnitureObjects.CURTAINS.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TORNCURTAINS.getXP());
			player.getSkills().refresh(22);
			break;
		case "opulent curtains":
			object = new WorldObject(FurnitureObjects.OPULENTCURTAINS.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.OPULENTCURTAINS.getXP());
			player.getSkills().refresh(22);
			break;
		case "marble fireplace":
			object = new WorldObject(FurnitureObjects.MARBLEFIREPLACE.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22,
					FurnitureObjects.MARBLEFIREPLACE.getXP());
			player.getSkills().refresh(22);
			break;
		case "wooden bed":
			object = new WorldObject(FurnitureObjects.NORMALBED.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.NORMALBED.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak bed":
			object = new WorldObject(FurnitureObjects.OAKBED.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.OAKBED.getXP());
			player.getSkills().refresh(22);
			break;
		case "large oak bed":
			object = new WorldObject(FurnitureObjects.LARGEOAKBED.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.LARGEOAKBED.getXP());
			player.getSkills().refresh(22);
			break;
		case "teak bed":
			object = new WorldObject(FurnitureObjects.TEAKBED.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.TEAKBED.getXP());
			player.getSkills().refresh(22);
			break;
		case "large teak bed":
			object = new WorldObject(FurnitureObjects.LARGETEAKBED.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.LARGETEAKBED.getXP());
			player.getSkills().refresh(22);
			break;
		case "4 bed":
			object = new WorldObject(FurnitureObjects.POSTER.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.POSTER.getXP());
			player.getSkills().refresh(22);
			break;
		case "gilded 4 bed":
			object = new WorldObject(FurnitureObjects.GILDEDPOSTER.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			save(object, player);
			World.spawnObject(object, true);
			player.getSkills().addXp(22, FurnitureObjects.GILDEDPOSTER.getXP());
			player.getSkills().refresh(22);
			break;
		case "oak staircase":
			bottomStair = new WorldObject(FurnitureObjects.OAKSTAIRS.getId(),
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane());
			topStair = new WorldObject(FurnitureObjects.OAKSTAIRS.getId() + 1,
					o.getType(), o.getRotation(), o.getX(), o.getY(),
					o.getPlane() + 1);
			save(bottomStair, player);
			save(topStair, player);
			World.spawnObject(bottomStair, true);
			World.spawnObject(topStair, true);
			/*
			 * if (player.getRoomReference().getCurrentRoom(player).getName().
			 * equalsIgnoreCase("quest hall 1")) { player.getRooms().add(new
			 * RoomReference(Room.QUESTHALL2, player.getRoomX(),
			 * player.getRoomY(), player.getPlane() + 1,
			 * player.getRoomReference().getRotation()));
			 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
			 * player.getHouse().refresh(player, player.getBoundChuncks()); } if
			 * (player.getRoomReference().getCurrentRoom(player).getName().
			 * equalsIgnoreCase("skill hall 1")) { player.getRooms().add(new
			 * RoomReference(Room.SKILLHALL2, player.getRoomX(),
			 * player.getRoomY(), player.getPlane() + 1,
			 * player.getRoomMaking().getRoomRotationToBuild()));
			 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
			 * player.getHouse().refresh(player, player.getBoundChuncks()); }
			 * break; case "teak staircase": bottomStair = new
			 * WorldObject(FurnitureObjects.TEAKSTAIRS.getId(), o.getType(),
			 * o.getRotation(), o.getX(), o.getY(), o.getPlane()); topStair =
			 * new WorldObject(FurnitureObjects.TEAKSTAIRS.getId() + 1,
			 * o.getType(), o.getRotation(), o.getX(), o.getY(), o.getPlane() +
			 * 1); save(bottomStair, player); save(topStair, player);
			 * World.spawnObject(bottomStair, true); World.spawnObject(topStair,
			 * true); if
			 * (player.getRoomReference().getCurrentRoom(player).getName
			 * ().equalsIgnoreCase("quest hall 1")) { player.getRooms().add(new
			 * RoomReference(Room.QUESTHALL2, player.getRoomX(),
			 * player.getRoomY(), player.getPlane() + 1,
			 * player.getRoomReference().getRotation()));
			 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
			 * player.getHouse().refresh(player, player.getBoundChuncks()); } if
			 * (player.getRoomReference().getCurrentRoom(player).getName().
			 * equalsIgnoreCase("skill hall 1")) { player.getRooms().add(new
			 * RoomReference(Room.SKILLHALL2, player.getRoomX(),
			 * player.getRoomY(), player.getPlane() + 1,
			 * player.getRoomMaking().getRoomRotationToBuild()));
			 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
			 * player.getHouse().refresh(player, player.getBoundChuncks()); }
			 * break; case "spiral staircase": bottomStair = new
			 * WorldObject(FurnitureObjects.SPIRAL.getId(), o.getType(),
			 * o.getRotation(), o.getX(), o.getY(), o.getPlane()); topStair =
			 * new WorldObject(FurnitureObjects.SPIRAL.getId(), o.getType(),
			 * o.getRotation(), o.getX(), o.getY(), o.getPlane() + 1);
			 * save(bottomStair, player); save(topStair, player);
			 * World.spawnObject(bottomStair, true); World.spawnObject(topStair,
			 * true); if
			 * (player.getRoomReference().getCurrentRoom(player).getName
			 * ().equalsIgnoreCase("quest hall 1")) { player.getRooms().add(new
			 * RoomReference(Room.QUESTHALL2, player.getRoomX(),
			 * player.getRoomY(), player.getPlane() + 1,
			 * player.getRoomReference().getRotation()));
			 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
			 * player.getHouse().refresh(player, player.getBoundChuncks()); } if
			 * (player.getRoomReference().getCurrentRoom(player).getName().
			 * equalsIgnoreCase("skill hall 1")) { player.getRooms().add(new
			 * RoomReference(Room.SKILLHALL2, player.getRoomX(),
			 * player.getRoomY(), player.getPlane() + 1,
			 * player.getRoomMaking().getRoomRotationToBuild()));
			 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
			 * player.getHouse().refresh(player, player.getBoundChuncks()); }
			 * break; case "marble staircase": bottomStair = new
			 * WorldObject(FurnitureObjects.MARBLESTAIRCASE.getId(),
			 * o.getType(), o.getRotation(), o.getX(), o.getY(), o.getPlane());
			 * topStair = new
			 * WorldObject(FurnitureObjects.MARBLESTAIRCASE.getId() + 1,
			 * o.getType(), o.getRotation(), o.getX(), o.getY(), o.getPlane() +
			 * 1); save(bottomStair, player); save(topStair, player);
			 * World.spawnObject(bottomStair, true); World.spawnObject(topStair,
			 * true); if
			 * (player.getRoomReference().getCurrentRoom(player).getName
			 * ().equalsIgnoreCase("quest hall 1")) { player.getRooms().add(new
			 * RoomReference(Room.QUESTHALL2, player.getRoomX(),
			 * player.getRoomY(), player.getPlane() + 1,
			 * player.getRoomReference().getRotation()));
			 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
			 * player.getHouse().refresh(player, player.getBoundChuncks()); } if
			 * (player.getRoomReference().getCurrentRoom(player).getName().
			 * equalsIgnoreCase("skill hall 1")) { player.getRooms().add(new
			 * RoomReference(Room.SKILLHALL2, player.getRoomX(),
			 * player.getRoomY(), player.getPlane() + 1,
			 * player.getRoomMaking().getRoomRotationToBuild()));
			 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
			 * player.getHouse().refresh(player, player.getBoundChuncks()); }
			 * break; case "marble spiral": bottomStair = new
			 * WorldObject(FurnitureObjects.MARBLESPIRAL.getId(), o.getType(),
			 * o.getRotation(), o.getX(), o.getY(), o.getPlane()); topStair =
			 * new WorldObject(FurnitureObjects.MARBLESPIRAL.getId() + 1,
			 * o.getType(), o.getRotation(), o.getX(), o.getY(), o.getPlane() +
			 * 1); save(bottomStair, player); save(topStair, player);
			 * World.spawnObject(bottomStair, true); World.spawnObject(topStair,
			 * true); if
			 * (player.getRoomReference().getCurrentRoom(player).getName
			 * ().equalsIgnoreCase("quest hall 1")) { player.getRooms().add(new
			 * RoomReference(Room.QUESTHALL2, player.getRoomX(),
			 * player.getRoomY(), player.getPlane() + 1,
			 * player.getRoomReference().getRotation()));
			 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
			 * player.getHouse().refresh(player, player.getBoundChuncks()); } if
			 * (player.getRoomReference().getCurrentRoom(player).getName().
			 * equalsIgnoreCase("skill hall 1")) { player.getRooms().add(new
			 * RoomReference(Room.SKILLHALL2, player.getRoomX(),
			 * player.getRoomY(), player.getPlane() + 1,
			 * player.getRoomMaking().getRoomRotationToBuild()));
			 * player.getHouse().constructHouse(player.getBoundChuncks(), true);
			 * player.getHouse().refresh(player, player.getBoundChuncks()); }
			 * break;
			 */
		case "opulent rug":
			int id1 = 13594;
			int id2 = 13595;
			int id3 = 13596;
			boolean isParlour = /*
								 * player.getRoomReference().getCurrentRoom(player
								 * ).getName().equalsIgnoreCase("parlour");
								 */false;
			if (isParlour) {
				for (int i = 51; i < 53; i++) { /* Generate middle of rug */
					for (int i1 = 51; i1 < 53; i++) {
						/*
						 * object = new WorldObject(id2, o.getType(),
						 * o.getRotation(), convertX(i, player), convertY(i1,
						 * player), o.getPlane()); World.spawnObject(object,
						 * true); save(object, player);
						 */
					}
					System.out.println(i);
				}
			}
			break;
		}
		player.setNextAnimation(new Animation(898));
		// refreshRoom(player);
	}

	private static int convertX(int localX, Player player) {
		return (localX + 8 * (player.getChunkX() - (102 >> 4)));
	}

	private static int convertY(int localY, Player player) {
		return (localY + 8 * (player.getChunkY() - (102 >> 4)));
	}

	public static void refreshRoom(Player player) {
		// house.refresh(player, player.getBoundChuncks());
	}

}
