package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class Xmas extends Dialogue {

        private int npcId = 8540;
        public boolean xmas = false;

        @Override
        public void start() {
                sendEntityDialogue(SEND_2_TEXT_CHAT,
                                new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                                "Hello, Would you like to start the Christmas event?"}, IS_NPC, npcId, 9827);
        }
        
        @Override
        public void run(int interfaceId, int componentId) {
    		if (xmas == false) {
    			sendNPCDialogue(npcId, 9827, "You have already spoke to me?");
    		}
                if (stage == -1) {
                        sendOptionsDialogue("Would you?", "Yes", "No.");
                        stage = 1;
                } else if (stage == 1) {
                        if (componentId == OPTION_1) {
                        	sendNPCDialogue(npcId, 9827, "I fell down the stairs and broke me hip, can you deliver the rest of the presents?");
                                stage = 2;
                                }
                        else if (componentId == OPTION_2) {
                                end();
                                }
                } else if (stage == 2) {
                	sendPlayerDialogue(9827, "Sure, I'd be delighted!");
                	stage = 3;
                } else if (stage == 3) {
                	sendNPCDialogue(npcId, 9827, "Thankyou sir, alright first you need to take this present to the top of falador's general store, place it in the box at the top.");
                	player.getInventory().addItem(6542, 1);
                	stage = 4;
                } else if (stage == 4) { 
                	sendNPCDialogue(npcId, 9827, "When you have delivered the present meet me lumbridge for your next assignment.");
                	xmas = false;
                	stage = 5;
                } else if (stage == 5) { 
                	end();
                }
    		}


        @Override
        public void finish() {

        }
}