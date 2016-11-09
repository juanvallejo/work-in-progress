package com.rs.game.player.dialogues;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.player.Skills;
import com.rs.utils.ShopsHandler;

public class SkillerCape extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an option.",
				"I would like to see the requirements.", "I think im worth it.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			player.getInterfaceManager().sendInterface(275);
		    player.getPackets().sendIComponentText(275, 1, "Skiller Cape Requirements");
		    player.getPackets().sendIComponentText(275, 10, "");
		    player.getPackets().sendIComponentText(275, 11, "<u>You can access this page by using the command ::skill</u>");
		    player.getPackets().sendIComponentText(275, 12, "");
		    player.getPackets().sendIComponentText(275, 13, "You must have cooked 450 fish/meat " +player.cook+ "/450");
		    player.getPackets().sendIComponentText(275, 14, "You must have cut 750 gems: " +player.stones+ "/750");
		    player.getPackets().sendIComponentText(275, 15, "You must have completed 90 laps of the barbarian assult course: " +player.barbLap+ "/90");
		    player.getPackets().sendIComponentText(275, 16, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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
		    player.getPackets().sendIComponentText(275, 31, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		    player.getPackets().sendIComponentText(275, 32, "You must have mined 1000 ore of any kind: " +player.ore+ "/1000");
		    player.getPackets().sendIComponentText(275, 33, "");
		    player.getPackets().sendIComponentText(275, 34, "You have caught " +player.fish+ "/750 fish");
		    player.getPackets().sendIComponentText(275, 35, "You have used the chest"  +player.chest+ "/15");
		    player.getPackets().sendIComponentText(275, 36, "You have burned: " +player.logs+ "/250 logs.");
		    player.getPackets().sendIComponentText(275, 37, "You have cut: " +player.wc+ "/350 logs.");
		    player.getPackets().sendIComponentText(275, 38, "You have made: " +player.con+ "/750 beer barrels.");
		    player.getPackets().sendIComponentText(275, 39, "You have exchanged: " +player.slayer+ "/700 pieces of eight.");
		    player.getPackets().sendIComponentText(275, 40, "You must have stolen: " +player.thieve+ "/700 scimitars.");
		    player.getPackets().sendIComponentText(275, 41, "You must have completed: " +player.agility+ "/100 laps of the gnome agility course.");
		    player.getPackets().sendIComponentText(275, 42, "You must have voted 8 times for the server: " +player.voteee+ "/8");
		    player.getPackets().sendIComponentText(275, 43, "");
		    player.getPackets().sendIComponentText(275, 44, "");
		    player.getPackets().sendIComponentText(275, 45, "");
		    player.getPackets().sendIComponentText(275, 46, "");
		    player.getPackets().sendIComponentText(275, 47, "");
		    player.getPackets().sendIComponentText(275, 48, "");
		    player.getPackets().sendIComponentText(275, 49, "");
		    player.getPackets().sendIComponentText(275, 50, "");
		    for (int i = 51; i < 300; i++)
		    player.getPackets().sendIComponentText(275, i, "");
				end();
			}
		if (componentId == OPTION_2) {
			player.skillCape();
			if(!player.isCompletedSkill()) {
				player.setNextForceTalk(new ForceTalk("Because im so not worth it!"));
				player.setNextAnimation(new Animation(767));
		} else if (player.isCompletedSkill()) {
			ShopsHandler.openShop(player, 21);	
			}
		end();
		} else if (componentId == OPTION_3) {
		}
		end();
		}

	

	@Override
	public void finish() {

	}

}
