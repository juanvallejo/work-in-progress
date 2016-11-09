package com.rs.net.decoders;

import java.io.File;
import java.io.IOException;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.minigames.clanwars.ClanWars;
import com.rs.game.minigames.creations.StealingCreation;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Inventory;
import com.rs.game.player.LogicPacket;
import com.rs.game.player.Player;
import com.rs.game.player.PublicChatMessage;
import com.rs.game.player.QuickChatMessage;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.PlayerCombat;
import com.rs.game.player.actions.PlayerFollow;
import com.rs.game.player.actions.Summoning;
import com.rs.game.player.content.Shop;
import com.rs.game.player.content.Commands;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.Notes.Note;
import com.rs.game.player.content.SkillCapeCustomizer;
import com.rs.game.player.content.Vote;
import com.rs.game.player.controlers.HouseCon;
import com.rs.game.player.controlers.HouseControler;
import com.rs.io.InputStream;
import com.rs.net.Session;
import com.rs.net.decoders.handlers.ButtonHandler;
import com.rs.net.decoders.handlers.InventoryOptionsHandler;
import com.rs.net.decoders.handlers.NPCHandler;
import com.rs.net.decoders.handlers.ObjectHandler;
import com.rs.utils.DisplayNames;
import com.rs.utils.IPBanL;
import com.rs.utils.Logger;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;
import com.rs.utils.huffman.Huffman;

public final class WorldPacketsDecoder extends Decoder {

	private static final byte[] PACKET_SIZES = new byte[104];

	private final static int WALKING_PACKET = 8;
	private final static int MINI_WALKING_PACKET = 58;
	private final static int AFK_PACKET = -1;
	public final static int ACTION_BUTTON1_PACKET = 14;
	public final static int ACTION_BUTTON2_PACKET = 67;
	public final static int ACTION_BUTTON3_PACKET = 5;
	public final static int ACTION_BUTTON4_PACKET = 55;
	public final static int ACTION_BUTTON5_PACKET = 68;
	public final static int ACTION_BUTTON6_PACKET = 90;
	public final static int ACTION_BUTTON7_PACKET = 6;
	public final static int ACTION_BUTTON8_PACKET = 32;
	public final static int ACTION_BUTTON9_PACKET = 27;
	public final static int WORLD_MAP_CLICK = 38;
	public final static int ACTION_BUTTON10_PACKET = 96;
	public final static int RECEIVE_PACKET_COUNT_PACKET = 33;
	private final static int MAGIC_ON_ITEM_PACKET = 154;//
	private final static int PLAYER_OPTION_4_PACKET = 17;
	private final static int MOVE_CAMERA_PACKET = 103;
	private final static int INTERFACE_ON_OBJECT = 37;
	private final static int CLICK_PACKET = -1;
	private final static int MOUVE_MOUSE_PACKET = -1;
	private final static int KEY_TYPED_PACKET = -1;
	private final static int CLOSE_INTERFACE_PACKET = 54;
	private final static int COMMANDS_PACKET = 60;
	private final static int ITEM_ON_ITEM_PACKET = 3;
	private final static int IN_OUT_SCREEN_PACKET = -1;
	private final static int DONE_LOADING_REGION_PACKET = 30;
	private final static int PING_PACKET = 21;
	private final static int SCREEN_PACKET = 98;
	private final static int CHAT_TYPE_PACKET = 83;
	private final static int CHAT_PACKET = 53;
	private final static int PUBLIC_QUICK_CHAT_PACKET = 86;
	private final static int ADD_FRIEND_PACKET = 89;
	private final static int ADD_IGNORE_PACKET = 4;
	private final static int REMOVE_IGNORE_PACKET = 73;
	private final static int JOIN_FRIEND_CHAT_PACKET = 36;
	private final static int CHANGE_FRIEND_CHAT_PACKET = 22;
	private final static int KICK_FRIEND_CHAT_PACKET = 74;
	private final static int REMOVE_FRIEND_PACKET = 24;
	private final static int SEND_FRIEND_MESSAGE_PACKET = 82;
	private final static int SEND_FRIEND_QUICK_CHAT_PACKET = 0;
	private final static int OBJECT_CLICK1_PACKET = 26;
	private final static int OBJECT_CLICK2_PACKET = 59;
	private final static int OBJECT_CLICK3_PACKET = 40;
	private final static int OBJECT_CLICK4_PACKET = 23;
	private final static int OBJECT_CLICK5_PACKET = 80;
	private final static int OBJECT_EXAMINE_PACKET = 25;
	private final static int NPC_CLICK1_PACKET = 31;
	private final static int NPC_CLICK2_PACKET = 101;
	private final static int NPC_CLICK3_PACKET = 34;
	private final static int ATTACK_NPC = 20;
	private final static int PLAYER_OPTION_1_PACKET = 42;
	private final static int PLAYER_OPTION_2_PACKET = 46;
	private final static int PLAYER_OPTION_5_PACKET = 77;
	private final static int PLAYER_OPTION_6_PACKET = 49;
	private final static int PLAYER_OPTION_7_PACKET = 51;
	private final static int ITEM_TAKE_PACKET = 57;
	private final static int DIALOGUE_CONTINUE_PACKET = 72;
	private final static int ENTER_INTEGER_PACKET = 81;
	private final static int ENTER_NAME_PACKET = 29;
	private final static int ENTER_STRING_PACKET = -1;
	private final static int SWITCH_INTERFACE_ITEM_PACKET = 76;
	private final static int INTERFACE_ON_PLAYER = 50;
	private final static int INTERFACE_ON_NPC = 66;
	private final static int COLOR_ID_PACKET = 97;
	private static final int NPC_EXAMINE_PACKET = 9;
	private final static int REPORT_ABUSE_PACKET = -1;

	static {
		loadPacketSizes();
	}

	public static void loadPacketSizes() {
		PACKET_SIZES[0] = -1;
		PACKET_SIZES[1] = -2;
		PACKET_SIZES[2] = -1;
		PACKET_SIZES[3] = 16;
		PACKET_SIZES[4] = -1;
		PACKET_SIZES[5] = 8;
		PACKET_SIZES[6] = 8;
		PACKET_SIZES[7] = 3;
		PACKET_SIZES[8] = -1;
		PACKET_SIZES[9] = 3;
		PACKET_SIZES[10] = -1;
		PACKET_SIZES[11] = -1;
		PACKET_SIZES[12] = -1;
		PACKET_SIZES[13] = 7;
		PACKET_SIZES[14] = 8;
		PACKET_SIZES[15] = 6;
		PACKET_SIZES[16] = 2;
		PACKET_SIZES[17] = 3;
		PACKET_SIZES[18] = -1;
		PACKET_SIZES[19] = -2;
		PACKET_SIZES[20] = 3;
		PACKET_SIZES[21] = 0;
		PACKET_SIZES[22] = -1;
		PACKET_SIZES[23] = 9;
		PACKET_SIZES[24] = -1;
		PACKET_SIZES[25] = 9;
		PACKET_SIZES[26] = 9;
		PACKET_SIZES[27] = 8;
		PACKET_SIZES[28] = 4;
		PACKET_SIZES[29] = -1;
		PACKET_SIZES[30] = 0;
		PACKET_SIZES[31] = 3;
		PACKET_SIZES[32] = 8;
		PACKET_SIZES[33] = 4;
		PACKET_SIZES[34] = 3;
		PACKET_SIZES[35] = -1;
		PACKET_SIZES[36] = -1;
		PACKET_SIZES[37] = 17;
		PACKET_SIZES[38] = 4;
		PACKET_SIZES[39] = 4;
		PACKET_SIZES[40] = 9;
		PACKET_SIZES[41] = -1;
		PACKET_SIZES[42] = 3;
		PACKET_SIZES[43] = 7;
		PACKET_SIZES[44] = -2;
		PACKET_SIZES[45] = 7;
		PACKET_SIZES[46] = 3;
		PACKET_SIZES[47] = 4;
		PACKET_SIZES[48] = -1;
		PACKET_SIZES[49] = 3;
		PACKET_SIZES[50] = 11;
		PACKET_SIZES[51] = 3;
		PACKET_SIZES[52] = -1;
		PACKET_SIZES[53] = -1;
		PACKET_SIZES[54] = 0;
		PACKET_SIZES[55] = 8;
		PACKET_SIZES[56] = 3;
		PACKET_SIZES[57] = 7;
		PACKET_SIZES[58] = -1;
		PACKET_SIZES[59] = 9;
		PACKET_SIZES[60] = -1;
		PACKET_SIZES[61] = 7;
		PACKET_SIZES[62] = 7;
		PACKET_SIZES[63] = 12;
		PACKET_SIZES[64] = 4;
		PACKET_SIZES[65] = 3;
		PACKET_SIZES[66] = 11;
		PACKET_SIZES[67] = 8;
		PACKET_SIZES[68] = 8;
		PACKET_SIZES[69] = 15;
		PACKET_SIZES[70] = 1;
		PACKET_SIZES[71] = 2;
		PACKET_SIZES[72] = 6;
		PACKET_SIZES[73] = -1;
		PACKET_SIZES[74] = -1;
		PACKET_SIZES[75] = -2;
		PACKET_SIZES[76] = 16;
		PACKET_SIZES[77] = 3;
		PACKET_SIZES[78] = 1;
		PACKET_SIZES[79] = 3;
		PACKET_SIZES[80] = 9;
		PACKET_SIZES[81] = 4;
		PACKET_SIZES[82] = -2;
		PACKET_SIZES[83] = 1;
		PACKET_SIZES[84] = 1;
		PACKET_SIZES[85] = 3;
		PACKET_SIZES[86] = -1;
		PACKET_SIZES[87] = 4;
		PACKET_SIZES[88] = 3;
		PACKET_SIZES[89] = -1;
		PACKET_SIZES[90] = 8;
		PACKET_SIZES[91] = -2;
		PACKET_SIZES[92] = -1;
		PACKET_SIZES[93] = -1;
		PACKET_SIZES[94] = 9;
		PACKET_SIZES[95] = -2;
		PACKET_SIZES[96] = 8;
		PACKET_SIZES[97] = 2;
		PACKET_SIZES[98] = 6;
		PACKET_SIZES[99] = 2;
		PACKET_SIZES[100] = -2;
		PACKET_SIZES[101] = 3;
		PACKET_SIZES[102] = 7;
		PACKET_SIZES[103] = 4;
	}

	private Player player;
	private int chatType;

	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	@Override
	public void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected()
				&& !player.hasFinished()) {
			int packetId = stream.readPacket(player);
			if (packetId >= PACKET_SIZES.length || packetId < 0) {
				if (Settings.DEBUG)
					System.out.println("PacketId " + packetId
							+ " has fake packet id.");
				break;
			}
			int length = PACKET_SIZES[packetId];
			if (length == -1)
				length = stream.readUnsignedByte();
			else if (length == -2)
				length = stream.readUnsignedShort();
			else if (length == -3)
				length = stream.readInt();
			else if (length == -4) {
				length = stream.getRemaining();
				if (Settings.DEBUG)
					System.out.println("Invalid size for PacketId " + packetId
							+ ". Size guessed to be " + length);
			}
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				if (Settings.DEBUG)
					System.out.println("PacketId " + packetId
							+ " has fake size. - expected size " + length);
				// break;

			}
			/*
			 * System.out.println("PacketId " +packetId+
			 * " has . - expected size " +length);
			 */
			int startOffset = stream.getOffset();
			processPackets(packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}

	public static void decodeLogicPacket(final Player player, LogicPacket packet) {
		int packetId = packet.getId();
		InputStream stream = new InputStream(packet.getData());
		if (packetId == WALKING_PACKET || packetId == MINI_WALKING_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			long currentTime = Utils.currentTimeMillis();
			if (player.getLockDelay() > currentTime)
				return;
			if (player.getFreezeDelay() >= currentTime) {
				player.getPackets().sendGameMessage(
						"A magical force prevents you from moving.");
				return;
			}
			int length = stream.getLength();
			/*if (packetId == MINI_WALKING_PACKET)
				length -= 13;*/
			int baseX = stream.readUnsignedShort128();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			int baseY = stream.readUnsignedShort128();
			int steps = (length - 5) / 2;
			if (steps > 25)
				steps = 25;
			player.stopAll();
			if(forceRun)
				player.setRun(forceRun);
			for (int step = 0; step < steps; step++)
				if (!player.addWalkSteps(baseX + stream.readUnsignedByte(),
						baseY + stream.readUnsignedByte(), 25,
						true))
					break;
		} else if (packetId == INTERFACE_ON_OBJECT) {
			boolean forceRun = stream.readByte128() == 1;
			int itemId = stream.readShortLE128();
			int y = stream.readShortLE128();
			int objectId = stream.readIntV2();
			int interfaceHash = stream.readInt();
			final int interfaceId = interfaceHash >> 16;
			int slot = stream.readShortLE();
			int x = stream.readShort128();
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			long currentTime = Utils.currentTimeMillis();
			if (player.getLockDelay() >= currentTime || player.getEmotesManager().getNextEmoteEnd() >= currentTime)
				return;
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			WorldObject mapObject = World.getRegion(regionId).getObject(objectId, tile);
			if (mapObject == null || mapObject.getId() != objectId)
				return;
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(objectId, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
			final Item item = player.getInventory().getItem(slot);
			if (player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (item == null || item.getId() != itemId)
				return;
			player.stopAll(false); // false
			if(forceRun)
				player.setRun(forceRun);
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE: // inventory
				ObjectHandler.handleItemOnObject(player, object, interfaceId, item);
				break;
			}
		} else if (packetId == PLAYER_OPTION_2_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerFollow(p2));
		} else if (packetId == PLAYER_OPTION_5_PACKET) {//Rubber Chicken
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			player.faceEntity(p2);
			player.setNextAnimation(new Animation(1833));
		} else if (packetId == PLAYER_OPTION_6_PACKET) {
			player.crackerRewards();
		} else if (packetId == PLAYER_OPTION_7_PACKET) {
			 @SuppressWarnings("unused")
			    boolean unknown = stream.readByte() == 1;
			    int playerIndex = stream.readUnsignedShortLE128();
			    Player other = World.getPlayers().get(playerIndex);
			    if (other == null || other.isDead() || other.hasFinished()
			            || !player.getMapRegionsIds().contains(other.getRegionId()))
			        return;
			    if (player.getLockDelay() > Utils.currentTimeMillis())
			        return;
			    if (!other.withinDistance(player, 14)) {
			        player.getPackets().sendGameMessage(
			                "Unable to find target "+other.getDisplayName());
			        return;
			    }
			    player.getDialogueManager().startDialogue("ModPanel", playerIndex);
			return;	
	} else if (packetId == PLAYER_OPTION_4_PACKET) {
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			if(player.isCantTrade()) {
				player.getPackets().sendGameMessage("You are busy.");
				return;
			}
			if (p2.getInterfaceManager().containsScreenInter() || p2.isCantTrade() || player.getControlerManager().getControler() instanceof DuelArena || player.getControlerManager().getControler() instanceof DuelControler) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			if (!p2.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage(
						"Unable to find target "+p2.getDisplayName());
				return;
			}
			if (p2.getTemporaryAttributtes().get("TradeTarget") == player) {
				p2.getTemporaryAttributtes().remove("TradeTarget");
				player.getTrade().openTrade(p2);
				p2.getTrade().openTrade(player);
				return;
			}
			player.getTemporaryAttributtes().put("TradeTarget", p2);
			player.getPackets().sendGameMessage("Sending " + p2.getDisplayName() + " a request...");
			p2.getPackets().sendTradeRequestMessage(player);
		} else if (packetId == PLAYER_OPTION_1_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis()
					|| !player.getControlerManager().canPlayerOption1(p2))
				return;
			if (!player.isCanPvp())
				return;
			if (!player.getControlerManager().canAttack(p2))
				return;

			if (!player.isCanPvp() || !p2.isCanPvp()) {
				player.getPackets()
				.sendGameMessage(
						"You can only attack players in a player-vs-player area.");
				return;
			}
			if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
				if (player.getAttackedBy() != p2
						&& player.getAttackedByDelay() > Utils
						.currentTimeMillis()) {
					player.getPackets().sendGameMessage(
							"You are already in combat.");
					return;
				}
				if (p2.getAttackedBy() != player
						&& p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
					if (p2.getAttackedBy() instanceof NPC) {
						p2.setAttackedBy(player); // changes enemy to player,
						// player has priority over
						// npc on single areas
					} else {
						player.getPackets().sendGameMessage(
								"That player is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(p2));
		} else if (packetId == ATTACK_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead()) {
				return;
			}
			if (player.getLockDelay() > Utils.currentTimeMillis()) {
				return;
			}
			int npcIndex = stream.readUnsignedShort128();
			boolean forceRun = stream.read128Byte() == 1;
			if(forceRun)
				player.setRun(forceRun);
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId())
					|| !npc.getDefinitions().hasAttackOption()) {
				return;
			}
			if (!player.getControlerManager().canAttack(npc)) {
				return;
			}
			if (npc instanceof Familiar) {
				Familiar familiar = (Familiar) npc;
				if (familiar == player.getFamiliar()) {
					player.getPackets().sendGameMessage(
							"You can't attack your own familiar.");
					return;
				}
				if (!familiar.canAttack(player)) {
					player.getPackets().sendGameMessage(
							"You can't attack this npc.");
					return;
				}
			} else if (!npc.isForceMultiAttacked()) {
				if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
					if (player.getAttackedBy() != npc
							&& player.getAttackedByDelay() > Utils
							.currentTimeMillis()) {
						player.getPackets().sendGameMessage(
								"You are already in combat.");
						return;
					}
					if (npc.getAttackedBy() != player
							&& npc.getAttackedByDelay() > Utils
							.currentTimeMillis()) {
						player.getPackets().sendGameMessage(
								"This npc is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(npc));
		} else if (packetId == INTERFACE_ON_PLAYER) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			@SuppressWarnings("unused")
			int junk1 = stream.readUnsignedShort();
			int playerIndex = stream.readUnsignedShort();
			int interfaceHash = stream.readIntV2();
			@SuppressWarnings("unused")
			int junk2 = stream.readUnsignedShortLE128();
			@SuppressWarnings("unused")
			boolean unknown = stream.read128Byte() == 1;
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (componentId == 65535)
				componentId = -1;
			if (componentId != -1
					&& Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			player.stopAll(false);
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE:// Item on player
				//if (!player.getControlerManager().processItemOnPlayer(p2, junk1))
				//	return;
				InventoryOptionsHandler.handleItemOnPlayer(player, p2, junk1);
				break;
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 15)
						|| (interfaceId == 662 && componentId == 65)
						|| (interfaceId == 662 && componentId == 74)
						|| interfaceId == 747 && componentId == 18) {
					if ((interfaceId == 662 && componentId == 74
							|| interfaceId == 747 && componentId == 24 || interfaceId == 747
							&& componentId == 18)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (!player.isCanPvp() || !p2.isCanPvp()) {
						player.getPackets()
						.sendGameMessage(
								"You can only attack players in a player-vs-player area.");
						return;
					}
					if (!player.getFamiliar().canAttack(p2)) {
						player.getPackets()
						.sendGameMessage(
								"You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && componentId == 74
								|| interfaceId == 747
								&& componentId == 18);
						player.getFamiliar().setTarget(p2);
					}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2
								.getCoordFaceX(p2.getSize()), p2
								.getCoordFaceY(p2.getSize()), p2.getPlane()));
						if (!player.getControlerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets()
							.sendGameMessage(
									"You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2
									&& player.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								player.getPackets()
								.sendGameMessage(
										"That "
												+ (player
														.getAttackedBy() instanceof Player ? "player"
																: "npc")
																+ " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player
									&& p2.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets()
									.sendGameMessage(
											"That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager()
						.setAction(new PlayerCombat(p2));
					}
					break;
				}
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 86: // teleblock
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 91: // fire surge
				case 99: // storm of armadyl
				case 36: // bind
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2
								.getCoordFaceX(p2.getSize()), p2
								.getCoordFaceY(p2.getSize()), p2.getPlane()));
						if (!player.getControlerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets()
							.sendGameMessage(
									"You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2
									&& player.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								player.getPackets()
								.sendGameMessage(
										"That "
												+ (player
														.getAttackedBy() instanceof Player ? "player"
																: "npc")
																+ " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player
									&& p2.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets()
									.sendGameMessage(
											"That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager()
						.setAction(new PlayerCombat(p2));
					}
					break;
				}
				break;
			}
			if (Settings.DEBUG)
				System.out.println("Spell:" + componentId);
		} else if (packetId == INTERFACE_ON_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int interfaceHash = stream.readInt();
			int npcIndex = stream.readUnsignedShortLE();
			int interfaceSlot = stream.readUnsignedShortLE128();
			@SuppressWarnings("unused")
			int junk2 =stream.readUnsignedShortLE();
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (componentId == 65535)
				componentId = -1;
			if (componentId != -1
					&& Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId()))
				return;
			player.stopAll(false);
			if (interfaceId != Inventory.INVENTORY_INTERFACE) {
				if (!npc.getDefinitions().hasAttackOption()) {
					player.getPackets().sendGameMessage(
							"You can't attack this npc.");
					return;
				}
			}
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE:
				Item item = player.getInventory().getItem(interfaceSlot);
				if (item == null || !player.getControlerManager().processItemOnNPC(npc, item))
					return;
				InventoryOptionsHandler.handleItemOnNPC(player, npc, item);
				/*@SuppressWarnings("unused")
				//boolean unknown = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShortLE128();
				Player p2 = World.getPlayers().get(playerIndex);
				if (item == null || !player.getControlerManager().processItemOnPlayer(player, item))
					return;
				InventoryOptionsHandler.handleItemOnPlayer(player, npc, item);*/
				break;
				
			case 1165:
				Summoning.attackDreadnipTarget(npc, player);
				break;
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 15)
						|| (interfaceId == 662 && componentId == 65)
						|| (interfaceId == 662 && componentId == 74)
						|| interfaceId == 747 && componentId == 18
						|| interfaceId == 747 && componentId == 24) {
					if ((interfaceId == 662 && componentId == 74 || interfaceId == 747
							&& componentId == 18)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if(npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if (familiar == player.getFamiliar()) {
							player.getPackets().sendGameMessage("You can't attack your own familiar.");
							return;
						}
						if (!player.getFamiliar().canAttack(familiar.getOwner())) {
							player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
							return;
						}
					}
					if (!player.getFamiliar().canAttack(npc)) {
						player.getPackets()
						.sendGameMessage(
								"You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && componentId == 74
								|| interfaceId == 747
								&& componentId == 18);
						player.getFamiliar().setTarget(npc);
					}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc
								.getCoordFaceX(npc.getSize()), npc
								.getCoordFaceY(npc.getSize()), npc.getPlane()));
						if (!player.getControlerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage(
										"You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage(
										"You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(
								new PlayerCombat(npc));
					}
					break;
				}
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 93:
				case 91: // fire surge
				case 99: // storm of Armadyl
				case 36: // bind
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc
								.getCoordFaceX(npc.getSize()), npc
								.getCoordFaceY(npc.getSize()), npc.getPlane()));
						if (!player.getControlerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage(
										"You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage(
										"You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(
								new PlayerCombat(npc));
					}
					break;
				}
				break;
			}
			if (Settings.DEBUG)
				System.out.println("Spell:" + componentId);
		} else if (packetId == NPC_CLICK1_PACKET)
			NPCHandler.handleOption1(player, stream);
		else if (packetId == NPC_CLICK2_PACKET)
			NPCHandler.handleOption2(player, stream);
		else if (packetId == NPC_CLICK3_PACKET)
			NPCHandler.handleOption3(player, stream);
		else if (packetId == OBJECT_CLICK1_PACKET)
			ObjectHandler.handleOption(player, stream, 1);
		else if (packetId == OBJECT_CLICK2_PACKET)
			ObjectHandler.handleOption(player, stream, 2);
		else if (packetId == OBJECT_CLICK3_PACKET)
			ObjectHandler.handleOption(player, stream, 3);
		else if (packetId == OBJECT_CLICK4_PACKET)
			ObjectHandler.handleOption(player, stream, 4);
		else if (packetId == OBJECT_CLICK5_PACKET)
			ObjectHandler.handleOption(player, stream, 5);
		else if (packetId == ITEM_TAKE_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			long currentTime = Utils.currentTimeMillis();
			if (player.getLockDelay() > currentTime)
				// || player.getFreezeDelay() >= currentTime)
				return;
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShortLE();
			final int id = stream.readUnsignedShort();
			boolean forceRun = stream.read128Byte() == 1;
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			final FloorItem item = World.getRegion(regionId).getGroundItem(id,
					tile, player);
			if (item == null)
				return;
			player.stopAll(false);
			if(forceRun)
				player.setRun(forceRun);
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					final FloorItem item = World.getRegion(regionId)
							.getGroundItem(id, tile, player);
					if (item == null)
						return;
			/*		if (player.getRights() > 0 || player.isSupporter()) 
						player.getPackets().sendGameMessage("This item was dropped by [Username] "+item.getOwner().getUsername()+ " [DiplayName] "+item.getOwner().getDisplayName());
			*/		player.setNextFaceWorldTile(tile);
					player.addWalkSteps(tile.getX(), tile.getY(), 1);
					World.removeGroundItem(player, item);
				}
			}, 1, 1));
		}
	}

	public void processPackets(final int packetId, InputStream stream,
			int length) {
		player.setPacketsDecoderPing(Utils.currentTimeMillis());
		if (packetId == PING_PACKET) {
			// kk we ping :)
		} else if (packetId == MOUVE_MOUSE_PACKET) {
			// USELESS PACKET
		} else if (packetId == KEY_TYPED_PACKET) {
			// USELESS PACKET
		} else if (packetId == RECEIVE_PACKET_COUNT_PACKET) {
			// interface packets
			stream.readInt();
		} else if (packetId == ITEM_ON_ITEM_PACKET) {
			InventoryOptionsHandler.handleItemOnItem(player, stream);
		} else if (packetId == MAGIC_ON_ITEM_PACKET) {
			InventoryOptionsHandler.handleMagicOnItem(player, stream);
			//}
		} else if (packetId == AFK_PACKET) {
			player.getSession().getChannel().close();
		} else if (packetId == CLOSE_INTERFACE_PACKET) {
			if (player.hasStarted() && !player.hasFinished() && !player.isRunning()) { //used for old welcome screen
				player.run();
				return;
			}
			player.stopAll();
		} else if (packetId == MOVE_CAMERA_PACKET) {
			// not using it atm
			stream.readUnsignedShort();
			stream.readUnsignedShort();
		} else if (packetId == IN_OUT_SCREEN_PACKET) {
			// not using this check because not 100% efficient
			@SuppressWarnings("unused")
			boolean inScreen = stream.readByte() == 1;
		} else if (packetId == SCREEN_PACKET) {
			int displayMode = stream.readUnsignedByte();
			player.setScreenWidth(stream.readUnsignedShort());
			player.setScreenHeight(stream.readUnsignedShort());
			@SuppressWarnings("unused")
			boolean switchScreenMode = stream.readUnsignedByte() == 1;
			if (!player.hasStarted() || player.hasFinished()
					|| displayMode == player.getDisplayMode()
					|| !player.getInterfaceManager().containsInterface(742))
				return;
			player.setDisplayMode(displayMode);
			player.getInterfaceManager().removeAll();
			player.getInterfaceManager().sendInterfaces();
			player.getInterfaceManager().sendInterface(742);
		} else if (packetId == CLICK_PACKET) {
			int mouseHash = stream.readShortLE128();
			int mouseButton = mouseHash >> 15;
			int time = mouseHash - (mouseButton << 15); // time
			int positionHash = stream.readIntV1();
			int y = positionHash >> 16; // y;
			int x = positionHash - (y << 16); // x
			@SuppressWarnings("unused")
			boolean clicked;
			// mass click or stupid autoclicker, lets stop lagg
			if (time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0
					|| y > player.getScreenHeight()) {
				// player.getSession().getChannel().close();
				clicked = false;
				return;
			}
			clicked = true;
		} else if (packetId == DIALOGUE_CONTINUE_PACKET) {
			int interfaceHash = stream.readInt();
			int junk = stream.readShort128();
			int interfaceId = interfaceHash >> 16;
			int buttonId = (interfaceHash & 0xFF);
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				// hack, or server error or client error
				// player.getSession().getChannel().close();
				return;
			}
			if (!player.isRunning()
					|| !player.getInterfaceManager().containsInterface(
							interfaceId))
				return;
			if(Settings.DEBUG)
				Logger.log(this, "Dialogue: "+interfaceId+", "+buttonId+", "+junk);
			int componentId = interfaceHash - (interfaceId << 16);
			player.getDialogueManager().continueDialogue(interfaceId,
					componentId);
		} else if (packetId == WORLD_MAP_CLICK) {
			int coordinateHash = stream.readInt();
			int x = coordinateHash >> 14;
			int y = coordinateHash & 0x3fff;
			int plane = coordinateHash >> 28;
            Integer hash  =  (Integer)player.getTemporaryAttributtes().get("worldHash");
			if (hash == null || coordinateHash != hash)
				player.getTemporaryAttributtes().put("worldHash", coordinateHash);
			else {
				player.getTemporaryAttributtes().remove("worldHash");
				player.getHintIconsManager().addHintIcon(x, y, plane, 20, 0, 2, -1, true);
				player.getPackets().sendConfig(1159, coordinateHash);
            }
		} else if (packetId == ACTION_BUTTON1_PACKET
				|| packetId == ACTION_BUTTON2_PACKET
				|| packetId == ACTION_BUTTON4_PACKET
				|| packetId == ACTION_BUTTON5_PACKET
				|| packetId == ACTION_BUTTON6_PACKET
				|| packetId == ACTION_BUTTON7_PACKET
				|| packetId == ACTION_BUTTON8_PACKET
				|| packetId == ACTION_BUTTON3_PACKET
				|| packetId == ACTION_BUTTON9_PACKET
				|| packetId == ACTION_BUTTON10_PACKET) {
			ButtonHandler.handleButtons(player, stream, packetId);
		} else if (packetId == ENTER_NAME_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			String value = stream.readString();
			int input = stream.readInt();
			if (value.equals(""))
				return;
			
			if (player.getInterfaceManager().containsInterface(1108)) 
				player.getFriendsIgnores().setChatPrefix(value);
			else if (player.getTemporaryAttributtes().get("yellcolor") == Boolean.TRUE) {
				if(value.length() != 6) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The HEX yell color you wanted to pick cannot be longer and shorter then 6.");
				} else if(Utils.containsInvalidCharacter(value) || value.contains("_")) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The requested yell color can only contain numeric and regular characters.");
				} else {
					player.setYellColor(value);
					player.getDialogueManager().startDialogue("SimpleMessage", "Your yell color has been changed to <col="+player.getYellColor()+">"+player.getYellColor()+"</col>.");
				}
				player.getTemporaryAttributtes().put("yellcolor", Boolean.FALSE);
			}
			
			else if (player.getTemporaryAttributtes().get("entering_note") == Boolean.TRUE) {
				player.getNotes().add(new Note(value, 1));
				player.getNotes().refresh();
				player.getTemporaryAttributtes().put("entering_note", Boolean.FALSE);
				return;
			} else if (player.getTemporaryAttributtes().get("editing_note") == Boolean.TRUE) {
				Note note = (Note) player.getTemporaryAttributtes().get("curNote");
				player.getNotes().getNotes().get(player.getNotes().getNotes().indexOf(note));
				player.getNotes().refresh();
				player.getTemporaryAttributtes().put("editing_note", Boolean.FALSE);
			} else if(player.getTemporaryAttributtes().get("teleto_player") == Boolean.TRUE) {
	            String name = value;
	            Player target = World.getPlayerByDisplayName(name);
	            if(target == null) {
	                player.getPackets().sendGameMessage(
	                        "Couldn't find player " + name + ".");
	            player.getTemporaryAttributtes().put("teleto_player", Boolean.FALSE);
	            } else {
	                player.setNextWorldTile(target);
	            player.getTemporaryAttributtes().put("teleto_player", Boolean.FALSE);
	            return;
	     }
			} else if(player.getTemporaryAttributtes().get("highscores") == Boolean.TRUE) {
	            String name = value;
	            Player target = World.getPlayerByDisplayName(name);
	            if(target == null) {
	                player.getPackets().sendGameMessage(
	                        "Couldn't find " + name + ".");
	            player.getTemporaryAttributtes().put("highscores", Boolean.FALSE);
	            } else {
	            	int totalXp = 0;
	            	for(int i = 0; i < 24; i++) {
	                    totalXp += player.getSkills().getXp(i);
	                }
	            	int totalXp2 = 0;
	            	for(int i = 0; i < 24; i++) {
	                    totalXp2 += target.getSkills().getXp(i);
	                }
	            	if (totalXp > totalXp2) {
	            		player.HoL = "a higher";
	            	} else if (target.getUsername() == player.getUsername()) {
	            		player.HoL = "the same";
	            	} else {
	            		player.HoL = "a lower";
	            	}
	        		player.getInterfaceManager().sendInterface(960);
	        		player.getPackets().sendIComponentText(960, 69, "The Highscores Of "+target.getDisplayName());
	        		player.getPackets().sendIComponentText(960, 49, "<u>Combat</u>");
	        		player.getPackets().sendIComponentText(960, 56, "Level "+ target.getSkills().getLevel(Skills.ATTACK) +" in Attack.");
	        		player.getPackets().sendIComponentText(960, 61, "Level "+ target.getSkills().getLevel(Skills.STRENGTH) +" in Strength.");
	        		player.getPackets().sendIComponentText(960, 62, "Level "+ target.getSkills().getLevel(Skills.DEFENCE) +" in Defence.");
	        		player.getPackets().sendIComponentText(960, 54, "Level "+ target.getSkills().getLevel(Skills.RANGE) +" in Range.");
	        		player.getPackets().sendIComponentText(960, 63, "Level "+ target.getSkills().getLevel(Skills.PRAYER) +" in Prayer.");
	        		player.getPackets().sendIComponentText(960, 55, "Level "+ target.getSkills().getLevel(Skills.MAGIC) +" in Magic.");
	        		player.getPackets().sendIComponentText(960, 51, "Level "+ target.getSkills().getLevel(Skills.HITPOINTS) +" in Hitpoints.");
	        		player.getPackets().sendIComponentText(960, 60, "Level "+ target.getSkills().getLevel(Skills.SUMMONING) +" in Summoning.");
	        		player.getPackets().sendIComponentText(960, 58, "Level "+ target.getSkills().getLevel(Skills.DUNGEONEERING) +" in Dungeoneering.");
	        		player.getPackets().sendIComponentText(960, 53, "<u>Skills</u>");
	        		player.getPackets().sendIComponentText(960, 50, "Level "+ target.getSkills().getLevel(Skills.AGILITY) +" in Agility.");
	        		player.getPackets().sendIComponentText(960, 57, "Level "+ target.getSkills().getLevel(Skills.HERBLORE) +" in Herblore.");
	        		player.getPackets().sendIComponentText(960, 59, "Level "+ target.getSkills().getLevel(Skills.THIEVING) +" in Thieving.");
	        		player.getPackets().sendIComponentText(960, 52, "Level "+ target.getSkills().getLevel(Skills.CRAFTING) +" in Crafting.");
	        		
	        		//Right side page
	        		player.getPackets().sendIComponentText(960, 33, "Level "+ target.getSkills().getLevel(Skills.FLETCHING) +" in Fletching.");
	        		player.getPackets().sendIComponentText(960, 39, "Level "+ target.getSkills().getLevel(Skills.SLAYER) +" in Slayer.");
	        		player.getPackets().sendIComponentText(960, 36, "Level "+ target.getSkills().getLevel(Skills.HUNTER) +" in Hunter.");
	        		player.getPackets().sendIComponentText(960, 44, "Level "+ target.getSkills().getLevel(Skills.MINING) +" in Mining.");
	        		player.getPackets().sendIComponentText(960, 37, "Level "+ target.getSkills().getLevel(Skills.SMITHING) +" in Smithing.");
	        		player.getPackets().sendIComponentText(960, 46, "Level "+ target.getSkills().getLevel(Skills.FISHING) +" in Fishing.");
	        		player.getPackets().sendIComponentText(960, 40, "Level "+ target.getSkills().getLevel(Skills.COOKING) +" in Cooking.");
	        		player.getPackets().sendIComponentText(960, 42, "Level "+ target.getSkills().getLevel(Skills.FIREMAKING) +" in Firemaking.");
	        		player.getPackets().sendIComponentText(960, 34, "Level "+ target.getSkills().getLevel(Skills.WOODCUTTING) +" in Woodcutting.");
	        		player.getPackets().sendIComponentText(960, 35, "Level "+ target.getSkills().getLevel(Skills.FARMING) +" in Farming.");	        		
	        		player.getPackets().sendIComponentText(960, 38, "Level "+ target.getSkills().getLevel(Skills.CONSTRUCTION) +" in Construction.");
	        		player.getPackets().sendIComponentText(960, 43, "Level "+ target.getSkills().getLevel(Skills.RUNECRAFTING) +" in Runecrafting.");
	        		player.getPackets().sendIComponentText(960, 47, "You have <u>"+player.HoL+"</u> total level");
	        		player.getPackets().sendIComponentText(960, 45, "than <u>"+target.getUsername()+"</u>.");
	        		player.getPackets().sendIComponentText(960, 41, "Times Prestiged: "+target.prestigeNumber);
	        		
	        		//buttons
	        		player.getPackets().sendIComponentText(960, 70, "Previous");
	        		player.getPackets().sendIComponentText(960, 71, "Next");
	            player.getTemporaryAttributtes().put("highscores", Boolean.FALSE);
	            return;
	     }
			} else if(player.getTemporaryAttributtes().get("drop_log") == Boolean.TRUE) {
	            String name = value;
	            Player target = World.getPlayerByDisplayName(name);
	            if(target == null) {
	                player.getPackets().sendGameMessage(
	                        "Couldn't find " + name + ".");
	            player.getTemporaryAttributtes().put("drop_log", Boolean.FALSE);
	            } else {
	        		player.getInterfaceManager().sendInterface(960);
	        		player.getPackets().sendIComponentText(960, 69, "The Drop Log Of "+target.getDisplayName()); // Title
	        		player.getPackets().sendIComponentText(960, 49, "Bandos Chestplate: "+target.bandosChest);
	        		player.getPackets().sendIComponentText(960, 56, "Bandos Tasset: "+target.bandosTassets);
	        		player.getPackets().sendIComponentText(960, 61, "Bandos Hilt: "+target.bandosHilt);
	        		player.getPackets().sendIComponentText(960, 62, "");
	        		player.getPackets().sendIComponentText(960, 54, "Armadyl Chestplate: "+target.armadylPlate);
	        		player.getPackets().sendIComponentText(960, 63, "Armadyl Plateskirt: "+target.armadylLegs);
	        		player.getPackets().sendIComponentText(960, 55, "Armadyl Helmet: "+target.armadylHelm);
	        		player.getPackets().sendIComponentText(960, 51, "Armadyl Hilt: "+target.armadylHilt);
	        		player.getPackets().sendIComponentText(960, 60, "");
	        		player.getPackets().sendIComponentText(960, 58, "Torva Helm: "+target.torvaHelm);
	        		player.getPackets().sendIComponentText(960, 53, "Torva Plate: "+target.torvaPlate);
	        		player.getPackets().sendIComponentText(960, 50, "Torva Platelegs: "+target.torvaLegs);
	        		player.getPackets().sendIComponentText(960, 57, "");
	        		player.getPackets().sendIComponentText(960, 59, "Dragon Claws: "+target.dragonClaws);
	        		player.getPackets().sendIComponentText(960, 52, "");
	        		
	        		//Right side page
	        		player.getPackets().sendIComponentText(960, 33, "Virtus Mask: "+target.virtusMask);
	        		player.getPackets().sendIComponentText(960, 39, "Virtus Robe Top: "+target.virtusTop);
	        		player.getPackets().sendIComponentText(960, 36, "Virtus Robe Legs: "+target.virtusLegs);
	        		player.getPackets().sendIComponentText(960, 44, "");
	        		player.getPackets().sendIComponentText(960, 37, "Pernix Cowl: "+target.pernixCowl);
	        		player.getPackets().sendIComponentText(960, 46, "Pernix Body: "+target.pernixBody);
	        		player.getPackets().sendIComponentText(960, 40, "Pernix Chaps: "+target.pernixChaps);
	        		player.getPackets().sendIComponentText(960, 42, "");
	        		player.getPackets().sendIComponentText(960, 34, "Dharoks Helm: "+target.dharoksHelm);
	        		player.getPackets().sendIComponentText(960, 35, "Dharoks Plate: "+target.dharoksPlate);
	        		player.getPackets().sendIComponentText(960, 38, "Dharoks Legs: "+target.dharoksLegs);
	        		player.getPackets().sendIComponentText(960, 43, "Dharoks Greataxe: "+target.dharoksGreataxe);
	        		player.getPackets().sendIComponentText(960, 47, "");
	        		player.getPackets().sendIComponentText(960, 45, "PK'd an item worth:");
	        		player.getPackets().sendIComponentText(960, 41, ""+Shop.commas(Integer.toString(target.highestGPKill))+" coins.");


	        		//Right and left button text
	        		player.getPackets().sendIComponentText(960, 70, "");
	        		player.getPackets().sendIComponentText(960, 71, "");
	            player.getTemporaryAttributtes().put("drop_log", Boolean.FALSE);
	            return;
	     }
			} else if(player.getTemporaryAttributtes().get("teleto_house") == Boolean.TRUE) {
				int[] boundChuncks;
				boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8); 
	            String name = value;
	            Player target = World.getPlayerByDisplayName(name);
	            if(target == null || target.inBuildMode == true || target.hasLocked == true) {
	                player.getPackets().sendGameMessage(
	                        "Cannot enter the house of " + name + ".");
	            player.getTemporaryAttributtes().put("teleto_house", Boolean.FALSE);
	            } else {
	            player.setNextWorldTile(target);
	            player.out("This has not been fully coded yet.");
	            player.getTemporaryAttributtes().put("teleto_house", Boolean.FALSE);
	            return;
	     }
			}   else if(player.getTemporaryAttributtes().get("teletome_player") == Boolean.TRUE) {
	                String name = value;
	                Player target = World.getPlayerByDisplayName(name);
	                if(target == null) {
	                    player.getPackets().sendGameMessage(
	                            "Couldn't find player " + name + ".");
	                    player.getTemporaryAttributtes().put("teletome_player", Boolean.FALSE);
	         } else {
	                    if (target.isLocked() || target.getControlerManager().getControler() != null) {
	                        player.getPackets().sendGameMessage("You cannot teleport this player.");
	                        player.getTemporaryAttributtes().put("teletome_player", Boolean.FALSE);
	                        return;
	                    }
	                    player.getTemporaryAttributtes().put("teletome_player", Boolean.FALSE);
	                    target.setNextWorldTile(player);
	                }
	                return;
			}
		     else if(player.getTemporaryAttributtes().get("kick_player") == Boolean.TRUE) {
		            String name = value;
		            Player target = World.getPlayerByDisplayName(name);
		            if (target == null) {
		                player.getPackets().sendGameMessage(
		                        Utils.formatPlayerNameForDisplay(name)+" is not logged in.");
		                player.getTemporaryAttributtes().put("kick_player", Boolean.FALSE);
		                return;
		            }
		            if (target.getUsername().equalsIgnoreCase("brandon")) {
		                player.getPackets().sendGameMessage(
		                        "<col=FF0000>Unable to Kick A Developer.");
		                player.getTemporaryAttributtes().put("kick_player", Boolean.FALSE);
		                return;
		            }
		            target.getSession().getChannel().close();
		            player.getPackets().sendGameMessage("You have kicked: "+target.getDisplayName()+".");
		            player.getTemporaryAttributtes().put("kick_player", Boolean.FALSE);
		            return;
		     }
		     
		     else if(player.getTemporaryAttributtes().get("mute_player") == Boolean.TRUE) {
		            String name = value;
		            Player target = World.getPlayerByDisplayName(name);
		            if (target != null) {
		                //target.setMuted(Utils.currentTimeMillis()
		                        //+ (1 * 60 * 60 * 1000));
		                target.setMuted(Utils.currentTimeMillis()
		                        + (1 * 60 * 60 * 1000));
		                target.getPackets().sendGameMessage(
		                        "You've been muted for " + ("1 Hour by ") +Utils.formatPlayerNameForDisplay(player.getUsername())+".");
		                player.getPackets().sendGameMessage(
		                        "You have muted " + ("1 hour by ") + target.getDisplayName()+".");
		                player.getTemporaryAttributtes().put("mute_player", Boolean.FALSE);
		            } else {
		                name = Utils.formatPlayerNameForProtocol(name);
		                if(!SerializableFilesManager.containsPlayer(name)) {
		                    player.getPackets().sendGameMessage(
		                            "Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
		                    player.getTemporaryAttributtes().put("mute_player", Boolean.FALSE);
		                    return;
		                }
		                if (target.getUsername().equalsIgnoreCase("brandon")) {
		                    player.getPackets().sendGameMessage(
		                            "<col=FF0000>Unable to mute a developer.");
		                    player.getTemporaryAttributtes().put("mute_player", Boolean.FALSE);
		                    return;
		                }
		                target = SerializableFilesManager.loadPlayer(name);
		                target.setUsername(name);
		                target.setMuted(Utils.currentTimeMillis()
		                        + (1 * 60 * 60 * 1000));
		                player.getPackets().sendGameMessage(
		                        "You have muted " + ("1 hour by ") + target.getDisplayName()+".");
		                SerializableFilesManager.savePlayer(target);
		                player.getTemporaryAttributtes().put("mute_player", Boolean.FALSE);
		            }
		            return;
		      
		     }
		     else if(player.getTemporaryAttributtes().get("mute_player1") == Boolean.TRUE) {
		            String name = value;
		            Player target = World.getPlayerByDisplayName(name);
		            if (target != null) {
		                //target.setMuted(Utils.currentTimeMillis()
		                        //+ (1 * 60 * 60 * 1000));
		                target.setMuted(Utils.currentTimeMillis()
		                        + (6 * 60 * 60 * 1000));
		                target.getPackets().sendGameMessage(
		                        "You've been muted for " + ("6 Hour by ") +Utils.formatPlayerNameForDisplay(player.getUsername())+".");
		                player.getPackets().sendGameMessage(
		                        "You have muted " + ("6 hour by ") + target.getDisplayName()+".");
		                player.getTemporaryAttributtes().put("mute_player1", Boolean.FALSE);
		            } else {
		                name = Utils.formatPlayerNameForProtocol(name);
		                if(!SerializableFilesManager.containsPlayer(name)) {
		                    player.getPackets().sendGameMessage(
		                            "Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
		                    player.getTemporaryAttributtes().put("mute_player1", Boolean.FALSE);
		                    return;
		                }
		                if (target.getUsername().equalsIgnoreCase("mod_nick")) {
		                    player.getPackets().sendGameMessage(
		                            "<col=FF0000>Unable to mute a developer.");
		                    player.getTemporaryAttributtes().put("mute_player1", Boolean.FALSE);
		                    return;
		                }
		                target = SerializableFilesManager.loadPlayer(name);
		                target.setUsername(name);
		                target.setMuted(Utils.currentTimeMillis()
		                        + (6 * 60 * 60 * 1000));
		                player.getPackets().sendGameMessage(
		                        "You have muted " + ("6 hour by ") + target.getDisplayName()+".");
		                SerializableFilesManager.savePlayer(target);
		                player.getTemporaryAttributtes().put("mute_player1", Boolean.FALSE);
		            }
		            return;
		      
		     }
		      
		             else if(player.getTemporaryAttributtes().get("mute_player2") == Boolean.TRUE) {
		            String name = value;
		            Player target = World.getPlayerByDisplayName(name);
		            if (target != null) {
		                //target.setMuted(Utils.currentTimeMillis()
		                        //+ (1 * 60 * 60 * 1000));
		                target.setMuted(Utils.currentTimeMillis()
		                        + (12 * 60 * 60 * 1000));
		                target.getPackets().sendGameMessage(
		                        "You've been muted for " + ("12 Hour by ") +Utils.formatPlayerNameForDisplay(player.getUsername())+".");
		                player.getPackets().sendGameMessage(
		                        "You have muted " + ("12 hour by ") + target.getDisplayName()+".");
		                player.getTemporaryAttributtes().put("mute_player2", Boolean.FALSE);
		            } else {
		                name = Utils.formatPlayerNameForProtocol(name);
		                if(!SerializableFilesManager.containsPlayer(name)) {
		                    player.getPackets().sendGameMessage(
		                            "Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
		                    player.getTemporaryAttributtes().put("mute_player2", Boolean.FALSE);
		                    return;
		                }
		                if (target.getUsername().equalsIgnoreCase("mod_nick")) {
		                    player.getPackets().sendGameMessage(
		                            "<col=FF0000>Unable to mute a developer.");
		                    player.getTemporaryAttributtes().put("mute_player2", Boolean.FALSE);
		                    return;
		                }
		                target = SerializableFilesManager.loadPlayer(name);
		                target.setUsername(name);
		                target.setMuted(Utils.currentTimeMillis()
		                        + (12 * 60 * 60 * 1000));
		                player.getPackets().sendGameMessage(
		                        "You have muted " + ("12 hour by ") + target.getDisplayName()+".");
		                SerializableFilesManager.savePlayer(target);
		                player.getTemporaryAttributtes().put("mute_player2", Boolean.FALSE);
		            }
		            return;
		      
		     }
		      
		     else if(player.getTemporaryAttributtes().get("mute_player3") == Boolean.TRUE) {
		            String name = value;
		            Player target = World.getPlayerByDisplayName(name);
		            if (target != null) {
		                //target.setMuted(Utils.currentTimeMillis()
		                        //+ (1 * 60 * 60 * 1000));
		                target.setMuted(Utils.currentTimeMillis()
		                        + (24 * 60 * 60 * 1000));
		                target.getPackets().sendGameMessage(
		                        "You've been muted for " + ("1 day by ") +Utils.formatPlayerNameForDisplay(player.getUsername())+".");
		                player.getPackets().sendGameMessage(
		                        "You have muted " + ("1 day by ") + target.getDisplayName()+".");
		                player.getTemporaryAttributtes().put("mute_player3", Boolean.FALSE);
		            } else {
		                name = Utils.formatPlayerNameForProtocol(name);
		                if(!SerializableFilesManager.containsPlayer(name)) {
		                    player.getPackets().sendGameMessage(
		                            "Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
		                    player.getTemporaryAttributtes().put("mute_player3", Boolean.FALSE);
		                    return;
		                }
		                if (target.getUsername().equalsIgnoreCase("mod_nick")) {
		                    player.getPackets().sendGameMessage(
		                            "<col=FF0000>Unable to mute a developer.");
		                    player.getTemporaryAttributtes().put("mute_player3", Boolean.FALSE);
		                    return;
		                }
		                target = SerializableFilesManager.loadPlayer(name);
		                target.setUsername(name);
		                target.setMuted(Utils.currentTimeMillis()
		                        + (24 * 60 * 60 * 1000));
		                player.getPackets().sendGameMessage(
		                        "You have muted " + ("1 day by ") + target.getDisplayName()+".");
		                SerializableFilesManager.savePlayer(target);
		                player.getTemporaryAttributtes().put("mute_player3", Boolean.FALSE);
		            }
		            return;
		      
		     }
		      
		            else if (player.getTemporaryAttributtes().get("mute_player4") == Boolean.TRUE) {
		            String name = value;
		            Player target = World.getPlayerByDisplayName(name);
		            if (target != null) {
		                //target.setMuted(Utils.currentTimeMillis()
		                        //+ (1 * 60 * 60 * 1000));
		                target.setMuted(Utils.currentTimeMillis()
		                        + (48 * 60 * 60 * 1000));
		                target.getPackets().sendGameMessage(
		                        "You've been muted for " + ("2 day by ") +Utils.formatPlayerNameForDisplay(player.getUsername())+".");
		                player.getPackets().sendGameMessage(
		                        "You have muted " + ("2 day by ") + target.getDisplayName()+".");
		                player.getTemporaryAttributtes().put("mute_player4", Boolean.FALSE);
		            } else {
		                name = Utils.formatPlayerNameForProtocol(name);
		                if(!SerializableFilesManager.containsPlayer(name)) {
		                    player.getPackets().sendGameMessage(
		                            "Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
		                    player.getTemporaryAttributtes().put("mute_player4", Boolean.FALSE);
		                    return;
		                }
		                if (target.getUsername().equalsIgnoreCase("mod_nick")) {
		                    player.getPackets().sendGameMessage(
		                            "<col=FF0000>Unable to mute a developer.");
		                    player.getTemporaryAttributtes().put("mute_player4", Boolean.FALSE);
		                    return;
		                }
		                target = SerializableFilesManager.loadPlayer(name);
		                target.setUsername(name);
		                target.setMuted(Utils.currentTimeMillis()
		                        + (48 * 60 * 60 * 1000));
		                player.getPackets().sendGameMessage(
		                        "You have muted " + (" 2 day ") + target.getDisplayName()+".");
		                SerializableFilesManager.savePlayer(target);
		                player.getTemporaryAttributtes().put("mute_player4", Boolean.FALSE);
		            }
		            return;
		      
		     }
		         
		            else if (player.getTemporaryAttributtes().get("ban_player") == Boolean.TRUE) {
		                player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
		            }
		         
		            else if (player.getTemporaryAttributtes().get("jail_player") == Boolean.TRUE) {
		                String name = value;
		                Player target = World.getPlayerByDisplayName(name);
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
		                    player.getTemporaryAttributtes().put("jail_player", Boolean.FALSE);
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
		                            "You have Jailed 24 hours: "+Utils.formatPlayerNameForDisplay(name)+".");
		                    player.getTemporaryAttributtes().put("jail_player", Boolean.FALSE);
		                    try {
		                        SerializableFilesManager.storeSerializableClass(target, acc1);
		                    } catch (IOException e) {
		                        e.printStackTrace();
		                    }
		                     
		                }
		                if (target.getUsername().equalsIgnoreCase("mod_nick")) {
		                    player.getPackets().sendGameMessage(
		                            "<col=FF0000>Unable to mute a developer.");
		                    player.getTemporaryAttributtes().put("jail_player", Boolean.FALSE);
		                    return;
		                }
		 
		                player.getTemporaryAttributtes().put("jail_player", Boolean.FALSE);
		                return;
		                //player.getTemporaryAttributtes().put("unjail_player", Boolean.FALSE);
		            }
		         
		            else if (player.getTemporaryAttributtes().get("unjail_player") == Boolean.TRUE) {
		                //name = "";
		                String name = value;
		                Player target = World.getPlayerByDisplayName(name);
		                if (target != null) {
		                    target.setJailed(0);
		                    target.getControlerManager()
		                    .startControler("JailControler");
		                    target.getPackets().sendGameMessage(
		                            "You've been unjailed by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
		                    player.getPackets().sendGameMessage(
		                            "You have unjailed: "+target.getDisplayName()+".");
		                    SerializableFilesManager.savePlayer(target);
		                    player.getTemporaryAttributtes().put("unjail_player", Boolean.FALSE);
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
		                    player.getTemporaryAttributtes().put("unjail_player", Boolean.FALSE);
		                    try {
		                        SerializableFilesManager.storeSerializableClass(target, acc1);
		                    } catch (IOException e) {
		                        e.printStackTrace();
		                    }
		                }
		                return;
		            }
		         
		            else if (player.getTemporaryAttributtes().get("unban_player") == Boolean.TRUE) {
		                //name = "";
		                String name = value;
		                Player target = World.getPlayerByDisplayName(name);
		                if (target != null) {
		                    IPBanL.unban(target);
		                    player.getPackets().sendGameMessage("You have unbanned: "+target.getDisplayName()+".");
		                    player.getTemporaryAttributtes().put("unban_player", Boolean.FALSE);
		                } else {
		                    name = Utils.formatPlayerNameForProtocol(name);
		                    if(!SerializableFilesManager.containsPlayer(name)) {
		                        player.getPackets().sendGameMessage(
		                                "Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
		                        player.getTemporaryAttributtes().put("unban_player", Boolean.FALSE);
		                        return;
		                    }
		                    target = SerializableFilesManager.loadPlayer(name);
		                    target.setUsername(name);
		                    IPBanL.unban(target);
		                    player.getPackets().sendGameMessage("You have unbanned: "+target.getDisplayName()+".");
		                    SerializableFilesManager.savePlayer(target);
		                    player.getTemporaryAttributtes().put("unban_player", Boolean.FALSE);
		                }
		                return;
		            }
		            else if (player.getTemporaryAttributtes().get("ban_player") == Boolean.TRUE) {
		                String name = value;
		                Player target = World.getPlayerByDisplayName(name);
		                if (target != null) {
		                    target.setBanned(Utils.currentTimeMillis()
		                            + (12 * 60 * 60 * 1000));
		                    target.getSession().getChannel().close();
		                    player.getPackets().sendGameMessage("You have banned 12 hours: "+target.getDisplayName()+".");
		                    player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
		                } else {
		                    name = Utils.formatPlayerNameForProtocol(name);
		                    if(!SerializableFilesManager.containsPlayer(name)) {
		                        player.getPackets().sendGameMessage(
		                                "Account name "+Utils.formatPlayerNameForDisplay(name)+" doesn't exist.");
		                        player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
		                        return;
		                    }
		                    if (target.getUsername().equalsIgnoreCase("tzhaar")) {
		                        player.getPackets().sendGameMessage(
		                                "<col=FF0000>Unable to mute a developer.");
		                        player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
		                        return;
		                    }
		                    target = SerializableFilesManager.loadPlayer(name);
		                    target.setUsername(name);
		                    target.setBanned(Utils.currentTimeMillis()
		                            + (12 * 60 * 60 * 1000));
		                    player.getPackets().sendGameMessage(
		                            "You have banned 12 hours: "+Utils.formatPlayerNameForDisplay(name)+".");
		                    SerializableFilesManager.savePlayer(target);
		                    player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
		                }
		                return;
		            }
		            else if (player.getTemporaryAttributtes().get("unmute_player") == Boolean.TRUE) {
		                String name = value;
		                Player target = World.getPlayerByDisplayName(name);
		                if (target != null) {
		                    target.setMuted(0);
		                    target.getPackets().sendGameMessage(
		                            "You've been unmuted by "+Utils.formatPlayerNameForDisplay(player.getUsername())+".");
		                    player.getPackets().sendGameMessage(
		                            "You have unmuted: "+target.getDisplayName()+".");
		                    SerializableFilesManager.savePlayer(target);
		                    player.getTemporaryAttributtes().put("unmute_player", Boolean.FALSE);
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
		                    player.getTemporaryAttributtes().put("unmute_player", Boolean.FALSE);
		                    try {
		                        SerializableFilesManager.storeSerializableClass(target, acc1);
		                    } catch (IOException e) {
		                        e.printStackTrace();
		                    }
		                }
		                player.getTemporaryAttributtes().put("unmute_player", Boolean.FALSE);
		                return;

			} else if (player.getTemporaryAttributtes().get("view_name") == Boolean.TRUE) {
				player.getTemporaryAttributtes().remove("view_name");
				Player other = World.getPlayerByDisplayName(value);
				if (other == null) {
					player.getPackets().sendGameMessage("Couldn't find player.");
					return;
				}
				ClanWars clan = other.getCurrentFriendChat() != null ? other.getCurrentFriendChat().getClanWars() : null;
				if (clan == null) {
					player.getPackets().sendGameMessage("This player's clan is not in war.");
					return;
				}
				if (clan.getSecondTeam().getOwnerDisplayName() != other.getCurrentFriendChat().getOwnerDisplayName()) {
					player.getTemporaryAttributtes().put("view_prefix", 1);
				}
				player.getTemporaryAttributtes().put("view_clan", clan);
				ClanWars.enter(player);
				
			} else if (player.getTemporaryAttributtes().remove("setdisplay") != null) {
				if(Utils.invalidAccountName(Utils
						.formatPlayerNameForProtocol(value))) {
					player.getPackets().sendGameMessage("Invalid name!");
					return;
				}
				if(!DisplayNames.setDisplayName(player, value)) {
					player.getPackets().sendGameMessage("Name already in use!");
					return;
				}
				player.getPackets().sendGameMessage("Changed display name!");
			} else if (player.getTemporaryAttributtes().remove("checkvoteinput") != null) 
				Vote.checkVote(player, value);
			
		} else if (packetId == ENTER_STRING_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			String value = stream.readString();
			if (value.equals(""))
				return;
		} else if (packetId == ENTER_INTEGER_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			int value = stream.readInt();
			if ((player.getInterfaceManager().containsInterface(762) && player.getInterfaceManager().containsInterface(763))
					|| player.getInterfaceManager().containsInterface(11)) {
				if (value < 0)
					return;
				Integer bank_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bank_item_X_Slot");
				if (bank_item_X_Slot == null)
					return;
				player.getBank().setLastX(value);
				player.getBank().refreshLastX();
				if (player.getTemporaryAttributtes().remove("bank_isWithdraw") != null)//TODO
					player.getBank().withdrawItem(bank_item_X_Slot, value);
				else
					player.getBank().depositItem(bank_item_X_Slot, value, 
							player.getInterfaceManager().containsInterface(11) ? false : true);
			} else if (player.getInterfaceManager().containsInterface(335) && player.getInterfaceManager().containsInterface(336)) {
				if (value <= 0)
					return;
				Integer add_To_Trade = (Integer) player.getTemporaryAttributtes().remove("add_To_Trade");
				if (add_To_Trade == null)
					return;
				if (value <= 0)
					return;
				//if (player.getTemporaryAttributtes().remove("add_coins_to_trade") != null) {
					/*
					 * Below contains adding coins to trade.
					 */
					//if (value > player.money) {
						//player.out("You do not have enough coins in your money pouch.");
						//return;
					//} else if (value <= player.money) {
						//player.money -= value;
						//player.getPackets().sendRunScript(5561, 0, value);
						//player.refreshMoneyPouch();
						//player.moneyInTrade = value;
						//player.getTrade().addFromPouch(value);
					//}
				//}
				Integer trade_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("trade_item_X_Slot");//TODO
				if (trade_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("trade_isRemove") != null) {
					player.getTrade().removeItem(trade_item_X_Slot, value);
					player.out("Remove Item");
				} else {
					player.getTrade().addItem(trade_item_X_Slot, value);
				}
			} else if (player.getInterfaceManager().containsInterface(206)
					&& player.getInterfaceManager().containsInterface(207)) {
				if (value < 0)
					return;
				Integer pc_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("pc_item_X_Slot");
				if (pc_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("pc_isRemove") != null)
					player.getPriceCheckManager().removeItem(pc_item_X_Slot,
							value);
				else
					player.getPriceCheckManager()
					.addItem(pc_item_X_Slot, value);
			} else if (player.getInterfaceManager().containsInterface(671)
					&& player.getInterfaceManager().containsInterface(665)) {
				if (player.getFamiliar() == null
						|| player.getFamiliar().getBob() == null)
					return;
				if (value < 0)
					return;
				Integer bob_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("bob_item_X_Slot");
				if (bob_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("bob_isRemove") != null)
					player.getFamiliar().getBob()
					.removeItem(bob_item_X_Slot, value);
				else
					player.getFamiliar().getBob()
					.addItem(bob_item_X_Slot, value);
			}

			/**
			 * Start Of Money Pouch
			 */
		else if (player.getInterfaceManager().containsInterface(548) || player.getInterfaceManager().containsInterface(746)) {
			Integer remove_X_Amount_money = (Integer) player.getTemporaryAttributtes().remove("remove_X_Amount_money");
			int amount = player.getInventory().getItems().getNumberOf(remove_X_Amount_money);
			if (remove_X_Amount_money == null)
				return;
			if (value <= 0)
				return;
			if (player.getTemporaryAttributtes().remove("remove_money") != null) {
				/*
				 * If players pouch has 0gp don't remove anything.
				 */
				if (player.money == 0) {
					player.out("You don't have any coins to withdraw.");
					return;
				}
				/*
				 * Removes money from pouch
				 */
				if (value <= player.money) {
					if (amount + value > 0) {
						player.getInventory().addItem(remove_X_Amount_money, value);
						player.getPackets().sendRunScript(5561, 0, value);
						player.money -= value;
						player.refreshMoneyPouch();
					} else {
						player.getPackets().sendGameMessage("You cannot add over 2147483647 coins to your inventory.");
					}
				} else {
		            if(amount + value > 0) {
		                player.getInventory().addItem(remove_X_Amount_money, player.money);
		                player.getPackets().sendRunScript(5561, 0, player.money);
		                player.money -= player.money;
		                player.refreshMoneyPouch();
		            } else {
		                player.getPackets().sendGameMessage("Not enough space in your Inventory.");   
		            }
				}
				
			}
		} else if (player.getTemporaryAttributtes().get("skillId") != null) {
				if (player.getEquipment().wearingArmour()) {
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "You cannot do this while having armour on!");
					return;
				}
				int skillId = (Integer) player.getTemporaryAttributtes().remove("skillId");
				if (skillId == Skills.HITPOINTS && value <= 9)
					value = 10;
				else if (value < 1)
					value = 1;
				else if (value > 99)
					value = 99;
				player.getSkills().set(skillId, value);
				player.getSkills().setXp(skillId, Skills.getXPForLevel(value));
				player.getAppearence().generateAppearenceData();
				player.getDialogueManager().finishDialogue();
			} else if (player.getTemporaryAttributtes().get("kilnX") != null) {
				int index = (Integer) player.getTemporaryAttributtes().get("scIndex");
				int componentId = (Integer) player.getTemporaryAttributtes().get("scComponentId");
				int itemId = (Integer) player.getTemporaryAttributtes().get("scItemId");
				player.getTemporaryAttributtes().remove("kilnX");
				if (StealingCreation.proccessKilnItems(player, componentId, index, itemId, value))
					return;
			}
		} else if (packetId == SWITCH_INTERFACE_ITEM_PACKET) {
			stream.readShortLE128();
			int fromInterfaceHash = stream.readIntV1();
			int toInterfaceHash = stream.readInt();
			int fromSlot = stream.readUnsignedShort();
			int toSlot = stream.readUnsignedShortLE128();
			stream.readUnsignedShortLE();


			int toInterfaceId = toInterfaceHash >> 16;
				int toComponentId = toInterfaceHash - (toInterfaceId << 16);
				int fromInterfaceId = fromInterfaceHash >> 16;
				int fromComponentId = fromInterfaceHash - (fromInterfaceId << 16);

				if (Utils.getInterfaceDefinitionsSize() <= fromInterfaceId
						|| Utils.getInterfaceDefinitionsSize() <= toInterfaceId)
					return;
				if (!player.getInterfaceManager()
						.containsInterface(fromInterfaceId)
						|| !player.getInterfaceManager().containsInterface(
								toInterfaceId))
					return;
				if (fromComponentId != -1
						&& Utils.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId)
					return;
				if (toComponentId != -1
						&& Utils.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId)
					return;
				if (fromInterfaceId == 1265 && toInterfaceId == 1266 && player.getTemporaryAttributtes().get("shop_buying") != null) {
					if ((boolean) player.getTemporaryAttributtes().get("shop_buying") == true) {
						Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
						if (shop == null)
							return;
						shop.buy(player, fromSlot, 1);
					}
				}
				if (Utils.getInterfaceDefinitionsSize() <= fromInterfaceId
						|| Utils.getInterfaceDefinitionsSize() <= toInterfaceId)
					return;
				if (!player.getInterfaceManager()
						.containsInterface(fromInterfaceId)
						|| !player.getInterfaceManager().containsInterface(
								toInterfaceId))
					return;
				if (fromComponentId != -1
						&& Utils.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId)
					return;
				if (toComponentId != -1
						&& Utils.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId)
					return;
				if (fromInterfaceId == Inventory.INVENTORY_INTERFACE
						&& fromComponentId == 0
						&& toInterfaceId == Inventory.INVENTORY_INTERFACE
						&& toComponentId == 0) {
					toSlot -= 28;
					if (toSlot < 0
							|| toSlot >= player.getInventory()
							.getItemsContainerSize()
							|| fromSlot >= player.getInventory()
							.getItemsContainerSize())
						return;
					player.getInventory().switchItem(fromSlot, toSlot);
				} else if (fromInterfaceId == 763 && fromComponentId == 0
						&& toInterfaceId == 763 && toComponentId == 0) {
					if (toSlot >= player.getInventory().getItemsContainerSize()
							|| fromSlot >= player.getInventory()
							.getItemsContainerSize())
						return;
					player.getInventory().switchItem(fromSlot, toSlot);
				} else if (fromInterfaceId == 762 && toInterfaceId == 762) {
					player.getBank().switchItem(fromSlot, toSlot, fromComponentId,
							toComponentId);
				}
				if (Settings.DEBUG)
					System.out.println("Switch item " + fromInterfaceId + ", "
							+ fromSlot + ", " + toSlot);
		} else if (packetId == DONE_LOADING_REGION_PACKET) {
			/*
			 * if(!player.clientHasLoadedMapRegion()) { //load objects and items
			 * here player.setClientHasLoadedMapRegion(); }
			 * //player.refreshSpawnedObjects(); //player.refreshSpawnedItems();
			 */
		} else if (packetId == WALKING_PACKET
				|| packetId == MINI_WALKING_PACKET
				|| packetId == ITEM_TAKE_PACKET
				|| packetId == PLAYER_OPTION_2_PACKET
				|| packetId == PLAYER_OPTION_4_PACKET
				|| packetId == PLAYER_OPTION_5_PACKET
				|| packetId == PLAYER_OPTION_6_PACKET
				|| packetId == PLAYER_OPTION_7_PACKET
				|| packetId == PLAYER_OPTION_1_PACKET || packetId == ATTACK_NPC
				|| packetId == INTERFACE_ON_PLAYER
				|| packetId == INTERFACE_ON_NPC
				|| packetId == NPC_CLICK1_PACKET
				|| packetId == NPC_CLICK2_PACKET
				|| packetId == NPC_CLICK3_PACKET
				|| packetId == OBJECT_CLICK1_PACKET
				|| packetId == SWITCH_INTERFACE_ITEM_PACKET
				|| packetId == OBJECT_CLICK2_PACKET
				|| packetId == OBJECT_CLICK3_PACKET
				|| packetId == OBJECT_CLICK4_PACKET
				|| packetId == OBJECT_CLICK5_PACKET
				|| packetId == INTERFACE_ON_OBJECT)
			player.addLogicPacketToQueue(new LogicPacket(packetId, length,
					stream));
		else if (packetId == OBJECT_EXAMINE_PACKET) {
			ObjectHandler.handleOption(player, stream, -1);
		}else if (packetId == NPC_EXAMINE_PACKET) {
			NPCHandler.handleExamine(player, stream);
		} else if (packetId == JOIN_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			FriendChatsManager.joinChat(stream.readString(), player);
		} else if (packetId == KICK_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 1000); // avoids
			// message
			// appearing
			player.kickPlayerFromFriendsChannel(stream.readString());
		} else if (packetId == CHANGE_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted()
					|| !player.getInterfaceManager().containsInterface(1108))
				return;
			player.getFriendsIgnores().changeRank(stream.readString(),
					stream.readUnsignedByte128());
		} else if (packetId == ADD_FRIEND_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().addFriend(stream.readString());
		} else if (packetId == REMOVE_FRIEND_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().removeFriend(stream.readString());
		} else if (packetId == ADD_IGNORE_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().addIgnore(stream.readString(), stream.readUnsignedByte() == 1);
		} else if (packetId == REMOVE_IGNORE_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().removeIgnore(stream.readString());
		} else if (packetId == SEND_FRIEND_MESSAGE_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You temporary muted. Recheck in 48 hours.");
				return;
			}
			String username = stream.readString();
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 == null)
				return;

			player.getFriendsIgnores().sendMessage(
					p2,
					Utils.fixChatMessage(Huffman.readEncryptedMessage(150,
							stream)));
		} else if (packetId == SEND_FRIEND_QUICK_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			String username = stream.readString();
			int fileId = stream.readUnsignedShort();
			if (fileId > 1163) 
			return;
			byte[] data = null;
			if (length > 3 + username.length()) {
				data = new byte[length - (3 + username.length())];
				stream.readBytes(data);
			}
			data = Utils.completeQuickMessage(player, fileId, data);
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 == null)
				return;
			player.getFriendsIgnores().sendQuickChatMessage(p2,
					new QuickChatMessage(fileId, data));
		} else if (packetId == PUBLIC_QUICK_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getLastPublicMessage() > Utils.currentTimeMillis())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			// just tells you which client script created packet
			@SuppressWarnings("unused")
			boolean secondClientScript = stream.readByte() == 1;// script 5059
			// or 5061
			int fileId = stream.readUnsignedShort();
			if (fileId > 1163)
			return;
			byte[] data = null;
			if (length > 3) {
				data = new byte[length - 3];
				stream.readBytes(data);
			}
			data = Utils.completeQuickMessage(player, fileId, data);
			if (chatType == 0)
				player.sendPublicChatMessage(new QuickChatMessage(fileId, data));
			else if (chatType == 1)
				player.sendFriendsChannelQuickMessage(new QuickChatMessage(
						fileId, data));
			else if (Settings.DEBUG)
				Logger.log(this, "Unknown chat type: " + chatType);
		} else if (packetId == CHAT_TYPE_PACKET) {
			chatType = stream.readUnsignedByte();
		} else if (packetId == CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getLastPublicMessage() > Utils.currentTimeMillis())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			int colorEffect = stream.readUnsignedByte();
			int moveEffect = stream.readUnsignedByte();
			String message = Huffman.readEncryptedMessage(200, stream);
			/**
			 * Censors website's links/words.
			 */
			 if (player.getRights() < 2) {
				   String[] Q = { ".com", ".net", ".co.uk", ".tk", "scape", ".c0m", ".n3t", ".webs", ".co.cc", "www.", ".org", ".us", ".weebly", ".edu", ".uk", ".fr", ".ru", ".gov", ".us.to", ".zzl.org" };
				   for (String string : Q)
				    if (message.contains(string)) {
				     player.getPackets().sendGameMessage("Sorry, No advertising.");
				     return;
				    }
				   }
				  //Known DKK chat Exploits - "0hdr2ufufl9ljlzlyla", "0hdr7lo", "0hdr3lo", "0hdr4lo", "0hdr2ufuflal9l2l2uol=lulglgldlzlyl(lul%lcuolyl7lsl(l%l?uolul8l9lguolzl7lglylal(l" };
				    if (message.contains("0hdr") || (message.contains("ohdr") || message.contains("0hdr2ufuflal9l2l2uol=lulglgldlzlyl(lul%lcuolyl7lsl(l%l?uolul8l9lguolzl7lglylal(l"))) {
				     player.getPackets().sendGameMessage("Sorry You Cannot Send This Message.");
				     return;
				   }
				    if (message.length() > 80) {
				    	player.out("The message you entered is too long.");
				    	return;
				    }
			Player.chatLog(player, message);
			if (message == null || message.replaceAll(" ", "").equals(""))
				return;
			if (message.startsWith("::") || message.startsWith(";;")) {
				Commands.processCommand(player, message.replace("::", "")
						.replace(";;", ""), false, false);
				//Player.chatLog(player, message);
				return;
			}
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You temporary muted. Recheck in 48 hours.");
				return;
			}
			int effects = (colorEffect << 8) | (moveEffect & 0xff);
			if (chatType == 1)
				player.sendFriendsChannelMessage(Utils.fixChatMessage(message));
			else
				player.sendPublicChatMessage(new PublicChatMessage(Utils.fixChatMessage(message), effects));
			player.setLastMsg(message);
			if (Settings.DEBUG)
				Logger.log(this, "Chat type: " + chatType);
		} else if (packetId == COMMANDS_PACKET) {
			if (!player.isRunning())
				return;
			boolean clientCommand = stream.readUnsignedByte() == 1;
			@SuppressWarnings("unused")
			boolean unknown = stream.readUnsignedByte() == 1;
			String command = stream.readString();
			if (!Commands.processCommand(player, command, true, clientCommand)
					&& Settings.DEBUG)
				Logger.log(this, "Command: " + command);
		} else if (packetId == COLOR_ID_PACKET) {
			if (!player.hasStarted())
				return;
			int colorId = stream.readUnsignedShort();
			if (player.getTemporaryAttributtes().get("SkillcapeCustomize") != null)
				SkillCapeCustomizer.handleSkillCapeCustomizerColor(player,
						colorId);
		}else if (packetId == REPORT_ABUSE_PACKET) {
			if (!player.hasStarted())
				return;
			@SuppressWarnings("unused")
			String username = stream.readString();
			@SuppressWarnings("unused")
			int type = stream.readUnsignedByte();
			@SuppressWarnings("unused")
			boolean mute = stream.readUnsignedByte() == 1;
			@SuppressWarnings("unused")
			String unknown2 = stream.readString();
		} else {
			//if (Settings.DEBUG)
			//	Logger.log(this, "Missing packet " + packetId
			//			+ ", expected size: " + length + ", actual size: "
			//			+ PACKET_SIZES[packetId]);
		}
	}

}
