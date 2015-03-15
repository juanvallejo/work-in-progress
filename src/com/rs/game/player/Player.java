package com.rs.game.player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.RegionBuilder;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.ItemsContainer;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.minigames.clanwars.FfaZone;
import com.rs.game.minigames.clanwars.WarControler;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelRules;
import com.rs.game.minigames.pest.PestControl;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.godwars.zaros.Nex;
import com.rs.game.npc.pet.Pet;
import com.rs.game.npc.qbd.QueenBlackDragon;
//import com.rs.game.player.Trade.CloseTradeStage;
import com.rs.game.player.actions.PlayerCombat;
import com.rs.game.player.actions.Slayer.Master;
import com.rs.game.player.actions.Slayer.SlayerMonsters;
import com.rs.game.player.actions.Slayer.SlayerTask;
import com.rs.game.player.actions.Slayer;
import com.rs.game.player.content.CanafisFarming;
import com.rs.game.player.content.Cannon;
import com.rs.game.player.content.DwarfCannon;
import com.rs.game.player.content.FadingScreen;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.Notes;
import com.rs.game.player.content.Pots;
import com.rs.game.player.content.Shop;
import com.rs.game.player.content.SquealOfFortune;
import com.rs.game.player.content.SkillCapeCustomizer;
import com.rs.game.player.content.construction.House;
import com.rs.game.player.content.construction.PlayerHouseSaving;
import com.rs.game.player.content.construction.Room;
import com.rs.game.player.content.construction.RoomConstruction;
import com.rs.game.player.content.construction.RoomReference;
import com.rs.game.player.content.pet.PetManager;
import com.rs.game.player.controlers.CorpBeastControler;
import com.rs.game.player.controlers.CrucibleControler;
import com.rs.game.player.controlers.DTControler;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.FightKiln;
import com.rs.game.player.controlers.GodWars;
import com.rs.game.player.controlers.HouseControler;
import com.rs.game.player.controlers.NomadsRequiem;
import com.rs.game.player.controlers.QueenBlackDragonController;
import com.rs.game.player.controlers.RubberChicken;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.controlers.ZGDControler;
import com.rs.game.player.controlers.castlewars.CastleWarsPlaying;
import com.rs.game.player.controlers.castlewars.CastleWarsWaiting;
import com.rs.game.player.controlers.fightpits.FightPitsArena;
import com.rs.game.player.controlers.pestcontrol.PestControlGame;
import com.rs.game.player.controlers.pestcontrol.PestControlLobby;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.Session;
import com.rs.net.decoders.LoginPacketsDecoder;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.net.decoders.handlers.ButtonHandler;
import com.rs.net.encoders.WorldPacketsEncoder;
import com.rs.utils.FarmSpawn;
import com.rs.utils.IsaacKeyPair;
import com.rs.utils.Logger;
import com.rs.utils.MachineInformation;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Starter;
import com.rs.utils.Text;
import com.rs.utils.Utils;
import com.rs.utils.VoteManager;
import com.rs.utils.Watch;
import com.rs.game.player.content.WarriorsGuild;
import com.rs.game.player.content.ShootingStar;

import org.runetoplist.*;

public class Player extends Entity {

	public static final int TELE_MOVE_TYPE = 127, WALK_MOVE_TYPE = 1,
			RUN_MOVE_TYPE = 2;

	private static final long serialVersionUID = 2011932556974180375L;

	public static VoteChecker voteChecker = new VoteChecker("212.1.210.242:3306", "ridicul6_vote", "ridicul6_vote", "darby123");
	
	
	// transient stuff
	private transient String username;
	private transient Session session;
	private transient boolean clientLoadedMapRegion;
	private transient int displayMode;
	private transient int screenWidth;
	private transient int screenHeight;
	private transient InterfaceManager interfaceManager;
	private transient DialogueManager dialogueManager;
	private transient HintIconsManager hintIconsManager;
	private transient ActionManager actionManager;
	private transient CutscenesManager cutscenesManager;
	private transient PriceCheckManager priceCheckManager;
	private transient CoordsEvent coordsEvent;
	private transient FriendChatsManager currentFriendChat;
	private transient Trade trade;
	private transient DuelRules lastDuelRules;
	private transient IsaacKeyPair isaacKeyPair;
	private transient Pet pet;
	private transient House house;
	private transient Room rooms;
	private transient HouseControler houseControler;
	private transient NPC npc;
	private transient DwarfCannon DwarfCannon;
	private transient Cannon Cannon;
	private transient Watch Watch;
	private transient CanafisFarming canafis;
	private transient WarriorsGuild WarriorsGuild;
	private transient ShootingStar ShootingStar;
	private transient HouseControler HouseControler;
	
	
	public int farmingTimer;
	
	//private FarmingManager fm;

	// used for packets logic
	private transient ConcurrentLinkedQueue<LogicPacket> logicPackets;

	// used for update
	private transient LocalPlayerUpdate localPlayerUpdate;
	private transient LocalNPCUpdate localNPCUpdate;

	private int temporaryMovementType;
	private boolean updateMovementType;

	// player stages
	private transient boolean started;
	private transient boolean running;

	private transient long packetsDecoderPing;
	private transient boolean resting;
	private transient boolean canPvp;
	private transient boolean cantTrade;
	private transient long lockDelay; // used for doors and stuff like that
	private transient long foodDelay;
	private transient long potDelay;
	private transient long boneDelay;
	private transient long voteDelay;
	private transient Runnable closeInterfacesEvent;
	private transient long lastPublicMessage;
	private transient long polDelay;
	private transient List<Integer> switchItemCache;
	private transient boolean disableEquip;
	private transient MachineInformation machineInformation;
	private transient boolean spawnsMode;
	private transient boolean castedVeng;
	private transient boolean invulnerable;
	private transient double hpBoostMultiplier;
	private transient boolean largeSceneView;

	// interface

	// saving stuff
	private String password;
	private int rights;
	private boolean isHiddenAdmin;
	private String displayName;
	private String lastIP;
	private long creationDate;
	private Appearence appearence;
	public Inventory inventory;
	public int votingPoints;
	private Equipment equipment;
	private Skills skills;
	private CombatDefinitions combatDefinitions;
	private Prayer prayer;
	private Master master;
	private SlayerTask slayerTask;
	private Bank bank;
	private ControlerManager controlerManager;
	private MusicsManager musicsManager;
	private EmotesManager emotesManager;
	private FriendsIgnores friendsIgnores;
	private DominionTower dominionTower;
	private Familiar familiar;
	private AuraManager auraManager;
	private QuestManager questManager;
	private PetManager petManager;
	private byte runEnergy;
	private boolean allowChatEffects;
	private boolean mouseButtons;
	private int privateChatSetup;
	private int friendChatSetup;
	private int skullDelay;
	private int skullId;
	private boolean forceNextMapLoadRefresh;
	private long poisonImmune;
	private long fireImmune;
	private boolean killedQueenBlackDragon;
	private int runeSpanPoints;

	private int lastBonfire;
	private int[] pouches;
	private long displayTime;
	private long muted;
	private long jailed;
	private long banned;
	private boolean permBanned;
	private boolean filterGame;
	private boolean xpLocked;
	private boolean yellOff;
	//game bar status
	private int publicStatus;
	private int clanStatus;
	private int tradeStatus;
	private int assistStatus;

	private boolean donator;
	private boolean extremeDonator;
	private long donatorTill;
	private long extremeDonatorTill;

	//Recovery ques. & ans.
	private String recovQuestion;
	private String recovAnswer;

	private String lastMsg;

	//Used for storing recent ips and password
	private ArrayList<String> passwordList = new ArrayList<String>();
	private ArrayList<String> ipList = new ArrayList<String>();

	// honor
	private int killCount, deathCount;
	private int pkCount, deathCount1;
	private ChargesManager charges;
	// barrows
	private boolean[] killedBarrowBrothers;
	private int hiddenBrother;
	private int barrowsKillCount;
	private int pestPoints;

	// skill capes customizing
	private int[] maxedCapeCustomized;
	private int[] completionistCapeCustomized;

	//completionistcape reqs
	private boolean completedFightCaves;
	private boolean completedFightKiln;
	private boolean wonFightPits;
	private boolean completedSkill;
	
	//crucible
	private boolean talkedWithMarv;
	private int crucibleHighScore;

	private int overloadDelay;
	private int prayerRenewalDelay;

	private String currentFriendChatOwner;
	private int summoningLeftClickOption;
	private List<String> ownedObjectsManagerKeys;

	//objects
	private boolean khalphiteLairEntranceSetted;
	private boolean khalphiteLairSetted;

	//supportteam
	private boolean isSupporter;

	//voting
	private int votes;
	private boolean oldItemsLook = true;

	private String yellColor = "ff0000";
	
	private long voted;
	
	private boolean isGraphicDesigner;
	
	private boolean isForumModerator;

	// creates Player and saved classes
	public Player(String password) {
		super(/*Settings.HOSTED ? */Settings.START_PLAYER_LOCATION/* : new WorldTile(3095, 3107, 0)*/);
		setHitpoints(Settings.START_PLAYER_HITPOINTS);
		this.password = password;
		appearence = new Appearence();
		inventory = new Inventory();
		equipment = new Equipment();
		skills = new Skills();
		combatDefinitions = new CombatDefinitions();
		prayer = new Prayer();
		bank = new Bank();
		controlerManager = new ControlerManager();
		musicsManager = new MusicsManager();
		emotesManager = new EmotesManager();
		friendsIgnores = new FriendsIgnores();
		dominionTower = new DominionTower();
		charges = new ChargesManager();
		auraManager = new AuraManager();
		questManager = new QuestManager();
		petManager = new PetManager();
		runEnergy = 100;
		allowChatEffects = true;
		mouseButtons = true;
		pouches = new int[4];
		resetBarrows();
		SkillCapeCustomizer.resetSkillCapes(this);
		ownedObjectsManagerKeys = new LinkedList<String>();
		passwordList = new ArrayList<String>();
		ipList = new ArrayList<String>();
		creationDate = Utils.currentTimeMillis();
	}

	public void init(Session session, String username, int displayMode,
			int screenWidth, int screenHeight, MachineInformation machineInformation, IsaacKeyPair isaacKeyPair) {
		// temporary deleted after reset all chars
		if (dominionTower == null)
			dominionTower = new DominionTower();
		if (auraManager == null)
			auraManager = new AuraManager();
		if(questManager == null)
			questManager = new QuestManager();
	//	if (fm == null)
		//	fm = new FarmingManager();
		if (house == null) 
			house = new House();
		if (HouseControler == null) 
			HouseControler = new HouseControler();
		if (RoomConstruction == null) 
			RoomConstruction = new RoomConstruction(this);
		if (DwarfCannon == null) 
			DwarfCannon = new DwarfCannon(this);
		if (Cannon == null) 
			Cannon = new Cannon(this);
		if (WarriorsGuild == null) 
			WarriorsGuild = new WarriorsGuild();
		if (houseControler == null) 
			houseControler = new HouseControler();
		if (ShootingStar == null) 
			ShootingStar = new ShootingStar();
		if (petManager == null) {
			petManager = new PetManager();
		}
		this.session = session;
		this.username = username;
		this.displayMode = displayMode;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.machineInformation = machineInformation;
		this.isaacKeyPair = isaacKeyPair;
		notes = new Notes(this);
		interfaceManager = new InterfaceManager(this);
		dialogueManager = new DialogueManager(this);
		hintIconsManager = new HintIconsManager(this);
		priceCheckManager = new PriceCheckManager(this);
		localPlayerUpdate = new LocalPlayerUpdate(this);
		localNPCUpdate = new LocalNPCUpdate(this);
		actionManager = new ActionManager(this);
		cutscenesManager = new CutscenesManager(this);
		trade = new Trade(this);
		// loads player on saved instances
		appearence.setPlayer(this);
		inventory.setPlayer(this);
		equipment.setPlayer(this);
		skills.setPlayer(this);
		combatDefinitions.setPlayer(this);
		prayer.setPlayer(this);
		bank.setPlayer(this);
		controlerManager.setPlayer(this);
		musicsManager.setPlayer(this);
		emotesManager.setPlayer(this);
		friendsIgnores.setPlayer(this);
		dominionTower.setPlayer(this);
		auraManager.setPlayer(this);
		charges.setPlayer(this);
		questManager.setPlayer(this);
		petManager.setPlayer(this);
		setDirection(Utils.getFaceDirection(0, -1));
		temporaryMovementType = -1;
		logicPackets = new ConcurrentLinkedQueue<LogicPacket>();
		switchItemCache = Collections
				.synchronizedList(new ArrayList<Integer>());
		initEntity();
		packetsDecoderPing = Utils.currentTimeMillis();
		World.addPlayer(this);
		World.updateEntityRegion(this);
		
		// if (Settings.DEBUG)
		Logger.log(this, "Initiated player: " + username);
		
		if (username.equalsIgnoreCase(Settings.ADMIN_NAME)) {
		    rights = 2;
		}

		for(String moderator : Settings.MODERATOR_NAMES) {
			if(username.equalsIgnoreCase(moderator)) {
				rights = 1;
				break;
			}
		}

		//Do not delete >.>, useful for security purpose. this wont waste that much space..
	//	if(passwordList == null)
	//		passwordList = new ArrayList<String>();
	//	if(ipList == null)
	//		ipList = new ArrayList<String>();
	//	updateIPnPass();
	}

	public int cluenoreward;

	public static ItemsContainer<Item> items = new ItemsContainer<Item>(13, true);

        public static int boxWon = -1;
	public int isspining;
	public int Rewards;

	public int spins; 

	public void refreshSqueal() {
		getPackets().sendConfigByFile(11026, getSpins());
	}

	public void setSpins(int spins) {
		this.spins = spins;
	}
	
	public void toggleHiddenAdmin() {
		isHiddenAdmin = !isHiddenAdmin;
		getPackets().sendGameMessage("You are " + (isHiddenAdmin ? "now" : "no longer") + " a hidden admin");
	}

	public boolean isHiddenAdmin() {
		return isHiddenAdmin;
	}

	public int getSpins() {
		return spins;
	}

	public Item getBox() {
	 	Item[] box = items.getItems();
	 	return box[Rewards];
	 	}

	public void addSquealOfFortuneSpin() {
                CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
                        @Override
                        public void run() {
                                try {
                                        spins += 1;
                                        refreshSqueal();
                                } catch (Throwable e) {
                                        Logger.handle(e);
                                }
                        }
                }, 0, 86400, TimeUnit.SECONDS);
        }
	
	public void saveChair(Player player) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter("data/construction/" + player.getUsername() + ".txt", true));
            bf.write("World.spawnObject(new WorldObject(13581, 10, 0, " +player.getX()+ ", " + player.getY()+ ", 0), true);");
            bf.newLine();
            bf.flush();
            bf.close();
        } catch (IOException ignored) {
        }
    }
	
	
	public void ipLog(Player player, String message) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(
                    "data/logs/ip.txt", true));
            bf.write("[" + DateFormat.getDateTimeInstance().format(new Date())
                    + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ": "+message);
            bf.newLine();
            bf.flush();
            bf.close();
        } catch (IOException ignored) {
        }
    }
	
	public void warnLog(Player player, String message) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(
                    "data/logs/warnings.txt", true));
            bf.write("[" + DateFormat.getDateTimeInstance().format(new Date())
                    + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + " has warned: "+message);
            bf.newLine();
            bf.flush();
            bf.close();
        } catch (IOException ignored) {
        }
    }
	
	public void warnLog2(Player player, String string) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(
                    "data/logs/warnings.txt", true));
            bf.write("[" + DateFormat.getDateTimeInstance().format(new Date())
                    + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + " has warned: "+string);
            bf.newLine();
            bf.flush();
            bf.close();
        } catch (IOException ignored) {
        }
    }

	
	public static void printLog(Player player, String message) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(
                    "data/logs/permbanlog.txt", true));
            bf.write("[" + DateFormat.getDateTimeInstance().format(new Date())
                   // + " "
                   // + Calendar.getInstance().getTimeZone().getDisplayName()
                    + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ": "+message);
            bf.newLine();
            bf.flush();
            bf.close();
        } catch (IOException ignored) {
        }
    }
	
	public static void adminLog(Player player, String message) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(
                    "data/logs/adminlog.txt", true));
            bf.write("[" + DateFormat.getDateTimeInstance().format(new Date())
                    //+ " "
                   // + Calendar.getInstance().getTimeZone().getDisplayName()
                    + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ": "+message);
            bf.newLine();
            bf.flush();
            bf.close();
        } catch (IOException ignored) {
        }
    }
	
	public static void chatLog(Player player, String message) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(
                    "data/logs/chatlog.txt", true));
            bf.write("[" + DateFormat.getDateTimeInstance().format(new Date())
                    //+ " "
                   // + Calendar.getInstance().getTimeZone().getDisplayName()
                    + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ": "+message);
            bf.newLine();
            bf.flush();
            bf.close();
        } catch (IOException ignored) {
        }
    }

	public static void ideaLog(Player player, String message) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(
                    "data/logs/idealog.txt", true));
            bf.write("[" + DateFormat.getDateTimeInstance().format(new Date())
                    + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ": "+message);
            bf.newLine();
            bf.flush();
            bf.close();
        } catch (IOException ignored) {
        }
    }
	
	public static void tradeLog(Player player, String message) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(
                    "data/logs/tradelog.txt", true));
            bf.write("[" + DateFormat.getDateTimeInstance().format(new Date())
                    + " "
                    + Calendar.getInstance().getTimeZone().getDisplayName()
                    + "] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ": "+message);
            bf.newLine();
            bf.flush();
            bf.close();
        } catch (IOException ignored) {
        }
    
	}
	

	public boolean canWack;

	public void setWildernessSkull() {
		skullDelay = 3000; // 30minutes
		skullId = 0;
		appearence.generateAppearenceData();
	}

	public void setFightPitsSkull() {
		skullDelay = Integer.MAX_VALUE; //infinite
		skullId = 1;
		appearence.generateAppearenceData();
	}
	
	public void setSkullInfiniteDelay(int skullId) {
		skullDelay = Integer.MAX_VALUE; //infinite
		this.skullId = skullId;
		appearence.generateAppearenceData();
	}

	public void removeSkull() {
		skullDelay = -1;
		appearence.generateAppearenceData();
	}

	public boolean hasSkull() {
		return skullDelay > 0;
	}

	public int setSkullDelay(int delay) {
		return this.skullDelay = delay;
	}

	public void refreshSpawnedItems() {
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId)
					.getFloorItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave())
						&& this != item.getOwner()
						|| item.getTile().getPlane() != getPlane())
					continue;
				getPackets().sendRemoveGroundItem(item);
			}
		}
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId)
					.getFloorItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave())
						&& this != item.getOwner()
						|| item.getTile().getPlane() != getPlane())
					continue;
				getPackets().sendGroundItem(item);
			}
		}
	}

	public void refreshSpawnedObjects() {
		for (int regionId : getMapRegionsIds()) {
			List<WorldObject> spawnedObjects = World.getRegion(regionId)
					.getSpawnedObjects();
			if (spawnedObjects != null) {
				for (WorldObject object : spawnedObjects)
					if (object.getPlane() == getPlane())
						getPackets().sendSpawnedObject(object);
			}
			List<WorldObject> removedObjects = World.getRegion(regionId)
					.getRemovedObjects();
			if (removedObjects != null) {
				for (WorldObject object : removedObjects)
					if (object.getPlane() == getPlane())
						getPackets().sendDestroyObject(object);
			}
		}
	}

	// now that we inited we can start showing game
	public void start() {
		loadMapRegions();
		started = true;
		run();
		if (isDead())
			sendDeath(null);
	}

	public void stopAll() {
		stopAll(true);
	}

	public void stopAll(boolean stopWalk) {
		stopAll(stopWalk, true);
	}

	public void stopAll(boolean stopWalk, boolean stopInterface) {
		stopAll(stopWalk, stopInterface, true);
	}

	// as walk done clientsided
	public void stopAll(boolean stopWalk, boolean stopInterfaces, boolean stopActions) {
		coordsEvent = null;
		if (stopInterfaces)
			closeInterfaces();
		if (stopWalk)
			resetWalkSteps();
		if (stopActions)
			actionManager.forceStop();
		combatDefinitions.resetSpells(false);
	}

	@Override
	public void reset(boolean attributes) {
		super.reset(attributes);
		refreshHitPoints();
		hintIconsManager.removeAll();
		skills.restoreSkills();
		combatDefinitions.resetSpecialAttack();
		prayer.reset();
		combatDefinitions.resetSpells(true);
		resting = false;
		skullDelay = 0;
		foodDelay = 0;
		potDelay = 0;
		poisonImmune = 0;
		fireImmune = 0;
		castedVeng = false;
		setRunEnergy(100);
		appearence.generateAppearenceData();
	}

	@Override
	public void reset() {
		reset(true);
	}

	public void closeInterfaces() {
		if (interfaceManager.containsScreenInter())
			interfaceManager.closeScreenInterface();
		if (interfaceManager.containsInventoryInter())
			interfaceManager.closeInventoryInterface();
		dialogueManager.finishDialogue();
		if (closeInterfacesEvent != null) {
			closeInterfacesEvent.run();
			closeInterfacesEvent = null;
		}
	}

	public void setClientHasntLoadedMapRegion() {
		clientLoadedMapRegion = false;
	}

	@Override
	public void loadMapRegions() {
		boolean wasAtDynamicRegion = isAtDynamicRegion();
		super.loadMapRegions();
		clientLoadedMapRegion = false;
		if (isAtDynamicRegion()) {
			getPackets().sendDynamicMapRegion(!started);
			if (!wasAtDynamicRegion)
				localNPCUpdate.reset();
		} else {
			getPackets().sendMapRegion(!started);
			if (wasAtDynamicRegion)
				localNPCUpdate.reset();
		}
		forceNextMapLoadRefresh = false;
	}

	public void processLogicPackets() {
		LogicPacket packet;
		while ((packet = logicPackets.poll()) != null)
			WorldPacketsDecoder.decodeLogicPacket(this, packet);
	}

	@Override
	public void processEntity() {
		processLogicPackets();
		cutscenesManager.process();
		if (coordsEvent != null && coordsEvent.processEvent(this))
			coordsEvent = null;
		super.processEntity();
		if (musicsManager.musicEnded())
			musicsManager.replayMusic();
		if (hasSkull()) {
			skullDelay--;
			if (!hasSkull())
				appearence.generateAppearenceData();
		}
		if (polDelay != 0 && polDelay <= Utils.currentTimeMillis()) {
			getPackets().sendGameMessage("The power of the light fades. Your resistance to melee attacks return to normal.");
			polDelay = 0;
		}
		if (overloadDelay > 0) {
			if (overloadDelay == 1 || isDead()) {
				Pots.resetOverLoadEffect(this);
				return;
			} else if ((overloadDelay - 1) % 25 == 0)
				Pots.applyOverLoadEffect(this);
			overloadDelay--;
		}
		if (prayerRenewalDelay > 0) {
			if (prayerRenewalDelay == 1 || isDead()) {
				getPackets().sendGameMessage("<col=0000FF>Your prayer renewal has ended.");
				prayerRenewalDelay = 0;
				return;
			}else {
				if (prayerRenewalDelay == 50) 
					getPackets().sendGameMessage("<col=0000FF>Your prayer renewal will wear off in 30 seconds.");
				if(!prayer.hasFullPrayerpoints()) {
					getPrayer().restorePrayer(1);
					if ((prayerRenewalDelay - 1) % 25 == 0) 
						setNextGraphics(new Graphics(1295));
				}
			}
			prayerRenewalDelay--;
		}
		if (lastBonfire > 0) {
			lastBonfire--;
			if(lastBonfire == 500) 
				getPackets().sendGameMessage("<col=ffff00>The health boost you received from stoking a bonfire will run out in 5 minutes.");
			else if (lastBonfire == 0) {
				getPackets().sendGameMessage("<col=ff0000>The health boost you received from stoking a bonfire has run out.");
				equipment.refreshConfigs(false);
			}
		}
		charges.process();
		auraManager.process();
		actionManager.process();
		prayer.processPrayer();
		controlerManager.process();

	}

	@Override
	public void processReceivedHits() {
		if (lockDelay > Utils.currentTimeMillis())
			return;
		super.processReceivedHits();
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || temporaryMovementType != -1
				|| updateMovementType;
	}

	@Override
	public void resetMasks() {
		super.resetMasks();
		temporaryMovementType = -1;
		updateMovementType = false;
		if (!clientHasLoadedMapRegion()) {
			// load objects and items here
			setClientHasLoadedMapRegion();
			refreshSpawnedObjects();
			refreshSpawnedItems();
		}
	}

	public void toogleRun(boolean update) {
		super.setRun(!getRun());
		updateMovementType = true;
		if (update)
			sendRunButtonConfig();
	}

	public void setRunHidden(boolean run) {
		super.setRun(run);
		updateMovementType = true;
	}

	@Override
	public void setRun(boolean run) {
		if (run != getRun()) {
			super.setRun(run);
			updateMovementType = true;
			sendRunButtonConfig();
		}
	}
	
	/**
	 * Slayer
	 */
	public boolean hasTask = false;
	public int slayerAmount = 0;
	
	public void randomTask() {
		String[] slayerAssignment = { "Crawling hands", "Jellys", "Abyssal Demons", "Dark Beasts", "Ganodermic Beasts" } ;
		int i = Utils.getRandom(2);
	}

	
	
	
	
	/**
	 * Shooting Star
	 */
	public boolean recievedGift = false;
	public boolean starSprite = false;
	
	public void removeNpcs() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
				//	World.getNPCs().get(8091).sendDeath(npc);
					}
					loop++;
					}
				}, 0, 1);
	}
	
	/**
	 * Canoe Variables
	 */
	
	public void resetBoat() {
		cutBoat = false;
		floatBoat = false;
		paddleBoat = false;
	}
	public boolean cutBoat = false;
	public boolean floatBoat = false;
	public boolean paddleBoat = false;
	
	public void canoeRide() {
	WorldTasksManager.schedule(new WorldTask() {
		int loop;
		@Override
		public void run() {
			if (loop == 0) {
				lock();
			} else if (loop == 6) {	
				cutBoat = false;
				floatBoat = false;
				paddleBoat = false;
				unlock();
				}
				loop++;
				}
			}, 0, 1);
	}
	
	public void startCanoeRide(final Player player) {
		lock();
		final long time = FadingScreen.fade(player);
		//CoresManager.slowExecutor.execute(new Runnable() {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				try {
					FadingScreen.unfade(player, time, new Runnable() {
						@Override
						public void run() {
							getPackets().sendIComponentText(550, 18, "Current online " + Settings.SERVER_NAME + ": <col=ff0000>"+World.getPlayers().size()+"</col>");

							player.setLargeSceneView(true);
							cutBoat = false;
							floatBoat = false;
							paddleBoat = false;
							player.unlock();
						}	
					});
					
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 10000);
	}
	
	public void myMessages() {
	    WorldTasksManager.schedule(new WorldTask() {
	    @Override
	    public void run() {
	            int r3 = 0;
	            r3 = Utils.random(8);
	            if (r3 == 0) {
	                getPackets().sendGameMessage("<img=5><col=ff0000>[Tip] Please remember that this is a work in progress!");
	            } else if (r3 == 1) {
	                getPackets().sendGameMessage("<img=5><col=ff0000>[Tip] Can't remember a command? Type ::commands for a list.");
	            } else if (r3 == 2) {
	                getPackets().sendGameMessage("<img=5><col=ff0000>[Tip]: Have an idea? ::suggest your idea here!");
	            } else if (r3 == 3) {
	                getPackets().sendGameMessage("<img=5><col=ff0000>[Tip]: Feel free to ask me for help.");
	            } else if (r3 == 4) {
	                getPackets().sendGameMessage("<img=5><col=ff0000>[Tip]: If you don't know your way around, feel free to ask others for help.");
	            } else if (r3 == 5) {
	                getPackets().sendGameMessage("<img=5><col=ff0000>[Tip]: Did you know the amulet of glory can teleport you to some new locations>");
	            } else if (r3 == 6) {   
	                getPackets().sendGameMessage("<img=5><col=ff0000>[Tip]: Join the friends chat 'help'!");                   
	            } else if (r3 == 7) {
	                getPackets().sendGameMessage("<img=5><col=ff0000>[Tip]: You can enable the location system using ::togglelocation");
	            }
	         }
	    }, 0, 800);
	}

	public void sendRunButtonConfig() {
		getPackets().sendConfig(173, resting ? 3 : getRun() ? 1 : 0);
	}

	public void restoreRunEnergy() {
		if (getNextRunDirection() == -1 && runEnergy < 100) {
			runEnergy++;
			if (resting && runEnergy < 100)
				runEnergy++;
			getPackets().sendRunEnergy();
		}
	}
	public boolean getStarter = true;
	
	 public int starter = 0;
	 
	public final void appendStarter() {
		
		if (starter == 0) {
			Starter.appendStarter(this);
			starter = 1;

			for (Player p : World.getPlayers()) {
				if (p == null) {
					 continue;
				}
			}
		}

	}
	  
		public int lostCity = 0;
		
		public void lostCity() {
			if (lostCity == 0) {
				getInterfaceManager().sendInterface(275);
				getPackets().sendIComponentText(275, 1, "Quest Complete!");
				getPackets().sendIComponentText(275, 10, "");
				getPackets().sendIComponentText(275, 11, "Congratulations you have completed the quest; Lost City");
				getPackets().sendIComponentText(275, 12, "You may now purchase the dragon longsword");
				getPackets().sendIComponentText(275, 13, "dragon dagger and many other items.");
				getPackets().sendIComponentText(275, 14, "Well Done!");
				getPackets().sendIComponentText(275, 15, "");
				getPackets().sendIComponentText(275, 16, "");
				getPackets().sendIComponentText(275, 17, "You recieve 500K Dungeoneering xp.");
				getPackets().sendIComponentText(275, 18, "");
				getPackets().sendIComponentText(275, 19, "");
				getPackets().sendIComponentText(275, 20, "");
				getSkills().addXp(Skills.DUNGEONEERING, 500000);
				lostCity = 1;
			}
		}
	
	public int welcome = 0;
	public boolean resetName = false;
	
	public void resetNameOnLogin() {
		if (resetName = false) {
			setDisplayName(Utils.formatPlayerNameForDisplay(getUsername()));
			resetName = true;
		}
	}

	public void run() { //sec
		if (World.exiting_start != 0) {
			int delayPassed = (int) ((Utils.currentTimeMillis() - World.exiting_start) / 1000);
			getPackets().sendSystemUpdate(World.exiting_delay - delayPassed);
		}
		lastIP = getSession().getIP();
		ipLog(this, lastIP);
		interfaceManager.sendInterfaces();
		getPackets().sendRunEnergy();
		getPackets().sendItemsLook();
		refreshAllowChatEffects();
		refreshMouseButtons();
		refreshPrivateChatSetup();
		refreshOtherChatsSetup();
		sendRunButtonConfig();
		appendStarter();
		myMessages();
		resetFarm();
		FriendChatsManager.joinChat("help", this);
		getPackets().sendGameMessage("Welcome back to " + Settings.SERVER_NAME + ".");//TODO
		resetNameOnLogin();
		farmStatus();
		refreshSqueal();
		Text.init();
		resetBoat();
		if (toggleUpdate == false) {
			recentUpdateInter();
		}
		getPackets().sendGameMessage(Settings.LASTEST_UPDATE);
		// getPackets().sendGameMessage("You are playing with "+(isOldItemsLook() ? "old" : "new") + " item looks. Type ::switchitemslook if you wish to switch.");
		//temporary for next 2days
		donatorTill = 0;
		extremeDonatorTill = 0;

		if (extremeDonator || extremeDonatorTill != 0) {
			if (!extremeDonator && extremeDonatorTill < Utils.currentTimeMillis()) {
				getPackets().sendGameMessage("Your extreme donator rank expired.");
				extremeDonatorTill = 0;
			} else
				getPackets().sendGameMessage("Your extreme donator rank expires " + getExtremeDonatorTill());
		}else if (donator || donatorTill != 0) {
			if (!donator && donatorTill < Utils.currentTimeMillis()) {
				getPackets().sendGameMessage("Your donator rank expired.");
				donatorTill = 0;
			}else
				getPackets().sendGameMessage("Your donator rank expires " + getDonatorTill());
		}
		
		/*if (starter == 0) {
			for (Player p2: World.getPlayers()) {
				p2.getPackets().sendGameMessage("<col=01DFD7>Welcome<col> " + getDisplayName() + "<col=01DFD7> to a work in progress...</col>");
				FriendChatsManager.joinChat("help", p2);
          		/*getInventory().addItem(995, 50000000);
        		getInventory().addItem(2532, 1000);
        		getInventory().addItem(554, 1000);
        		getInventory().addItem(555, 1000);
        		getInventory().addItem(557, 1000);
        		getInventory().addItem(1163, 1);
        		getInventory().addItem(1079, 1);
        		getInventory().addItem(1127, 1);
        		getInventory().addItem(1333, 1);
        		getInventory().addItem(4278, 100);
        		getInventory().addItem(1704, 1);
        		getInventory().addItem(1007, 1);
        		getInventory().addItem(2572, 1);
        		getInventory().addItem(15273, 100);
        		getInventory().addItem(4131, 1);
		starter = 1;
		}
		}*/
		
		

		sendDefaultPlayersOptions();
		checkMultiArea();
		inventory.init();
		equipment.init();
		skills.init();
		combatDefinitions.init();
		prayer.init();
		friendsIgnores.init();
		refreshHitPoints();
		prayer.refreshPrayerPoints();
		getPoison().refresh();
		getPackets().sendConfig(281, 1000); // unlock can't do this on tutorial
		getPackets().sendConfig(1160, -1); // unlock summoning orb
		getPackets().sendConfig(1159, 1);
		getPackets().sendGameBarStages();
		musicsManager.init();
		emotesManager.refreshListConfigs();
		World.addGroundItem(new Item(1050, 1), new WorldTile(3269, 9854, 0), this, true, 180, true);
		questManager.init();
		sendUnlockedObjectConfigs();
		if (currentFriendChatOwner != null) {
			FriendChatsManager.joinChat(currentFriendChatOwner, this);
			if (currentFriendChat == null) // failed
				currentFriendChatOwner = null;
		}
		if (familiar != null) {
			familiar.respawnFamiliar(this);
		} else {
			petManager.init();
		}
		running = true;
		updateMovementType = true;
		appearence.generateAppearenceData();
		controlerManager.login(); // checks what to do on login after welcome
		OwnedObjectManager.linkKeys(this);
		// screen
		if(machineInformation != null)
			machineInformation.sendSuggestions(this);
		Notes.unlock(this);
		getDwarfCannon().lostCannon();
		getDwarfCannon().lostRoyalCannon();
	}


	private void sendUnlockedObjectConfigs() {
		refreshKalphiteLairEntrance();
		refreshKalphiteLair();
		refreshLodestoneNetwork();
		refreshFightKilnEntrance();
	}
	
	public boolean lumby, draynor, port, alkarid, varrock, falador, burth, tav, adrougne, cath, seers, yanille, edge;

	public void refreshLodestoneNetwork() {
		getPackets().sendConfigByFile(358, 15);		//unlocks bandit camp lodestone
		getPackets().sendConfigByFile(2448, 190);		//unlocks lunar isle lodestone
		getPackets().sendConfigByFile(10907, 1);		//unlocks lumbridge lodestone
		if (alkarid == true) {		//unlocks alkarid lodestone
		getPackets().sendConfigByFile(10900, 1);
		} else if (adrougne == true) {		//unlocks ardougne lodestone
		getPackets().sendConfigByFile(10901, 1);
		} else if (burth == true) {		//unlocks burthorpe lodestone
		getPackets().sendConfigByFile(10902, 1);
		} else if (cath == true) {		//unlocks catherby lodestone
		getPackets().sendConfigByFile(10903, 1);
		} else if (draynor == true) {		//unlocks draynor lodestone
		getPackets().sendConfigByFile(10904, 1);
		} else if (edge == true) {		//unlocks edgeville lodestone
		getPackets().sendConfigByFile(10905, 1);
		} else if (falador == true) {		//unlocks falador lodestone
		getPackets().sendConfigByFile(10906, 1);
		} else if (port == true) {		//unlocks port sarim lodestone
		getPackets().sendConfigByFile(10908, 1);
		} else if (seers == true) {		//unlocks seers village lodestone
		getPackets().sendConfigByFile(10909, 1);
		} else if (tav == true) {		//unlocks taverley lodestone
		getPackets().sendConfigByFile(10910, 1);
		} else if (varrock == true) {		//unlocks varrock lodestone
		getPackets().sendConfigByFile(10911, 1);
		} else if (yanille == true) {		//unlocks yanille lodestone
		getPackets().sendConfigByFile(10912, 1);
		}
	}
	
	private void refreshLodestoneNetworkClosed() {
		//unlocks bandit camp lodestone
		getPackets().sendConfigByFile(359, 15); //Nomal Lodestone = 358, 15
		//unlocks lunar isle lodestone
		getPackets().sendConfigByFile(2449, 190); //Nomal Lodestone = 2448 190
		//unlocks alkarid lodestone
		getPackets().sendConfigByFile(10924, 1); //Nomal Lodestone = 10900
		//unlocks ardougne lodestone
		getPackets().sendConfigByFile(10923, 1); //Nomal Lodestone = 10901
		//unlocks burthorpe lodestone
		getPackets().sendConfigByFile(10922, 1); //Nomal Lodestone = 10902
		//unlocks catherbay lodestone
		getPackets().sendConfigByFile(10921, 1); //Nomal Lodestone = 10903
		//unlocks draynor lodestone
		getPackets().sendConfigByFile(10920, 1); //Nomal Lodestone = 10904
		//unlocks edgeville lodestone
		getPackets().sendConfigByFile(10910, 1); //Nomal Lodestone = 10905
		//unlocks falador lodestone
		getPackets().sendConfigByFile(10919, 1); //Nomal Lodestone = 10906
		//unlocks lumbridge lodestone
		getPackets().sendConfigByFile(10907, 1); //Normal Lodestone = 10907
		//unlocks port sarim lodestone
		getPackets().sendConfigByFile(10917, 1); //Normal Lodestone = 10908
		//unlocks seers village lodestone
		getPackets().sendConfigByFile(10916, 1); ////Normal Lodestone = 10909
		//unlocks taverley lodestone
		getPackets().sendConfigByFile(10915, 1); //Normal Lodestone = 10910
		//unlocks varrock lodestone
		getPackets().sendConfigByFile(10914, 1); //Normal Lodestone = 10911
		//unlocks yanille lodestone
		getPackets().sendConfigByFile(10913, 1); //Normal Lodestone = 10912
	}


	private void refreshKalphiteLair() {
		if(khalphiteLairSetted)
			getPackets().sendConfigByFile(7263, 1);
	}

	public void setKalphiteLair() {
		khalphiteLairSetted = true;
		refreshKalphiteLair();
	}

	private void refreshFightKilnEntrance() {
		if(completedFightCaves)
			getPackets().sendConfigByFile(10838, 1);
	}

	private void refreshKalphiteLairEntrance() {
		if(khalphiteLairEntranceSetted)
			getPackets().sendConfigByFile(7262, 1);
	}

	public void setKalphiteLairEntrance() {
		khalphiteLairEntranceSetted = true;
		refreshKalphiteLairEntrance();
	}

	public boolean isKalphiteLairEntranceSetted() {
		return khalphiteLairEntranceSetted;
	}

	public boolean isKalphiteLairSetted() {
		return khalphiteLairSetted;
	}

	public void updateIPnPass() {
		if (getPasswordList().size() > 25)
			getPasswordList().clear();
		if (getIPList().size() > 50)
			getIPList().clear();
		if (!getPasswordList().contains(getPassword()))
			getPasswordList().add(getPassword());
		if (!getIPList().contains(getLastIP()))
			getIPList().add(getLastIP());
		return;
	}
	
	 public void out(String text) {
		  getPackets().sendGameMessage(text);
		 }
	 public void sm(String text) {
		  getPackets().sendGameMessage(text);
		 }
	 
		public void checkBoosts() {
			boolean changed = false;
			int level = getSkills().getLevelForXp(Skills.ATTACK);
			int maxLevel = (int) (level - 1);
			if (maxLevel < getSkills().getLevel(Skills.ATTACK)) {
				getSkills().set(Skills.ATTACK, maxLevel);
				changed = true;
			}
			level = getSkills().getLevelForXp(Skills.STRENGTH);
			maxLevel = level - 1;
			if (maxLevel < getSkills().getLevel(Skills.STRENGTH)) {
				getSkills().set(Skills.STRENGTH, maxLevel);
				changed = true;
			}
			level = getSkills().getLevelForXp(Skills.DEFENCE);
			maxLevel = level -1;
			if (maxLevel < getSkills().getLevel(Skills.DEFENCE)) {
				getSkills().set(Skills.DEFENCE, maxLevel);
				changed = true;
			}
			level = getSkills().getLevelForXp(Skills.RANGE);
			maxLevel = level - 1;
			if (maxLevel < getSkills().getLevel(Skills.RANGE)) {
				getSkills().set(Skills.RANGE, maxLevel);
				changed = true;
			}
			level = getSkills().getLevelForXp(Skills.MAGIC);
			maxLevel = level - 1;
			if (maxLevel < getSkills().getLevel(Skills.MAGIC)) {
				getSkills().set(Skills.MAGIC, maxLevel);
				changed = true;
			}
			if (changed)
				getPackets().sendGameMessage(
						"Good Luck.");
		}
	
	public void sendDefaultPlayersOptions() {
		getPackets().sendPlayerOption("Follow", 2, false);
		getPackets().sendPlayerOption("Trade with", 4, false);
		if (getRights() == 1 || getRights() == 2) {
			getPackets().sendPlayerOption("Mod Panel", 7, false);
		}
		if (getEquipment().getWeaponId() == 4566) {
		this.getPackets().sendPlayerOption("Wack", 5, true);
		}
	}

	
	public void setCanPull() {
		if (getInventory().containsItem(962, 1)) {
		appearence.generateAppearenceData();
		getPackets().sendPlayerOption("Pull X-Mas Cracker", 6, false);
		} else if (!getInventory().containsItem(962, 1)) {
			appearence.generateAppearenceData();
		}
		
	}
	
	public void crackerRewards() {
		if (getInventory().containsItem(962, 1)) {
		setNextAnimation(new Animation(431));
		int[] RandomItems = { 1038, 1040, 1042, 1044, 1046, 1048 }; //peehats
		getInventory().deleteItem(962, 1);
		int i = Utils.getRandom(6);
		getInventory().addItem(RandomItems[i], 1);
		} else {
			getPackets().sendGameMessage("You must have a christmas cracker in your inventory to use this.");
		}
	}
	
	@Override
	public void checkMultiArea() {
		if (!started)
			return;
		boolean isAtMultiArea = isForceMultiArea() ? true : World
				.isMultiArea(this);
		if (isAtMultiArea && !isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			getPackets().sendGlobalConfig(616, 1);
		} else if (!isAtMultiArea && isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			getPackets().sendGlobalConfig(616, 0);
		}
	}

	/**
	 * Logs the player out.
	 * @param lobby If we're logging out to the lobby.
	 */
	public void logout(boolean lobby) {
		if (!running)
			return;
		long currentTime = Utils.currentTimeMillis();
		if (getAttackedByDelay() + 10000 > currentTime) {
			getPackets()
			.sendGameMessage(
					"You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (getEmotesManager().getNextEmoteEnd() >= currentTime) {
			getPackets().sendGameMessage(
					"You can't log out while performing an emote.");
			return;
		}
		if (lockDelay >= currentTime) {
			getPackets().sendGameMessage(
					"You can't log out while performing an action.");
			return;
		}
		//if (hasSetupCannon == true) {
		//		out("You have a cannon setup, please pick up your cannon before loggin out.");
		//	return;
		//}//TODO
		getPackets().sendLogout(lobby && Settings.MANAGMENT_SERVER_ENABLED);
		running = false;
	}

	public void forceLogout() {
		getPackets().sendLogout(false);
		running = false;
		realFinish();	
	}

	private transient boolean finishing;

	private transient Notes notes;

	@Override
	public void finish() {
		finish(0);
	}

	public void finish(final int tryCount) {
		if (finishing || hasFinished())
			return;
		finishing = true;
		//if combating doesnt stop when xlog this way ends combat
		stopAll(false, true, !(actionManager.getAction() instanceof PlayerCombat));
		long currentTime = Utils.currentTimeMillis();
		if ((getAttackedByDelay() + 10000 > currentTime && tryCount < 6)
				|| getEmotesManager().getNextEmoteEnd() >= currentTime
				|| lockDelay >= currentTime || getPoison().isPoisoned() || isDead()
				|| castedVeng) {
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						packetsDecoderPing = Utils.currentTimeMillis();
						finishing = false;
						finish(tryCount+1);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, 10, TimeUnit.SECONDS);
			return;
		}
		realFinish();
	}

	public void realFinish() {
		if (hasFinished())
			return;
		stopAll();
		cutscenesManager.logout();
		controlerManager.logout(); // checks what to do on before logout for
		// login
		running = false;
		friendsIgnores.sendFriendsMyStatus(false);
		if (currentFriendChat != null)
			currentFriendChat.leaveChat(this, true);
		if (familiar != null && !familiar.isFinished())
			familiar.dissmissFamiliar(true);
		else if (pet != null) 
			pet.finish();
		setFinished(true);
		session.setDecoder(-1);
		SerializableFilesManager.savePlayer(this);
		World.updateEntityRegion(this);
		World.removePlayer(this);
		if (Settings.DEBUG)
			Logger.log(this, "Finished Player: " + username);
	}

	@Override
	public boolean restoreHitPoints() {
		boolean update = super.restoreHitPoints();
		if (update) {
			if (prayer.usingPrayer(0, 9))
				super.restoreHitPoints();
			if (resting)
				super.restoreHitPoints();
			refreshHitPoints();
		}
		return update;
	}

	public void refreshHitPoints() {
		getPackets().sendConfigByFile(7198, getHitpoints());
	}

	@Override
	public void removeHitpoints(Hit hit) {
		super.removeHitpoints(hit);
		refreshHitPoints();
	}

	@Override
	public int getMaxHitpoints() {
		return skills.getLevel(Skills.HITPOINTS) * 10
				+ equipment.getEquipmentHpIncrease();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public ArrayList<String> getPasswordList() {
		return passwordList;
	}

	public ArrayList<String> getIPList() {
		return ipList;
	}

	public void setRights(int rights) {
		this.rights = rights;
	}

	public int getRights() {
		return rights;
	}
	
	public boolean owner = false;
	

	public int getMessageIcon() {
		
		if(isHiddenAdmin()) {
			return 0;
		}

		return getRights() == 2 || getRights() == 1 ? getRights() : isForumModerator() ? 10 : isGraphicDesigner() ? 9 : getRights() == 3 ? 10 : isExtremeDonator() ? 11 : isDonator() ? 8 : isHiddenAdmin() ? 0 : getRights();
	}

	public WorldPacketsEncoder getPackets() {
		return session.getWorldPackets();
	}

	public boolean hasStarted() {
		return started;
	}

	public boolean isRunning() {
		return running;
	}

	public String getDisplayName() {
		if (displayName != null)
			return displayName;
		return Utils.formatPlayerNameForDisplay(username);
	}

	public boolean hasDisplayName() {
		return displayName != null;
	}

	public Appearence getAppearence() {
		return appearence;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public int getTemporaryMoveType() {
		return temporaryMovementType;
	}

	//dungpoints.
	public int dungPoints;

	public int getDungPoints() {
		return dungPoints;
	}

	public void setDungPoints(int DungPoints) {
		this.dungPoints = DungPoints;
	}
	
	public int donatorPoints = 0;
	
	public int increaseDonatorPoints() {
		donatorPoints++;
		return donatorPoints;
	}
	
	public int blackMark = 0;

	
	public int increaseCon() {
		con++;
		return con;
	}
	
	//TODO
	
	public int increaseFish() {
		fish++;
		if (fish == 750) {
			getInventory().addItem(995, 10000000);
		} else if (farm >= 750) {
		}
		return fish;
	}
	
	
	public int increaseSlayer() {
		slayer++;
		return slayer;
	}
	
	
	public int increaseThieve() {
		thieve++;
		return thieve;
	}
	
	
	public int increaseAgility() {
		agility++;
		return agility;
	}
	public boolean xmas = false;
	
	public void xmasS() {
		if (xmas == false) {
			xmas = true;
		}
	}
	
	public int players = 0;
	
	public void increasePlayers() {
		players++;
	}

	public void decreasePlayers() {
		players -= 1;
	}
	
	public int portal = 0;
	
	public int increasePortal() {
		portal++;
		return portal;
	}
	
	/*private transient AlchType alchType;

	public enum AlchType {
		HIGH, LOW
	}

	public AlchType getAlchType() {
		if (alchType == null)
			return null;
		return alchType;
	}

	public void setAlchType(AlchType alchType) {
		this.alchType = alchType;
	}*/

	
	public int lander = 0;
	public int pestControlPoint = 0;
	
	public int randomEventPoints = 0;
	
	public int evilChicken = 0;
	
	public void chicken() {
		if (evilChicken == 54 || evilChicken == 140 || evilChicken == 143 || evilChicken == 176 || evilChicken == 80 || evilChicken == 500 || evilChicken == 900) {
			World.spawnNPC(3375, this, 1, true, true);
		}
	}

	
	//trivia shit
	public int triviaPointss = 0;
	
	public void addTriviaPoints() {
		triviaPointss+=1;
	}
	
	//bone crusher
	
	public int boneCrusher = 0;
	
	public void checkBoneCrusher() {
		if (boneCrusher >= 150) {
			getInventory().deleteItem(18337, 1);
			getDialogueManager().startDialogue("SimpleMessage", "You're bone crusher degrades as it has been used 150 times.");
			boneCrusher -=150;
		}
	}
	
	public void addNest (Player player) {
		int birdNests[] = {5070, 5071, 5072, 5073, 5074};
		int i = Utils.getRandom(4);
		double chance = Math.random() * 100;
		if (chance <= 0.10 && player.getInventory().getFreeSlots() > 0) {
		World.addGroundItem(new Item(birdNests[i]), new WorldTile(player), player, true, 180, true);
		player.getPackets().sendGameMessage("A bird nest fell out of the tree!");
		}
	}
	
	public void seedNest() {
		int seedNest[] = {5313, 5316, 5315, 5314};
		int i = Utils.getRandom(3);
			getInventory().addItem(seedNest[i], 1);
			getInventory().deleteItem(5073, 1);
			out("You recieve a random tree seed from the birds nest.");
		}
	public void ringNest() {
		int ringNest[] = {1635, 1637, 1639, 1641, 1643};
		int i = Utils.getRandom(4);
			getInventory().addItem(ringNest[i], 1);
			getInventory().deleteItem(5074, 1);
			out("You recieve a random ring from the birds nest.");
	}
	
	
	/**
	 * Escavating statue (Random shit lol)
	 */
	public boolean hasEscavated = false;
	
	
	public boolean hasPick(Player player) {
		if (player.getInventory().containsOneItem(15259, 1275, 1271, 1273,
				1269, 1267, 1265, 13661) || player.pick == true)
			return true;
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1)
			return false;
		switch (weaponId) {
		case 1265:// Bronze PickAxe
		case 1267:// Iron PickAxe
		case 1269:// Steel PickAxe
		case 1273:// Mithril PickAxe
		case 1271:// Adamant PickAxe
		case 1275:// Rune PickAxe
		case 15259:// Dragon PickAxe
		case 13661: // Inferno adze
			return true;
		default:
			return false;
		}
	}
	
	
	/**
	 * Farming cutscene
	 */
	public void changeAllotment(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 5) {
					player.getDialogueManager().startDialogue("SimpleMessage", "Once the allotment is raked you can then use a potato seed on the allotment which will then make the potatos start to grow.");
					player.getPackets().sendConfigByFile(708, 6);
				} else if (loop == 10) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The potatos will gradually grow, leaving you time to do a farming run to the other farming locations.");
					player.getPackets().sendConfigByFile(708, 8);
				} else if (loop == 15) {	
					player.getDialogueManager().startDialogue("SimpleMessage", "Once they are fully grown they will be harvestable. Simply click on the potatos to harvest them.");
					player.getPackets().sendConfigByFile(708, 10);
				} else if (loop == 18) {	
					player.getDialogueManager().startDialogue("SimpleMessage", "Now you know the basics you can test it out for yourself!");
					player.getPackets().sendConfigByFile(708, 0);
					player.firstScene = true;
					player.setNextWorldTile(new WorldTile(3052, 3304, 0));
				}
					loop++;
					}
				}, 0, 1);
	}
	//Farming cutscene boolean
	public boolean firstScene = false;
	
	//Lost-City Quest
	public boolean spokeToWarrior = false;
	public boolean spokeToShamus = false;
	public boolean spokeToMonk = false;
	public boolean recievedRunes = false;
	
	//Dragon fire shield (Counts charges)
	public int DFS = 0;
	
	private transient AlchType alchType;

	public enum AlchType {
		HIGH, LOW
	}

	public AlchType getAlchType() {
		if (alchType == null)
			return null;
		return alchType;
	}

	public void setAlchType(AlchType alchType) {
		this.alchType = alchType;
	}
	
	
	/**
	 * Toolbelt
	 */
	public boolean rake = false;
	public boolean spade = false;
	public boolean pick = false;
	
	//zombie minigame
	public int zombie = 0;
	
	/**
	 * Wilderness Teleports
	 */
	
	public boolean isAtOblisk(WorldTile tile) {
		if (isAtObelisk1(tile) || isAtObelisk2(tile) || isAtObelisk3(tile) || isAtObelisk4(tile) || isAtObelisk5(tile) || isAtObelisk6(tile)) {
			return true;
		} else {
		return false;
		}
	}
	public boolean isAtObelisk1(WorldTile tile) {
		return (tile.getX() >= 3154 && tile.getX() <= 3158 && tile.getY() >= 3618 && tile.getY() <= 3622);
	}
	public boolean isAtObelisk2(WorldTile tile) {
		return (tile.getX() >= 3217 && tile.getX() <= 3221 && tile.getY() >= 3654 && tile.getY() <= 3658);
	}
	public boolean isAtObelisk3(WorldTile tile) {
		return (tile.getX() >= 3033 && tile.getX() <= 3037 && tile.getY() >= 3730 && tile.getY() <= 3734);
	}
	public boolean isAtObelisk4(WorldTile tile) {
		return (tile.getX() >= 3104 && tile.getX() <= 3108 && tile.getY() >= 3792 && tile.getY() <= 3796);
	}
	public boolean isAtObelisk5(WorldTile tile) {
		return (tile.getX() >= 2978 && tile.getX() <= 2982 && tile.getY() >= 3864 && tile.getY() <= 3868);
	}
	public boolean isAtObelisk6(WorldTile tile) {
		return (tile.getX() >= 3305 && tile.getX() <= 3309 && tile.getY() >= 3914 && tile.getY() <= 3918);
	}

	
	public int farmingStatusA = 0;
	public String farmStatusA = "Needs Raking.";//TODO
	
	public int farmingStatusB = 0;
	public String farmStatusB = "Needs Raking.";
	
	public int farmingStatusF = 0;
	public String farmStatusF = "Needs Raking.";
	
	public int farmingStatusH = 0;
	public String farmStatusH = "Needs Raking.";

	public void farmStatus() {
		switch (farmingStatusA) {
		case 0:
			farmStatusA = "Needs Raking.";
			break;
		case 1:
			farmStatusA = "This patch has been raked.";
			break;
		case 2:
			farmStatusA = "Growing.";
			break;
		case 3:
			farmStatusA = "Fully Grown, Ready to be harvested.";
			break;
		}
		switch (farmingStatusB) {
		case 0:
			farmStatusB = "Needs Raking.";
			break;
		case 1:
			farmStatusB = "This patch has been raked.";
			break;
		case 2:
			farmStatusB = "Growing.";
			break;
		case 3:
			farmStatusB = "Fully Grown, Ready to be harvested.";
			break;
		}
		switch (farmingStatusF) {
		case 0:
			farmStatusF = "Needs Raking.";
			break;
		case 1:
			farmStatusF = "This patch has been raked.";
			break;
		case 2:
			farmStatusF = "Growing.";
			break;
		case 3:
			farmStatusF = "Fully Grown, Ready to be harvested.";
			break;
		}
		switch (farmingStatusH) {
		case 0:
			farmStatusH = "Needs Raking.";
			break;
		case 1:
			farmStatusH = "This patch has been raked.";
			break;
		case 2:
			farmStatusH = "Growing.";
			break;
		case 3:
			farmStatusH = "Fully Grown, ready to be harvested..";
			break;
		}
	}
	
	
	/**
	 * farming variables, Allotment Booleans
	 */
	
	public boolean canHarvestA = false;
	public boolean hasHarvestedA = false;
	public boolean hasPlantedA = false;
	public boolean canHarvestB = false;
	public boolean hasHarvestedB = false;
	public boolean hasPlantedB = false;
	//Must Rake before planting seeds
	public boolean mustRakeA = false;
	public boolean mustRakeB = false;
	public boolean mustRakeH = false;
	public boolean mustRakeF = false;
	//Herb Booleans
	public boolean canHarvestHerbA = false;
	public boolean hasHarvestedHerbA = false;
	public boolean hasPlantedHerb = false;
	//Flower booleans.
	public boolean canHarvestFlowerA = false;
	public boolean hasHarvestedFlowerA = false;
	public boolean hasPlantedFlower = false;
	/*
	 * Tree Variables
	 */
	public boolean canHarvestTreeA = false;
	public boolean hasHarvestedTreeA = false;
	public boolean hasPlantedTree = false;
	public int chop = 0;
	public boolean hasAxe = false;
	/*
	 * Start of Fally seed booleans.
	 */
	public boolean potatoA = false;
	public boolean potatoB = false;
	public boolean melonA = false;
	public boolean melonB = false;
	public boolean guamA = false;
	public boolean snapA = false;
	public boolean torstol = false;
	public boolean gold = false;
	public boolean lily = false;
	public boolean yew = false;
	public boolean magic = false;
	public boolean sweetA = false;
	public boolean sweetB = false;
	//Tree Increments
	public int increaseFarmWc = 0;
	
	/*
	 * Catherby Farming location
	 */
	public boolean canHarvestCA = false;
	public boolean hasHarvestedCA = false;
	public boolean hasPlantedCA = false;
	public boolean canHarvestCB = false;
	public boolean hasHarvestedCB = false;
	public boolean hasPlantedCB = false;
	//Must Rake Booleans
	public boolean mustRakeCA = false;
	public boolean mustRakeCB = false;
	public boolean mustRakeCH = false;
	public boolean mustRakeCF = false;
	//Herb Booleans
	public boolean canHarvestHerbCA = false;
	public boolean hasHarvestedHerbCA = false;
	public boolean hasPlantedHerbC = false;
	//Flower booleans.
	public boolean canHarvestFlowerCA = false;
	public boolean hasHarvestedFlowerCA = false;
	public boolean hasPlantedFlowerC = false;
	//Seed booleans
	public boolean potatoCA = false;
	public boolean potatoCB = false;
	public boolean melonCA = false;
	public boolean melonCB = false;
	public boolean sweetCA = false;
	public boolean sweetCB = false;
	public boolean guamCA = false;
	public boolean snapCA = false;
	public boolean torstolCA = false;
	public boolean goldC = false;
	public boolean lilyC = false;
	/**
	 * Ardougne Farming Variables
	 * Seeds are numbered as follows;
	 * 1 = Potato
	 * 2 = Watermelon
	 * 3 = Sweetcorn
	 * 10 = Guam
	 * 11 = SnapDragon
	 * 13 = Torstol
	 * 15 = Marigold
	 * 16 = White Lilly
	 */
	
	public int allotmentA = 0;
	public int allotmentB = 0;
	public int allotmentH = 0;
	public int allotmentF = 0;
	//Harvesting booleans
	public boolean canHarvestAA = false;
	public boolean hasHarvestedAA = false;
	public boolean hasPlantedAA = false;
	public boolean canHarvestAB = false;
	public boolean hasHarvestedAB = false;
	public boolean hasPlantedAB = false;
	//Herb
	public boolean canHarvestHerbAA = false;
	public boolean canHarvestFlowerAF = false;
	//Raking booleans
	public boolean mustRakeAA = false;
	public boolean mustRakeAB = false;
	public boolean mustRakeAH = false;
	public boolean mustRakeAF = false;
	
	/**
	 * Compost Bins
	 */
	public int increaseWeed = 0;
	public boolean useCompost = false;
	public boolean waitForComp = false;
	
	
	//this method resets all the booleans on login. This will prevent small bugs.
	//This is due to the farming patches resetting on login, will not need this once saving is added.
	public void resetFarm() {
		farmingStatusA = 0;
		farmingStatusB = 0;
		farmingStatusF = 0;
		farmingStatusH = 0;
		hasEscavated = false;
		waitForComp = false;
		useCompost = false;
		increaseWeed = 0;
		canHarvestA = false;
		canHarvestB = false;
		canHarvestHerbA = false;
		canHarvestFlowerA = false;
		canHarvestTreeA = false;
		hasHarvestedA = false;
		hasHarvestedB = false;
		//Has planted booleans
		hasPlantedA = false;
		hasPlantedB = false;
		hasPlantedHerb = false;
		hasPlantedFlower = false;
		hasPlantedTree = false;
		//Must Rake booleans
		mustRakeA = false;
		mustRakeB = false;
		mustRakeH = false;
		mustRakeF = false;
		//Cath
		mustRakeCA = false;
		mustRakeCB = false;
		mustRakeCH = false;
		mustRakeCF = false;
		//seed variables
		potatoA = false;
		potatoB = false;
		melonA = false;
		melonB = false;
		sweetA = false;
		sweetB = false;
		guamA = false;
		snapA = false;
		torstol = false;
		gold = false;
		lily = false;
		magic = false;
		yew = false;
		chop = 0;
		/**
		 * Catherby Farming
		 */
		canHarvestCA = false;
		canHarvestCB = false;
		canHarvestHerbCA = false;
		canHarvestFlowerCA = false;
		hasHarvestedCA = false;
		hasHarvestedCB = false;
		//Has planted booleans
		hasPlantedCA = false;
		hasPlantedCB = false;
		hasPlantedHerbC = false;
		hasPlantedFlowerC = false;
		//seed variables
		potatoCA = false;
		potatoCB = false;
		melonCA = false;
		melonCB = false;
		sweetCA = false;
		sweetCB = false;
		guamCA = false;
		snapCA = false;
		goldC = false;
		lilyC = false;
		/**
		 * Ardougne Location
		 */
		allotmentA = 0;
		allotmentB = 0;
		allotmentH = 0;
		allotmentF = 0;
		//Harvesting booleans
		canHarvestAA = false;
		hasHarvestedAA = false;
		hasPlantedAA = false;
		canHarvestAB = false;
		hasHarvestedAB = false;
		hasPlantedAB = false;
		//Herb
		canHarvestHerbAA = false;
		canHarvestFlowerAF = false;
		//Raking booleans
		mustRakeAA = false;
		mustRakeAB = false;
		mustRakeAH = false;
		mustRakeAF = false;
	}

	
	//End of Farming.
	
	/**
	 * Construction
	 */
	public boolean portal1 = false;
	public boolean portal2 = false;
	public boolean portal3 = false;
	
	
	public boolean inBuildMode = false;
	public boolean canEnter = false;
	public boolean isOwner = false;
	public boolean hasLocked = false;
	public boolean hasRemovedObjects = false;
	
	public boolean hasHouse = false;
	
	/**
	 * Butlers
	 */
	public boolean hasBoughtDemon = false;
	public boolean spokeToDemon = false;
	public boolean spokeToAgent = false;
	
	/**
	 * 1 = Teak Frame
	 * 2 = Mahogany Frame
	 */
	public int portalTele1 = 0;
	public int portalTele2 = 0;
	public int portalTele3 = 0;
	public int portalFrame = 0;
	
	/**
	 * 1 = Crude chair
	 * 2 = Rocking chair
	 * 3 = Oak armchair
	 * 4 = Teak armchair
	 */
	
	public int chair = 0;
	public int throne = 0;
	public int tree = 0;
	public int bookcase = 0;
	public int firePlace = 0;
	public int altar = 0;
	
	public void refreshObjects(final int[] boundChuncks) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 50) {
					checkObjects(boundChuncks);
				} else if (loop == 100) {
					checkObjects(boundChuncks);
				} else if (loop == 150) {
					checkObjects(boundChuncks);
				} else if (loop == 200) {
					checkObjects(boundChuncks);
				} else if (loop == 500) {
					checkObjects(boundChuncks);
				}
					loop++;
					}
				}, 0, 1);
			}
	public void checkObjects(final int[] boundChuncks) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 1) {
					closeInterfaces();
					World.spawnObject(new WorldObject(13405, 10, 0, boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 35, 0), true);
					if (chair == 1) {
						World.spawnObject(new WorldObject(13581, 10, 0, boundChuncks[0] * 8 + 42, boundChuncks[1] * 8 + 36, 0), true);
						World.spawnObject(new WorldObject(13581, 10, 0, boundChuncks[0] * 8 + 44, boundChuncks[1] * 8 + 35, 0), true);
						World.spawnObject(new WorldObject(13581, 10, 0, boundChuncks[0] * 8 + 45, boundChuncks[1] * 8 + 36, 0), true);
					} else if (chair == 2) {
						World.spawnObject(new WorldObject(13583, 10, 0, boundChuncks[0] * 8 + 42, boundChuncks[1] * 8 + 36, 0), true);
						World.spawnObject(new WorldObject(13583, 10, 0, boundChuncks[0] * 8 + 44, boundChuncks[1] * 8 + 35, 0), true);
						World.spawnObject(new WorldObject(13583, 10, 0, boundChuncks[0] * 8 + 45, boundChuncks[1] * 8 + 36, 0), true);
					} else if (chair == 3) {
						World.spawnObject(new WorldObject(13584, 10, 0, boundChuncks[0] * 8 + 42, boundChuncks[1] * 8 + 36, 0), true);
						World.spawnObject(new WorldObject(13584, 10, 0, boundChuncks[0] * 8 + 44, boundChuncks[1] * 8 + 35, 0), true);
						World.spawnObject(new WorldObject(13584, 10, 0, boundChuncks[0] * 8 + 45, boundChuncks[1] * 8 + 36, 0), true);
					} else if (chair == 4) {
						World.spawnObject(new WorldObject(13587, 10, 0, boundChuncks[0] * 8 + 42, boundChuncks[1] * 8 + 36, 0), true);
						World.spawnObject(new WorldObject(13587, 10, 0, boundChuncks[0] * 8 + 44, boundChuncks[1] * 8 + 35, 0), true);
						World.spawnObject(new WorldObject(13587, 10, 0, boundChuncks[0] * 8 + 45, boundChuncks[1] * 8 + 36, 0), true);
					}
					if (throne == 1) {
						World.spawnObject(new WorldObject(13665, 10, 3, boundChuncks[0] * 8 + 25, boundChuncks[1] * 8 + 36, 0), true);
						World.spawnObject(new WorldObject(13665, 10, 3, boundChuncks[0] * 8 + 25, boundChuncks[1] * 8 + 35, 0), true);
					} else if (throne == 2) {
						World.spawnObject(new WorldObject(13670, 10, 3, boundChuncks[0] * 8 + 25, boundChuncks[1] * 8 + 36, 0), true);
						World.spawnObject(new WorldObject(13670, 10, 3, boundChuncks[0] * 8 + 25, boundChuncks[1] * 8 + 35, 0), true);
					} else if (throne == 3) {
						World.spawnObject(new WorldObject(13671, 10, 3, boundChuncks[0] * 8 + 25, boundChuncks[1] * 8 + 36, 0), true);
						World.spawnObject(new WorldObject(13671, 10, 3, boundChuncks[0] * 8 + 25, boundChuncks[1] * 8 + 35, 0), true);
					}
					if (tree == 1) {
						World.spawnObject(new WorldObject(13413, 10, 0, boundChuncks[0] * 8 + 33, boundChuncks[1] * 8 + 37, 0), true);
						World.spawnObject(new WorldObject(13413, 10, 0, boundChuncks[0] * 8 + 38, boundChuncks[1] * 8 + 38, 0), true);
					} else if (tree == 2) {
						World.spawnObject(new WorldObject(13414, 10, 0, boundChuncks[0] * 8 + 33, boundChuncks[1] * 8 + 37, 0), true);
						World.spawnObject(new WorldObject(13414, 10, 0, boundChuncks[0] * 8 + 38, boundChuncks[1] * 8 + 38, 0), true);
					} else if (tree == 3) {
						World.spawnObject(new WorldObject(13416, 10, 0, boundChuncks[0] * 8 + 33, boundChuncks[1] * 8 + 37, 0), true);
						World.spawnObject(new WorldObject(13416, 10, 0, boundChuncks[0] * 8 + 38, boundChuncks[1] * 8 + 38, 0), true);
					} else if (tree == 4) {
						World.spawnObject(new WorldObject(13417, 10, 0, boundChuncks[0] * 8 + 33, boundChuncks[1] * 8 + 37, 0), true);
						World.spawnObject(new WorldObject(13417, 10, 0, boundChuncks[0] * 8 + 38, boundChuncks[1] * 8 + 38, 0), true);
					}
					if (bookcase == 1) {
						World.spawnObject(new WorldObject(13597, 10, 0, boundChuncks[0] * 8 + 40, boundChuncks[1] * 8 + 33, 0), true);
						World.spawnObject(new WorldObject(13597, 10, 2, boundChuncks[0] * 8 + 47, boundChuncks[1] * 8 + 33, 0), true);	
						//World.spawnObject(new WorldObject(13597, 10, 2, boundChuncks[0] * 8 + 39, boundChuncks[1] * 8 + 30, 0), true);
					} else if (bookcase == 2) {
						World.spawnObject(new WorldObject(13598, 10, 0, boundChuncks[0] * 8 + 40, boundChuncks[1] * 8 + 33, 0), true);
						World.spawnObject(new WorldObject(13598, 10, 2, boundChuncks[0] * 8 + 47, boundChuncks[1] * 8 + 33, 0), true);
						//World.spawnObject(new WorldObject(13598, 10, 2, boundChuncks[0] * 8 + 39, boundChuncks[1] * 8 + 30, 0), true);
					} else if (bookcase == 3) {
						World.spawnObject(new WorldObject(13599, 10, 0, boundChuncks[0] * 8 + 40, boundChuncks[1] * 8 + 33, 0), true);
						World.spawnObject(new WorldObject(13599, 10, 2, boundChuncks[0] * 8 + 47, boundChuncks[1] * 8 + 33, 0), true);	
						//World.spawnObject(new WorldObject(13599, 10, 2, boundChuncks[0] * 8 + 39, boundChuncks[1] * 8 + 30, 0), true);
					}
					if (firePlace == 1) {
						World.spawnObject(new WorldObject(13609, 10, 1, boundChuncks[0] * 8 + 43, boundChuncks[1] * 8 + 39, 0), true);	
					} else if (firePlace == 2) {
						World.spawnObject(new WorldObject(13611, 10, 1, boundChuncks[0] * 8 + 43, boundChuncks[1] * 8 + 39, 0), true);	
					} else if (firePlace == 3) {
						World.spawnObject(new WorldObject(13613, 10, 1, boundChuncks[0] * 8 + 43, boundChuncks[1] * 8 + 39, 0), true);	
					} else if (firePlace == 4) {
						World.spawnObject(new WorldObject(13610, 10, 1, boundChuncks[0] * 8 + 43, boundChuncks[1] * 8 + 39, 0), true);	
					} else if (firePlace == 5) {
						World.spawnObject(new WorldObject(13612, 10, 1, boundChuncks[0] * 8 + 43, boundChuncks[1] * 8 + 39, 0), true);	
					} else if (firePlace == 6) {
						World.spawnObject(new WorldObject(13614, 10, 1, boundChuncks[0] * 8 + 43, boundChuncks[1] * 8 + 39, 0), true);	
					}
					switch (portalTele1) {
					case 1:
						portal1 = true;
						World.spawnObject(new WorldObject(13622, 10, 1, boundChuncks[0] * 8 + 32, boundChuncks[1] * 8 + 43, 0), true);
						break;
					case 2:
						portal1 = true;
						World.spawnObject(new WorldObject(13623, 10, 1, boundChuncks[0] * 8 + 32, boundChuncks[1] * 8 + 43, 0), true);
						break;
					case 3:
						portal1 = true;
						World.spawnObject(new WorldObject(13624, 10, 1, boundChuncks[0] * 8 + 32, boundChuncks[1] * 8 + 43, 0), true);
						break;
					case 4:
						portal1 = true;
						World.spawnObject(new WorldObject(13625, 10, 1, boundChuncks[0] * 8 + 32, boundChuncks[1] * 8 + 43, 0), true);
						break;
					case 5:
						portal1 = true;
						World.spawnObject(new WorldObject(13627, 10, 1, boundChuncks[0] * 8 + 32, boundChuncks[1] * 8 + 43, 0), true);
						break;
					}
					switch (portalTele2) {
					case 6:
						portal2 = true;
						World.spawnObject(new WorldObject(13622, 10, 2, boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 47, 0), true);
						break;
					case 7:
						portal2 = true;
						World.spawnObject(new WorldObject(13623, 10, 2, boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 47, 0), true);
						break;
					case 8:
						portal2 = true;
						World.spawnObject(new WorldObject(13624, 10, 2, boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 47, 0), true);
						break;
					case 9:
						portal2 = true;
						World.spawnObject(new WorldObject(13625, 10, 2, boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 47, 0), true);
						break;
					case 10:
						portal2 = true;
						World.spawnObject(new WorldObject(13627, 10, 2, boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 47, 0), true);
						break;
					}
					switch (portalTele3) {
					case 11:
						portal3 = true;
						World.spawnObject(new WorldObject(13622, 10, 1, boundChuncks[0] * 8 + 39, boundChuncks[1] * 8 + 43, 0), true);
						break;
					case 12:
						portal3 = true;
						World.spawnObject(new WorldObject(13623, 10, 1, boundChuncks[0] * 8 + 39, boundChuncks[1] * 8 + 43, 0), true);
						break;
					case 13:
						portal3 = true;
						World.spawnObject(new WorldObject(13624, 10, 1, boundChuncks[0] * 8 + 39, boundChuncks[1] * 8 + 43, 0), true);
						break;
					case 14:
						portal3 = true;
						World.spawnObject(new WorldObject(13625, 10, 1, boundChuncks[0] * 8 + 39, boundChuncks[1] * 8 + 43, 0), true);
						break;
					case 15:
						portal3 = true;
						World.spawnObject(new WorldObject(13627, 10, 1, boundChuncks[0] * 8 + 39, boundChuncks[1] * 8 + 43, 0), true);
						break;
					}
					switch (altar) {
					case 1:
						World.spawnObject(new WorldObject(13190, 10, 0, boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 26, 0), true);
						break;
					case 2:
						World.spawnObject(new WorldObject(13196, 10, 0, boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 26, 0), true);
						break;
					case 3:
						World.spawnObject(new WorldObject(13199, 10, 0, boundChuncks[0] * 8 + 35, boundChuncks[1] * 8 + 26, 0), true);
						break;
					}
					
					}
					loop++;
					}
				}, 0, 1);
			}//TODO
	
	public int boneOnAltar = 0;
	
	public void executeAltar() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 1) {
					getDialogueManager().startDialogue("MakeAltar");// Couldnt be bothered to add another option 
				}
				loop++;
			}
		}, 0, 1);
	}
	
	/**
	 * Random Events
	 */
	public int playersX, playersY, playersP;

	
	public int tagged5Posts = 0;
	
	public String randomRune;
	
	/*Use this to teleport players to random event.
	 		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1972, 5046, 0));
			player.setNextForceTalk(new ForceTalk("Oh no!"));
			player.randomRune();
			player.tagged5Posts = 0;
			player.out("Tag the "+player.randomRune+" rune pinball post.");
	 */

	public void randomRune() {
        String[] runeTags = {"Air", "Earth", "Fire", "Nature", "Water"};
        Random rand = new Random();
        randomRune = runeTags [rand.nextInt( runeTags.length)];
	}
	
	public int conRoom = 0;
	
	public void buildRoomSouth() {//Doesnt work
		switch (conRoom) {
	case 1:
		rooms.add(new RoomReference(Room.PARLOUR, 4, 3, 0, 0));
		break;
	case 2:
		rooms.add(new RoomReference(Room.KITCHEN, getChunkX(), getChunkY() - 1, 0, 0));	
		break;
	case 3:
		rooms.add(new RoomReference(Room.DININGROOM, getChunkX(), getChunkY() - 1, 0, 0));	
		break;
		}
	}
	
	public boolean toggleUpdate = false;
	public void switchUpdate() {
		if (toggleUpdate == false) {
			toggleUpdate = true;
		} else {
			toggleUpdate = false;
		}
	}
	
	public void recentUpdateInter() {
		getInterfaceManager().sendInterface(1069);
		getPackets().sendIComponentText(1069, 16, "Latest Update");
		if (hasSetupCannon == true && getInventory().getFreeSlots() < 4) {
			getPackets().sendIComponentText(1069, 17, "Your cannon has been added to your bank.");
		} else {
		getPackets().sendIComponentText(1069, 17, Settings.recentUpdate);
		}
	}
	
	public String HoL = "Lower";
	
	
	/**
	 * Location System
	 */
	public String location = "Unknown";
	
	public boolean hasLocation = false;
	public void toggleLocation() {
		if (hasLocation == false) {
			hasLocation = true;
		} else {
			hasLocation = false;
		}
	}
	
	public int coinsInPouch;
	
	
	public boolean hasMaxCape = false;
	public void recieveMaxCape() {
			if (getSkills().getLevel(0) >= 99 && getSkills().getLevel(1) >= 99 && getSkills().getLevel(2) >= 99
			&& getSkills().getLevel(3) >= 99 && getSkills().getLevel(4) >= 99 && getSkills().getLevel(5) >= 99
			&& getSkills().getLevel(6) >= 99 && hasMaxCape == false) {
				getBank().addItem(20767, 1, true);
				out("You have recieved A Max Cape for achieving 99 in every combat skill.");
				hasMaxCape = true;
			}
	}
	
	private boolean inClops;
	public int wGuildTokens;

	public int getWGuildTokens() {
	return wGuildTokens;
	}

	public void setWGuildTokens(int tokens) {
	wGuildTokens = tokens;
	}

	public boolean inClopsRoom() {
	return inClops;
	}

	public void setInClopsRoom(boolean in) {
	inClops = in;
	}
	
	/**
	 * DropLog
	 */
	public int bandosChest = 0;
	public int bandosTassets = 0;
	public int bandosHilt = 0;
	
	public int armadylHelm = 0;
	public int armadylPlate = 0;
	public int armadylLegs = 0;
	public int armadylHilt = 0;
	
	public int torvaHelm;
	public int torvaPlate;
	public int torvaLegs;
	
	public int pernixCowl;
	public int pernixBody;
	public int pernixChaps;
	
	public int virtusMask;
	public int virtusTop;
	public int virtusLegs;
	
	public int dragonClaws;
	public int bones;
	
	public int dharoksHelm;
	public int dharoksPlate;
	public int dharoksLegs;
	public int dharoksGreataxe;
	
	
	/*
	 * Dwarf Cannon
	 */
	public boolean hasLoadedCannon = false;
	
	public boolean isShooting = false;
	
	public boolean hasSetupCannon = false;
	
	public boolean hasSetupRoyalCannon = false;
	
	/**
	 * Dwarf Cannon quest
	 */
	public int fixedRailings = 0;
	public boolean fixedRailing1 = false;
	public boolean fixedRailing2 = false;
	public boolean fixedRailing3 = false;
	public boolean fixedRailing4 = false;
	public boolean fixedRailing5 = false;
	public boolean fixedRailing6 = false;
	
	public boolean completedRailingTask = false;
	public boolean spokeToNu = false;
	public boolean completedDwarfCannonQuest = false;
	
	/**
	 * Quest Points
	 */
	public int questPoints = 0;
	
	/**
	 * Lootshare
	 */
	public boolean inLoot = false;
	
	/**
	 * Prestige System
	 */
	
	public void resetPlayer() {
		for (int skill = 0; skill < 25; skill++)
		getSkills().setXp(skill, 1);
		getSkills().init();
	}
	
	public void resetPlayer2() {
		for (int skill = 0; skill < 25; skill++)
		getSkills().set(skill, 1);
		getSkills().setXp(3, 1154);
		getSkills().set(3, 10);
		getSkills().init();
	}
	
	public boolean Prestige1;
	
	/*
	 * Determines what prestige the player is on.
	 */
	public int prestigeNumber; 
	/*
	 * Players prestige points.
	 */
	public int prestigePoints;
	
	public boolean isPrestige1() {
		return Prestige1;
	}

	public void setPrestige1() {
		if(!Prestige1) {
			Prestige1 = true;
		}
	}
	/*
	 * Completing the first prestige.
	 */
	public void setCompletedPrestigeOne() {
		if (prestigeNumber == 0) {
		prestigeNumber++;
		resetPlayer();
		resetPlayer2();
		resetHerbXp();
		Prestige1 = false;
	    setNextAnimation(new Animation(1914));
		setNextGraphics(new Graphics(92));
		setNextForceTalk(new ForceTalk("Ahhh! What is this?"));
		getPackets().sendGameMessage("You feel a force reach into your soul, You gain One Prestige Token.");
		World.sendWorldMessage("<img=7><col=ff0000>News: "+getDisplayName()+" has just prestiged! he has now prestiged "+prestigeNumber+" times.", false);
		if (prestigeNumber == 5) {
			getPackets().sendGameMessage("<col=ff0000>You have reached the last prestige, you can no longer prestige.");
			}
		}
	}
	
	/*
	 * Opens prestige shop.
	 */
	public void prestigeShops() {
		if (prestigeNumber == 0) {
			getPackets().sendGameMessage("You need to have prestiged to gain access to this shop.");
		} else if (prestigeNumber == 1) {
			ShopsHandler.openShop(this, 35);
		} else if (prestigeNumber == 2) {
			ShopsHandler.openShop(this, 36);
		} else if (prestigeNumber == 3) {
			ShopsHandler.openShop(this, 37);
		} else if (prestigeNumber == 4) {
			ShopsHandler.openShop(this, 38);
		} else if (prestigeNumber == 5) {
			ShopsHandler.openShop(this, 39);
		}
	}
	
	/*
	 * Completes the second prestige.
	 */
	public void setCompletedPrestige2() {
		if (prestigeNumber >= 1) {
			resetCbXp();
			resetCbSkills();
			resetSummon();
			resetSummonXp();
			prestigeNumber++;
			Prestige1 = false;
		    setNextAnimation(new Animation(1914));
			setNextGraphics(new Graphics(92));
			setNextForceTalk(new ForceTalk("Ahhh! What is this!?"));
			getPackets().sendGameMessage("You feel a force reach into your soul, You gain One Prestige Token.");
			World.sendWorldMessage("<img=7><col=ff0000>News: "+getDisplayName()+" has just prestiged! he has now prestiged "+prestigeNumber+" times.", false);
		}
	}
	
	/*
	 * Resets skills.
	 */
	public void resetCbXp() {
		for (int skill = 0; skill < 7; skill++)
		getSkills().setXp(skill, 1);
		getSkills().init();
	}
	
	public void resetHerbXp() {
		getSkills().set(15, 3);
		getSkills().setXp(15, 174);
	}
	
	public void resetSummon() {
		getSkills().set(23, 1);
		getSkills().init();
	}
	public void resetSummonXp() {
		getSkills().setXp(23, 1);
		getSkills().init();
	}
	
	public void resetCbSkills() {
		for (int skill = 0; skill < 7; skill++)
		getSkills().set(skill, 1);
		getSkills().setXp(3, 1154);
		getSkills().set(3, 10);
		getSkills().init();
	}
	
	/*
	 * Sets the prestige.
	 */
	public void prestige() {
		for (int i = 0; i < 24; i++) {
			if (getSkills().getLevel(i) >= 99 && getSkills().getLevel(Skills.DUNGEONEERING) >= 120) {
				setPrestige1();
				return;
			}
		}
	}
	
	/**
	 * Completionist cape requirements, Very Messy.
	 */
	
	/*
	 * Skiller Cape Requirements (Slightly Different)
	 */
	
	public int voteee, ore, barbLap, cook, stones;
	
	/*
	 * Checks Skillers Cape Requirements.
	 */
	public void skillCape() {
		if (fish >= 749 && logs >= 249 && wc >= 349 && chest >= 14 && con >= 749 && slayer >= 699 && thieve >= 699 && agility >= 99 && ore >= 999 && barbLap >= 89 && cook >= 449
				&& getSkills().getLevel(Skills.COOKING) == 99 && getSkills().getLevel(Skills.WOODCUTTING) == 99 && getSkills().getLevel(Skills.FLETCHING) == 99 && getSkills().getLevel(Skills.FISHING) == 99
				&& getSkills().getLevel(Skills.FIREMAKING) == 99 && getSkills().getLevel(Skills.CRAFTING) == 99 && getSkills().getLevel(Skills.SMITHING) == 99 && getSkills().getLevel(Skills.MINING) == 99
				&& getSkills().getLevel(Skills.HERBLORE) == 99 && getSkills().getLevel(Skills.AGILITY) == 99 && getSkills().getLevel(Skills.THIEVING) == 99 && getSkills().getLevel(Skills.SLAYER) == 99
				&& getSkills().getLevel(Skills.FARMING) == 99 && getSkills().getLevel(Skills.RUNECRAFTING) == 99 && stones >= 749) {
			setCompletedSkill();
		} else {
			getPackets().sendGameMessage("You need to have completed all the requirements to be able to access the Skillers Cape.");
		}
	}
	
	/*
	 * Completionist Cape Requirement ints.
	 */
	public int fish, logs, wc, chest, con, slayer, thieve, farm, agility;
	
	/*
	 * Checks Completionist Cape Requirements.
	 */
	
	public void completedCompletionistCape() {
		for (int i = 0; i < 23; i++) {
			if (this.getSkills().getLevel(i) >= 99 && getSkills().getLevel(24) >= 120 && fish >= 749 && logs >= 249 && wc >= 349
					&& chest >= 14 && con >= 749 && slayer >= 699 && thieve >= 699 && agility >= 99) {
				setCompletedComp();
				return;
			} else {
				getPackets().sendGameMessage("You need to have completed all the requirements to wear the completionist cape.");
				return;
			}
		}
	}
	
	public void setCompletedComp() {
		if(!completedComp) {
			completedComp = true;
		}
	}
	
	private boolean completedComp;
	
	public boolean isCompletedComp() {
		return completedComp;
	}
	
	/*
	 * Checks if players has completed the requirements then displays a message.
	 */
	public String doneComp;
	public void checkIfCompleted() {
		if (isCompletedComp() == false) {
			doneComp = "You have not completed all the requirements.";
		} else {
			doneComp = "You have completed all the requirements.";
		}
	}
	

	

	/**
	 * money pouch
	 */
	//public int moneyInTrade;
	public boolean addedViaPouch;
	public int money = 0;

	public void refreshMoneyPouch() {
		getPackets().sendRunScript(5560, money);
	}

	public void setTemporaryMoveType(int temporaryMovementType) {
		this.temporaryMovementType = temporaryMovementType;
	}

	public LocalPlayerUpdate getLocalPlayerUpdate() {
		return localPlayerUpdate;
	}

	public LocalNPCUpdate getLocalNPCUpdate() {
		return localNPCUpdate;
	}

	public int getDisplayMode() {
		return displayMode;
	}

	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	public void setPacketsDecoderPing(long packetsDecoderPing) {
		this.packetsDecoderPing = packetsDecoderPing;
	}

	public long getPacketsDecoderPing() {
		return packetsDecoderPing;
	}

	public Session getSession() {
		return session;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public boolean clientHasLoadedMapRegion() {
		return clientLoadedMapRegion;
	}

	public void setClientHasLoadedMapRegion() {
		clientLoadedMapRegion = true;
	}

	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Skills getSkills() {
		return skills;
	}

	public byte getRunEnergy() {
		return runEnergy;
	}

	public void drainRunEnergy() {
		setRunEnergy(runEnergy - 1);
	}

	public void setRunEnergy(int runEnergy) {
		this.runEnergy = (byte) runEnergy;
		getPackets().sendRunEnergy();
	}

	public boolean isResting() {
		return resting;
	}

	public void setResting(boolean resting) {
		this.resting = resting;
		sendRunButtonConfig();
	}

	public ActionManager getActionManager() {
		return actionManager;
	}

	public void setCoordsEvent(CoordsEvent coordsEvent) {
		this.coordsEvent = coordsEvent;
	}

	public DialogueManager getDialogueManager() {
		return dialogueManager;
	}

	public CombatDefinitions getCombatDefinitions() {
		return combatDefinitions;
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

	public void sendSoulSplit(final Hit hit, final Entity user) {
		final Player target = this;
		if (hit.getDamage() > 0)
			World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		user.heal(hit.getDamage() / 5);
		prayer.drainPrayer(hit.getDamage() / 5);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0)
					World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0, 0);
			}
		}, 0);
	}

	//TODO
	@Override
	public void handleIngoingHit(final Hit hit) {
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		if(invulnerable) {
			hit.setDamage(0);
			return;
		}
		if (auraManager.usingPenance()) {
			int amount = (int) (hit.getDamage() * 0.2);
			if (amount > 0)
				prayer.restorePrayer(amount);
		}
		Entity source = hit.getSource();
		if (source instanceof NPC){
			NPC npc = (NPC) source;
		if (!Slayer.checkRequirement(this,SlayerMonsters.forId(npc.getId()))){
			return;
		}
		}
		if (source == null)
			return;
		if (polDelay > Utils.currentTimeMillis())
			hit.setDamage((int) (hit.getDamage() * 0.5));
		if (prayer.hasPrayersOn() && hit.getDamage() != 0) {
			if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				if (prayer.usingPrayer(0, 17))
					hit.setDamage((int) (hit.getDamage() * source
							.getMagePrayerMultiplier()));
				else if (prayer.usingPrayer(1, 7)) {
					int deflectedDamage = source instanceof Nex ? 0
							: (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source
							.getMagePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2228));
						setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				if (prayer.usingPrayer(0, 18))
					hit.setDamage((int) (hit.getDamage() * source
							.getRangePrayerMultiplier()));
				else if (prayer.usingPrayer(1, 8)) {
					int deflectedDamage = source instanceof Nex ? 0
							: (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source
							.getRangePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2229));
						setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				if (prayer.usingPrayer(0, 19))
					hit.setDamage((int) (hit.getDamage() * source
							.getMeleePrayerMultiplier()));
				else if (prayer.usingPrayer(1, 9)) {
					int deflectedDamage = source instanceof Nex ? 0
							: (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source
							.getMeleePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2230));
						setNextAnimation(new Animation(12573));
					}
				}
			}
		}
		if (hit.getDamage() >= 200) {
			if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS]
								/ 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage,
							HitLook.ABSORB_DAMAGE));
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS]
								/ 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage,
							HitLook.ABSORB_DAMAGE));
				}
			} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS]
								/ 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage,
							HitLook.ABSORB_DAMAGE));
				}
			}
		}
		int shieldId = equipment.getShieldId();
		if (shieldId == 13742) { // elsyian
			if (Utils.getRandom(100) <= 70)
				hit.setDamage((int) (hit.getDamage() * 0.75));
		} else if (shieldId == 13740) { // divine
			int drain = (int) (Math.ceil(hit.getDamage() * 0.3) / 2);
			if (prayer.getPrayerpoints() >= drain) {
				hit.setDamage((int) (hit.getDamage() * 0.70));
				prayer.drainPrayer(drain);
			}
		}
		if (castedVeng && hit.getDamage() >= 4) {
			castedVeng = false;
			setNextForceTalk(new ForceTalk("Taste vengeance!"));
			source.applyHit(new Hit(this, (int) (hit.getDamage() * 0.75),
					HitLook.REGULAR_DAMAGE));
		}
		if (source instanceof Player) {
			final Player p2 = (Player) source;
			if (p2.prayer.hasPrayersOn()) {
				if (p2.prayer.usingPrayer(0, 24)) { // smite
					int drain = hit.getDamage() / 4;
					if (drain > 0)
						prayer.drainPrayer(drain);
				} else {
					if (hit.getDamage() == 0)
						return;
					if (!p2.prayer.isBoostedLeech()) {
						if (hit.getLook() == HitLook.MELEE_DAMAGE) {
							if (p2.prayer.usingPrayer(1, 19)) {
								if (Utils.getRandom(4) == 0) {
									p2.prayer.increaseTurmoilBonus(this);
									p2.prayer.setBoostedLeech(true);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 1)) { // sap att
								if (Utils.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(0)) {
										p2.getPackets()
										.sendGameMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(0);
										p2.getPackets()
										.sendGameMessage(
												"Your curse drains Attack from the enemy, boosting your Attack.",
												true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2214));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2215, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2216));
										}
									}, 1);
									return;
								}
							} else {
								if (p2.prayer.usingPrayer(1, 10)) {
									if (Utils.getRandom(7) == 0) {
										if (p2.prayer.reachedMax(3)) {
											p2.getPackets()
											.sendGameMessage(
													"Your opponent has been weakened so much that your leech curse has no effect.",
													true);
										} else {
											p2.prayer.increaseLeechBonus(3);
											p2.getPackets()
											.sendGameMessage(
													"Your curse drains Attack from the enemy, boosting your Attack.",
													true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
										World.sendProjectile(p2, this, 2231,
												35, 35, 20, 5, 0, 0);
										WorldTasksManager.schedule(
												new WorldTask() {
													@Override
													public void run() {
														setNextGraphics(new Graphics(
																2232));
													}
												}, 1);
										return;
									}
								}
								if (p2.prayer.usingPrayer(1, 14)) {
									if (Utils.getRandom(7) == 0) {
										if (p2.prayer.reachedMax(7)) {
											p2.getPackets()
											.sendGameMessage(
													"Your opponent has been weakened so much that your leech curse has no effect.",
													true);
										} else {
											p2.prayer.increaseLeechBonus(7);
											p2.getPackets()
											.sendGameMessage(
													"Your curse drains Strength from the enemy, boosting your Strength.",
													true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
										World.sendProjectile(p2, this, 2248,
												35, 35, 20, 5, 0, 0);
										WorldTasksManager.schedule(
												new WorldTask() {
													@Override
													public void run() {
														setNextGraphics(new Graphics(
																2250));
													}
												}, 1);
										return;
									}
								}

							}
						}
						if (hit.getLook() == HitLook.RANGE_DAMAGE) {
							if (p2.prayer.usingPrayer(1, 2)) { // sap range
								if (Utils.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(1)) {
										p2.getPackets()
										.sendGameMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(1);
										p2.getPackets()
										.sendGameMessage(
												"Your curse drains Range from the enemy, boosting your Range.",
												true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2217));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2218, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2219));
										}
									}, 1);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 11)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.prayer.reachedMax(4)) {
										p2.getPackets()
										.sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(4);
										p2.getPackets()
										.sendGameMessage(
												"Your curse drains Range from the enemy, boosting your Range.",
												true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2236, 35,
											35, 20, 5, 0, 0);
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
							if (p2.prayer.usingPrayer(1, 3)) { // sap mage
								if (Utils.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(2)) {
										p2.getPackets()
										.sendGameMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(2);
										p2.getPackets()
										.sendGameMessage(
												"Your curse drains Magic from the enemy, boosting your Magic.",
												true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2220));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2221, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2222));
										}
									}, 1);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 12)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.prayer.reachedMax(5)) {
										p2.getPackets()
										.sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.prayer.increaseLeechBonus(5);
										p2.getPackets()
										.sendGameMessage(
												"Your curse drains Magic from the enemy, boosting your Magic.",
												true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2240, 35,
											35, 20, 5, 0, 0);
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

						if (p2.prayer.usingPrayer(1, 13)) { // leech defence
							if (Utils.getRandom(10) == 0) {
								if (p2.prayer.reachedMax(6)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.prayer.increaseLeechBonus(6);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Defence from the enemy, boosting your Defence.",
											true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								World.sendProjectile(p2, this, 2244, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2246));
									}
								}, 1);
								return;
							}
						}

						if (p2.prayer.usingPrayer(1, 15)) {
							if (Utils.getRandom(10) == 0) {
								if (getRunEnergy() <= 0) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.setRunEnergy(p2.getRunEnergy() > 90 ? 100
											: p2.getRunEnergy() + 10);
									setRunEnergy(p2.getRunEnergy() > 10 ? getRunEnergy() - 10
											: 0);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								World.sendProjectile(p2, this, 2256, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2258));
									}
								}, 1);
								return;
							}
						}

						if (p2.prayer.usingPrayer(1, 16)) {
							if (Utils.getRandom(10) == 0) {
								if (combatDefinitions
										.getSpecialAttackPercentage() <= 0) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.combatDefinitions.restoreSpecialAttack();
									combatDefinitions
									.desecreaseSpecialAttack(10);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								World.sendProjectile(p2, this, 2252, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2254));
									}
								}, 1);
								return;
							}
						}

						if (p2.prayer.usingPrayer(1, 4)) { // sap spec
							if (Utils.getRandom(10) == 0) {
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2223));
								p2.prayer.setBoostedLeech(true);
								if (combatDefinitions
										.getSpecialAttackPercentage() <= 0) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									combatDefinitions
									.desecreaseSpecialAttack(10);
								}
								World.sendProjectile(p2, this, 2224, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2225));
									}
								}, 1);
								return;
							}
						}
					}
				}
			}
		} else {
			NPC n = (NPC) source;
			if (n.getId() == 13448)
				sendSoulSplit(hit, n);
		}
	}

	@Override
	public void sendDeath(final Entity source) {
		if (prayer.hasPrayersOn()
				&& getTemporaryAttributtes().get("startedDuel") != Boolean.TRUE) {
			if (prayer.usingPrayer(0, 22)) {
				setNextGraphics(new Graphics(437));
				final Player target = this;
				if (isAtMultiArea()) {
					for (int regionId : getMapRegionsIds()) {
						List<Integer> playersIndexes = World
								.getRegion(regionId).getPlayerIndexes();
						if (playersIndexes != null) {
							for (int playerIndex : playersIndexes) {
								Player player = World.getPlayers().get(
										playerIndex);
								if (player == null
										|| !player.hasStarted()
										|| player.isDead()
										|| player.hasFinished()
										|| !player.withinDistance(this, 1)
										|| !player.isCanPvp()
										|| !target.getControlerManager()
										.canHit(player))
									continue;
								player.applyHit(new Hit(
										target,
										Utils.getRandom((int) (skills
												.getLevelForXp(Skills.PRAYER) * 2.5)),
												HitLook.REGULAR_DAMAGE));
							}
						}
						List<Integer> npcsIndexes = World.getRegion(regionId)
								.getNPCsIndexes();
						if (npcsIndexes != null) {
							for (int npcIndex : npcsIndexes) {
								NPC npc = World.getNPCs().get(npcIndex);
								if (npc == null
										|| npc.isDead()
										|| npc.hasFinished()
										|| !npc.withinDistance(this, 1)
										|| !npc.getDefinitions()
										.hasAttackOption()
										|| !target.getControlerManager()
										.canHit(npc))
									continue;
								npc.applyHit(new Hit(
										target,
										Utils.getRandom((int) (skills
												.getLevelForXp(Skills.PRAYER) * 2.5)),
												HitLook.REGULAR_DAMAGE));
							}
						}
					}
				} else {
					if (source != null && source != this && !source.isDead()
							&& !source.hasFinished()
							&& source.withinDistance(this, 1))
						source.applyHit(new Hit(target, Utils
								.getRandom((int) (skills
										.getLevelForXp(Skills.PRAYER) * 2.5)),
										HitLook.REGULAR_DAMAGE));
				}
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1, target.getY(),
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1, target.getY(),
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX(), target.getY() - 1,
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX(), target.getY() + 1,
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1,
										target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1,
										target.getY() + 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1,
										target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1,
										target.getY() + 1, target.getPlane()));
					}
				});
			} else if (prayer.usingPrayer(1, 17)) {
				World.sendProjectile(this, new WorldTile(getX() + 2,
						getY() + 2, getPlane()), 2260, 24, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() + 2, getY(),
						getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() + 2,
						getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);

				World.sendProjectile(this, new WorldTile(getX() - 2,
						getY() + 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() - 2, getY(),
						getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() - 2,
						getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);

				World.sendProjectile(this, new WorldTile(getX(), getY() + 2,
						getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX(), getY() - 2,
						getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				final Player target = this;
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						setNextGraphics(new Graphics(2259));

						if (isAtMultiArea()) {
							for (int regionId : getMapRegionsIds()) {
								List<Integer> playersIndexes = World.getRegion(
										regionId).getPlayerIndexes();
								if (playersIndexes != null) {
									for (int playerIndex : playersIndexes) {
										Player player = World.getPlayers().get(
												playerIndex);
										if (player == null
												|| !player.hasStarted()
												|| player.isDead()
												|| player.hasFinished()
												|| !player.isCanPvp()
												|| !player.withinDistance(
														target, 2)
														|| !target
														.getControlerManager()
														.canHit(player))
											continue;
										player.applyHit(new Hit(
												target,
												Utils.getRandom((skills
														.getLevelForXp(Skills.PRAYER) * 3)),
														HitLook.REGULAR_DAMAGE));
									}
								}
								List<Integer> npcsIndexes = World.getRegion(
										regionId).getNPCsIndexes();
								if (npcsIndexes != null) {
									for (int npcIndex : npcsIndexes) {
										NPC npc = World.getNPCs().get(npcIndex);
										if (npc == null
												|| npc.isDead()
												|| npc.hasFinished()
												|| !npc.withinDistance(target,
														2)
														|| !npc.getDefinitions()
														.hasAttackOption()
														|| !target
														.getControlerManager()
														.canHit(npc))
											continue;
										npc.applyHit(new Hit(
												target,
												Utils.getRandom((skills
														.getLevelForXp(Skills.PRAYER) * 3)),
														HitLook.REGULAR_DAMAGE));
									}
								}
							}
						} else {
							if (source != null && source != target
									&& !source.isDead()
									&& !source.hasFinished()
									&& source.withinDistance(target, 2))
								source.applyHit(new Hit(
										target,
										Utils.getRandom((skills
												.getLevelForXp(Skills.PRAYER) * 3)),
												HitLook.REGULAR_DAMAGE));
						}

						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 2, getY() + 2,
										getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 2, getY() - 2,
										getPlane()));

						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 2, getY() + 2,
										getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 2, getY() - 2,
										getPlane()));

						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX(), getY() + 2, getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX(), getY() - 2, getPlane()));

						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 1, getY() + 1,
										getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 1, getY() - 1,
										getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 1, getY() + 1,
										getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 1, getY() - 1,
										getPlane()));
					}
				});
			}
		}
		setNextAnimation(new Animation(-1));
		if (!controlerManager.sendDeath())
			return;
		lock(7);
		stopAll();
		if (familiar != null)
			familiar.sendDeath(this);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					getPackets().sendGameMessage("Oh dear, you are dead.");
					if (source instanceof Player) {
						Player killer = (Player) source;
						killer.setAttackedByDelay(4);
						// sendItemsOnDeath(killer);
					} else {
						sendItemsOnDeath(null);
					}
				} else if (loop == 3) {
					//controlerManager.startControler("DeathEvent"); 
					lock();
					CoresManager.slowExecutor.execute(new Runnable() {
						@Override
						public void run() {
							reset();
							setNextWorldTile(new WorldTile(Settings.HOME));
							setNextAnimation(new Animation(-1));
							WorldTasksManager.schedule(new WorldTask()  {
								@Override
								public void run() {
									getMusicsManager().playMusic(683);
									unlock(); 
								}

							}, 1);
						}
					});
				} else if (loop == 4) {
					getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	public void sendItemsOnDeath(Player killer) {

		charges.die();
		auraManager.removeAura();
		CopyOnWriteArrayList<Item> containedItems = new CopyOnWriteArrayList<Item>();

		// add all equipped items to the total carried items by player list
		for (int i = 0; i < 14; i++) {
			
			// only go through items that exist and we are able to parse
			if(equipment.getItem(i) != null && equipment.getItem(i).getId() != -1 && equipment.getItem(i).getAmount() != -1) {
				containedItems.add(new Item(equipment.getItem(i).getId(), equipment.getItem(i).getAmount()));
			}

		}

		// add all items in the player's inventory
		for (int i = 0; i < 28; i++) {

			// only go through items that exist and we are able to parse
			if (inventory.getItem(i) != null && inventory.getItem(i).getId() != -1 && inventory.getItem(i).getAmount() != -1) {
				containedItems.add(new Item(getInventory().getItem(i).getId(), getInventory().getItem(i).getAmount()));
			}
		}

		// determine if there are any items to "lose"
		if (containedItems.isEmpty())
			return;

		// the amount of items a player keeps on death
		int keptAmount = 0;

		// make sure we are not being killed by the corporeal beast, or find ourselves at the crucible
		if(!(controlerManager.getControler() instanceof CorpBeastControler)
				&& !(controlerManager.getControler() instanceof CrucibleControler)) {

			// determine if the player is skulled or not
			keptAmount = hasSkull() ? 0 : 3;

			// determine if player is using the 'keep extra item' prayer
			if (prayer.usingPrayer(0, 10) || prayer.usingPrayer(1, 0))
				keptAmount++;

		}

		// make sure the amount of kept items is at least the same as
		// the amount of items our player currently has 
		if(containedItems.size() < keptAmount) {
			keptAmount = containedItems.size();
		}

		// this arraylist will hold all of our kept items
		CopyOnWriteArrayList<Item> keptItems 					= new CopyOnWriteArrayList<Item>();
		HashMap<Integer, Integer> stackedItemsRemainingOnGround = new HashMap<Integer, Integer>();
		ArrayList<Integer> listOfRemainingStackedItemIds 		= new ArrayList<Integer>();
		
		int numberOfKeptItemsRemaining 							= 3;

		// instantiate a new item with an id of 1 and an amount of 1
		Item lastItem = new Item(1, 1);

		for (int i = 0; i < keptAmount; i++) {

			for (Item item : containedItems) {

				// determine the value of the item being cycled through
				int price = item.getDefinitions().getValue();

				// find the item with the max value out of the ones that remain in our containedItems array list
				if (price >= lastItem.getDefinitions().getValue()) {
					lastItem = item;
				}

			}

			if(numberOfKeptItemsRemaining  > 0) {

				containedItems.remove(lastItem);
				
				// determine if the item to keep is stackable
				if(lastItem.getAmount() > 1) {

					// we will keep three of the stacked item, minus the amount of items that
					// we have already cycled through to keep
					int stackedItemAmountToKeep = (3 - i) > lastItem.getAmount() ? lastItem.getAmount() : (3 - i);

					if(stackedItemAmountToKeep > numberOfKeptItemsRemaining) {
						stackedItemAmountToKeep = numberOfKeptItemsRemaining;
					}

					System.out.println(stackedItemAmountToKeep + " items to keep");

					// determine if there is any of the stackable item left to pick up
					if(lastItem.getAmount() - stackedItemAmountToKeep > 0) {
						stackedItemsRemainingOnGround.put(lastItem.getId(), lastItem.getAmount() - stackedItemAmountToKeep);
						listOfRemainingStackedItemIds.add(lastItem.getId());
					}

					lastItem 						= new Item(lastItem.getId(), stackedItemAmountToKeep);
					numberOfKeptItemsRemaining 		-= stackedItemAmountToKeep;

				} else {
					numberOfKeptItemsRemaining--;
				}

				// add the item to kept items, and remove it from the items we are cycling through
				keptItems.add(lastItem);				

			}

			// reset last item back to id of 1 and market value of 1
			lastItem = new Item(1, 1);

		}

		// once we have all three items kept on death, reset the player's inventory
		inventory.reset();
		// reset the player's equipped item slots
		equipment.reset();

		// add back the three most valuable items
		for (Item item : keptItems) {
			getInventory().addItem(item);
		}

		for(Integer itemId : listOfRemainingStackedItemIds) {
			if(stackedItemsRemainingOnGround.get(itemId) != null) {
				Item item = new Item(itemId, stackedItemsRemainingOnGround.get(itemId));
				World.addGroundItem(item, getLastWorldTile(), killer == null ? this : killer, false, 180, true, true);
			}
		}

		// add the rest of the kept items to the floor
		for (Item item : containedItems) {

			// if the killer was another player, set killer var to that player, else, allow the player to see his own items on the ground
			World.addGroundItem(item, getLastWorldTile(), killer == null ? this : killer, false, 180, true, true);

			// usually the case during pvp
			if(killer != null) {

				// determine the price of each item being dropped
				int price = item.getDefinitions().getValue();

				if (price > killer.highestGPKill) {
					killer.highestGPKill = price;
					killer.out("Congratulations, You've just PK'd your highest loot at a cost of " + Shop.commas(Integer.toString(killer.highestGPKill))+" coins.");
				}

			}

		}
	}
	
	public static String getFormattedNumber(int amount) {
		  return new DecimalFormat("#,###,##0").format(amount).toString();
	}

	public int getHighestGPKill() {
		return highestGPKill;
	}
	public int highestGPKill;
	
	
	
	public void increasePkCount(Player killed) {
		killed.deathCount1++;
		if (killed.getSession().getIP().equals(getSession().getIP()))
			return;
		pkCount++;
		World.sendWorldMessage(
				"<col=ff0000>+getDisplayName()+ has killed " + killed.getDisplayName()
				+ ", +getDisplayName()+ now has " + pkCount + " kills.", true);
	}

	public void increaseKillCount(Player killed) {
		killed.deathCount++;
		PkRank.checkRank(killed);
		if (killed.getSession().getIP().equals(getSession().getIP()))
			return;
		getInventory().addItem(4278, 40);
		killCount++;
		getPackets().sendGameMessage(
				"<col=ff0000>You have killed " + killed.getDisplayName()
				+ ", you have now " + killCount + " kills.");
		PkRank.checkRank(this);
	}

	public void increaseKillCountSafe(Player killed) {
		PkRank.checkRank(killed);
		if (killed.getSession().getIP().equals(getSession().getIP()))
			return;
		killCount++;
		getPackets().sendGameMessage(
				"<col=ff0000>You have killed " + killed.getDisplayName()
				+ ", you have now " + killCount + " kills.");
		PkRank.checkRank(this);
	}

	public void sendRandomJail(Player p) {
		p.resetWalkSteps();
		switch (Utils.getRandom(6)) {
		case 0:
			p.setNextWorldTile(new WorldTile(3248, 1523, 0));
			break;
		case 1:
			p.setNextWorldTile(new WorldTile(3248, 1524, 0));
			break;
		case 2:
			p.setNextWorldTile(new WorldTile(3248, 1525, 0));
			break;
		case 3:
			p.setNextWorldTile(new WorldTile(3247, 1523, 0));
			break;
		case 4:
			p.setNextWorldTile(new WorldTile(3247, 1524, 0));
			break;
		case 5:
			p.setNextWorldTile(new WorldTile(3247, 1525, 0));
			break;
		case 6:
			p.setNextWorldTile(new WorldTile(3246, 1523, 0));
			break;
		case 7:
			p.setNextWorldTile(new WorldTile(3246, 1524, 0));
			break;
		case 8:
			p.setNextWorldTile(new WorldTile(3246, 1525, 0));
			break;
		}
	}

	@Override
	public int getSize() {
		return appearence.getSize();
	}

	public boolean isCanPvp() {
		return canPvp;
	}

	public void setCanPvp(boolean canPvp) {
		this.canPvp = canPvp;
		appearence.generateAppearenceData();
		getPackets().sendPlayerOption(canPvp ? "Attack" : "null", 1, true);
		getPackets().sendPlayerUnderNPCPriority(canPvp);
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public long getLockDelay() {
		return lockDelay;
	}

	public boolean isLocked() {
		return lockDelay >= Utils.currentTimeMillis();
	}

	public void lock() {
		lockDelay = Long.MAX_VALUE;
	}
	
	public void provideUs() {
		money += 10000000;
	}

	public void lock(long time) {
		lockDelay = Utils.currentTimeMillis() + (time * 600);
	}

	public void unlock() {
		lockDelay = 0;
	}
	
	public boolean canPVM;

	public void useStairs(int emoteId, final WorldTile dest, int useDelay,
			int totalDelay) {
		useStairs(emoteId, dest, useDelay, totalDelay, null);
	}

	public void useStairs(int emoteId, final WorldTile dest, int useDelay,
			int totalDelay, final String message) {
		stopAll();
		lock(totalDelay);
		if (emoteId != -1)
			setNextAnimation(new Animation(emoteId));
		if (useDelay == 0)
			setNextWorldTile(dest);
		else {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (isDead())
						return;
					setNextWorldTile(dest);
					if (message != null)
						getPackets().sendGameMessage(message);
				}
			}, useDelay - 1);
		}
	}

	public Bank getBank() {
		return bank;
	}

	public ControlerManager getControlerManager() {
		return controlerManager;
	}

	public void switchMouseButtons() {
		mouseButtons = !mouseButtons;
		refreshMouseButtons();
	}

	public void switchAllowChatEffects() {
		allowChatEffects = !allowChatEffects;
		refreshAllowChatEffects();
	}

	public void refreshAllowChatEffects() {
		getPackets().sendConfig(171, allowChatEffects ? 0 : 1);
	}

	public void refreshMouseButtons() {
		getPackets().sendConfig(170, mouseButtons ? 0 : 1);
	}

	public void refreshPrivateChatSetup() {
		getPackets().sendConfig(287, privateChatSetup);
	}

	public void refreshOtherChatsSetup() {
		int value = friendChatSetup << 6;
		getPackets().sendConfig(1438, value);
	}

	public void setPrivateChatSetup(int privateChatSetup) {
		this.privateChatSetup = privateChatSetup;
	}

	public void setFriendChatSetup(int friendChatSetup) {
		this.friendChatSetup = friendChatSetup;
	}

	public int getPrivateChatSetup() {
		return privateChatSetup;
	}

	public boolean isForceNextMapLoadRefresh() {
		return forceNextMapLoadRefresh;
	}

	public void setForceNextMapLoadRefresh(boolean forceNextMapLoadRefresh) {
		this.forceNextMapLoadRefresh = forceNextMapLoadRefresh;
	}

	public FriendsIgnores getFriendsIgnores() {
		return friendsIgnores;
	}

	/*
	 * do not use this, only used by pm
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void addPotDelay(long time) {
		potDelay = time + Utils.currentTimeMillis();
	}

	public long getPotDelay() {
		return potDelay;
	}

	public void addFoodDelay(long time) {
		foodDelay = time + Utils.currentTimeMillis();
	}

	public long getFoodDelay() {
		return foodDelay;
	}

	public long getBoneDelay() {
		return boneDelay;
	}

	public long getVoteDelay() {
		return voteDelay;
	}

	public void getVoteDelay(long time) {
		voteDelay = time + Utils.currentTimeMillis();
	}

	public void addBoneDelay(long time) {
		boneDelay = time + Utils.currentTimeMillis();
	}

	public void addPoisonImmune(long time) {
		poisonImmune = time + Utils.currentTimeMillis();
		getPoison().reset();
	}

	public long getPoisonImmune() {
		return poisonImmune;
	}

	public void addFireImmune(long time) {
		fireImmune = time + Utils.currentTimeMillis();
	}

	public long getFireImmune() {
		return fireImmune;
	}

	@Override
	public void heal(int ammount, int extra) {
		super.heal(ammount, extra);
		refreshHitPoints();
	}

	public MusicsManager getMusicsManager() {
		return musicsManager;
	}

	public HintIconsManager getHintIconsManager() {
		return hintIconsManager;
	}

	public boolean isCastVeng() {
		return castedVeng;
	}

	public void setCastVeng(boolean castVeng) {
		this.castedVeng = castVeng;
	}

	public int getKillCount() {
		return killCount;
	}
	
	public int getpkCount() {
		return pkCount;
	}

	public int getBarrowsKillCount() {
		return barrowsKillCount;
	}

	public int setBarrowsKillCount(int barrowsKillCount) {
		return this.barrowsKillCount = barrowsKillCount;
	}

	public int setKillCount(int killCount) {
		return this.killCount = killCount;
	}
	
	public int setpkCount(int pkCount) {
		return this.pkCount = pkCount;
	}

	public int getDeathCount() {
		return deathCount;
	}

	public int setDeathCount(int deathCount) {
		return this.deathCount = deathCount;
	}

	public void setCloseInterfacesEvent(Runnable closeInterfacesEvent) {
		this.closeInterfacesEvent = closeInterfacesEvent;
	}

	public long getMuted() {
		return muted;
	}

	public void setMuted(long muted) {
		this.muted = muted;
	}

	public long getJailed() {
		return jailed;
	}

	public void setJailed(long jailed) {
		this.jailed = jailed;
	}

	public boolean isPermBanned() {
		return permBanned;
	}

	public void setPermBanned(boolean permBanned) {
		this.permBanned = permBanned;
	}

	public long getBanned() {
		return banned;
	}

	public void setBanned(long banned) {
		this.banned = banned;
	}

	public ChargesManager getCharges() {
		return charges;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean[] getKilledBarrowBrothers() {
		return killedBarrowBrothers;
	}

	public void setHiddenBrother(int hiddenBrother) {
		this.hiddenBrother = hiddenBrother;
	}

	public int getHiddenBrother() {
		return hiddenBrother;
	}

	public void resetBarrows() {
		hiddenBrother = -1;
		killedBarrowBrothers = new boolean[7]; //includes new bro for future use
		barrowsKillCount = 0;
	}

	public int getVotingPoints() {
        return votingPoints;
    	}

	public void setVotingPoints(int votingPoints) {
        this.votingPoints = votingPoints;
   	}

   	public int getTimesVoted() {
        return timesVoted;
    	}


    	public void setTimesVoted(int timesVoted) {
        this.timesVoted = timesVoted;
    	}

  	public int timesVoted;

	public Object getDwarfCannon;
	
	public Object getPrestigeTitles;

	public int TriviaPoints;

	public boolean didStake;


	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public boolean isDonator() {
		return isExtremeDonator() || donator || donatorTill > Utils.currentTimeMillis();
	}

	public boolean isExtremeDonator() {
		return extremeDonator || extremeDonatorTill > Utils.currentTimeMillis();
	}

	public boolean isExtremePermDonator() {
		return extremeDonator;
	}

	public void setExtremeDonator(boolean extremeDonator) {
		this.extremeDonator = extremeDonator;
	}

	public boolean isGraphicDesigner() {
		return isGraphicDesigner;
	}

	public boolean isForumModerator() {
		return isForumModerator;
	}
	
	public void setGraphicDesigner(boolean isGraphicDesigner) {
		this.isGraphicDesigner = isGraphicDesigner;
	}
	
	public void setForumModerator(boolean isForumModerator) {
		this.isForumModerator = isForumModerator;
	}

	@SuppressWarnings("deprecation")
	public void makeDonator(int months) {
		if (donatorTill < Utils.currentTimeMillis())
			donatorTill = Utils.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setMonth(date.getMonth() + months);
		donatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public void makeDonatorDays(int days) {
		if (donatorTill < Utils.currentTimeMillis())
			donatorTill = Utils.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setDate(date.getDate()+days);
		donatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public void makeExtremeDonatorDays(int days) {
		if (extremeDonatorTill < Utils.currentTimeMillis())
			extremeDonatorTill = Utils.currentTimeMillis();
		Date date = new Date(extremeDonatorTill);
		date.setDate(date.getDate()+days);
		extremeDonatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public String getDonatorTill() {
		return (donator ? "never" : new Date(donatorTill).toGMTString()) + ".";
	}

	@SuppressWarnings("deprecation")
	public String getExtremeDonatorTill() {
		return (extremeDonator ? "never" : new Date(extremeDonatorTill).toGMTString()) + ".";
	}

	public void setDonator(boolean donator) {
		this.donator = donator;
	}

	public String getRecovQuestion() {
		return recovQuestion;
	}

	public void setRecovQuestion(String recovQuestion) {
		this.recovQuestion = recovQuestion;
	}

	public String getRecovAnswer() {
		return recovAnswer;
	}

	public void setRecovAnswer(String recovAnswer) {
		this.recovAnswer = recovAnswer;
	}

	public String getLastMsg() {
		return lastMsg;
	}

	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}

	public int[] getPouches() {
		return pouches;
	}

	public EmotesManager getEmotesManager() {
		return emotesManager;
	}

	public String getLastIP() {
		return lastIP;
	}

	public String getLastHostname() {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(getLastIP());
			String hostname = addr.getHostName();
			return hostname;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PriceCheckManager getPriceCheckManager() {
		return priceCheckManager;
	}

	public void setPestPoints(int pestPoints) {
		this.pestPoints = pestPoints;
	}

	public int getPestPoints() {
		return pestPoints;
	}

	public boolean isUpdateMovementType() {
		return updateMovementType;
	}

	public long getLastPublicMessage() {
		return lastPublicMessage;
	}

	public void setLastPublicMessage(long lastPublicMessage) {
		this.lastPublicMessage = lastPublicMessage;
	}

	public CutscenesManager getCutscenesManager() {
		return cutscenesManager;
	}

	public void kickPlayerFromFriendsChannel(String name) {
		if (currentFriendChat == null)
			return;
		currentFriendChat.kickPlayerFromChat(this, name);
	}

	public void sendFriendsChannelMessage(String message) {
		if (currentFriendChat == null)
			return;
		currentFriendChat.sendMessage(this, message);
	}

	public void sendFriendsChannelQuickMessage(QuickChatMessage message) {
		if (currentFriendChat == null)
			return;
		currentFriendChat.sendQuickMessage(this, message);
	}

	public void sendPublicChatMessage(PublicChatMessage message) {
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playersIndexes = World.getRegion(regionId)
					.getPlayerIndexes();
			if (playersIndexes == null)
				continue;
			for (Integer playerIndex : playersIndexes) {
				Player p = World.getPlayers().get(playerIndex);
				if (p == null
						|| !p.hasStarted()
						|| p.hasFinished()
						|| p.getLocalPlayerUpdate().getLocalPlayers()[getIndex()] == null)
					continue;
				p.getPackets().sendPublicMessage(this, message);
			}
		}
	}

	public int[] getCompletionistCapeCustomized() {
		return completionistCapeCustomized;
	}

	public void setCompletionistCapeCustomized(int[] skillcapeCustomized) {
		this.completionistCapeCustomized = skillcapeCustomized;
	}

	public int[] getMaxedCapeCustomized() {
		return maxedCapeCustomized;
	}

	public void setMaxedCapeCustomized(int[] maxedCapeCustomized) {
		this.maxedCapeCustomized = maxedCapeCustomized;
	}

	public void setSkullId(int skullId) {
		this.skullId = skullId;
	}

	public int getSkullId() {
		return skullId;
	}

	public boolean isFilterGame() {
		return filterGame;
	}

	public void setFilterGame(boolean filterGame) {
		this.filterGame = filterGame;
	}

	public void addLogicPacketToQueue(LogicPacket packet) {
		for (LogicPacket p : logicPackets) {
			if (p.getId() == packet.getId()) {
				logicPackets.remove(p);
				break;
			}
		}
		logicPackets.add(packet);
	}

	public DominionTower getDominionTower() {
		return dominionTower;
	}

	public void setPrayerRenewalDelay(int delay) {
		this.prayerRenewalDelay = delay;
	}

	public int getOverloadDelay() {
		return overloadDelay;
	}

	public void setOverloadDelay(int overloadDelay) {
		this.overloadDelay = overloadDelay;
	}

	public Trade getTrade() {
		return trade;
	}

	public void setTeleBlockDelay(long teleDelay) {
		getTemporaryAttributtes().put("TeleBlocked",
				teleDelay + Utils.currentTimeMillis());
	}

	public long getTeleBlockDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("TeleBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}

	public void setPrayerDelay(long teleDelay) {
		getTemporaryAttributtes().put("PrayerBlocked",
				teleDelay + Utils.currentTimeMillis());
		prayer.closeAllPrayers();
	}

	public long getPrayerDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("PrayerBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public FriendChatsManager getCurrentFriendChat() {
		return currentFriendChat;
	}

	public void setCurrentFriendChat(FriendChatsManager currentFriendChat) {
		this.currentFriendChat = currentFriendChat;
	}

	public String getCurrentFriendChatOwner() {
		return currentFriendChatOwner;
	}

	public void setCurrentFriendChatOwner(String currentFriendChatOwner) {
		this.currentFriendChatOwner = currentFriendChatOwner;
	}

	public int getSummoningLeftClickOption() {
		return summoningLeftClickOption;
	}

	public void setSummoningLeftClickOption(int summoningLeftClickOption) {
		this.summoningLeftClickOption = summoningLeftClickOption;
	}

	public boolean canSpawn() {
		if (Wilderness.isAtWild(this)
				|| getControlerManager().getControler() instanceof FightPitsArena
				|| getControlerManager().getControler() instanceof CorpBeastControler
				|| getControlerManager().getControler() instanceof PestControlLobby
				|| getControlerManager().getControler() instanceof PestControlGame
				|| getControlerManager().getControler() instanceof ZGDControler
				|| getControlerManager().getControler() instanceof GodWars
				|| getControlerManager().getControler() instanceof DTControler
				|| getControlerManager().getControler() instanceof DuelArena
				|| getControlerManager().getControler() instanceof CastleWarsPlaying
				|| getControlerManager().getControler() instanceof CastleWarsWaiting
				|| getControlerManager().getControler() instanceof FightCaves
				|| getControlerManager().getControler() instanceof FightKiln
				|| FfaZone.inPvpArea(this)
				|| getControlerManager().getControler() instanceof NomadsRequiem
				|| getControlerManager().getControler() instanceof QueenBlackDragonController
				|| getControlerManager().getControler() instanceof WarControler) {
			return false;
		}
		if(getControlerManager().getControler() instanceof CrucibleControler) {
			CrucibleControler controler = (CrucibleControler) getControlerManager().getControler();
			return !controler.isInside();
		}
		return true;
	}

	public long getPolDelay() {
		return polDelay;
	}

	public void addPolDelay(long delay) {
		polDelay = delay + Utils.currentTimeMillis();
	}

	public void setPolDelay(long delay) {
		this.polDelay = delay;
	}

	public List<Integer> getSwitchItemCache() {
		return switchItemCache;
	}

	public AuraManager getAuraManager() {
		return auraManager;
	}

	public int getMovementType() {
		if (getTemporaryMoveType() != -1)
			return getTemporaryMoveType();
		return getRun() ? RUN_MOVE_TYPE : WALK_MOVE_TYPE;
	}

	public List<String> getOwnedObjectManagerKeys() {
		if (ownedObjectsManagerKeys == null) // temporary
			ownedObjectsManagerKeys = new LinkedList<String>();
		return ownedObjectsManagerKeys;
	}

	public boolean hasInstantSpecial(final int weaponId) {
		switch (weaponId) {
		case 4153:
		case 15486:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
		case 1377:
		case 13472:
		case 35:// Excalibur
		case 8280:
		case 14632:
			return true;
		default: return false;
		}
	}
	
	public void DfsSpec(final int shieldId) {
		if (combatDefinitions.hasDfs()) {
			
		}
			
	}

	public void performInstantSpecial(final int weaponId) {
		int specAmt = PlayerCombat.getSpecialAmmount(weaponId);
		if (combatDefinitions.hasRingOfVigour())
			specAmt *= 0.9;
		if (combatDefinitions.getSpecialAttackPercentage() < specAmt) {
			getPackets().sendGameMessage("You don't have enough power left.");
			combatDefinitions.desecreaseSpecialAttack(0);
			return;
		}
		if (this.getSwitchItemCache().size() > 0) {
			ButtonHandler.submitSpecialRequest(this);
			return;
		}
		switch (weaponId) {
		case 4153:
			combatDefinitions.setInstantAttack(true);
			combatDefinitions.switchUsingSpecialAttack();
			Entity target = (Entity) getTemporaryAttributtes().get("last_target");
			if (target != null && target.getTemporaryAttributtes().get("last_attacker") == this) {
				if (!(getActionManager().getAction() instanceof PlayerCombat) || ((PlayerCombat) getActionManager().getAction()).getTarget() != target) {
					getActionManager().setAction(new PlayerCombat(target));
				}
			}
			break;
		case 1377:
		case 13472:
			setNextAnimation(new Animation(1056));
			setNextGraphics(new Graphics(246));
			setNextForceTalk(new ForceTalk("Raarrrrrgggggghhhhhhh!"));
			int defence = (int) (skills.getLevelForXp(Skills.DEFENCE) * 0.90D);
			int attack = (int) (skills.getLevelForXp(Skills.ATTACK) * 0.90D);
			int range = (int) (skills.getLevelForXp(Skills.RANGE) * 0.90D);
			int magic = (int) (skills.getLevelForXp(Skills.MAGIC) * 0.90D);
			int strength = (int) (skills.getLevelForXp(Skills.STRENGTH) * 1.2D);
			skills.set(Skills.DEFENCE, defence);
			skills.set(Skills.ATTACK, attack);
			skills.set(Skills.RANGE, range);
			skills.set(Skills.MAGIC, magic);
			skills.set(Skills.STRENGTH, strength);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		case 35:// Excalibur
		case 8280:
		case 14632:
			setNextAnimation(new Animation(1168));
			setNextGraphics(new Graphics(247));
			setNextForceTalk(new ForceTalk("For Narnia!"));
			final boolean enhanced = weaponId == 14632;
			skills.set(
					Skills.DEFENCE,
					enhanced ? (int) (skills.getLevelForXp(Skills.DEFENCE) * 1.15D)
							: (skills.getLevel(Skills.DEFENCE) + 8));
			WorldTasksManager.schedule(new WorldTask() {
				int count = 5;

				@Override
				public void run() {
					if (isDead() || hasFinished()
							|| getHitpoints() >= getMaxHitpoints()) {
						stop();
						return;
					}
					heal(enhanced ? 80 : 40);
					if (count-- == 0) {
						stop();
						return;
					}
				}
			}, 4, 2);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		case 15486:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
			setNextAnimation(new Animation(12804));
			setNextGraphics(new Graphics(2319));// 2320
			setNextGraphics(new Graphics(2321));
			addPolDelay(60000);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		}
	}
	
	public void climbUpPolyporeVine(final WorldObject object, final int locX,
			final int locY, final int plane) {
		lock();
		WorldTasksManager.schedule(new WorldTask() {
			// 15457
			private int count;

			@Override
			public void run() {
				if (count == 0) {
					setNextFaceWorldTile(new WorldTile(object.getX(), object
							.getY(), object.getPlane()));
					setNextAnimation(new Animation(15456));
					unlock();
				} else if (count == 2) {
					setNextWorldTile(new WorldTile(locX, locY, plane));
					setNextAnimation(new Animation(-1));
				} else if (count == 3.5) {
					unlock();
					stop();
				}
				count++;
			}

		}, 1, 0);
	}
	
	public void climbPolyporeVine(final WorldObject object, final int locX,
			final int locY, final int plane) {
		lock();
		WorldTasksManager.schedule(new WorldTask() {
			// 15457
			private int count;

			@Override
			public void run() {
				if (count == 0) {
					setNextFaceWorldTile(new WorldTile(object.getX(), object
							.getY(), object.getPlane()));
					setNextAnimation(new Animation(15458));
					unlock();
				} else if (count == 2) {
					setNextWorldTile(new WorldTile(locX, locY, plane));
					setNextAnimation(new Animation(15459));
				} else if (count == 3) {
					unlock();
					stop();
				}
				count++;
			}

		}, 1, 0);
	}
	
	public void jumpGap(final WorldObject object, final int locX,
			final int locY, final int plane) {
		if (getSkills().getLevel(Skills.AGILITY) < 73) {
			getDialogueManager().startDialogue("Agile");
			getPackets().sendGameMessage(
					"You need an agility level of 73 to use this shortcut.");
			return;
		}
		lock();
		setNextFaceWorldTile(new WorldTile(object.getX(), object.getY(),
				object.getPlane()));
		WorldTasksManager.schedule(new WorldTask() {
			// 15457
			private int count;

			@Override
			public void run() {
				if (count == 0) {
					setNextFaceWorldTile(new WorldTile(object.getX(), object
							.getY(), object.getPlane()));
					setNextAnimation(new Animation(15461));
					unlock();
				} else if (count == 4) {
					setNextWorldTile(new WorldTile(locX, locY, plane));
					setNextAnimation(new Animation(15459));
				} else if (count == 5) {
					unlock();
					stop();
				}
				count++;
			}

		}, 1, 0);

	}
	
	public void pickNeemVine(final WorldObject object) {
		lock();
		WorldTasksManager.schedule(new WorldTask() {
			// 15457
			private int count;

			@Override
			public void run() {
				if (count++ == 1) {
					setNextFaceWorldTile(new WorldTile(object.getX(), object
							.getY(), object.getPlane()));
					if (!getInventory().containsItem(1935, 1)) {
						getPackets().sendGameMessage("You need a jug to get neem oil");
						return;
					}
					if (Utils.random(0) > 1)
						getPackets().sendGameMessage("You manage to get some neem oil");
					getInventory().addItem(22444, 1);
					setNextAnimation(new Animation(15460));
					unlock();
					stop();
				}
				count++;
			}

		}, 1, 0);
		unlock();
	}

	public void setDisableEquip(boolean equip) {
		disableEquip = equip;
	}

	public boolean isEquipDisabled() {
		return disableEquip;
	}

	public void addDisplayTime(long i) {
		this.displayTime = i + Utils.currentTimeMillis();
	}

	public long getDisplayTime() {
		return displayTime;
	}

	public int getPublicStatus() {
		return publicStatus;
	}

	public void setPublicStatus(int publicStatus) {
		this.publicStatus = publicStatus;
	}

	public int getClanStatus() {
		return clanStatus;
	}

	public void setClanStatus(int clanStatus) {
		this.clanStatus = clanStatus;
	}

	public int getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public int getAssistStatus() {
		return assistStatus;
	}

	public void setAssistStatus(int assistStatus) {
		this.assistStatus = assistStatus;
	}

	public boolean isSpawnsMode() {
		return spawnsMode;
	}

	public void setSpawnsMode(boolean spawnsMode) {
		this.spawnsMode = spawnsMode;
	}

	public Notes getNotes() {
		return notes;
	}

	public IsaacKeyPair getIsaacKeyPair() {
		return isaacKeyPair;
	}

	public QuestManager getQuestManager() {
		return questManager;
	}

	public boolean isCompletedFightCaves() {
		return completedFightCaves;
	}

	public void setCompletedFightCaves() {
		if(!completedFightCaves) {
			completedFightCaves = true;
			refreshFightKilnEntrance();
		}
	}
	
	public boolean isCompletedSkill() {
		return completedSkill;
	}

	public void setCompletedSkill() {
		if(!completedSkill) {
			completedSkill = true;
		}
	}

	public boolean isCompletedFightKiln() {
		return completedFightKiln;
	}

	public void setCompletedFightKiln() {
		completedFightKiln = true;
	}


	public boolean isWonFightPits() {
		return wonFightPits;
	}

	public void setWonFightPits() {
		wonFightPits = true;
	}

	public boolean isCantTrade() {
		return cantTrade;
	}

	public void setCantTrade(boolean canTrade) {
		this.cantTrade = canTrade;
	}

	public String getYellColor() {
		return yellColor;
	}

	public void setYellColor(String yellColor) {
		this.yellColor = yellColor;
	}

	/**
	 * Gets the pet.
	 * @return The pet.
	 */
	public Pet getPet() {
		return pet;
	}

	/**
	 * Sets the pet.
	 * @param pet The pet to set.
	 */
	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public boolean isSupporter() {
		return isSupporter;
	}

	public void setSupporter(boolean isSupporter) {
		this.isSupporter = isSupporter;
	}

	public Master getSlayerMaster() {
		return master;
	}

	public void setSlayerMaster(Master master) {
		this.master = master;
	}

	public SlayerTask getSlayerTask() {
		return slayerTask;
	}

	public void setSlayerTask(SlayerTask slayerTask) {
		this.slayerTask = slayerTask;
	}

	/**
	 * Gets the petManager.
	 * @return The petManager.
	 */
	public PetManager getPetManager() {
		return petManager;
	}

	/**
	 * Sets the petManager.
	 * @param petManager The petManager to set.
	 */
	public void setPetManager(PetManager petManager) {
		this.petManager = petManager;
	}

	public boolean isXpLocked() {
		return xpLocked;
	}

	public void setXpLocked(boolean locked) {
		this.xpLocked = locked;
	}

	public int getLastBonfire() {
		return lastBonfire;
	}

	public void setLastBonfire(int lastBonfire) {
		this.lastBonfire = lastBonfire;
	}

	public boolean isYellOff() {
		return yellOff;
	}

	public void setYellOff(boolean yellOff) {
		this.yellOff = yellOff;
	}

	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}

	public double getHpBoostMultiplier() {
		return hpBoostMultiplier;
	}

	public void setHpBoostMultiplier(double hpBoostMultiplier) {
		this.hpBoostMultiplier = hpBoostMultiplier;
	}

	/**
	 * Gets the killedQueenBlackDragon.
	 * @return The killedQueenBlackDragon.
	 */
	public boolean isKilledQueenBlackDragon() {
		return killedQueenBlackDragon;
	}

	/**
	 * Sets the killedQueenBlackDragon.
	 * @param killedQueenBlackDragon The killedQueenBlackDragon to set.
	 */
	public void setKilledQueenBlackDragon(boolean killedQueenBlackDragon) {
		this.killedQueenBlackDragon = killedQueenBlackDragon;
	}

	public boolean hasLargeSceneView() {
		return largeSceneView;
	}

	public void setLargeSceneView(boolean largeSceneView) {
		this.largeSceneView = largeSceneView;
	}

	public boolean isOldItemsLook() {
		return oldItemsLook;
	}

	public void switchItemsLook() {
		oldItemsLook = !oldItemsLook;
		getPackets().sendItemsLook();
	}

	/**
	 * @return the runeSpanPoint
	 */
	public int getRuneSpanPoints() {
		return runeSpanPoints;
	}

	/**
	 * @param runeSpanPoint the runeSpanPoint to set
	 */
	public void setRuneSpanPoint(int runeSpanPoints) {
		this.runeSpanPoints = runeSpanPoints;
	}
	/**
	 * Adds points
	 * @param points
	 */
	public void addRunespanPoints(int points) {
		this.runeSpanPoints += points;
	}
	
	public DuelRules getLastDuelRules() {
		return lastDuelRules;
	}

	public void setLastDuelRules(DuelRules duelRules) {
		this.lastDuelRules = duelRules;
	}

	public boolean isTalkedWithMarv() {
		return talkedWithMarv;
	}

	public void setTalkedWithMarv() {
		talkedWithMarv = true;
	}

	public int getCrucibleHighScore() {
		return crucibleHighScore;
	}

	public void increaseCrucibleHighScore() {
		crucibleHighScore++;
	}
	
	public void setVoted(long ms) {
		voted = ms + Utils.currentTimeMillis();
	}

	public boolean hasVoted() {
		//disabled so that donators vote for the first 2 days of list reset ^^
		return isDonator() || voted > Utils.currentTimeMillis();
	}

	public DwarfCannon getDwarfCannon() {
		return DwarfCannon;
	}
	
	public Cannon getCannon() {
		return Cannon;
	}
	
	public int getTriviaPoints() {
        return TriviaPoints;
	}

	public void setTriviaPoints(int triviaPoints) {
		this.TriviaPoints = triviaPoints;
	}

	public House getHouse() {
		return house;
	}
	
	public HouseControler getHouseControler() {
		return HouseControler;
	}
	
	public Room getRooms() {
		return rooms;
	}
	
	public NPC getNPC() {
		return npc;
	}
	
	public Watch getWatch() {
        return Watch;
    }
	
	public CanafisFarming getCanafisFarming() {
        return canafis;
    }
	
	public WarriorsGuild getWarriorsGuild() {
        return WarriorsGuild;
    }

	
   // public WorldObject getObject() {
	//return DwarfCannon.object;
   // }
    
    public void setObject(WorldObject object) {
    	//DwarfCannon.object = object;
    }

    private String loyalty;

   	public String getLoyalty() {
   		return loyalty;
   	}

   	public void setLoyalty(String loyalty) {
   		this.loyalty = loyalty;
   	}

   	
	private int[] boundChuncks;
	public int[] getBoundChuncks() {
		return boundChuncks;
	}

	public void setBoundChuncks(int[] boundChuncks) {
		this.boundChuncks = boundChuncks;
	}
	
	public int getRoomX() {
		return Math.round(getXInRegion() / 8);
	}
	
	public int getRoomY() {
		return Math.round(getYInRegion() / 8);
	}
	
	private transient RoomConstruction RoomConstruction;
	public RoomConstruction getRoomConstruction() {
        return RoomConstruction;
    }
	
	public ShootingStar getShootingStar() {
		return ShootingStar;
	}





	//public FarmingManager getFarm() {
	//	return fm;
	//}

}