package com.rs.game.player.content;

import java.io.Serializable;

import com.rs.game.Animation;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * @author <Prometheus Development Team>
 * 
 */
public class Cannon implements Serializable
{

    /**
     * Dwarf Multi Cannon Object Ids: Broken = 5, Full-Cannon = 6, Cannon Base =
     * 7, Base+Stand = 8, Base+Stand+Barrels = 9, Base+Stand+Barrels+Furnace =
     * 10
     * 
     * Dwarf Cannon Movement Ids: North = 303 North East = 305 East = 307 South
     * East - 289 South = 184 South West 182 West = 178 North West = 291
     * 
     * Cannon Ball Graphics Id: 53
     * 
     * Dwarf Multi Cannon Item Ids: CannonBall = 2, Base = 6, Stand = 8, Barrels
     * = 10, Furnace = 12
     */

    /**
	 * 
	 */
    private static final long serialVersionUID = 6100930614455400025L;

    public int[] CANNON = { 6, 7, 8, 9 };
    public int[] GOLD_ITEMS = { 20494, 20495, 20496, 20497 };
    public int[] ROYAL_ITEMS = { 20498, 20499, 20500, 20501 };
    public int[] ITEMS = { 6, 8, 10, 12, 2 };
    private WorldObject lastObject;
    private int cannonBalls = 0;
    private boolean first = true;
    private Player player;
    private boolean hasCannon = false;
    private boolean isFiring = false;
    private int cannonDirection;
    private boolean loadedOnce = false;
    private boolean rotating;
    private boolean settingUp = false;
    private WorldObject object;

    /**
     * Initializes the player variable
     * 
     * @param player
     */
    public Cannon(Player player)
    {
	this.player = player;
    }

    /**
     * Cannon setup
     */
    public void cannonSetup()
    {
	if (hasCannon()) {
	    getPlayer().sm("You already have a cannon setup.");
	    return;
	}
	if (!getPlayer().getInventory().containsItems(
		new Item[] { new Item(ITEMS[0]), new Item(ITEMS[1]),
			new Item(ITEMS[2]), new Item(ITEMS[3]) })) {
	    getPlayer()
		    .sm("You do not have all the required items to set up the Dwarf Multi-Cannon.");
	    return;
	}
	getPlayer().setNextAnimation(new Animation(827));
	setCannon(true);
	setSettingUp(true);
	WorldTasksManager.schedule(new WorldTask()
	{
	    int count = 0;

	    @Override
	    public void run()
	    {
		switch (count) {
		case 0:
		    getPlayer().lock();
		    setLastObject(new WorldObject(CANNON[1], 10, 0, player));
		    World.spawnObject(getLastObject(), false);
		    getPlayer()
			    .sm("You place the cannon base on the ground...");
		    getPlayer().getInventory().deleteItem(ITEMS[0], 1);
		    break;
		case 1:
		    World.removeObject(getLastObject(), false);
		    World.getRegion(getLastObject().getRegionId())
			    .removeObject(getLastObject());
		    setLastObject(new WorldObject(CANNON[2], 10, 0, player));
		    World.spawnObject(getLastObject(), false);
		    getPlayer().sm("You add the stand...");
		    getPlayer().getInventory().deleteItem(ITEMS[1], 1);
		    break;
		case 2:
		    World.removeObject(getLastObject(), false);
		    World.getRegion(getLastObject().getRegionId())
			    .removeObject(getLastObject());
		    setLastObject(new WorldObject(CANNON[3], 10, 0, player));
		    World.spawnObject(getLastObject(), false);
		    getPlayer().sm("You add the barrel...");
		    getPlayer().getInventory().deleteItem(ITEMS[2], 1);
		    break;
		case 3:
		    World.removeObject(getLastObject(), false);
		    World.getRegion(getLastObject().getRegionId())
			    .removeObject(getLastObject());
		    setLastObject(new WorldObject(CANNON[0], 10, 0, player));
		    World.spawnObject(getLastObject(), false);
		    setObject(getLastObject());
		    setSettingUp(false);
		    getPlayer().sm("You add the furnace...");
		    getPlayer().getInventory().deleteItem(ITEMS[3], 1);
		    getPlayer().unlock();
		    this.stop();
		    break;
		}
		getPlayer().setNextAnimation(new Animation(827));
		count++;
	    }

	}, 0, 1);
    }

    /**
     * Pre-rotation setup check
     * 
     * @param object
     */
    public void preRotationSetup(WorldObject object)
    {
	if (getObject() != object) {
	    getPlayer().sm("You are not the owner of this Dwarf Cannon.");
	    return;
	}
	if (isRotating()) {
	    getPlayer().sm("Your cannon is already firing!");
	    return;
	}
	if (getCannonBalls() < 1) {
	    getPlayer().sm("Your cannon has no ammo left!");
	    setFiring(false);
	    setRotating(false);
	    return;
	}
	if (isFirst() == false) {
	    setFirst(true);
	}
	setRotating(true);
	startRotation(object);
    }

    /**
     * Starts the rotation after pre-setup
     * 
     * @param object
     */
    public void startRotation(final WorldObject object)
    {
	WorldTasksManager.schedule(new WorldTask()
	{
	    int count = (hasLoadedOnce() == true ? getCannonDirection() : 0);

	    @Override
	    public void run()
	    {
		if (isRotating() == false) {
		    this.stop();
		} else if (isRotating() == true) {
		    switch (count) {
		    case 0:
			if (isFirst()) {
			    setLoadedOnce(true);
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(305));
			} else {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(303));
			}
			setCannonDirection(0);
			break;
		    case 1:
			if (isFirst()) {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(307));
			} else {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(305));
			}
			setCannonDirection(1);
			break;
		    case 2:
			if (isFirst()) {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(289));
			} else {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(307));
			}
			setCannonDirection(2);
			break;
		    case 3:
			if (isFirst()) {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(184));
			} else {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(289));
			}
			setCannonDirection(3);
			break;
		    case 4:
			if (isFirst()) {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(182));
			} else {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(184));
			}
			setCannonDirection(4);
			break;
		    case 5:
			if (isFirst()) {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(178));
			} else {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(182));
			}
			setCannonDirection(5);
			break;
		    case 6:
			if (isFirst()) {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(291));
			} else {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(178));
			}
			setCannonDirection(6);
			break;
		    case 7:
			if (isFirst()) {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(303));
			    setFirst(false);
			} else {
			    World.sendObjectAnimation(getPlayer(), object,
				    new Animation(291));
			}
			setCannonDirection(7);
			count = -1;
			break;
		    }
		}
		count++;
		if (fireDwarfCannon(object)) {
		    if (!retainsCannonBalls()) {
			getPlayer().sm("Your cannon has ran out of ammo!");
			setFiring(false);
			setRotating(false);
			setLoadedOnce(false);
			setFirst(true);
			this.stop();
		    }
		}
	    }
	}, 0, 0);
    }

    /**
     * Picks up and removes the dwarf cannon from the game
     * 
     * @param stage
     * @param object
     */
    public void pickUpDwarfCannon(int stage, WorldObject object)
    {
	if (getObject() != object) {
	    getPlayer().sm("You are not the owner of this Dwarf Cannon.");
	    return;
	}
	if (isSettingUp()) {
	    getPlayer()
		    .sm("Please finish setting up your current cannon before picking it up.");
	    return;
	}
	if (stage == 0) {
	    getPlayer().getInventory().addItem(ITEMS[0], 1);
	    getPlayer().getInventory().addItem(ITEMS[1], 1);
	    getPlayer().getInventory().addItem(ITEMS[2], 1);
	    getPlayer().getInventory().addItem(ITEMS[3], 1);
	    if (getCannonBalls() < 1) {

	    } else {
		getPlayer().getInventory().addItem(2, cannonBalls);
	    }
	    setCannonBalls(0);
	    setRotating(false);
	    setCannon(false);
	    setFiring(false);
	    setLoadedOnce(false);
	    setFirst(true);
	    setCannonDirection(0);
	    setObject(null);
	    World.removeObject(getLastObject(), true);
	} else if (stage == 1 || stage == 2 || stage == 4) {

	}
    }

    /**
     * 
     * Loads the Dwarf Multicannon with ammunition
     */
    public void loadDwarfCannon(WorldObject object)
    {
	int ballsToAdd = 0;
	if (getObject() != object) {
	    getPlayer().sm("You are not the owner of this Dwarf Cannon.");
	    return;
	}
	if (getCannonBalls() == 0
		&& getPlayer().getInventory().containsItem(2, 30)) {
	    ballsToAdd = 30;
	    getPlayer().getInventory().deleteItem(2, ballsToAdd);
	    getPlayer().sm(
		    "You load the cannon with " + ballsToAdd + " cannonball"
			    + (ballsToAdd == 1 ? "" : "s") + ".");
	    this.cannonBalls += ballsToAdd;
	    setObject(object);
	}
	if (getCannonBalls() < 30
		&& getCannonBalls() < 1
		&& getPlayer().getInventory().containsItem(2,
			30 - getCannonBalls())) {
	    ballsToAdd = 30 - this.cannonBalls;
	    getPlayer().getInventory().deleteItem(2, ballsToAdd);
	    getPlayer().sm(
		    "You load the cannon with " + ballsToAdd + " cannonball"
			    + (ballsToAdd == 1 ? "" : "s") + ".");
	    this.cannonBalls += ballsToAdd;
	    setObject(object);
	}
    }

    /**
     * Fires the Dwarf MultiCannon
     */
    public boolean fireDwarfCannon(WorldObject object)
    {
	boolean hit = false;
	if (getCannonBalls() == 0) {
	    hit = false;
	    setFiring(false);
	    return false;
	}
	for (NPC n : World.getNPCs()) {
	    int damage = Utils.getRandom(300);
	    double combatXp = damage / 2.5;
	    int distanceX = n.getX() - object.getX();
	    int distanceY = n.getY() - object.getY();
	    if (n == null || n.isDead()
		    || !n.getDefinitions().hasAttackOption()) {
		continue;
	    }
	    switch (getCannonDirection()) {
	    case 0: // North
		if ((distanceY <= 8 && distanceY >= 0)
			&& (distanceX >= -1 && distanceX <= 1)) {
		    hit = true;
		}
		break;
	    case 1: // North East
		if ((distanceY <= 8 && distanceY >= 0)
			&& (distanceX <= 8 && distanceX >= 0)) {
		    hit = true;
		}
		break;
	    case 2: // East
		if ((distanceY <= 1 && distanceY >= -1)
			&& (distanceX <= 8 && distanceX >= 0)) {
		    hit = true;
		}
		break;
	    case 3: // South East
		if ((distanceY >= -8 && distanceY <= 0)
			&& (distanceX <= 8 && distanceX >= 0)) {
		    hit = true;
		}
		break;
	    case 4: // South
		if ((distanceY >= -8 && distanceY <= 0)
			&& (distanceX <= 1 && distanceX >= -1)) {
		    hit = true;
		}
		break;
	    case 5: // South West
		if ((distanceY >= -8 && distanceY <= 0)
			&& (distanceX >= -8 && distanceX <= 0)) {
		    hit = true;
		}
		break;
	    case 6: // West
		if ((distanceY >= -1 && distanceY <= 1)
			&& (distanceX >= -8 && distanceX <= 0)) {
		    hit = true;
		}
		break;
	    case 7: // North West
		if ((distanceY <= 8 && distanceY >= 0)
			&& (distanceX >= -8 && distanceX <= 0)) {
		    hit = true;
		}
		break;
	    default:
		hit = false;
		break;
	    }
	    if (hit) {
		this.cannonBalls -= 1;
		n.getCombat().setTarget(getPlayer());
		World.sendProjectile(getPlayer(), object, n, 53, 52, 52, 30, 0,
			0, 2);
		n.applyHit(new Hit(getPlayer(), damage, HitLook.CANNON_DAMAGE));
		getPlayer().getSkills().addXp(Skills.RANGE, combatXp / 2);
		return true;
	    }
	}
	return false;
    }

    /**
     * Returns the current direction of a player's cannon
     * 
     * @return
     */
    public int getCannonDirection()
    {
	return cannonDirection;
    }

    /**
     * Sets the direction of a player's cannon
     * 
     * @param cannonDirection
     */
    public void setCannonDirection(int cannonDirection)
    {
	this.cannonDirection = cannonDirection;
    }

    /**
     * Returns whether the player has loaded the cannon this setup before
     * 
     * @return
     */
    public boolean hasLoadedOnce()
    {
	return loadedOnce;
    }

    /**
     * Sets whether the player has loaded their cannon on the current setup
     * 
     * @param loadedOnce
     */
    public void setLoadedOnce(boolean loadedOnce)
    {
	this.loadedOnce = loadedOnce;
    }

    /**
     * Returns whether the player has cannonball's in their cannon
     * 
     * @return
     */
    public boolean retainsCannonBalls()
    {
	return cannonBalls > 0;
    }

    /**
     * Returns the rotation of the cannon
     * 
     * @return
     */
    public boolean isRotating()
    {
	return rotating;
    }

    /**
     * Sets the player's cannon rotation
     * 
     * @param rotating
     */
    public void setRotating(boolean rotating)
    {
	this.rotating = rotating;
    }

    /**
     * Returns whether the player has a cannon or not
     * 
     * @return
     */
    public boolean hasCannon()
    {
	return hasCannon;
    }

    /**
     * Sets the player's cannon
     * 
     * @param hasCannon
     */
    public void setCannon(boolean hasCannon)
    {
	this.hasCannon = hasCannon;
    }

    /**
     * Returns true if it's the first shot, or false otherwise
     * 
     * @return
     */
    public boolean isFirst()
    {
	return first;
    }

    /**
     * Sets whether it's the player's first shot or not
     * 
     * @param first
     */
    public void setFirst(boolean first)
    {
	this.first = first;
    }

    /**
     * Returns the current player
     * 
     * @return
     */
    public Player getPlayer()
    {
	return player;
    }

    /**
     * Sets the player the class belongs to
     * 
     * @param player
     */
    public void setPlayer(Player player)
    {
	this.player = player;
    }

    /**
     * Returns true if the player's cannon is firing or not
     * 
     * @return
     */
    public boolean isFiring()
    {
	return isFiring;
    }

    /**
     * Sets whether the player's cannon is firing or not
     * 
     * @param isFiring
     */
    public void setFiring(boolean isFiring)
    {
	this.isFiring = isFiring;
    }

    /**
     * Returns the last object set
     * 
     * @return
     */
    public WorldObject getLastObject()
    {
	return lastObject;
    }

    /**
     * Sets the last object set
     * 
     * @param lastObject
     */
    public void setLastObject(WorldObject lastObject)
    {
	this.lastObject = lastObject;
    }

    /**
     * Returns true if the player is setting up and false otherwise
     * 
     * @return
     */
    public boolean isSettingUp()
    {
	return settingUp;
    }

    /**
     * Sets whether the player is setting up or not
     * 
     * @param settingUp
     */
    public void setSettingUp(boolean settingUp)
    {
	this.settingUp = settingUp;
    }

    /**
     * Returns the amount of cannonball's the player has in their cannon
     * 
     * @return
     */
    public int getCannonBalls()
    {
	return cannonBalls;
    }

    /**
     * Sets the amount of cannonball's in a player's cannon
     * 
     * @param cannonBalls
     */
    public void setCannonBalls(int cannonBalls)
    {
	this.cannonBalls = cannonBalls;
    }

    /**
     * Returns the player's object
     * 
     * @return
     */
    public WorldObject getObject()
    {
	return object;
    }

    /**
     * Sets the player's object
     * 
     * @param object
     */
    public void setObject(WorldObject object)
    {
	this.object = object;
    }
}