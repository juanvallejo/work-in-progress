package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.CombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.Combat;
import com.rs.utils.Utils;

public class MetalDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Bronze dragon", "Iron dragon", "Steel dragon",
				"Mithril dragon" };
	}

	/**
	 * Calculates magic damage on a player based on player's anti-fire defenses
	 * and given damage modifiers. Will also output to player method of protection if any.
	 * Total computed damage is influenced by a player's prayer and armour bonuses
	 */
	public int doMagicDamage(NPC npc, Entity target, double fullProtectMod, double partialProtectMod, boolean sendProjectile) {

		final Player player = target instanceof Player ? (Player)target : null;
		int damage = Utils.getRandom(650);

		// if player is null, simply send animation to dragon, ignore damage calculation
		if(player == null) {

			if(sendProjectile) {
				npc.setNextAnimation(new Animation(13160));
				World.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
			} else {
				npc.setNextAnimation(new Animation(13164));
				npc.setNextGraphics(new Graphics(2465));
			}

			delayHit(npc, 1, target, getRegularHit(npc, damage));

			return damage;
		}

		// calculate probability of damage being applied
		// it's good to have some zeroes on there
		// use the npc's stab attack since it is the only
		// combat style defined for now. Defence is taken from
		// the player's total magic defence bonuses
		int[] npcBonuses = npc.getBonuses();
		double chanceOfAttack = npcBonuses[CombatDefinitions.STAB_ATTACK] / 10.0;
		double chanceOfDefence = (player.getSkills().getLevel(Skills.MAGIC) + (2 * player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF])) * player.getPrayer().getMageMultiplier();
		double chanceOfDamage = chanceOfAttack / chanceOfDefence;

		// chance gets smaller as player's defence
		// increases
		if(chanceOfDamage >= 0.90) {
			chanceOfDamage = 0.80;
		} else if(chanceOfDamage < 0.05) {
			chanceOfDamage = 0.05;
		}

		if((chanceOfDamage) < Math.random()) {
			return 0;
		}

		// if player has combination of potion and prayer or potion and anti-fire armour,
		// or just super anti-fire potion, reduce damage by 70%
		if((player.getFireImmune() > Utils.currentTimeMillis() && player.isSuperFireImmune())
			|| (Combat.hasAntiDragProtection(target) && player.getFireImmune() > Utils.currentTimeMillis())
			|| (player.getFireImmune() > Utils.currentTimeMillis() && (player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7)))) {

			damage = (int)(damage * fullProtectMod);

			// decrease more damage if using super anti-fire potion
			if(player.isSuperFireImmune()) {
				player.getPackets().sendGameMessage("Your super anti-fire potion absorbs a large amount of the dragon's breath!", true);
			} else {

				String fireProtectMethod = "shield";

				if(player.getFireImmune() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("Your potion absorbs some of the dragon's breath!", true);
				} else if(!Combat.hasAntiDragProtection(target)) {
					fireProtectMethod = "prayer";
				}

				player.getPackets().sendGameMessage("Your " + fireProtectMethod + " absorbs most of the dragon's breath!", true);

			}

		// if player is using an antifire potion, fire-protection armour, or any magic prayer
		// decrease fire damage slightly (-40% with potion, -30% with armour or prayer).
		} else if((player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7))
			|| Combat.hasAntiDragProtection(target)
			|| player.getFireImmune() > Utils.currentTimeMillis()) {

			damage = (int)(damage * partialProtectMod);

			// if player has no anti-fire potion, but is using a combination of
			// both anti-fire armour and protect from magic prayer, give no added bonus
			if(player.getFireImmune() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("Your potion absorbs some of the dragon's breath!", true);
			} else {

				String fireProtectMethod = "prayer";

				if(Combat.hasAntiDragProtection(target)) {
					fireProtectMethod = "shield";
				}

				player.getPackets().sendGameMessage("Your " + fireProtectMethod + " absorbs most of the dragon's breath!", true);
			}

		}

		if(sendProjectile) {
			npc.setNextAnimation(new Animation(13160));
			World.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
		} else {
			npc.setNextAnimation(new Animation(13164));
			npc.setNextGraphics(new Graphics(2465));
		}

		delayHit(npc, 1, target, getRegularHit(npc, damage));

		// reduce damage according to player's protection bonuses
		damage /= (chanceOfDefence > 1.0 ? (1 - ((chanceOfDefence / 100) / 3)) : 1);

		return (int)damage;

	}

	@Override
	public int attack(NPC npc, Entity target) {

		NPCCombatDefinitions defs 	= npc.getCombatDefinitions();
		final Player player 		= target instanceof Player ? (Player) target : null;

		int damage;

		switch(Utils.getRandom(1)) {

			case 0:

				// if npc is within 3 tiles of player, use melee attack and animation
				if(npc.withinDistance(target, 3)) {

					damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target);
					npc.setNextAnimation(new Animation(defs.getAttackEmote()));

					delayHit(npc, 0, target, getMeleeHit(npc, damage));

				} else {
					damage = doMagicDamage(npc, target, 0.1, 0.4, true);
				}

				break;

			case 1:

				// in this attack style, even if npc is within 3 tiles of player,
				// npc will send projectile to player
				if (npc.withinDistance(target, 3)) {

					damage = doMagicDamage(npc, target, 0.2, 0.4, false);

				} else {
					damage = doMagicDamage(npc, target, 0.2, 0.4, true);
				}

				break;
		}

		return defs.getAttackDelay();
	}

}
