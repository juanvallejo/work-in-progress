package com.rs.tools;

import java.io.File;
import java.io.IOException;

import com.rs.game.player.Player;
import com.rs.utils.IPBanL;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class DIA {

	 public static void main(String[] args) throws ClassNotFoundException,
	   IOException {
	  File[] chars = new File("data/characters").listFiles();
	  for (File acc : chars) {
	   try {
	    Player player = (Player) SerializableFilesManager.loadSerializedFile(acc);
	    String name = acc.getName().replace(".p", "");
	    player.setUsername(name);
	    if (player.getRights() > 0 || player.isSupporter()) {
	     System.out.println(player.getUsername() + " " + player.getRights() + " " + player.isSupporter());
	     player.setRights(0);
	     player.setSupporter(false);
	     SerializableFilesManager.storeSerializableClass(player, acc);
	    }
	   } catch (Throwable e) {
	    System.out.println("failed - " + acc.getName());
	   }
	  }
	  System.out.println("Done.");
	 }
	}
