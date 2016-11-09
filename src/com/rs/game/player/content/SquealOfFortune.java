package com.rs.game.player.content;

 import java.text.DecimalFormat;

 import com.rs.game.item.Item;
 import com.rs.game.item.ItemsContainer;
 import com.rs.game.player.Player;
 import com.rs.game.player.Inventory;
import com.rs.utils.Utils;

 public class SquealOfFortune {

	public static ItemsContainer<Item> items = Player.items;
	public Player player;

	public SquealOfFortune(Player player) {
	this.player = player;
	}

	private static Item[] SUPER_RARE_ITEM = { new Item(995, 200000000), new Item(995, 10000000), new Item(23724, 1), new Item(23671, 1),
	new Item(23674) 
	};
 
	private static Item[] RARE_SLOT1 = { new Item(23715), new Item(23679), new Item(23680),
	new Item(23681), new Item(23682), new Item(23683), new Item(23690) 
	},
	RARE_SLOT2 = { new Item(23684), new Item(23685), new Item(23686),
	new Item(23687), new Item(23688), new Item(23689) 
	},
	RARE_SLOT3 = { new Item(23691), new Item(23692), new Item(23693),
	new Item(23694), new Item(23695), new Item(23696),
	new Item(23697), new Item(23698), new Item(23699),
	new Item(23700) };
 
	private static Item[] UNCOMMON_SLOT1 = { new Item(9181), new Item(15441), new Item(9183), new Item(4151), new Item(892, 1000), new Item(868, 2000),
	new Item(857) }, UNCOMMON_SLOT2 = { new Item(1119), new Item(1121),
	new Item(1123), new Item(1127) },
	UNCOMMON_SLOT3 = { new Item(12976), new Item(1133), new Item(23750) 
	};
 
	private static Item[] COMMON_SLOT1 = { new Item(23757), new Item(9177), new Item(9179),
	new Item(9174) }, COMMON_SLOT2 = { new Item(2, 30),
	new Item(886, 300), new Item(15704) }, COMMON_SLOT3 = { new Item(1515, 15),
	new Item(444, 20) }, COMMON_SLOT4 = { new Item(563, 40),
	new Item(843), new Item(1325), new Item(1313), new Item(1361) },
	COMMON_SLOT5 = { new Item(1367), new Item(1297), new Item(1327),
	new Item(1205) }, COMMON_SLOT6 = { new Item(853),
	new Item(1311), new Item(1365), new Item(1353),
	new Item(1295), new Item(1325) };

	public static void openSOF(Player player) {
		if(player.spins <= 0) {
		player.getPackets().sendGameMessage("You do not have enough spins to open Squeal of Fortune");
		}
	else
		if(player.spins >= 0) {
		player.isspining = 0;
		player.getPackets().sendConfigByFile(11026, player.spins + 1);
		player.getPackets().sendRunScript(5879);
		player.getPackets().sendConfigByFile(11155, 3);
		player.getPackets().sendWindowsPane(1253, 0);
		sendItems(player);
		}
	}
	public static void closeSOF(Player player) {
		player.getPackets().sendWindowsPane(
            		player.getInterfaceManager().hasRezizableScreen() ? 746
            		: 548, -1);
            		player.closeInterfaces();
	}

	public static Item getSRare() {
		return SUPER_RARE_ITEM[(int) (Math.random() * SUPER_RARE_ITEM.length)];
	}

	public static Item getRare(int slot) {
		if (slot == 2) {
		return RARE_SLOT2[(int) (Math.random() * RARE_SLOT2.length)];
	} else if (slot == 3) {
		return RARE_SLOT3[(int) (Math.random() * RARE_SLOT3.length)];
	} else {
		System.out.println(""
				+ RARE_SLOT1[(int) (Math.random() * RARE_SLOT1.length)]);
		return RARE_SLOT1[(int) (Math.random() * RARE_SLOT1.length)];
		}
	}

	public static Item getCommon(int slot) {
		if (slot == 2) {
		return COMMON_SLOT1[(int) (Math.random() * COMMON_SLOT1.length)];
	} else if (slot == 3) {
		return COMMON_SLOT2[(int) (Math.random() * COMMON_SLOT2.length)];
	} else if (slot == 4) {
		return COMMON_SLOT3[(int) (Math.random() * COMMON_SLOT3.length)];
	} else if (slot == 5) {
		return COMMON_SLOT4[(int) (Math.random() * COMMON_SLOT4.length)];
	} else if (slot == 6) {
		return COMMON_SLOT5[(int) (Math.random() * COMMON_SLOT5.length)];
	} else {
		return COMMON_SLOT6[(int) (Math.random() * COMMON_SLOT6.length)];
		}
	}

	public static Item getUnCommon(int slot) {
		if (slot == 2) {
		return UNCOMMON_SLOT1[(int) (Math.random() * UNCOMMON_SLOT1.length)];
	} else if (slot == 3) {
		return UNCOMMON_SLOT2[(int) (Math.random() * UNCOMMON_SLOT2.length)];
	} else {
		return UNCOMMON_SLOT3[(int) (Math.random() * UNCOMMON_SLOT3.length)];
		}
	}

	public static void sendItems(Player player) {
		items.clear();
		items.add(getRare(1)); // Rare
		items.add(getCommon(1)); // Common
		items.add(getSRare()); // Super-Rare
		items.add(getCommon(2)); // Common
		items.add(getRare(2)); // Rare
		items.add(getCommon(3)); // Common
		items.add(getUnCommon(1)); // UnCommon
		items.add(getCommon(4)); // Common
		items.add(getRare(3)); // Rare
		items.add(getUnCommon(2)); // UnCommon
		items.add(getCommon(5)); // Com
		items.add(getUnCommon(3)); // UnCom
		items.add(getCommon(6)); // Common
		player.getPackets().sendItems(665, false, items);
	}

	public static String getFormattedNumber(int amount) {
		return new DecimalFormat("#,###,##0").format(amount).toString ();
	}

	public static void claimItem(Player player) {
		int amount = player.getBox().getAmount();
		int id = player.getBox().getId();
		player.getBank().addItem(id, amount, true);
	if(amount == 1)
		player.getPackets().sendGameMessage("Your " + player.getBox().getName() + " has been added to your bank.");
	else if(amount > 1)
		player.getPackets().sendGameMessage("Your " + player.getBox().getName() + "'s has been added to your bank.");
		player.getPackets().sendConfigByFile(11026, player.spins + 1);
		player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? 746 : 548, -1);
		player.Rewards = 0;
		player.isspining = 0;
	}

	public static int calculatePercent(double a, double b, int trials) {
		double syntax = 100 * a / b;
		double ratio = syntax / trials;
		Math.floor(ratio);
		return (int) ratio;
	}

	public static int getRandomCommon() {
		int ran = Utils.random(6);
		if(ran == 1) {
			return 0;
		} else if(ran == 2) {
			return 3;
		} else if(ran == 3) {
			return 5;
		} else if(ran == 4) {
			return 7;
		} else if(ran == 5) {
			return 9;
		} else if(ran == 6) {
			return 11;
		} else {
			return 1;
		}
	}
	
	public static int getRandomUncommon() {
		int ran = Utils.random(3);
		if(ran == 1) {
			return 0;
		} else if(ran == 2) {
			return 2;
		} else {
			return 10;
		}
	}
	
	public static int getRandomRare() {
		int ran = Utils.random(3);
		if(ran == 1) {
			return 4;
		} else if(ran == 2) {
			return 8;
		} else {
			return 12;
		}
	}
	
	private static int getSpinChances() {
		int common = Utils.random(10, 12);
		int uncommon = Utils.random(10, 20);
		int rare = Utils.random(0, 40);
		int superRare = Utils.random(0, 3333);// it would take 2000 spins ~ to get a super rare in RS
		if(common == 10 || common == 11) {
			return getRandomCommon();
		} else if(common != 10 || common != 11 && uncommon == 11 || uncommon == 14) {
			return getRandomUncommon();
		} else if(common != 10 || common != 11 && uncommon != 11 
				|| uncommon != 14 && rare == 12% 100 || rare == (10)) {
			return getRandomRare();
		} else if(common != 10 || common != 11 && uncommon != 11 
				|| uncommon != 14 && rare != 12% 100 
				|| rare != (10) && superRare == Utils.calculatePercent(1, .333,  2)) {//2 sof spins p/day
			return 6;//super rare
		} else {
			return getRandomCommon();
		}
			
	}
	
	public static void spin(Player player) {
		int reward = Utils.random(1, 1000);
		int slot = 0;
		int slotchange = Utils.getRandom(6);
	if (reward >= 1 && reward <= 920) {
			if (slotchange == 1) {
				slot = 1;
			} else if (slotchange == 2) {
				slot = 3;
			} else if (slotchange == 3) {
				slot = 5;
			} else if (slotchange == 4) {
				slot = 7;
			} else if (slotchange == 5) {
				slot = 10;
			} else if (slotchange == 6) {
				slot = 12;
			}
		} else if (reward >= 921 && reward <= 987) {
			if (slotchange >= 5) {
				slot = 6;
			} else if (slotchange <= 2) {
				slot = 9;
			} else if (slotchange >= 3 && slotchange < 5) {
				slot = 11;
			}
		} else if (reward >= 988 && reward <= 997) {
			if (slotchange >= 5) {
				slot = 0;
			} else if (slotchange <= 2) {
				slot = 4;
			} else if (slotchange >= 3 && slotchange < 5) {
				slot = 8;
			}
		} else if (reward >= 998) {
			slot = 2;
		}
	if (player.isspining == 0) {
		player.spins -= 1;//we gotta fix one more thin
		player.isspining += 1;
		player.getPackets().sendConfigByFile(11026, player.spins + 1);
		player.getPackets().sendConfigByFile(10860, slot);
		player.getPackets().sendGlobalConfig(1781, 0);
		player.getPackets().sendGlobalConfig(1781, 0);
		player.Rewards = slot;
		}
	}
 }