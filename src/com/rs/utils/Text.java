package com.rs.utils;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
 
import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
 
public class Text {
 
     
            public static void init() {
                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                   for(Iterator<?> iterator = World.getPlayers().iterator(); iterator.hasNext();)
                         {
                             Player players = (Player)iterator.next();
                             players.getPackets().sendIComponentText(187, 4, "Players On " +Settings.SERVER_NAME +": <col=ff0000>" + World.getPlayers().size());
                         }
                    }
                }, 0, 30);
                   
                 }
}