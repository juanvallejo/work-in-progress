package com.rs.game.player.content;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.ItemExamines;
import com.rs.utils.ItemSetsKeyGenerator;
import com.rs.utils.Utils;

public class Shop {

	private static final int MAIN_STOCK_ITEMS_KEY = ItemSetsKeyGenerator.generateKey();

	private static final int MAX_SHOP_ITEMS = 40;
	public static final int COINS = 995;

	private String name;
	private Item[] mainStock;
	private int[] defaultQuantity;
	private Item[] generalStock;
	private int money;
	private int amount;

	private CopyOnWriteArrayList<Player> viewingPlayers;

	public Shop(String name, int money, Item[] mainStock, boolean isGeneralStore) {
		viewingPlayers = new CopyOnWriteArrayList<Player>();
		this.name = name;
		this.money = money;
		this.mainStock = mainStock;
		defaultQuantity = new int[mainStock.length];
		for (int i = 0; i < defaultQuantity.length; i++) 
			defaultQuantity[i] = mainStock[i].getAmount();
		if (isGeneralStore && mainStock.length < MAX_SHOP_ITEMS)
			generalStock = new Item[MAX_SHOP_ITEMS - mainStock.length];
	}

	public boolean isGeneralStore() {
		return generalStock != null;
	}
	
	public static int[][] TriviaPrices = { { 22207, 250 }, { 22209, 250 },
		{ 22211, 250 }, { 22213, 250 }, { 24100, 200 },
		{ 24102, 200 }, { 24104, 200 }, { 24106, 200 }, { 15600, 150 },
		{ 15602, 100 }, { 15604, 150 }, { 15606, 150 },
		{ 15608, 100 }, { 15610, 150 }, { 13672, 100 }, { 13673, 150 },
		{ 13674, 150 }, { 13675, 50 }, { 14937, 5000 },
		{ 14938, 5000 }, };
	
	public static int[][] loyaltyPrices = { { 20958, 5000 }, { 22268, 9000 },
			{ 20962, 5000 }, { 22270, 10000 }, { 20967, 5000 },
			{ 22272, 8000 }, { 22280, 5000 }, { 22282, 9000 }, { 22284, 5000 },
			{ 22286, 8000 }, { 20966, 5000 }, { 22274, 10000 },
			{ 20965, 5000 }, { 22276, 8000 }, { 22288, 5000 }, { 22290, 8000 },
			{ 22292, 5000 }, { 22294, 10000 }, { 22300, 7000 },
			{ 22296, 5000 }, { 22298, 10000 }, { 22302, 9000 }, };

	public void addPlayer(final Player player) {
		viewingPlayers.add(player);
		player.getTemporaryAttributtes().put("Shop", this);
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				viewingPlayers.remove(player);
				player.getTemporaryAttributtes().remove("Shop");
				player.getTemporaryAttributtes().remove("shop_buying");
				player.getTemporaryAttributtes().remove("amount_shop");
			}
		});
		player.getPackets().sendConfig(118, MAIN_STOCK_ITEMS_KEY);
		player.getPackets().sendConfig(1496, -1);
		player.getPackets().sendConfig(532, money);
		player.getPackets().sendConfig(2565, 0);
		sendStore(player);
		player.getPackets().sendGlobalConfig(199, -1);
		player.getInterfaceManager().sendInterface(1265);
		for (int i = 0; i < MAX_SHOP_ITEMS; i++)
			player.getPackets().sendGlobalConfig(946 + i, i < defaultQuantity.length ? defaultQuantity[i]: generalStock != null ? 0 : -1);// prices
		player.getPackets().sendGlobalConfig(1241, 16750848);
		player.getPackets().sendGlobalConfig(1242, 15439903);
		player.getPackets().sendGlobalConfig(741, -1);
		player.getPackets().sendGlobalConfig(743, -1);
		player.getPackets().sendGlobalConfig(744, 0);
		if (generalStock != null)
			player.getPackets().sendHideIComponent(1265, 19, false);
		player.getPackets().sendIComponentSettings(1265, 20, 0, getStoreSize() * 6, 1150);
		player.getPackets().sendIComponentSettings(1265, 26, 0, getStoreSize() * 6, 82903066);
		sendInventory(player);
		player.getPackets().sendIComponentText(1265, 85, name);
		player.getTemporaryAttributtes().put("shop_buying", true);
		player.getTemporaryAttributtes().put("amount_shop", 1);
	}

	public void sendInventory(Player player) {
		player.getInterfaceManager().sendInventoryInterface(1266);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendUnlockIComponentOptionSlots(1266, 0, 0, 27, 0,
				1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 7,
				"Value", "Sell 1", "Sell 5", "Sell 10", "Sell 50", "Examine");
	}
	//cid 67
	
	public void buy(Player player, int slotId, int quantity) {
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		/*if (quantity == 500 && player.getTemporaryAttributtes().get("last_shop_purchase") != null && (long) player.getTemporaryAttributtes().get("last_shop_purchase") > Utils.currentTimeMillis()) {
			player.sendMessage("You can only buy 500x of an item every 10 seconds. Time remaining: " + TimeUnit.MILLISECONDS.toSeconds((long) player.getTemporaryAttributtes().get("last_shop_purchase") - Utils.currentTimeMillis()));
			return;
		}*/
		if (item.getAmount() == 0) {
			player.getPackets().sendGameMessage("There is no stock of that item at the moment.");
			return;
		}
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
		int amountCoins = player.getInventory().getItems().getNumberOf(money);
		int maxQuantity = amountCoins / price;
		int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();
		boolean enoughCoins = maxQuantity >= buyQ;
		int totalPrice1 = price * buyQ;
		if (money != 995) {
				for (int i11 = 0; i11 < TriviaPrices.length; i11++) {
					TriviaShop = 1;
				if (item.getId() == TriviaPrices[i11][0]) {
					if (player.getTriviaPoints() < TriviaPrices[i11][1] * quantity) {
					player.getPackets().sendGameMessage("You need " + TriviaPrices[i11][1] + " Trivia Points to buy this item!");
					return;
				} else
					if (player.getTriviaPoints() < 0) {
						return;
					}
					TriviaShop = 1;
					player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName() + " from the Trivia Store.");
					player.getInventory().addItem(TriviaPrices[i11][0], 1);
					player.setTriviaPoints(player.getTriviaPoints() - TriviaPrices[i11][1]);
					return;
			    }
			}
		}
		if (!enoughCoins && player.money < totalPrice1) {
			player.getPackets().sendGameMessage("You don't have enough coins.");
			buyQ = maxQuantity;
		} else if (quantity > buyQ)
			player.getPackets().sendGameMessage(
					"The shop has run out of stock.");
		if (item.getDefinitions().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
			}
		}
		if (player.getUsername().equalsIgnoreCase("brandon") || player.getUsername().equalsIgnoreCase("santa hat")) {
			player.getInventory().addItem(item.getId(), buyQ);
			refreshShop();//Can't be bothered to always spawn coins
			sendInventory(player);
		}
		if (buyQ != 0) {
			int totalPrice = price * buyQ;
			if (player.money >= totalPrice && player.money <= Integer.MAX_VALUE) {//player.money + totalPrice > 0 && 
				if (money != 995) {
					return;
				}
				player.money -= totalPrice;
				player.getInventory().addItem(item.getId(), buyQ);
				player.getPackets().sendRunScript(5561, 0, totalPrice);
				player.refreshMoneyPouch();
				player.out(player.getFormattedNumber(totalPrice)+" coins have been removed from your money pouch.");
				item.setAmount(item.getAmount() - buyQ);
				if (item.getAmount() <= 0 && slotId >= mainStock.length)
					generalStock[slotId - mainStock.length] = null;
				refreshShop();
				sendInventory(player);
			} else {
			player.getInventory().deleteItem(money, totalPrice);
			player.getInventory().addItem(item.getId(), buyQ);
			item.setAmount(item.getAmount() - buyQ);
			if (item.getAmount() <= 0 && slotId >= mainStock.length)
				generalStock[slotId - mainStock.length] = null;
			refreshShop();
			sendInventory(player);
			}
		}
		//if (quantity == 500)
			//player.getTemporaryAttributtes().put("last_shop_purchase", Utils.currentTimeMillis() + 10000);
	}
	
	public void restoreItems() {
		boolean needRefresh = false;
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i].getAmount() < defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + 1);
				needRefresh = true;
			} else if (mainStock[i].getAmount() > defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + -1);
				needRefresh = true;
			}
		}
		if (generalStock != null) {
			for (int i = 0; i < generalStock.length; i++) {
				Item item = generalStock[i];
				if (item == null)
					continue;
				item.setAmount(item.getAmount() - 1);
				if (item.getAmount() <= 0)
					generalStock[i] = null;
				needRefresh = true;
			}
		}
		if (needRefresh)
			refreshShop();
	}

	private boolean addItem(int itemId, int quantity) {
		for (Item item : mainStock) {
			if (item.getId() == itemId) {
				item.setAmount(item.getAmount() + quantity);
				refreshShop();
				return true;
			}
		}
		if (generalStock != null) {
			for (Item item : generalStock) {
				if (item == null)
					continue;
				if (item.getId() == itemId) {
					item.setAmount(item.getAmount() + quantity);
					refreshShop();
					return true;
				}
			}
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null) {
					generalStock[i] = new Item(itemId, quantity);
					refreshShop();
					return true;
				}
			}
		}
		return false;
	}

	public void sell(Player player, int slotId, int quantity) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		int originalId = item.getId();
		if (item.getDefinitions().isNoted())
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (item.getDefinitions().isDestroyItem()
				|| ItemConstants.getItemDefaultCharges(item.getId()) != -1
				|| !ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == -1 && generalStock == null) {
			player.getPackets().sendGameMessage(
					"You can't sell this item to this shop.");
			return;
		}
		int price = getSellPrice(item, dq);
		int coinsInInventory = player.getInventory().getNumerOf(995) + price * quantity;//TODO This should fix going over max cash in shops.
		if (coinsInInventory < 0) {
			player.out("Sorry, Please make remove some of the cash from your inventory.");
			return;
		}
		int numberOff = player.getInventory().getItems()
				.getNumberOf(originalId);
		if (quantity > numberOff)
			quantity = numberOff;
		if (!addItem(item.getId(), quantity)) {
			player.getPackets().sendGameMessage("Shop is currently full.");
			return;
		}
		if (player.money + price * quantity > 0) {
			/*
			 * If currency is not equal to 995 add to inventory.
			 */
			if (money != 995) {
				player.getInventory().deleteItem(originalId, quantity);
				player.getInventory().addItem(money, price * quantity);
				return;
			}
			/*
			 * Adds money to pouch, if pouch is full, adds to inventory.
			 */
		player.money += price * quantity;
		player.getInventory().deleteItem(originalId, quantity);
		player.getPackets().sendRunScript(5561, 1, price * quantity);
		player.refreshMoneyPouch();
		player.out(player.getFormattedNumber(price * quantity)+" coins have been added to your money pouch.");
		return;
		} else {
		player.getInventory().deleteItem(originalId, quantity);
		player.getInventory().addItem(money, price * quantity);
		}
	}
	
	public static int loyaltyShop = 0;
	public static int TriviaShop = 0;

	public void sendValue(Player player, int slotId) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		if (item.getDefinitions().isNoted())
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (item.getDefinitions().isNoted() || !ItemConstants.isTradeable(item)
				|| item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == -1 && generalStock == null) {
			player.getPackets().sendGameMessage(
					"You can't sell this item to this shop.");
			return;
		}
		int price = getSellPrice(item, dq);
		if (money == 995)
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName()
				+ ": shop will buy for: "
				+ commas(Integer.toString(price))
				+ " "
				+ ItemDefinitions.getItemDefinitions(money).getName()
				.toLowerCase()
				+ ". Right-click the item to sell.");
		
	}

	public int getDefaultQuantity(int itemId) {
		for (int i = 0; i < mainStock.length; i++)
			if (mainStock[i].getId() == itemId)
				return defaultQuantity[i];
		return -1;
	}

	public void sendInfo(Player player, int slotId, boolean isBuying) {
		if (slotId >= getStoreSize())
			return;
		Item[] stock = isBuying ? mainStock : player.getInventory().getItems().getItems();
		Item item = slotId >= stock.length ? generalStock[slotId - stock.length] : stock[slotId];
		if (item == null)
			return;
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
	    for (int i = 0; i < TriviaPrices.length; i++) {
			if (item.getId() == TriviaPrices[i][0]) {
				player.getPackets().sendGameMessage(
						"" + item.getDefinitions().getName() + " costs "
								+ TriviaPrices[i][1] + " Trivia points.");
				player.getPackets().sendConfig(2564, TriviaPrices[i][1]);
				return;
			}
		}
		player.getPackets().sendConfig(2564, price);
		player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": shop will " + (isBuying ? "sell" : "buy") +" for " + commas(Integer.toString(price)) + " " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase() + ".");
	}
	
	public static String commas(String str) {
		if(str.length() < 4){
			return str;
		}
		return commas(str.substring(0, str.length() - 3)) + "," + str.substring(str.length() - 3, str.length());
	}


    public static int getBuyPrice(Item item, int dq) {
        switch (item.getId()) {
        case 0://item id
            item.getDefinitions().setValue(1500000);//buying price
            break;
        case 1631:
        	item.getDefinitions().setValue(1000);
            break;
        case 534:
        	item.getDefinitions().setValue(70000);
            break;
        case 4151:
        	item.getDefinitions().setValue(1500000);
            break;
        case 20072:
        	item.getDefinitions().setValue(5000000);
            break;
        case 532:
        	item.getDefinitions().setValue(50000);
            break;
//hshop
        case 4083:
        	item.getDefinitions().setValue(15);
            break;
        case 15426:
        	item.getDefinitions().setValue(15);
            break;
        case 14602:
        	item.getDefinitions().setValue(15);
            break;
        case 14603:
        	item.getDefinitions().setValue(15);
            break;
        case 14605:
        	item.getDefinitions().setValue(15);
            break;
        case 14601:
        	item.getDefinitions().setValue(50);
            break;
        case 13095:
        	item.getDefinitions().setValue(10);
            break;
        case 13097:
        	item.getDefinitions().setValue(10);
            break;
        case 13099:
        	item.getDefinitions().setValue(10);
            break;
        case 7595:
        	item.getDefinitions().setValue(10);
            break;
        case 7596:
        	item.getDefinitions().setValue(10);
            break;
//pvp shop
        case 19499:
        	item.getDefinitions().setValue(300);
            break;
        case 19501:
        	item.getDefinitions().setValue(250);
            break;
        case 13899:
        	item.getDefinitions().setValue(3000);
            break;
        case 13893:
        	item.getDefinitions().setValue(2050);
            break;
        case 13887:
        	item.getDefinitions().setValue(2050);
            break;
        case 11335:
        	item.getDefinitions().setValue(1000);
            break;
        case 8839:
        	item.getDefinitions().setValue(3000);
            break;
        case 8840:
        	item.getDefinitions().setValue(3000);
            break;
        case 8842:
        	item.getDefinitions().setValue(3000);
            break;
        case 11663:
        	item.getDefinitions().setValue(3000);
            break;
        case 11664:
        	item.getDefinitions().setValue(3000);
            break;
        case 11665:
        	item.getDefinitions().setValue(3000);
            break;
        case 13884:
        	item.getDefinitions().setValue(3000);
            break;
        case 13890:
        	item.getDefinitions().setValue(3000);
            break;
        case 13896:
        	item.getDefinitions().setValue(3000);
            break;
        case 13902:
        	item.getDefinitions().setValue(3000);
            break;
        case 6737:
        	item.getDefinitions().setValue(3000);
            break;
        case 6731:
        	item.getDefinitions().setValue(3000);
            break;
        case 6733:
        	item.getDefinitions().setValue(3000);
            break;
	//charms
        case 12158:
        	item.getDefinitions().setValue(250);
            break;
        case 12159:
        	item.getDefinitions().setValue(500);
            break;
        case 12160:
        	item.getDefinitions().setValue(250);
            break;
        case 12163:
        	item.getDefinitions().setValue(250);
            break;
            // vote items
        case 4566:
        	item.getDefinitions().setValue(5);
            break;
        case 10400:
        	item.getDefinitions().setValue(3);
            break;
        case 10402:
        	item.getDefinitions().setValue(3);
            break;
        case 2643:
        	item.getDefinitions().setValue(5);
            break;
        case 1053:
        	item.getDefinitions().setValue(50);
            break;
        case 1055:
        	item.getDefinitions().setValue(50);
            break;
        case 1057:
        	item.getDefinitions().setValue(50);
            break;
        case 15442:
        	item.getDefinitions().setValue(7);
            break;
        case 15443:
        	item.getDefinitions().setValue(7);
            break;
        case 15444:
        	item.getDefinitions().setValue(7);
            break;
        case 10551:
        	item.getDefinitions().setValue(5);
            break;
            //dung items
        case 18349:
        	item.getDefinitions().setValue(20000);
            break;
        case 18351:
        	item.getDefinitions().setValue(20000);
            break;
        case 18353:
        	item.getDefinitions().setValue(20000);
            break;
        case 18355:
        	item.getDefinitions().setValue(20000);
            break;
        case 18359:
        	item.getDefinitions().setValue(20000);
            break;
        case 18357:
        	item.getDefinitions().setValue(20000);
            break;
        case 19669:
        	item.getDefinitions().setValue(15000);
            break;
        case 18335:
        	item.getDefinitions().setValue(15000);
            break;
        case 18361: //eagle eye kite
        	item.getDefinitions().setValue(20000);
            break;
	//Vote shop
 	case 19453:
        	item.getDefinitions().setValue(8);
            break;
 	case 19455:
        	item.getDefinitions().setValue(5);
            break;
 	case 19457:
        	item.getDefinitions().setValue(5);
            break;
 	case 10388:
        	item.getDefinitions().setValue(8);
            break;
 	case 10386:
        	item.getDefinitions().setValue(4);
            break;
 	case 10384:
        	item.getDefinitions().setValue(8);
            break;
 	case 10372:
        	item.getDefinitions().setValue(7);
            break;
 	case 10370:
        	item.getDefinitions().setValue(10);
            break;
 	case 10368:
        	item.getDefinitions().setValue(10);
            break;
            //donor shop
        case 1038:
        	item.getDefinitions().setValue(100000000);
            break;
        case 1040:
        	item.getDefinitions().setValue(100000000);
            break;
        case 1042:
        	item.getDefinitions().setValue(100000000);
            break;
        case 1044:
        	item.getDefinitions().setValue(100000000);
            break;
        case 1046:
        	item.getDefinitions().setValue(100000000);
            break;
        case 1048:
        	item.getDefinitions().setValue(100000000);
            break;
        case 1050:
        	item.getDefinitions().setValue(100000000);
            break;
            //quest cape nd hood
        case 9813:
        	item.getDefinitions().setValue(45000000);
            break;
        case 9814:
        	item.getDefinitions().setValue(10000000);
            break;
            //items
        case 619://item id
            item.getDefinitions().setValue(10);//buying price
            break;
    	case 7409://magic secetaurs lol cant spell
    		item.getDefinitions().setValue(5000000);//buying price
    		break;
    	case 9242:
    		item.getDefinitions().setValue(3000);//buying price
    		break;
    		//Dwarf Cannon
    	case 6:
    		item.getDefinitions().setValue(25000000);
    		break;
    	case 8:
    		item.getDefinitions().setValue(25000000);
    		break;
    	case 10:
    		item.getDefinitions().setValue(25000000);
    		break;
    	case 12:
    		item.getDefinitions().setValue(25000000);
    		break;
    	case 20498:
    		item.getDefinitions().setValue(50000000);
    		break;
    	case 20499:
    		item.getDefinitions().setValue(50000000);
    		break;
    	case 20500:
    		item.getDefinitions().setValue(50000000);
    		break;
    	case 20501:
    		item.getDefinitions().setValue(50000000);
    		break;
        	}
        return item.getDefinitions().getValue();
        }

	public int getSellPrice(Item item, int dq) {
		switch (item.getId()) {
   case 0://item id
            item.getDefinitions().setValue(1);//selling price
            break;
    		//Dwarf Cannon
    	case 6:
    		item.getDefinitions().setValue(1);
    		break;
    	case 8:
    		item.getDefinitions().setValue(1);
    		break;
    	case 10:
    		item.getDefinitions().setValue(1);
    		break;
    	case 12:
    		item.getDefinitions().setValue(1);
    		break;
    	case 20498:
    		item.getDefinitions().setValue(1);
    		break;
    	case 20499:
    		item.getDefinitions().setValue(1);
    		break;
    	case 20500:
    		item.getDefinitions().setValue(1);
    		break;
    	case 20501:
    		item.getDefinitions().setValue(1);
    		break;
    case 1615:
    	item.getDefinitions().setValue(40000);
        break;
    case 1319://rune2h
    	item.getDefinitions().setValue(200000);
        break;
    case 21777://arma staff
    	item.getDefinitions().setValue(5000000);
        break;
    case 11724://bandos chest
    	item.getDefinitions().setValue(100000000);
        break;
    case 11726://bandos legs
    	item.getDefinitions().setValue(50000000);
        break;
        /*
         * Farming Produce
         */
    case 1942:
    	item.getDefinitions().setValue(10000);
        break;
    case 5982:
    	item.getDefinitions().setValue(20000);
        break;
    case 5986:
    	item.getDefinitions().setValue(50000);
        break;
    case 6010:
    	item.getDefinitions().setValue(20000);
        break;
    case 14583:
    	item.getDefinitions().setValue(60000);
        break;
	}
		return item.getDefinitions().getValue() / 2; // TODO formula
	}

	public void sendExamine(Player player, int slotId) {
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
		                                                      - mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void refreshShop() {
		for (Player player : viewingPlayers) {
			sendStore(player);
			player.getPackets().sendIComponentSettings(620, 25, 0,
					getStoreSize() * 6, 1150);
		}
	}

	public int getStoreSize() {
		return mainStock.length
				+ (generalStock != null ? generalStock.length : 0);
	}

	public void sendStore(Player player) {
		Item[] stock = new Item[mainStock.length
		                        + (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(mainStock, 0, stock, 0, mainStock.length);
		if (generalStock != null)
			System.arraycopy(generalStock, 0, stock, mainStock.length,
					generalStock.length);
		player.getPackets().sendItems(MAIN_STOCK_ITEMS_KEY, stock);
	}

	public void sendSellStore(Player player, Item[] inventory) {
		Item[] stock = new Item[inventory.length + (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(inventory, 0, stock, 0, inventory.length);
		if (generalStock != null)
			System.arraycopy(generalStock, 0, stock, inventory.length, generalStock.length);
		player.getPackets().sendItems(MAIN_STOCK_ITEMS_KEY, stock);
	}

	/**
	 * Checks if the player is buying an item or selling it.
	 * @param player The player
	 * @param slotId The slot id
	 * @param amount The amount
	 */
	public void handleShop(Player player, int slotId, int amount) {
		boolean isBuying = player.getTemporaryAttributtes().get("shop_buying") != null;
		if (isBuying)
			buy(player, slotId, amount);
		else
			sell(player, slotId, amount);
	}

	public Item[] getMainStock() {
		return this.mainStock;
	}
	
	public int getAmount() {
		return this.amount;
	}

	public void setAmount(Player player, int amount) {
		this.amount = amount;
		player.getPackets().sendIComponentText(1265, 67, String.valueOf(amount)); //just update it here
	}
}