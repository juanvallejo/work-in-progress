package com.rs.game.player.dialogues;


public class magic extends Dialogue {

public int ID1 = 11694;
public int Amount1 = 1;
public int ID2 = 11700;
public int Amount2 = 1;

	@Override
	public void start() {
		Options2("TzHaar", "Armadyl Godsword", "Zamarok Godsword");
		player.getPackets().sendItemOnIComponent(1185, 4, ID1, Amount1);
		player.getPackets().sendItemOnIComponent(1185, 5, ID2, Amount2);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		  if (stage == -1) {
			  switch (componentId) {
			  case OPTION1:
				player.getInventory().addItem(ID1, Amount1);
				end();
				break;
			  case OPTION2:
				player.getInventory().addItem(ID2, Amount2);
				end();
				break;
			  }
		  }
	}


	@Override
	public void finish() {

	}
}