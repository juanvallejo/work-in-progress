package com.rs.game.player.dialogues;
 
 
public class Panel extends Dialogue {
 
public int ID1 = 22417;
public int Amount1 = 1;
public int ID2 = 1722;
public int Amount2 = 1;
 
    @Override
    public void start() {
        if (player.getRights() >= 1) {
            Options2("Staff Control Panel", "Commands Panel", "Punishment Panel");
            player.getPackets().sendItemOnIComponent(1185, 4, ID1, Amount1);
            player.getPackets().sendItemOnIComponent(1185, 5, ID2, Amount2);
            stage = 2;
    } else {
            sendDialogue("<col=FF0000>You must be staff in-order to use this");
             
     
    }
}
 
    @Override
    public void run(int interfaceId, int componentId) {
          if (stage == 2) {
              switch (componentId) {
              case OPTION1:
                  player.getPackets().sendGameMessage("Not ready yet :) (will remove all commands from the server)");
                end();
                break;
              case OPTION2:
                player.getDialogueManager().startDialogue("Punish");
                break;
              }
          }
 
    }
 
    @Override
    public void finish() {
 
    }
}