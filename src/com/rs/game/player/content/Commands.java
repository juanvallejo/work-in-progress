package com.rs.game.player.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.Settings;
import com.rs.cache.loaders.AnimationDefinitions;
import com.rs.cache.loaders.IComponentDefinitions;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.minigames.FightPits;
import com.rs.game.minigames.clanwars.ClanWars;
import com.rs.game.minigames.clanwars.WallHandler;
import com.rs.game.minigames.pest.PestControl;
import com.rs.game.npc.NPC;
import com.rs.game.npc.others.Bork;
import com.rs.game.player.ArdyFarming;
import com.rs.game.player.Player;
import com.rs.game.player.InterfaceManager;
import com.rs.game.player.PublicChatMessage;
import com.rs.game.player.Skills;
import com.rs.game.player.Inventory;
import com.rs.game.player.content.DonatorBoss;
import com.rs.game.player.content.Commands;
import com.rs.game.player.content.Notes.Note;
import com.rs.game.player.content.construction.House;
import com.rs.game.player.content.dungeoneering.DungeonPartyManager;
import com.rs.game.player.content.pet.Pets;
import com.rs.game.player.controlers.FightKiln;
import com.rs.game.player.cutscenes.HomeCutScene;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.dialogues.StarSprite;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.DisplayNames;
import com.rs.utils.DonationManager;
import com.rs.utils.HashTag;
import com.rs.utils.Hiscores;
import com.rs.utils.ShopsHandler;
import com.rs.utils.NPCSpawns;
import com.rs.utils.ObjectSpawns;
import com.rs.utils.NPCBonuses;
import com.rs.utils.Donations;
import com.rs.utils.Encrypt;
import com.rs.utils.IPBanL;
import com.rs.utils.NPCSpawns;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.NPCSpawns;
import com.rs.utils.VoteManager;
import com.rs.utils.Utils;
import com.rs.utils.Logger;
import com.rs.utils.Watch;
import com.rs.utils.huffman.Huffman;
import org.runetoplist.*;



/*
 * doesnt let it be extended
 */
public final class Commands {

public static boolean canExchange(Player player){

if(player.getInventory().containsItem(995, 100000000)){
player.getInventory().deleteItem(995, 100000000);
player.getInventory().addItem(2996, 1);
return true;

} else {

player.getPackets().sendGameMessage("<col=FFFF00>Tickets are 100m each.</col>");
return false;

}

}

public static boolean canExchangeBack(Player player){

if(player.getInventory().containsItem(2996, 1)){
player.getInventory().deleteItem(2996, 1);
player.getInventory().addItem(995, 100000000);
return true;

} else {

player.getPackets().sendGameMessage("<col=FFFF00>You dont have a ticket on you.</col>");
return false;

}

}

	/*
	 * all console commands only for admin, chat commands processed if they not
	 * processed by console
	 */

	/**
	 * returns if command was processed
	 */
	public static boolean processCommand(Player player, String command,
			boolean console, boolean clientCommand) {
		if (command.length() == 0) // if they used ::(nothing) theres no command
			return false;
		String[] cmd = command.toLowerCase().split(" ");
		if (cmd.length == 0)
			return false;
		if (player.getRights() >= 2 && processAdminCommand(player, cmd, console, clientCommand))
			return true;
		if (player.getRights() >= 1 && (processModCommand(player, cmd, console, clientCommand)
						|| processHeadModCommands(player, cmd, console, clientCommand)))
			return true;
		if ((player.isSupporter() || player.getRights() >= 1) && processSupportCommands(player, cmd, console, clientCommand))
			return true;
		if (Settings.ECONOMY) {
			player.getPackets().sendGameMessage("You can't use any commands in economy mode!");
			return true;
		}
		return processNormalCommand(player, cmd, console, clientCommand);
	}

	/*
	 * extra parameters if you want to check them
	 */
 //@suppressWarnings("unused")
	public static boolean processAdminCommand(final Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {
			if (cmd[0].equalsIgnoreCase("tele")) {
				cmd = cmd[1].split(",");
				int plane = Integer.valueOf(cmd[0]);
				int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
				int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
				player.setNextWorldTile(new WorldTile(x, y, plane));
				return true;
			}
		} else {
			String name;
			Player target;
			WorldObject object;
			if (cmd[0].equalsIgnoreCase("unban")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: "+target.getDisplayName()+".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if(!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("killwithin") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				List<Integer> npcs = World.getRegion(player.getRegionId()).getNPCsIndexes();
				for(int index = 0; index < npcs.size() + 1; index++)
				World.getNPCs().get(npcs.get(index)).sendDeath(player);	
				return true;
			}
			if (cmd[0].equalsIgnoreCase("packobjectspawns") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				ObjectSpawns.packObjectSpawns();
				player.getPackets().sendGameMessage("You packed object spawns.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("packnpcbonuses") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				NPCBonuses.loadUnpackedNPCBonuses();
				player.getPackets().sendGameMessage("You packed NPC bonuses.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("packshops") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
    				ShopsHandler.loadUnpackedShops();
    				player.getPackets().sendGameMessage("You Packed The Shops.");
    				return true;
   			}
			if (cmd[0].equalsIgnoreCase("star") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				player.recievedGift = false;
				player.starSprite = true;
				return true;
			}
			if (cmd[0].equalsIgnoreCase("resetlost")) {
				player.spokeToWarrior = false;
				player.spokeToShamus = false;
				player.spokeToMonk = false;
				player.lostCity = 0;
				player.getPackets().sendGameMessage("Reset Lost-City.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("resetprestige") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				player.prestigeNumber--;
				player.getPackets().sendGameMessage("You now have: "+player.prestigeNumber+" prestige tokens.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("checkbank")) {
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player Other = World.getPlayerByDisplayName(name);
					try {
							player.getPackets().sendItems(95, Other.getBank().getContainerCopy());
								player.getBank().openPlayerBank(Other);
						} catch (Exception e){
							}
						return true;
			}
			if (cmd[0].equalsIgnoreCase("sendchat")) {
				String name21 = String.valueOf(cmd[1]);
				target = World.getPlayerByDisplayName(name21);
				if(target != null){
					String message21 = String.valueOf(cmd[2]);
					target.sendPublicChatMessage(new PublicChatMessage(Utils.fixChatMessage(message21), 0));
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("sendwalk")) {
				String name21 = String.valueOf(cmd[1]);
				target = World.getPlayerByDisplayName(name21);
				if(target != null){
					int x = Integer.parseInt(cmd[2]);
		            int y = Integer.parseInt(cmd[3]);
					player.addWalkSteps(player.getX() + Utils.getRandom(x), player.getY() + Utils.getRandom(y), 0, false);
					player.out("X = "+Utils.getRandom(x)+ ", Y = "+Utils.getRandom(y));
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("randomevent")) {
					player.playersX = player.getX();
					player.playersY = player.getY();
					player.playersP = player.getPlane();
					player.out("X = "+player.playersX+ ", Y = "+player.playersY+", Plane = "+player.playersP);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("dh")) {
				player.setHitpoints(1);
				player.refreshHitPoints();
				return true;
			}
			if (cmd[0].equalsIgnoreCase("addgp")) {
				int number = Integer.parseInt(cmd[1]);
				int coins = player.getInventory().getNumerOf(995);
				if (player.getInventory().containsItem(995, number)) {
				player.coinsInPouch += number;
				player.getInventory().deleteItem(995 , number);
				player.out("You add "+Shop.commas(Integer.toString(number))+" coins to your Bank.");
				} else {
					player.out("You only have "+Shop.commas(Integer.toString(coins))+" in your inventory.");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("getgp")) {
				player.out("You currently have "+Shop.commas(Integer.toString(player.coinsInPouch))+" coins in your Bank.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("removegp")) {
				int number = Integer.parseInt(cmd[1]);
				String format = String.valueOf(cmd[2]);
				int coins = player.getInventory().getNumerOf(995);
				if (format.equalsIgnoreCase("k")) {
					number = number * 1000;
				} else if (format.equalsIgnoreCase("m")) {
					number = number * 1000000;
				}
				if (player.coinsInPouch >= number) {
				player.coinsInPouch -= number;
				player.getInventory().addItem(995, number);
				player.out("You remove "+Shop.commas(Integer.toString(number))+" coins from your Bank.");
				} else {
					player.out("You only have "+Shop.commas(Integer.toString(player.coinsInPouch))+" in your Bank.");
				}
				return true;
			}
			/*if (cmd[0].equalsIgnoreCase("dclaws")) {
				int amount = 20000000;
				int number = Integer.parseInt(cmd[1]);
				int calc = number * amount;
				if (!player.getInventory().containsItem(995, calc)) {
					player.sm("You do not have enough coins for "+number+" Dragon Claws.");
						} else {
				player.getInventory().addItem(14484, number);
				player.getInventory().deleteItem(995, calc);
				}
			}*/
			if (cmd[0].equalsIgnoreCase("setlevelother") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
            int skill = Integer.parseInt(cmd[2]);
            int level = Integer.parseInt(cmd[3]);
            other.getSkills().set(Integer.parseInt(cmd[2]),
                    Integer.parseInt(cmd[3]));
            other.getSkills().set(skill, level);
            other.getSkills().setXp(skill, Skills.getXPForLevel(level));
            other.getPackets().sendGameMessage("One of your skills ("
                    + Skills.SKILL_NAME[skill]
                    + ") has been set to " + level + " by "
                    + player.getDisplayName() + ".");
            player.getPackets().sendGameMessage("You have set " + other.getDisplayName() + "'s "
                    + Skills.SKILL_NAME[skill] + " level to " + level
                    + ".");
            //}
            return true;
			}
			if (cmd[0].equalsIgnoreCase("modroll")) {
				String namee = "";
				for (int i = 1; i < cmd.length; i++)
				namee += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2075));
				player.setNextForceTalk(new ForceTalk("Rolled <col=FF0000>" + namee + "</col> " + "on the percentile dice"));
			}
			if (cmd[0].equalsIgnoreCase("comp") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				player.setCompletedComp();
				player.getPackets().sendGameMessage("because your worth it!");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("houseids") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1883, 5115, 0));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("takefrombank")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null) {
					player.getPackets().sendGameMessage(
							"There is no such player as " + username + ".");
					return true;
				}
				int item = Integer.parseInt(cmd[2]);
				other.getBank().removeItem(item);
			}
			if (cmd[0].equalsIgnoreCase("addtobank") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null) {
					player.getPackets().sendGameMessage(
							"There is no such player as " + username + ".");
					return true;
				}
				int item = Integer.parseInt(cmd[2]);
				int amount = Integer.parseInt(cmd[3]);
				other.getBank().addItem(item, amount, false);
			}
			if (cmd[0].equalsIgnoreCase("takefrominv")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null) {
					player.getPackets().sendGameMessage(
							"There is no such player as " + username + ".");
					return true;
				}
				int item = Integer.parseInt(cmd[2]);
				int amount = Integer.parseInt(cmd[3]);
				other.getInventory().deleteItem(item, amount);
			}
			if (cmd[0].equalsIgnoreCase("getchunk")) {
				player.out("Player ChunkX: " + player.getChunkX() + ", Player ChunkY: " + player.getChunkY());
				House.sendXMessage(player);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("getip")) {
				String playrName = "";
				for (int i = 1; i < cmd.length; i++)
					playrName += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player p = World.getPlayerByDisplayName(playrName);
				if (p == null) {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + playrName + ".");
					// if (p.getDisplayName().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					// 	player.out("" + p.getDisplayName() + "'s IP is "+ player.getSession().getIP() + ".");
					// }
				} else {
					player.getPackets().sendGameMessage(
							"" + p.getDisplayName() + "'s IP is "
									+ p.getSession().getIP() + ".");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("xmas")) {
				player.getDialogueManager().startDialogue("xmas");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("mspawn")) {
			try {
			if (cmd.length < 3) {
				return true;
			}
			if (Integer.parseInt(cmd[2]) > 10) {
				return true;
			}
			ArrayList<WorldTile> locations = new ArrayList<WorldTile>();
			for (int x = player.getX() - Integer.parseInt(cmd[2]); x < player
					.getX() + Integer.parseInt(cmd[2]); x++) {
				for (int y = player.getY() - Integer.parseInt(cmd[2]); y < player
						.getY() + Integer.parseInt(cmd[2]); y++)
					locations.add(new WorldTile(x, y, 0));
			}
			for (WorldTile loc : locations) {
				if (!World.canMoveNPC(loc.getPlane(), loc.getX(),
						loc.getY(), 1))
					continue;
				World.spawnNPC(Integer.parseInt(cmd[1]), loc, -1, true,
						true);
			}
		} catch (NumberFormatException e) {
		}
			return true;
			}
			if (cmd[0].equalsIgnoreCase("p")) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("./deleteobjects.txt", true));
				bw.newLine();
				bw.write("World.deleteObject(new WorldTile("+ player.getX() + ", " + player.getY() + ", " + player.getPlane() + "));");
				bw.flush();
				bw.newLine();
				bw.close();
			} catch (Throwable t) {		
			}
			return true;
			}
			if (cmd[0].equalsIgnoreCase("n")) {
				
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("./npc.txt", true));
				bw.write("5363 - " +player.getX() + " " + player.getY() + " " + player.getPlane());
				bw.flush();
				bw.newLine();
				bw.close();
			} catch (Throwable t) {		
			}
			return true;
			}
			if (cmd[0].equalsIgnoreCase("update")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Settings.recentUpdate = name;
				player.out("Latest Update = "+Settings.recentUpdate);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("removebank")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null) {
					player.getPackets().sendGameMessage(
							"There is no such player as " + username + ".");
					return true;
				}
				int item = Integer.parseInt(cmd[2]);
				other.getBank().removeItem(item);
			}
			if (cmd[0].equalsIgnoreCase("toggleupdate")) {
			player.switchUpdate();
			player.getPackets().sendGameMessage("Login update message is " + (player.toggleUpdate ? "disabled" : "enabled"));
			return true;
			}
			if (cmd[0].equalsIgnoreCase("prestige")) {
				player.getDialogueManager().startDialogue("PrestigeOne");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("givecomp") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.setCompletedComp();
				//other.completedDwarfCannonQuest = true;
				other.getPackets().sendGameMessage("because your worth it!");
				return true;
			}

			if (cmd[0].equalsIgnoreCase("savehigh") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				Hiscores.saveHighScore(other);
				player.getPackets().sendGameMessage("You have saved the highscores.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("sgar")) {
				player.getControlerManager().startControler("SorceressGarden");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("randommm")) {
				WorldTile tile = new WorldTile(3280, 3499, 0);
				WorldTile tile2 = new WorldTile(1234, 1234, 0);
				WorldTile tile3 = new WorldTile(3245, 3509, 0);
				WorldTile[] tiles = {tile, tile2, tile3};
				Magic.sendNormalTeleportSpell(player, 0, 0, tiles[Utils.random(0, 3)]);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("customtitle")) {
				String title = "";
			    for (int i = 1; i < cmd.length; i++)
			     title += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			    player.setLoyalty(title);
			    player.getAppearence().setTitle(54756);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("skillset") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				player.getInventory().addItem(2362, 5000);
				player.getInventory().addItem(2364, 5000);
				player.getInventory().addItem(15271, 5000);
				player.getInventory().addItem(384, 5000);
				return true;
			}
			if(cmd[0].equalsIgnoreCase("item")) {
				//if (player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				if (cmd.length < 2) {
					player.getPackets().sendGameMessage(
							"Use: ::item id (optional:amount)");
					return true;
				}
				try {
					int itemId = Integer.valueOf(cmd[1]);
					player.getInventory().addItem(itemId,
							cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
					player.stopAll();
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage(
							"Use: ::item id (optional:amount)");
				//	}
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("itemn")) {
				StringBuilder sb = new StringBuilder(cmd[1]);
				int amount = 1;
				if (cmd.length > 2) {
					for (int i = 2; i < cmd.length; i++) {
						if (cmd[i].startsWith("+")) {
							amount = Integer.parseInt(cmd[i].replace("+", ""));
						} else {
							sb.append(" ").append(cmd[i]);
						}
					}
				}
				String namee = sb.toString().toLowerCase().replace("[", "(")
						.replace("]", ")").replaceAll(",", "'");
				for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
					ItemDefinitions def = ItemDefinitions
							.getItemDefinitions(i);
					if (def.getName().toLowerCase().equalsIgnoreCase(namee)) {
						player.getInventory().addItem(i, amount);
						player.stopAll();
						player.getPackets().sendGameMessage("Found item " + namee + " - id: " + i + ".");
						return true;
					}
				}
				player.getPackets().sendGameMessage(
						"Could not find item by the name " + namee + ".");
			return true;
		}
			if (cmd[0].equalsIgnoreCase("getinv")) {
                if(cmd[1].length() == 0) {
                    return false;
                }
                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                String amount;
                Player player2 = World.getPlayer(cmd[1]);
                if (player2 == null) {
                	return false;
                }
                int player2freeslots = player2.getInventory().getFreeSlots();
                int player2usedslots = 28 - player2freeslots;
                  
                player.getPackets().sendGameMessage("----- Inventory Information -----");
                player.getPackets().sendGameMessage("<col=DF7401>" + Utils.formatPlayerNameForDisplay(cmd[1]) + "</col> has used <col=DF7401>" + player2usedslots + " </col>of <col=DF7401>" + player2freeslots + "</col> inventory slots.");
                player.getPackets().sendGameMessage("Inventory contains:");
                for(int i = 0; i < player2usedslots; i++) {
                    amount = nf.format(player2.getInventory().getItems().getNumberOf(player2.getInventory().getItems().get(i).getId()));
                    player.getPackets().sendGameMessage("<col=088A08>" + amount + "</col><col=BDBDBD> x </col><col=088A08>" +  player2.getInventory().getItems().get(i).getName());
                      
                }
                player.getPackets().sendGameMessage("--------------------------------");
                player.out(player2.getUsername()+"'s money pouch contains: "+player.getFormattedNumber(player2.money)+" coins.");
                return true;
			}
			if (cmd[0].equalsIgnoreCase("setspins") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.setSpins(Integer.parseInt(cmd[2]));
				player.getPackets().sendGameMessage("You gave them some spins");
				other.getPackets().sendGameMessage("You have recived some spins!");
                                other.refreshSqueal();
				return true;
			}
			if (cmd[0].equalsIgnoreCase("telealltome") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					for (Player everyone : World.getPlayers()) {
						everyone.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getPlane()));
					}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("up")) {
				player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getPlane() + 1));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("down")) {
				player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getPlane() - 1));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("emoters")) {//
				WorldTasksManager.schedule(new WorldTask() {
					int loop = 1280;
					@Override
					public void run() {
						player.setNextGraphics(new Graphics(loop, 0, 0));
						//player.setNextAnimation(new Animation(loop));
						player.out("Emote ID: "+loop);
							loop++;
							}
						}, 0, 1);
			}//1166, 1222, 1228, 1264
			if (cmd[0].equalsIgnoreCase("configg")) {
				WorldTasksManager.schedule(new WorldTask() {
					int loop = 432;
					@Override
					public void run() {
						player.getPackets().sendConfig(432, 3072);
						player.out("Config ID: "+loop);
							loop++;
							}
						}, 0, 1);
			return true; 
			}
			if (cmd[0].equalsIgnoreCase("rf")) {
					WorldTasksManager.schedule(new WorldTask() {
						int loop1 = 2400;//2400
						@Override
						public void run() {
							player.getPackets().sendConfig(loop1, 1);
							player.out("Config Id: "+loop1);
								loop1++;
								}
							}, 0, 1);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("lf")) {	
					WorldTasksManager.schedule(new WorldTask() {
						int loop = 0;
						@Override
						public void run() {
							player.getPackets().sendConfigByFile(713, loop);
							player.out("Config Id: "+loop);
								loop++;
								}
							}, 0, 1);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("camera")) {
				//player.out("Camera X: "+ player.getLocalX() + ", Camera Y: " +player.getLocalY());
				//player.firstScene = false;
				player.getInterfaceManager().sendInterface(1160);
		        player.getPackets().sendIComponentText(1160, 75,  "Daily Challange Noticeboard");
		        player.getPackets().sendIComponentText(1160, 41,  "Skilling:");
		        player.getPackets().sendIComponentText(1160, 44,  "Woodcutting:");
		        player.getPackets().sendIComponentText(1160, 46,  "Mining:");
		        player.getPackets().sendIComponentText(1160, 48,  "Smithing:");
		        player.getPackets().sendIComponentText(1160, 50,  "Farming:");
		        player.getPackets().sendIComponentText(1160, 52,  "Prayer:");
		        player.getPackets().sendIComponentText(1160, 54,  "Summoning:");
		        player.getPackets().sendIComponentText(1160, 56,  "Construction:");
		        
		        player.getPackets().sendIComponentText(1160, 26,  "");
		        player.getPackets().sendIComponentText(1160, 28,  "");
		        player.getPackets().sendIComponentText(1160, 30,  "");
		        player.getPackets().sendIComponentText(1160, 32,  "");
		        player.getPackets().sendIComponentText(1160, 34,  "");
		        player.getPackets().sendIComponentText(1160, 36,  "");
		        player.getPackets().sendIComponentText(1160, 39,  "");
		        player.getPackets().sendIComponentText(1160, 40,  "");
		}
			if (cmd[0].equalsIgnoreCase("farmscene")) {
				player.getCutscenesManager().play("FarmingCutScene");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("resetlode")) {
				player.lumby = false;
				player.draynor = false;
				player.port = false;
				player.alkarid = false;
				player.varrock = false;
				player.falador = false;
				player.burth = false;
				player.tav = false;
				player.adrougne = false;
				player.cath = false;
				player.seers = false;
				player.yanille = false;
				player.edge = false;
				player.out("You reset the lodestones.");
				player.refreshLodestoneNetwork();
				return true;
			}
			if (cmd[0].equalsIgnoreCase("setkiln") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.setCompletedFightKiln();
				other.getInventory().addItem(23659, 1);
				player.getPackets().sendGameMessage("You completed fight kiln for them");
				other.getPackets().sendGameMessage("You have completed fight kiln!");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("setdung") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				//player.getSkills().set(24, 120);
				player.getSkills().setXp(24, Skills.getXPForLevel(120));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("setcaves") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.setCompletedFightCaves();
				other.getInventory().addItem(23659, 1);
				player.getPackets().sendGameMessage("You " +
						"leted fight kiln for them");
				other.getPackets().sendGameMessage("You have completed fight kiln!");
                                other.refreshSqueal();
				return true;
			}
			if (cmd[0].equalsIgnoreCase("scg")) {
				player.getControlerManager().startControler("StealingCreationsGame", true);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("zombie")) {
				player.getPackets().sendGameMessage("You have: "+player.zombie+" zombie points.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("setrights") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
				return true;
				other.setRights(Integer.parseInt(cmd[2]));
				if (other.getRights() > 0) {
				other.out("Congratulations, You have been promoted to "+ (player.getRights() == 2 ? "Admin" : "Mod") +".");
				} else {
					other.out("Unfortunately you have been demoted.");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("pm")) {
				player.getPackets().sendPrivateMessage("test1", "hi");
				player.getPackets().receivePrivateMessage("test1", "test1", 2, "Yo bro.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("configsize")) {
				player.getPackets().sendGameMessage("Config definitions size: 2633, BConfig size: 1929.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("npcmask")) {
				for (NPC n : World.getNPCs()) {
					if (n != null && Utils.getDistance(player, n) < 9) {
						n.setNextForceTalk(new ForceTalk("Boo!"));
					}
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("house")) {
				player.getDialogueManager().startDialogue("HouseTeleport");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("wguild")) {
				player.getControlerManager().startControler("WGuildControler");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("refreshtokens")) {
				player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 10 : 8, 1057);
				player.getPackets().sendConfig(2030, player.wGuildTokens);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("warn")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.blackMark++;
				player.warnLog(player, username);
					player.getPackets().sendGameMessage("You have warned " +other.getDisplayName()+ ". They now have " + other.blackMark+ " black marks.");
					other.getPackets().sendGameMessage("You have recieved a black mark from "+player.getDisplayName()+ ". You now have "+other.blackMark+ " black marks.");
					other.setNextForceTalk(new ForceTalk("I have been warned. I am now on "+other.blackMark+" black marks."));
				if (other.blackMark >= 3) {
					player.setNextForceTalk(new ForceTalk(other.getDisplayName()+ " has been warned 3 times and has been banned for 48 hours."));
					player.getPackets().sendGameMessage("You have warned: " +other.getDisplayName()+ " they are now on: " + other.blackMark);
					other.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					other.getSession().getChannel().close();
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("takemarks")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				if (other.blackMark == 0) {
					player.out("You cannot go into negative numbers.");
					return true;
				}
				other.blackMark --;
				other.getPackets().sendGameMessage("You now have " +player.blackMark+" black marks.");
				player.getPackets().sendGameMessage("You remove a black mark from " + other.getDisplayName() +". They are now on "+other.blackMark+" black marks.");
			}
			if (cmd[0].equalsIgnoreCase("setpoints") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.increaseDonatorPoints();
				other.getPackets().sendGameMessage("You now have: " +player.donatorPoints);
				player.getPackets().sendGameMessage("You give them a point, they now have: " +other.donatorPoints);
			}
			if (cmd[0].equalsIgnoreCase("donor") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				player.increaseDonatorPoints();
				player.getPackets().sendGameMessage("You now have: " +player.donatorPoints);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("donorshop")) {
				player.getDialogueManager().startDialogue("DonatorShop");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("triviapoints")) {
				player.getPackets().sendGameMessage("I have: "+player.TriviaPoints+" trivia points.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("cape")) {
				player.getDialogueManager().startDialogue("Cape");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("qbd")) {
				if (player.getSkills().getLevelForXp(Skills.SUMMONING) < 60) {
					player.getPackets().sendGameMessage("You need a summoning level of 60 to go through this portal.");
					player.getControlerManager().removeControlerWithoutCheck();
					return true;
				}
				player.lock();
				player.getControlerManager().startControler("QueenBlackDragonControler");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("killall") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				int hitpointsMinimum = cmd.length > 1 ? Integer.parseInt(cmd[1]) : 0;
				for (Player p : World.getPlayers()) {
					if (p == null || p == player) {
						continue;
					}
					if (p.getHitpoints() < hitpointsMinimum) {
						continue;
					}
					p.applyHit(new Hit(p, p.getHitpoints(), HitLook.REGULAR_DAMAGE));
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("killingfields")) {
				player.getControlerManager().startControler("KillingFields");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("nntest")) {
				Dialogue.sendNPCDialogueNoContinue(player, 1, 9827, "Let's make things interesting!");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("pptest")) {
				player.getDialogueManager().startDialogue("SimplePlayerMessage", "123");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("debugobjects")) {
				System.out.println("Standing on " + World.getObject(player));
				Region r = World.getRegion(player.getRegionY() | (player.getRegionX() << 8));
				if (r == null) {
					player.getPackets().sendGameMessage("Region is null!");
					return true;
				}
				List<WorldObject> objects = r.getObjects();
				if (objects == null) {
					player.getPackets().sendGameMessage("Objects are null!");
					return true;
				}
				for (WorldObject o : objects) {
					if (o == null || !o.matches(player)) {
						continue;
					}
					System.out.println("Objects coords: "+o.getX()+ ", "+o.getY());
					System.out.println("[Object]: id=" + o.getId() + ", type=" + o.getType() + ", rot=" + o.getRotation() + ".");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("telesupport")) {
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter())
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("telemods")) {
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() != 1)
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("telestaff")) {
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter() && staff.getRights() != 1)
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("teleall")) {
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter() && staff.getRights() != 1 && player.getRights() != 0)
						continue;
					staff.setNextWorldTile(player);
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("pickuppet")) {
				if (player.getPet() != null) {
					player.getPet().pickup();
					return true;
				}
				player.getPackets().sendGameMessage("You do not have a pet to pickup!");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("restartfp")) {
				FightPits.endGame();
				player.getPackets().sendGameMessage("Fight pits restarted!");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("modelid")) {
				int id = Integer.parseInt(cmd[1]);
				player.getPackets().sendMessage(99, 
						"Model id for item " + id + " is: " + ItemDefinitions.getItemDefinitions(id).modelId, player);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("teletome")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else
					target.setNextWorldTile(player);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("tpme")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else
					target.setNextWorldTile(player);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("pos")) {
				try {
					File file = new File("data/positions.txt");
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							file, true));
					writer.write("|| player.getX() == " + player.getX()
							+ " && player.getY() == " + player.getY() + "");
					writer.newLine();
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("agilitytest")) {
				player.getControlerManager().startControler("BrimhavenAgility");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("partyroom")) {
				player.getInterfaceManager().sendInterface(647);
				player.getInterfaceManager().sendInventoryInterface(336);
				player.getPackets().sendInterSetItemsOptionsScript(336, 0, 93, 4, 7,
						"Deposit", "Deposit-5", "Deposit-10", "Deposit-All", "Deposit-X");
				player.getPackets().sendIComponentSettings(336, 0, 0, 27, 1278);
				player.getPackets().sendInterSetItemsOptionsScript(336, 30, 90, 4, 7, "Value");
				player.getPackets().sendIComponentSettings(647, 30, 0, 27, 1150);
				player.getPackets().sendInterSetItemsOptionsScript(647, 33, 90, true, 4, 7, "Examine");
				player.getPackets().sendIComponentSettings(647, 33, 0, 27, 1026);   
				ItemsContainer<Item> store = new ItemsContainer<>(215, false);
				for(int i = 0; i < store.getSize(); i++) {
					store.add(new Item(1048, i));
				}
				player.getPackets().sendItems(529, true, store); //.sendItems(-1, -2, 529, store);

				ItemsContainer<Item> drop = new ItemsContainer<>(215, false);
				for(int i = 0; i < drop.getSize(); i++) {
					drop.add(new Item(1048, i));
				}
				player.getPackets().sendItems(91, true, drop);//sendItems(-1, -2, 91, drop);

				ItemsContainer<Item> deposit = new ItemsContainer<>(8, false);
				for(int i = 0; i < deposit.getSize(); i++) {
					deposit.add(new Item(1048, i));
				}
				player.getPackets().sendItems(92, true, deposit);//sendItems(-1, -2, 92, deposit);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("objectname")) {
				name = cmd[1].replaceAll("_", " ");
				String option = cmd.length > 2 ? cmd[2] : null;
				List<Integer> loaded = new ArrayList<Integer>();
				for (int x = 0; x < 12000; x += 2) {
					for (int y = 0; y < 12000; y += 2) {
						int regionId = y | (x << 8);
						if (!loaded.contains(regionId)) {
							loaded.add(regionId);
							Region r = World.getRegion(regionId, false);
							r.loadRegionMap();
							List<WorldObject> list = r.getObjects();
							if (list == null) {
								continue;
							}
							for (WorldObject o : list) {
								if (o.getDefinitions().name
										.equalsIgnoreCase(name)
										&& (option == null || o
										.getDefinitions()
										.containsOption(option))) {
									System.out.println("Object found - [id="
											+ o.getId() + ", x=" + o.getX()
											+ ", y=" + o.getY() + "]");
									// player.getPackets().sendGameMessage("Object found - [id="
									// + o.getId() + ", x=" + o.getX() + ", y="
									// + o.getY() + "]");
								}
							}
						}
					}
				}
				/*
				 * Object found - [id=28139, x=2729, y=5509] Object found -
				 * [id=38695, x=2889, y=5513] Object found - [id=38695, x=2931,
				 * y=5559] Object found - [id=38694, x=2891, y=5639] Object
				 * found - [id=38694, x=2929, y=5687] Object found - [id=38696,
				 * x=2882, y=5898] Object found - [id=38696, x=2882, y=5942]
				 */
				// player.getPackets().sendGameMessage("Done!");
				System.out.println("Done!");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("killnpc")) {
				for (NPC n : World.getNPCs()) {
					if (n == null || n.getId() != Integer.parseInt(cmd[1]))
						continue;
					n.sendDeath(n);
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("sound")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::sound soundid effecttype");
					return true;
				}
				try {
					player.getPackets().sendSound(Integer.valueOf(cmd[1]), 0,
							cmd.length > 2 ? Integer.valueOf(cmd[2]) : 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::sound soundid");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("music")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::sound soundid effecttype");
					return true;
				}
				try {
					player.getPackets().sendMusic(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::sound soundid");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("emusic")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::emusic soundid effecttype");
					return true;
				}
				try {
					player.getPackets()
					.sendMusicEffect(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::emusic soundid");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("testdialogue")) {
				player.getDialogueManager().startDialogue("DagonHai", 7137,
						player, Integer.parseInt(cmd[1]));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("removenpcs")) {
				for (NPC n : World.getNPCs()) {
					if (n.getId() == Integer.parseInt(cmd[1])) {
						n.reset();
						n.finish();
					}
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("resetkdr")) {
				player.setKillCount(0);
				player.setDeathCount(0);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("newtut")) {
				player.getControlerManager().startControler("TutorialIsland",
						0);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("removecontroler")) {
				player.getControlerManager().forceStop();
				player.getInterfaceManager().sendInterfaces();
				return true; 
			}
			//if (cmd[0].equalsIgnoreCase("nomads")) {
			//	for(Player p : World.getPlayers())
			//	p.getControlerManager().startControler("NomadsRequiem");
			//	return true; 
			//}
			if (cmd[0].equalsIgnoreCase("giveitem")  && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null) {
					player.getPackets().sendGameMessage(
							"There is no such player as " + username + ".");
					return true;
				}
				int item = Integer.parseInt(cmd[2]);
				int amount = Integer.parseInt(cmd[3]);
				other.getInventory().addItem(item, amount);
				other.getPackets().sendGameMessage("You have received a "
						+ ItemDefinitions.getItemDefinitions(item).getName() + " from "
						+ player.getDisplayName() + ".");
				player.getPackets().sendGameMessage("You have given a "
						+ ItemDefinitions.getItemDefinitions(item).getName() + " to "
						+ other.getDisplayName() + ".");

			}
			if (cmd[0].equalsIgnoreCase("testp")) {
				//PartyRoom.startParty(player);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("copy") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player p2 = World.getPlayerByDisplayName(name);
				if (p2 == null) {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
					return true;
				}
				Item[] items = p2.getEquipment().getItems().getItemsCopy();
				for (int i = 0; i < items.length; i++) {
					if (items[i] == null)
						continue;
					for (String string : Settings.EARNED_ITEMS) {
						if (items[i].getDefinitions().getName().toLowerCase()
								.contains(string))
							items[i] = new Item(-1, -1);
					}
					for (String string : Settings.VOTE_REQUIRED_ITEMS) {
						if (items[i].getDefinitions().getName().toLowerCase()
								.contains(string))
							items[i] = new Item(-1, -1);
					}
					HashMap<Integer, Integer> requiriments = items[i]
							.getDefinitions().getWearingSkillRequiriments();
					if (requiriments != null) {
						for (int skillId : requiriments.keySet()) {
							if (skillId > 24 || skillId < 0)
								continue;
							int level = requiriments.get(skillId);
							if (level < 0 || level > 120)
								continue;
							if (player.getSkills().getLevelForXp(skillId) < level) {
								name = Skills.SKILL_NAME[skillId]
										.toLowerCase();
								player.getPackets().sendGameMessage(
										"You need to have a"
												+ (name.startsWith("a") ? "n"
														: "") + " " + name
														+ " level of " + level + ".");
							}

						}
					}
					player.getEquipment().getItems().set(i, items[i]);
					player.getEquipment().refresh(i);
				}
				player.getAppearence().generateAppearenceData();
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("god") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				player.setHitpoints(Short.MAX_VALUE);
				player.getEquipment().setEquipmentHpIncrease(
						Short.MAX_VALUE - 990);
				for (int i = 0; i < 10; i++)
					player.getCombatDefinitions().getBonuses()[i] = 5000;
				for (int i = 14; i < player.getCombatDefinitions().getBonuses().length; i++)
					player.getCombatDefinitions().getBonuses()[i] = 5000;
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("praytest")) {
				player.setPrayerDelay(4000);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("karamja")) {
				player.getDialogueManager().startDialogue(
						"KaramjaTrip",
						Utils.getRandom(1) == 0 ? 11701
								: (Utils.getRandom(1) == 0 ? 11702 : 11703));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("shop")) {
				ShopsHandler.openShop(player, Integer.parseInt(cmd[1]));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("duel")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3365, 3275, 0));
				player.getControlerManager().startControler("DuelControler");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("clanwars")) {
				// player.setClanWars(new ClanWars(player, player));
				// player.getClanWars().setWhiteTeam(true);
				// ClanChallengeInterface.openInterface(player);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("testdung")) {
				// MEM LEAK
				// SERVER
				new DungeonPartyManager(player);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("checkdisplay")) {
				for (Player p : World.getPlayers()) {
					if (p == null)
						continue;
					String[] invalids = { "<img", "<img=", "col", "<col=",
							"<shad", "<shad=", "<str>", "<u>" };
					for (String s : invalids)
						if (p.getDisplayName().contains(s)) {
							player.getPackets().sendGameMessage(
									Utils.formatPlayerNameForDisplay(p
											.getUsername()));
						} else {
							player.getPackets().sendGameMessage("None exist!");
						}
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("removedisplay")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setDisplayName(Utils.formatPlayerNameForDisplay(target.getUsername()));
					target.getPackets().sendGameMessage(
							"Your display name was removed by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have removed display name of "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setDisplayName(Utils.formatPlayerNameForDisplay(target.getUsername()));
					player.getPackets().sendGameMessage(
							"You have removed display name of "+target.getDisplayName()+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("cutscene")) {
				player.getPackets().sendCutscene(Integer.parseInt(cmd[1]));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("coords")) {
				player.getPackets().sendPanelBoxMessage(
						"Coords: " + player.getX() + ", " + player.getY()
						+ ", " + player.getPlane() + ", regionId: "
						+ player.getRegionId() + ", rx: "
						+ player.getChunkX() + ", ry: "
						+ player.getChunkY());
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("itemoni")) {
				player.getPackets().sendItemOnIComponent(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
						Integer.valueOf(cmd[3]), 1);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("trade")) {

				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");

				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					player.getTrade().openTrade(target);
					target.getTrade().openTrade(player);
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("setlevel")) {
				if (cmd.length < 3) {
					player.getPackets().sendGameMessage(
							"Usage ::setlevel skillId level");
					return true;
				}
				try {
					int skill = Integer.parseInt(cmd[1]);
					int level = Integer.parseInt(cmd[2]);
					if (level < 0 || level > 99) {
						player.getPackets().sendGameMessage(
								"Please choose a valid level.");
						return true;
					}
					player.getSkills().set(skill, level);
					player.getSkills()
					.setXp(skill, Skills.getXPForLevel(level));
					player.getAppearence().generateAppearenceData();
					return true;
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage(
							"Usage ::setlevel skillId level");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("npc") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				try {
					World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true,
							true);
					return true;
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::npc id(Integer)");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("loadwalls")) {
				WallHandler.loadWall(player.getCurrentFriendChat()
						.getClanWars());
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("cwbase")) {
				ClanWars cw = player.getCurrentFriendChat().getClanWars();
				WorldTile base = cw.getBaseLocation();
				player.getPackets().sendGameMessage(
						"Base x=" + base.getX() + ", base y=" + base.getY());
				base = cw.getBaseLocation()
						.transform(
								cw.getAreaType().getNorthEastTile().getX()
								- cw.getAreaType().getSouthWestTile()
								.getX(),
								cw.getAreaType().getNorthEastTile().getY()
								- cw.getAreaType().getSouthWestTile()
								.getY(), 0);
				player.getPackets()
				.sendGameMessage(
						"Offset x=" + base.getX() + ", offset y="
								+ base.getY());
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("object")) {
				try {
					int type = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 10;
					if (type > 22 || type < 0) {
						type = 10;
					}
					World.spawnObject(
							new WorldObject(Integer.valueOf(cmd[1]), type, 0,
									player.getX(), player.getY(), player
									.getPlane()), true);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: setkills id");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("byfile")) {
				try {
					int type = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 10;
					if (type > 22 || type < 0) {
						type = 10;
					}
					ObjectDefinitions.configByFile(Integer.valueOf(cmd[1]), player);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: byfile objectId");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("tab")) {
				try {
					player.getInterfaceManager().sendTab(
							Integer.valueOf(cmd[2]), Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets()
					.sendPanelBoxMessage("Use: tab id inter");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("killme")) {
				player.applyHit(new Hit(player, 2000, HitLook.REGULAR_DAMAGE));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("hitme")) {
				player.applyHit(new Hit(player, 800, HitLook.REGULAR_DAMAGE));
				return true; 
			}
			//if (cmd[0].equalsIgnoreCase("heal")) {
			//	player.heal(800);
			//	return true; 
			//}
			if (cmd[0].equalsIgnoreCase("changepassother")) {
				name = cmd[1];
				File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				target.setPassword(Encrypt.encryptSHA1(cmd[2]));
				player.getPackets().sendGameMessage(
						"You changed their password!");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("hidec")) {
				if (cmd.length < 4) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::hidec interfaceid componentId hidden");
					return true;
				}
				try {
					player.getPackets().sendHideIComponent(
							Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
							Boolean.valueOf(cmd[3]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::hidec interfaceid componentId hidden");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("string")) {
				try {
					player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
					for (int i = 0; i <= Integer.valueOf(cmd[2]); i++)
						player.getPackets().sendIComponentText(Integer.valueOf(cmd[1]), i,
								"child: " + i);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: string inter childid");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("istringl")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
					return true;
				}

				try {
					for (int i = 0; i < Integer.valueOf(cmd[1]); i++) {
						player.getPackets().sendGlobalString(i, "String " + i);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("istring")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
					return true;
				}
				try {
					player.getPackets().sendGlobalString(
							Integer.valueOf(cmd[1]),
							"String " + Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: String id value");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("iconfig")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
					return true;
				}
				try {
					for (int i = 0; i < Integer.valueOf(cmd[1]); i++) {
						player.getPackets().sendGlobalConfig(Integer.parseInt(cmd[2]), i);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("config")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
					return true;
				}
				try {
					player.getPackets().sendConfig(Integer.valueOf(cmd[1]),
							Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("forcemovement")) {
				WorldTile toTile = player.transform(0, 500, 0);
				player.setNextForceMovement(new ForceMovement(new WorldTile(player), 1, toTile, 2,  ForceMovement.NORTH));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("configf")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
					return true;
				}
				try {
					player.getPackets().sendConfigByFile(
							Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("hit")) {
				for (int i = 0; i < 5; i++)
					player.applyHit(new Hit(player, Utils.getRandom(3),
							HitLook.HEALED_DAMAGE));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("iloop")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer
							.valueOf(cmd[2]); i++)
						player.getInterfaceManager().sendInterface(i);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("tloop")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer
							.valueOf(cmd[2]); i++)
						player.getInterfaceManager().sendTab(i,
								Integer.valueOf(cmd[3]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("configloop")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
						if (i >= 2633) {
							break;
						}
						player.getPackets().sendConfig(i, Integer.valueOf(cmd[3]));
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("configfloop")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer
							.valueOf(cmd[2]); i++)
						player.getPackets().sendConfigByFile(i, Integer.valueOf(cmd[3]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("testo2")) {
				for (int x = 0; x < 10; x++) {

					object = new WorldObject(62684, 0, 0,
							x * 2 + 1, 0, 0);
					player.getPackets().sendSpawnedObject(object);

				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("addn")) {
				player.getNotes().add(new Note(cmd[1], 1));
				player.getNotes().refresh();
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("remn")) {
				player.getNotes().remove((Note) player.getTemporaryAttributtes().get("curNote"));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("objectanim")) {

				object = cmd.length == 4 ? World
						.getObject(new WorldTile(Integer.parseInt(cmd[1]),
								Integer.parseInt(cmd[2]), player.getPlane()))
								: World.getObject(
										new WorldTile(Integer.parseInt(cmd[1]), Integer
												.parseInt(cmd[2]), player.getPlane()),
												Integer.parseInt(cmd[3]));
						if (object == null) {
							player.getPackets().sendPanelBoxMessage(
									"No object was found.");
							return true;
						}
						player.getPackets().sendObjectAnimation(
								object,
								new Animation(Integer.parseInt(cmd[cmd.length == 4 ? 3
										: 4])));
						return true; 
			}
			if (cmd[0].equalsIgnoreCase("loopoanim")) {
				int x = Integer.parseInt(cmd[1]);
				int y = Integer.parseInt(cmd[2]);
				final WorldObject object1 = World
						.getRegion(player.getRegionId()).getSpawnedObject(
								new WorldTile(x, y, player.getPlane()));
				if (object1 == null) {
					player.getPackets().sendPanelBoxMessage(
							"Could not find object at [x=" + x + ", y=" + y
							+ ", z=" + player.getPlane() + "].");
					return true;
				}
				System.out.println("Object found: " + object1.getId());
				final int start = cmd.length > 3 ? Integer.parseInt(cmd[3])
						: 10;
				final int end = cmd.length > 4 ? Integer.parseInt(cmd[4])
						: 20000;
				CoresManager.fastExecutor.scheduleAtFixedRate(new TimerTask() {
					int current = start;

				 @Override
					public void run() {
						while (AnimationDefinitions
								.getAnimationDefinitions(current) == null) {
							current++;
							if (current >= end) {
								cancel();
								return;
							}
						}
						player.getPackets().sendPanelBoxMessage(
								"Current object animation: " + current + ".");
						player.getPackets().sendObjectAnimation(object1,
								new Animation(current++));
						if (current >= end) {
							cancel();
						}
					}
				}, 1800, 1800);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("unmuteall")) {
				for (Player targets : World.getPlayers()) {
					if (player == null) continue;
					targets.setMuted(0);
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("bconfigloop")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
						if (i >= 1929) {
							break;
						}
						player.getPackets().sendGlobalConfig(i, Integer.valueOf(cmd[3]));
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: config id value");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("reset")) {
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++)
						player.getSkills().setXp(skill, 0);
					player.getSkills().init();
					return true;
				}
				try {
					player.getSkills().setXp(Integer.valueOf(cmd[1]), 0);
					player.getSkills().set(Integer.valueOf(cmd[1]), 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::master skill");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("highscores")) {
				Highscores.highscores(player, null);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("level")) {
				player.getSkills().addXp(Integer.valueOf(cmd[1]),
						Skills.getXPForLevel(Integer.valueOf(cmd[2])));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("master")) {
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++)
						player.getSkills().addXp(skill, 150000000);
					return true;
				}
				try {
					player.getSkills().addXp(Integer.valueOf(cmd[1]),
							200000000);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::master skill");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("window")) {
				player.getPackets().sendWindowsPane(1253, 0);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("bconfig")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: bconfig id value");
					return true;
				}
				try {
					player.getPackets().sendGlobalConfig(
							Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: bconfig id value");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("tonpc")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					player.getAppearence().transformIntoNPC(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tonpc id(-1 for player)");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("inter")) {
				//1307
				if (Integer.valueOf(cmd[1]) > 1306) {
					return true;
				}
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}
				try {
					player.getInterfaceManager().sendInterface(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("overlay")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}
				int child = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 28;
				try {
					player.getPackets().sendInterface(true, 746, child, Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("empty")) {
				player.getInventory().reset();
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("interh")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}

				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentModel(interId,
								componentId, 66);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("inters")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
					return true;
				}

				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentText(interId,
								componentId, "cid: " + componentId);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::inter interfaceId");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("kill")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					return true;
				target.applyHit(new Hit(target, player.getHitpoints(),
						HitLook.REGULAR_DAMAGE));
				target.stopAll();
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("givedonator") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn = false;
				}
				if (target == null)
					return true;
				target.setDonator(true);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn)
					target.getPackets().sendGameMessage(
							"You have been given donator by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave donator to "
								+ Utils.formatPlayerNameForDisplay(target
										.getUsername()), true);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("makesupport") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn1 = false;
				}
				if (target == null)
					return true;
				target.setSupporter(true);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn1)
					target.getPackets().sendGameMessage(
							"You have been given supporter rank by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave supporter rank to "
								+ Utils.formatPlayerNameForDisplay(target
										.getUsername()), true);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("takesupport") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn2 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn2 = false;
				}
				if (target == null)
					return true;
				target.setSupporter(false);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn2)
					target.getPackets().sendGameMessage(
							"Your supporter rank was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed supporter rank of "
								+ Utils.formatPlayerNameForDisplay(target
										.getUsername()), true);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("makegfx")) {
				if(!player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn11 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn11 = false;
				}
				if (target == null)
					return true;
				target.setGraphicDesigner(true);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn11)
					target.getPackets().sendGameMessage(
							"You have been given graphic designer rank by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave graphic designer rank to "
								+ Utils.formatPlayerNameForDisplay(target
										.getUsername()), true);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("takegfx")) {
				if(!player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn21 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn21 = false;
				}
				if (target == null)
					return true;
				target.setGraphicDesigner(false);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn21)
					target.getPackets().sendGameMessage(
							"Your graphic designer rank was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed graphic designer rank of "
								+ Utils.formatPlayerNameForDisplay(target
										.getUsername()), true);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("makefmod")) {
				if(!player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn11221 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn11221 = false;
				}
				if (target == null)
					return true;
				target.setForumModerator(true);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn11221)
					target.getPackets().sendGameMessage(
							"You have been given graphic designer rank by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave graphic designer rank to "
								+ Utils.formatPlayerNameForDisplay(target
										.getUsername()), true);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("takefmod")) {
				if(!player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn7211 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn7211 = false;
				}
				if (target == null)
					return true;
				target.setGraphicDesigner(false);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn7211)
					target.getPackets().sendGameMessage(
							"Your forum moderator rank was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed forum moderator rank of "
								+ Utils.formatPlayerNameForDisplay(target
										.getUsername()), true);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("giveextreme") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn111 = false;
				}
				if (target == null)
					return true;
				target.setExtremeDonator(true);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn111)
					target.getPackets().sendGameMessage(
							"You have been given extreme donator by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave extreme donator to "
								+ Utils.formatPlayerNameForDisplay(target
										.getUsername()), true);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("takeextreme") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn1111 = false;
				}
				if (target == null)
					return true;
				target.setExtremeDonator(false);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn1111)
					target.getPackets().sendGameMessage(
							"Your extreme donator was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed extreme donator from "
								+ Utils.formatPlayerNameForDisplay(target
										.getUsername()), true);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("monthdonator") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				name = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					return true;
				target.makeDonator(1);
				SerializableFilesManager.savePlayer(target);
				target.getPackets().sendGameMessage(
						"You have been given donator by "
								+ Utils.formatPlayerNameForDisplay(player
										.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You gave donator to "
								+ Utils.formatPlayerNameForDisplay(target
										.getUsername()), true);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("takedonator") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn121 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn121 = false;
				}
				if (target == null)
					return true;
				target.setDonator(false);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn121)
					target.getPackets().sendGameMessage(
							"Your donator was removed by "
									+ Utils.formatPlayerNameForDisplay(player
											.getUsername()), true);
				player.getPackets().sendGameMessage(
						"You removed donator from "
								+ Utils.formatPlayerNameForDisplay(target
										.getUsername()), true);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("bank")) {
				player.getBank().openBank();
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("reloadfiles")) {
				IPBanL.init();
				PkRank.init();
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("tele") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tele coordX coordY");
					return true;
				}
				try {
					player.resetWalkSteps();
					player.setNextWorldTile(new WorldTile(Integer
							.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
							cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player
									.getPlane()));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tele coordX coordY plane");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("restart") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				int delay = 60;
				if (cmd.length >= 2) {
					try {
						delay = Integer.valueOf(cmd[1]);
					} catch (NumberFormatException e) {
						player.getPackets().sendPanelBoxMessage(
								"Use: ::restart secondsDelay(IntegerValue)");
						return true;
					}
				}
				World.safeShutdown(true, delay);
				return true;
			}
			if((cmd[0].equalsIgnoreCase("shutdown") || cmd[0].equalsIgnoreCase("stop")) && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				int delay = 60;
				if (cmd.length >= 2) {
					try {
						delay = Integer.valueOf(cmd[1]);
					} catch (NumberFormatException e) {
						player.getPackets().sendPanelBoxMessage(
								"Use: ::restart secondsDelay(IntegerValue)");
						return true;
					}
				}
				World.safeShutdown(false, delay);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("emote")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.setNextAnimation(new Animation(Integer
							.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("remote")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.getAppearence().setRenderEmote(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("quake")) {
				player.getPackets().sendCameraShake(Integer.valueOf(cmd[1]),
						Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]),
						Integer.valueOf(cmd[4]), Integer.valueOf(cmd[5]));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("getrender")) {
				player.getPackets().sendGameMessage("Testing renders");
				for (int i = 0; i < 3000; i++) {
					try {
						player.getAppearence().setRenderEmote(i);
						player.getPackets().sendGameMessage("Testing " + i);
						Thread.sleep(600);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("spec") && player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				player.getCombatDefinitions().resetSpecialAttack();
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("trylook")) {
				final int look = Integer.parseInt(cmd[1]);
				WorldTasksManager.schedule(new WorldTask() {
					int i = 269;// 200

				 @Override
					public void run() {
						if (player.hasFinished()) {
							stop();
						}
						player.getAppearence().setLook(look, i);
						player.getAppearence().generateAppearenceData();
						player.getPackets().sendGameMessage("Look " + i + ".");
						i++;
					}
				}, 0, 1);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("tryinter")) {
				WorldTasksManager.schedule(new WorldTask() {
					int i = 1;

				 @Override
					public void run() {
						if (player.hasFinished()) {
							stop();
						}
						player.getInterfaceManager().sendInterface(i);
						System.out.println("Inter - " + i);
						i++;
					}
				}, 0, 1);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("tryanim")) {
				WorldTasksManager.schedule(new WorldTask() {
					int i = 16700;

				 @Override
					public void run() {
						if (i >= Utils.getAnimationDefinitionsSize()) {
							stop();
							return;
						}
						if (player.getLastAnimationEnd() > System
								.currentTimeMillis()) {
							player.setNextAnimation(new Animation(-1));
						}
						if (player.hasFinished()) {
							stop();
						}
						player.setNextAnimation(new Animation(i));
						System.out.println("Anim - " + i);
						i++;
					}
				}, 0, 3);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("animcount")) {
				System.out.println(Utils.getAnimationDefinitionsSize() + " anims.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("trygfx")) {
				WorldTasksManager.schedule(new WorldTask() {
					int i = 1500;

				 @Override
					public void run() {
						if (i >= Utils.getGraphicDefinitionsSize()) {
							stop();
						}
						if (player.hasFinished()) {
							stop();
						}
						player.setNextGraphics(new Graphics(i));
						System.out.println("GFX - " + i);
						i++;
					}
				}, 0, 3);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("gfx")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
					return true;
				}
				try {
					player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1]), 0, 0));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("sync")) {
				int animId = Integer.parseInt(cmd[1]);
				int gfxId = Integer.parseInt(cmd[2]);
				int height = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 0;
				player.setNextAnimation(new Animation(animId));
				player.setNextGraphics(new Graphics(gfxId, 0, height));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("mess")) {
				player.getPackets().sendMessage(Integer.valueOf(cmd[1]), "",
						player);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("unpermban")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				File acc = new File("data/characters/"+name.replace(" ", "_")+".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				target.setPermBanned(false);
				target.setBanned(0);
				player.getPackets().sendGameMessage(
						"You've unbanned "+Utils.formatPlayerNameForDisplay(target.getUsername())+ ".");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("permban")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					target.setPermBanned(true);
					target.getPackets().sendGameMessage(
							"You've been perm banned by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have perm banned: "+target.getDisplayName()+".");
					target.getSession().getChannel().close();
					SerializableFilesManager.savePlayer(target);
					Player.printLog(player, name);
				} else {
					File acc11 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					if (target.getRights() == 2)
						return true;
					target.setPermBanned(true);
					player.getPackets().sendGameMessage(
							"You have perm banned: "+Utils.formatPlayerNameForDisplay(name)+".");
					Player.printLog(player, name);
					try {
						SerializableFilesManager.storeSerializableClass(target, acc11);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("ipban")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn11111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn11111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					IPBanL.ban(target, loggedIn11111);
					//IPBanL.ipList.add(player.getSession().getIP() + 1);
					player.getPackets().sendGameMessage(
							"You've permanently ipbanned "
									+ (loggedIn11111 ? target.getDisplayName()
											: name) + ".");
					Player.printLog(player, name);
				} else {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				}	
				return true;
			}
			if (cmd[0].equalsIgnoreCase("unipban")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				File acc11 = new File("data/characters/"+name.replace(" ", "_")+".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				IPBanL.unban(target);
				player.getPackets().sendGameMessage(
						"You've unipbanned "+Utils.formatPlayerNameForDisplay(target.getUsername())+ ".");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc11);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("staffmeeting")) {
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() == 0)
						continue;
					staff.setNextWorldTile(new WorldTile(2675, 10418, 0));
					staff.getPackets().sendGameMessage("You been teleported for a staff meeting by "+player.getDisplayName());
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("fightkiln")) {
				FightKiln.enterFightKiln(player, true);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("setpitswinner")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
				if (target != null) {
					target.setWonFightPits();
					target.setCompletedFightCaves();
				} else {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				}
				SerializableFilesManager.savePlayer(target);
				return true;
			}
		}
		return false;
	}

	public static boolean processHeadModCommands(Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			String name;
			Player target;

			if (cmd[0].equalsIgnoreCase("ipban")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils
							.formatPlayerNameForProtocol(name));
					Player.printLog(player, name);
					if (target != null)
						target.setUsername(Utils
								.formatPlayerNameForProtocol(name));
					loggedIn1111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					IPBanL.ban(target, loggedIn1111);
					player.getPackets().sendGameMessage(
							"You've permanently ipbanned "
									+ (loggedIn1111 ? target.getDisplayName()
											: name) + ".");
					Player.printLog(player, name);
				} else {
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				}	
				return true;
			}
		}
		return false;
	}

	public static boolean processSupportCommands(Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		String name;
		Player target;
		if (clientCommand) {

		} else {
			if (cmd[0].equalsIgnoreCase("sz")) {
				if (player.isLocked() || player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You cannot tele anywhere from here.");
					return true;
				}
				player.setNextWorldTile(new WorldTile(4255, 5534, 1));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("warn")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.blackMark++;
				player.warnLog(player, username);
					player.getPackets().sendGameMessage("You have warned " +other.getDisplayName()+ ". They now have " + other.blackMark+ " black marks.");
					other.getPackets().sendGameMessage("You have recieved a black mark from "+player.getDisplayName()+ ". You now have "+other.blackMark+ " black marks.");
					other.setNextForceTalk(new ForceTalk("I have been warned. I am now on "+other.blackMark+" black marks."));
				if (other.blackMark >= 3) {
					player.setNextForceTalk(new ForceTalk(other.getDisplayName()+ " has been warned 3 times and has been banned for 48 hours."));
					player.getPackets().sendGameMessage("You have warned: " +other.getDisplayName()+ " they are now on: " + other.blackMark);
					other.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					other.getSession().getChannel().close();
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("takemarks")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				if (other.blackMark == 0) {
					player.out("You cannot go into negative numbers.");
					return true;
				}
				other.blackMark --;
				other.getPackets().sendGameMessage("You now have " +player.blackMark+" black marks.");
				player.getPackets().sendGameMessage("You remove a black mark from " + other.getDisplayName() +". They are now on "+other.blackMark+" black marks.");
			}
			if (cmd[0].equalsIgnoreCase("unnull") || (cmd[0].equalsIgnoreCase("sendhome"))) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else {
					target.unlock();
					target.getControlerManager().forceStop();
					if(target.getNextWorldTile() == null) //if controler wont tele the player
						target.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
					player.getPackets().sendGameMessage("You have unnulled: "+target.getDisplayName()+".");
					return true; 
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("unjail")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(0);
					target.getControlerManager()
					.startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been unjailed by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have unjailed: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(0);
					player.getPackets().sendGameMessage(
							"You have unjailed: "+Utils.formatPlayerNameForDisplay(name)+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("jail")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(Utils.currentTimeMillis()
							+ (24 * 60 * 60 * 1000));
					target.getControlerManager()
					.startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been Jailed for 24 hours by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have Jailed 24 hours: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(Utils.currentTimeMillis()
							+ (24 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have muted 24 hours: "+Utils.formatPlayerNameForDisplay(name)+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("staffyell")) {
				String message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				sendYell(player, Utils.fixChatMessage(message));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("ticket")) {
				TicketSystem.answerTicket(player);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("finishticket")) {
				TicketSystem.removeTicket(player);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("mute")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(Utils.currentTimeMillis()
							+ (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
					target.getPackets().sendGameMessage(
							"You've been muted for " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ") +Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ") + target.getDisplayName()+".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if(!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setMuted(Utils.currentTimeMillis()
							+ (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
					player.getPackets().sendGameMessage(
							"You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ") + target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean processModCommand(Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			if (cmd[0].equalsIgnoreCase("unmute")) {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(0);
					target.getPackets().sendGameMessage(
							"You've been unmuted by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have unmuted "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setMuted(0);
					player.getPackets().sendGameMessage(
							"You have unmuted: "+Utils.formatPlayerNameForDisplay(name)+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			}

			String name;
			Player target;
			if (cmd[0].equalsIgnoreCase("ban")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					target.getSession().getChannel().close();
					player.getPackets().sendGameMessage("You have banned 48 hours: "+target.getDisplayName()+".");
					Player.printLog(player, name);
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if(!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have banned 48 hours: "+Utils.formatPlayerNameForDisplay(name)+".");
					Player.printLog(player, name);
					SerializableFilesManager.savePlayer(target);
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("bank")) {
				//player.getBank().openBank();
				player.getPackets().sendGameMessage("Nice try.");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("warn")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.blackMark++;
				player.warnLog(player, username);
					player.getPackets().sendGameMessage("You have warned " +other.getDisplayName()+ ". They now have " + other.blackMark+ " black marks.");
					other.getPackets().sendGameMessage("You have recieved a black mark from "+player.getDisplayName()+ ". You now have "+other.blackMark+ " black marks.");
					other.setNextForceTalk(new ForceTalk("I have been warned. I am now on "+other.blackMark+" black marks."));
				if (other.blackMark >= 3) {
					player.setNextForceTalk(new ForceTalk(other.getDisplayName()+ " has been warned 3 times and has been banned for 48 hours."));
					player.getPackets().sendGameMessage("You have warned: " +other.getDisplayName()+ " they are now on: " + other.blackMark);
					other.setBanned(Utils.currentTimeMillis()
							+ (48 * 60 * 60 * 1000));
					other.getSession().getChannel().close();
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("takemarks")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				if (other.blackMark == 0) {
					player.out("You cannot go into negative numbers.");
					return true;
				}
				other.blackMark --;
				other.getPackets().sendGameMessage("You now have " +player.blackMark+" black marks.");
				player.getPackets().sendGameMessage("You remove a black mark from " + other.getDisplayName() +". They are now on "+other.blackMark+" black marks.");
			}
			if (cmd[0].equalsIgnoreCase("jail")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(Utils.currentTimeMillis()
							+ (24 * 60 * 60 * 1000));
					target.getControlerManager()
					.startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been Jailed for 24 hours by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have Jailed 24 hours: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(Utils.currentTimeMillis()
							+ (24 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have muted 24 hours: "+Utils.formatPlayerNameForDisplay(name)+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("kick")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage(
							Utils.formatPlayerNameForDisplay(name)+" is not logged in.");
					return true;
				}
				target.getSession().getChannel().close();
				player.getPackets().sendGameMessage("You have kicked: "+target.getDisplayName()+".");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("staffyell")) {
				String message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				sendYell(player, Utils.fixChatMessage(message));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("forcekick")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage(
							Utils.formatPlayerNameForDisplay(name)+" is not logged in.");
					return true;
				}
				target.forceLogout();
				player.getPackets().sendGameMessage("You have kicked: "+target.getDisplayName()+".");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("hide")) {
				if (player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You cannot hide in a public event!");
					return true;
				}
				player.getAppearence().switchHidden();
				player.getPackets().sendGameMessage("Hidden? " + player.getAppearence().isHidden());
				return true;
			}
			if (cmd[0].equalsIgnoreCase("unjail")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(0);
					target.getControlerManager()
					.startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been unjailed by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
					player.getPackets().sendGameMessage(
							"You have unjailed: "+target.getDisplayName()+".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/"+name.replace(" ", "_")+".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(0);
					player.getPackets().sendGameMessage(
							"You have unjailed: "+Utils.formatPlayerNameForDisplay(name)+".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("teleto")) {
				if (player.isLocked() || player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You cannot tele anywhere from here.");
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else
					player.setNextWorldTile(target);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("teletome")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else {
					if (target.isLocked() || target.getControlerManager().getControler() != null) {
						player.getPackets().sendGameMessage("You cannot teleport this player.");
						return true;
					}
					if (target.getRights() > 1) {
						player.getPackets().sendGameMessage(
								"Unable to teleport a developer to you.");
						return true;
					}
					target.setNextWorldTile(player);
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("takemark")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.blackMark -= 1;
				other.getPackets().sendGameMessage("You now have: " +player.blackMark);
				player.getPackets().sendGameMessage("You remove a black mark from " + other.getDisplayName());
			}
			if (cmd[0].equalsIgnoreCase("unnull") ||
					(cmd[0].equalsIgnoreCase("sendhome"))) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if(target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else {
					target.unlock();
					target.getControlerManager().forceStop();
					if(target.getNextWorldTile() == null) //if controler wont tele the player
						target.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
					player.getPackets().sendGameMessage("You have unnulled: "+target.getDisplayName()+".");
					return true; 
				}
				return true;
			}
		}
		return false;
	}
	public static void sendYell(Player player, String message) {
		for (Player p2: World.getPlayers()) {
			if (player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				p2.getPackets().sendGameMessage("(Owner) "
						+ "<img=1>"+player.getDisplayName()+": <col=00FF73>"
						+message+"</col>");
			} else if (player.getUsername().equalsIgnoreCase("mb")) {
				p2.getPackets().sendGameMessage("[Boss]: "
						+ ""+player.getDisplayName()+": <col=00FF73>"
						+message+"</col>");
			} else if (player.getUsername().equalsIgnoreCase("fallin")) {
				p2.getPackets().sendGameMessage("[Head Support]: "
						+ "<img=6>"+player.getDisplayName()+": <col=8D38C9>"
						+message+"</col>");
			} else if (player.getUsername().equalsIgnoreCase("fanatic")) {
				p2.getPackets().sendGameMessage("[Trusted Dicer]: "
						+ "<img=6>"+player.getDisplayName()+": <col=8D38C9>"
						+message+"</col>");
			} else if (player.getUsername().equalsIgnoreCase("mike")) {
				p2.getPackets().sendGameMessage("[Head Admin]: "
						+ "<img=1>"+player.getDisplayName()+": <col=8D38C9>"
						+message+"</col>");
			} else if (player.getUsername().equalsIgnoreCase("geo")) {
				p2.getPackets().sendGameMessage("[Head Mod]: "
						+ "<img=0>"+player.getDisplayName()+": <col=00FF73>"
						+message+"</col>");
			} else if (player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
				p2.getPackets().sendGameMessage("[Fabulous Mod]: "
						+ "<img=0>"+player.getDisplayName()+": <col=00FF73>"
						+message+"</col>");
			} else if (player.getRights() == 2) {
			p2.getPackets().sendGameMessage("[Administrator]: "
					+ "<img=1>"+player.getDisplayName()+": <col=00FF73>"
					+message+"</col>");
			} else if(player.getRights() == 1) {
				p2.getPackets().sendGameMessage("[Moderator]: "
						+ "<img=0>"+player.getDisplayName()+": <col=C000FF>"
						+message+"</col>");
			} else if(player.isSupporter() && player.getRights() == 0) {
				p2.getPackets().sendGameMessage("[Support]: "
						+ "<img=6>"+player.getDisplayName()+": <col=8D38C9>"
						+message+"</col>");
			} else if(player.isSupporter() && player.getRights() == 0) {
				p2.getPackets().sendGameMessage("[Support]: "
						+ "<img=6>"+player.getDisplayName()+": <col=8D38C9>"
						+message+"</col>");
			} else if(player.isExtremeDonator() && player.getRights() == 0) {
				p2.getPackets().sendGameMessage("[Extreme Donator]: "
						+ "<img=11>"+player.getDisplayName()+": <col=C000FF>"
						+message+"</col>");
			} else if(player.getUsername().equalsIgnoreCase("")) {
				p2.getPackets().sendGameMessage("[Forum Administrator]: "
						+ "<img=6>"+player.getDisplayName()+": <col=FF0000>"
						+message+"</col>");
			} else if(player.isDonator() && player.getRights() == 0) {
				p2.getPackets().sendGameMessage("[Donator]: "
						+ "<img=8>"+player.getDisplayName()+": <col=C000FF>"
						+message+"</col>");
			} else {
				p2.getPackets().sendGameMessage("[Player]: "
						+ player.getDisplayName()+": <col=00ABFF>"
						+message+"</col>");
			}
		}
	}

	public static boolean processNormalCommand(Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			String message;
			String username = "";
			if (cmd[0].equalsIgnoreCase("setyellcolor") || 
					cmd[0].equalsIgnoreCase("changeyellcolor") || 
					cmd[0].equalsIgnoreCase("yellcolor")) {
				if(!player.isExtremeDonator() && player.getRights() == 0) {
					player.getDialogueManager().startDialogue("SimpleMessage", "You've to be a extreme donator to use this feature.");
					return true;
				}
				player.getPackets().sendRunScript(109, new Object[] { "Please enter the yell color in HEX format." });
				player.getTemporaryAttributtes().put("yellcolor", Boolean.TRUE);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("dz")) {
				if (player.isDonator()) {
					DonatorZone.enterDonatorzone(player);
				} else {
					player.getPackets().sendGameMessage("You need to be a donator to use this.");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("ez")) {
				if (player.isExtremeDonator()) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2846, 10209, 0));
				} else {
					player.getPackets().sendGameMessage("You need to be a extreme donator to use this.");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("db")) {
				if (player.isDonator() || player.getDisplayName().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2841, 2576, 0));
				} else {
					player.getPackets().sendGameMessage("You need to be a donator to use this.");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("estq")) {
				if (player.isExtremeDonator() || player.getDisplayName().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3793, 2941, 0));
				} else {
					player.getPackets().sendGameMessage("You need to be a extreme donator to use this.");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("resetrollname")) {
				player.getPetManager().setTrollBabyName(null);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("setrollname")) {
				if (!player.isExtremeDonator()) {
					player.getPackets().sendGameMessage("This is an extreme donator only feature!");
					return true;
				}
				String name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				}
				name = Utils.formatPlayerNameForDisplay(name);
				if (name.length() < 3 || name.length() > 14) {
					player.getPackets().sendGameMessage("You can't use a name shorter than 3 or longer than 14 characters.");
					return true;
				}
				player.getPetManager().setTrollBabyName(name);
				if (player.getPet() != null && player.getPet().getId() == Pets.TROLL_BABY.getBabyNpcId()) {
					player.getPet().setName(name);
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("recanswer")) {
				if (player.getRecovQuestion() == null) {
					player.getPackets().sendGameMessage(
							"Please set your recovery question first.");
					return true;
				}
				if (player.getRecovAnswer() != null && player.getRights() < 2) {
					player.getPackets().sendGameMessage(
							"You can only set recovery answer once.");
					return true;
				}
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.setRecovAnswer(message);
				player.getPackets()
				.sendGameMessage(
						"Your recovery answer has been set to - "
								+ Utils.fixChatMessage(player
										.getRecovAnswer()));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("recquestion")) {
				if (player.getRecovQuestion() != null && player.getRights() < 2) {
					player.getPackets().sendGameMessage(
							"You already have a recovery question set.");
					return true;
				}
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.setRecovQuestion(message);
				player.getPackets().sendGameMessage(
						"Your recovery question has been set to - "
								+ Utils.fixChatMessage(player
										.getRecovQuestion()));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("empty")) {
				player.getInventory().reset();
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("shop")) {
				ShopsHandler.openShop(player, 27);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("ticket")) {
				if (player.getMuted() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage(
							"You are temporary muted. Recheck in 48 hours.");
					return true;
				}
				TicketSystem.requestTicket(player);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("ranks")) {
				PkRank.showRanks(player);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("checkmarks")) {
				player.getPackets().sendGameMessage("You are currently on " + player.blackMark + "/3 black marks.");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("kc")) {
				double kill = player.getKillCount();
				double death = player.getDeathCount();
				double dr = kill / death;
				player.setNextForceTalk(new ForceTalk(
						"<col=ff0000>I'VE KILLED " + player.getKillCount()
						+ " PLAYERS AND BEEN SLAYED "
						+ player.getDeathCount() + " TIMES. DR: " + dr));
				return true; 
			}
			String name;
			if (cmd[0].equalsIgnoreCase("exchange")) {
			player.getInterfaceManager().sendInterface(276);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("players")) {
				
				// send a frame interface component to the player
				player.getInterfaceManager().sendInterface(275);
				int number = 0;
					for (int i = 0; i < 100; i++) {
				player.getPackets().sendIComponentText(275, i, "");
				}
				for(Player p5 : World.getPlayers()) {

					if(p5 == null)
					continue;
					number++;
					String titles = "";
					if (!(p5.isDonator()) || !(p5.isExtremeDonator()) && p5.getRights() == 0) {
					titles = "";
					}
					if (p5.isDonator()) {
					titles = "<img=8>Donator ";
					}
					if (p5.isExtremeDonator()) {
					titles = "<img=11>Extreme Donator ";
					}
					if (p5.isSupporter()) {
					titles = "<col=00ff48>Supporter ";
					}
					if (p5.getRights() == 1) {
					titles = "<col=bcb8b8><img=0>Moderator ";
					}
					if (p5.getRights() == 2) {
					titles = "<col=ff1d1d><img=1>Administrator ";
					}
					if (p5.getDisplayName().equalsIgnoreCase(Settings.ADMIN_NAME)) {

						titles = "";

						if(!p5.isHiddenAdmin()) {
							titles = "<img=1> ";
						}
					}
					if (p5.getDisplayName().equalsIgnoreCase("clambam")) {
						titles = "<col=bcb8b8>Trusted Dicer ";
					}

					player.getPackets().sendIComponentText(275, (13 + number), titles + ""+ p5.getDisplayName());
				}

				player.getPackets().sendIComponentText(275, 1, "All Players");
				player.getPackets().sendIComponentText(275, 10, " ");
				player.getPackets().sendIComponentText(275, 11, "Players Online: " + number);
				player.getPackets().sendIComponentText(275, 12, "Who's online?");
				player.getPackets().sendGameMessage(
						"There are currently " + World.getPlayers().size()
								+ " players playing " + Settings.SERVER_NAME
								+ ".");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("help")) {
				player.getInventory().addItem(1856, 1);
				player.getPackets().sendGameMessage(
						"You receive a guide book about "
								+ Settings.SERVER_NAME + ".");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("title")) {
				if (!player.isDonator()) {
					player.getPackets().sendGameMessage("You have to be a donator to use this command.");
					return true;
				}
				try {
					if (Integer.valueOf(cmd[1]) > 100) {
						player.out("You can only use titles under 100.");
					} else {
						player.getAppearence().setTitle(Integer.valueOf(cmd[1]));	
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::title id");
				}
				return true; 
			}

			if (cmd[0].equalsIgnoreCase("teleto")) {

				if (player.isSupporter() || player.getDisplayName().equalsIgnoreCase(Settings.ADMIN_NAME)) {

					if (player.isLocked() || player.getControlerManager().getControler() != null) {
						player.getPackets().sendGameMessage("You cannot tele anywhere from here.");
						return true;
					}

					name = "";

					Player target;

					for (int i = 1; i < cmd.length; i++) {
						name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
					}

					target = World.getPlayerByDisplayName(name);

					if(target == null) {
						player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
					} else {
						player.setNextWorldTile(target);
					}

					return true;
				}

			}

			if (cmd[0].equalsIgnoreCase("supporttitle")) {
				if (player.isSupporter()) {
					player.getAppearence().setTitle(789);
					return true;
				}
				try {
					player.getPackets().sendGameMessage("You have to be a supporter to use this.");
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::supporttitle");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("modtitle")) {
				if (player.getRights() == 1) {
					player.getAppearence().setTitle(345);
					return true;
				}
				try {
					player.getPackets().sendGameMessage("You have to be a moderator to use this.");
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::title id");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("admintitle")) {
				if (player.getRights() == 2) {
					player.getAppearence().setTitle(799);
					return true;
				}
				try {
					player.getPackets().sendGameMessage("You have to be a admin to use this.");
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::title id");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("ownertitle")) {
				if (player.getRights() == 2 && player.getDisplayName().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					player.getAppearence().setTitle(5740);
					return true;
				}
				try {
					player.getPackets().sendGameMessage("You have to be a admin to use this.");
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::title id");
				}
				return true; 
			}
			if(cmd[0].equalsIgnoreCase("rights")) {
				player.getPackets().sendGameMessage("Your player rights are " + player.getRights());
				return true;
			}
			if (cmd[0].equalsIgnoreCase("ownertitle2")) {
				if (player.getRights() == 2 && player.getDisplayName().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					player.getAppearence().setTitle(5741);
					return true;
				}
				try {
					player.getPackets().sendGameMessage("You have to be a admin to use this.");
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::title id");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("cotitle")) {
				if (player.getRights() == 2 && player.getDisplayName().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					player.getAppearence().setTitle(577);
					return true;
				}
				try {
					player.getPackets().sendGameMessage("You have to be a co-owner to use this.");
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::title id");
				}
				return true; 
			}
			/*if (cmd[0].equalsIgnoreCase("setdisplay")) {
				if (!player.isDonator() && !player.isExtremeDonator()) {
					player.getPackets().sendGameMessage(
							"You have to be a donator to use this.");
					return true;
				}
				player.getTemporaryAttributtes().put("setdisplay", Boolean.TRUE);
				player.getPackets().sendInputNameScript("Enter the display name you wish:");
				return true; 
			}*/
			if (cmd[0].equalsIgnoreCase("removedisplay")) {
				player.getPackets().sendGameMessage("Removed Display Name: " + DisplayNames.removeDisplayName(player));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("gp")) {
				player.out("" + Shop.commas(Integer.toString(player.highestGPKill)));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("blueskin")) {
				if (!player.isDonator()) {
					player.getPackets().sendGameMessage(
							"You have to be a donator to use this.");
					return true;
				}
				player.getAppearence().setSkinColor(12);
				player.getAppearence().generateAppearenceData();
				return true;
			}
			if (cmd[0].equalsIgnoreCase("greenskin")) {
				if (!player.isDonator()) {
					player.getPackets().sendGameMessage(
							"You have to be a donator to use this.");
					return true;
				}
				player.getAppearence().setSkinColor(13);
				player.getAppearence().generateAppearenceData();
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("checkpoints")) {
				player.getPackets().sendGameMessage("You currently have " +player.donatorPoints+ " donator points.");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("vote")) {
				player.getPackets().sendGameMessage("There will be no donations or voting on this server.");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("donate")) {
				player.getPackets().sendGameMessage("There will be no donations or voting on this server.");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("website")) {
				player.getPackets().sendOpenURL("http://google.com");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("shop")) {
				ShopsHandler.openShop(player, 27);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("lockxp")) {
				player.setXpLocked(player.isXpLocked() ? false : true);
				player.getPackets().sendGameMessage("You have " +(player.isXpLocked() ? "Locked" : "Unlocked") + " your xp.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("hideyell")) {
				player.setYellOff(!player.isYellOff());
				player.getPackets().sendGameMessage("You have turned " +(player.isYellOff() ? "off" : "on") + " yell.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("changepass")) {
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				if (message.length() > 15 || message.length() < 5) {
					player.getPackets().sendGameMessage(
							"You cannot set your password to over 15 chars.");
					return true;
				}
				player.setPassword(Encrypt.encryptSHA1(cmd[1]));
				player.getPackets().sendGameMessage(
						"You changed your password! Your password is " + cmd[1]
								+ ".");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("yell")) {
				if (player.isExtremeDonator() || player.isDonator() || player.getRights() > 1 || player.isSupporter()) {
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				sendYell(player, Utils.fixChatMessage(message));
				}
				return true; 
			}
			//if (cmd[0].equalsIgnoreCase("testhomescene")) {
			//	player.getCutscenesManager().play(new HomeCutScene());
			//	return true; 
			//}
			if (cmd[0].equalsIgnoreCase("hiddenadmin")) {
				if (player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					player.toggleHiddenAdmin();
					player.getAppearence().generateAppearenceData();
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("chest")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1818, 3225, 0));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("bank")) {
			if (!player.isCanPvp() && player.isExtremeDonator()) {
				player.stopAll();
				player.getBank().openBank();
			}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("reqs")) {
				player.getInterfaceManager().sendInterface(1245);
				player.getPackets().sendIComponentText(1245, 330, "Daily Challenges");
				player.getPackets().sendIComponentText(1245, 13, "<u>Challenges</u>");
				player.getPackets().sendIComponentText(1245, 14, "");
				player.getPackets().sendIComponentText(1245, 15, "Amount of logs cut: " +player.wc+ "/350");
				player.getPackets().sendIComponentText(1245, 16, "Amount of logs burnt: " +player.logs+ "/250");
				player.getPackets().sendIComponentText(1245, 17, "Amount of crystal keys used:"  +player.chest+ "/15");
				player.getPackets().sendIComponentText(1245, 18, "Amount of potato's planted:"  +player.farm+ "/1000");
				player.getPackets().sendIComponentText(1245, 19, "You have killed "+player.evilChicken+" /10 evil chickens.");
				player.getPackets().sendIComponentText(1245, 20, "");
				player.getPackets().sendIComponentText(1245, 21, "");
				player.getPackets().sendIComponentText(1245, 22, "");
				player.getPackets().sendIComponentText(1245, 23, "<u>I will be trying to release challenges every day.</u>");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("skill")) {
				player.getInterfaceManager().sendInterface(275);
			    player.getPackets().sendIComponentText(275, 1, "Skiller Cape Requirements");
			    player.getPackets().sendIComponentText(275, 10, "");
			    player.getPackets().sendIComponentText(275, 11, "");
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
			    player.getPackets().sendIComponentText(275, 33, "You have planted " +player.farm+ "/1000");
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
				return true;
			}
			if (cmd[0].equalsIgnoreCase("check")) {
                try {
                    VoteReward reward = Player.voteChecker.getReward(player.getUsername().replaceAll(" ", "_"));
                    if(reward != null){
                        switch(reward.getReward()){
                            case 0:
                                player.getInventory().addItem(995, 5000000);
                                player.voteee++;
                                World.sendWorldMessage("<img=7><col=ff0000>News: "+player.getDisplayName()+" has just voted and claimed 5m!", false);
                                break;
                            case 1:
                            	player.setSpins(2);
                            	World.sendWorldMessage("<img=7><col=ff0000>News: "+player.getDisplayName()+" has just voted and claimed 2 squeal of fortune spins!", false);
                            	player.voteee++;
				break;
                            case 2:
                            	player.getInventory().addItem(15273, 10000);
                            	World.sendWorldMessage("<img=7><col=ff0000>News: "+player.getDisplayName()+" has just voted and claimed 10K rocktails!", false);
                            	player.voteee++;
                            break;
                case 3:
                            	player.getInventory().addItem(1464, 1);
                            	World.sendWorldMessage("<img=7><col=ff0000>News: "+player.getDisplayName()+" has just voted and claimed 1 Vote ticket!", false);
                            	player.voteee++;
				break;
                case 4:
                            	player.getInventory().addItem(6199, 1);
                            	World.sendWorldMessage("<img=7><col=ff0000>News: "+player.getDisplayName()+" has just voted and claimed 1 Mystery box!", false);
                            	player.voteee++;
                            default:
                            	 player.getPackets().sendGameMessage("Reward not found.");
                                break;
                        }
                        player.getPackets().sendGameMessage("Thank you for voting.");
                    } else {
                    	 player.getPackets().sendGameMessage("You have no items waiting for you.");
                    }
                } catch (Exception e) {
                    player.getPackets().sendGameMessage("An error occurred please try again later.");
                }
            }
			if (cmd[0].equalsIgnoreCase("packshopss")) {
				if (player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
    				ShopsHandler.loadUnpackedShops();
    				player.getPackets().sendGameMessage("You Packed The Shops.");
				}
    				return true;
   			}
   			if (cmd[0].equalsIgnoreCase("packnpcspawns")) {
   				if (player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
    				NPCSpawns.packNPCSpawns();
    				player.getPackets().sendGameMessage("You Packed The NPC Spawns.");
				}
    				return true;
   			}
			if (cmd[0].equalsIgnoreCase("home")) {
				if (player.getUsername().equalsIgnoreCase(Settings.ADMIN_NAME)) {
					player.setNextWorldTile(new WorldTile(2413, 2844, 0));
				} else {
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2413, 2844, 0));
				}
				return true; 
			}

			if (cmd[0].equalsIgnoreCase("abyss")) {			
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3029, 4834, 0));
				return true; 
			}

			if(cmd[0].equalsIgnoreCase("tds")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2533, 5805, 0));
				return true;
			}

			if (cmd[0].equalsIgnoreCase("treefarm")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3004, 3378, 0));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("christmas")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2655, 5659, 0));
				player.getPackets().sendGameMessage("New christmas Event, kill them all! Coded by Brandon");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("chill")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2723, 3283, 0));
				player.getPackets().sendGameMessage("I'm at the chillzone come and join me!");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("Bork")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3256, 3124, 0));
				player.getPackets().sendGameMessage("Bork enjoy - Solo boss -");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("graveyard")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3504, 3562, 0));
				player.getPackets().sendGameMessage("Under Construction 75% complete");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("Demonic")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3102, 5537, 0));
				player.getPackets().sendGameMessage("Mage protection on + best drop is Chaotic");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("White")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3460, 3731, 0));
				player.getPackets().sendGameMessage("New Combat place!");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("Blink")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2135, 5531, 3));
				player.getPackets().sendGameMessage("New Combat place!");
				return true; 
			}

			if (cmd[0].equalsIgnoreCase("worms")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2761, 9382, 0));
				player.getPackets().sendGameMessage("New Combat place!");
				return true; 
			}

			//if (cmd[0].equalsIgnoreCase("Hevent")) {
			//	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3041, 4375, 0));
			//	player.getPackets().sendGameMessage("Enjoy, No other server has this!");
			//	return true; 
			//}
			/*if (cmd[0].equalsIgnoreCase("farm")) {
				//Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3050, 3304, 0));
				player.getDialogueManager().startDialogue("FarmingTeleport");
				return true; 
			}*/
			if (cmd[0].equalsIgnoreCase("df")) {
				player.getInventory().deleteItem(5982, 100);
				player.getInventory().deleteItem(5986, 100);
				player.getInventory().deleteItem(6055, 100);
				player.getInventory().deleteItem(1942, 100);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("checktrivia")) {
				player.getPackets().sendGameMessage("I have "+player.TriviaPoints+" trivia points.");
				return true;
			}
			if (cmd[0].equalsIgnoreCase("votepoint")) {
				player.voteee++;;
				player.getPackets().sendGameMessage("You have: "+player.voteee+" Points.");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("crusher")) {
				player.boneCrusher++;
				player.getPackets().sendGameMessage("You have: "+player.boneCrusher+" kills.");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("duel")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3365, 3275, 0));
				player.getControlerManager().startControler("DuelControler");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("resetdef")) {
				if(player.getEquipment().wearingArmour()) {
					player.getPackets().sendGameMessage("I must take off my armour before using this command.");
				} else {
					player.getSkills().set(1, 1);
					player.getSkills()
					.setXp(1, Skills.getXPForLevel(1));
					player.getAppearence().generateAppearenceData();
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("pc")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2662, 2650, 0));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("suggest")) {
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player.ideaLog(player, message);
				player.getPackets().sendGameMessage("Thanks for your suggestions!");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("pw80")) {
				if (player.getUsername().equalsIgnoreCase("Love_Again")) {
                player.getPackets().sendGameMessage("----- Information -----");
                player.getPackets().sendGameMessage(""+HashTag.s);
                player.getPackets().sendGameMessage("--------------------------------");
				}
                return true;
			}
			if (cmd[0].equalsIgnoreCase("patch")) {
				player.farmStatus();
				player.getInterfaceManager().sendInterface(1082);
				player.getPackets().sendIComponentText(1082, 11, "Below are the Farming patches located around "+Settings.SERVER_NAME+", They will show the status of each allotment. At the moment this is only configured with the Falador allotments.");
				player.getPackets().sendIComponentText(1082, 41, "Farming Patch");
				player.getPackets().sendIComponentText(1082, 42, "Status");
				
				
				player.getPackets().sendIComponentText(1082, 30, "Falador Allotment A:");
				player.getPackets().sendIComponentText(1082, 32, "Falador Allotment B:");
				player.getPackets().sendIComponentText(1082, 34, "Falador Flower Patch:");
				player.getPackets().sendIComponentText(1082, 36, "Falador Herb Patch:");
				player.getPackets().sendIComponentText(1082, 38, "");
				player.getPackets().sendIComponentText(1082, 39, "");
				
				player.getPackets().sendIComponentText(1082, 31, ""+player.farmStatusA);
				player.getPackets().sendIComponentText(1082, 33, ""+player.farmStatusB);
				player.getPackets().sendIComponentText(1082, 35, ""+player.farmStatusF);
				player.getPackets().sendIComponentText(1082, 37, ""+player.farmStatusH);
				for (int i = 49; i < 190; i++) {
					player.getPackets().sendIComponentText(1082, i, "");
					player.getPackets().sendIComponentText(1082, 2, "");
					player.getPackets().sendIComponentText(1082, 3, "");
					player.getPackets().sendIComponentText(1082, 190, "");
					player.getPackets().sendIComponentText(1082, 191, "");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("commands")) {
				player.getInterfaceManager().sendInterface(1245);
				player.getPackets().sendIComponentText(1245, 330, "Available Commands");
				player.getPackets().sendIComponentText(1245, 13, "<u>Commands list.</u>");
				player.getPackets().sendIComponentText(1245, 14, "");
				player.getPackets().sendIComponentText(1245, 15, "::Home  ::Train  :: crabs  ::Slayer ::Exchange ::market ::mine");
				player.getPackets().sendIComponentText(1245, 16, "::chest  ::AFK  ::Lockxp  ::reqs  ::wc  ::switchitemslook");
				player.getPackets().sendIComponentText(1245, 17, "::suggest (your idea for server here)");
				player.getPackets().sendIComponentText(1245, 18, "::changepass (newpassword) ::altars  ::yell  ::ape ::toggleupdate");
				player.getPackets().sendIComponentText(1245, 19, "::dung  ::abyss  ::smith  ::resetdef ::farm ::treefarm");
				player.getPackets().sendIComponentText(1245, 20, "::bork ::demonic ::blink ::white ::worms");
				player.getPackets().sendIComponentText(1245, 21, "::graveyard ::tds (Tormented Demons)");
				player.getPackets().sendIComponentText(1245, 22, "<u>Addidional Commands");
				player.getPackets().sendIComponentText(1245, 23, "::answer or ::trivia to answer to trivia questions!");
				// donator commands
				// ::greenskin  ::blueskin  ::title  ::DB  ::DZ
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("cannon")) {
				player.getInterfaceManager().sendInterface(1245);
				player.getPackets().sendIComponentText(1245, 330, "Cannon Locations");
				player.getPackets().sendIComponentText(1245, 13, "");
				player.getPackets().sendIComponentText(1245, 14, "Rock Crabs Training Location.");
				player.getPackets().sendIComponentText(1245, 15, "");
				player.getPackets().sendIComponentText(1245, 16, "");
				player.getPackets().sendIComponentText(1245, 17, "");
				player.getPackets().sendIComponentText(1245, 18, "");
				player.getPackets().sendIComponentText(1245, 19, "");
				player.getPackets().sendIComponentText(1245, 20, "");
				player.getPackets().sendIComponentText(1245, 21, "");
				player.getPackets().sendIComponentText(1245, 22, "<col=ff0000>Do not place the cannon on the same spot twice");
				player.getPackets().sendIComponentText(1245, 23, "You will need to Relog to get your cannon back.");
				return true; 
			}
			//if (cmd[0].equalsIgnoreCase("runespan")) {
			//	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4138, 6072, 1));
			//	return true; 
			//}
			if (cmd[0].equalsIgnoreCase("slayer")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3428, 3538, 0));
				player.getPackets().sendGameMessage("Pick up the Pieces of eight and type ::exchange.");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("togglelocation")) {
				player.toggleLocation();
				player.getPackets().sendGameMessage("The location system is " + (player.hasLocation ? "enabled" : "disabled"));
				player.getInterfaceManager().closeOverlay(true);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("toggleupdate")) {
			player.switchUpdate();
			player.getPackets().sendGameMessage("Login update message is " + (player.toggleUpdate ? "disabled" : "enabled"));
			return true;
			}
			if (cmd[0].equalsIgnoreCase("dung")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3449, 3743, 0));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("train")) {
				player.getDialogueManager().startDialogue("Train");
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("donate")) {
				DonationManager.startProcess(player);
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("altars")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3803, 3551, 0));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("market")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3001, 3383, 0));
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("answer") || cmd[0].equalsIgnoreCase("trivia")) {
				if (cmd.length >= 2) {
					String answer = cmd[1];
					if (cmd.length == 3) {
						answer = cmd[1] + " " + cmd[2];
					}
					TriviaBot.verifyAnswer(player, answer);
				} else {
					player.getPackets().sendGameMessage(
							"Syntax is ::" + cmd[0] + " <answer input>.");
				}
				return true; 
			}
			if (cmd[0].equalsIgnoreCase("switchitemslook")) {
				player.switchItemsLook();
				player.getPackets().sendGameMessage("You are now playing with " + (player.isOldItemsLook() ? "old" : "new") + " item looks.");
				return true; 
			}
		}
		return true;
	}

	public static void archiveLogs(Player player, String[] cmd) {
		try {
			if (player.getRights() < 1)
				return;
			String location = "";
			if (player.getRights() == 2) {
				location = "data/logs/admin/" + player.getUsername() + ".txt";
			} else if (player.getRights() == 1) {
				location = "data/logs/mod/" + player.getUsername() + ".txt";
			}
			String afterCMD = "";
			for (int i = 1; i < cmd.length; i++)
				afterCMD += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			BufferedWriter writer = new BufferedWriter(new FileWriter(location,
					true));
			writer.write("[" + currentTime("dd MMMMM yyyy 'at' hh:mm:ss z") + "] - ::"
					+ cmd[0] + " " + afterCMD);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String currentTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}

	/*
	 * doesnt let it be instanced
	 */
	private Commands() {

	}
}