package com.rs.game.player.dialogues;

import com.rs.utils.Utils;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class XmasS extends Dialogue {

        private int npcId = 9400;

        @Override
        public void start() {
                sendEntityDialogue(SEND_2_TEXT_CHAT,
                                new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                                "Ah my little helper, how did you get on?"}, IS_NPC, npcId, 9827);
        }
        
        @Override
        public void run(int interfaceId, int componentId) {
                if (stage == -1) {
                        sendOptionsDialogue("How did you get on?", "Fine.", "Not so well.");
                        stage = 1;
                } else if (stage == 1) {
                        if (componentId == OPTION_1) {
                        	if (player.getInventory().containsItem(6121, 1))
                        	sendPlayerDialogue(9827, "I Delivered the present to Veronica outside of Draynor Manor.");
                                stage = 2;
                                }
                        else if (componentId == OPTION_2) {
                                end();
                                }
                } else if (stage == 2) {
                	sendPlayerDialogue(9827, "She told me to give this to you.");
                	player.getInventory().deleteItem(6121, 28);
                	stage = 3;
                } else if (stage == 3) {
                	sendNPCDialogue(npcId, 9827, "Awhh she's a lovely person. Anyway heres your gift for helping me");
                	int[] RandomItems = { 14595, 14603, 14605, 14602, 4084, 6858, 6859, 6860, 6861, 10840, 15426 }; //Other ids go in there as well
            		player.getInventory().deleteItem(16, 1);
            		int i = Utils.getRandom(11);
            		player.getInventory().addItem(RandomItems[i], 1);
                	stage = 4;
                } else if (stage == 4) { 
                	sendNPCDialogue(npcId, 9827, "ere if your up to the challenge, I need you to take care of this problem, These frost dragons are taking over the north pole, if you can kill one you will be rewarded with a special gift.");
                	stage = 5;
                } else if (stage == 5) { 
                	sendOptionsDialogue("Kill the Frost Dragon?", "Slay the fucker.", "No thanks I dont want to train my combat.");
                	stage = 6;
                } else if (stage == 6) { 
                	 if (componentId == OPTION_1) {
                		 Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3735, 5551, 0));
                	 }
                	 else if (componentId == OPTION_2) {
                		 end();
                	 }
                }
                }

        @Override
        public void finish() {

        }
}