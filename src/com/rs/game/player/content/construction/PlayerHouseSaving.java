package com.rs.game.player.content.construction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class PlayerHouseSaving {

	static String playerName;
	private static Player player;
	@SuppressWarnings("unused")
	private static PlayerHouseSaving instance = new PlayerHouseSaving();

	public PlayerHouseSaving() {

	}
	private static final String CON_PATH = "data/construction/" + playerName + ".txt";

	public static void init(Player p) {
		player = p;
		playerName = player.getUsername();
		//player.setPlayerHouseSaving(instance);
		File file = new File("data/construction/" + player.getUsername() + ".txt");
		boolean doesSaveExist = file.exists();
		if (doesSaveExist) {
			 loadPlayerHouse(p);
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadPlayerHouse(Player player) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(CON_PATH));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
		//	System.out.println(player.getConObjectsToBeLoaded().isEmpty());
			//for (WorldObject o : player.getConObjectsToBeLoaded()) {
			//	World.spawnObject(o, true);
			//	Logger.log(this, "Adding " + o + " to house");
		//	}
		} catch (NullPointerException e) {
			//Logger.log(getClass(), "Null player objects");
			e.printStackTrace();
		}
	}

	public static void writePlayerHouse(int objectId, int objectX, int objectY,
			Player owner) {

	}

}
