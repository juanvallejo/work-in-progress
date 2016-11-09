package com.rs.game.player.content.construction;

import com.rs.game.WorldObject;
import com.rs.game.player.Player;

public class FurnitureInterface extends Furniture {

	public FurnitureInterface(House house) {
		super(house);
	}

	public boolean showInterface(int interfaceId, WorldObject o, Player player) {
		switch (interfaceId) {
		case 396: /* Chair building interface */
			player.getInterfaceManager().sendInterface(396);
			// player.getPackets().sendIComponentSettings(396, 0, 63, 70, 190);
			player.getPackets().sendIComponentText(396, 14,
					"Crude wooden chair");
			player.getPackets().sendIComponentText(396, 19, "Wooden chair");
			player.getPackets().sendIComponentText(396, 24, "Rocking chair");
			player.getPackets().sendIComponentText(396, 29, "Oak chair");
			player.getPackets().sendIComponentText(396, 34, "Oak armchair");
			player.getPackets().sendIComponentText(396, 39, "Teak armchair");
			player.getPackets()
					.sendIComponentText(396, 44, "Mahogany armchair");
			player.getPackets().sendIComponentModel(396, 63, 4554);
			break;
		}
		return true;
	}

}
