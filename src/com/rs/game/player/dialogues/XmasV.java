package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class XmasV extends Dialogue {

        private int npcId = 285;

        @Override
        public void start() {
                sendEntityDialogue(SEND_2_TEXT_CHAT,
                                new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                                "Hello, What can I do for you?"}, IS_NPC, npcId, 9827);
        }
        
        @Override
        public void run(int interfaceId, int componentId) {
                if (stage == -1) {
                        sendOptionsDialogue("What can I do for you?", "Santa sent me", "Nothing.");
                        stage = 1;
                } else if (stage == 1) {
                        if (componentId == OPTION_1) {
                        	if (player.getInventory().containsItem(15420, 1))
                        	sendPlayerDialogue(9827, "Santa sent me here to give you this present.?");
                        	player.getInventory().deleteItem(15420, 28);
                                stage = 2;
                                }
                        else if (componentId == OPTION_2) {
                                end();
                                }
                } else if (stage == 2) {
                	sendNPCDialogue(npcId, 9827, "Oh okay thank you, why did santa not come himself?");
                	stage = 3;
                } else if (stage == 3) {
                	sendPlayerDialogue(9827, "Santa couldnt make it, he fell down the stairs and broke his hip.");
                	stage = 4;
                } else if (stage == 4) { 
                	sendNPCDialogue(npcId, 9827, "OH MY!! Give him this letter from me, I hope he gets better soon!");
                	player.getInventory().addItem(6121, 1);
                	player.getPackets().sendGameMessage("Meet Santa at Yanille. (use the loadstone)");
                	stage = 5;
                } else if (stage == 5) { 
                	end();
                }
                }

        @Override
        public void finish() {

        }
}