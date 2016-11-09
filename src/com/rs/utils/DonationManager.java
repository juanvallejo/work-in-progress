package com.rs.utils;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
import com.rs.game.player.Player;
import com.rs.game.World;
 
public class DonationManager {
 
    public static Connection con = null;
    public static Statement stmt;
    public static boolean connectionMade;
 
     
    public static void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        String IP="ridiculous-pk.co.uk";
        String DB="ridicul6_donate";
        String User="ridicul6_donate";
        String Pass="darby123"; 
            con = DriverManager.getConnection("jdbc:mysql://"+IP+"/"+DB, User, Pass);
            stmt = con.createStatement();
        System.out.println("Connection to Donation database successful!"); //You can take these system prints out. Get annoying sometimes.
        } catch (Exception e) {
        System.out.println("Connection to Donation database failed");
        e.printStackTrace();
        }
    }
     
    public static void startProcess(final Player player) {  //Choose which payment options give which things.
        createConnection();
        if(checkDonation(player.getUsername())) {
            if(player.getInventory().getFreeSlots() < 10) {
                player.getPackets().sendGameMessage("<col=00FFCC>Create some more space in your inventory Before doing this!");           
                return;
            }
            /*
            *   READ THIS!!!
            *   To Choose which item they would get simple edit the below product id's.
            *   Example: If they chose option number 4 on the website, the product id would be 4.
            */
            if(checkDonationItem(player.getUsername()) == 1) { //Productid 1 
                player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Received : Regular Donator!");
                player.getPackets().sendGameMessage("<col=00FFCC>The donatorzone can be found at ::dz");
                if (!player.isDonator()) {
                    player.setDonator(true);
                    for(Player p : World.getPlayers()) {
                        if (p ==null) {
                            continue; 
                        }
                player.getPackets().sendGameMessage("Enjoy Your Donation, Thanks for Donating!!");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>" + player.getUsername() + " has donated tell him thanks for contributing!<img=1>");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>Donate at www.ridiculous-pk.co.uk/donate!<img=1>");
                    }
                }
                donationGiven(player.getUsername());        
            }
            if(checkDonationItem(player.getUsername()) == 2) { //Productid 2 
                player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Received : Extreme Donator!");
                player.getPackets().sendGameMessage("<col=00FFCC>The donatorzone can be found by the portal at home or ::donatorzone");
                if (!player.isDonator()) {
                    player.setDonator(true);
                if (!player.isExtremeDonator()) {
                    player.setExtremeDonator(true);
                    for(Player p : World.getPlayers()) {
                        if (p ==null) {
                            continue; 
                        }
                    player.getPackets().sendGameMessage("Enjoy Your Donation, Thanks for Donating!!");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>" + player.getUsername() + " has donated tell him thanks for contributing!<img=1>");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>Donate at www.ridiculous-pk.co.uk/donate!<img=1>");
                    }
                }
                }
                donationGiven(player.getUsername());        
            }
            if(checkDonationItem(player.getUsername()) == 3) { //Productid 3
                player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Recieved 2 Donator tokens!");
               player.donatorPoints+=2;
                    for(Player p : World.getPlayers()) {
                        if (p ==null) {
                            continue; 
                        }
                    player.getPackets().sendGameMessage("Enjoy Your Donation, Thanks for Donating!!");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>" + player.getUsername() + " has donated tell him thanks for contributing!<img=1>");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>Donate at www.ridiculous-pk.co.uk/donate!<img=1>");
                    }
                donationGiven(player.getUsername());        
            }
            if(checkDonationItem(player.getUsername()) == 4) { //Productid 4 
                player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Recieved Your : 5 donator tokens!");
                player.donatorPoints+=5;
                    for(Player p : World.getPlayers()) {
                        if (p ==null) {
                            continue; 
                        }
                    player.getPackets().sendGameMessage("Enjoy Your Donation, Thanks for Donating!!");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>" + player.getUsername() + " has donated tell him thanks for contributing!<img=1>");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>Donate at www.ridiculous-pk.co.uk!<img=1>");
                    }
                donationGiven(player.getUsername());        
            }
        }
    }
          /*  if(checkDonationItem(player.getUsername()) == 5) { //Productid 5
                player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Recieved Your : H'Ween Set!");
                player.getInventory().addItem(1053, 1);
                player.getInventory().addItem(1055, 1);
                player.getInventory().addItem(1057, 1);
                    for(Player p : World.getPlayers()) {
                        if (p ==null) {
                            continue; 
                        }
                    player.getPackets().sendGameMessage("Enjoy Your Donation, Thanks for Donating!!");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>" + player.getUsername() + "has donated tell him thanks for contributing!<img=1>");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1Donate at pkerz614.com!<img=1>");
                    }
                donationGiven(player.getUsername());        
            }
            if(checkDonationItem(player.getUsername()) == 6) { //Productid 6
                player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Recieved Your : Santa Hat!");
                player.getPackets().sendGameMessage("<col=00FFCC>The donatorzone can be found by the portal at home or ::donatorzone");
                player.getInventory().addItem(1050, 1);
                    for(Player p : World.getPlayers()) {
                        if (p ==null) {
                            continue; 
                        }
                    player.getPackets().sendGameMessage("Enjoy Your Donation, Thanks for Donating!!");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>" + player.getUsername() + "has donated tell him thanks for contributing!<img=1>");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1Donate at pkerz614.com!<img=1>");
                    }
                donationGiven(player.getUsername());        
            }
            if(checkDonationItem(player.getUsername()) == 7) { //Productid 7
                player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Recieved Your : Mystery Box's");
                player.getInventory().addItem(6199, 10);
                    for(Player p : World.getPlayers()) {
                        if (p ==null) {
                            continue; 
                        }
                    player.getPackets().sendGameMessage("Enjoy Your Donation, Thanks for Donating!!");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>" + player.getUsername() + "has donated tell him thanks for contributing!<img=1>");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1Donate at pkerz614.com!<img=1>");
                    }
                donationGiven(player.getUsername());        
            }
            if(checkDonationItem(player.getUsername()) == 8) { //Productid 8 
                player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Recieved Your : Torva Set!");
                player.getInventory().addItem(20135, 1);
                player.getInventory().addItem(20139, 1);
                player.getInventory().addItem(20143, 1);
                    for(Player p : World.getPlayers()) {
                        if (p ==null) {
                            continue; 
                        }
                    player.getPackets().sendGameMessage("Enjoy Your Donation, Thanks for Donating!!");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>" + player.getUsername() + "has donated tell him thanks for contributing!<img=1>");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1Donate at pkerz614.com!<img=1>");
                    }
                donationGiven(player.getUsername());        
            }
            if(checkDonationItem(player.getUsername()) == 9) { //Productid 9
                player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Recieved Your : Virtus Set!");
                player.getInventory().addItem(20159, 1);
                player.getInventory().addItem(20163, 1);
                player.getInventory().addItem(20167, 1);
                    for(Player p : World.getPlayers()) {
                        if (p ==null) {
                            continue; 
                        }
                    player.getPackets().sendGameMessage("Enjoy Your Donation, Thanks for Donating!!");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>" + player.getUsername() + "has donated tell him thanks for contributing!<img=1>");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1Donate at pkerz614.com!<img=1>");
                    }
                donationGiven(player.getUsername());        
            }
            if(checkDonationItem(player.getUsername()) == 10) { //Productid 10
                player.getPackets().sendGameMessage("<shad=cc0ff><img=1>You Have Recieved Your : Pernix!");
                player.getInventory().addItem(20147, 1);
                player.getInventory().addItem(20151, 1);
                player.getInventory().addItem(20155, 1);
                    for(Player p : World.getPlayers()) {
                        if (p ==null) {
                            continue; 
                        }
                    player.getPackets().sendGameMessage("Enjoy Your Donation, Thanks for Donating!!");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1>" + player.getUsername() + "has donated tell him thanks for contributing!<img=1>");
                p.getPackets().sendGameMessage("<shad=cc0ff><img=1Donate at pkerz614.com!<img=1>");
                    }
                donationGiven(player.getUsername());        
            }
        }
    }*/
     
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
            destroyConnection();
        }
        return null;
    }
 
    public static void destroyConnection() {
        try {
            stmt.close();
            con.close();
        } catch (Exception e) {
        }
    }
 
    public static boolean checkDonation(String playerName) {
    
        try {
            String name2 = playerName.replaceAll("_", " ");
            Statement statement = con.createStatement();
            String query = "SELECT * FROM donation WHERE username = '" + name2 + "'";
            ResultSet results = statement.executeQuery(query);
                while(results.next()) {
                    int tickets = results.getInt("tickets"); //tickets are "Recieved" technically. 0 for claimed, 1 for not claimed.
                        if(tickets == 1) {                     
                         return true;
                         }
                                 
                        }
                } catch(SQLException e) {
                        e.printStackTrace();
                }
                 
                return false;
                 
        }
     
    public static int checkDonationItem(String playerName) {
         
        try {
            String name2 = playerName.replaceAll("_", " ");
            Statement statement = con.createStatement();
            String query = "SELECT * FROM donation WHERE username = '" + name2 + "'";
            ResultSet results = statement.executeQuery(query);
                while(results.next()) {
                    int productid = results.getInt("productid");
                        if(productid >= 1) {                                       
                            return productid;
                            }
                                                                
                        }
                } catch(SQLException e) {
                        e.printStackTrace();
                }
                 
                return 0;
                 
        }       
     
    public static boolean donationGiven(String playerName) {       
               
              try
                {
                String name2 = playerName.replaceAll("_", " ");
                query("DELETE FROM `donation` WHERE username = '"+name2+"';");
                       // query("UPDATE donations SET tickets = 0 WHERE username = '" + playerName + "'");
                        //query("UPDATE donations SET productid = 0 WHERE username = '" + playerName + "'");
                         
                } catch (Exception e) {
                        e.printStackTrace();
                         
                        return false;
                }
                return true;
        }   
     
}