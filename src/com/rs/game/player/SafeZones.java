package com.rs.game.player;

import com.rs.game.player.Player;

public class SafeZone {
	
	public static boolean playerTiles(Player player) {
		if (edgeBank(player) 
				|| grandExchange(player) 
				|| varrockBank(player)
				|| varrockEastBank(player)
				|| camelot(player)
				|| camelot2(player)
				|| lumbridge(player)
				|| falador(player)
				|| faladorEast(player)
				|| ardy(player)
				|| ardy2(player)) {
				player.setCanPvp(false);
				safeSkull(player);
		} else {
				player.setCanPvp(true);
				dangerSkull(player);
			
		}
		return true;
	}
	
	public static boolean safeSkull(Player player) {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 34 : 0, 373);
		player.getPackets().sendIComponentText(373, 10, "<col=00FF00>You are currently in a safe spot!");
		return true;
		}
	
	public static boolean dangerSkull(Player player) {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 11: 0,	381);
		return true;
	}
	
	//edge
		 public static boolean edgeBank(Player player) { 
			return (player.getX() > 3090 && player.getY() < 3500
				&& player.getX() < 3099 && player.getY() > 3487);
			 	}
		 
		//grand exchange 
		 public static boolean grandExchange(Player player) { 
			 return (player.getX() > 3143 && player.getY() < 3513
				&& player.getX() < 3186 && player.getY() > 3471);
		}

	//varrock west bank
		 public static boolean varrockBank(Player player) { 
			 return (player.getX() > 3178 && player.getY() < 3447
				&& player.getX() < 3195 && player.getY() > 3431);
		 }
		 
	//varrock east bank
		 public static boolean varrockEastBank(Player player) { 
			 return (player.getX() > 3249 && player.getY() < 3426
				&& player.getX() < 3259 && player.getY() > 3413);
		}
		 
	//camelot
		 public static boolean camelot(Player player) { 
			 return (player.getX() > 2718 && player.getY() < 3498
				&& player.getX() < 2731 && player.getY() > 3486);
		}
		 
	//cammy2
		 public static boolean camelot2(Player player) { 
			 return (player.getX() > 2805 && player.getY() < 3446
				&& player.getX() < 2813 && player.getY() > 3437);
		}

	//lummy
		 public static boolean lumbridge(Player player) { 
			 return (player.getX() > 3201 && player.getY() < 3237
				&& player.getX() < 3229 && player.getY() > 3200);
		}
		 
	//fally
		 public static boolean falador(Player player) { 
			 return (player.getX() > 2942 && player.getY() < 3374
				&& player.getX() < 2950 && player.getY() > 3367);
		}

	//fally2	 
		 public static boolean faladorEast(Player player) { 
			 return (player.getX() > 3008 && player.getY() < 3359
				&& player.getX() < 3020 && player.getY() > 3352);
		}
		
	//ardy
			 public static boolean ardy(Player player) { 
				 return (player.getX() > 2611 && player.getY() < 3336
				&& player.getX() < 2622 && player.getY() > 3329);
		}
		 
	//ardy2
		 public static boolean ardy2(Player player) { 
			 return (player.getX() > 2648 && player.getY() < 3288
				&& player.getX() < 2659 && player.getY() > 3279);
		 }
}