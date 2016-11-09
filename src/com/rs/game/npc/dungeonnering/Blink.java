package com.rs.game.npc.dungeonnering;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.Hit.HitLook;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * Dungeonnering boss, not actual combat script tho
 * 
 * @author cache[j].standAnimation = 14948; cache[j].walkAnimation = 14950;
 */
@SuppressWarnings("serial")
public class Blink extends NPC {

	public static final int[] HITSOUNDS = new int[] { 3005, 3006, 3010, 3014,
			3048, 2978 };

	// 12878
	public Blink(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setForceMultiAttacked(true);
		setForceAgressive(true);
		setLureDelay(0);
		setRun(true);

	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}


	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		if (hit.getSource() != null) {
			int recoil = (int) (hit.getDamage() * 0.1);// reflects 10%
			if (recoil > 0) {
				this.playSound(HITSOUNDS[Utils.random(HITSOUNDS.length)], 2);
				hit.getSource().applyHit(
						new Hit(this, recoil, HitLook.REFLECTED_DAMAGE));
			}
		}
	}
}