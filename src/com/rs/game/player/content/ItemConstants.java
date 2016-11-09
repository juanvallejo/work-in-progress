
package com.rs.game.player.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.QuestManager.Quests;

public class ItemConstants {

	public static int getDegradeItemWhenWear(int id) {
		// pvp armors
		if (id == 13958)
			return id + 2; // if you wear it it becomes corrupted LOL
		return -1;
	}

	// return amt of charges
	public static int getItemDefaultCharges(int id) {
		// pvp armors
		if (id == 13910)
			return 1500;
		if (id == 13960)
			return 3000;
		if (id == 13860)
			return 6000; // 1hour
		// nex armors
		if (id == 20137)
			return 60000;
		return -1;
	}

	// return what id it degrades to, -1 for disapear which is default so we
	// dont add -1
	public static int getItemDegrade(int id) {
		if (id == 11285) // DFS
			return 11283;
		// nex armors
		if (id == 20137)
			return id + 1;
		return -1;
	}

	public static int getDegradeItemWhenCombating(int id) {
		// nex armors
		//if (id == 20135)
			//return id + 2;
		return -1;
	}

	public static boolean itemDegradesWhileHit(int id) {
		if (id == 2550)
			return true;
		return false;
	}

	public static boolean itemDegradesWhileWearing(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName()
				.toLowerCase();
		if (name.contains("c. dragon"))
			return true;
		return false;
	}

	public static boolean itemDegradesWhileCombating(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName()
				.toLowerCase();
		// nex armors
		if (name.contains("zaryte"))
			return true;
		return false;
	}

	public static boolean canWear(Item item, Player player) {
		if(player.getRights() == 2)
			return true;
		if((item.getId() == 20769 || item.getId() == 20770)) {
			if (item.getId() == 20771 && player.getDominionTower().getKilledBossesCount() < 100) {
				player.getPackets().sendGameMessage("You need to have kill atleast 100 bosses in the Dominion tower to use this cape.");
				return false;
			}
			if(!player.isCompletedFightKiln()) {
				player.getPackets().sendGameMessage("You need to complete at least once fight kiln minigame to use this cape.");
				return false;
			}
			/*if(!player.isWonFightPits()) {
				player.getPackets().sendGameMessage("You need to win at least once fight pits minigame to use this cape.");
				return false;
			}*/
			if (!player.isKilledQueenBlackDragon()) {
				player.getPackets().sendGameMessage("You need to have killed the Queen Black Dragon atleast once to use this cape.");
				return false;
			}
			//if(!player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM)) {
			//	player.getPackets().sendGameMessage("You need to have completed Nomad's Requiem miniquest to use this cape.");
			//	return false;
			//}
		}else if (item.getId() == 6570
				|| item.getId() == 10566
				|| item.getId() == 10637) { //temporary
			if(!player.isCompletedFightCaves()) {
				player.getPackets().sendGameMessage("You need to complete at least once fight cave minigame to use this cape.");
				return false;
			}
		}else if (item.getId() == 23659) {
			if(!player.isCompletedFightKiln()) {
				player.getPackets().sendGameMessage("You need to complete at least once fight kiln minigame to use this cape.");
				return false;
			}
		//}else if (item.getId() == 20771) {
		//	if(!player.isCompletedComp() && !(player.prestigeTokens == 5)) {
		//		player.getPackets().sendGameMessage("You need to have completed the completionist cape requirements and have prestiged 5 times.");
		//		return false;
		//	}
		}else if (item.getId() == 20771) {
			if(!(player.prestigeNumber == 5) && !player.isCompletedFightKiln()) {
				player.getPackets().sendGameMessage("You need to have completed the completionist cape requirements and have prestiged 5 times.");
				return false;
			}
		}else if (item.getId() == 14642
				|| item.getId() == 14645
				|| item.getId() == 15433
				|| item.getId() == 15435
				|| item.getId() == 14641
				|| item.getId() == 15432
				|| item.getId() == 15434) {
			if(!player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM)) {
				player.getPackets().sendGameMessage("You need to have completed Nomad's Requiem miniquest to use this cape.");
				return false;
			}
		}
		String itemName = item.getName();
		if (itemName.contains("goliath gloves") || itemName.contains("spellcaster glove") || itemName.contains("swift glove")) {
			if (player.getDominionTower().getKilledBossesCount() < 50) {
				player.getPackets().sendGameMessage("You need to have kill atleast 50 bosses in the Dominion tower to wear these gloves.");
				return true;
			}
		}
		return true;
	}
	public static boolean isTradeable(Item item) {
		if (//item.getDefinitions().isDestroyItem()
				 item.getDefinitions().isLended()
				|| ItemConstants.getItemDefaultCharges(item.getId()) != -1)
			return false;
		if (item.getDefinitions().getName().toLowerCase().contains("flaming skull"))
			return true;
		switch (item.getId()) {
		case 6570: //firecape
		case 6529: //tokkul
		case 7462: //barrow gloves
		case 23659: //tookhaar-kal
		case 9813: //quest cape
		case 9814: //quest hood
		case 20769: //comp
		case 20771: //orange comp
		case 23679:
		case 23680:
		case 23681:
		case 23682:
		case 23687:
		case 23688:
		case 23689:
		case 23695:
		case 23697:
		case 23698:
		case 23699:
		case 23700:
		case 20760:
		case 20763:
		case 18744:
		case 18745:
		case 18746:
		case 29984:
		case 29985:	
		case 29986:
		case 29987:
		case 29988:
		case 29989:
		case 771:
		case 772:
			//Dwarf Cannon
		case 6:
		case 8:
		case 10:
		case 12:
			//Royal Dwarf Cannon
		case 20498:
		case 20499:
		case 20500:
		case 20501:
			return false;
		default:
			return true;
		}
	}
}
