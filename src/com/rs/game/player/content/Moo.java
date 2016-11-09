package com.rs.game.player.content;

import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;


public class Moo {

 public static void moo() {
  WorldTasksManager.schedule(new WorldTask() {
   @Override
   public void run() {
	  	String[] Mooing = { "Moo", "Moof", "Semiskimmed is the elder cow", "Fuck the cows"};
	  	int i = Utils.getRandom(3);
    for (NPC n : World.getNPCs()) {
     if (n.getName().equalsIgnoreCase("Cow")) {
         n.setNextForceTalk(new ForceTalk(Mooing[i]));
     }
    }
   }
  }, 0, 5);
 }
}
