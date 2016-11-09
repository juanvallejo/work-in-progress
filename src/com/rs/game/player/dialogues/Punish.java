package com.rs.game.player.dialogues;
 
public class Punish extends Dialogue {
 
 
 
    @Override
    public void start() {
    if (player.getRights() >= 1) {
        sendOptionsDialogue("Punish Panel", "Teleto",
                "Teletome", "Kick", "Mute's", "More Commands");
        stage = 2;
    } else {
        sendDialogue("<col=FF0000>You must be staff in-order to use this");
    }
    }
 
    @Override
    public void run(int interfaceId, int componentId) {
         if (stage == 2) {
            if (componentId == OPTION_1) {
                player.getTemporaryAttributtes().put("teleto_player", true);
                player.getPackets().sendRunScript(109, "Enter Name To Teleport To Them:");
                end();
            } else if (componentId == OPTION_2) {
                player.getTemporaryAttributtes().put("teletome_player", true);
                player.getPackets().sendRunScript(109, "Enter Name To Teleport Them To You:");
                end();
            } else if (componentId == OPTION_3) {
                player.getTemporaryAttributtes().put("kick_player", true);
                player.getPackets().sendRunScript(109, "Enter Name To Kick Them:");
                end();
            } else if (componentId == OPTION_4) {
                sendOptionsDialogue("Mute's", "One Hour Mute",
                        "Six Hour Mute", "Twelve Hour Mute", "One Day Mute", "Two Day Mute");
                stage = 3;
            } else if (componentId == OPTION_5) {
                stage = 4;
                sendOptionsDialogue("More Commands", "Jail",
                        "Ban's", "Un-Jail", "Un-Mute", "Un-Ban");
            }           
            }    else if (stage == 4) {
                if (componentId == OPTION_1) {
                    player.getTemporaryAttributtes().put("jail_player", true);
                    player.getPackets().sendRunScript(109, "Enter Name To Jail To Them:");
                    end();
                } else if (componentId == OPTION_2) {
                    player.getTemporaryAttributtes().put("ban_player", true);
                    player.getPackets().sendRunScript(109, "Enter Name To Ban To Them for 12 Hours:");
                    end();
                } else if (componentId == OPTION_3) {
                    player.getTemporaryAttributtes().put("unjail_player", true);
                    player.getPackets().sendRunScript(109, "Enter Name of player to unjail them:");
                    end();
                } else if (componentId == OPTION_4) {
                    player.getTemporaryAttributtes().put("unmute_player", true);
                    player.getPackets().sendRunScript(109, "Enter Name of player to unmute them:");
                    end();
                } else if (componentId == OPTION_5) {
                    player.getTemporaryAttributtes().put("unban_player", true);
                    player.getPackets().sendRunScript(109, "Enter Name of player to unban them:");
                    end();
                }
              
        }    else if (stage == 3) {
            if (componentId == OPTION_1) {
                player.getTemporaryAttributtes().put("mute_player", true);
                player.getPackets().sendRunScript(109, "Enter Name To Mute To Them for 1 Hour:");
                end();
            } else if (componentId == OPTION_2) {
                player.getTemporaryAttributtes().put("mute_player1", true);
                player.getPackets().sendRunScript(109, "Enter Name To Mute To Them for 6 Hours:");
                end();
            } else if (componentId == OPTION_3) {
                player.getTemporaryAttributtes().put("mute_player2", true);
                player.getPackets().sendRunScript(109, "Enter Name To Mute To Them for 12 Hours:");
                end();
            } else if (componentId == OPTION_4) {
                player.getTemporaryAttributtes().put("mute_player3", true);
                player.getPackets().sendRunScript(109, "Enter Name To Mute To Them for 1 Day:");
                end();
            } else if (componentId == OPTION_5) {
                player.getTemporaryAttributtes().put("mute_player4", true);
                player.getPackets().sendRunScript(109, "Enter Name To Mute To Them for 2 Days:");
                end();
            }
         }
    }
 
 
    @Override
    public void finish() {
 
    }
}