package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class ModPanel extends Dialogue {

    private int playerIndex;
    
    @Override
    public void start() {
        playerIndex = (Integer) parameters[0];
        Player target = World.getPlayers().get(playerIndex);
        stage = 1;
        if (stage == 1) {
            sendOptionsDialogue("Player Options: "+target.getDisplayName()+"",
                    "<col=ff0000>Ban Permanently</col>", 
                    "<col=ff0000>Mute 24 Hours</col>",
                    "<col=ff0000>Force Logout</col>", 
                    "<col=ff0000>Jail 24 Hours</col>",
                    "<col=ff0000>Warn</col>");
            stage = 2;
        }
    }

    @Override
    public void run(int interfaceId, int componentId) {
        if (stage == 2) {
            if (componentId == OPTION_1) {
                ban();
                end();
            }
            if (componentId == OPTION_2) {
                mute();
                end();
            }
            if (componentId == OPTION_3) {
                kick();
                end();
            }
            if (componentId == OPTION_4) {
                jail();
                end();
            }
            if (componentId == OPTION_5) {
            	warn();
                end();
            }
        }
    }

    public void ban() {
        Player target = World.getPlayers().get(playerIndex);
        if (target.getRights() == 2) {
            player.sm("You can't ban an administrator.");
            target.sm(""+player.getDisplayName()+" has attempted to ban you.");
            return;
        }
        
        SerializableFilesManager.savePlayer(target);
        target.setPermBanned(true);
        target.forceLogout();
        player.getPackets().sendGameMessage("You have banned " + target.getDisplayName()+".");
        World.sendWorldMessage("<col=ff0000><img=7>News: " + target.getDisplayName() + " has been banned by "+player.getDisplayName()+"", true);
    }
    
    public void mute() {
        Player target = World.getPlayers().get(playerIndex);
        if (target.getRights() == 2) {
            player.sm("You can't mute an administrator.");
            target.sm(""+player.getDisplayName()+" has attempted to mute you.");
            return;
        }
        player.getPackets().sendGameMessage("You have muted " + target.getDisplayName()+" for 24 hours.");
        target.setMuted(Utils.currentTimeMillis() + 1440000);
        target.getPackets().sendGameMessage("You have been muted for 24 hours by "+player.getDisplayName()+".");
        World.sendWorldMessage("<col=ff0000><img=5>News: " + target.getDisplayName() + " has been muted for 24 hours by "+player.getDisplayName()+"", true);
    }
    
    public void kick() {
        Player target = World.getPlayers().get(playerIndex);
        target.forceLogout();
        player.getPackets().sendGameMessage("You have kicked: " + target.getDisplayName() + ".");
    }
    
    public void jail() {
        Player target = World.getPlayers().get(playerIndex);
        if (target.getRights() == 2) {
            player.sm("You can't jail an administrator.");
            target.sm(""+player.getDisplayName()+" has attempted to jail you.");
            return;
        }
        target.setJailed(Utils.currentTimeMillis() + 1440000);
        target.getControlerManager().startControler("JailControler");
        target.getPackets().sendGameMessage("You've been Jailed for 24 hours by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
        player.getPackets().sendGameMessage("You have Jailed 24 hours: " + target.getDisplayName() + ".");
        SerializableFilesManager.savePlayer(target);
    }
    
    public void warn() {
    	Player other = World.getPlayers().get(playerIndex);
		other.blackMark++;
		player.warnLog2(player, other.getDisplayName());
			player.getPackets().sendGameMessage("You have warned " +other.getDisplayName()+ ". They now have " + other.blackMark+ " black marks.");
			other.getPackets().sendGameMessage("You have recieved a black mark from "+player.getDisplayName()+ ". You now have "+other.blackMark+ " black marks.");
			other.setNextForceTalk(new ForceTalk("I have been warned. I am now on "+other.blackMark+" black marks."));
		if (other.blackMark >= 3) {
			player.setNextForceTalk(new ForceTalk(other.getDisplayName()+ " has been warned 3 times and has been banned for 48 hours."));
			player.getPackets().sendGameMessage("You have warned: " +other.getDisplayName()+ " they are now on: " + other.blackMark);
			other.setBanned(Utils.currentTimeMillis()
					+ (48 * 60 * 60 * 1000));
			other.getSession().getChannel().close();
		}
    }
    
    @Override
    public void finish() {
        // TODO Auto-generated method stub
        
    }

}