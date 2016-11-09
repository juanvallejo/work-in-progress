package com.rs.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;

public final class FarmSpawn {

	public static final void init(Player player) {
		if (!new File("data/farm/"+player.getUsername()+".txt").exists())
			packFarmSpawns(player);
	}

	private static final void packFarmSpawns(Player player) {
		Logger.log("ObjectSpawns", "Packing farming patches...");
		if (!new File("data/map/packedSpawns").mkdir())
			throw new RuntimeException(
					"Couldn't create packedSpawns directory.");
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"data/farm/"+player.getUsername()+".txt"));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ");
				if (splitedLine.length != 2)
					throw new RuntimeException("Invalid farm Spawn line: "
							+ line);
				String[] splitedLine2 = splitedLine[0].split(" ");
				String[] splitedLine3 = splitedLine[1].split(" ");
				if (splitedLine2.length != 3 || splitedLine3.length != 4)
					throw new RuntimeException("Invalid farm Spawn line: "
							+ line);
				int objectId = Integer.parseInt(splitedLine2[0]);
				int type = Integer.parseInt(splitedLine2[1]);
				int rotation = Integer.parseInt(splitedLine2[2]);

				WorldTile tile = new WorldTile(
						Integer.parseInt(splitedLine3[0]),
						Integer.parseInt(splitedLine3[1]),
						Integer.parseInt(splitedLine3[2]));
				addObjectSpawn(objectId, type, rotation, tile.getRegionId(),
						tile, Boolean.parseBoolean(splitedLine3[3]));
			}
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void loadFarmSpawns(Player player, int configId, int grow) {
		File file = new File("data/farm/"+player.getUsername()+".txt");
		if (!file.exists())
			return;
		try {
			RandomAccessFile in = new RandomAccessFile(file, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			while (buffer.hasRemaining()) {
				player.getPackets().sendConfigByFile(configId, grow);
			}
			channel.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final void addObjectSpawn(int objectId, int type,
			int rotation, int regionId, WorldTile tile, boolean cliped) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					"data/map/packedSpawns/" + regionId + ".os", true));
			out.writeShort(objectId);
			out.writeByte(type);
			out.writeByte(rotation);
			out.writeByte(tile.getPlane());
			out.writeShort(tile.getX());
			out.writeShort(tile.getY());
			out.writeBoolean(cliped);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private FarmSpawn() {
	}

}
