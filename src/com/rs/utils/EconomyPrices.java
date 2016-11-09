package com.rs.utils;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.content.ItemConstants;

public final class EconomyPrices {

	public static int getPrice(int itemId) {
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
		if (defs.isNoted())
			itemId = defs.getCertId();
		else if (defs.isLended())
			itemId = defs.getLendId();
		if (!ItemConstants.isTradeable(new Item(itemId, 1)))
			return 0;
		if (itemId == 995) // TODO after here
			return 1;
		if (itemId == 1048) // TODO after here
			return 2000000000;
		if (itemId == 1050) // TODO after here
			return 2000000000;
		if (itemId == 1038) // TODO after here
			return 2000000000;
		if (itemId == 1040) // TODO after here
			return 2000000000;
		if (itemId == 1042) // TODO after here
			return 2000000000;
		if (itemId == 1044) // TODO after here
			return 2000000000;
		if (itemId == 1046) // TODO after here
			return 2000000000;
		if (itemId == 1053) // TODO after here
			return 2000000000;
		if (itemId == 1055) // TODO after here
			return 2000000000;
		if (itemId == 1057) // TODO after here
			return 2000000000;
		return defs.getValue() * 3; // TODO get price from real item from saved
									// prices from ge
	}

	private EconomyPrices() {

	}
}
