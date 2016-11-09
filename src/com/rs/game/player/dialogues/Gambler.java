package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.Gamble;

public class Gambler extends Dialogue {

        private int npcId;

        @Override
        public void start() {
                sendEntityDialogue(SEND_2_TEXT_CHAT,
                                new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                                "Hello, how much would you like to gamble?"}, IS_NPC, npcId, 9827);
        }
        
        @Override
        public void run(int interfaceId, int componentId) {
                if (stage == -1) {
                        sendOptionsDialogue("How much do you wish to gamble?", "10000gp (100k)",
                                        "100000gp (1mill)", "1000000gp (10mil)", "10000000gp (100m)", "I don't want to bet.");
                        stage = 1;
                } else if (stage == 1) {
                        if (componentId == OPTION_1) {
                                new Gamble(player, 100000);
                                end();
                                }
                        else if (componentId == OPTION_2) {
                                new Gamble(player, 1000000);
                                end();
                                }
                        else if (componentId == OPTION_3) {
                                new Gamble(player, 10000000);
                                end();
                                }
                        else if (componentId == OPTION_4) {
                                new Gamble(player, 100000000);
                                end();
                                }
                        else if (componentId == OPTION_5) {
                                player.getPackets().sendGameMessage("I hope you don't wimp out next time!");
                                end();
                                }
                        }
                }

        @Override
        public void finish() {

        }
}