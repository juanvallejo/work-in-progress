package com.rs.game.player.dialogues;

import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelRules;
 
 
public class Class1 extends Dialogue {
 
public int ID1 = 20139;
public int Amount1 = 1;
public int ID2 = 11694;
public int Amount2 = 1;
 
    @Override
    public void start() {
            Options2("Choose your starter class", "Main", "Pure");
            player.getPackets().sendItemOnIComponent(1185, 4, ID1, Amount1);
            player.getPackets().sendItemOnIComponent(1185, 5, ID2, Amount2);
            stage = 2;
    	}
     
 
    @Override
    public void run(int interfaceId, int componentId) {
          if (stage == 2) {
              switch (componentId) {
              case OPTION1:
          		player.getInventory().addItem(995, 10000000);
        		player.getInventory().addItem(1333, 1);
        		player.getInventory().addItem(4587, 1);
        		player.getInventory().addItem(1323, 1);
        		player.getInventory().addItem(1153, 1);
        		player.getInventory().addItem(1115, 1);
        		player.getInventory().addItem(1067, 1);
        		player.getInventory().addItem(841, 1);
        		player.getInventory().addItem(884, 10000);
                end();
                break;
              case OPTION2:
            		player.getInventory().addItem(995, 10000000);
            		player.getInventory().addItem(1333, 1);
            		player.getInventory().addItem(4587, 1);
            		player.getInventory().addItem(1323, 1);
            		player.getInventory().addItem(1153, 1);
            		player.getInventory().addItem(1115, 1);
            		player.getInventory().addItem(1067, 1);
            		player.getInventory().addItem(841, 1);
            		player.getInventory().addItem(884, 10000);
                break;
              }
              end();
          }
 
    }
 
    @Override
    public void finish() {
 
    }
}