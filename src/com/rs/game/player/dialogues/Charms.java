package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.minigames.CastleWars;
import com.rs.game.player.content.Magic;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.FightKiln;
import com.rs.game.player.controlers.ZombieMinigame;

public class Charms extends Dialogue {

        private int npcId;

        @Override
        public void start() {
                sendEntityDialogue(SEND_2_TEXT_CHAT,
                                new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                                "Hello, I can teleport you to many Quest and Minigame Locations. Would you like to see them?"}, IS_NPC, npcId, 9827);
        }
        
        @Override
        public void run(int interfaceId, int componentId) {

			if (stage == -1) {

				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Sure, why not." }, IS_PLAYER, player.getIndex(), 9827);
				stage = 1;
			
			} else if (stage == 1) {

				sendOptionsDialogue("Where would you like to go?", "Undead Nightmare", "Lost-City Quest", "Fight Caves", "Fight Kiln", "More Options");
				stage = 2;
			
			} else if (stage == 2) {
				
				if(componentId == OPTION_1) {
					ZombieMinigame.enterZombieGame(player);
				} else if(componentId == OPTION_2) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3153, 3209, 0));
				} else if (componentId == OPTION_3) {
					Magic.sendNormalTeleportSpell(player, 0, 0, FightCaves.OUTSIDE);
				} else if (componentId == OPTION_4) {
					Magic.sendNormalTeleportSpell(player, 0, 0, FightKiln.OUTSIDE);
				} else if (componentId == OPTION_5) {
					
					stage = 3;
					sendOptionsDialogue("Where would you like to go?", "Charms Location", "Castle Wars", "Duel Arena.", "Dwarf Cannon Quest", "More Options");

				}
			
			} else if (stage == 3) {
				
				if (componentId == OPTION_1) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3143, 5446, 0));
				} else if(componentId == OPTION_2) {
					Magic.sendNormalTeleportSpell(player, 0, 0, CastleWars.LOBBY);
				} else if(componentId == OPTION_3) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3376, 3275, 0));
				} else if(componentId == OPTION_4) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2568, 3453, 0));
				} else if(componentId == OPTION_5) {

					stage = 4;
					sendOptionsDialogue("Where would you like to go?", "Heroes Guild", "Comming Soon", "Comming Soon", "Start Over");
				
				}
			
			} else if (stage == 4) {
					
				if (componentId == OPTION_1) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2895, 9908, 0));
				} else if(componentId == OPTION_2) {
					player.out("Currently Unavailable.");
				} else if(componentId == OPTION_3) {
					player.out("Currently Unavailable.");
				} else if(componentId == OPTION_4) {

					stage = 2;
					sendOptionsDialogue("Where would you like to go?", "Undead Nightmare", "Lost-City Quest", "Fight Caves", "Fight Kiln", "More Options");
				}
			}
				
		}
                

        @Override
        public void finish() {

        }
}