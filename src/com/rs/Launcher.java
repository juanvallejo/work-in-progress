package com.rs;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.alex.store.Index;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemsEquipIds;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Region;
import com.rs.game.RegionBuilder;
import com.rs.game.StarterMap;
import com.rs.game.World;
import com.rs.game.npc.combat.CombatScriptsHandler;
import com.rs.game.player.Player;
import com.rs.game.player.content.FishingSpotsHandler;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.Moo;
import com.rs.game.player.controlers.ControlerHandler;
import com.rs.game.player.cutscenes.CutscenesHandler;
import com.rs.game.player.dialogues.DialogueHandler;
import com.rs.net.ServerChannelHandler;
import com.rs.utils.DTRank;
import com.rs.utils.DisplayNames;
import com.rs.utils.IPBanL;
import com.rs.utils.ItemBonuses;
import com.rs.utils.ItemExamines;
import com.rs.utils.Logger;
import com.rs.utils.MapArchiveKeys;
import com.rs.utils.MapAreas;
import com.rs.utils.MusicHints;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;
import com.rs.utils.NPCDrops;
import com.rs.utils.NPCSpawns;
import com.rs.utils.ObjectSpawns;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Text;
import com.rs.utils.Utils;
import com.rs.utils.huffman.Huffman;
import com.rs.utils.NPCSpawning;

// test
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;

public final class Launcher {
	
	/**
	 * Massive Thanks To The Matrix Team!
	 */

	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			System.out.println("USE: guimode(boolean) debug(boolean) hosted(boolean)");
		    return;
		}
		Settings.HOSTED = Boolean.parseBoolean(args[2]);
		Settings.DEBUG = Boolean.parseBoolean(args[1]);
		long currentTime = Utils.currentTimeMillis();
		if (Settings.HOSTED) {
			// System.setErr(new PrintStream(new
			// FileOutputStream("data/auto/err.txt")));
			// System.setOut(new PrintStream(new
			// FileOutputStream("data/auto/out.txt")));
		}

		if(Boolean.parseBoolean(args[1])) {
			Logger.log("DEBUG", "Debug mode on");
		}

		Logger.log("Launcher", "Initiating Cache...");
		Cache.init();
		ItemsEquipIds.init();
		Huffman.init();
		Logger.log("Launcher", "Initiating Data Files...");
		DisplayNames.init();
		IPBanL.init();
		PkRank.init();
		DTRank.init();
		MapArchiveKeys.init();
		MapAreas.init();
		ObjectSpawns.init();
		NPCSpawns.init();
		NPCCombatDefinitionsL.init();
		NPCBonuses.init();
		NPCDrops.init();
		ItemExamines.init();
		ItemBonuses.init();
		MusicHints.init();
		ShopsHandler.init();
		//StarterMap.init();
		Text.init();
		//Moo.moo();
		Logger.log("Launcher", "Initiating Fishing Spots...");
		FishingSpotsHandler.init();
		Logger.log("Launcher", "Initiating NPC Combat Scripts...");
		CombatScriptsHandler.init();
		Logger.log("Launcher", "Initiating Dialogues...");
		DialogueHandler.init();
		Logger.log("Launcher", "Initiating Controlers...");
		ControlerHandler.init();
		Logger.log("Launcher", "Initiating Cutscenes...");
		CutscenesHandler.init();
		Logger.log("Launcher", "Initiating Friend Chats Manager...");
		FriendChatsManager.init();
		Logger.log("Launcher", "Initiating Cores Manager...");
		CoresManager.init();
		Logger.log("Launcher", "Initiating World...");
		World.init();
		Logger.log("Launcher", "Initiating Region Builder...");
		RegionBuilder.init();
		Logger.log("Launcher", "Initiating Server Channel Handler...");

		try {
			ServerChannelHandler.init();
			NPCSpawning.spawnNPCS();
		} catch (Throwable e) {
			Logger.handle(e);
			Logger.log("Launcher",
					"Failed Initiating Server Channel Handler. Shutting down...");
			System.exit(1);
			return;
		}
		Logger.log("Launcher", "The server took "
				+ (Utils.currentTimeMillis() - currentTime)
				+ " miliseconds to launch.");
		addAccountsSavingTask();
		if (Settings.HOSTED)
			addUpdatePlayersOnlineTask();
		addCleanMemoryTask();

		// was already commented
		// Donations.init();
	}

	
	/*private static void setWebsitePlayersOnline(int amount) throws IOException {
		URL url = new URL("http://www.tzhaar.co.uk/online.php?players="+ amount + "&auth=JFHDJF3847234");
		url.openStream().close();
	}*/

	private static void addUpdatePlayersOnlineTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					//setWebsitePlayersOnline(World.getPlayers().size());
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 2, 2, TimeUnit.MINUTES);
	}

	private static void addCleanMemoryTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					cleanMemory(Runtime.getRuntime().freeMemory() < Settings.MIN_FREE_MEM_ALLOWED);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 10, TimeUnit.MINUTES);
	}

	private static void addAccountsSavingTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					saveFiles();
				} catch (Throwable e) {
					Logger.handle(e);
				}

			}
		}, 15, 15, TimeUnit.SECONDS);
	}

	public static void saveFiles() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished())
				continue;
			SerializableFilesManager.savePlayer(player);
		}
		DisplayNames.save();
		IPBanL.save();
		PkRank.save();
		DTRank.save();
	}

	public static void cleanMemory(boolean force) {
		if (force) {
			ItemDefinitions.clearItemsDefinitions();
			NPCDefinitions.clearNPCDefinitions();
			ObjectDefinitions.clearObjectDefinitions();
			for (Region region : World.getRegions().values())
				region.removeMapFromMemory();
		}
		for (Index index : Cache.STORE.getIndexes())
			index.resetCachedFiles();
		CoresManager.fastExecutor.purge();
		System.gc();
	}

	public static void shutdown() {
		try {
			closeServices();
		} finally {
			System.exit(0);
		}
	}

	public static void closeServices() {
		ServerChannelHandler.shutdown();
		CoresManager.shutdown();
		if (Settings.HOSTED) {
			try {
				//setWebsitePlayersOnline(0);
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
	}

	public static void restart() {
		closeServices();
		System.gc();
		try {
			Runtime.getRuntime().exec("java -Xmx1024m -classpath \"bin:bin/com/mysql/jdbc/*:lib/*:data/libs/*\" com.rs.Launcher true false false &");
			System.exit(0);
		} catch (Throwable e) {
			Logger.handle(e);
		}

	}

	private Launcher() {

	}

}
