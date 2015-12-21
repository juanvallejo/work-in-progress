package com.rs.net.decoders.handlers;

import java.util.TimerTask;

import com.rs.Settings;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.minigames.CastleWars;
import com.rs.game.minigames.Crucible;
import com.rs.game.minigames.CrystalChest;
import com.rs.game.minigames.FightPits;
import com.rs.game.player.ArdyFarming;
import com.rs.game.player.CathFarming;
import com.rs.game.player.ClueScrolls;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.FarmingManager;
import com.rs.game.player.InterfaceManager;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.player.Player;
import com.rs.game.player.QuestManager.Quests;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Bonfire;
import com.rs.game.player.actions.BoxAction.HunterEquipment;
import com.rs.game.player.actions.BoxAction.HunterNPC;
import com.rs.game.player.actions.Cooking;
import com.rs.game.player.actions.Cooking.Cookables;
import com.rs.game.player.actions.BoxAction;
import com.rs.game.player.actions.CowMilkingAction;
import com.rs.game.player.actions.FarmWoodcutting;
import com.rs.game.player.actions.PlayerCombat;
import com.rs.game.player.actions.Smelting;
import com.rs.game.player.actions.Smithing.ForgingBar;
import com.rs.game.player.actions.Smithing.ForgingInterface;
import com.rs.game.player.actions.Summoning;
import com.rs.game.player.actions.Woodcutting;
import com.rs.game.player.actions.Woodcutting.TreeDefinitions;
import com.rs.game.player.actions.mining.EssenceMining;
import com.rs.game.player.actions.mining.EssenceMining.EssenceDefinitions;
import com.rs.game.player.actions.mining.Mining;
import com.rs.game.player.actions.mining.Mining.RockDefinitions;
import com.rs.game.player.actions.mining.MiningBase;
import com.rs.game.player.actions.runecrafting.SihponActionNodes;
import com.rs.game.player.actions.thieving.Thieving;
import com.rs.game.player.content.BonesOnAltar;
import com.rs.game.player.content.BonesOnAltar.Bones;
import com.rs.game.player.content.CanafisFarming;
import com.rs.game.player.content.Hunter;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.PartyRoom;
import com.rs.game.player.content.PolyporeDungeon;
import com.rs.game.player.content.Runecrafting;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.content.agility.BarbarianOutpostAgility;
import com.rs.game.player.content.agility.GnomeAgility;
import com.rs.game.player.content.construction.Furniture;
import com.rs.game.player.content.construction.House;
import com.rs.game.player.content.construction.Furniture.FurnitureObjects;
import com.rs.game.player.content.construction.PlayerHouseSaving;
import com.rs.game.player.content.construction.Room;
import com.rs.game.player.content.construction.RoomReference;
import com.rs.game.player.controlers.Falconry;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.FightKiln;
import com.rs.game.player.controlers.WGuildControler;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.controlers.ZombieMinigame;
import com.rs.game.player.dialogues.MiningGuildDwarf;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.utils.Java;
import com.rs.utils.Logger;
import com.rs.utils.PkRank;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;
import com.rs.utils.Watch;

public final class ObjectHandler {
	private static House house;
	private ObjectHandler() {

	}

	public static void handleOption(final Player player, InputStream stream, int option) {
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
				|| player.isDead())
			return;
		long currentTime = Utils.currentTimeMillis();
		if (player.getLockDelay() >= currentTime
				|| player.getEmotesManager().getNextEmoteEnd() >= currentTime)
			return;
		boolean forceRun = stream.readUnsignedByte128() == 1;
		final int id = stream.readIntLE();
		int x = stream.readUnsignedShortLE();
		int y = stream.readUnsignedShortLE128();
		int rotation = 0;
		if (player.isAtDynamicRegion()) {
			rotation = World.getRotation(player.getPlane(), x, y);
			if(rotation == 1) {
				ObjectDefinitions defs = ObjectDefinitions
						.getObjectDefinitions(id);
				y += defs.getSizeY() - 1;
			}else if(rotation == 2) {
				ObjectDefinitions defs = ObjectDefinitions
						.getObjectDefinitions(id);
				x += defs.getSizeY() - 1;
			}
		}
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		final int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
		if (mapObject == null || mapObject.getId() != id) 
			return;
		if (player.isAtDynamicRegion()
				&& World.getRotation(player.getPlane(), x, y) != 0) { //temp fix
			ObjectDefinitions defs = ObjectDefinitions
					.getObjectDefinitions(id);
			if (defs.getSizeX() > 1 || defs.getSizeY() > 1) {
				for (int xs = 0; xs < defs.getSizeX() + 1
						&& (mapObject == null || mapObject.getId() != id); xs++) {
					for (int ys = 0; ys < defs.getSizeY() + 1
							&& (mapObject == null || mapObject.getId() != id); ys++) {
						tile.setLocation(x + xs, y + ys, tile.getPlane());
						mapObject = World.getRegion(regionId).getObject(id,
								tile);
					}
				}
			}
			if (mapObject == null || mapObject.getId() != id)
				return;
		}
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(id, mapObject.getType(),
						(mapObject.getRotation() + rotation % 4), x, y, player.getPlane());
		player.stopAll(false);
		if(forceRun)
			player.setRun(forceRun);
		switch(option) {
		case 1:
			handleOption1(player, object);
			break;
		case 2:
			handleOption2(player, object);
			break;
		case 3:
			handleOption3(player, object);
			break;
		case 4:
			handleOption4(player, object);
			break;
		case 5:
			handleOption5(player, object);
			break;
		case -1:
			handleOptionExamine(player, object);
			break;
		}
	}

	private static void handleOption1(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		final int x = object.getX();
		final int y = object.getY();
		if(SihponActionNodes.siphon(player, object)) 
			return;
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControlerManager().processObjectClick1(object))
					return;
				if (CastleWars.handleObjects(player, id))
					return;
				if (ClueScrolls.objectSpot(player, object)){
					return;
				}

				// broadcast current object id being clicked
				System.out.println("Object clicked -> id = " + id);

				if (id == 15482) {
					player.getDialogueManager().startDialogue("HouseTeleport");
				}
				if (id == 6 || id == 29408) {
					player.getDwarfCannon().preRotationSetup(object);
				}

				// al-kharid gate click handler
				if(id == 35549) {
					
					//check to see if player has at least ten coins
					if(!player.getInventory().containsItem(995, 10)) {
						player.getDialogueManager().startDialogue("AlKharidGate");
						System.out.println("You don't have enough coins to pass through!");
						return;
					}

					System.out.println("Right");
					// assume at least 10 coins were found
					player.getDialogueManager().startDialogue("GeneralStore");

				}
				//if (id == 19671) // Not finished
				//	player.getActionManager().setAction(new BoxAction(HunterEquipment.SALAMANDER));
				if (id == 65616 || id == 65617 || id == 65618|| id == 65619 || id == 65620 || id == 65621) {
					WorldTile tile = new WorldTile(3156, 3620, 0);
					WorldTile tile2 = new WorldTile(3219, 3656, 0);
					WorldTile tile3 = new WorldTile(3035, 3732, 0);
					WorldTile tile4 = new WorldTile(3106, 3794, 0);
					WorldTile tile5 = new WorldTile(2980, 3866, 0);
					WorldTile tile6 = new WorldTile(3307, 3916, 0);
					WorldTile[] tiles = {tile, tile2, tile3, tile4, tile5, tile6};
					Magic.sendWildyTeleport(player, 0, 0, tiles[Utils.random(0, 6)]);
				}
				if (id == 15595) {
					if (player.getInventory().containsItem(14, 1) && player.getInventory().containsItem(2347, 1) && player.fixedRailing1 == false) {
					player.getPackets().sendConfigByFile(2245, 1);
					player.fixedRailings++;
					player.out("You have fixed "+player.fixedRailings+" railing.");
					player.fixedRailing1 = true;
					player.getInventory().deleteItem(14, 1);
					} else {
						player.out("You need a hammer and a railing to fix this.");
					}
				}
				if (id == 15594) {
					if (player.getInventory().containsItem(14, 1) && player.getInventory().containsItem(2347, 1) && player.fixedRailing2 == false) {
					player.getPackets().sendConfigByFile(2244, 1);
					player.fixedRailings++;
					player.out("You have fixed "+player.fixedRailings+" railings.");
					player.fixedRailing2 = true;
					player.getInventory().deleteItem(14, 1);
					} else {
						player.out("You need a hammer and a railing to fix this.");
					}
				}
				if (id == 15593) {
					if (player.getInventory().containsItem(14, 1) && player.getInventory().containsItem(2347, 1) && player.fixedRailing3 == false) {
					player.getPackets().sendConfigByFile(2243, 1);
					player.fixedRailings++;
					player.out("You have fixed "+player.fixedRailings+" railings.");
					player.fixedRailing3 = true;
					player.getInventory().deleteItem(14, 1);
					} else {
						player.out("You need a hammer and a railing to fix this.");
					}
				}
				if (id == 15592) {
					if (player.getInventory().containsItem(14, 1) && player.getInventory().containsItem(2347, 1) && player.fixedRailing4 == false) {
					player.getPackets().sendConfigByFile(2242, 1);
					player.fixedRailings++;
					player.out("You have fixed "+player.fixedRailings+" railings.");
					player.fixedRailing4 = true;
					player.getInventory().deleteItem(14, 1);
					} else {
						player.out("You need a hammer and a railing to fix this.");
					}
				}
				if (id == 15591) {
					if (player.getInventory().containsItem(14, 1) && player.getInventory().containsItem(2347, 1) && player.fixedRailing5 == false) {
					player.getPackets().sendConfigByFile(2241, 1);
					player.fixedRailings++;
					player.out("You have fixed "+player.fixedRailings+" railings.");
					player.fixedRailing5 = true;
					player.getInventory().deleteItem(14, 1);
					} else {
						player.out("You need a hammer and a railing to fix this.");
					}
				}
				if (id == 15590) {
					if (player.getInventory().containsItem(14, 1) && player.getInventory().containsItem(2347, 1) && player.fixedRailing6 == false) {
					player.getPackets().sendConfigByFile(2240, 1);
					player.fixedRailings++;
					player.out("You have fixed "+player.fixedRailings+" railings.");
					player.fixedRailing6 = true;
					player.getInventory().deleteItem(14, 1);
					} else {
						player.out("You need a hammer and a railing to fix this.");
					}
				}
				if (object.getId() == 13405)
					player.getDialogueManager().startDialogue("LeaveHouse");
				if (id == 23096 && player.hasEscavated == false) {
						if (player.hasPick(player)) {
						WorldTasksManager.schedule(new WorldTask() {
							int loop;
							@Override
							public void run() {
								if (loop == 0) {
									player.lock();
									player.setNextAnimation(new Animation(625));
								} else if (loop == 3) {
									player.setNextForceTalk(new ForceTalk("Hmm."));
								} else if (loop == 6) {	
									player.getPackets().sendConfigByFile(3524, 1);
									player.stopAll();
									player.unlock();
									player.out("You break through! You find a secret entrance!");
									player.hasEscavated = true;
								}
									loop++;
									}
								}, 0, 1);
							} else if (!player.hasPick(player)) {
								player.out("You'll need a pickaxe to attempt this.");
							}
					}
				if (id == 23096 && player.hasEscavated == true) {
							player.setNextWorldTile(new WorldTile(3298, 9825, 0));
				}
				if (id == 13999) {
					player.setNextWorldTile(new WorldTile(3284, 3467, 0));
				}
				
				
				/**
				 * Construction Teleport Portals
				 */
				
				switch (object.getId()) {
				case 13622: //Varrock
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3213, 3428, 0));
					break;
				case 13623: //Lumbridge
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3222, 3219, 0));
					break;
				case 13624: //Falador
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2965, 3380, 0));
					break;
				case 13625: //Camelot
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2726, 3485, 0));
					break;
				case 13627: // Yanille
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2606, 3092, 0));
					break;
				case 13613:
					if (!player.getInventory().containsItem(590, 1)) {
					player.out("You need a tinder box to light the fireplace.");
					} else {
						player.firePlace = 6;
						player.out("You light the fire place.");
					}
					break;
				case 13611:
					if (!player.getInventory().containsItem(590, 1)) {
					player.out("You need a tinder box to light the fireplace.");
					} else {
						player.firePlace = 5;
						player.out("You light the fire place.");
					}
					break;
				case 13609:
					if (!player.getInventory().containsItem(590, 1)) {
					player.out("You need a tinder box to light the fireplace.");
					} else {
						player.firePlace = 4;
						player.out("You light the fire place.");
					}
					break;
				}

				
				switch (objectDef.name.toLowerCase()) {
				case "chair":
					if (id == 13581) {
						player.out("This chair may break if I were to sit on it.");
						break;
					}
					Furniture.sitInChair(object, player);
					player.faceObject(object);
				case "throne":
					Furniture.sitInChair(object, player);
					break;
				}
				if (object.getId() == 2145)
					ZombieMinigame.enterZombieGame(player);
				
				if (object.getId() == 2563)
					player.getDialogueManager().startDialogue("Cape");
				
				if (object.getId() == 3014)
					player.getPackets().sendConfig(406, 1);
				
				if (object.getId() == 18769)
					player.getDialogueManager().startDialogue("SkillerCape");
				
				if (object.getId() == 2079)
					player.getDialogueManager().startDialogue("DonatorShop");
			   // end();
			  
			    if (object.getId() == 4019)//summon
			    	Summoning.infusePouches(player);
			    
			    if (object.getId() == 48496) {//dung ladder
			    	player.getControlerManager().startControler("DungeonControler");
			    }
			    if (object.getId() == 16708) {
			    	player.provideUs();
			    }
			    if (object.getId() == 1)//box 1
			    	if (player.getInventory().containsItem(6542, 1)) {
			    		player.getInventory().addItem(11339, 1);
			    		player.getInventory().deleteItem(6542, 28);
			    		player.getPackets().sendGameMessage("You should now go find santa at lumbridge.");
			    	}
			    if (object.getId() == 2273)
			    	player.getDialogueManager().startDialogue("PkPortal");
			    else if (object.getId() == 2409 && player.spokeToWarrior == true) {
			    	player.getDialogueManager().startDialogue("Shamus");
			    }

			    if(object.getId() == 1293) {
			    	System.out.println("Work in progress...");
			    }
			    
			    if (id == 8556) {
			    	//CanafisFarming.useRakeCanA(player, 714);//TODO
			    }
			    
				if (object.getId() == 15001 && player.randomRune == "Air" && player.tagged5Posts < 5) {
					player.randomRune();
					player.setNextForceTalk(new ForceTalk("Next is, "+player.randomRune));
					player.tagged5Posts++;
				} else if (object.getId() == 15003 && player.randomRune == "Earth" && player.tagged5Posts < 5) {
					player.randomRune();
					player.setNextForceTalk(new ForceTalk("Next is, "+player.randomRune));
					player.tagged5Posts++;
				} else if (object.getId() == 15005 && player.randomRune == "Fire" && player.tagged5Posts < 5) {
					player.randomRune();
					player.setNextForceTalk(new ForceTalk("Next is, "+player.randomRune));
					player.tagged5Posts++;
				} else if (object.getId() == 15007 && player.randomRune == "Nature" && player.tagged5Posts < 5) {
					player.randomRune();
					player.setNextForceTalk(new ForceTalk("Next is, "+player.randomRune));
					player.tagged5Posts++;
				} else if (object.getId() == 15009 && player.randomRune == "Water" && player.tagged5Posts < 5) {
					player.randomRune();
					player.setNextForceTalk(new ForceTalk("Next is, "+player.randomRune));
					player.tagged5Posts++;
				} else if (id == 15001 || id == 15003 || id == 15007 || id == 15009 && player.tagged5Posts >= 5) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(Settings.HOME));
					player.out("Congratulations, you have completed the random event.");
					player.getInventory().addItem(22340, 1);
				}
			
			  
			    /*
			     * 	Start of farming. Allotment A
			     */
			    
	else if (object.getId() == 8550 && player.canHarvestA == false) {
			    	FarmingManager.useRake(player, 708);
		} else if (object.getId() == 8550 && player.canHarvestA == true && player.potatoA == true) {
			if (player.getInventory().containsItem(952, 1) || player.spade == true) {
					player.setNextAnimation(new Animation(2272));
					player.getInventory().addItem(1942, 4);
					player.getSkills().addXp(Skills.FARMING, 8000);
					player.canHarvestA = false;
					player.hasHarvestedA = false;
					player.hasPlantedA = false;
					player.potatoA = false;
					player.getPackets().sendConfigByFile(708, 3);
					player.farmingStatusA = 1;
					player.farmStatus();
	    } else {
    		player.out("You need a spade to dig up your crops");
    	}
			    } else if (object.getId() == 8550 && player.canHarvestA == true && player.melonA == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
			    		player.setNextAnimation(new Animation(2272));
			    		player.getInventory().addItem(5982, 4);
			    		player.getSkills().addXp(Skills.FARMING, 18000);
			    		player.canHarvestA = false;
			    		player.hasHarvestedA = false;
			    		player.hasPlantedA = false;
			    		player.melonA = false;
			    		player.getPackets().sendConfigByFile(708, 3);
						player.farmingStatusA = 1;
						player.farmStatus();
			    } else {
		    		player.out("You need a spade to dig up your crops");
		    	}
			    } else if (object.getId() == 8550 && player.canHarvestA == true && player.sweetA == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
		    		player.setNextAnimation(new Animation(2272));
		    		player.getInventory().addItem(5986, 4);
		    		player.getSkills().addXp(Skills.FARMING, 30000);
		    		player.canHarvestA = false;
		    		player.hasHarvestedA = false;
		    		player.hasPlantedA = false;
		    		player.sweetA = false;
		    		player.getPackets().sendConfigByFile(708, 3);
					player.farmingStatusA = 1;
					player.farmStatus();
			    } else {
		    		player.out("You need a spade to dig up your crops");
		    	}
		    }
			    //Melon seeds below.
			    
			    
			    // Allotment B
			    
			    else if (object.getId() == 8551 && player.canHarvestB == false) {
			    	FarmingManager.useRakeB(player, 709);
			    } else if (object.getId() == 8551 && player.canHarvestB == true && player.potatoB == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
					player.setNextAnimation(new Animation(2272));
					player.getInventory().addItem(1942, 4);
					player.getSkills().addXp(Skills.FARMING, 8000);
					player.canHarvestB = false;
					player.hasHarvestedB = false;
					player.hasPlantedB = false;
					player.potatoB = false;
					player.getPackets().sendConfigByFile(709, 3);
					player.farmingStatusB = 1;
					player.farmStatus();
			    } else {
		    		player.out("You need a spade to dig up your crops");
		    	}
			    } else if (object.getId() == 8551 && player.canHarvestB == true && player.melonB == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
			    	player.setNextAnimation(new Animation(2272));
			    		player.getInventory().addItem(5982, 4);
			    			player.getSkills().addXp(Skills.FARMING, 18000);
			    				player.canHarvestB = false;
			    					player.hasHarvestedB = false;
			    						player.hasPlantedB = false;
			    						player.melonB = false;
			    							player.getPackets().sendConfigByFile(709, 3);
			    							player.farmingStatusB = 1;
			    							player.farmStatus();
			    } else {
		    		player.out("You need a spade to dig up your crops");
		    	}
		    } else if (object.getId() == 8551 && player.canHarvestB == true && player.sweetB == true) {
		    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
	    		player.setNextAnimation(new Animation(2272));
	    		player.getInventory().addItem(5986, 4);
	    		player.getSkills().addXp(Skills.FARMING, 30000);
	    		player.canHarvestB = false;
	    		player.hasHarvestedB = false;
	    		player.hasPlantedB = false;
	    		player.sweetB = false;
	    		player.getPackets().sendConfigByFile(709, 3);
				player.farmingStatusB = 1;
				player.farmStatus();
		    } else {
	    		player.out("You need a spade to dig up your crops");
	    	}
	    }
			    
			    //Herb Patch
	
			    else if (object.getId() == 8150 && player.canHarvestHerbA == false) {
			    	FarmingManager.useRakeH(player, 780);
			    } else if (object.getId() == 8150 && player.canHarvestHerbA == true && player.guamA == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
					player.setNextAnimation(new Animation(2272));
					player.getInventory().addItem(199, 4);
					player.getSkills().addXp(Skills.FARMING, 10000);
					player.canHarvestHerbA = false;
					player.hasHarvestedHerbA = false;
					player.hasPlantedHerb = false;
					player.guamA = false;
					player.getPackets().sendConfigByFile(780, 3);
					player.farmingStatusH = 1;
					player.farmStatus();
			    } else {
		    		player.out("You need a spade to dig up your crops");
		    	}
			    } else if (object.getId() == 8150 && player.canHarvestHerbA == true && player.snapA == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
			    	player.setNextAnimation(new Animation(2272));
			    		player.getInventory().addItem(3000, 4);
			    			player.getSkills().addXp(Skills.FARMING, 22000);
			    				player.canHarvestHerbA = false;
			    					player.hasHarvestedHerbA = false;
			    						player.hasPlantedHerb = false;
			    						player.snapA = false;
			    							player.getPackets().sendConfigByFile(780, 3);
			    							player.farmingStatusH = 1;
			    							player.farmStatus();
			    } else {
		    		player.out("You need a spade to dig up your crops");
		    	}
		    } else if (object.getId() == 8150 && player.canHarvestHerbA == true && player.torstol == true) {
		    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
		    	player.setNextAnimation(new Animation(2272));
	    		player.getInventory().addItem(219, 4);
	    			player.getSkills().addXp(Skills.FARMING, 60000);
	    				player.canHarvestHerbA = false;
	    					player.hasHarvestedHerbA = false;
	    						player.hasPlantedHerb = false;
	    						player.torstol = false;
	    							player.getPackets().sendConfigByFile(780, 3);
	    							player.farmingStatusH = 1;
	    							player.farmStatus();
		    } else {
	    		player.out("You need a spade to dig up your crops");
	    	}
		    }
			    
			    //Flower Patch
			    
			    else if (object.getId() == 7847 && player.canHarvestFlowerA == false) {
			    	FarmingManager.useRakeF(player, 728);
			    } else if (object.getId() == 7847 && player.canHarvestFlowerA == true && player.gold == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
					player.setNextAnimation(new Animation(2272));
					player.getInventory().addItem(6010, 4);
					player.getSkills().addXp(Skills.FARMING, 9000);
					player.canHarvestFlowerA = false;
					player.hasHarvestedFlowerA = false;
					player.hasPlantedFlower = false;
					player.gold = false;
					player.getPackets().sendConfigByFile(728, 3);
					player.farmingStatusF = 1;
					player.farmStatus();
			    } else {
		    		player.out("You need a spade to dig up your crops");
		    	}
			    } else if (object.getId() == 7847 && player.canHarvestFlowerA == true && player.lily == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
			    	player.setNextAnimation(new Animation(2272));
			    		player.getInventory().addItem(14583, 4);
			    			player.getSkills().addXp(Skills.FARMING, 20000);
			    				player.canHarvestFlowerA = false;
			    					player.hasHarvestedFlowerA = false;
			    						player.hasPlantedFlower = false;
			    						player.lily = false;
			    							player.getPackets().sendConfigByFile(728, 3);
			    							player.farmingStatusF = 1;
			    							player.farmStatus();
			    } else {
		    		player.out("You need a spade to dig up your crops");
		    	}
		    }
			    
			    
			    
			    //Start of Catherby farming. Allotment A
				   
		else if (object.getId() == 8553 && player.canHarvestCA == false) {
			    	CathFarming.useRakeCA(player, 711);
		} else if (object.getId() == 8553 && player.canHarvestCA == true && player.potatoCA == true) {
			if (player.getInventory().containsItem(952, 1) || player.spade == true) {
					player.setNextAnimation(new Animation(2272));
					player.getInventory().addItem(1942, 4);
					player.getSkills().addXp(Skills.FARMING, 8000);
					player.canHarvestCA = false;
					player.hasHarvestedCA = false;
					player.hasPlantedCA = false;
					player.potatoCA = false;
					player.getPackets().sendConfigByFile(711, 3);
	    } else {
    		player.out("You need a spade to dig up your crops");
    	}
			    } else if (object.getId() == 8553 && player.canHarvestCA == true && player.melonCA == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
			    		player.setNextAnimation(new Animation(2272));
			    		player.getInventory().addItem(5982, 4);
			    		player.getSkills().addXp(Skills.FARMING, 18000);
			    		player.canHarvestCA = false;
			    		player.hasHarvestedCA = false;
			    		player.hasPlantedCA = false;
			    		player.melonCA = false;
			    		player.getPackets().sendConfigByFile(711, 3);
				    } else {
			    		player.out("You need a spade to dig up your crops");
			    	}
			    } else if (object.getId() == 8553 && player.canHarvestCA == true && player.sweetCA == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
		    		player.setNextAnimation(new Animation(2272));
		    		player.getInventory().addItem(5986, 4);
		    		player.getSkills().addXp(Skills.FARMING, 30000);
		    		player.canHarvestCA = false;
		    		player.hasHarvestedCA = false;
		    		player.hasPlantedCA = false;
		    		player.sweetCA = false;
		    		player.getPackets().sendConfigByFile(711, 3);
				    } else {
			    		player.out("You need a spade to dig up your crops");
			    	}
		    }
			    //Melon seeds below.
			    
			    
			    // Allotment B
			    
			    else if (object.getId() == 8552 && player.canHarvestCB == false) {
			    	CathFarming.useRakeCB(player, 710);
			    } else if (object.getId() == 8552 && player.canHarvestCB == true && player.potatoCB == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
					player.setNextAnimation(new Animation(2272));
					player.getInventory().addItem(1942, 4);
					player.getSkills().addXp(Skills.FARMING, 8000);
					player.canHarvestCB = false;
					player.hasHarvestedCB = false;
					player.hasPlantedCB = false;
					player.potatoCB = false;
					player.getPackets().sendConfigByFile(710, 3);
			    } else {
		    		player.out("You need a spade to dig up your crops");
		    	}
			    } else if (object.getId() == 8552 && player.canHarvestCB == true && player.melonCB == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
			    	player.setNextAnimation(new Animation(2272));
			    		player.getInventory().addItem(5982, 4);
			    			player.getSkills().addXp(Skills.FARMING, 18000);
			    				player.canHarvestCB = false;
			    					player.hasHarvestedCB = false;
			    						player.hasPlantedCB = false;
			    						player.melonCB = false;
			    							player.getPackets().sendConfigByFile(710, 3);
			    } else {
		    		player.out("You need a spade to dig up your crops");
		    	}
		    } else if (object.getId() == 8552 && player.canHarvestCB == true && player.sweetCB == true) {
		    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
		    	player.setNextAnimation(new Animation(2272));
	    		player.getInventory().addItem(5986, 4);
	    			player.getSkills().addXp(Skills.FARMING, 30000);
	    				player.canHarvestCB = false;
	    					player.hasHarvestedCB = false;
	    						player.hasPlantedCB = false;
	    						player.sweetCB = false;
	    							player.getPackets().sendConfigByFile(710, 3);
		    } else {
	    		player.out("You need a spade to dig up your crops");
	    	}
		    }
			    
			    //Herb Patch
	
			    else if (object.getId() == 8151 && player.canHarvestHerbCA == false) {
			    	CathFarming.useRakeCH(player, 781);
			    } else if (object.getId() == 8151 && player.canHarvestHerbCA == true && player.guamCA == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
					player.setNextAnimation(new Animation(2272));
					player.getInventory().addItem(199, 4);
					player.getSkills().addXp(Skills.FARMING, 10000);
					player.canHarvestHerbCA = false;
					player.hasHarvestedHerbCA = false;
					player.hasPlantedHerbC = false;
					player.guamCA = false;
					player.getPackets().sendConfigByFile(781, 3);
			    	} else {
			    		player.out("You need a spade to dig up your crops");
			    	}
			    } else if (object.getId() == 8151 && player.canHarvestHerbCA == true && player.snapCA == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
			    	player.setNextAnimation(new Animation(2272));
			    		player.getInventory().addItem(3000, 4);
			    			player.getSkills().addXp(Skills.FARMING, 22000);
			    				player.canHarvestHerbCA = false;
			    					player.hasHarvestedHerbCA = false;
			    						player.hasPlantedHerbC = false;
			    						player.snapCA = false;
			    							player.getPackets().sendConfigByFile(781, 3);
			    	} else {
			    		player.out("You need a spade to dig up your crops");
			    	}
		    } else if (object.getId() == 8151 && player.canHarvestHerbCA == true && player.torstolCA == true) {
		    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
		    	player.setNextAnimation(new Animation(2272));
	    		player.getInventory().addItem(219, 4);
	    			player.getSkills().addXp(Skills.FARMING, 60000);
	    				player.canHarvestHerbCA = false;
	    					player.hasHarvestedHerbCA = false;
	    						player.hasPlantedHerbC = false;
	    						player.torstolCA = false;
	    							player.getPackets().sendConfigByFile(781, 3);
  		    		} else {
  		    				player.out("You need a spade to dig up your crops");
  		    		}
		    }
			    
			    //Flower Patch
			    
			    else if (object.getId() == 7848 && player.canHarvestFlowerCA == false) {
			    	CathFarming.useRakeCF(player, 729);
			    } else if (object.getId() == 7848 && player.canHarvestFlowerCA == true && player.goldC == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
					player.setNextAnimation(new Animation(2272));
					player.getInventory().addItem(6010, 4);
					player.getSkills().addXp(Skills.FARMING, 9000);
					player.canHarvestFlowerCA = false;
					player.hasHarvestedFlowerCA = false;
					player.hasPlantedFlowerC = false;
					player.goldC = false;
					player.getPackets().sendConfigByFile(729, 3);
			    	} else {
			    		player.out("You need a spade to dig up your crops");
			    	}
			    } else if (object.getId() == 7848 && player.canHarvestFlowerCA == true && player.lilyC == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
			    	player.setNextAnimation(new Animation(2272));
			    		player.getInventory().addItem(14583, 4);
			    			player.getSkills().addXp(Skills.FARMING, 20000);
			    				player.canHarvestFlowerCA = false;
			    					player.hasHarvestedFlowerCA = false;
			    						player.hasPlantedFlowerC = false;
			    						player.lilyC = false;
			    							player.getPackets().sendConfigByFile(729, 3);
		    	} else {
		    		player.out("You need a spade to dig up your crops");
		    	}
		    }
	
			    //End of Catherby Farming
			    
			    /**
			     * Start of Adrougne Farming
			     */
			    else if (object.getId() == 8555 && player.allotmentA == 0) { //TODO Finish all the Ardougne crops. 
			    	ArdyFarming.useRakeAA(player, 713);
			    } else if (object.getId() == 8555 && player.allotmentA == 1 && player.canHarvestAA == true) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
						player.setNextAnimation(new Animation(2272));
						player.getInventory().addItem(1942, 4);
						player.getSkills().addXp(Skills.FARMING, 8000);
					player.canHarvestAA = false;
					player.hasHarvestedAA = false;
					player.hasPlantedAA = false;
					player.allotmentA = 0;
					player.getPackets().sendConfigByFile(713, 3);
			    	} else {
			    		player.out("You need a spade to dig up your crops");
			    	}
			    } else if (object.getId() == 8555 && player.canHarvestAA == true && player.allotmentA == 2) {
			    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
			    	player.setNextAnimation(new Animation(2272));
			    		player.getInventory().addItem(5982, 4);
			    			player.getSkills().addXp(Skills.FARMING, 18000);
							player.canHarvestAA = false;
							player.hasHarvestedAA = false;
							player.hasPlantedAA = false;
							player.allotmentA = 0;
			    			player.getPackets().sendConfigByFile(713, 3);
			    } else {
		    		player.out("You need a spade to dig up your crops");
		    	}
		    } else if (object.getId() == 8555 && player.canHarvestAA == true && player.allotmentA == 3) {
		    	if (player.getInventory().containsItem(952, 1) || player.spade == true) {
		    	player.setNextAnimation(new Animation(2272));
	    		player.getInventory().addItem(5986, 4);
	    			player.getSkills().addXp(Skills.FARMING, 30000);
					player.canHarvestAA = false;
					player.hasHarvestedAA = false;
					player.hasPlantedAA = false;
					player.allotmentA = 0;
	    			player.getPackets().sendConfigByFile(713, 3);
		    } else {
	    		player.out("You need a spade to dig up your crops");
	    	}
		    }
			    
		    else if (object.getId() == 8554) {
		    	player.getPackets().sendConfigByFile(712, 73);
		    }
			    	
			    
			    
			    
			    //Below's method empty's the compost
				else if (object.getId() == 7836) {
					if (player.getInventory().containsItem(6055, 1)) {
					player.setNextAnimation(new Animation(2292));
					player.getInventory().deleteItem(6055, 1);
					player.out("You empty your weeds into the compost bin.");
					player.getPackets().sendConfigByFile(740, 15);
					player.getSkills().addXp(Skills.FARMING, 500);
					} else {
						player.getDialogueManager().startDialogue("SimpleMessage", "You can only empty weeds into the compost bin.");
					}
				}
			    
				/*else if (object.getId() == 7839) {
					if (player.getInventory().containsItem(6055, 1)) {
					player.setNextAnimation(new Animation(2292));
					player.getInventory().deleteItem(6055, 1);
					player.out("You empty your weeds into the compost bin.");
					player.getPackets().sendConfigByFile(743, 15);
					player.getSkills().addXp(Skills.FARMING, 500);
					} else {
						player.getDialogueManager().startDialogue("SimpleMessage", "You can only empty weeds into the compost bin.");
					}
				}*/
				else if (object.getId() == 7839 && player.useCompost == false) {
					player.getDialogueManager().startDialogue("SimpleMessage", "You can use compost bins to create compost, in order to grow compost you must use your weeds on the compost bin.");
				} else if (object.getId() == 7839 && player.useCompost == true) {
					player.getPackets().sendConfigByFile(743, 31);
					player.waitForComp = true;
				} else if (object.getId() == 7839 && player.waitForComp == true) {
					player.getDialogueManager().startDialogue("SimpleMessage", "You must wait for the compost to decompose.");
				}
			    
				else if (object.getId() == 7837) {
					if (player.getInventory().containsItem(6055, 1)) {
					player.setNextAnimation(new Animation(2292));
					player.getInventory().deleteItem(6055, 1);
					player.out("You empty your weeds into the compost bin.");
					player.getPackets().sendConfigByFile(741, 15);
					player.getSkills().addXp(Skills.FARMING, 500);
					} else {
						player.getDialogueManager().startDialogue("SimpleMessage", "You can only empty weeds into the compost bin.");
					}
				}
			    
				else if (object.getId() == 1292) {
				if (player.getSkills().getLevel(Skills.WOODCUTTING) < 36) {
					player.getDialogueManager().startDialogue("SimpleMessage", "You need a woodcutting level of 36 and an axe to cut this tree.");
				} else {
					if (player.spokeToShamus == true && player.getInventory().containsItem(1349, 1) || player.getInventory().containsItem(1351, 1)) {
				World.spawnNPC(655, new WorldTile(player.getX(), player.getY() + 1, 0), -1, true, true);
						}
					}
				}
			    
				else if (object.getId() == 12163 && player.cutBoat == false && player.floatBoat == false && player.paddleBoat == false) {
					player.setNextAnimation(new Animation(875));
					player.getPackets().sendConfigByFile(1839, 10); //10 for shape log
					player.cutBoat = true;
				} else if (object.getId() == 12163 && player.cutBoat == true) {
					player.getInterfaceManager().sendInterface(52);
				} else if (object.getId() == 12163 && player.floatBoat == true) {
					player.getPackets().sendConfigByFile(1839, 5);
					player.floatBoat = false;
					player.paddleBoat = true;
				} else if (object.getId() == 12163 && player.paddleBoat == true) {
					player.getInterfaceManager().sendInterface(53);
				}
			    
			    
				else if (object.getId() == 9311) {
					player.setNextWorldTile(new WorldTile(3144, 3514, 0));
				} else if (object.getId() == 9312) {
					player.setNextWorldTile(new WorldTile(3138, 3516, 0));

				}
			    
			    //Waterfall area
				else if (object.getId() == 1987) {
					player.setNextWorldTile(new WorldTile(2512, 3481, 0));
					player.out("The Log Raft crashes into the rocks.");
				} else if (object.getId() == 10283) {
					player.setNextWorldTile(new WorldTile(2513, 3468, 0));
					player.out("You manage to swim to the other side.");
				} else if (object.getId() == 37247) {
					player.setNextWorldTile(new WorldTile(2575, 9861, 0));
					player.out("You enter the waterfall.");
				} else if (object.getId() == 32711) {
					player.setNextWorldTile(new WorldTile(2511, 3463, 0));
					player.out("You exit the waterfall.");
				} else if (object.getId() == 2022) {
					player.setNextWorldTile(new WorldTile(2528, 3414, 0));
					player.out("You exit the barrel and swim to shore.");
				} else if (object.getId() == 2020) {
					player.setNextWorldTile(new WorldTile(2511, 3463, 0));
					player.out("You climb down the tree onto the waterfall's edge.");
				}
			    
			    
			    //Activate Lodestones
				else if (object.getId() == 69829) {
					player.alkarid = true;
					player.out("You have activated the Alkarid lodestone.");
					player.refreshLodestoneNetwork();
				} else if (object.getId() == 69833) {
					player.draynor = true;
					player.out("You have activated the Draynor Village lodestone.");
					player.refreshLodestoneNetwork();
				} else if (object.getId() == 69837) {
					player.port = true;
					player.out("You have activated the Port Sarim lodestone.");
					player.refreshLodestoneNetwork();
				} else if (object.getId() == 69835) {
					player.falador = true;
					player.out("You have activated the Falador lodestone.");
					player.refreshLodestoneNetwork();
				} else if (object.getId() == 69840) {
					player.varrock = true;
					player.out("You have activated the Varrock lodestone.");
					player.refreshLodestoneNetwork();
				} else if (object.getId() == 69834) {
					player.edge = true;
					player.out("You have activated the Edgeville lodestone.");
					player.refreshLodestoneNetwork();
				} else if (object.getId() == 69831) {
					player.burth = true;
					player.out("You have activated the Burthope lodestone.");
					player.refreshLodestoneNetwork();
				} else if (object.getId() == 69839) {
					player.tav = true;
					player.out("You have activated the Taverly lodestone.");
					player.refreshLodestoneNetwork();
				} else if (object.getId() == 69832) {
					player.cath = true;
					player.out("You have activated the Catherby lodestone.");
					player.refreshLodestoneNetwork();
				} else if (object.getId() == 69838) {
					player.seers = true;
					player.out("You have activated the Seers Village lodestone.");
					player.refreshLodestoneNetwork();
				} else if (object.getId() == 69830) {
					player.adrougne = true;
					player.out("You have activated the Adougne lodestone.");
					player.refreshLodestoneNetwork();
				} else if (object.getId() == 69841) {
					player.yanille = true;
					player.out("You have activated the Yanille lodestone.");
					player.refreshLodestoneNetwork();
				}

			    
			    
			    //Fally Tree Patch
			   
			    else if (object.getId() == 8389 && player.canHarvestTreeA == false) {
			    	FarmingManager.useRake(player, 701);
			   } else if (object.getId() == 8389 && player.canHarvestTreeA == true && player.yew == true) {
						player.getActionManager().setAction(new Woodcutting(object,	TreeDefinitions.YEW_TREE));
			    } else if (object.getId() == 8389 && player.canHarvestTreeA == true && player.magic == true) {
						player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAGIC_TREE));
			 /*   } else if (object.getId() == 8389 && player.canHarvestTreeA == true && player.yew == true) {
			    	if (player.farmAxe() == false) {
			    		player.out("You need an axe to cut this tree.");
			    	} else {
					player.setNextAnimation(new Animation(867)); //Players get the advantage of chopping magic trees fast when farming
					player.out("You Chop the Yew Tree.");
					player.getInventory().addItem(1515, 1);
					player.getSkills().addXp(Skills.WOODCUTTING, 30); // 54k
					player.chop++;
				    if (player.chop >= 26) {
				    	player.out("The tree collapsed as you chopped all the wood.");
						player.getPackets().sendConfigByFile(701, 3);
						player.chop = 0;
						player.hasPlantedTree = false;
				    }
					}
			    } else if (object.getId() == 8389 && player.canHarvestTreeA == true && player.magic == true) {
			    	if (player.farmAxe() == false) {
			    		player.out("You need an axe to cut this tree.");
			    	} else {
					player.setNextAnimation(new Animation(867)); //Players get the advantage of chopping magic trees fast when farming
					player.out("You Chop the Magic Tree.");
					player.getInventory().addItem(1513, 1);
					player.getSkills().addXp(Skills.WOODCUTTING, 40); //77k
					player.chop++;
				    if (player.chop >= 36) {
				    	player.out("Your tree collapsed as you chopped all the wood.");
						player.getPackets().sendConfigByFile(701, 3);
						player.chop = 0;
						player.hasPlantedTree = false;
				    	}
					}*/
			    }
	 
			    
			    //End of farming!
			    	
			    
				if (object.getId() == 7133)//nature
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2398, 4841, 0));//not done
				if (object.getId() == 7134)//chaos
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2269, 4843, 0));//not done
				if (object.getId() == 7135)//law
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2471, 4838, 0));//done
				if (object.getId() == 7136)//death
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2207, 4836, 0));//not done
				if (object.getId() == 7137)//water
					player.getPackets().sendGameMessage("A strange power blocks your entrance");
				if (object.getId() == 7138)//soul
					player.getPackets().sendGameMessage("A strange power blocks your entrance");
				if (object.getId() == 7139)//air
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2842, 4835, 0));//done
				if (object.getId() == 7140)//mind
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2796, 4818, 0));//done
				if (object.getId() == 7131)//body
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2523, 4833, 0));//not done
				if (object.getId() == 7130)//earth
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2660, 4839, 0));//not done
				if (object.getId() == 7129)//fire
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2584, 4836, 0));//not done
				if (object.getId() == 7141)//blood
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2468, 4889, 1));//not done
				if (object.getId() == 7132)//cosmic
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2162, 4833, 0));//not done
				if (object.getId() == 2366)
					player.getInterfaceManager().sendInterface(1245);
					player.getPackets().sendIComponentText(1245, 330, "Daily Challenges");
					player.getPackets().sendIComponentText(1245, 13, "<u>Challenges</u>");
					player.getPackets().sendIComponentText(1245, 14, "");
					player.getPackets().sendIComponentText(1245, 15, "Amount of logs cut: " +player.wc+ "/350");
					player.getPackets().sendIComponentText(1245, 16, "Amount of logs burnt: " +player.logs+ "/250");
					player.getPackets().sendIComponentText(1245, 17, "Amount of crystal keys used:"  +player.chest+ "/15");
					player.getPackets().sendIComponentText(1245, 18, "Amount of potato's planted:"  +player.farm+ "/1000");
					player.getPackets().sendIComponentText(1245, 19, "Amount of fish caught:" +player.fish+ "/750");
					player.getPackets().sendIComponentText(1245, 20, "");
					player.getPackets().sendIComponentText(1245, 21, "");
					player.getPackets().sendIComponentText(1245, 22, "");
					player.getPackets().sendIComponentText(1245, 23, "<u>I will be trying to release challenges every day.</u>");
				if (object.getId() == 2408)
				player.useStairs(828, new WorldTile(2828, 9767, 0), 1, 2);
			
				if (object.getId() == 19205)
					Hunter.createLoggedObject(player, object, true);
				HunterNPC hunterNpc = HunterNPC.forObjectId(id);
				if (hunterNpc != null) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.setNextAnimation(hunterNpc.getEquipment().getPickUpAnimation());
							player.getInventory().getItems().addAll(hunterNpc.getItems());
						player.getInventory().addItem(hunterNpc.getEquipment().getId(), 1);
						player.getSkills().addXp(Skills.HUNTER, hunterNpc.getXp());
						player.getInventory().refresh();
					} else {
						player.getPackets().sendGameMessage("This isn't your trap.");
					}
				} else if (id == HunterEquipment.BOX.getObjectId() || id == 19192) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.setNextAnimation(new Animation(5208));
						player.getInventory().addItem(HunterEquipment.BOX.getId(), 1);
						player.getInventory().refresh();
					} else
						player.getPackets().sendGameMessage("This isn't your trap.");
				} else if (id == HunterEquipment.BRID_SNARE.getObjectId() || id == 19174) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.setNextAnimation(new Animation(5207));
						player.getInventory().addItem(HunterEquipment.BRID_SNARE.getId(), 1);
						player.getInventory().refresh();
					} else
						player.getPackets().sendGameMessage("This isn't your trap.");
				} else if (id == 2350
						&& (object.getX() == 3352 && object.getY() == 3417 && object
						.getPlane() == 0))
					player.useStairs(832, new WorldTile(3177, 5731, 0), 1, 2);
				else if (id == 2353
						&& (object.getX() == 3177 && object.getY() == 5730 && object
						.getPlane() == 0))
					player.useStairs(828, new WorldTile(3353, 3416, 0), 1, 2);
				else if (id == 11554 || id == 11552)
					player.getPackets().sendGameMessage(
							"That rock is currently unavailable.");
				else if (id == 38279)
					player.getDialogueManager().startDialogue("RunespanPortalD");
				else if (id == 2491)
					player.getActionManager()
					.setAction(
							new EssenceMining(
									object,
									player.getSkills().getLevel(
											Skills.MINING) < 30 ? EssenceDefinitions.Rune_Essence
													: EssenceDefinitions.Pure_Essence));
				else if (id == 2478)
					Runecrafting.craftEssence(player, 556, 1, 5, false, 11, 2,
							22, 3, 34, 4, 44, 5, 55, 6, 66, 7, 77, 88, 9, 99,
							10);
				else if (id == 2479)
					Runecrafting.craftEssence(player, 558, 2, 5.5, false, 14,
							2, 28, 3, 42, 4, 56, 5, 70, 6, 84, 7, 98, 8);
				else if (id == 2480)
					Runecrafting.craftEssence(player, 555, 5, 6, false, 19, 2,
							38, 3, 57, 4, 76, 5, 95, 6);
				else if (id == 2481)
					Runecrafting.craftEssence(player, 557, 9, 6.5, false, 26,
							2, 52, 3, 78, 4);
				else if (id == 2482)
					Runecrafting.craftEssence(player, 554, 14, 7, false, 35, 2,
							70, 3);
				else if (id == 2483)
					Runecrafting.craftEssence(player, 559, 20, 7.5, false, 46,
							2, 92, 3);
				else if (id == 2484)
					Runecrafting.craftEssence(player, 564, 27, 8, true, 59, 2);
				else if (id == 2487)
					Runecrafting
					.craftEssence(player, 562, 35, 8.5, true, 74, 2);
				else if (id == 17010)
					Runecrafting.craftEssence(player, 9075, 40, 8.7, true, 82,
							2);
				else if (id == 2486)
					Runecrafting.craftEssence(player, 561, 45, 9, true, 91, 2);
				else if (id == 2485)
					Runecrafting.craftEssence(player, 563, 50, 9.5, true);
				else if (id == 2488)
					Runecrafting.craftEssence(player, 560, 65, 10, true);
				else if (id == 30624)
					Runecrafting.craftEssence(player, 565, 77, 10.5, true);
				else if (id == 2452) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.AIR_TIARA
							|| hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1438, 1))
						Runecrafting.enterAirAltar(player);
				} else if (id == 3784) {
					int dl = player.getSkills().getLevel(24);
						int xp = 0;
						int fn = 0;
						if (dl >= 110){
						xp = 1800;
						} else if (dl >= 90){
						xp = 900;
						} else if (dl >= 75){
						xp = 850;
						} else if (dl >= 50){
						xp= 500;
						} else if (dl >= 25){
						xp = 250;
						} else if (dl >= 10){
						xp = 100;
						} else {
						xp = 100;
						}
						fn = (xp);
						player.getSkills().addXp(24, xp);
                                         player.stopAll();
			                 player.getAppearence().generateAppearenceData();
					player.dungPoints += (xp*60/10);
			                player.getPackets().sendGameMessage("<col=960000>You've completed the dungeon and earned Dungeoneering experience and Dungeonering Tokens.");
			                 player.getControlerManager().forceStop();
					player.setNextWorldTile(Settings.DUNG_DOWN);
				/*} else if (id == 4878) {
					player.getPackets().sendGameMessage("You successfully thieve from the stall"); //message can change
					player.getInventory().addItem(995, 100000); //The amount of GP it gives.
                    player.setNextAnimation(new Animation(881));
					player.getSkills().addXp(17, 10); //the exp multiplyer that it gives.*/
				} else if (id == 12352)
					if (player.getInventory().containsItem(1351, 1) || player.getInventory().containsItem(1349, 1)) {
					player.addWalkSteps(2859, 9741, -1, false);
					} else {
					player.getPackets().sendGameMessage("You need a bronze/iron axe in your inventory to pass");
				} else if (id == 3443)
					if (player.getSkills().getLevel(Skills.SLAYER) < 52) {
						player.getPackets().sendGameMessage(
								"You need a level of 52 slayer to enter this room.");
					return;
				} else {
					player.addWalkSteps(3427, 3548, -1, false);
				} else if (id == 2406)
					if (player.getEquipment().getWeaponId() == 772 && player.spokeToWarrior == true && player.spokeToShamus == true && player.spokeToMonk == true) {
						player.setNextWorldTile(new WorldTile(2383, 4458, player.getPlane()));
						player.lostCity();
				} else {
				//	player.getPackets().sendGameMessage("You need to wield the dramen staff to enter Zanaris");
				} else if (id == 11005)
					if (player.getSkills().getLevel(Skills.SLAYER) < 85) {
						player.getPackets().sendGameMessage(
								"You need a level of 85 slayer to enter this room.");
					return;
				} else {
					player.addWalkSteps(3411, 3568, -1, false);
				} else if (id == 12351)
					if (player.getSkills().getLevel(Skills.SLAYER) < 90) {
						player.getPackets().sendGameMessage(
								"You need a level of 90 slayer to enter this room.");
					return;
				} else {
					player.addWalkSteps(3427, 3573, -1, false);
				} else if (id == 2455) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.EARTH_TIARA
							|| hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1440, 1))
						Runecrafting.enterEarthAltar(player);
				} else if (id == 2456) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.FIRE_TIARA
							|| hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1442, 1))
						Runecrafting.enterFireAltar(player);
				} else if (id == 2454) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.WATER_TIARA
							|| hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1444, 1))
						Runecrafting.enterWaterAltar(player);
				} else if (id == 2457) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.BODY_TIARA
							|| hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1446, 1))
						Runecrafting.enterBodyAltar(player);
				} else if (id == 2453) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.MIND_TIARA
							|| hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1448, 1))
						Runecrafting.enterMindAltar(player);
				} else if (id == 43804) { //spec restore
					player.getCombatDefinitions().resetSpecialAttack();
					player.getPackets().sendGameMessage("You recharge your special attack.");
				} else if (id == 47120) { // zaros altar
					// recharge if needed
					if (player.getPrayer().getPrayerpoints() < player
							.getSkills().getLevelForXp(Skills.PRAYER) * 10) {
						player.lock(12);
						player.setNextAnimation(new Animation(12563));
						player.getPrayer().setPrayerpoints(
								(int) ((player.getSkills().getLevelForXp(
										Skills.PRAYER) * 10) * 1.15));
					}
					player.getDialogueManager().startDialogue("ZarosAltar");
				} else if (id == 19222) 
					Falconry.beginFalconry(player);
				else if (id == 36786)
					player.getDialogueManager().startDialogue("Banker", 4907);
				else if (id == 42377 || id == 42378)
					player.getDialogueManager().startDialogue("Banker", 2759);
				else if (id == 42217 || id == 782 || id == 34752)
					player.getDialogueManager().startDialogue("Banker", 553);
				else if (id == 57437)
					player.getBank().openBank();
				else if (id == 42425 && object.getX() == 3220
						&& object.getY() == 3222) { // zaros portal
					player.useStairs(10256, new WorldTile(3353, 3416, 0), 4, 5,
							"And you find yourself into a digsite.");
					player.addWalkSteps(3222, 3223, -1, false);
					player.getPackets().sendGameMessage(
							"You examine portal and it aborves you...");
				} else if (id == 9356) 
					FightCaves.enterFightCaves(player);
				else if (id == 68107)
					FightKiln.enterFightKiln(player, false);
				else if (id == 68223)
					FightPits.enterLobby(player, false);
				else if (id == 46500 && object.getX() == 3351
						&& object.getY() == 3415) { // zaros portal
					player.useStairs(-1, new WorldTile(
							Settings.RESPAWN_PLAYER_LOCATION.getX(),
							Settings.RESPAWN_PLAYER_LOCATION.getY(),
							Settings.RESPAWN_PLAYER_LOCATION.getPlane()), 2, 3,
							"You found your way back to home.");
					player.addWalkSteps(3351, 3415, -1, false);
				} else if (id == 32462) {
                   	 			CrystalChest.searchChest(player);
						player.resetWalkSteps();
				} else if (id == 9293) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 70) {
						player.getPackets()
						.sendGameMessage(
								"You need an agility level of 70 to use this obstacle.",
								true);
						return;
					}
					int x = player.getX() == 2886 ? 2892 : 2886;
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {
							player.setNextAnimation(new Animation(844));
							if (count++ == 1)
								stop();
						}

					}, 0, 0);
					player.setNextForceMovement(new ForceMovement(
							new WorldTile(x, 9799, 0), 3,
							player.getX() == 2886 ? 1 : 3));
					player.useStairs(-1, new WorldTile(x, 9799, 0), 3, 4);
				}else if (id == 29370 && (object.getX() == 3150 || object.getX() == 3153) && object.getY() == 9906) { // edgeville dungeon cut
					if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
						player.getPackets().sendGameMessage("You need an agility level of 53 to use this obstacle.");
						return;
					}
					final boolean running = player.getRun();
					player.setRunHidden(false);
					player.lock(8);
					player.addWalkSteps(x == 3150 ?  3155 : 3149, 9906, -1, false);
					player.getPackets().sendGameMessage("You pulled yourself through the pipes.", true);
					WorldTasksManager.schedule(new WorldTask() {
						boolean secondloop;

						@Override
						public void run() {
							if (!secondloop) {
								secondloop = true;
								player.getAppearence().setRenderEmote(295);
							} else {
								player.getAppearence().setRenderEmote(-1);
								player.setRunHidden(running);
								player.getSkills().addXp(Skills.AGILITY, 7);
								stop();
							}
						}
					}, 0, 5);
				}
				//start forinthry dungeon
				else if (id == 18341 && object.getX() == 3036 && object.getY() == 10172) 
					player.useStairs(-1, new WorldTile(3039, 3765, 0), 0, 1);
				else if (id == 20599 && object.getX() == 3038 && object.getY() == 3761) 
					player.useStairs(-1, new WorldTile(3037, 10171, 0), 0, 1);
				else if (id == 18342 && object.getX() == 3075 && object.getY() == 10057) 
					player.useStairs(-1, new WorldTile(3071, 3649, 0), 0, 1);
				else if (id == 20600 && object.getX() == 3072 && object.getY() == 3648)
					player.useStairs(-1, new WorldTile(3077, 10058, 0), 0, 1);
				//nomads requiem
				//else if (id == 18425 && !player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM))
				//	NomadsRequiem.enterNomadsRequiem(player);	 
				else if (id == 42219)  {
					player.useStairs(-1, new WorldTile(1886, 3178, 0), 0, 1);
					if(player.getQuestManager().getQuestStage(Quests.NOMADS_REQUIEM) == -2) //for now, on future talk with npc + quest reqs
						player.getQuestManager().setQuestStageAndRefresh(Quests.NOMADS_REQUIEM, 0);
				} else if (id == 8689) 
					player.getActionManager().setAction(new CowMilkingAction());
				else if (id == 42220) 
					player.useStairs(-1, new WorldTile(3082, 3475, 0), 0, 1);
				//start falador mininig
				else if (id == 30942 && object.getX() == 3019 && object.getY() == 3450) 
					player.useStairs(828, new WorldTile(3020, 9850, 0), 1, 2);
				else if (id == 6226 && object.getX() == 3019 && object.getY() == 9850) 
					player.useStairs(833, new WorldTile(3018, 3450, 0), 1, 2);
				else if (id == 31002 && player.getQuestManager().completedQuest(Quests.PERIL_OF_ICE_MONTAINS)) 
					player.useStairs(833, new WorldTile(2998, 3452, 0), 1, 2);
				else if (id == 31012 && player.getQuestManager().completedQuest(Quests.PERIL_OF_ICE_MONTAINS)) 
					player.useStairs(828, new WorldTile(2996, 9845, 0), 1, 2);
				else if (id == 30943 && object.getX() == 3059 && object.getY() == 9776) 
					player.useStairs(-1, new WorldTile(3061, 3376, 0), 0, 1);
				else if (id == 30944 && object.getX() == 3059 && object.getY() == 3376) 
					player.useStairs(-1, new WorldTile(3058, 9776, 0), 0, 1);
				else if (id == 2112 && object.getX() == 3046 && object.getY() == 9756) {
					if(player.getSkills().getLevelForXp(Skills.MINING) < 60) {
						player.getDialogueManager().startDialogue("SimpleNPCMessage", MiningGuildDwarf.getClosestDwarfID(player),"Sorry, but you need level 60 Mining to go in there.");
						return;
					}
					WorldObject openedDoor = new WorldObject(object.getId(),
							object.getType(), object.getRotation() - 1,
							object.getX() , object.getY() + 1, object.getPlane());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(
								3046, player.getY() > object.getY() ? object.getY()
										: object.getY() + 1 , -1, false);
					}
				}else if (id == 2113) {
					if(player.getSkills().getLevelForXp(Skills.MINING) < 60) {
						player.getDialogueManager().startDialogue("SimpleNPCMessage", MiningGuildDwarf.getClosestDwarfID(player),"Sorry, but you need level 60 Mining to go in there.");
						return;
					}
					player.useStairs(-1, new WorldTile(3021, 9739, 0), 0, 1);
				}else if (id == 6226  && object.getX() == 3019 && object.getY() == 9740) 
					player.useStairs(828, new WorldTile(3019, 3341, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3019 && object.getY() == 9738) 
					player.useStairs(828, new WorldTile(3019, 3337, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3018 && object.getY() == 9739) 
					player.useStairs(828, new WorldTile(3017, 3339, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3020 && object.getY() == 9739) 
					player.useStairs(828, new WorldTile(3021, 3339, 0), 1, 2);
				else if (id == 30963)
					player.getBank().openBank();
				else if (id == 6045)
					player.getPackets().sendGameMessage("You search the cart but find nothing.");
				else if (id == 5906) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 42) {
						player.getPackets().sendGameMessage("You need an agility level of 42 to use this obstacle.");
						return;
					}
					player.lock();
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {
							if(count == 0) {
								player.setNextAnimation(new Animation(2594));
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -2 : +2), object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							}else if (count == 2) {
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -2 : +2), object.getY(), 0);
								player.setNextWorldTile(tile);
							}else if (count == 5) {
								player.setNextAnimation(new Animation(2590));
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -5 : +5), object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							}else if (count == 7) {
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -5 : +5), object.getY(), 0);
								player.setNextWorldTile(tile);
							}else if (count == 10) {
								player.setNextAnimation(new Animation(2595));
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -6 : +6), object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							}else if (count == 12) {						 
								WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -6 : +6), object.getY(), 0);
								player.setNextWorldTile(tile);
							}else if (count == 14) {
								stop();
								player.unlock();
							}
							count++;
						}

					}, 0, 0);
					//BarbarianOutpostAgility start
				}else if (id == 20210) 
					BarbarianOutpostAgility.enterObstaclePipe(player, object);
				else if (id == 43526)
					BarbarianOutpostAgility.swingOnRopeSwing(player, object);
				else if (id == 43595 && x == 2550 && y == 3546)
					BarbarianOutpostAgility.walkAcrossLogBalance(player, object);
				else if (id == 20211 && x == 2538 && y == 3545)
					BarbarianOutpostAgility.climbObstacleNet(player, object);
				else if (id == 2302 && x == 2535 && y == 3547)
					BarbarianOutpostAgility.walkAcrossBalancingLedge(player, object);
				else if (id == 1948)
					BarbarianOutpostAgility.climbOverCrumblingWall(player, object);
				else if (id == 43533)
					BarbarianOutpostAgility.runUpWall(player, object);
				else if (id == 43597) 
					BarbarianOutpostAgility.climbUpWall(player, object);
				else if (id == 43587)
					BarbarianOutpostAgility.fireSpringDevice(player, object);
				else if (id == 43527)
					BarbarianOutpostAgility.crossBalanceBeam(player, object);
				else if (id == 43531)
					BarbarianOutpostAgility.jumpOverGap(player, object);
				else if (id == 43532)
					BarbarianOutpostAgility.slideDownRoof(player, object);

				//rock living caverns
				else if (id == 45077) {
					player.lock();
					if(player.getX() != object.getX() || player.getY() != object.getY())
						player.addWalkSteps(object.getX(), object.getY(), -1, false);
					WorldTasksManager.schedule(new WorldTask() {

						private int count;
						@Override
						public void run() {
							if(count == 0) {
								player.setNextFaceWorldTile(new WorldTile(object.getX() -1, object.getY(), 0));
								player.setNextAnimation(new Animation(12216));
								player.unlock();
							}else if(count == 2) {
								player.setNextWorldTile(new WorldTile(3651, 5122, 0));
								player.setNextFaceWorldTile(new WorldTile(3651, 5121, 0));
								player.setNextAnimation(new Animation(12217));
							}else if (count == 3) {
								//find emote
								//player.getPackets().sendObjectAnimation(new WorldObject(45078, 0, 3, 3651, 5123, 0), new Animation(12220));
							}else if(count == 5) {
								player.unlock();
								stop();
							}
							count++;
						}

					}, 1, 0);
				}else if (id == 45076)
					player.getActionManager().setAction(new Mining(object, RockDefinitions.LRC_Gold_Ore));
				else if (id == 5999)
					player.getActionManager().setAction(new Mining(object, RockDefinitions.LRC_Coal_Ore));
				else if (id == 45078)
					player.useStairs(2413, new WorldTile(3012, 9832, 0), 2, 2);
				else if (id == 45079)
					player.getBank().openDepositBox();
				//champion guild
				else if (id == 24357 && object.getX() == 3188 && object.getY() == 3355) 
					player.useStairs(-1, new WorldTile(3189, 3354, 1), 0, 1);
				else if (id == 24359 && object.getX() == 3188 && object.getY() == 3355) 
					player.useStairs(-1, new WorldTile(3189, 3358, 0), 0, 1);
				else if (id == 1805 && object.getX() == 3191 && object.getY() == 3363) {
					WorldObject openedDoor = new WorldObject(object.getId(),
							object.getType(), object.getRotation() - 1,
							object.getX() , object.getY(), object.getPlane());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(
								3191, player.getY() >= object.getY() ? object.getY() - 1
										: object.getY() , -1, false);
						if(player.getY() >= object.getY())
							player.getDialogueManager().startDialogue("SimpleNPCMessage", 198, "Greetings bolt adventurer. Welcome to the guild of", "Champions.");
					}
				}
				//start of varrock dungeon
				else if (id == 29355 && object.getX() == 3230 && object.getY() == 9904) //varrock dungeon climb to bear
					player.useStairs(828, new WorldTile(3229, 3503, 0), 1, 2);
				else if (id == 24264)
					player.useStairs(833, new WorldTile(3229, 9904, 0), 1, 2);
				else if (id == 24366)
					player.useStairs(828, new WorldTile(3237, 3459, 0), 1, 2);
				else if (id == 882 && object.getX() == 3237 && object.getY() == 3458) 
					player.useStairs(833, new WorldTile(3237, 9858, 0), 1, 2);
				else if (id == 29355 && object.getX() == 3097 && object.getY() == 9867) //edge dungeon climb
					player.useStairs(828, new WorldTile(3096, 3468, 0), 1, 2);
				else if (id == 26934)
					player.useStairs(833, new WorldTile(3097, 9868, 0), 1, 2);
				else if (id == 29355 && object.getX() == 3088 && object.getY() == 9971)
					player.useStairs(828, new WorldTile(3087, 3571, 0), 1, 2);
				else if (id == 65453)
					player.useStairs(833, new WorldTile(3089, 9971, 0), 1, 2);
				else if (id == 12389 && object.getX() == 3116 && object.getY() == 3452)
					player.useStairs(833, new WorldTile(3117, 9852, 0), 1, 2);
				else if (id == 29355 && object.getX() == 3116 && object.getY() == 9852)
					player.useStairs(833, new WorldTile(3115, 3452, 0), 1, 2);
				else if (id == 69526)
					GnomeAgility.walkGnomeLog(player);
				else if (id == 69383)
					GnomeAgility.climbGnomeObstacleNet(player);
				else if (id == 69508)
					GnomeAgility.climbUpGnomeTreeBranch(player);
				else if (id == 69506)
					GnomeAgility.climbUpGnomeTreeBranch2(player);
				else if (id == 69514)
					GnomeAgility.RunGnomeBoard2(player, object);
				else if (id == 24596)
					GnomeAgility.PreSwing(player, object);
				else if (id == 69389)
					GnomeAgility.JumpDown(player, object);
				else if (id == 2312)
					GnomeAgility.walkGnomeRope(player);
				else if (id == 4059)
					GnomeAgility.walkBackGnomeRope(player);
				else if (id == 69507)
					GnomeAgility.climbDownGnomeTreeBranch(player);
				else if (id == 69384)
					GnomeAgility.climbGnomeObstacleNet2(player);
				else if (id == 69377 || id == 69378)
					GnomeAgility.enterGnomePipe(player, object.getX(), object.getY());
				else if (Wilderness.isDitch(id)) {// wild ditch
					player.getDialogueManager().startDialogue(
							"WildernessDitch", object);
				} else if (id == 42611) {// Magic Portal
					player.getDialogueManager().startDialogue("MagicPortal");
				} else if (object.getDefinitions().name.equalsIgnoreCase("Obelisk") && object.getY() > 3525) {
					//Who the fuck removed the controler class and the code from SONIC!!!!!!!!!!
					//That was an hour of collecting coords :fp: Now ima kill myself.
				} else if (id == 27254) {// Edgeville portal
					player.getPackets().sendGameMessage(
							"You enter the portal...");
					player.useStairs(10584, new WorldTile(3087, 3488, 0), 2, 3,
							"..and are transported to Edgeville.");
					player.addWalkSteps(1598, 4506, -1, false);
				} else if (id == 12202) {// mole entrance
					if(!player.getInventory().containsItem(952, 1)) {
						player.getPackets().sendGameMessage("You need a spade to dig this.");
						return;
					}
					if(player.getX() != object.getX() || player.getY() != object.getY()) {
						player.lock();
						player.addWalkSteps(object.getX(), object.getY());
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								InventoryOptionsHandler.dig(player);
							}

						}, 1);
					}else
						InventoryOptionsHandler.dig(player);
				} else if (id == 12230 && object.getX() == 1752 && object.getY() == 5136) {// mole exit 
					player.setNextWorldTile(new WorldTile(2986, 3316, 0));
				} else if (id == 15522) {// portal sign
					if (player.withinDistance(new WorldTile(1598, 4504, 0), 1)) {// PORTAL
						// 1
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13,
								"Edgeville");
						player.getPackets()
						.sendIComponentText(
								327,
								14,
								"This portal will take you to edgeville. There "
										+ "you can multi pk once past the wilderness ditch.");
					}
					if (player.withinDistance(new WorldTile(1598, 4508, 0), 1)) {// PORTAL
						// 2
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13,
								"Mage Bank");
						player.getPackets()
						.sendIComponentText(
								327,
								14,
								"This portal will take you to the mage bank. "
										+ "The mage bank is a 1v1 deep wilderness area.");
					}
					if (player.withinDistance(new WorldTile(1598, 4513, 0), 1)) {// PORTAL
						// 3
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13,
								"Magic's Portal");
						player.getPackets()
						.sendIComponentText(
								327,
								14,
								"This portal will allow you to teleport to areas that "
										+ "will allow you to change your magic spell book.");
					}
				} else if (id == 38811 || id == 37929) {// corp beast
					if (object.getX() == 2971 && object.getY() == 4382)
						player.getInterfaceManager().sendInterface(650);
					else if (object.getX() == 2918 && object.getY() == 4382) {
						player.stopAll();
						player.setNextWorldTile(new WorldTile(
								player.getX() == 2921 ? 2917 : 2921, player
										.getY(), player.getPlane()));
					}
				} else if (id == 37928 && object.getX() == 2883
						&& object.getY() == 4370) {
					player.stopAll();
					player.setNextWorldTile(new WorldTile(3214, 3782, 0));
					player.getControlerManager().startControler("Wilderness");
				} else if (id == 38815 && object.getX() == 3209
						&& object.getY() == 3780 && object.getPlane() == 0) {
					if (player.getSkills().getLevelForXp(Skills.WOODCUTTING) < 37
							|| player.getSkills().getLevelForXp(Skills.MINING) < 45
							|| player.getSkills().getLevelForXp(
									Skills.SUMMONING) < 23
									|| player.getSkills().getLevelForXp(
											Skills.FIREMAKING) < 47
											|| player.getSkills().getLevelForXp(Skills.PRAYER) < 55) {
						player.getPackets()
						.sendGameMessage(
								"You need 23 Summoning, 37 Woodcutting, 45 Mining, 47 Firemaking and 55 Prayer to enter this dungeon.");
						return;
					}
					player.stopAll();
					player.setNextWorldTile(new WorldTile(2885, 4372, 2));
					player.getControlerManager().forceStop();
				} else if(id == 48803 && player.isKalphiteLairSetted()) {
					player.setNextWorldTile(new WorldTile(3508, 9494, 0));
				} else if(id == 48802 && player.isKalphiteLairEntranceSetted()) {
					player.setNextWorldTile(new WorldTile(3483, 9510, 2));
				} else if(id == 3829) {
					if(object.getX() == 3483 && object.getY() == 9510) {
						player.useStairs(828, new WorldTile(3226, 3108, 0), 1, 2);
					}
				} else if(id == 3832) {
					if(object.getX() == 3508 && object.getY() == 9494) {
						player.useStairs(828, new WorldTile(3509, 9496, 2), 1, 2);
					}
				} else if (id == 9369) {
					player.getControlerManager().startControler("FightPits");
				  } else if (id == 50205){
					  Summoning.infusePouches(player);
				} else if (id == 54019 || id == 54020 || id == 55301)
					PkRank.showRanks(player);
				else if (id == 1817 && object.getX() == 2273
						&& object.getY() == 4680) { // kbd lever
					Magic.pushLeverTeleport(player, new WorldTile(3067, 10254,
							0));
				} else if (id == 1816 && object.getX() == 3067
						&& object.getY() == 10252) { // kbd out lever
					Magic.pushLeverTeleport(player,
							new WorldTile(2273, 4681, 0));
				} else if (id == 32015 && object.getX() == 3069
						&& object.getY() == 10256) { // kbd stairs
					player.useStairs(828, new WorldTile(3017, 3848, 0), 1, 2);
					player.getControlerManager().startControler("Wilderness");
				} else if (id == 1765 && object.getX() == 3017
						&& object.getY() == 3849) { // kbd out stairs
					player.stopAll();
					player.setNextWorldTile(new WorldTile(3069, 10255, 0));
					player.getControlerManager().forceStop();
				} else if (id == 14315) {
					player.getControlerManager().startControler("PestControlLobby", 1);
				/*} else if (id == 14314) {
					player.setNextWorldTile(new WorldTile(2657, 2639, 0));
					player.getPackets().sendGameMessage("You exit the lander.", true);*/
				} else if (id == 5959) {
					Magic.pushLeverTeleport(player,
							new WorldTile(2539, 4712, 0));
				} else if (id == 5960) {
					Magic.pushLeverTeleport(player,
							new WorldTile(3089, 3957, 0));
				} else if (id == 1814) {
					Magic.pushLeverTeleport(player,
							new WorldTile(3155, 3923, 0));
				} else if (id == 1815) {
					Magic.pushLeverTeleport(player,
							new WorldTile(2561, 3311, 0));
				} else if (id == 62675)
					player.getCutscenesManager().play("DTPreview");
				else if (id == 62681)
					player.getDominionTower().viewScoreBoard();
				else if (id == 62678 || id == 62679)
					player.getDominionTower().openModes();
				else if (id == 62688)
					player.getDialogueManager().startDialogue("DTClaimRewards");
				else if (id == 62677)
					player.getDominionTower().talkToFace();
				else if (id == 62680)
					player.getDominionTower().openBankChest();
				else if (id == 48797)
					player.useStairs(-1, new WorldTile(3877, 5526, 1), 0, 1);
				else if (id == 48798)
					player.useStairs(-1, new WorldTile(3246, 3198, 0), 0, 1);
				else if (id == 48678 && x == 3858 && y == 5533)
					player.useStairs(-1, new WorldTile(3861, 5533, 0), 0, 1);
				else if (id == 48678 && x == 3858 && y == 5543)
					player.useStairs(-1, new WorldTile(3861, 5543, 0), 0, 1);
				else if (id == 48678 && x == 3858 && y == 5533)
					player.useStairs(-1, new WorldTile(3861, 5533, 0), 0, 1);
				else if (id == 48677 && x == 3858 && y == 5543)
					player.useStairs(-1, new WorldTile(3856, 5543, 1), 0, 1);
				else if (id == 48677 && x == 3858 && y == 5533)
					player.useStairs(-1, new WorldTile(3856, 5533, 1), 0, 1);
				else if (id == 48679)
					player.useStairs(-1, new WorldTile(3875, 5527, 1), 0, 1);
				else if (id == 48688)
					player.useStairs(-1, new WorldTile(3972, 5565, 0), 0, 1);
				else if (id == 48683)
					player.useStairs(-1, new WorldTile(3868, 5524, 0), 0, 1);
				else if (id == 48682)
					player.useStairs(-1, new WorldTile(3869, 5524, 0), 0, 1);
				else if (id == 62676) { // dominion exit
					player.useStairs(-1, new WorldTile(3374, 3093, 0), 0, 1);
				} else if (id == 62674) { // dominion entrance
					player.useStairs(-1, new WorldTile(3744, 6405, 0), 0, 1);
				} else if(id == 3192) {
					PkRank.showRanks(player);
				} else if (id == 65349) {
					player.useStairs(-1, new WorldTile(3044, 10325, 0), 0, 1);
				} else if (id == 32048 && object.getX() == 3043 &&  object.getY() == 10328) {
					player.useStairs(-1, new WorldTile(3045, 3927, 0), 0, 1);
				} else if(id == 26194) {
					player.getDialogueManager().startDialogue("PartyRoomLever");
				}else if (id == 61190 || id == 61191 || id == 61192 || id == 61193) {
					if (objectDef.containsOption(0, "Chop down"))
						player.getActionManager().setAction(
								new Woodcutting(object,
										TreeDefinitions.NORMAL));
				} else if(id == 20573)
					player.getControlerManager().startControler("RefugeOfFear");
				//crucible
				else if (id == 67050)
					player.useStairs(-1, new WorldTile(3359, 6110, 0), 0, 1);
				else if (id == 67053)
					player.useStairs(-1, new WorldTile(3120, 3519, 0), 0, 1);
				else if (id == 67051)
					player.getDialogueManager().startDialogue("Marv", false);
				else if (id == 67052)
					Crucible.enterCrucibleEntrance(player);
				else {
					switch (objectDef.name.toLowerCase()) {
					case "trapdoor":
					case "manhole":
						if (objectDef.containsOption(0, "Open")) {
							WorldObject openedHole = new WorldObject(object.getId()+1,
									object.getType(), object.getRotation(), object.getX(),
									object.getY(), object.getPlane());
							//if (World.removeTemporaryObject(object, 60000, true)) {
							player.faceObject(openedHole);
							World.spawnTemporaryObject(openedHole, 60000, true);
							//}
						}
						break;
					case "closed chest":
						if (objectDef.containsOption(0, "Open")) {
							player.setNextAnimation(new Animation(536));
							player.lock(2);
							WorldObject openedChest = new WorldObject(object.getId()+1,
									object.getType(), object.getRotation(), object.getX(),
									object.getY(), object.getPlane());
							//if (World.removeTemporaryObject(object, 60000, true)) {
							player.faceObject(openedChest);
							World.spawnTemporaryObject(openedChest, 60000, true);
							//}
						}
						break;
					case "open chest":
						if (objectDef.containsOption(0, "Search")) 
							player.getPackets().sendGameMessage("You search the chest but find nothing.");
						break;
					case "spiderweb":
						if(object.getRotation() == 2) {
							player.lock(2);
							if (Utils.getRandom(1) == 0) {
								player.addWalkSteps(player.getX(), player.getY() < y ? object.getY()+2 : object.getY() - 1, -1, false);
								player.getPackets().sendGameMessage("You squeeze though the web.");
							} else
								player.getPackets().sendGameMessage(
										"You fail to squeeze though the web; perhaps you should try again.");
						}
						break;
					case "web":
						if (objectDef.containsOption(0, "Slash")) {
							player.setNextAnimation(new Animation(PlayerCombat
									.getWeaponAttackEmote(player.getEquipment()
											.getWeaponId(), player
											.getCombatDefinitions()
											.getAttackStyle())));
							slashWeb(player, object);
						}
						break;
					case "anvil":
						if (objectDef.containsOption(0, "Smith")) {
							ForgingBar bar = ForgingBar.getBar(player);
							if (bar != null)
								ForgingInterface.sendSmithingInterface(player, bar);
							else
								player.getPackets().sendGameMessage("You have no bars which you have smithing level to use."); 
						}
						break;
					case "tin ore rocks":
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Tin_Ore));
						break;
					case "gold ore rocks":
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Gold_Ore));
						break;
					case "iron ore rocks":
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Iron_Ore));
						break;
					case "silver ore rocks":
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Silver_Ore));
						break;
					case "coal rocks":
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Coal_Ore));
						break;
					case "clay rocks":
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Clay_Ore));
						break;
					case "copper ore rocks":
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Copper_Ore));
						break;
					case "adamantite ore rocks":
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Adamant_Ore));
						break;
					case "runite ore rocks":
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Runite_Ore));
						break;
					case "granite rocks":
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Granite_Ore));
						break;
					case "sandstone rocks":
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Sandstone_Ore));
						break;
					case "crashed star":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.CRASHED_STAR));
						break;
					case "mithril ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Mithril_Ore));
						break;
					case "bank deposit box":
						if (objectDef.containsOption(0, "Deposit"))
							player.getBank().openDepositBox();
						break;
					case "bank":
					case "bank chest":
					case "bank booth":
					case "counter":
						if (objectDef.containsOption(0, "Bank") || objectDef.containsOption(0, "Use"))
							player.getBank().openBank();
						break;
						// Woodcutting start
					case "tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(
									new Woodcutting(object,
											TreeDefinitions.NORMAL));
						break;
					case "evergreen":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(
									new Woodcutting(object,
											TreeDefinitions.EVERGREEN));
						break;
					case "dead tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(
									new Woodcutting(object,
											TreeDefinitions.DEAD));
						break;
					case "oak":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager()
							.setAction(
									new Woodcutting(object,
											TreeDefinitions.OAK));
						break;
					case "willow":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(
									new Woodcutting(object,
											TreeDefinitions.WILLOW));
						break;
					case "maple tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(
									new Woodcutting(object,
											TreeDefinitions.MAPLE));
						break;
					case "ivy":
						if (objectDef.containsOption(0, "Chop"))
							player.getActionManager()
							.setAction(
									new Woodcutting(object,
											TreeDefinitions.IVY));
						break;
					case "yew":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager()
							.setAction(
									new Woodcutting(object,
											TreeDefinitions.YEW));
						break;
					case "magic tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(
									new Woodcutting(object,
											TreeDefinitions.MAGIC));
						break;
					case "cursed magic tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setAction(
									new Woodcutting(object,
											TreeDefinitions.CURSED_MAGIC));
						break;
						// Woodcutting end
					case "gate":
					case "large door":
					case "metal door":
						if (object.getType() == 0
						&& objectDef.containsOption(0, "Open"))
							if(!handleGate(player, object))
								handleDoor(player, object);
						break;
					case "door":
						if (object.getType() == 0
						&& (objectDef.containsOption(0, "Open") || objectDef
								.containsOption(0, "Unlock")))
							handleDoor(player, object);
						break;
					case "ladder":
						handleLadder(player, object, 1);
						break;
					case "staircase":
						handleStaircases(player, object, 1);
						break;
					case "Summoning obelisk":
					case "small obelisk":
						if (objectDef.containsOption(0, "Renew-points") || objectDef.containsOption(1, "Renew-points")) {
							int summonLevel = player.getSkills().getLevelForXp(Skills.SUMMONING);
							if(player.getSkills().getLevel(Skills.SUMMONING) < summonLevel) {
								player.lock(3);
								player.setNextAnimation(new Animation(8502));
								player.getSkills().set(Skills.SUMMONING, summonLevel);
								player.getPackets().sendGameMessage(
										"You have recharged your Summoning points.", true);
							}else
								player.getPackets().sendGameMessage("You already have full Summoning points.");
						}
						break;
					case "altar":
					case "bandos altar":
					case "gorilla statue":
						if (objectDef.containsOption(0, "Pray") || objectDef.containsOption(0, "Pray-at")) {
							final int maxPrayer = player.getSkills()
									.getLevelForXp(Skills.PRAYER) * 10;
							if (player.getPrayer().getPrayerpoints() < maxPrayer) {
								player.lock(5);
								player.getPackets().sendGameMessage(
										"You pray to the gods...", true);
								player.setNextAnimation(new Animation(645));
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.getPrayer().restorePrayer(
												maxPrayer);
										player.getPackets()
										.sendGameMessage(
												"...and recharged your prayer.",
												true);
									}
								}, 2);
							} else 
								player.getPackets().sendGameMessage(
										"You already have full prayer.");
							if (id == 6552)
								player.getDialogueManager().startDialogue(
										"AncientAltar");
						}
						break;
					case "vine":
					case "gap":
					case "cave entrance":
					case "steps":
						PolyporeDungeon.handleObjects(object, player);
						break;
					default:
						//player.getPackets().sendGameMessage(
						//		"Nothing interesting happens.");
						break;
					}
				}
				if (Settings.DEBUG)
					Logger.log(
							"ObjectHandler",
							"clicked 1 at object id : " + id + ", "
									+ object.getX() + ", " + object.getY()
									+ ", " + object.getPlane() + ", "
									+ object.getType() + ", "
									+ object.getRotation() + ", "
									+ object.getDefinitions().name);
			}
		}, objectDef.getSizeX(), Wilderness.isDitch(id) ? 4 : objectDef
				.getSizeY(), object.getRotation()));
	}

	private static void handleOption2(final Player player, final WorldObject object) {

		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);

				if (!player.getControlerManager().processObjectClick2(object))
					return;
				else if (object.getDefinitions().name
						.equalsIgnoreCase("furnace"))
					player.getDialogueManager().startDialogue("SmeltingD",
							object);
				else if (id == 6) {
					player.getDwarfCannon().pickUpDwarfCannon(0, object);
				}
				else if(id == 50205) {

					if (objectDef.containsOption(0, "Renew-points") || objectDef.containsOption(1, "Renew-points")) {
							
						int summonLevel = player.getSkills().getLevelForXp(Skills.SUMMONING);
						
						if(player.getSkills().getLevel(Skills.SUMMONING) < summonLevel) {

							player.lock(3);
							
							player.setNextAnimation(new Animation(8502));
							player.getSkills().set(Skills.SUMMONING, summonLevel);
							player.getPackets().sendGameMessage("You have recharged your Summoning points.", true);

						} else {
							player.getPackets().sendGameMessage("You already have full Summoning points.");
						}
					}
				}
				else if (id == 29408) {
					player.getDwarfCannon().pickUpDwarfCannonRoyal(0, object);
				}
				else if (id == 15410)
						player.getSkills().addXp(Skills.CONSTRUCTION, 1);
				else if (id == 17010)
					player.getDialogueManager().startDialogue("LunarAltar");
				else if (id == 8555 && player.allotmentA == 0)
					player.out("There is nothing currently growing.");
				else if (id == 8555 && player.allotmentA >= 1)
					player.out("There is something growing in the allotment.");
				else if (id == 62677)
					player.getDominionTower().openRewards();
				else if (id == 13405 && player.isOwner == true && player.hasLocked == false && player.inBuildMode == false) {
					player.out("You have locked your house, no one can enter.");
					player.hasLocked = true;
				} else if (id == 13405 && player.isOwner == true && player.hasLocked == true && player.inBuildMode == false) {
					player.out("You have un-locked your house, friends can now enter.");
					player.hasLocked = false;
				}
				
				else if (object.getId() == 4878)
					if (player.getInventory().getFreeSlots() < 1) {
						player.getPackets().sendGameMessage("Not enough space in your inventory.");
						return;
			} else {
				player.getInventory().addItem(1333, 1);
				if (player.getSkills().getLevel(17) == 1) {
					player.getSkills().addXp(Skills.THIEVING, 1);
				}
				player.getSkills().addXp(Skills.THIEVING, player.getSkills().getLevel(17) / 2);
				player.setNextAnimation(new Animation(881));
				player.increaseThieve();//1.2k
				player.lock(1);
				}
				else if (object.getId() == 4876)
					if (player.getInventory().getFreeSlots() < 1) {
						player.getPackets().sendGameMessage("Not enough space in your inventory.");
			} else {
				player.getInventory().addItem(1127, 1);
				player.getSkills().addXp(Skills.THIEVING, 90);
				player.setNextAnimation(new Animation(881));
				player.increaseThieve();
				}
				
				else if (id == 62688)
					player.getDialogueManager().startDialogue(
							"SimpleMessage",
							"You have a Dominion Factor of "
									+ player.getDominionTower()
									.getDominionFactor() + ".");
				else if (id == 68107)
					FightKiln.enterFightKiln(player, true);
				else if (id == 34384 || id == 34383 || id == 14011
						|| id == 7053 || id == 34387 || id == 34386
						|| id == 34385)
					Thieving.handleStalls(player, object);
				else if(id == 2418)
					PartyRoom.openPartyChest(player);
				else if (id == 2646) {
					World.removeTemporaryObject(object, 50000, true);
					player.getInventory().addItem(1779, 1);
					//crucible
				}else if (id == 67051)
					player.getDialogueManager().startDialogue("Marv", true);
				else {
					switch (objectDef.name.toLowerCase()) {
					case "cabbage":
						if (objectDef.containsOption(1, "Pick") && player.getInventory().addItem(1965, 1)) {
							player.setNextAnimation(new Animation(827));
							player.lock(2);
							World.removeTemporaryObject(object, 60000, false);
						}
						break;
					case "bank":
					case "bank chest":
					case "bank booth":
					case "counter":
						if (objectDef.containsOption(1, "Bank"))
							player.getBank().openBank();
						break;
					case "gates":
					case "gate":
					case "metal door":
						if (object.getType() == 0
						&& objectDef.containsOption(1, "Open"))
							handleGate(player, object);
						break;
					case "door":
						if (object.getType() == 0
						&& objectDef.containsOption(1, "Open"))
							handleDoor(player, object);
						break;
					case "ladder":
						handleLadder(player, object, 2);
						break;
					case "staircase":
						handleStaircases(player, object, 2);
						break;
					default:
						player.getPackets().sendGameMessage(
								"Nothing interesting happens.");
						break;
					}
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "clicked 2 at object id : " + id
							+ ", " + object.getX() + ", " + object.getY()
							+ ", " + object.getPlane());
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOption3(final Player player, final WorldObject object) {

		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);

				if (!player.getControlerManager().processObjectClick3(object))
					return;
				switch (objectDef.name.toLowerCase()) {
				case "gate":
				case "metal door":
					if (object.getType() == 0
					&& objectDef.containsOption(2, "Open"))
						handleGate(player, object);
					break;
				case "door":
					if (object.getType() == 0
					&& objectDef.containsOption(2, "Open"))
						handleDoor(player, object);
					break;
				case "ladder":
					handleLadder(player, object, 3);
					break;
				case "staircase":
					handleStaircases(player, object, 3);
					break;
				default:
					player.getPackets().sendGameMessage(
							"Nothing interesting happens.");
					break;
				}
				if (id == 8555) { //TODO Finish all the clearing of allotments
					player.getPackets().sendConfigByFile(713, 3);
					player.allotmentA = 0;
					player.hasPlantedAA = false;
				} else if (id == 8554) {
					player.getPackets().sendConfigByFile(712, 3);
					player.allotmentB = 0;
					player.hasPlantedAB = false;
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "cliked 3 at object id : " + id
							+ ", " + object.getX() + ", " + object.getY()
							+ ", " + object.getPlane() + ", ");
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOption4(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);

				if (!player.getControlerManager().processObjectClick4(object))
					return;
				//living rock Caverns
				if (id == 45076)
					MiningBase.propect(player, "This rock contains a large concentration of gold.");
				else if (id == 8555 || id == 7849 || id == 8152 || id == 8554) {
				player.getTemporaryAttributtes().put("skillMenu", 21);
				player.getPackets().sendConfig(965, 21);
				player.getInterfaceManager().sendInterface(499);
				}
				else if (id == 5999)
					MiningBase.propect(player, "This rock contains a large concentration of coal.");
				else{
					switch (objectDef.name.toLowerCase()) {
					default:
						player.getPackets().sendGameMessage(
								"Nothing interesting happens.");
						break;
					}
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "cliked 4 at object id : " + id
							+ ", " + object.getX() + ", " + object.getY()
							+ ", " + object.getPlane() + ", ");
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOption5(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {

			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);

				if (!player.getControlerManager().processObjectClick5(object))
					return;
				if (id == -1) {
					//unused
				} else if (object.getId() == 15412 || object.getId() == 15410 || object.getId() == 15411) {
					player.getDialogueManager().startDialogue("MakeChair");
				} else if (object.getId() == 15406 || object.getId() == 15407 || object.getId() == 15408) {
					player.getDialogueManager().startDialogue("MakePortal");
				} else if (object.getId() == 13637 || object.getId() == 13636) {
					player.portalFrame = 0;
					player.out("You remove the portal frames, re-enter your house for it to take effect.");
				} else if (object.getId() == 13581 || object.getId() == 13583 || object.getId() == 13584 || object.getId() == 13587) {
					player.chair = 0;
					player.out("You remove the chairs, re-enter your house for it to take effect.");
				} else if (id == 13416 || id == 13413 || id == 13414 || id == 13417) {
					player.tree = 0;
					player.out("You remove the trees, re-enter your house for it to take effect.");
				} else if (id == 13671 || id == 13670 || id == 13665) {
					player.throne = 0;
					player.out("You remove the throne, re-enter your house for it to take effect.");
				} else if (id == 13613 || id == 13611 || id == 13609) {
					player.firePlace = 0;
					player.out("You remove the fireplace, re-enter your house for it to take effect.");
				} else if (id == 13599) {
					player.bookcase = 0;
					player.out("You remove the bookcase, re-enter your house for it to take effect.");
				} else if (object.getId() == 15270) {
					player.out("Altar");
				/*} else if (object.getId() == 15416) {
					House.makeWardrobe(object, player);
				} else if (object.getId() == 15270) {
					House.makeAltar(object, player);
				} else if (object.getId() == 15260) {
					House.makeBed(object, player);
				} else if (object.getId() == 15361) {
					House.buildPortal(object, player);
				} else if (object.getId() == 13581) {
					House.whiteChair(object, player);*/
				} else if (object.getId() == 15409) {
					player.getDialogueManager().startDialogue("CreatePortal");
				} else if (object.getId() == 15426) {
					player.getDialogueManager().startDialogue("MakeThrone");
				} else if (id == 15362) {
					player.getDialogueManager().startDialogue("MakeTrees");
				} else if (id == 15416 || id == 15397) {
					player.getDialogueManager().startDialogue("MakeBook");
				} else if (id == 15418) {
					player.getDialogueManager().startDialogue("MakeFire");
				} else if (id == 15313) {
				//	player.getDialogueManager().startDialogue("RoomCreation");
					
					
					//TODO
				/**
				 * Removing Construction Portals
				 */
				} else if (id == 13622 && player.portal1 == true) {
							player.portalTele1 = 0;
							player.out("You remove the left portal, Changes take effect one you re-enter your house.");
							player.portal1 = false;
				} else if (id == 13622 && player.portal2 == true) {
							player.portalTele2 = 0;
							player.out("You remove the center portal, Changes take effect one you re-enter your house.");
							player.portal2 = false;
				} else if (id == 13622 && player.portal3 == true) {
							player.portalTele3 = 0;
							player.out("You remove the right portal, Changes take effect one you re-enter your house.");
							player.portal3 = false;
				} else if (id == 13623 && player.portal1 == true) {
							player.portalTele1 = 0;
							player.out("You remove the left portal, Changes take effect one you re-enter your house.");
							player.portal1 = false;
				} else if (id == 13623 && player.portal2 == true) {
							player.portalTele2 = 0;
							player.out("You remove the center portal, Changes take effect one you re-enter your house.");
							player.portal2 = false;
				} else if (id == 13623 && player.portal3 == true) {
							player.portalTele3 = 0;
							player.out("You remove the right portal, Changes take effect one you re-enter your house.");
							player.portal3 = false;
				} else if (id == 13624 && player.portal1 == true) {
					player.portalTele1 = 0;
					player.out("You remove the left portal, Changes take effect one you re-enter your house.");
					player.portal1 = false;
				} else if (id == 13624 && player.portal2 == true) {
					player.portalTele2 = 0;
					player.out("You remove the center portal, Changes take effect one you re-enter your house.");
					player.portal2 = false;
				} else if (id == 13624 && player.portal3 == true) {
					player.portalTele3 = 0;
					player.out("You remove the right portal, Changes take effect one you re-enter your house.");
					player.portal3 = false;
				} else if (id == 13625 && player.portal1 == true) {
					player.portalTele1 = 0;
					player.out("You remove the left portal, Changes take effect one you re-enter your house.");
					player.portal1 = false;
				} else if (id == 13625 && player.portal2 == true) {
					player.portalTele2 = 0;
					player.out("You remove the center portal, Changes take effect one you re-enter your house.");
					player.portal2 = false;
				} else if (id == 13625 && player.portal3 == true) {
					player.portalTele3 = 0;
					player.out("You remove the right portal, Changes take effect one you re-enter your house.");
					player.portal3 = false;
				} else if (id == 13627 && player.portal1 == true) {
					player.portalTele1 = 0;
					player.out("You remove the left portal, Changes take effect one you re-enter your house.");
					player.portal1 = false;
				} else if (id == 13627 && player.portal2 == true) {
					player.portalTele2 = 0;
					player.out("You remove the center portal, Changes take effect one you re-enter your house.");
					player.portal2 = false;
				} else if (id == 13627 && player.portal3 == true) {
					player.portalTele3 = 0;
					player.out("You remove the right portal, Changes take effect one you re-enter your house.");
					player.portal3 = false;
					
					

					
					
				} else {
					switch (objectDef.name.toLowerCase()) {
					case "door hotspot":
						//player.getInterfaceManager().sendInterface(402);
				break;
					case "repair space":
						player.getInterfaceManager().sendInterface(397);
				case "bed space":
						if(!player.getInventory().containsItem(8778, 4)) {
							player.getPackets().sendGameMessage("You need 4 oak planks to make a bed");
				} else {
							player.getInventory().deleteItem(8778, 4);
							player.getSkills().addXp(Skills.CONSTRUCTION, 10000);
							player.setNextAnimation(new Animation(898));
							player.getPackets().sendGameMessage("You make a bed");

				}
				//case "chair space":
				//	House.makeChair(object, player);
				//case "chair":
				//	House.removeChair(object, player);
			
			//	case "chair space":
			//			if(!player.getInventory().containsItem(960, 4)) {
			//				player.getPackets().sendGameMessage("You need 4 planks to make a chair");
			//	} else {
			//				player.getInventory().deleteItem(960, 4);
			//				player.getSkills().addXp(Skills.CONSTRUCTION, 2000);
			//				player.setNextAnimation(new Animation(898));
			//				player.getPackets().sendGameMessage("You make a chair");
			//	}
				//case "chair":
				//World.spawnObject(
				//		new WorldObject(15412, 10, 2,
					//			player.getX() +1, player.getY(), player
					//			.getPlane()), true);

				/*case "bookcase space":
						if(!player.getInventory().containsItem(8780, 4)) {
							//player.getPackets().sendGameMessage("You need 4 teak planks to make a bookcase");
				} else {
							player.getInventory().deleteItem(8780, 4);
							player.getSkills().addXp(Skills.CONSTRUCTION, 10000);
							player.setNextAnimation(new Animation(898));
							player.getPackets().sendGameMessage("You make a bookcase");
				}
				case "rug space":
						if(!player.getInventory().containsItem(8790, 4)) {
							//player.getPackets().sendGameMessage("You need 4 bolts of cloth to make a rug");
				} else {
							player.getInventory().deleteItem(8790, 4);
							player.getSkills().addXp(Skills.CONSTRUCTION, 2000);
							player.setNextAnimation(new Animation(898));
							player.getPackets().sendGameMessage("You make a rug");
				}
				case "wardrobe space":
						if(!player.getInventory().containsItem(8782, 5)) {
							//player.getPackets().sendGameMessage("You need 5 mahogany planks to make a wardrobe");
				} else {
							player.getInventory().deleteItem(8782, 4);
							player.getSkills().addXp(Skills.CONSTRUCTION, 12000);
							player.setNextAnimation(new Animation(898));
							player.getPackets().sendGameMessage("You make a wardrobe");
				}*/
					case "fire":
						if(objectDef.containsOption(4, "Add-logs"))
							Bonfire.addLogs(player, object);
						break;
					default:
						player.getPackets().sendGameMessage(
								"Nothing interesting happens.");
						break;
					}
				}
				//System.out.println("ConfigByFile ID = " + ObjectDefinitions.configByFile(objectId));
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "cliked 5 at object id : " + id
							+ ", " + object.getX() + ", " + object.getY()
							+ ", " + object.getPlane() + ", ");
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOptionExamine(final Player player, final WorldObject object) {
		if(player.getUsername().equalsIgnoreCase("tyler")) {
			int offsetX = object.getX() - player.getX();
			int offsetY = object.getY() - player.getY();
			System.out.println("Offsets"+offsetX+ " , "+offsetY);
		}

		player.getPackets().sendGameMessage("It's a " + object.getDefinitions().name + ".");

		if (Settings.DEBUG)
			if (Settings.DEBUG)
				
				Logger.log(
						"ObjectHandler",
						"examined object id : " + object.getId() + ", "
								+ object.getX() + ", " + object.getY()
								+ ", " + object.getPlane() + ", "
								+ object.getType() + ", "
								+ object.getRotation() + ", "
								+ object.getDefinitions().name);
		if (player.getUsername().equalsIgnoreCase("brandon")) {
			ObjectDefinitions.configByFile(object.getId(), player);
			ObjectDefinitions.objectAnimationId(object.getId());
		}
	}


	private static void slashWeb(Player player, WorldObject object) {
		if (Utils.getRandom(1) == 0) {
			World.spawnTemporaryObject(new WorldObject(object.getId() + 1,
					object.getType(), object.getRotation(), object.getX(),
					object.getY(), object.getPlane()), 60000, true);
			player.getPackets().sendGameMessage("You slash through the web!");
		} else
			player.getPackets().sendGameMessage(
					"You fail to cut through the web.");
	}


	private static boolean handleGate(Player player, WorldObject object) {
		if (World.isSpawnedObject(object))
			return false;
		if (object.getRotation() == 0) {

			boolean south = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX(), object.getY() + 1, object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(
						new WorldTile(object.getX(), object.getY() - 1, object
								.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
						.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor1.setRotation(3);
				openedDoor2.moveLocation(-1, 0, 0);
			} else {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor2.moveLocation(-1, 0, 0);
				openedDoor2.setRotation(3);
			}

			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 2) {

			boolean south = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX(), object.getY() + 1, object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(
						new WorldTile(object.getX(), object.getY() - 1, object
								.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
						.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor2.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			} else {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor1.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 3) {

			boolean right = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX() - 1, object.getY(), object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(new WorldTile(object.getX() + 1,
						object.getY(), object.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
						.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor2.setRotation(0);
				openedDoor1.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			} else {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 1) {

			boolean right = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX() - 1, object.getY(), object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(new WorldTile(object.getX() + 1,
						object.getY(), object.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
						.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			} else {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor2.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		}
		return false;
	}

	public static boolean handleDoor(Player player, WorldObject object, long timer) {
		if (World.isSpawnedObject(object))
			return false;
		WorldObject openedDoor = new WorldObject(object.getId(),
				object.getType(), object.getRotation() + 1, object.getX(),
				object.getY(), object.getPlane());
		if (object.getRotation() == 0)
			openedDoor.moveLocation(-1, 0, 0);
		else if (object.getRotation() == 1)
			openedDoor.moveLocation(0, 1, 0);
		else if (object.getRotation() == 2)
			openedDoor.moveLocation(1, 0, 0);
		else if (object.getRotation() == 3)
			openedDoor.moveLocation(0, -1, 0);
		if (World.removeTemporaryObject(object, timer, true)) {
			player.faceObject(openedDoor);
			World.spawnTemporaryObject(openedDoor, timer, true);
			return true;
		}
		return false;
	}

	private static boolean handleDoor(Player player, WorldObject object) {
		return handleDoor(player, object, 60000);
	}

	private static boolean handleStaircases(Player player, WorldObject object,
			int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(),
					player.getPlane() + 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(),
					player.getPlane() - 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue(
					"ClimbNoEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() + 1),
							new WorldTile(player.getX(), player.getY(), player
									.getPlane() - 1), "Go up the stairs.",
					"Go down the stairs.");
		} else
			return false;
		return false;
	}

	private static boolean handleLadder(Player player, WorldObject object,
			int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(),
					player.getPlane() + 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(),
					player.getPlane() - 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue(
					"ClimbEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() + 1),
							new WorldTile(player.getX(), player.getY(), player
									.getPlane() - 1), "Climb up the ladder.",
									"Climb down the ladder.", 828);
		} else
			return false;
		return true;
	}

	public static void handleItemOnObject(final Player player, final WorldObject object, final int interfaceId, final Item item) {
		final int itemId = item.getId();
		final ObjectDefinitions objectDef = object.getDefinitions();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				player.faceObject(object);
				if (itemId == 1438 && object.getId() == 2452) {
					Runecrafting.enterAirAltar(player);
				} else if (itemId == 1440 && object.getId() == 2455) {
					Runecrafting.enterEarthAltar(player);
				} else if (itemId == 1442 && object.getId() == 2456) {
					Runecrafting.enterFireAltar(player);
				} else if (itemId == 1444 && object.getId() == 2454) {
					Runecrafting.enterWaterAltar(player);
				} else if (itemId == 1446 && object.getId() == 2457) {
					Runecrafting.enterBodyAltar(player);
				} else if (itemId == 1448 && object.getId() == 2453) {
					Runecrafting.enterMindAltar(player);
				} else if (object.getId() == 733 || object.getId() == 64729) {
					player.setNextAnimation(new Animation(PlayerCombat
							.getWeaponAttackEmote(-1, 0)));
					slashWeb(player, object);
				} else if (object.getId() == 48803 && itemId == 954) {
					if (player.isKalphiteLairSetted())
						return;
					player.getInventory().deleteItem(954, 1);
					player.setKalphiteLair();
				} else if (itemId == 1704 && object.getId() == 36695) {
					player.getInventory().addItem(1712, 1);
					player.getInventory().deleteItem(1704, 1);
				} else if (itemId == 1706 && object.getId() == 36695) {
					player.getInventory().addItem(1712, 1);
					player.getInventory().deleteItem(1706, 1);
				} else if (itemId == 1708 && object.getId() == 36695) {
					player.getInventory().addItem(1712, 1);
					player.getInventory().deleteItem(1708, 1);
				} else if (itemId == 1710 && object.getId() == 36695) {
					player.getInventory().addItem(1712, 1);
					player.getInventory().deleteItem(1710, 1);
				} else if (itemId == 2353) {
				switch (objectDef.name.toLowerCase()) {
					case "furnace":
					if (player.getInventory().containsItem(4, 1) && player.completedDwarfCannonQuest == true) {
						player.getActionManager().setAction(new Smelting(-1, object, player.getInventory().getNumerOf(2353)));
					} else {
						player.out("You need a cannon ball mould.");
					}
						break;
					}
				} else if(object.getId() == 409) {
					player.out("You can now only use Bones on altar in your house.");
				} else if(object.getId() == 13190) {
					Bones bone = BonesOnAltar.isGood(item);
					if(bone != null) {
						player.getDialogueManager().startDialogue("PrayerD", bone, object);
						player.boneOnAltar = 1;
						return;
					} else {
						player.getPackets().sendGameMessage("Nothing interesting happens.");
						return;
					}
				} else if(object.getId() == 13196) {
					Bones bone = BonesOnAltar.isGood(item);
					if(bone != null) {
						player.getDialogueManager().startDialogue("PrayerD", bone, object);
						player.boneOnAltar = 2;
						return;
					} else {
						player.getPackets().sendGameMessage("Nothing interesting happens.");
						return;
					}
				} else if(object.getId() == 13199) {
					Bones bone = BonesOnAltar.isGood(item);
					if(bone != null) {
						player.getDialogueManager().startDialogue("PrayerD", bone, object);
						player.boneOnAltar = 3;
						return;
					} else {
						player.getPackets().sendGameMessage("Nothing interesting happens.");
						return;
					}
				//} else if (itemId == 1079 || itemId == 1127 || itemId == 1163 && object.getId() == 15621) {
				//	WGuildControler.handleItemOnObject(player, object, item);
				} else if (itemId == 6055 && object.getId() == 7839) {
					player.increaseWeed++;
					//player.getInventory().deleteItem(6055, 1);
					player.out("You place a weed in the compost bin. "+player.increaseWeed );
					if (player.increaseWeed <= 14) {
					player.getPackets().sendConfigByFile(743, 1);
					} else if (player.increaseWeed >= 15) {
					player.getPackets().sendConfigByFile(743, 15);
					player.useCompost = true;
					}
				
					
					// Start of Farming, Potato Seeds Below.
				} else if (itemId == 5318 && object.getId() == 8550 && player.hasPlantedA == false && player.mustRakeA == true) {
					FarmingManager.plantPotatoA(player, 708);
						player.potatoA = true; 
						player.hasPlantedA = true;
						
				} else if (itemId == 5318 && object.getId() == 8551 && player.hasPlantedB == false && player.mustRakeB == true) {
					FarmingManager.plantPotatoB(player, 709);
						player.potatoB = true;
						player.hasPlantedB = true;
						// End of potato.
						
						
							
					//Water melon, Level needed to grow = 47.
				} else if (itemId == 5321 && object.getId() == 8550 && player.hasPlantedA == false && player.mustRakeA == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 47) {
						player.getPackets().sendGameMessage("You need a Farming level of 47 to plant this.");
						return;
					}
					FarmingManager.plantMelonA(player, 708);	
					player.melonA = true;
					player.hasPlantedA = true;
					
				} else if (itemId == 5321 && object.getId() == 8551 && player.hasPlantedB == false && player.mustRakeB == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 47) {
						player.getPackets().sendGameMessage("You need a Farming level of 47 to plant this.");
						return;
					}
					FarmingManager.plantMelonB(player, 709);	
					player.melonB = true;
					player.hasPlantedB = true;
					//End of Melon.
					
					//Start of sweetcorn
					
				} else if (itemId == 5320 && object.getId() == 8550 && player.hasPlantedA == false && player.mustRakeA == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 71) {
						player.getPackets().sendGameMessage("You need a Farming level of 71 to plant this.");
						return;
					}
					FarmingManager.plantSweetA(player, 708);	
					player.sweetA = true;
					player.hasPlantedA = true;
					
				} else if (itemId == 5320 && object.getId() == 8551 && player.hasPlantedB == false && player.mustRakeB == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 71) {
						player.getPackets().sendGameMessage("You need a Farming level of 71 to plant this.");
						return;
					}
					FarmingManager.plantSweetB(player, 709);	
					player.sweetB = true;
					player.hasPlantedB = true;
					
					//End of sweetcorn
					
					
					
					
					//Guam Herb
				} else if (itemId == 5291 && object.getId() == 8150 && player.hasPlantedHerb == false && player.mustRakeH == true) {
					FarmingManager.plantGuamA(player, 780);
					player.guamA = true;
					player.hasPlantedHerb = true;
					
					
				} else if (itemId == 5300 && object.getId() == 8150 && player.hasPlantedHerb == false && player.mustRakeH == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 62) {
						player.getPackets().sendGameMessage("You need a Farming level of 62 to plant this.");
						return;
					}
					FarmingManager.plantSnap(player, 780);
					player.snapA = true;
					player.hasPlantedHerb = true;
					
				} else if (itemId == 5304 && object.getId() == 8150 && player.hasPlantedHerb == false && player.mustRakeH == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 85) {
						player.getPackets().sendGameMessage("You need a Farming level of 85 to plant Torstol Seeds.");
						return;
					}
					FarmingManager.plantTorstol(player, 780);
					player.torstol = true;
					player.hasPlantedHerb = true;
					
					
					//Flower Patch, Below is Marigold seeds
				} else if (itemId == 5096 && object.getId() == 7847 && player.hasPlantedFlower == false && player.mustRakeF == true) {
					FarmingManager.plantGold(player, 728);
					player.gold = true;
					player.hasPlantedFlower = true;
					
					//Below is White Lily seeds
				} else if (itemId == 14589 && object.getId() == 7847 && player.hasPlantedFlower == false && player.mustRakeF == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 52) {
						player.getPackets().sendGameMessage("You need a Farming level of 52 to plant this.");
						return;
					}
					FarmingManager.plantLily(player, 728);
					player.lily = true;
					player.hasPlantedFlower = true;
					
					
					//Start of Catherby Farming
					
					
					// Start of Farming, Potato Seeds Below.
				} else if (itemId == 5318 && object.getId() == 8553 && player.hasPlantedCA == false && player.mustRakeCA == true) {
					CathFarming.plantPotatoCA(player, 711);
						player.potatoCA = true; 
						player.hasPlantedCA = true;
						
				} else if (itemId == 5318 && object.getId() == 8552 && player.hasPlantedCB == false && player.mustRakeCB == true) {
					CathFarming.plantPotatoCB(player, 710);
						player.potatoCB = true;
						player.hasPlantedCB = true;
						// End of potato.
						
						
							
					//Water melon, Level needed to grow = 47.
				} else if (itemId == 5321 && object.getId() == 8553 && player.hasPlantedCA == false && player.mustRakeCA == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 47) {
						player.getPackets().sendGameMessage("You need a Farming level of 47 to plant Melon.");
						return;
					}
					CathFarming.plantMelonCA(player, 711);	
					player.melonCA = true;
					player.hasPlantedCA = true;
					
				} else if (itemId == 5321 && object.getId() == 8552 && player.hasPlantedCB == false && player.mustRakeCB == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 47) {
						player.getPackets().sendGameMessage("You need a Farming level of 47 to plant Melon.");
						return;
					}
					CathFarming.plantMelonCB(player, 710);	
					player.melonCB = true;
					player.hasPlantedCB = true;
					//End of Melon.
					
				} else if (itemId == 5320 && object.getId() == 8553 && player.hasPlantedCA == false && player.mustRakeCA == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 71) {
						player.getPackets().sendGameMessage("You need a Farming level of 71 to plant Sweetcorn.");
						return;
					}
					CathFarming.plantSweetCA(player, 711);	
					player.sweetCA = true;
					player.hasPlantedCA = true;
					
				} else if (itemId == 5320 && object.getId() == 8552 && player.hasPlantedCB == false && player.mustRakeCB == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 71) {
						player.getPackets().sendGameMessage("You need a Farming level of 71 to plant Sweetcorn.");
						return;
					}
					CathFarming.plantSweetCB(player, 710);	
					player.sweetCB = true;
					player.hasPlantedCB = true;
					
					
					//Guam Herb
				} else if (itemId == 5291 && object.getId() == 8151 && player.hasPlantedHerbC == false && player.mustRakeCH == true) {
					CathFarming.plantGuamC(player, 781);
					player.guamCA = true;
					player.hasPlantedHerbC = true;
					
					
				} else if (itemId == 5300 && object.getId() == 8151 && player.hasPlantedHerbC == false && player.mustRakeCH == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 62) {
						player.getPackets().sendGameMessage("You need a Farming level of 62 to plant this.");
						return;
					}
					CathFarming.plantSnapC(player, 781);
					player.snapCA = true;
					player.hasPlantedHerbC = true;
					
				} else if (itemId == 5304 && object.getId() == 8151 && player.hasPlantedHerbC == false && player.mustRakeCH == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 85) {
						player.getPackets().sendGameMessage("You need a Farming level of 85 to plant Torstol Seeds.");
						return;
					}
					CathFarming.plantTorstolC(player, 781);
					player.torstolCA = true;
					player.hasPlantedHerbC = true;
					
					
					//Flower Patch, Below is Marigold seeds
				} else if (itemId == 5096 && object.getId() == 7848 && player.hasPlantedFlowerC == false && player.mustRakeCF == true) {
					CathFarming.plantGoldC(player, 729);
					player.goldC = true;
					player.hasPlantedFlowerC = true;
					
					//Below is White Lily seeds
				} else if (itemId == 14589 && object.getId() == 7848 && player.hasPlantedFlowerC == false && player.mustRakeCF == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 52) {
						player.getPackets().sendGameMessage("You need a Farming level of 52 to plant this.");
						return;
					}
					CathFarming.plantLilyC(player, 729);
					player.lilyC = true;
					player.hasPlantedFlowerC = true;
					
					//End of Catherby Farming
			
					
					//Start of trees
					
				} else if (itemId == 5315 && object.getId() == 8389 && player.hasPlantedTree == false) {
					if (player.getSkills().getLevel(Skills.FARMING) < 60) {
						player.getPackets().sendGameMessage("You need a Farming level of 60 to plant this.");
						return;
					}
					FarmingManager.plantYew(player, 701);
					player.yew = true;
					player.hasPlantedTree = true;
					
				} else if (itemId == 5316 && object.getId() == 8389 && player.hasPlantedTree == false) {
					if (player.getSkills().getLevel(Skills.FARMING) < 75) {
						player.getPackets().sendGameMessage("You need a Farming level of 75 to plant this.");
						return;
					}
					FarmingManager.plantMagic(player, 701);
					player.magic = true;
					player.hasPlantedTree = true;
					
					/**
					 * Ardougne Farming
					 */
				} else if (itemId == 5318 && object.getId() == 8555 && player.hasPlantedAA == false && player.mustRakeAA == true) {//TODO Finish all seeds
					ArdyFarming.plantPotatoAA(player, 713);
						player.allotmentA = 1;
						player.hasPlantedAA = true;
				} else if (itemId == 5320 && object.getId() == 8555 && player.hasPlantedAA == false && player.mustRakeAA == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 20) {
						player.getPackets().sendGameMessage("You need a Farming level of 20 to plant Sweetcorn.");
						return;
					}
					ArdyFarming.plantSweetAA(player, 713);
						player.allotmentA = 3;
						player.hasPlantedAA = true;
				} else if (itemId == 5321 && object.getId() == 8555 && player.hasPlantedAA == false && player.mustRakeAA == true) {
					if (player.getSkills().getLevel(Skills.FARMING) < 47) {
						player.getPackets().sendGameMessage("You need a Farming level of 47 to plant Melons.");
						return;
					}
					ArdyFarming.plantMelonAA(player, 713);
						player.allotmentA = 2;
						player.hasPlantedAA = true;
					//End of farming.
					
					//TODO
				} else if (itemId == 18830 && object.getId() == 13199 || object.getId() == 13190 || object.getId() == 13196) {
						player.getPackets().sendGameMessage("You pray at the gods");
						player.getInventory().deleteItem(new Item(18830, 1));
						player.getSkills().addXp(Skills.PRAYER, 14000);
                                                player.getPackets().sendSound(2738, 0, 1);
                                                player.setNextAnimation(new Animation(896));
                                                player.setNextGraphics(new Graphics(624));
                                                player.getInventory().refresh();
				} else if (itemId == 532 && object.getId() == 13199 || object.getId() == 13190 || object.getId() == 13196) {
						player.getPackets().sendGameMessage("You pray at the gods");
						player.getInventory().deleteItem(new Item(532, 1));
						player.getSkills().addXp(Skills.PRAYER, 4400);
                                                player.getPackets().sendSound(2738, 0, 1);
                                                player.setNextAnimation(new Animation(896));
                                                player.setNextGraphics(new Graphics(624));
                                                player.getInventory().refresh();
				} else if (itemId == 536 && object.getId() == 13199 || object.getId() == 13190 || object.getId() == 13196) {
					if (player.getInventory().containsItem(536, 10))
						player.getPackets().sendGameMessage("You pray at the gods");
						player.getInventory().deleteItem(new Item(536, 1));
						player.getSkills().addXp(Skills.PRAYER, 8500);
                                                player.getPackets().sendSound(2738, 0, 1);
                                                player.setNextAnimation(new Animation(896));
                                                player.setNextGraphics(new Graphics(624));
                                                player.getInventory().refresh();
				} else if (object.getId() == 48802 && itemId == 954) {
					if (player.isKalphiteLairEntranceSetted())
						return;
					player.getInventory().deleteItem(954, 1);
					player.setKalphiteLairEntrance();
				} else {
					switch (objectDef.name.toLowerCase()) {
					case "anvil":
						ForgingBar bar = ForgingBar.forId(itemId);
						if (bar != null)
							ForgingInterface.sendSmithingInterface(player, bar);
						break;
					case "fire":
						if (objectDef.containsOption(4, "Add-logs")
								&& Bonfire.addLog(player, object, item))
							return;
					case "range":
					case "cooking range":
					case "stove":
						Cookables cook = Cooking.isCookingSkill(item);
						if (cook != null) {
							player.getDialogueManager().startDialogue(
									"CookingD", cook, object);
							return;
						}
						player.getDialogueManager()
						.startDialogue(
								"SimpleMessage",
								"You can't cook that on a "
										+ (objectDef.name
												.equals("Fire") ? "fire"
														: "range") + ".");
						break;
					default:
						player.getPackets().sendGameMessage(
								"Nothing interesting happens.");
						break;
					}
					if (Settings.DEBUG)
						System.out.println("Item on object: " + object.getId());
				}
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}
}
