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

public final class ObjectSpawns {

	public static final void init() {
		if (!new File("data/map/packedSpawns").exists())
			packObjectSpawns();
	}

	private static boolean deleteDir(File rootNode) {
		if (rootNode == null) {
			return false;
		}
		File[] files = rootNode.listFiles();
		if (files == null) {
			return (rootNode.delete());
		}
		for(int i = 0; i < files.length; i++) {
			if(files[i].isDirectory()) {
				deleteDir(files[i]);
				continue;
			}
			files[i].delete();
		}
		return (rootNode.delete());
	}

	public static final void packObjectSpawns() {
		Logger.log("ObjectSpawns", "Packing object spawns...");

		File packedSpawns = new File("data/map/packedSpawns");
		if(packedSpawns.exists()) {
			deleteDir(packedSpawns);
		}
		if(!packedSpawns.mkdir()) {
			throw new RuntimeException(
					"Couldn't create packedSpawns directory.");
		}

		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"data/map/unpackedSpawnsList.txt"));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//") || line.length() == 0)
					continue;
				String[] splitedLine = line.split(" - ");
				if (splitedLine.length != 2)
					throw new RuntimeException("Invalid Object Spawn line: "
							+ line);
				String[] splitedLine2 = splitedLine[0].split(" ");
				String[] splitedLine3 = splitedLine[1].split(" ");
				if (splitedLine2.length != 3 || splitedLine3.length != 4)
					throw new RuntimeException("Invalid Object Spawn line: "
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
			Logger.log("ObjectSpawns", "Finished packing object spawns...");
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void loadObjectSpawns(int regionId) {
		File file = new File("data/map/packedSpawns/" + regionId + ".os");
		if (!file.exists())
			return;
		try {
			RandomAccessFile in = new RandomAccessFile(file, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			while (buffer.hasRemaining()) {
				int objectId = buffer.getShort() & 0xffff;
				int type = buffer.get() & 0xff;
				int rotation = buffer.get() & 0xff;
				int plane = buffer.get() & 0xff;
				int x = buffer.getShort() & 0xffff;
				int y = buffer.getShort() & 0xffff;
				boolean cliped = buffer.get() == 1;
				World.spawnObject(new WorldObject(objectId, type, rotation, x,
						y, plane), cliped);
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

	private ObjectSpawns() {
	}

}
