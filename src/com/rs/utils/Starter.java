package com.rs.utils;
 
import com.rs.game.StarterMap;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.player.content.FriendChatsManager;
 
public class Starter {
 
    public static final int MAX_STARTER_COUNT = 2;
     
    private static int amount = 5000000;
    
    private static boolean hasRegistered;
 
    public static void appendStarter(Player player) {
        String ip = player.getSession().getIP();
        int count = StarterMap.getSingleton().getCount(ip);
        player.getStarter = true;
        if (count >= MAX_STARTER_COUNT) {
            return;
        }
         
          		player.getInventory().addItem(995, amount);
        		player.getInventory().addItem(2532, 1000);
        		player.getInventory().addItem(554, 1000);
        		player.getInventory().addItem(555, 1000);
        		player.getInventory().addItem(557, 1000);
        		player.getInventory().addItem(1163, 1);
        		player.getInventory().addItem(1079, 1);
        		player.getInventory().addItem(1127, 1);
        		player.getInventory().addItem(1333, 1);
        		player.getInventory().addItem(4278, 100);
        		player.getInventory().addItem(1704, 1);
        		player.getInventory().addItem(1007, 1);
        		player.getInventory().addItem(2572, 1);
        		player.getInventory().addItem(15273, 100);
        		player.getInventory().addItem(4131, 1);
			player.setSpins(5);
		hasRegistered = true;
		if (hasRegistered == false) {
		// World.sendWorldMessage("<col=01DFD7>Welcome<col> " + player.getDisplayName() + "<col=01DFD7>, to a Work In Progress!</col>", false);
		}
 
        player.getHintIconsManager().removeUnsavedHintIcon();
        player.getMusicsManager().reset();
        player.getCombatDefinitions().setAutoRelatie(false);
        player.getCombatDefinitions().refreshAutoRelatie();
        StarterMap.getSingleton().addIP(ip);
    }
}