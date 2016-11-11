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

public class FrostDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Frost dragon" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = target instanceof Player ? (Player) target : null;
		
		int     damage            = 0;
		double  partialProtectMod = 0.6;
		double  fullProtectMod    = 0.3;

		int attackStyle = Utils.getRandom(3);

		// calculate probability of damage being applied
		// it's good to have some zeros on there
		// use the npc's stab attack since it is the only
		// combat style defined for now. Defence is taken from
		// the player's total magic defence bonuses
		int[] npcBonuses = npc.getBonuses();
		double chanceOfAttack = npcBonuses[CombatDefinitions.STAB_ATTACK] / 10.0;
		double chanceOfDefence = (player.getSkills().getLevel(Skills.MAGIC) + (2 * player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF])) * player.getPrayer().getMageMultiplier();
		double chanceOfDamage = chanceOfAttack / chanceOfDefence;

		// chance gets smaller as player's defence increases
		if(chanceOfDamage >= 0.90) {
			chanceOfDamage = 0.80;
		} else if(chanceOfDamage < 0.05) {
			chanceOfDamage = 0.05;
		}

		if((chanceOfDamage) < Math.random() && attackStyle > 0) {
			return 0;
		}

		switch (attackStyle) {
		case 0: // Melee
			if (npc.withinDistance(target, 3)) {
				damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target);
				npc.setNextAnimation(new Animation(defs.getAttackEmote()));
				delayHit(npc, 0, target, getMeleeHit(npc, damage));
			} else {	
				damage = Utils.getRandom(650);
				if (Combat.hasAntiDragProtection(target)
						|| (player != null && (player.getPrayer().usingPrayer(0, 17) 
								|| player.getPrayer().usingPrayer(1, 7)))) {
					damage = 0;
					player.getPackets()
							.sendGameMessage(
									"Your "
											+ (Combat.hasAntiDragProtection(target) ? "shield" : "prayer")+ 
											" absorbs most of the dragon's breath!", true);
				} else if ((!Combat.hasAntiDragProtection(target)
						|| !player.getPrayer().usingPrayer(0, 17) || !player
						.getPrayer().usingPrayer(1, 7))
						&& player.getFireImmune() > Utils.currentTimeMillis()) {
					damage = Utils.getRandom(164);
					player.getPackets().sendGameMessage(
							"Your potion absorbs most of the dragon's breath!",
							true);
				}
				npc.setNextAnimation(new Animation(13155));
				World.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
				delayHit(npc, 1, target, getRegularHit(npc, damage));
			}
			break;
		case 1: // Dragon breath
			if (npc.withinDistance(target, 3)) {
				damage = Utils.getRandom(650);

				// if player has an anti-fire shield, or is using a protect-magic prayer
				// decrease damage by 40%
				if (Combat.hasAntiDragProtection(target)
						|| (player != null && (player.getPrayer().usingPrayer(
								0, 17) || player.getPrayer().usingPrayer(1, 7)))) {
					
					String potionMessage = "";
					damage = (int)(damage * partialProtectMod);

					// if player also happens to have a super anti-dragon fire potion,
					// further diminish damage given
					if(player.getFireImmune() > Utils.currentTimeMillis() && player.isSuperFireImmune()) {
						potionMessage = "potion and ";
						damage = (int)(damage * 0.73);
					}

					player.getPackets()
							.sendGameMessage(
									"Your " + potionMessage
											+ (Combat
													.hasAntiDragProtection(target) ? "shield"
													: "prayer")
											+ " absorb" + (potionMessage.length() > 0 ? "" : "s") + " most of the dragon's breath!",
									true);
				// if player only has the protection of an [super] anti-dragon potion,
				// decrease damage by 35% (or 67% with a super anti-dragon potion)
				} else if ((!Combat.hasAntiDragProtection(target)
						|| !player.getPrayer().usingPrayer(0, 17) || !player
						.getPrayer().usingPrayer(1, 7))
						&& player.getFireImmune() > Utils.currentTimeMillis()) {

					String protectMessage = " fully ";
					damage = Utils.getRandom(650);

					if(player.isSuperFireImmune()) {
						damage = (int)(damage * 0.33);
					} else {
						protectMessage = " ";
						damage = (int)(damage * 0.65);
					}

					player.getPackets()
							.sendGameMessage(
									"Your potion" + protectMessage + "protects you from the heat of the dragon's breath!",
									true);
				}
				npc.setNextAnimation(new Animation(13152));
				npc.setNextGraphics(new Graphics(2465));
				delayHit(npc, 1, target, getRegularHit(npc, damage));
			} else {
				damage = Utils.getRandom(650);

				// if player has an anti-fire shield, or is using a protect-magic prayer
				// decrease damage by 40%
				if (Combat.hasAntiDragProtection(target)
						|| (player != null && (player.getPrayer().usingPrayer(
								0, 17) || player.getPrayer().usingPrayer(1, 7)))) {

					String potionMessage = "";
					damage = (int)(damage * partialProtectMod);

					// if player also happens to have a super anti-dragon fire potion,
					// further diminish damage given
					if(player.getFireImmune() > Utils.currentTimeMillis() && player.isSuperFireImmune()) {
						potionMessage = "potion and ";
						damage = (int)(damage * 0.73);
					}

					player.getPackets()
							.sendGameMessage(
									"Your " + potionMessage
											+ (Combat
													.hasAntiDragProtection(target) ? "shield"
													: "prayer")
											+ " absorbs most of the dragon's breath!",
									true);

				// if player only has the protection of an [super] anti-dragon potion,
				// decrease damage by 35% (or 67% with a super anti-dragon potion)
				} else if ((!Combat.hasAntiDragProtection(target)
						|| !player.getPrayer().usingPrayer(0, 17) || !player
						.getPrayer().usingPrayer(1, 7))
						&& player.getFireImmune() > Utils.currentTimeMillis()) {
					
					String protectMessage = " fully ";
					damage = Utils.getRandom(164);

					if(player.isSuperFireImmune()) {
						damage = (int)(damage * 0.33);
					} else {
						protectMessage = " ";
						damage = (int)(damage * 0.65);
					}

					player.getPackets()
							.sendGameMessage(
									"Your potion" + protectMessage +"protects you from the heat of the dragon's breath!",
									true);
				}
				npc.setNextAnimation(new Animation(13155));
				World.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
				delayHit(npc, 1, target, getRegularHit(npc, damage));
			}
			break;
		case 2: // Range
			damage = Utils.getRandom(250);
			npc.setNextAnimation(new Animation(13155));
			World.sendProjectile(npc, target, 2707, 28, 16, 35, 35, 16, 0);
			delayHit(npc, 1, target, getRangeHit(npc, damage));
			break;
		case 3: // Ice arrows range
			damage = Utils.getRandom(300);
			npc.setNextAnimation(new Animation(13155));
			World.sendProjectile(npc, target, 369, 28, 16, 35, 35, 16, 0);
			delayHit(npc, 1, target, getRangeHit(npc, damage));
			break;
		case 4: // Orb crap
			break;
		}
		return defs.getAttackDelay();
	}

}
