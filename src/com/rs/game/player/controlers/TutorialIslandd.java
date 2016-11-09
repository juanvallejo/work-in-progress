package com.rs.game.player.controlers;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.InterfaceManager;

public class TutorialIslandd extends Controler {
	
	private static final int QUEST_GUIDE_NPC = 945;
	
	
	@Override
	public void start() {
		player.setYellOff(true);
		refreshStage();
		sendInterfaces();
		player.getDialogueManager()
				.startDialogue(
						"SimpleMessage",
						"Welcome To the Tutorial Island ",
						"To get started, speak with the Runescape Guide.");
		

	}
	
	@Override
	public void sendInterfaces() {
		int stage = getStage();
		if (stage == 0) {
		player.getInterfaceManager().sendOverlay(371, false);
		} else if (stage == 1) {
		//player.getPackets().sendConfig(406, 0);
		//InterfaceManager.sendInterfaceConfig(player, 371, 4, false);
		}
	}
	
	public void updateProgress() {
		setStage(getStage() + 1);
		if (getStage() == 2) {
			player.getDialogueManager().startDialogue("QuestGuide",
					QUEST_GUIDE_NPC, this);
		}
		refreshStage();
	}
	
	public NPC findNPC(int id) {
		// as it may be far away
		for (NPC npc : World.getNPCs()) {
			if (npc == null || npc.getId() != id)
				continue;
			return npc;
		}
		return null;
	}
	
	public void setStage(int stage) {
		getArguments()[0] = stage;
	}

	public int getStage() {
		if (getArguments() == null)
			setArguments(new Object[] { 0 }); // index 0 = stage
		return (Integer) getArguments()[0];
	}
	
	public void refreshStage() {
		int stage = getStage();
		if (stage == 0) {
			NPC guide = findNPC(QUEST_GUIDE_NPC);
			if (guide != null)
				player.getHintIconsManager().addHintIcon(guide, 0, -1, false);
		} else if (stage == 1) {
			player.getHintIconsManager().addHintIcon(3102, 3504, 0, 100, 0, 0,
					-1, false);
		} else if (stage == 2) {
			player.getHintIconsManager().addHintIcon(3092, 3521, 0, 0, 0, 0,
					-1, false);
		}
		sendInterfaces();
	}
	
	
	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}
	
	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage","A magical force prevents you from teleporting from the arena.");
		return false;
	}


}
