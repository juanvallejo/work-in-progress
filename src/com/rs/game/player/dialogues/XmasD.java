package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class XmasD extends Dialogue {

        private int npcId = 1552;

        @Override
        public void start() {
                sendEntityDialogue(SEND_2_TEXT_CHAT,
                                new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                                "Hello again, did you deliver the present?"}, IS_NPC, npcId, 9827);
        }
        
        @Override
        public void run(int interfaceId, int componentId) {
                if (stage == -1) {
                        sendOptionsDialogue("Did you deliver the present?", "Yes", "No.");
                        stage = 1;
                } else if (stage == 1) {
                        if (componentId == OPTION_1) {
                        	if (player.getInventory().containsItem(11339, 1))
                        	sendNPCDialogue(npcId, 9827, "Thank you so much! I will take those notes off of you.");
                        	player.getInventory().deleteItem(11339, 28);
                        	stage = 2;
                        }
                        else if (!player.getInventory().containsItem(11339, 1))
                        		sendNPCDialogue(npcId, 9827, "You have to bring me the notes you recieved!");
                                //stage = 2;
                                }
                        else if (componentId == OPTION_2) {
                                end();
                                }
                 else if (stage == 2) {
                	sendNPCDialogue(npcId, 9827, "The next present needs to be delivered to draynor manor, Veronica should be standing outside the gates, give her this present.");
                	player.getInventory().addItem(15420, 1);
                	stage = 3;
                } else if (stage == 3) {
                	sendPlayerDialogue(9827, "Okay santa, by the way, you lost a lot of weight! Well done!");
                	player.getInventory().deleteItem(11339, 5);
                	stage = 4;
                } else if (stage == 4) { 
                	sendNPCDialogue(npcId, 9827, "Thank you, well I like to ride my bike and its a great way to stay in shape, oh and by the way once you have delivered this present I will be at Yanille, meet me there.");
                	stage = 5;
                } else if (stage == 5) { 
                	end();
                }
                }

        @Override
        public void finish() {

        }
}