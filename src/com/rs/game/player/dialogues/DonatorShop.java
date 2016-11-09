package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class DonatorShop extends Dialogue {

        private int npcId;

        @Override
        public void start() {
            sendEntityDialogue(SEND_2_TEXT_CHAT,
                    new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
                                    "Hello, Would you like to see the donator shop? To check how many donator points you have use the command ::checkpoints"}, IS_NPC, npcId, 9827);
        }
 
 
        @Override
        public void run(int interfaceId, int componentId) {
            if (stage == -1) {
                sendOptionsDialogue("Please select an item.", "Red Partyhat (10)", "Blue Partyhat (10)", "Yellow Partyhat (10)", "Green Partyhat (10)", "Next Page");
                stage = 1;
        } else if (stage == 1) {
                if (componentId == OPTION_1) {
                	if (player.donatorPoints >= 20) {
                        player.getInventory().addItem(1038, 1);
                        player.donatorPoints -=10;
                                end();
                        } else
                        	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                        end();
                } else if (componentId == OPTION_2) {
                	if (player.donatorPoints >= 20) {
                        player.getInventory().addItem(1042, 1);
                        player.donatorPoints -=10;
                                end();
                        } else
                        	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                        end();
                } else if (componentId == OPTION_3) {
                	if (player.donatorPoints >= 20) {
                        player.getInventory().addItem(1040, 1);
                        player.donatorPoints -=10;
                                end();
                        } else
                        	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                        end();
                } else if (componentId == OPTION_4) {
                	if (player.donatorPoints >= 20) {
                        player.getInventory().addItem(1040, 1);
                        player.donatorPoints -=10;
                                end();
                        } else
                        	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                        end();
                } else if (componentId == OPTION_5) {
                        stage = 2;
                        	sendOptionsDialogue("Please select an item.", "White Partyhat (10)", "Purple Partyhat (10)", "Santa Hat (10)", "Dragon Claws (3)", "Next Page");
                }
                    } else if (stage == 2) {
                            if (componentId == OPTION_1) {
                            	if (player.donatorPoints >= 20) {
                                    player.getInventory().addItem(1048, 1);
                                    player.donatorPoints -=10;
                                            end();
                                    } else
                                    	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                    end();
                            } else if (componentId == OPTION_2) {
                            	if (player.donatorPoints >= 20) {
                                    player.getInventory().addItem(1046, 1);
                                    player.donatorPoints -=10;
                                            end();
                                    } else
                                    	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                    end();
                            } else if (componentId == OPTION_3) {
                            	if (player.donatorPoints >= 15) {
                                    player.getInventory().addItem(1050, 1);
                                    player.donatorPoints -=10;
                                            end();
                                    } else
                                    	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                    end();
                            } else if (componentId == OPTION_4) {
                            	if (player.donatorPoints >= 10) {
                                    player.getInventory().addItem(14484, 1);
                                    player.donatorPoints -=3;
                                            end();
                                    } else
                                    	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                    end();
                            } else if (componentId == OPTION_5) {
                                    stage = 3;
                                    	sendOptionsDialogue("Please select an item.", "Torva Helm (2)", "Torva Platebody (4)", "Torva Platelegs (4)", "Zaryte Bow (4)", "Next Page");
                            }
                                } else if (stage == 3) {
                                        if (componentId == OPTION_1) {
                                        	if (player.donatorPoints >= 10) {
                                                player.getInventory().addItem(20135, 1);
                                                player.donatorPoints -=2;
                                                        end();
                                                } else
                                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                end();
                                        } else if (componentId == OPTION_2) {
                                        	if (player.donatorPoints >= 10) {
                                                player.getInventory().addItem(20139, 1);
                                                player.donatorPoints -=4;
                                                        end();
                                                } else
                                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                end();
                                        } else if (componentId == OPTION_3) {
                                        	if (player.donatorPoints >= 10) {
                                                player.getInventory().addItem(20143, 1);
                                                player.donatorPoints -=4;
                                                        end();
                                                } else
                                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                end();
                                        } else if (componentId == OPTION_4) {
                                        	if (player.donatorPoints >= 10) {
                                                player.getInventory().addItem(20171, 1);
                                                player.donatorPoints -=4;
                                                        end();
                                                } else
                                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                end();
                                        } else if (componentId == OPTION_5) {
                                            stage = 4;
                                        	sendOptionsDialogue("Please select an item.", "Pernix Coif (2)", "Pernix Body (4)", "Pernix Chaps (4)", "Armadyl Godsword (3)", "Next Page");
                                }
                                } else if (stage == 4) {
                                    if (componentId == OPTION_1) {
                                    	if (player.donatorPoints >= 15) {
                                            player.getInventory().addItem(20147, 1);
                                            player.donatorPoints -=2;
                                                    end();
                                            } else
                                            	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                            end();
                                    } else if (componentId == OPTION_2) {
                                    	if (player.donatorPoints >= 10) {
                                            player.getInventory().addItem(20151, 1);
                                            player.donatorPoints -=4;
                                                    end();
                                            } else
                                            	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                            end();
                                    } else if (componentId == OPTION_3) {
                                    	if (player.donatorPoints >= 10) {
                                            player.getInventory().addItem(20155, 1);
                                            player.donatorPoints -=4;
                                                    end();
                                            } else
                                            	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                            end();
                                    } else if (componentId == OPTION_4) {
                                    	if (player.donatorPoints >= 9) {
                                            player.getInventory().addItem(11694, 1);
                                            player.donatorPoints -=3;
                                                    end();
                                            } else
                                            	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                            end();
                                    } else if (componentId == OPTION_5) {
                                    	stage = 5;
                                    	sendOptionsDialogue("Please select an item.", "Virtus Mask (2)", "Virtus Robe Top (4)", "Virtus Robe Legs (4)", "Flaming Skull (3)", "Next Page");
                                    }
                                    } else if (stage == 5) {
                                        if (componentId == OPTION_1) {
                                        	if (player.donatorPoints >= 2) {
                                                player.getInventory().addItem(20159, 1);
                                                player.donatorPoints -=2;
                                                        end();
                                                } else
                                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                end();
                                        } else if (componentId == OPTION_2) {
                                        	if (player.donatorPoints >= 4) {
                                                player.getInventory().addItem(20163, 1);
                                                player.donatorPoints -=4;
                                                        end();
                                                } else
                                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                end();
                                        } else if (componentId == OPTION_3) {
                                        	if (player.donatorPoints >= 4) {
                                                player.getInventory().addItem(20167, 1);
                                                player.donatorPoints -=4;
                                                        end();
                                                } else
                                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                end();
                                        } else if (componentId == OPTION_4) {
                                        	if (player.donatorPoints >= 3) {
                                                player.getInventory().addItem(24437, 1);
                                                player.donatorPoints -=3;
                                                        end();
                                                } else
                                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                end();
                                        } else if (componentId == OPTION_5) {
                                        	stage = 6;
                                        	sendOptionsDialogue("Please select an item.", "Arcane Spirit (4)", "Divine Spirit (7)", "Elysian Spirit (4)", "Spectral Spirit (4)", "Close Page");
                                        }
                                        } else if (stage == 6) {
                                            if (componentId == OPTION_1) {
                                            	if (player.donatorPoints >= 4) {
                                                    player.getInventory().addItem(13738, 1);
                                                    player.donatorPoints -=4;
                                                            end();
                                                    } else
                                                    	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                    end();
                                            } else if (componentId == OPTION_2) {
                                            	if (player.donatorPoints >= 7) {
                                                    player.getInventory().addItem(13740, 1);
                                                    player.donatorPoints -=7;
                                                            end();
                                                    } else
                                                    	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                    end();
                                            } else if (componentId == OPTION_3) {
                                            	if (player.donatorPoints >= 3) {
                                                    player.getInventory().addItem(13742, 1);
                                                    player.donatorPoints -=4;
                                                            end();
                                                    } else
                                                    	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                    end();
                                            } else if (componentId == OPTION_4) {
                                            	if (player.donatorPoints >= 3) {
                                                    player.getInventory().addItem(13744, 1);
                                                    player.donatorPoints -=4;
                                                            end();
                                                    } else
                                                    	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                                    end();
                                            } else if (componentId == OPTION_5) {
                                            	end();
                }
        }
        }


                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	/*stage = 2;
                	sendOptionsDialogue("Please select an item.", "White Partyhat (5)", "Purple Partyhat (5)", "Santa Hat (5)", "Dragon Claws (3)", "Next Page");
                } else if (stage == 2) {
                        if (componentId == OPTION_1) {
                        	if (player.donatorPoints >= 5) {
                                player.getInventory().addItem(1048, 1);
                                player.donatorPoints -=5;
                                        end();
                                } else
                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                end();
                        } else if (componentId == OPTION_2) {
                        	if (player.donatorPoints >= 5) {
                                player.getInventory().addItem(1046, 1);
                                player.donatorPoints -=5;
                                        end();
                                } else
                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                end();
                        } else if (componentId == OPTION_3) {
                        	if (player.donatorPoints >= 5) {
                                player.getInventory().addItem(1050, 1);
                                player.donatorPoints -=5;
                                        end();
                                } else
                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                end();
                        } else if (componentId == OPTION_4) {
                        	if (player.donatorPoints >= 3) {
                                player.getInventory().addItem(14484, 1);
                                player.donatorPoints -=3;
                                        end();
                                } else
                                	player.getPackets().sendGameMessage("You dont not have enough donator points to buy this, you currently have: " +player.donatorPoints);
                                end();
                        } else if (componentId == OPTION_5) {
                        	stage = 3;
                        } else if (stage == 3) {
                        	sendNPCDialogue(npcId, 9827, "Here is the next page of items");
                        	stage = 4;
                        	//sendOptionsDialogue("Please select an item.", "Torva Helm (1)", "Torva Platebody (2)", "Torva Platelegs (3)", "Zaryte Bow (4)", "Next Page");
                        }
                }
        }
        }*/
        						

                				
                        
                			
                			
                        
                        


        @Override
        public void finish() {

        }
}