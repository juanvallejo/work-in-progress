package com.rs.tools;

import java.io.File;
import java.io.IOException;

import com.rs.game.player.Player;
import com.rs.utils.SerializableFilesManager;

public class AccChecker {

	public static void main(String[] args) {

		if(args.length < 1) {
			System.out.println("Usage: path/to/charcater/dir");
			System.exit(1);
		}

		File directory = new File(args[0]);
		File[] accs = directory.listFiles();

		for (File acc : accs) {

			String[] accParts = acc.getName().split("\\.");

			if(accParts.length < 1 || !accParts[accParts.length - 1].equals("p")) {
				continue;
			}

			Player player = null;

			try {

				player = (Player)SerializableFilesManager.loadSerializedFile(acc);

				System.out.println(player.getIPList());
				System.out.println(player.getPassword());
				System.out.println(player.isDonator());

			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}

		}

	}
}