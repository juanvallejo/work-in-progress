package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.player.Skills;
import com.rs.utils.ShopsHandler;

public class Cape extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an option.",
				"I would like to see the requirements.", "I would like the completionist cape please.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			player.checkIfCompleted();
			player.getInterfaceManager().sendInterface(275);
		    player.getPackets().sendIComponentText(275, 1, "Completionist Cape");
		    player.getPackets().sendIComponentText(275, 3, "");
		    player.getPackets().sendIComponentText(275, 4, "");
		    player.getPackets().sendIComponentText(275, 5, "");
		    player.getPackets().sendIComponentText(275, 6, "");
		    player.getPackets().sendIComponentText(275, 7, "");
		    player.getPackets().sendIComponentText(275, 8, "");
		    player.getPackets().sendIComponentText(275, 9, "");
		    player.getPackets().sendIComponentText(275, 10, "You are level " + player.getSkills().getLevel(Skills.ATTACK) + "/99 in attack.");
		    player.getPackets().sendIComponentText(275, 11, "You are level " + player.getSkills().getLevel(Skills.DEFENCE) + "/99 in defence.");
		    player.getPackets().sendIComponentText(275, 12, "You are level " + player.getSkills().getLevel(Skills.STRENGTH) + "/99 in strength.");
		    player.getPackets().sendIComponentText(275, 13, "You are level " + player.getSkills().getLevel(Skills.HITPOINTS) + "/99 in constitution.");
		    player.getPackets().sendIComponentText(275, 14, "You are level " + player.getSkills().getLevel(Skills.RANGE) + "/99 in range.");
		    player.getPackets().sendIComponentText(275, 15, "You are level " + player.getSkills().getLevel(Skills.PRAYER) + "/99 in prayer.");
		    player.getPackets().sendIComponentText(275, 16, "You are level " + player.getSkills().getLevel(Skills.MAGIC) + "/99 in magic.");
		    player.getPackets().sendIComponentText(275, 17, "You are level " + player.getSkills().getLevel(Skills.COOKING) + "/99 in cooking.");
		    player.getPackets().sendIComponentText(275, 18, "You are level " + player.getSkills().getLevel(Skills.WOODCUTTING) + "/99 in woodcutting.");
		    player.getPackets().sendIComponentText(275, 19, "You are level " + player.getSkills().getLevel(Skills.FLETCHING) + "/99 in fletching.");
		    player.getPackets().sendIComponentText(275, 20, "You are level " + player.getSkills().getLevel(Skills.FISHING) + "/99 in fishing.");
		    player.getPackets().sendIComponentText(275, 21, "You are level " + player.getSkills().getLevel(Skills.FIREMAKING) + "/99 in firemaking.");
		    player.getPackets().sendIComponentText(275, 22, "You are level " + player.getSkills().getLevel(Skills.CRAFTING) + "/99 in crafting.");
		    player.getPackets().sendIComponentText(275, 23, "You are level " + player.getSkills().getLevel(Skills.SMITHING) + "/99 in smithing.");
		    player.getPackets().sendIComponentText(275, 24, "You are level " + player.getSkills().getLevel(Skills.MINING) + "/99 in mining.");
		    player.getPackets().sendIComponentText(275, 25, "You are level " + player.getSkills().getLevel(Skills.HERBLORE) + "/99 in herblore.");
		    player.getPackets().sendIComponentText(275, 26, "You are level " + player.getSkills().getLevel(Skills.AGILITY) + "/99 in agility.");
		    player.getPackets().sendIComponentText(275, 27, "You are level " + player.getSkills().getLevel(Skills.THIEVING) + "/99 in thieving.");
		    player.getPackets().sendIComponentText(275, 28, "You are level " + player.getSkills().getLevel(Skills.SLAYER) + "/99 in slayer.");
		    player.getPackets().sendIComponentText(275, 29, "You are level " + player.getSkills().getLevel(Skills.FARMING) + "/99 in farming.");
		    player.getPackets().sendIComponentText(275, 30, "You are level " + player.getSkills().getLevel(Skills.RUNECRAFTING) + "/99 in runecrafting.");
		    player.getPackets().sendIComponentText(275, 31, "You are level " + player.getSkills().getLevel(Skills.SUMMONING) + "/99 in summoning.");
		    player.getPackets().sendIComponentText(275, 32, "You are level " + player.getSkills().getLevel(Skills.DUNGEONEERING) + "/120 in dungeoneering.");
		    player.getPackets().sendIComponentText(275, 33, "");
		    player.getPackets().sendIComponentText(275, 34, "You have fished " + player.fish + "/750 fish.");
		    player.getPackets().sendIComponentText(275, 35, "You have burned " + player.logs + "/250 logs.");
		    player.getPackets().sendIComponentText(275, 36, "You have chopped " + player.wc + "/350 logs.");
		    player.getPackets().sendIComponentText(275, 37, "You have opened " + player.chest + "/15 crystal key chests.");
		    player.getPackets().sendIComponentText(275, 38, "You have stolen " + player.thieve + "/700 rune scimitars.");
		    player.getPackets().sendIComponentText(275, 39, "You have obtained " + player.slayer + "/700 pieces of eight.");
		    player.getPackets().sendIComponentText(275, 40, "");
		    player.getPackets().sendIComponentText(275, 41, "");
		    player.getPackets().sendIComponentText(275, 42, "");
		    player.getPackets().sendIComponentText(275, 43, (player.isKilledQueenBlackDragon() ? "You have killed the QBD!" :"You must have killed QBD"));
		    player.getPackets().sendIComponentText(275, 44, "");
		    player.getPackets().sendIComponentText(275, 45, "");
		    player.getPackets().sendIComponentText(275, 46, "");
		    player.getPackets().sendIComponentText(275, 47, "" +player.doneComp);
		    player.getPackets().sendIComponentText(275, 48, "");
		    player.getPackets().sendIComponentText(275, 49, "");
		    player.getPackets().sendIComponentText(275, 50, "");
		    for (int i = 51; i < 300; i++)
		    player.getPackets().sendIComponentText(275, i, "");
				end();
			}
		if (componentId == OPTION_2) {
			player.completedCompletionistCape();
			if(!player.isCompletedComp()) {
				player.setNextForceTalk(new ForceTalk("Because im so not worth it!"));
				player.setNextAnimation(new Animation(767));
		} else if (player.isCompletedComp()) {
			ShopsHandler.openShop(player, 29);	
			}
		end();
		} else if (componentId == OPTION_3) {
			end();
		}
		end();
		
		}

	

	@Override
	public void finish() {

	}

}
