package com.rs.game.player;

import java.util.HashMap;
import java.util.Map;

public class VineHerbPatch {

	
	
	public enum HerbPatch {
		GUAM(5291, 199,   9, 12,  0x04),
		MARR(5292, 201,  14, 15,  0x0b),
		TARR(5293, 203,  19, 18,  0x12),
		HARR(5294, 205,  26, 24,  0x19),
		RANA(5295, 207,  32, 30,  0x20),
		TOAD(5296, 3049, 38, 38,  0x27),
		IRIT(5297, 209,  44, 48,  0x2e),
		AVAN(5298, 211,  50, 61,  0x35),
		KWUA(5299, 213,  56, 78,  0x44),
		SNAP(5300, 3051, 62, 98,  0x4b),
		CADA(5301, 215,  67, 120, 0x52),
		LANT(5302, 2485, 73, 151, 0x59),
		DWAR(5303, 217,  79, 192, 0x60),
		TORS(5304, 219,  85, 244, 0x67);

		private static Map<Integer, HerbPatch> herbs = new HashMap<Integer, HerbPatch>();

		public static HerbPatch forId(int id) {
			return herbs.get(id);
		}

		static {
			for (HerbPatch herb : HerbPatch.values())
				herbs.put(herb.seed, herb);
		}

		private int configId;
		private int req;
		private int seed;
		private int produce;
		private int xp;

		private HerbPatch(int seed, int produce, int req, int xp, int configId) {
			this.configId = configId;
			this.req = req;
			this.xp = xp;
			this.produce = produce;
			this.seed = seed;
		}
		
		public int getProduceId() {
			return produce;
		}
		
		public int getSeed() {
			return seed;
		}
		
		public int getXp() {
			return xp;
		}

		public int getConfigId() {
			return configId;
		}

		public int getRequirement() {
			return req;
		}
	}
	
	public enum AllotmentPatch {
		POTATO(     5318, 1942,  1,  9,  0x06, 4),
		ONIONS(     5319, 1957,  5,  11, 0x0d, 4),
		CABBAGE(    5324, 1965,  7,  12, 0x14, 4),
		TOMATOES(   5322, 1982,  12, 14, 0x1b, 4),
		SWEETCORN(  5320, 5986,  20, 19, 0x22, 6), 
		STRAWERRIES(5323, 5504,  31, 29, 0x2b, 6),
		WATERMELON( 5321, 5982,  47, 55, 0x34, 8);

		private static Map<Integer, AllotmentPatch> herbs = new HashMap<Integer, AllotmentPatch>();

		public static AllotmentPatch forId(int id) {
			return herbs.get(id);
		}

		static {
			for (AllotmentPatch herb : AllotmentPatch.values())
				herbs.put(herb.seed, herb);
		}

		private int configId;
		private int req;
		private int seed;
		private int produce;
		private int xp;
		private int maxStage;

		private AllotmentPatch(int seed, int produce, int req, int xp, int configId, int maxStage) {
			this.configId = configId;
			this.req = req;
			this.xp = xp;
			this.produce = produce;
			this.seed = seed;
			this.maxStage = maxStage;
		}
		
		public int getProduceId() {
			return produce;
		}
		
		public int getMaxStage() {
			return maxStage;
		}
		
		public int getSeed() {
			return seed;
		}
		
		public int getXp() {
			return xp;
		}

		public int getConfigId() {
			return configId;
		}

		public int getRequirement() {
			return req;
		}
	}
	
	public enum FlowerPatch {
		MARIGOLD(  5096, 6010, 2,  47,  0x08),
		ROSEMARY(  5097, 6014, 11, 66,  0x0d),
		NASTURTIUM(5098, 6012, 24, 111, 0x12),
		WOAD(      5099, 1793, 25, 115, 0x17),
		LIMPWURT(  5100,  225, 26, 120, 0x1c), 
		LILY(   14589, 14583,  52, 250, 0x25);

		private static Map<Integer, FlowerPatch> herbs = new HashMap<Integer, FlowerPatch>();

		public static FlowerPatch forId(int id) {
			return herbs.get(id);
		}

		static {
			for (FlowerPatch herb : FlowerPatch.values())
				herbs.put(herb.seed, herb);
		}

		private int configId;
		private int req;
		private int seed;
		private int produce;
		private int xp;

		private FlowerPatch(int seed, int produce, int req, int xp, int configId) {
			this.configId = configId;
			this.req = req;
			this.xp = xp;
			this.produce = produce;
			this.seed = seed;
		}
		
		public int getProduceId() {
			return produce;
		}
		
		public int getSeed() {
			return seed;
		}
		
		public int getXp() {
			return xp;
		}

		public int getConfigId() {
			return configId;
		}

		public int getRequirement() {
			return req;
		}
	}
	
}
