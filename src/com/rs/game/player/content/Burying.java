package com.rs.game.player.content;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class Burying {

	public enum Bone {
		NORMAL(526, 1000),

		BURNT(528, 1000),

		WOLF(2859, 1000),

		MONKEY(3183, 1250),

		BAT(530, 1250),

		BIG(532, 5000),

		JOGRE(3125, 2000),

		ZOGRE(4812, 2500),

		SHAIKAHAN(3123, 3000),

		BABY(534, 6500),

		WYVERN(6812, 4000),

		DRAGON(536, 20000),

		FAYRG(4830, 5250),

		RAURG(4832, 5500),

		DAGANNOTH(6729, 6500),

		OURG(4834, 12500),

		FROST_DRAGON(18830, 30000);

		private int id;
		private double experience;

		private static Map<Integer, Bone> bones = new HashMap<Integer, Bone>();

		static {
			for (Bone bone : Bone.values()) {
				bones.put(bone.getId(), bone);
			}
		}

		public static Bone forId(int id) {
			return bones.get(id);
		}

		private Bone(int id, double experience) {
			this.id = id;
			this.experience = experience;
		}

		public int getId() {
			return id;
		}

		public double getExperience() {
			return experience;
		}
		
		public static final Animation BURY_ANIMATION = new Animation(827);

		public static void bury(final Player player, int inventorySlot) {
			final Item item = player.getInventory().getItem(inventorySlot);
			if (item == null || Bone.forId(item.getId()) == null)
				return;
			if (player.getBoneDelay() > Utils.currentTimeMillis())
				return;
			if (player.getEquipment().getAmuletId() == 19887 && item.getId() == 526 || item.getId() == 528 || item.getId() == 530) {
				player.getPrayer().setPrayerpoints(player.getPrayer().getPrayerpoints() + 100);
			}
			if (player.getEquipment().getAmuletId() == 19887 && item.getId() == 532 || item.getId() == 7839 || item.getId() == 6812 || item.getId() == 536 || item.getId() == 18830 || item.getId() == 4834) {
				player.getPrayer().setPrayerpoints(player.getPrayer().getPrayerpoints() + 200);
			}
			final Bone bone = Bone.forId(item.getId());
			final ItemDefinitions itemDef = new ItemDefinitions(item.getId());
			player.lock(2);
			player.addBoneDelay(3000);
			player.getPackets().sendSound(2738, 0, 1);
			player.setNextAnimation(BURY_ANIMATION);
			player.getPackets().sendGameMessage(
					"You dig a hole in the ground...");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getPackets().sendGameMessage(
							"You bury the " + itemDef.getName().toLowerCase());
					player.getInventory().deleteItem(item.getId(), 1);
					double xp = bone.getExperience() * player.getAuraManager().getPrayerMultiplier();
					player.getSkills().addXp(Skills.PRAYER, xp);
					Double lastPrayer = (Double) player.getTemporaryAttributtes().get("current_prayer_xp");
					if (lastPrayer == null) {
						lastPrayer = 0.0;
					}
					double total = xp + lastPrayer;
					int amount = (int) (total / 500);
					if (amount != 0) {
						double restore = player.getAuraManager().getPrayerRestoration() * (player.getSkills().getLevelForXp(Skills.PRAYER) * 10);
						player.getPrayer().restorePrayer((int) (amount * restore));
						total -= amount * 500;
					}
					player.getTemporaryAttributtes().put("current_prayer_xp", total);
					stop();
				}

			}, 2);
		}
	}
}
