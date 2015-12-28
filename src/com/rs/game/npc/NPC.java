package com.rs.game.npc;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.cache.Cache;
import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.minigames.pest.Lander;
import com.rs.game.npc.combat.NPCCombat;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Slayer;
import com.rs.game.player.actions.Slayer.SlayerTask;
import com.rs.game.player.content.Burying;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.WarriorsGuild;
import com.rs.game.player.controlers.FightKiln;
import com.rs.game.player.controlers.WGuildControler;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.MapAreas;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;
import com.rs.utils.NPCDrops;
import com.rs.utils.Utils;
import com.rs.game.player.Skills;
import com.rs.game.player.content.Burying.Bone;

import com.rs.game.player.CombatDefinitions;

public class NPC extends Entity implements Serializable {

	private static final long serialVersionUID = -4794678936277614443L;

	private int id;
	private WorldTile respawnTile;
	private int mapAreaNameHash;
	private boolean canBeAttackFromOutOfArea;
	private boolean randomwalk;
	private int[] bonuses; // 0 stab, 1 slash, 2 crush,3 mage, 4 range, 5 stab
	// def, blahblah till 9
	private boolean spawned;
	private transient NPCCombat combat;
	public WorldTile forceWalk;

	private long lastAttackedByTarget;
	private boolean cantInteract;
	private int capDamage;
	private int lureDelay;
	private boolean cantFollowUnderCombat;
	private boolean forceAgressive;
	private int forceTargetDistance;
	private boolean forceFollowClose;
	private boolean forceMultiAttacked;
	private boolean noDistanceCheck;

	// used for bgs
	private int defenceDrainTicks = -1;

	// npc masks
	private transient Transformation nextTransformation;
	//name changing masks
	private String name;
	private transient boolean changedName;
	private int combatLevel;
	private transient boolean changedCombatLevel;
	private transient boolean locked;
	
	public NPC(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		this(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}

	/*
	 * creates and adds npc
	 */
	public NPC(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(tile);
		this.id = id;
		this.respawnTile = new WorldTile(tile);
		this.mapAreaNameHash = mapAreaNameHash;
		this.canBeAttackFromOutOfArea = canBeAttackFromOutOfArea;
		this.setSpawned(spawned);
		combatLevel = -1;
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		setRandomWalk((getDefinitions().walkMask & 0x2) != 0
				|| forceRandomWalk(id));
		bonuses = NPCBonuses.getBonuses(id);
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
		// npc is inited on creating instance
		initEntity();
		World.addNPC(this);
		World.updateEntityRegion(this);
		// npc is started on creating instance
		loadMapRegions();
		checkMultiArea();
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || nextTransformation != null || changedCombatLevel || changedName;
	}

	public void transformIntoNPC(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
	}

	public void setNPC(int id) {
		this.id = id;
		bonuses = NPCBonuses.getBonuses(id);
	}

	@Override
	public void resetMasks() {
		super.resetMasks();
		nextTransformation = null;
		changedCombatLevel = false;
		changedName = false;
	}

	public int getMapAreaNameHash() {
		return mapAreaNameHash;
	}

	public void setCanBeAttackFromOutOfArea(boolean b) {
		canBeAttackFromOutOfArea = b;
	}
	
	public boolean canBeAttackFromOutOfArea() {
		return canBeAttackFromOutOfArea;
	}

	public NPCDefinitions getDefinitions() {
		return NPCDefinitions.getNPCDefinitions(id);
	}

	public NPCCombatDefinitions getCombatDefinitions() {
		return NPCCombatDefinitionsL.getNPCCombatDefinitions(id);
	}

	@Override
	public int getMaxHitpoints() {
		return getCombatDefinitions().getHitpoints();
	}

	public static void main(String[] args) throws IOException {
		Cache.init();
	}

	public int getId() {
		return id;
	}


	public void processNPC() {
		if (isDead() || locked)
			return;
		if (!combat.process()) { // if not under combat
			if (!isForceWalking()) {// combat still processed for attack delay
				// go down
				// random walk
				if (!cantInteract) {
					if (!checkAgressivity()) {
						if (getFreezeDelay() < Utils.currentTimeMillis()) {
							if (((hasRandomWalk()) && World.getRotation(
									getPlane(), getX(), getY()) == 0) // temporary
									// fix
									&& Math.random() * 1000.0 < 100.0) {
								int moveX = (int) Math
										.round(Math.random() * 10.0 - 5.0);
								int moveY = (int) Math
										.round(Math.random() * 10.0 - 5.0);
								resetWalkSteps();
								if (getMapAreaNameHash() != -1) {
									if (!MapAreas.isAtArea(getMapAreaNameHash(), this)) {
										forceWalkRespawnTile();
										return;
									}
									addWalkSteps(getX() + moveX, getY() + moveY, 5);
								}else 
									addWalkSteps(respawnTile.getX() + moveX, respawnTile.getY() + moveY, 5);
							}
						}
					}
				}
			}
		}
		setRandomWalk(false);   
		if(id == 4243)
            setRandomWalk(true);
                             if(id == 2676)
                                    setRandomWalk(false);
                             if(id == 949)
                                    setRandomWalk(false);
                             if(id == 550)
                                    setRandomWalk(false);
                             if(id == 549)
                                    setRandomWalk(false);
                             if(id == 546)
                                    setRandomWalk(false);
                             if(id == 683)
                                    setRandomWalk(false);
                             if(id == 6970)
                                    setRandomWalk(false);
                             if(id == 519)
                                    setRandomWalk(false);
                             if(id == 520)
                                    setRandomWalk(false);
                             if(id == 45)
                                    setRandomWalk(false);
                             if(id == 538)
                                    setRandomWalk(false);
                             if(id == 522)
                                    setRandomWalk(false);
                             if(id == 523)
                                    setRandomWalk(false);
                             if(id == 11475)
                                    setRandomWalk(false);
                             if(id == 551)
                                    setRandomWalk(false);
                             if(id == 5080)
                                 setRandomWalk(true);
                             if(id == 2998)
                                 setName("Highscores & Drop Logs");
                             if(id == 231)
                                 setName("Skilling Teleports");
                             if(id == 3373)
                                 setName("Training Teleports");
                             if(id == 410)
                                 setName("Quests & Minigames Teleports");
                             if(id == 549)
                                 setName("General Store");
                             if(id == 522)
                                 setName("Skillcape Shop");
                             if(id == 548)
                                 setName("Skiller Outfits");
                             if(id == 3709)
                                 setName("Monster Teleports");
                             if(id == 551)
                                 setName("Armour Supplies");
                             if(id == 11475)
                                 setName("Magic Supplies");
                             if(id == 546)
                                 setName("Runecrafting & Construction Supplies");
                             if(id == 538) {
                                 setName("Pure Supplies");
                             }
                             if(id == 529) {
                                 setName("Herblore Shop");
                             }
                             if(id == 520)
                                 setName("Fishing Supplies");
                             if(id == 519)
                                 setName("Skilling Supplies");
                             if(id == 550)
                                 setName("Range Supplies");
                             if(id == 555)
                                 setName("Summoning Supplies");
                             if(id == 556)
                                 setName("Trivia Store");
                             if(id == 552)
                                 setName("Donator's Potions");
                             if(id == 2253)
                                 setName("Prestige Master");
                             if(id == 2323)
                                 setName("Farming Supplies");
                             if(id == 7557)
                                 setName("Farming Teleports");
                             if(id == 3021)
                                 setName("Farming Teleports");
                             if(id == 7559)
                                 setName("Farming Teleports");
                             if(id == 2324)
                                 setName("Farming Supplies");
                             if(id == 2340)
                                 setName("Farming Supplies");
		if (isForceWalking()) {
			if (getFreezeDelay() < Utils.currentTimeMillis()) {
				if (getX() != forceWalk.getX() || getY() != forceWalk.getY()) {
					if (!hasWalkSteps())
						addWalkSteps(forceWalk.getX(), forceWalk.getY(),
								getSize(), true);
					if (!hasWalkSteps()) { // failing finding route
						setNextWorldTile(new WorldTile(forceWalk)); // force
						// tele
						// to
						// the
						// forcewalk
						// place
						forceWalk = null; // so ofc reached forcewalk place
					}
				} else
					// walked till forcewalk place
					forceWalk = null;
			}
		}
	}

	@Override
	public void processEntity() {
		super.processEntity();
		processNPC();
	}

	public int getRespawnDirection() {
		NPCDefinitions definitions = getDefinitions();
		if (definitions.anInt853 << 32 != 0 && definitions.respawnDirection > 0
				&& definitions.respawnDirection <= 8)
			return (4 + definitions.respawnDirection) << 11;
		return 0;
	}

	/*
	 * forces npc to random walk even if cache says no, used because of fake
	 * cache information
	 */
	private static boolean forceRandomWalk(int npcId) {
		switch (npcId) {
		case 11226:
			return true;
		case 3341:
		case 3342:
		case 3343:
			return true;
		default:
			return false;
			/*
			 * default: return NPCDefinitions.getNPCDefinitions(npcId).name
			 * .equals("Icy Bones");
			 */
		}
	}
	
	public void sendSoulSplit(final Hit hit, final Entity user) {
		final NPC target = this;
		if (hit.getDamage() > 0)
			World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		user.heal(hit.getDamage() / 5);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0)
					World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0,
							0);
			}
		}, 1);
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (capDamage != -1 && hit.getDamage() > capDamage)
			hit.setDamage(capDamage);
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		Entity source = hit.getSource();
		if (source == null)
			return;
		if (source instanceof Player) {
			final Player p2 = (Player) source;
			if (p2.getPrayer().hasPrayersOn()) {
				if (p2.getPrayer().usingPrayer(1, 18)) 
					sendSoulSplit(hit, p2);
				if (hit.getDamage() == 0)
					return;
				if (!p2.getPrayer().isBoostedLeech()) {
					if (hit.getLook() == HitLook.MELEE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 19)) {
							p2.getPrayer().setBoostedLeech(true);
							return;
						} else if (p2.getPrayer().usingPrayer(1, 1)) { // sap
							// att
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(0)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(0);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Attack from the enemy, boosting your Attack.",
											true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2214));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2215, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2216));
									}
								}, 1);
								return;
							}
						} else {
							if (p2.getPrayer().usingPrayer(1, 10)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(3)) {
										p2.getPackets()
										.sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(3);
										p2.getPackets()
										.sendGameMessage(
												"Your curse drains Attack from the enemy, boosting your Attack.",
												true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, this, 2231, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2232));
										}
									}, 1);
									return;
								}
							}
							if (p2.getPrayer().usingPrayer(1, 14)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(7)) {
										p2.getPackets()
										.sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(7);
										p2.getPackets()
										.sendGameMessage(
												"Your curse drains Strength from the enemy, boosting your Strength.",
												true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, this, 2248, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2250));
										}
									}, 1);
									return;
								}
							}

						}
					}
					if (hit.getLook() == HitLook.RANGE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 2)) { // sap range
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(1)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(1);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Range from the enemy, boosting your Range.",
											true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2217));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2218, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2219));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 11)) {
							if (Utils.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(4)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(4);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Range from the enemy, boosting your Range.",
											true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2236, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2238));
									}
								});
								return;
							}
						}
					}
					if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 3)) { // sap mage
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(2)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(2);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Magic from the enemy, boosting your Magic.",
											true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2220));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2221, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2222));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 12)) {
							if (Utils.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(5)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(5);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Magic from the enemy, boosting your Magic.",
											true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2240, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2242));
									}
								}, 1);
								return;
							}
						}
					}

					// overall

					if (p2.getPrayer().usingPrayer(1, 13)) { // leech defence
						if (Utils.getRandom(10) == 0) {
							if (p2.getPrayer().reachedMax(6)) {
								p2.getPackets()
								.sendGameMessage(
										"Your opponent has been weakened so much that your leech curse has no effect.",
										true);
							} else {
								p2.getPrayer().increaseLeechBonus(6);
								p2.getPackets()
								.sendGameMessage(
										"Your curse drains Defence from the enemy, boosting your Defence.",
										true);
							}
							p2.setNextAnimation(new Animation(12575));
							p2.getPrayer().setBoostedLeech(true);
							World.sendProjectile(p2, this, 2244, 35, 35, 20, 5,
									0, 0);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									setNextGraphics(new Graphics(2246));
								}
							}, 1);
							return;
						}
					}
				}
			}
		}

	}

	@Override
	public void reset() {
		super.reset();
		setDirection(getRespawnDirection());
		combat.reset();
		defenceDrainTicks = -1;
		bonuses = NPCBonuses.getBonuses(id); // back to real bonuses
		forceWalk = null;
	}

	@Override
	public void finish() {
		if (hasFinished())
			return;
		setFinished(true);
		World.updateEntityRegion(this);
		World.removeNPC(this);
	}

	// usually called when bgs special attack is used on npc
	public void drainDefence(Player player, int drainAmount, int weaponId, int attackStyle) { ////--

		int npcDefenceBonus = this.getBonuses() != null ? this.getBonuses()[CombatDefinitions.getMeleeDefenceBonus(CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle))] : 0;
		final int ratioDrainAmount = (int) (((drainAmount * (npcDefenceBonus * 9)) / 100) / 10);

		if(defenceDrainTicks <= 0) {

			// player.getPackets().sendGameMessage("getting ready to drain defence by " + ratioDrainAmount + ". (normal drain on player would have been " + drainAmount + ")");

			final int finalDrainAmount = ratioDrainAmount;
			final NPC finalNPC = this;
			final Player finalPlayer = player;

			WorldTasksManager.schedule(new WorldTask() {

				int thisDrainAmount = finalDrainAmount;
				NPC thisNPC = finalNPC;
				boolean started = false;

				int ticks = 0;

				@Override
				public void run() {
					
					if(!started) {
						started = true;
						finalNPC.defenceDrainTicks = thisDrainAmount;
					}

					if(thisNPC.isDead()) {
						// finalPlayer.getPackets().sendGameMessage("The npc has died.");
						started = false;
						thisNPC.defenceDrainTicks = -1;
						stop();
						return;
					}

					if(thisNPC.defenceDrainTicks <= 0) {
						// finalPlayer.getPackets().sendGameMessage("Stopping");
						started = false;
						thisNPC.defenceDrainTicks = -1;
						stop();
						return;
					}

					if(ticks % 60 == 0) {

						// finalPlayer.getPackets().sendGameMessage("Tick " + thisNPC.defenceDrainTicks);
						thisNPC.defenceDrainTicks--;
					}

					ticks++;

				}

			}, 0, 1);

			return;
		}

		// assume defence already drained, drain again
		defenceDrainTicks += ratioDrainAmount;

	}

	// takes into account drained defence
	public int getDrainedDefenceModifier() {
		return this.defenceDrainTicks <= 0 ? 0 : this.defenceDrainTicks;
	}

	public void setRespawnTask() {
		if (!hasFinished()) {
			reset();
			setLocation(respawnTile);
			finish();
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawn();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, getCombatDefinitions().getRespawnDelay() * 600,
		TimeUnit.MILLISECONDS);
	}

	public void deserialize() {
		if (combat == null)
			combat = new NPCCombat(this);
		spawn();
	}

	public void spawn() {
		setFinished(false);
		World.addNPC(this);
		setLastRegionId(0);
		World.updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
	}

	public NPCCombat getCombat() {
		return combat;
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		combat.removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					drop();
					reset();
					setLocation(respawnTile);
					finish();
					if (!isSpawned())
						setRespawnTask();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
		
	
	public void drop() {
		try {
			Drop[] drops = NPCDrops.getDrops(id);
			if (drops == null)
				return;
			Player killer = getMostDamageReceivedSourcePlayer();
			if (killer == null)
				return;
			SlayerTask task = killer.getSlayerTask();
			if (this.getId() == 3375) {
				killer.randomEventPoints++;
				killer.getPackets().sendGameMessage("You kill the evil chicken and recieve 1 Random Event point. You now have: " +killer.randomEventPoints+ " points.");
			}
			if (this.getId() == 10360) {
				if (killer.getSkills().getLevel(24) == 1) {
					killer.getSkills().addXp(Skills.DUNGEONEERING, 1);
				}
			killer.getSkills().addXp(Skills.DUNGEONEERING, killer.getSkills().getLevel(24) * 2);
			killer.getInventory().addItem(12852, 100);
			}
			if (this.getId() == 10360) {
				if (killer.isDonator() || killer.isExtremeDonator()) {
				killer.getSkills().addXp(Skills.DUNGEONEERING, killer.getSkills().getLevel(24) * 2 + 10);
				killer.getInventory().addItem(12852, 100);
			}
			}
			if (task != null) {
				if (task.getName().contains(getDefinitions().name)) {
					Slayer.killedTask(killer, this);
				}
			}
			Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (Drop drop : drops) {
				Item item = ItemDefinitions.getItemDefinitions(drop.getItemId()).isStackable() ? new Item(drop.getItemId(),
						(drop.getMinAmount() * Settings.DROP_RATE)
								+ Utils.getRandom(drop.getExtraAmount()
										* Settings.DROP_RATE)) : new Item(
						drop.getItemId(), drop.getMinAmount()
								+ Utils.getRandom(drop.getExtraAmount()));
					if(killer.getInventory().containsItem(18337, 1) && item.getDefinitions().getName().toLowerCase().contains("bones")) {
						killer.getSkills().addXp(Skills.PRAYER, Burying.Bone.forId(drop.getItemId()).getExperience());
						killer.boneCrusher++;
						killer.checkBoneCrusher();
						if (killer.getEquipment().getAmuletId() == 19887) {
						killer.getPrayer().setPrayerpoints(killer.getPrayer().getPrayerpoints() + 100);
						killer.getPrayer().refreshPrayerPoints();
						}
						continue;
				}
					/*if (killer.getEquipment().getAmuletId() == 19887 && drop.getItemId() == 526 || drop.getItemId() == 528 || drop.getItemId() == 530) {
						killer.out("Bones 1");
						return;
					} else if (killer.getEquipment().getAmuletId() == 19887 && drop.getItemId() == 532 || drop.getItemId() == 7839 || drop.getItemId() == 6812 || drop.getItemId() == 536 || drop.getItemId() == 18830 || drop.getItemId() == 4834) {
						killer.out("Bones 2");
						return;
					}
					if (this.getId() == 116 || this.getId() == 4291 || this.getId() == 6078 || this.getId() == 6080 && drop.getItemId() == 8844) {
						WGuildControler.dropDefender(killer, this);
						return;
						}
					if (this.getId() == 4284) {
						killer.out("You receive 10 Warriors Guild Points, You are now on "+killer.getWGuildTokens()+" warriors guild tokens.");
						killer.wGuildTokens +=10;
						killer.getInventory().addItem(1127, 1);
						killer.getInventory().addItem(1079, 1);
						killer.getInventory().addItem(1163, 1);
						return;
						}*/
				if (drop.getRate() == 100)
					sendDrop(killer, drop);
				else {
					if ((Utils.getRandomDouble(99) + 1) <= drop.getRate() * 1.5)
						possibleDrops[possibleDropsCount++] = drop;
				}
			}
			if (possibleDropsCount > 0)
				sendDrop(killer, possibleDrops[Utils.getRandom(possibleDropsCount - 1)]);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}

	public void sendDrop(Player player, Drop drop) {
		int size = getSize();
		String dropName = ItemDefinitions.getItemDefinitions(drop.getItemId())
				.getName().toLowerCase();
		Item item = 
				ItemDefinitions.getItemDefinitions(drop.getItemId()).isStackable() ?
						new Item(drop.getItemId(), (drop.getMinAmount() * Settings.DROP_RATE) + Utils.getRandom(drop.getExtraAmount() *Settings.DROP_RATE))
		:
			new Item(drop.getItemId(), drop.getMinAmount() + Utils.getRandom(drop.getExtraAmount()));
						World.addGroundItem(item, new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane()), player, false, 180, true);
                    if (dropName.contains("pernix") 
                    || dropName.contains("torva")
                    || dropName.contains("virtus")
                    || dropName.contains("armadyl")
                    || dropName.contains("steadfast")
                    || dropName.contains("spirit shield")
                    || dropName.contains("ragefire")
                    || dropName.contains("dragonfire")
                    || dropName.contains("dragon claws")
                    || dropName.contains("glaiven")
                    || dropName.contains("saradomin")
                    || dropName.contains("zamorak")
                    || dropName.contains("vesta's")
                    || dropName.contains("statius's")
                    || dropName.contains("morrigan's")
                    || dropName.contains("zuriel's")
                    || dropName.contains("fury")
                    || dropName.contains("polypore")
                    || dropName.contains("ganodermic")
                    || dropName.contains("bandos")) {

                    	char firstChar = dropName.toLowerCase().charAt(0);
                    	String article = "a";

                    	if(firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u') {
                    		article = "an";
                    	}

                    	World.sendWorldMessage("<img=5>Drop Feed<col=FF0000> " + player.getDisplayName() + "</col> just recieved " + article +" <col=FF0000>"+ dropName +"</col> drop!", false);
                    }
                    /**
                     * Drop Log System
                     */
                    switch (drop.getItemId()) {
                    /*
                     * Bandos
                     */
                    case 11724://Bandos Chest
                    	player.bandosChest++;
                    break;
                    case 11726://Bandos Tassets
                    	player.bandosTassets++;
                    break;
                    case 11704://Bandos hilt
                    	player.bandosHilt++;
                    break;
                    /*
                     * Armadyl
                     */
                    case 11718://Armadyl Helm
                    	player.armadylHelm++;
                    break;
                    case 11720://Armadyl chest
                    	player.armadylPlate++;
                    break;
                    case 11722://Armadyl Legs
                    	player.armadylLegs++;
                    break;
                    case 11702://Armadyl Hilt
                    	player.armadylHilt++;
                    break;
                    /*
                     * Torva
                     */
                    case 20135:
                    	player.torvaHelm++;
                    	break;
                    case 20139:
                    	player.torvaPlate++;
                    	break;
                    case 20143:
                    	player.torvaLegs++;
                    	break;
                    /*
                     * Pernix
                     */
                    case 20147:
                    	player.pernixCowl++;
                    	break;
                    case 20151:
                    	player.pernixBody++;
                    	break;
                    case 20155:
                    	player.pernixChaps++;
                    	break;
                    	/*
                    	 * Virtus
                    	 */
                    case 20159:
                    	player.virtusMask++;
                    	break;
                    case 20163:
                    	player.virtusTop++;
                    	break;
                    case 20167:
                    	player.virtusLegs++;
                    	break;
                    	//Dragon Claws
                    case 14484:
                    	player.dragonClaws++;
                    	break;
                    	/*
                    	 * Dharok
                    	 */
                    case 4716:
                    	player.dharoksHelm++;
                    	break;
                    case 4718:
                    	player.dharoksGreataxe++;
                    	break;
                    case 4720:
                    	player.dharoksPlate++;
                    	break;
                    case 4722:
                    	player.dharoksLegs++;
                    	break;
                    }
	}

	@Override
	public int getSize() {
		return getDefinitions().size;
	}

	public int getMaxHit() {
		return getCombatDefinitions().getMaxHit();
	}

	public int[] getBonuses() {
		return bonuses;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0;
	}

	public WorldTile getRespawnTile() {
		return respawnTile;
	}

	public boolean isUnderCombat() {
		return combat.underCombat();
	}

	@Override
	public void setAttackedBy(Entity target) {
		super.setAttackedBy(target);
		if (target == combat.getTarget()
				&& !(combat.getTarget() instanceof Familiar))
			lastAttackedByTarget = Utils.currentTimeMillis();
	}

	public boolean canBeAttackedByAutoRelatie() {
		return Utils.currentTimeMillis() - lastAttackedByTarget > lureDelay;
	}

	public boolean isForceWalking() {
		return forceWalk != null;
	}

	public void setTarget(Entity entity) {
		if (isForceWalking()) // if force walk not gonna get target
			return;
		combat.setTarget(entity);
		lastAttackedByTarget = Utils.currentTimeMillis();
	}

	public void removeTarget() {
		if (combat.getTarget() == null)
			return;
		combat.removeTarget();
	}

	public void forceWalkRespawnTile() {
		setForceWalk(respawnTile);
	}

	public void setForceWalk(WorldTile tile) {
		resetWalkSteps();
		forceWalk = tile;
	}

	public boolean hasForceWalk() {
		return forceWalk != null;
	}

	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId)
					.getPlayerIndexes();
			if (playerIndexes != null) {
				for (int playerIndex : playerIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null
							|| player.isDead()
							|| player.hasFinished()
							|| !player.isRunning()
							|| !player
							.withinDistance(
									this,
									forceTargetDistance > 0 ? forceTargetDistance
											: (getCombatDefinitions()
													.getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
															: getCombatDefinitions()
															.getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 64
																	: 8))
																	|| (!forceMultiAttacked
																			&& (!isAtMultiArea() || !player
																					.isAtMultiArea())
																					&& player.getAttackedBy() != this && (player
																							.getAttackedByDelay() > Utils.
																							currentTimeMillis() || player
																							.getFindTargetDelay() > Utils
																							.currentTimeMillis()))
																							|| !clipedProjectile(player, false)
																							|| (!forceAgressive && !Wilderness.isAtWild(this) && player
																									.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2))
						continue;
					possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}

	public boolean checkAgressivity() {
		// if(!(Wilderness.isAtWild(this) &&
		// getDefinitions().hasAttackOption())) {
		if (!forceAgressive) {
			NPCCombatDefinitions defs = getCombatDefinitions();
			if (defs.getAgressivenessType() == NPCCombatDefinitions.PASSIVE)
				return false;
		}
		// }
		ArrayList<Entity> possibleTarget = getPossibleTargets();
		if (!possibleTarget.isEmpty()) {
			Entity target = possibleTarget.get(Utils.random(possibleTarget.size()));
			setTarget(target);
			target.setAttackedBy(target);
			target.setFindTargetDelay(Utils.currentTimeMillis() + 10000);
			return true;
		}
		return false;
	}

	public boolean isCantInteract() {
		return cantInteract;
	}

	public void setCantInteract(boolean cantInteract) {
		this.cantInteract = cantInteract;
		if (cantInteract)
			combat.reset();
	}

	public int getCapDamage() {
		return capDamage;
	}

	public void setCapDamage(int capDamage) {
		this.capDamage = capDamage;
	}

	public int getLureDelay() {
		return lureDelay;
	}

	public void setLureDelay(int lureDelay) {
		this.lureDelay = lureDelay;
	}

	public boolean isCantFollowUnderCombat() {
		return cantFollowUnderCombat;
	}

	public void setCantFollowUnderCombat(boolean canFollowUnderCombat) {
		this.cantFollowUnderCombat = canFollowUnderCombat;
	}

	public Transformation getNextTransformation() {
		return nextTransformation;
	}

	@Override
	public String toString() {
		return getDefinitions().name + " - " + id + " - " + getX() + " "
				+ getY() + " " + getPlane();
	}

	public boolean isForceAgressive() {
		return forceAgressive;
	}

	public void setForceAgressive(boolean forceAgressive) {
		this.forceAgressive = forceAgressive;
	}

	public int getForceTargetDistance() {
		return forceTargetDistance;
	}

	public void setForceTargetDistance(int forceTargetDistance) {
		this.forceTargetDistance = forceTargetDistance;
	}

	public boolean isForceFollowClose() {
		return forceFollowClose;
	}

	public void setForceFollowClose(boolean forceFollowClose) {
		this.forceFollowClose = forceFollowClose;
	}

	public boolean isForceMultiAttacked() {
		return forceMultiAttacked;
	}

	public void setForceMultiAttacked(boolean forceMultiAttacked) {
		this.forceMultiAttacked = forceMultiAttacked;
	}

	public boolean hasRandomWalk() {
		return randomwalk;
	}

	public void setRandomWalk(boolean forceRandomWalk) {
		this.randomwalk = forceRandomWalk;
	}

	public String getCustomName() {
		return name;
	}

	public void setName(String string) {
		this.name = getDefinitions().name.equals(string) ? null : string;
		changedName = true;
	}

	public int getCustomCombatLevel() {
		return combatLevel;
	}

	public int getCombatLevel() {
		return combatLevel >= 0 ? combatLevel : getDefinitions().combatLevel;
	}

	public String getName() {
		return name != null ? name : getDefinitions().name;
	}

	public void setCombatLevel(int level) {
		combatLevel  = getDefinitions().combatLevel == level ? -1 : level;
		changedCombatLevel = true;
	}

	public boolean hasChangedName() {
		return changedName;
	}

	public boolean hasChangedCombatLevel() {
		return changedCombatLevel;
	}

	public WorldTile getMiddleWorldTile() {
		int size = getSize();
		return new WorldTile(getCoordFaceX(size),getCoordFaceY(size), getPlane());
	}

	public boolean isSpawned() {
		return spawned;
	}

	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	public boolean isNoDistanceCheck() {
		return noDistanceCheck;
	}

	public void setNoDistanceCheck(boolean noDistanceCheck) {
		this.noDistanceCheck = noDistanceCheck;
	}
	
	public boolean withinDistance(Player tile, int distance) {
		return super.withinDistance(tile, distance);
	}

	/**
	 * Gets the locked.
	 * @return The locked.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Sets the locked.
	 * @param locked The locked to set.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
