package com.rs.game.player.content;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.actions.Cooking.Cookables;

@SuppressWarnings("unused")
public class BonesOnAltar extends Action {
	
	public final String MESSAGE = "The gods are very pleased with your offerings.";
	public final double MARBLE = 1.5;
	public final double GILDED = 2;
	
	public enum Bones {
		BONES(new Item(526, 1), 5000),
		BIG_BONES(new Item(532, 1), 10000),
		BABYDRAGON_BONES(new Item(534, 1), 13000),
		WYVERN_BONES(new Item(6812, 1), 10000),
		DRAGON_BONES(new Item(536, 1), 40000),
		OURG_BONES(new Item(4834, 1), 47000),
		FROST_DRAGON_BONES(new Item(18830, 1), 60000),
		DAGANNOTH_BONES(new Item(6729, 1), 25250);
	
		private static Map<Short, Bones> bones = new HashMap<Short, Bones>();
	
		public static Bones forId(short itemId) {
			return bones.get(itemId);
		}
	
		static {
			for (Bones bone: Bones.values()) {
				bones.put((short) bone.getBone().getId(), bone);
			}
		}
	
		private Item item;
		private int xp;
	
		private Bones(Item item, int xp) {
			this.item = item;
			this.xp = xp;
		}
		
		public Item getBone() {
			return item;
		}
		
		public int getXP() {
			return xp;
		}
	}
		
	private Bones bone;
	private int amount;
	private Item item;
	private WorldObject object;
	private Animation USING = new Animation(896);
	
	public BonesOnAltar(WorldObject object, Item item, int amount) {
		this.amount = amount;
		this.item = item;
		this.object = object;
	}
	
	public static Bones isGood(Item item) {
		return Bones.forId((short) item.getId());
	}
	
	public boolean start(Player player) {
		if((this.bone = Bones.forId((short) item.getId())) == null) {
			return false;
		}
		player.faceObject(object);
		return true;
	}
	
	public boolean process(Player player) {
		if (!World.getRegion(object.getRegionId()).containsObject(
						object.getId(), object))
			return false;
		if (!player.getInventory().containsItem(item.getId(), 1)) {
			return false;
		}
		if (!player.getInventory().containsItem(bone.getBone().getId(), 1)) {
			return false;
		}
		return true;
	}
	
	public int processWithDelay(Player player) {
		if (player.boneOnAltar == 1) {
			player.setNextAnimation(USING);
			player.getPackets().sendGraphics(new Graphics(624), object);
			player.getInventory().deleteItem(item.getId(), 1);
			//player.getSkills().addXp(Skills.PRAYER, bone.getXP()*MULTIPLIER);
			player.getSkills().addXp(Skills.PRAYER, bone.getXP());
			player.getPackets().sendGameMessage(MESSAGE);
			player.getInventory().refresh();
		} else if (player.boneOnAltar == 2){
				player.setNextAnimation(USING);
				player.getPackets().sendGraphics(new Graphics(624), object);
				player.getInventory().deleteItem(item.getId(), 1);
				player.getSkills().addXp(Skills.PRAYER, bone.getXP() * MARBLE);
				player.getPackets().sendGameMessage(MESSAGE);
				player.getInventory().refresh();
		} else if (player.boneOnAltar == 3) {
				player.setNextAnimation(USING);
				player.getPackets().sendGraphics(new Graphics(624), object);
				player.getInventory().deleteItem(item.getId(), 1);
				player.getSkills().addXp(Skills.PRAYER, bone.getXP() * GILDED);
				player.getPackets().sendGameMessage(MESSAGE);
				player.getInventory().refresh();
			}
			return 3;
	}

	public void stop(final Player player) {
		this.setActionDelay(player, 3);
	}
	
}