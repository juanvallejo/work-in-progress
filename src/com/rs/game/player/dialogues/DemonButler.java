package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class DemonButler extends Dialogue {

        private int npcId = 11307;

        @Override
        public void start() {
                sendEntityDialogue(SEND_2_TEXT_CHAT,
                                new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                                "Good day sir, I am the Demon butler, I am in search for a job, my role is converting planks for players such as yourself. Would you like to hire me?"}, IS_NPC, npcId, 9827);
        }
        
        @Override
        public void run(int interfaceId, int componentId) {
                if (stage == -1) {
                        sendOptionsDialogue("Would you like to hire the demon butler", "Yes please", "No thank you.");
                        stage = 1;
                } else if (stage == 1) {
                        if (componentId == OPTION_1) {
                        	sendNPCDialogue(npcId, 9827, "Very good sir, I will just run through a few things, I will need a one time payment so you will only need to pay once today.");
                                stage = 2;
                                }
                        else if (componentId == OPTION_2) {
                                end();
                                }
                } else if (stage == 2) {
                	sendPlayerDialogue(9827, "Ok that sounds fair. How much do you cost?");
                	stage = 3;
                } else if (stage == 3) {
                	sendNPCDialogue(npcId, 9827, "The one time payment will cost 70 Million gold coins.");
                	stage = 4;
                } else if (stage == 4) { 
                	sendNPCDialogue(npcId, 9827, "It may seem a lot, but It is definatly worth it in the long run.");
                	stage = 5;
                } else if (stage == 5) {
                    sendOptionsDialogue("Would you like hire the demon butler for 70M?", "Your Hired!", "No thank you, that seems too expensive!");
                    stage = 6;
                } else if (stage == 6) {
                    if (componentId == OPTION_1) {
                    	if (player.getInventory().containsItem(995, 70000000)) {
                    	sendNPCDialogue(npcId, 9827, "It's been a pleasure doing business Sir! I will be waiting in your house, the next time you enter.");
                    	player.getInventory().deleteItem(995, 70000000);
                    	player.hasBoughtDemon = true;
                    	player.spokeToDemon = true;
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