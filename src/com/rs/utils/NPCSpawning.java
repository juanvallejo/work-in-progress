package com.rs.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;

public class NPCSpawning {

	/**
	 * Contains the custom npc spawning
	 */

	public static void spawnNPCS() {
		/**
		 * NPCS
		 */;
		// World.spawnNPC(6537, new WorldTile(2644, 9394, 0), -1, true, true);
		/*World.spawnNPC(3709, new WorldTile(3686, 3468, 0), -1, true, true);
		World.spawnNPC(519, new WorldTile(3692, 3464, 0), -1, true, true);
		World.spawnNPC(494, new WorldTile(2724, 3486, 0), -1, true, true);
		World.spawnNPC(520, new WorldTile(3692, 3463, 0), -1, true, true);
		World.spawnNPC(521, new WorldTile(3699, 3461, 0), -1, true, true);
		World.spawnNPC(522, new WorldTile(3692, 3469, 0), -1, true, true);
		World.spawnNPC(523, new WorldTile(3692, 3461, 0), -1, true, true);
		World.spawnNPC(524, new WorldTile(3475, 3499, 0), -1, true, true);
		World.spawnNPC(525, new WorldTile(3475, 3493, 0), -1, true, true);
		World.spawnNPC(526, new WorldTile(3474, 3499, 1), -1, true, true);
		World.spawnNPC(527, new WorldTile(3477, 3496, 1), -1, true, true);
		World.spawnNPC(528, new WorldTile(3474, 3491, 1), -1, true, true);
		World.spawnNPC(529, new WorldTile(3482, 3486, 0), -1, true, true);
		World.spawnNPC(530, new WorldTile(3477, 3485, 0), -1, true, true);
		World.spawnNPC(531, new WorldTile(3692, 3462, 0), -1, true, true);
		World.spawnNPC(534, new WorldTile(3491, 3471, 0), -1, true, true);
		World.spawnNPC(535, new WorldTile(2646, 9393, 0), -1, true, true);
		World.spawnNPC(551, new WorldTile(3692, 3465, 0), -1, true, true);
		World.spawnNPC(552, new WorldTile(3692, 3468, 0), -1, true, true);
		World.spawnNPC(554, new WorldTile(3699, 3462, 0), -1, true, true);
		World.spawnNPC(555, new WorldTile(3693, 3461, 0), -1, true, true);
		World.spawnNPC(561, new WorldTile(3248, 9363, 0), -1, true, true);
		World.spawnNPC(1699, new WorldTile(3694, 3461, 0), -1, true, true);
		World.spawnNPC(1917, new WorldTile(3695, 3461, 0), -1, true, true);
		World.spawnNPC(11678, new WorldTile(3694, 3471, 0), -1, true, true);
		World.spawnNPC(11679, new WorldTile(3699, 3463, 0), -1, true, true);
		World.spawnNPC(536, new WorldTile(3692, 3471, 0), -1, true, true);
		World.spawnNPC(537, new WorldTile(3693, 3471, 0), -1, true, true);
		World.spawnNPC(538, new WorldTile(3692, 3461, 0), -1, true, true);
		World.spawnNPC(539, new WorldTile(3693, 3461, 0), -1, true, true);
		World.spawnNPC(540, new WorldTile(3694, 3461, 0), -1, true, true);
		World.spawnNPC(541, new WorldTile(3699, 3464, 0), -1, true, true);
		World.spawnNPC(542, new WorldTile(3696, 3461, 0), -1, true, true);
		World.spawnNPC(544, new WorldTile(3697, 3461, 0), -1, true, true);
		World.spawnNPC(545, new WorldTile(3698, 3461, 0), -1, true, true);
		World.spawnNPC(546, new WorldTile(3686, 3469, 0), -1, true, true);
		World.spawnNPC(9710, new WorldTile(3692, 3465, 0), -1, true, true);
		World.spawnNPC(45, new WorldTile(3248, 9365, 0), -1, true, true);
		//End home
		World.spawnNPC(50, new WorldTile(2258, 4694, 0), -1, true, true);
		World.spawnNPC(6260, new WorldTile(2870, 5363, 2), -1, true, true);
		World.spawnNPC(6247, new WorldTile(2898, 5262, 0), -1, true, true);
		World.spawnNPC(8133, new WorldTile(2894, 4393, 0), -1, true, true);
		World.spawnNPC(8133, new WorldTile(2994, 4381, 0), -1, true, true);
		World.spawnNPC(8349, new WorldTile(2604, 5712, 0), -1, true, true);
		World.spawnNPC(8349, new WorldTile(2617, 5726, 0), -1, true, true);
		World.spawnNPC(6222, new WorldTile(2828, 5302, 2), -1, true, true);
		World.spawnNPC(6203, new WorldTile(2934, 5324, 2), -1, true, true);
		World.spawnNPC(6265, new WorldTile(2867, 5354, 2), -1, true, true);
		World.spawnNPC(6263, new WorldTile(2870, 5354, 2), -1, true, true);
		World.spawnNPC(6261, new WorldTile(2874, 5354, 2), -1, true, true);
		//End Godwars And TD And Corp
		World.spawnNPC(1265, new WorldTile(2700, 3715, 0), -1, true, true);
		World.spawnNPC(1265, new WorldTile(2696, 3719, 0), -1, true, true);
		World.spawnNPC(1265, new WorldTile(2706, 3724, 0), -1, true, true);
		World.spawnNPC(1265, new WorldTile(2711, 3719, 0), -1, true, true);
		World.spawnNPC(1265, new WorldTile(2717, 3726, 0), -1, true, true);
		World.spawnNPC(1265, new WorldTile(2721, 3717, 0), -1, true, true);
		World.spawnNPC(1265, new WorldTile(2721, 3706, 0), -1, true, true);
		World.spawnNPC(1265, new WorldTile(2716, 3700, 0), -1, true, true);
		// Quest
		World.spawnNPC(7888, new WorldTile(3686, 3470, 0), -1, true, true); //Ozan
		World.spawnNPC(484, new WorldTile(2186, 3145, 0), -1, true, true); //LocalGnome
		World.spawnNPC(7133, new WorldTile(2897, 2733, 0), -1, true, true); //bork
		World.spawnNPC(1452, new WorldTile(2803, 2789, 0), -1, true, true); //monkey child		
		//End Crabs start abyss
		World.spawnNPC(1615, new WorldTile(3029, 4842, 0), -1, true, true);
		World.spawnNPC(3200, new WorldTile(3026, 4829, 0), -1, true, true);
		World.spawnNPC(1615, new WorldTile(3033, 4821, 0), -1, true, true);
		World.spawnNPC(3200, new WorldTile(3047, 4822, 0), -1, true, true);
		World.spawnNPC(1615, new WorldTile(3051, 4837, 0), -1, true, true);
		//Start thieving npcs and home wolfs
		World.spawnNPC(1, new WorldTile(3490, 3491, 0), -1, true, true);
		World.spawnNPC(2, new WorldTile(3503, 3488, 0), -1, true, true);
		World.spawnNPC(4, new WorldTile(3490, 3483, 0), -1, true, true);
		World.spawnNPC(5, new WorldTile(3507, 3503, 0), -1, true, true);
		World.spawnNPC(7, new WorldTile(3472, 3499, 0), -1, true, true);
		World.spawnNPC(15, new WorldTile(3482, 3476, 0), -1, true, true);
		World.spawnNPC(1715, new WorldTile(3479, 3496, 1), -1, true, true);
		World.spawnNPC(1714, new WorldTile(3497, 3474, 1), -1, true, true);
		World.spawnNPC(2109, new WorldTile(3497, 3485, 0), -1, true, true);
		//start of pest control
		World.spawnNPC(6142, new WorldTile(2628, 2591, 0), -1, true, true);
		World.spawnNPC(6143, new WorldTile(2680, 2588, 0), -1, true, true);
		World.spawnNPC(6144, new WorldTile(2669, 2570, 0), -1, true, true);
		World.spawnNPC(6145, new WorldTile(2645, 2569, 0), -1, true, true);
		//Start of fishing
		World.spawnNPC(327, new WorldTile(2478, 5128, 0), -1, true, true);
		World.spawnNPC(328, new WorldTile(2484, 5134, 0), -1, true, true);
		World.spawnNPC(312, new WorldTile(2488, 5137, 0), -1, true, true);
		World.spawnNPC(6267, new WorldTile(2492, 5128, 0), -1, true, true);
		World.spawnNPC(44, new WorldTile(2472, 5128, 0), -1, true, true);
		//Start of revs
		World.spawnNPC(13481, new WorldTile(3109, 10148, 0), -1, true, true);
		World.spawnNPC(13481, new WorldTile(3116, 10153, 0), -1, true, true);
		World.spawnNPC(13481, new WorldTile(3119, 10163, 0), -1, true, true);
		World.spawnNPC(13481, new WorldTile(3099, 10165, 0), -1, true, true);
		World.spawnNPC(13481, new WorldTile(3096, 10154, 0), -1, true, true);
		World.spawnNPC(13481, new WorldTile(3106, 10156, 0), -1, true, true);
		//start of daggs
		World.spawnNPC(2883, new WorldTile(3091, 10124, 0), -1, true, true);
		World.spawnNPC(2883, new WorldTile(3087, 10135, 0), -1, true, true);
		World.spawnNPC(2883, new WorldTile(3006, 10123, 0), -1, true, true);
		//start of bork
		World.spawnNPC(7133, new WorldTile(2873, 5207, 0), -1, true, true);
		//start of frost dragons
		World.spawnNPC(51, new WorldTile(3044, 9230, 0), -1, true, true);
		World.spawnNPC(51, new WorldTile(3040, 9242, 0), -1, true, true);
		World.spawnNPC(51, new WorldTile(3032, 9238, 0), -1, true, true);
		World.spawnNPC(51, new WorldTile(3024, 9229, 0), -1, true, true);
		World.spawnNPC(51, new WorldTile(3022, 9240, 0), -1, true, true);
		World.spawnNPC(51, new WorldTile(3022, 9254, 0), -1, true, true);
		World.spawnNPC(51, new WorldTile(3014, 9261, 0), -1, true, true);
		//Start Wyrmzz
		World.spawnNPC(9463, new WorldTile(2983, 9517, 1), -1, true, true);
		World.spawnNPC(9465, new WorldTile(3299, 3020, 0), -1, true, true);
		World.spawnNPC(9465, new WorldTile(3300, 3028, 0), -1, true, true);
		World.spawnNPC(9467, new WorldTile(2605, 4770, 0), -1, true, true);*/

		/**
		 * Object custom add
		 */
		/*World.spawnObject(new WorldObject(172, 10, 2, 3698, 3471, 0), true);
		World.spawnObject(new WorldObject(9398, 10, 0, 2470, 5129, 0), true);
		World.spawnObject(new WorldObject(2478, 10, 0, 3250, 9359, 0), true);
		World.spawnObject(new WorldObject(2479, 10, 0, 3345, 9358, 0), true);
		World.spawnObject(new WorldObject(2480, 10, 0, 3245, 9359, 0), true);
		World.spawnObject(new WorldObject(2481, 10, 0, 3243, 9371, 0), true);
		World.spawnObject(new WorldObject(2482, 10, 0, 3246, 9370, 0), true);
		World.spawnObject(new WorldObject(2483, 10, 0, 3248, 9370, 0), true);
		World.spawnObject(new WorldObject(2484, 10, 0, 3251, 9370, 0), true);
		World.spawnObject(new WorldObject(2486, 10, 0, 3252, 9367, 0), true);
		World.spawnObject(new WorldObject(2487, 10, 0, 3253, 9363, 0), true);
		World.spawnObject(new WorldObject(2488, 10, 0, 3253, 9358, 0), true);
		World.spawnObject(new WorldObject(17010, 10, 0, 3247, 9366, 0), true);
		World.spawnObject(new WorldObject(30624, 10, 0, 3242, 9366, 0), true);
		World.spawnObject(new WorldObject(6552, 10, 0, 3178, 5706, 0), true);
		World.spawnObject(new WorldObject(17010, 10, 0, 3184, 5705, 0), true);
		World.spawnObject(new WorldObject(114, 10, 3, 2471, 5130, 0), true);
		World.spawnObject(new WorldObject(2273, 10, 3, 3037, 10171, 0), true);*/
		World.spawnObject(new WorldObject(170, 10, 3, 2892, 2717, 0), true);

		// spawn home altar
		World.spawnObject(new WorldObject(13190, 10, 0, 2407, 2844, 0), true);

		World.deleteObject(new WorldTile(3691, 3469, 0));
		World.deleteObject(new WorldTile(3694, 3468, 0));
		World.deleteObject(new WorldTile(3695, 3468, 0));
		World.deleteObject(new WorldTile(3696, 3470, 0));

		//
		World.deleteObject(new WorldTile(3282, 3499, 0));
		World.deleteObject(new WorldTile(3283, 3499, 0));
		World.deleteObject(new WorldTile(3283, 3498, 0));
		World.deleteObject(new WorldTile(3282, 3498, 0));
		World.deleteObject(new WorldTile(3282, 3500, 0));
		World.deleteObject(new WorldTile(3283, 3500, 0));
		World.deleteObject(new WorldTile(3283, 3497, 0));
		World.deleteObject(new WorldTile(3282, 3497, 0));
		//next table
		World.deleteObject(new WorldTile(3278, 3500, 0));
		World.deleteObject(new WorldTile(3277, 3500, 0));
		World.deleteObject(new WorldTile(3278, 3499, 0));
		World.deleteObject(new WorldTile(3278, 3499, 0));
		World.deleteObject(new WorldTile(3278, 3498, 0));
		World.deleteObject(new WorldTile(3277, 3498, 0));
		World.deleteObject(new WorldTile(3277, 3497, 0));
		World.deleteObject(new WorldTile(3278, 3497, 0));
		//next table
		World.deleteObject(new WorldTile(3282, 3495, 0));
		World.deleteObject(new WorldTile(3283, 3495, 0));
		World.deleteObject(new WorldTile(3283, 3494, 0));
		World.deleteObject(new WorldTile(3283, 3493, 0));
		World.deleteObject(new WorldTile(3282, 3493, 0));
		World.deleteObject(new WorldTile(3282, 3494, 0));
		//next table
		World.deleteObject(new WorldTile(3279, 3493, 0));
		World.deleteObject(new WorldTile(3278, 3492, 0));
		World.deleteObject(new WorldTile(3276, 3493, 0));
		World.deleteObject(new WorldTile(3276, 3494, 0));
		World.deleteObject(new WorldTile(3277, 3494, 0));
		World.deleteObject(new WorldTile(3278, 3494, 0));
		World.deleteObject(new WorldTile(3276, 3493, 0));
		World.deleteObject(new WorldTile(3277, 3493, 0));
		//table
		World.deleteObject(new WorldTile(3284, 3489, 0));
		World.deleteObject(new WorldTile(3284, 3488, 0));
		//coloms
		World.deleteObject(new WorldTile(3284, 3501, 0));
		World.deleteObject(new WorldTile(3284, 3496, 0));
		World.deleteObject(new WorldTile(3277, 3496, 0));
		World.deleteObject(new WorldTile(3277, 3501, 0));
		//windows table
		World.deleteObject(new WorldTile(3286, 3497, 0));
		World.deleteObject(new WorldTile(3286, 3498, 0));
		World.deleteObject(new WorldTile(3286, 3500, 0));
		World.deleteObject(new WorldTile(3286, 3502, 0));
		//table by windows
		World.deleteObject(new WorldTile(3287, 3498, 0));
		World.deleteObject(new WorldTile(3287, 3499, 0));
		World.deleteObject(new WorldTile(3287, 3500, 0));
		World.deleteObject(new WorldTile(3287, 3501, 0));
		World.deleteObject(new WorldTile(3275, 3497, 0));
		World.deleteObject(new WorldTile(3275, 3496, 0));
		//bar
		World.deleteObject(new WorldTile(3280, 3489, 0));
		World.deleteObject(new WorldTile(3279, 3489, 0));
		World.deleteObject(new WorldTile(3278, 3489, 0));
		World.deleteObject(new WorldTile(3277, 3489, 0));
		World.deleteObject(new WorldTile(3276, 3489, 0));
		World.deleteObject(new WorldTile(3275, 3489, 0));
		World.deleteObject(new WorldTile(3274, 3489, 0));
		World.deleteObject(new WorldTile(3273, 3489, 0));
		World.deleteObject(new WorldTile(3272, 3489, 0));
		World.deleteObject(new WorldTile(3272, 3489, 0));
		World.deleteObject(new WorldTile(3272, 3487, 0));
		World.deleteObject(new WorldTile(3273, 3487, 0));
		World.deleteObject(new WorldTile(3272, 3488, 0));
		World.deleteObject(new WorldTile(3274, 3490, 0));
		World.deleteObject(new WorldTile(3276, 3490, 0));
		World.deleteObject(new WorldTile(3278, 3490, 0));
		//shelfs
		World.deleteObject(new WorldTile(3272, 3486, 0));
		World.deleteObject(new WorldTile(3273, 3486, 0));
		World.deleteObject(new WorldTile(3275, 3486, 0));
		World.deleteObject(new WorldTile(3276, 3486, 0));
		World.deleteObject(new WorldTile(3278, 3486, 0));
		World.deleteObject(new WorldTile(3279, 3486, 0));
		World.deleteObject(new WorldTile(3280, 3486, 0));
		World.deleteObject(new WorldTile(3281, 3486, 0));
		World.deleteObject(new WorldTile(3282, 3486, 0));
		//next table
		World.deleteObject(new WorldTile(3276, 3503, 0));
		World.deleteObject(new WorldTile(3276, 3504, 0));
		World.deleteObject(new WorldTile(3278, 3503, 0));
		World.deleteObject(new WorldTile(3278, 3504, 0));
		World.deleteObject(new WorldTile(3277, 3503, 0));
		World.deleteObject(new WorldTile(3277, 3504, 0));
		//next table
		World.deleteObject(new WorldTile(3284, 3503, 0));
		World.deleteObject(new WorldTile(3284, 3504, 0));
		World.deleteObject(new WorldTile(3286, 3503, 0));
		World.deleteObject(new WorldTile(3286, 3504, 0));
		World.deleteObject(new WorldTile(3285, 3503, 0));
		World.deleteObject(new WorldTile(3285, 3504, 0));
		//stairs
		World.deleteObject(new WorldTile(3285, 3493, 0));
		World.deleteObject(new WorldTile(2971, 3370, 0));


		
	}

	/**
	 * The NPC classes.
	 */
	private static final Map<Integer, Class<?>> CUSTOM_NPCS = new HashMap<Integer, Class<?>>();

	public static void npcSpawn() {
		int size = 0;
		boolean ignore = false;
		try {
			for (String string : FileUtilities
					.readFile("data/npcs/npcspawns.txt")) {
				if (string.startsWith("//") || string.equals("")) {
					continue;
				}
				if (string.contains("/*")) {
					ignore = true;
					continue;
				}
				if (ignore) {
					if (string.contains("*/")) {
						ignore = false;
					}
					continue;
				}
				String[] spawn = string.split(" ");
				@SuppressWarnings("unused")
				int id = Integer.parseInt(spawn[0]), x = Integer
						.parseInt(spawn[1]), y = Integer.parseInt(spawn[2]), z = Integer
						.parseInt(spawn[3]), faceDir = Integer
						.parseInt(spawn[4]);
				NPC npc = null;
				Class<?> npcHandler = CUSTOM_NPCS.get(id);
				if (npcHandler == null) {
					npc = new NPC(id, new WorldTile(x, y, z), -1, true, false);
				} else {
					npc = (NPC) npcHandler.getConstructor(int.class)
							.newInstance(id);
				}
				if (npc != null) {
					WorldTile spawnLoc = new WorldTile(x, y, z);
					npc.setLocation(spawnLoc);
					World.spawnNPC(npc.getId(), spawnLoc, -1, true, false);
					size++;
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		System.err.println("Loaded " + size + " custom npc spawns!");
	}

}