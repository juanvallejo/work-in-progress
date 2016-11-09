package com.rs.game.player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rs.game.Animation;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class FarmingManager {

	private static final int ALLOTMENT = 0, HERBS = 6, RAKE = 5341, WEEDS = 6055;

	private List<FarmingSpot> spots;
	private transient Player player;

	public FarmingManager() {
		spots = new ArrayList<FarmingSpot>();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void init() {
		for (FarmingSpot spot : spots)
			spot.refresh();
	}


	private static enum ProductInfo {
		Potato(5318, 1, 1942, 0, ALLOTMENT), Onion(5319, 5, 1957, 1, ALLOTMENT), Cabbage(
				5324, 7, 1965, 2, ALLOTMENT), Tomato(5322, 12, 1982, 3,
				ALLOTMENT), Sweetcorn(5320, 20, 5986, 4, ALLOTMENT), Strawberry(
				5323, 31, 5504, 5, ALLOTMENT), Watermelon(5321, 47, 5982, 6,
				ALLOTMENT);

		private int seedId;
		private int level;
		private int productId;
		private int configIndex;
		private int type;

		private ProductInfo(int seedId, int level, int productId,
				int configIndex, int type) {
			this.setSeedId(seedId);
			this.setLevel(level);
			this.setProductId(productId);
			this.configIndex = configIndex;
			this.type = type;
		}

		@SuppressWarnings("unused")
		public int getSeedId() {
			return seedId;
		}

		public void setSeedId(int seedId) {
			this.seedId = seedId;
		}

		@SuppressWarnings("unused")
		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		@SuppressWarnings("unused")
		public int getProductId() {
			return productId;
		}

		public void setProductId(int productId) {
			this.productId = productId;
		}
	}

	private static enum SpotInfo {
		Falador_Herb_patch(8550, 780, HERBS), Falador_Allotment_North(8550,
				708, ALLOTMENT), Falador_Allotment_South(8551, 709, ALLOTMENT);

		private int objectId;
		private int configFileId;
		private int type;

		private SpotInfo(int objectId, int configFileId, int type) {
			this.objectId = objectId;
			this.configFileId = configFileId;
			this.type = type;
		}
	}

	public boolean isFarming(int objectId, int optionId) {
		for (SpotInfo info : SpotInfo.values()) {
			if (info.objectId == objectId) {
				handleFarming(info, optionId);
				return true;
			}
		}
		return false;
	}

	private FarmingSpot getSpot(SpotInfo info) {
		for (FarmingSpot spot : spots)
			if (spot.spotInfo.equals(info))
				return spot;
		return null;
	}

	public void handleFarming(SpotInfo info, int optionId) {
		FarmingSpot spot = getSpot(info);
		if (spot == null) {
			switch (optionId) {
			case 0: // rake
			//	useRake(info); // creates spot
				break;
			case 1: // inspect
				sendNeedsWeeding();
				break;
			case 2: // guide
				sendGuide();
				break;
			}
		} else {
			switch (info.type) {
			case ALLOTMENT:

				break;
			}
		}
	}

	/*@SuppressWarnings("unused")
	private void useRake(FarmingSpot spot) {
		useRake(spot, null, false);
	}

	private void useRake(SpotInfo info) {
		useRake(null, info, true);
	}

	private void useRake(FarmingSpot spot, SpotInfo info, boolean create) {
		if (!player.getInventory().containsItem(RAKE, 1)) {
			player.getPackets().sendGameMessage(
					"You'll need a rake to get rid of the weeds.");
			return;
		}
	}*/
	
	public static void rakeLog(Player player, int configId) {
        try {
        	final String FILE_PATH = "data/farm/";
            BufferedWriter bf = new BufferedWriter(new FileWriter(FILE_PATH +player.getUsername()+".txt", true));
            bf.write("player.getPackets().sendConfigByFile("+configId+", 3);");
            bf.newLine();
            bf.flush();
            bf.close();
        } catch (IOException ignored) {
        }
    }
	
	
	public static void useRake(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1) || player.rake == true) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 1);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 2);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
				} else if (loop == 6) {	
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 3);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
					player.out("You successfully clear all the weeds.");
					player.mustRakeA = true;
					player.unlock();
					player.farmingStatusA = 1;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	
	public static void useRakeB(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1) || player.rake == true) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 1);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 2);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
				} else if (loop == 6) {	
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 3);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
					player.out("You successfully clear all the weeds.");
					player.mustRakeB = true;
					player.unlock();
					player.farmingStatusB = 1;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	public static void useRakeH(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1) || player.rake == true) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 1);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 2);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
				} else if (loop == 6) {	
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 3);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
					player.out("You successfully clear all the weeds.");
					player.mustRakeH = true;
					player.unlock();
					player.farmingStatusH = 1;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	public static void useRakeF(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1) || player.rake == true) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 1);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 2);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
				} else if (loop == 6) {	
					player.setNextAnimation(new Animation(2273));
					player.getPackets().sendConfigByFile(configId, 3);
					player.getInventory().addItem(WEEDS, 1);
					player.getSkills().addXp(Skills.FARMING, 1000);
					player.out("You successfully clear all the weeds.");
					player.mustRakeF = true;
					player.unlock();
					player.farmingStatusF = 1;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a rake to get rid of the weeds.");
			}
	}
	
	
	public static void plantPotatoA(final Player player, final int configId) {
		if (player.getInventory().containsItem(5318, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.farmingStatusA = 2;
					player.farmStatus();
					player.setNextAnimation(new Animation(2292));
					player.getPackets().sendConfigByFile(configId, 6);
					player.getInventory().deleteItem(5318, 1);
				} else if (loop == 120) {
					player.getPackets().sendConfigByFile(configId, 7);
					player.out("<col=FF0000>[Notice] Your crops are half way done at Falador.</col>");
				} else if (loop == 240) {	
					player.getPackets().sendConfigByFile(configId, 10);
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Falador, allotment A.</col>");
					player.canHarvestA = true;
					player.farmingStatusA = 3;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some potato seeds.");
			}
	}
	
	public static void plantPotatoB(final Player player, final int configId) {
		if (player.getInventory().containsItem(5318, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.farmingStatusB = 2;
					player.farmStatus();
					player.setNextAnimation(new Animation(2292));
					player.getPackets().sendConfigByFile(configId, 6);
					player.getInventory().deleteItem(5318, 1);
				} else if (loop == 120) {
					player.getPackets().sendConfigByFile(configId, 7);
					player.out("<col=FF0000>[Notice] Your crops are half way done at Falador.</col>");
				} else if (loop == 240) {	
					player.getPackets().sendConfigByFile(configId, 10);
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Falador, allotment B.</col>");
					player.canHarvestB = true;
					player.farmingStatusB = 3;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some potato seeds.");
			}
	}
	
	public static void plantMelonA(final Player player, final int configId) {
		if (player.getInventory().containsItem(5321, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.farmingStatusA = 2;
					player.farmStatus();
					player.setNextAnimation(new Animation(2292));
					player.getPackets().sendConfigByFile(configId, 52);
					player.getInventory().deleteItem(5321, 1);
				} else if (loop == 120) {
					player.getPackets().sendConfigByFile(configId, 56);
					player.out("<col=FF0000>[Notice] Your crops are half way done at Falador.</col>");
				} else if (loop == 240) {	
					player.getPackets().sendConfigByFile(configId, 60);
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Falador, allotment A.</col>");
					player.canHarvestA = true;
					player.farmingStatusA = 3;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some melon seeds.");
			}
	}
	
	public static void plantMelonB(final Player player, final int configId) {
		if (player.getInventory().containsItem(5321, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.farmingStatusB = 2;
					player.farmStatus();
					player.setNextAnimation(new Animation(2292));
					player.getPackets().sendConfigByFile(configId, 52);
					player.getInventory().deleteItem(5321, 1);
				} else if (loop == 120) {
					player.getPackets().sendConfigByFile(configId, 56);
					player.out("<col=FF0000>[Notice] Your crops are half way done at Falador.</col>");
				} else if (loop == 240) {	
					player.getPackets().sendConfigByFile(configId, 60);
					player.out("<col=FF0000>[Notice] Your crops have fully grown at Falador, allotment B.</col>");
					player.canHarvestB = true;
					player.farmingStatusB = 3;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some melon seeds.");
			}
	}
	
	public static void plantGuamA(final Player player, final int configId) {
		if (player.getInventory().containsItem(5291, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.farmingStatusH = 2;
					player.farmStatus();
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 4);
					player.getInventory().deleteItem(5291, 1);
				} else if (loop == 120) {
					player.getPackets().sendConfigByFile(configId, 6);
					player.out("<col=FF0000>[Notice] Your herbs are half way done at Falador.</col>");
				} else if (loop == 240) {	
					player.getPackets().sendConfigByFile(configId, 8);
					player.out("<col=FF0000>[Notice] Your herbs are fully grown at Falador.</col>");
					player.canHarvestHerbA = true;
					player.farmingStatusH = 3;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some guam seeds.");
			}
	}
	
	
	public static void plantSnap(final Player player, final int configId) {
		if (player.getInventory().containsItem(5300, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.farmingStatusH = 2;
					player.farmStatus();
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 4);
					player.getInventory().deleteItem(5300, 1);
				} else if (loop == 120) {
					player.getPackets().sendConfigByFile(configId, 6);
					player.out("<col=FF0000>[Notice] Your herbs are half way done at Falador.</col>");
				} else if (loop == 240) {	
					player.getPackets().sendConfigByFile(configId, 8);
					player.out("<col=FF0000>[Notice] Your herbs are fully grown at Falador.</col>");
					player.canHarvestHerbA = true;
					player.farmingStatusH = 3;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some snapdragon seeds.");
			}
	}
	
	public static void plantTorstol(final Player player, final int configId) {
		if (player.getInventory().containsItem(5304, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.farmingStatusH = 2;
					player.farmStatus();
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 4);
					player.getInventory().deleteItem(5304, 1);
				} else if (loop == 120) {
					player.getPackets().sendConfigByFile(configId, 6);
				} else if (loop == 240) {	
					player.getPackets().sendConfigByFile(configId, 8);
					player.out("<col=FF0000>[Notice] Your herbs are fully grown at Falador.</col>");
					player.canHarvestHerbA = true;
					player.farmingStatusH = 3;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some Torstol seeds.");
			}
	}
	
	public static void plantGold(final Player player, final int configId) {
		if (player.getInventory().containsItem(5096, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.farmingStatusF = 2;
					player.farmStatus();
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 8);
					player.getInventory().deleteItem(5096, 1);
				} else if (loop == 120) {
					player.getPackets().sendConfigByFile(configId, 10);
					player.out("<col=FF0000>[Notice] Your Marigolds are half way done at Falador.</col>");
				} else if (loop == 240) {	
					player.getPackets().sendConfigByFile(configId, 12);
					player.out("<col=FF0000>[Notice] Your Marigolds are half way done at Falador.</col>");
					player.canHarvestFlowerA = true;
					player.farmingStatusF = 3;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some Marigold seeds.");
			}
	}
	
	public static void plantLily(final Player player, final int configId) {
		if (player.getInventory().containsItem(14589, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.farmingStatusF = 2;
					player.farmStatus();
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 37);
					player.getInventory().deleteItem(14589, 1);
				} else if (loop == 120) {
					player.getPackets().sendConfigByFile(configId, 39);
					player.out("<col=FF0000>[Notice] Your White Lily's are half way done at Falador.</col>");
				} else if (loop == 240) {	
					player.getPackets().sendConfigByFile(configId, 41);
					player.out("<col=FF0000>[Notice] Your White Lily's have fully grown at Falador.</col>");
					player.canHarvestFlowerA = true;
					player.farmingStatusF = 3;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need some White Lily seeds.");
			}
	}
	
	public static void plantYew(final Player player, final int configId) {
		if (player.getInventory().containsItem(5315, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 35);
					player.getInventory().deleteItem(5315, 1);
				} else if (loop == 90) {
					player.getPackets().sendConfigByFile(configId, 43);
					player.out("<col=FF0000>[Notice] Your Yew Tree is half way done at Falador.</col>");
				} else if (loop == 180) {	
					player.getPackets().sendConfigByFile(configId, 46);
					player.out("<col=FF0000>[Notice] Your Yew Tree is fully grown at Falador.</col>");
					player.canHarvestTreeA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a Bagged Yew Tree.");
			}
	}

	public static void plantMagic(final Player player, final int configId) {
		if (player.getInventory().containsItem(5316, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 48);
					player.getInventory().deleteItem(5316, 1);
				} else if (loop == 45) {
					player.getPackets().sendConfigByFile(configId, 54);
				} else if (loop == 90) {
					player.getPackets().sendConfigByFile(configId, 58);
					player.out("<col=FF0000>[Notice] Your Magic Tree is half way done.</col>");
				} else if (loop == 180) {	
					player.getPackets().sendConfigByFile(configId, 61);
					player.out("<col=FF0000>[Notice] Your Magic Tree has fully grown.</col>");
					player.canHarvestTreeA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a Magic Seed.");
			}
	}
	
	public static void plantSweetA(final Player player, final int configId) {
		if (player.getInventory().containsItem(5320, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.farmingStatusA = 2;
					player.farmStatus();
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 34);
					player.getInventory().deleteItem(5320, 1);
				} else if (loop == 120) {
					player.getPackets().sendConfigByFile(configId, 37);
				} else if (loop == 240) {	
					player.getPackets().sendConfigByFile(configId, 40);
					player.out("<col=FF0000>[Notice] Your Sweetcorn has fully grown at Falador, Allotment A.</col>");
					player.canHarvestA = true;
					player.farmingStatusA = 3;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a Sweetcorn Seed.");
			}
	}
	
	public static void plantSweetB(final Player player, final int configId) {
		if (player.getInventory().containsItem(5320, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.farmingStatusB = 2;
					player.farmStatus();
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 34);
					player.getInventory().deleteItem(5320, 1);
				} else if (loop == 120) {
					player.getPackets().sendConfigByFile(configId, 37);
				} else if (loop == 240) {	
					player.getPackets().sendConfigByFile(configId, 40);
					player.out("<col=FF0000>[Notice] Your Sweetcorn has fully grown at Falador, Allotment B.</col>");
					player.canHarvestB = true;
					player.farmingStatusB = 3;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a Sweetcorn Seed.");
			}
	}
	
	public static void plantStrawC(final Player player, final int configId) {
		if (player.getInventory().containsItem(5320, 1)) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(2291));
					player.getPackets().sendConfigByFile(configId, 43);
					player.getInventory().deleteItem(5320, 1);
				} else if (loop == 12) {
					player.getPackets().sendConfigByFile(configId, 46);
				} else if (loop == 24) {	
					player.getPackets().sendConfigByFile(configId, 49);
					player.out("<col=FF0000>[Notice] Your Strawberries have fully grown at Adrougne, Allotment A.</col>");
					player.canHarvestAA = true;
				}
					loop++;
					}
				}, 0, 1);
			} else {
				player.out("You'll need a Sweetcorn Seed.");
			}
	}
	
	public void sendGuide() {
		player.getTemporaryAttributtes().put("skillMenu", 21);
		player.getPackets().sendConfig(965, 21);
		player.getInterfaceManager().sendInterface(499);
	}

	public void sendNeedsWeeding() {
		player.getPackets().sendGameMessage("The patch needs weeding.");
	}

	private class FarmingSpot {

		private SpotInfo spotInfo;
		private ProductInfo productInfo;
		private int stage;
		private long cycleTime;
		@SuppressWarnings("unused")
		private boolean watered;

		@SuppressWarnings("unused")
		public FarmingSpot(SpotInfo spotInfo, ProductInfo productInfo) {
			this.spotInfo = spotInfo;
			this.productInfo = productInfo;
			cycleTime = Utils.currentTimeMillis();
			stage = 1; // stage 0 is default null
			renewCycle();
		}

		public int getConfigValue() {
			if (productInfo != null) {
				if (productInfo.type == ALLOTMENT)
					return 6 + (productInfo.configIndex * 7);
			}
			return 0;
		}

		@SuppressWarnings("unused")
		public void process() {
			if (cycleTime == 0)
				return;
			while (cycleTime < Utils.currentTimeMillis()) {
				if (productInfo != null) {
					increaseStage();
					if (reachedMaxStage()) {
						cycleTime = 0;
						break;
					}
				} else {
					desecreaseStage();
					if (stage == 0) {
						remove();
						break;
					}
				}
				renewCycle();
			}

		}

		public void increaseStage() {
			stage++;
			refresh();
		}

		public void desecreaseStage() {
			stage--;
			refresh();
		}

		public void renewCycle() {
			cycleTime += 5000; // 5sec atm
		}

		public boolean reachedMaxStage() {
			return spotInfo.type == ALLOTMENT && stage == 4; // max stage ready
		}

		private void refresh() {
			player.getPackets().sendConfigByFile(spotInfo.configFileId,
					getConfigValue() + stage);
		}

		private void remove() {
			spots.remove(this);
		}

	}
}
