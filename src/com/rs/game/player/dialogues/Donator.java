package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.minigames.CastleWars;
import com.rs.game.player.Skills;
import com.rs.game.player.content.Magic;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.FightKiln;

public class Donator extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		if (Settings.ECONOMY) {
			player.getPackets().sendGameMessage("Mr.Ex is in no mood to talk to you.");
			end();
			return;
		}
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello, I can teleport you all around Ridiculous-PK,",
						" would you like to?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Sure, why not." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendOptionsDialogue("Please select an item.", "Red Partyhat", "Blue Partyhat", "Yellow Partyhat", "Green Partyhat", "Next Page");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == OPTION_1)
                if (componentId == OPTION_1) {
                	if (player.donatorPoints >= 5) {
                        player.getInventory().addItem(1038, 1);
                        player.donatorPoints -=5;
                	} else {
                		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                	}
                	end();	
                        }
			else if (componentId == OPTION_2)
            	if (player.donatorPoints >= 5) {
                    player.getInventory().addItem(1042, 1);
                    player.donatorPoints -=5;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                    }
            	end();
            	}
			else if (componentId == OPTION_3) {
            	if (player.donatorPoints >= 5) {
                    player.getInventory().addItem(1040, 1);
                    player.donatorPoints -=5;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
            	}
            	end();	
                    }
			else if (componentId == OPTION_4) {
            	if (player.donatorPoints >= 5) {
                    player.getInventory().addItem(1044, 1);
                    player.donatorPoints -=5;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
            	}
            	end();	
                    }
			else if (componentId == OPTION_5) {
				stage = 3;
				sendOptionsDialogue("Please select an item.", "White Partyhat", "Purple Partyhat", "Santa Hat", "Korasi Sword", "Next Page");
			}
		 else if (stage == 3) {
			if (componentId == OPTION_1) {
            	if (player.donatorPoints >= 5) {
                    player.getInventory().addItem(1048, 1);
                    player.donatorPoints -=5;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
            	}
            	end();	
                    }
			} else if (componentId == OPTION_2) {
            	if (player.donatorPoints >= 5) {
                    player.getInventory().addItem(1046, 1);
                    player.donatorPoints -=5;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                    }
            	end();
            	}
			else if (componentId == OPTION_3) {
            	if (player.donatorPoints >= 5) {
                    player.getInventory().addItem(1050, 1);
                    player.donatorPoints -=5;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                    }
            	end();
            	}
			else if (componentId == OPTION_4) {
            	if (player.donatorPoints >= 2) {
                    player.getInventory().addItem(19784, 1);
                    player.donatorPoints -=2;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                    }
            	end();
            	}
			else if (componentId == OPTION_5) {
				stage = 4;
				sendOptionsDialogue("Please select an item.", "Torva Helm", "Torva Platebody", "Torva Platelegs", "Dragon Claws", "Next Page");
			}
		 else if (stage == 4) {
			if (componentId == OPTION_1) {
             	if (player.donatorPoints >= 1) {
                    player.getInventory().addItem(20135, 1);
                    player.donatorPoints -=1;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
            	}
            	end();	
                    }
			} else if (componentId == OPTION_2) {
             	if (player.donatorPoints >= 2) {
                    player.getInventory().addItem(20139, 1);
                    player.donatorPoints -=2;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                    }
            	end();
            	}/*
			} else if (componentId == OPTION_3) {
             	if (player.donatorPoints >= 2) {
                    player.getInventory().addItem(20143, 1);
                    player.donatorPoints -=2;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
            	}
            	end();	
                    }
			else if (componentId == OPTION_4) {
             	if (player.donatorPoints >= 3) {
                    player.getInventory().addItem(14484, 1);
                    player.donatorPoints -=3;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
            	}
            	end();	
                    }
				player.getControlerManager().startControler("Wilderness");
			} else if (componentId == OPTION_5) {
				stage = 5;
				sendOptionsDialogue("Please select an item.", "Divine Spirit", "Arcane Spirit", "Elysian Spirit", "Spectral Spirit", "Back to page one");
			}
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
             	if (player.donatorPoints >= 5) {
                    player.getInventory().addItem(13740, 1);
                    player.donatorPoints -=5;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
            	}
            	end();	
                    }
			} else if (componentId == OPTION_2) {
             	if (player.donatorPoints >= 2) {
                    player.getInventory().addItem(13738, 1);
                    player.donatorPoints -=2;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                    }
            	end();
            	}
			else if (componentId == OPTION_3) {
             	if (player.donatorPoints >= 2) {
                    player.getInventory().addItem(13742, 1);
                    player.donatorPoints -=2;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                    }
            	end();
            	}
            else if (componentId == OPTION_4) {
             	if (player.donatorPoints >= 2) {
                    player.getInventory().addItem(13744, 1);
                    player.donatorPoints -=2;
            	} else {
            		player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                    }
            	end();
            	}
			} else if (componentId == OPTION_5) {
				stage = 6;
				sendOptionsDialogue("Please select an item.", "Comming Soon", "Comming Soon", "Comming Soon", "Comming Soon", "Next Page");
			}
		} else if (stage == 6) {
			if (componentId == OPTION_1)
				teleportPlayer(2925, 5330, 2);
			else if (componentId == OPTION_2)
				teleportPlayer(2838, 5297, 2);
			else if (componentId == OPTION_3)
				Magic.sendNormalTeleportSpell(player, 0, 0, CastleWars.LOBBY);
			else if (componentId == OPTION_4)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2647, 9378, 0));
			else if (componentId == OPTION_5) {
				sendOptionsDialogue("Where would you like to go?",
						"Kalphite Queen", "Fight Caves", "Fight Kiln", "Queen Black Dragon",
						"More Options");
				stage = 7;
			}
		} else if (stage == 7) {
			if (componentId == OPTION_1) 
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3226, 3108, 0));
			else if (componentId == OPTION_2)
				Magic.sendNormalTeleportSpell(player, 0, 0, FightCaves.OUTSIDE);
			else if (componentId == OPTION_3)
				Magic.sendNormalTeleportSpell(player, 0, 0, FightKiln.OUTSIDE);
			else if (componentId == OPTION_4) {
				end();
				if (player.getSkills().getLevelForXp(Skills.SUMMONING) < 60) {
					player.getPackets().sendGameMessage("You need a summoning level of 60 to go through this portal.");
					return;
				}
				player.getControlerManager().startControler("QueenBlackDragonControler");
			}
			else if (componentId == 2)
				teleportPlayer(2838, 5297, 2);
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, CastleWars.LOBBY);
			else if (componentId == 4)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2647, 9378, 0));
			else if (componentId == OPTION_5) {
				sendOptionsDialogue("Where would you like to go?",
						"Nex.", "Bandos.", "Sara.", "Tormented Demons", 
						"More Options");
				stage = 2;
			}
		}*/
	}

	private void teleportPlayer(int x, int y, int z) {
		player.setNextWorldTile(new WorldTile(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("GodWars");
	}

	@Override
	public void finish() {

	}
}
