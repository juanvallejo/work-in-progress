package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.utils.Utils;

public class Turael extends Dialogue {

	
	int npcId;
	private void randomTask() {
	String[] slayerAssignment = { "Crawling hands", "Jellys", "Abyssal Demons", "Dark Beasts", "Ganodermic Beasts" } ;
	int i = Utils.getRandom(2);
	}

	
	@Override
	public void start() {
        sendEntityDialogue(SEND_2_TEXT_CHAT,
                new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                "Hello, I am Turael, the slayer master, I am able to assign you a slayer task."}, IS_NPC, npcId, 9827);
		
	}

	@Override
	public void run(int interfaceId, int componentId) {
		 if (stage == -1) {
			 sendPlayerDialogue(9828, "What can I do for you?");
             stage = 1;
		 } else if (stage == 1) {
             if (componentId == OPTION_1) {
            	 sendOptionsDialogue("Would can I do for you?", "I would like A slayer assignment please.", "What is my current slayer task?");
                     stage = 2;
             } else if (stage == 2) {
            	 if (componentId == OPTION_1 && player.hasTask == false) {
            	 sendNPCDialogue(npcId, 9827, "Your new slayer task is to kill "+player.slayerAmount+ "");
            	 player.hasTask = true;
            	 } else if (componentId == OPTION_2) {
            		 
            	 }
             }
		 }
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
	
}