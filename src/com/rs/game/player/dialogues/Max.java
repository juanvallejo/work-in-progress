package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.minigames.CastleWars;
import com.rs.game.player.Skills;
import com.rs.game.player.content.Magic;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.FightKiln;

public class Max extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		if (Settings.ECONOMY) {
			player.getPackets().sendGameMessage("Mr.Ex is in no mood to talk to you.");
			end();
			return;
		}
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello I can teleport you to all the skilling area's,",
						" would you like to?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Sure, why not." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendOptionsDialogue("Where would you like to go?", "Mining",
					"Smithing", "Construction", "Slayer ::exchange", "More Options");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == OPTION_1)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3297, 3298, 0));
			else if (componentId == OPTION_2)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2710, 3493, 0));
			else if (componentId == OPTION_3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2544, 3092, 0));
			else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3428,
						3539, 0));
				player.out("<col=FFFF00>Pick the pieces of eight up and then type ::exchange to gain slayer experience.");
			} else if (componentId == OPTION_5) {
				stage = 3;
				sendOptionsDialogue("Where would you like to go?",
						"Hunter", "Agility (Gnome Agility)", "Woodcutting.",
						"Thieving", "More Options");
			}
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2567,
						2906, 0));
			} else if (componentId == OPTION_2)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2470,
						3436, 0));
			else if (componentId == OPTION_3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2724,
						3485, 0));
			else if (componentId == OPTION_4)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2662,
						3305, 0));
			else if (componentId == OPTION_5) {
				stage = 4;
				sendOptionsDialogue("Where would you like to go?",
						"Farming", "Fishing", "Higher Fishing",
						"Barbarian Agility", "More Options");
			}
		} else if (stage == 4) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3052,
						3304, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3088,
						3230, 0));
			} else if (componentId == OPTION_3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2843,
						3433, 0));
			else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2551,
						3557, 0));
			} else if (componentId == OPTION_5) {
				stage = 5;
				sendOptionsDialogue("Where would you like to go?",
						"Abyss (Runecrafting)", "Dungeoneering", "Tree Farm", "Comming soon",
						"Back to the first page");
			}
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3040, 4834, 0));
			}	else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3449, 3743, 0));
			}	 else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3004, 3378, 0));
			} else if (componentId == OPTION_4) {


			} else if (componentId == OPTION_5) {
				stage = 6;
			}
		}
				/*sendOptionsDialogue("Where would you like to go?",
						"Zamorak", "Armadyl", "Castle Wars", "Trivia",
						"More Options");
			}
		} else if (stage == 6) {
			if (componentId == OPTION_1)
				teleportPlayer(2925, 5330, 2);
			else if (componentId == OPTION_2)
				teleportPlayer(2838, 5297, 2);
			else if (componentId == OPTION_3)
				Magic.sendNormalTeleportSpell(player, 0, 0, CastleWars.LOBBY);
			else if (componentId == OPTION_4)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2647, 9378, 0));
			else if (componentId == OPTION_5) {
				sendOptionsDialogue("Where would you like to go?",
						"Kalphite Queen", "Fight Caves", "Fight Kiln", "Queen Black Dragon",
						"More Options");
				stage = 7;
			}
		} else if (stage == 7) {
			if (componentId == OPTION_1) 
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3226, 3108, 0));
			else if (componentId == OPTION_2)
				Magic.sendNormalTeleportSpell(player, 0, 0, FightCaves.OUTSIDE);
			else if (componentId == OPTION_3)
				Magic.sendNormalTeleportSpell(player, 0, 0, FightKiln.OUTSIDE);
			else if (componentId == OPTION_4) {
				end();
				if (player.getSkills().getLevelForXp(Skills.SUMMONING) < 60) {
					player.getPackets().sendGameMessage("You need a summoning level of 60 to go through this portal.");
					return;
				}
				player.getControlerManager().startControler("QueenBlackDragonControler");
			}
			else if (componentId == 2)
				teleportPlayer(2838, 5297, 2);
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, CastleWars.LOBBY);
			else if (componentId == 4)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2647, 9378, 0));
			else if (componentId == OPTION_5) {
				sendOptionsDialogue("Where would you like to go?",
						"Nex.", "Bandos.", "Sara.", "Tormented Demons", 
						"More Options");
				stage = 2;
			}
		}*/
	}

	private void teleportPlayer(int x, int y, int z) {
		player.setNextWorldTile(new WorldTile(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("GodWars");
	}

	@Override
	public void finish() {

	}
}
