package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class EstateAgent extends Dialogue {

        private int npcId = 4247;

        @Override
        public void start() {
                sendEntityDialogue(SEND_2_TEXT_CHAT,
                                new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                                "Hello, I am the estate agent here at Camelot, My job is to sell properties, May I intrest you in a property?"}, IS_NPC, npcId, 9827);
        }
        
        @Override
        public void run(int interfaceId, int componentId) {
                if (stage == -1) {
                        sendOptionsDialogue("Would you like to buy a property?", "Yes please", "No thank you.");
                        stage = 1;
                } else if (stage == 1) {
                        if (componentId == OPTION_1) {
                        	sendNPCDialogue(npcId, 9827, "At the moment I only have one property in Yanille, it's a lovely House and is very cheap!");
                                stage = 2;
                                }
                        else if (componentId == OPTION_2) {
                                end();
                                }
                } else if (stage == 2) {
                	sendPlayerDialogue(9827, "It sounds amazing! How much exactly does it cost?");
                	stage = 3;
                } else if (stage == 3) {
                	sendNPCDialogue(npcId, 9827, "The house only costs 50 Million gold coins. It's a bargin as it is reduced from 100 Million gold coins.");
                	stage = 4;
                } else if (stage == 4) { 
                	sendNPCDialogue(npcId, 9827, "So would you like to buy it now or later?");
                	stage = 5;
                } else if (stage == 5) {
                    sendOptionsDialogue("Would you like to buy a property for 50 Million gold coins?", "I'd like to buy it now please.", "I'll think about it and come back later.");
                    stage = 6;
                } else if (stage == 6) {
                    if (componentId == OPTION_1) {
                    	if (player.getInventory().containsItem(995, 50000000)) {
                    	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2544, 3092, 0));
                    	sendNPCDialogue(npcId, 9827, "It's been a pleasure doing business Sir! You can access your house at the Yanille House portal, I will teleport you there incase you can't find it, A little tip - Activate the lodestone.");
                    	player.getInventory().deleteItem(995, 50000000);
                    	player.hasHouse = true;
                    	player.spokeToAgent = true;
                            stage = 7;
                    	} else {
                    		sendNPCDialogue(npcId, 9827, "I'm sorry, it looks like you don't have the funds in your inventory, please come back when you do.");
                    		stage = 7;
                    	}
                     } else if (componentId == OPTION_2) {
                            end();
                            }
                } else if (stage == 7) { 
                	end();
                }
    		}


        @Override
        public void finish() {

        }
}