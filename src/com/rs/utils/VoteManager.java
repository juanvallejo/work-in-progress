package com.rs.utils;

import java.sql.*;

import com.rs.game.player.Player;
import com.rs.game.World;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
/** V1 converted by Displee **/
 public class VoteManager {
	
    public static Connection con = null;
    public static Statement stmt;
    public static boolean connectionMade;
	
    public static void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String IP="ridiculous-pk.co.uk";
            String DB="ridicul6_vote";
            String User="ridicul6_vote";
            String Pass="darby123"; 
            con = DriverManager.getConnection("jdbc:mysql://"+IP+"/"+DB, User, Pass);
            stmt = con.createStatement();
        } catch (Exception e) {
            Logger.log("VoteManager", "Connection to SQL database failed!");
            e.printStackTrace();
        }
    }

	public static boolean checkVote(Player p) {
        try {
        		createConnection();
                Statement stmt = con.createStatement();
        		String playerName = p.getUsername();
                ResultSet rs = query("SELECT status FROM `votes` WHERE `username`= '" + playerName + "'");
                if(p.getInventory().getItems().freeSlots() > 0) {
				try {
				String urlString = "http://ridiculous-pk.co.uk/vote.php?user=" + playerName;
                        urlString = urlString.replaceAll(" ", "%20");
                        URL url = new URL(urlString);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                        String results = reader.readLine();
				if (results.equals("1")) {
                if(rs.next()) {
                    int itemid = 995;
                    int amount = 30000000;
                    query("UPDATE `votes` SET `status` = 0 WHERE `username` = '" + playerName + "'");
                    p.getInventory().addItem(itemid, amount);
					//p.setVoteDelay(Utils.currentTimeMillis() + 86400000);
                    p.getPackets().sendGameMessage("Thank you for voting and enjoy your reward!");
					for (Player players : World.getPlayers()) {
						if (players == null)
							continue;
					players.getPackets().sendGameMessage("<col=ff0000>"+p.getDisplayName()+"</col> Has Just Voted And Claimed Their Prize!");
					}
                } else {
                    p.getPackets().sendGameMessage("You haven't voted yet. To vote use: ::vote");
                }
				}
				} catch (MalformedURLException e) {
                        System.out.println("Malformed URL Exception in checkVotes(String playerName)");
						return false;
                } catch (IOException e) {
                        System.out.println("IO Exception in checkVotes(String playerName)");
						return false;
                }
            } else {
                p.getPackets().sendGameMessage("Please make space for your items.");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
		return false;
    }
	
    public static ResultSet query(String s) throws SQLException {
        try {
            if (s.toLowerCase().startsWith("select")) {
                ResultSet rs = stmt.executeQuery(s);
                return rs;
            } else {
                stmt.executeUpdate(s);
            }
            return null;
        } catch (Exception e) {
            //destroyConnection();
			con = null;
            stmt = null;
        }
        return null;
    }
    	
}