package com.rs.game.player.dialogues;

import java.util.HashMap;

import com.rs.utils.Logger;

public final class DialogueHandler {

	private static final HashMap<Object, Class<Dialogue>> handledDialogues = new HashMap<Object, Class<Dialogue>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			Class<Dialogue> value1 = (Class<Dialogue>) Class
					.forName(LevelUp.class.getCanonicalName());
			handledDialogues.put("LevelUp", value1);
			Class<Dialogue> value2 = (Class<Dialogue>) Class
					.forName(ZarosAltar.class.getCanonicalName());
			handledDialogues.put("ZarosAltar", value2);
			Class<Dialogue> value3 = (Class<Dialogue>) Class
					.forName(ClimbNoEmoteStairs.class.getCanonicalName());
			handledDialogues.put("ClimbNoEmoteStairs", value3);
			Class<Dialogue> value4 = (Class<Dialogue>) Class
					.forName(Banker.class.getCanonicalName());
			handledDialogues.put("Banker", value4);
			Class<Dialogue> value5 = (Class<Dialogue>) Class
					.forName(DestroyItemOption.class.getCanonicalName());
			handledDialogues.put("DestroyItemOption", value5);
			Class<Dialogue> value6 = (Class<Dialogue>) Class
					.forName(FremennikShipmaster.class.getCanonicalName());
			handledDialogues.put("FremennikShipmaster", value6);
			Class<Dialogue> value8 = (Class<Dialogue>) Class
					.forName(NexEntrance.class.getCanonicalName());
			handledDialogues.put("NexEntrance", value8);
			Class<Dialogue> value9 = (Class<Dialogue>) Class
					.forName(MagicPortal.class.getCanonicalName());
			handledDialogues.put("MagicPortal", value9);
			Class<Dialogue> value10 = (Class<Dialogue>) Class
					.forName(LunarAltar.class.getCanonicalName());
			handledDialogues.put("LunarAltar", value10);
			Class<Dialogue> value11 = (Class<Dialogue>) Class
					.forName(AncientAltar.class.getCanonicalName());
			handledDialogues.put("AncientAltar", value11);
			// TODO 12 and 13
			Class<Dialogue> value12 = (Class<Dialogue>) Class
					.forName(FletchingD.class.getCanonicalName());
			handledDialogues.put("FletchingD", value12);
			Class<Dialogue> value14 = (Class<Dialogue>) Class
					.forName(RuneScapeGuide.class.getCanonicalName());
			handledDialogues.put("RuneScapeGuide", value14);
			Class<Dialogue> value15 = (Class<Dialogue>) Class
					.forName(SurvivalExpert.class.getCanonicalName());
			handledDialogues.put("SurvivalExpert", value15);
			Class<Dialogue> value16 = (Class<Dialogue>) Class
					.forName(SimpleMessage.class.getCanonicalName());
			handledDialogues.put("SimpleMessage", value16);
			Class<Dialogue> value17 = (Class<Dialogue>) Class
					.forName(ItemMessage.class.getCanonicalName());
			handledDialogues.put("ItemMessage", value17);
			Class<Dialogue> value18 = (Class<Dialogue>) Class
					.forName(ClimbEmoteStairs.class.getCanonicalName());
			handledDialogues.put("ClimbEmoteStairs", value18);
			Class<Dialogue> value20 = (Class<Dialogue>) Class
					.forName(GemCuttingD.class.getCanonicalName());
			handledDialogues.put("GemCuttingD", value20);
			Class<Dialogue> value21 = (Class<Dialogue>) Class
					.forName(CookingD.class.getCanonicalName());
			handledDialogues.put("CookingD", value21);
			Class<Dialogue> value22 = (Class<Dialogue>) Class
					.forName(HerbloreD.class.getCanonicalName());
			handledDialogues.put("HerbloreD", value22);
			Class<Dialogue> value23 = (Class<Dialogue>) Class
					.forName(BarrowsD.class.getCanonicalName());
			handledDialogues.put("BarrowsD", value23);
			Class<Dialogue> value24 = (Class<Dialogue>) Class
					.forName(SmeltingD.class.getCanonicalName());
			handledDialogues.put("SmeltingD", value24);
			Class<Dialogue> value25 = (Class<Dialogue>) Class
					.forName(LeatherCraftingD.class.getCanonicalName());
			handledDialogues.put("LeatherCraftingD", value25);
			Class<Dialogue> value26 = (Class<Dialogue>) Class
					.forName(EnchantedGemDialouge.class.getCanonicalName());
			handledDialogues.put("EnchantedGemDialouge", value26);
			Class<Dialogue> value27 = (Class<Dialogue>) Class
					.forName(ForfeitDialouge.class.getCanonicalName());
			handledDialogues.put("ForfeitDialouge", value27);
			Class<Dialogue> value28 = (Class<Dialogue>) Class
					.forName(Transportation.class.getCanonicalName());
			handledDialogues.put("Transportation", value28);
			Class<Dialogue> value29 = (Class<Dialogue>) Class
					.forName(WildernessDitch.class.getCanonicalName());
			handledDialogues.put("WildernessDitch", value29);
			Class<Dialogue> value30 = (Class<Dialogue>) Class
					.forName(SimpleNPCMessage.class.getCanonicalName());
			handledDialogues.put("SimpleNPCMessage", value30);
			Class<Dialogue> value31 = (Class<Dialogue>) Class
					.forName(Transportation.class.getCanonicalName());
			handledDialogues.put("Transportation", value31);
			Class<Dialogue> value32 = (Class<Dialogue>) Class
					.forName(DTSpectateReq.class.getCanonicalName());
			handledDialogues.put("DTSpectateReq", value32);
			Class<Dialogue> value33 = (Class<Dialogue>) Class
					.forName(StrangeFace.class.getCanonicalName());
			handledDialogues.put("StrangeFace", value33);
			Class<Dialogue> value34 = (Class<Dialogue>) Class
					.forName(AncientEffigiesD.class.getCanonicalName());
			handledDialogues.put("AncientEffigiesD", value34);
			Class<Dialogue> value35 = (Class<Dialogue>) Class
					.forName(DTClaimRewards.class.getCanonicalName());
			handledDialogues.put("DTClaimRewards", value35);
			Class<Dialogue> value36 = (Class<Dialogue>) Class
					.forName(SetSkills.class.getCanonicalName());
			handledDialogues.put("SetSkills", value36);
			Class<Dialogue> value37 = (Class<Dialogue>) Class
					.forName(DismissD.class.getCanonicalName());
			handledDialogues.put("DismissD", value37);
			Class<Dialogue> value38 = (Class<Dialogue>) Class
					.forName(MrEx.class.getCanonicalName());
			handledDialogues.put("MrEx", value38);
			Class<Dialogue> value39 = (Class<Dialogue>) Class
					.forName(MakeOverMage.class.getCanonicalName());
			handledDialogues.put("MakeOverMage", value39);
			Class<Dialogue> value40 = (Class<Dialogue>) Class
					.forName(KaramjaTrip.class.getCanonicalName());
			handledDialogues.put("KaramjaTrip", value40);
			Class<Dialogue> value42 = (Class<Dialogue>) Class
					.forName(DagonHai.class.getCanonicalName());
			handledDialogues.put("DagonHai", value42);
                        Class<Dialogue> value44 = (Class<Dialogue>) Class
                                        .forName(Gambler.class.getCanonicalName());
                        handledDialogues.put("Gambler", value44);
                        Class<Dialogue> value45 = (Class<Dialogue>) Class
                                        .forName(Shamus.class.getCanonicalName());
                        handledDialogues.put("Shamus", value45);
                        Class<Dialogue> value46 = (Class<Dialogue>) Class
                                        .forName(MonkOfEntrana.class.getCanonicalName());
                        handledDialogues.put("MonkOfEntrana", value46);
                        Class<Dialogue> value47 = (Class<Dialogue>) Class
                                        .forName(Warrior.class.getCanonicalName());
                        handledDialogues.put("Warrior", value47);
			Class<Dialogue> value48 = (Class<Dialogue>) Class
					.forName(Max.class.getCanonicalName());
			handledDialogues.put("Max", value48);
			Class<Dialogue> value49 = (Class<Dialogue>) Class
					.forName(BankTest.class.getCanonicalName());
			handledDialogues.put("BankTest", value49);
			Class<Dialogue> value50 = (Class<Dialogue>) Class
					.forName(Panel.class.getCanonicalName());
			handledDialogues.put("Panel", value50);
			Class<Dialogue> value51 = (Class<Dialogue>) Class
					.forName(Punish.class.getCanonicalName());
			handledDialogues.put("Punish", value51);
			Class<Dialogue> value52 = (Class<Dialogue>) Class
					.forName(Class1.class.getCanonicalName());
			handledDialogues.put("Class1", value52);
			Class<Dialogue> value53 = (Class<Dialogue>) Class
					.forName(Charms.class.getCanonicalName());
			handledDialogues.put("Charms", value53);
			Class<Dialogue> value54 = (Class<Dialogue>) Class
					.forName(DonatorShop.class.getCanonicalName());
			handledDialogues.put("DonatorShop", value54);
			Class<Dialogue> value55 = (Class<Dialogue>) Class
					.forName(Cape.class.getCanonicalName());
			handledDialogues.put("Cape", value55);
			Class<Dialogue> value56 = (Class<Dialogue>) Class
					.forName(Xmas.class.getCanonicalName());
			handledDialogues.put("Xmas", value56);
			Class<Dialogue> value57 = (Class<Dialogue>) Class
					.forName(XmasD.class.getCanonicalName());
			handledDialogues.put("XmasD", value57);
			Class<Dialogue> value58 = (Class<Dialogue>) Class
					.forName(XmasV.class.getCanonicalName());
			handledDialogues.put("XmasV", value58);
			Class<Dialogue> value59 = (Class<Dialogue>) Class
					.forName(XmasS.class.getCanonicalName());
			handledDialogues.put("XmasS", value59);
			Class<Dialogue> value60 = (Class<Dialogue>) Class
					.forName(SkillerCape.class.getCanonicalName());
			handledDialogues.put("SkillerCape", value60);
			Class<Dialogue> value61 = (Class<Dialogue>) Class
					.forName(PrestigeOne.class.getCanonicalName());
			handledDialogues.put("PrestigeOne", value61);
			Class<Dialogue> value62 = (Class<Dialogue>) Class
					.forName(FlowerGame.class.getCanonicalName());
			handledDialogues.put("FlowerGame", value62);
			Class<Dialogue> value63 = (Class<Dialogue>) Class
					.forName(PkPortal.class.getCanonicalName());
			handledDialogues.put("PkPortal", value63);
			Class<Dialogue> value64 = (Class<Dialogue>) Class
					.forName(FarmingTeleports.class.getCanonicalName());
			handledDialogues.put("FarmingTeleports", value64);
			Class<Dialogue> value65 = (Class<Dialogue>) Class
					.forName(FarmingTeleport.class.getCanonicalName());
			handledDialogues.put("FarmingTeleport", value65);
			Class<Dialogue> value66 = (Class<Dialogue>) Class
					.forName(StarSprite.class.getCanonicalName());
			handledDialogues.put("StarSprite", value66);
			
			Class<Dialogue> value67 = (Class<Dialogue>) Class
					.forName(Turael.class.getCanonicalName());
			handledDialogues.put("Turael", value67);
			Class<Dialogue> value68 = (Class<Dialogue>) Class
					.forName(HouseTeleport.class.getCanonicalName());
			handledDialogues.put("HouseTeleport", value68);
			
			Class<Dialogue> value69 = (Class<Dialogue>) Class
					.forName(MakeChair.class.getCanonicalName());
			handledDialogues.put("MakeChair", value69);
			Class<Dialogue> value70 = (Class<Dialogue>) Class
					.forName(MakePortal.class.getCanonicalName());
			handledDialogues.put("MakePortal", value70);
			Class<Dialogue> value71 = (Class<Dialogue>) Class
					.forName(LeaveHouse.class.getCanonicalName());
			handledDialogues.put("LeaveHouse", value71);
			Class<Dialogue> value72 = (Class<Dialogue>) Class
					.forName(CreatePortal.class.getCanonicalName());
			handledDialogues.put("CreatePortal", value72);
			Class<Dialogue> value73 = (Class<Dialogue>) Class
					.forName(Butler.class.getCanonicalName());
			handledDialogues.put("Butler", value73);
			Class<Dialogue> value74 = (Class<Dialogue>) Class
					.forName(MakeThrone.class.getCanonicalName());
			handledDialogues.put("MakeThrone", value74);
			Class<Dialogue> value75 = (Class<Dialogue>) Class
					.forName(MakeTrees.class.getCanonicalName());
			handledDialogues.put("MakeTrees", value75);
			Class<Dialogue> value76 = (Class<Dialogue>) Class
					.forName(MakeBook.class.getCanonicalName());
			handledDialogues.put("MakeBook", value76);
			Class<Dialogue> value77 = (Class<Dialogue>) Class
					.forName(MakeFire.class.getCanonicalName());
			handledDialogues.put("MakeFire", value77);
			Class<Dialogue> value78 = (Class<Dialogue>) Class
					.forName(EstateAgent.class.getCanonicalName());
			handledDialogues.put("EstateAgent", value78);
			Class<Dialogue> value79 = (Class<Dialogue>) Class
					.forName(DemonButler.class.getCanonicalName());
			handledDialogues.put("DemonButler", value79);
			Class<Dialogue> value80 = (Class<Dialogue>) Class
					.forName(RoomCreation.class.getCanonicalName());
			handledDialogues.put("RoomCreation", value80);
			Class<Dialogue> value81 = (Class<Dialogue>) Class
					.forName(StatLog.class.getCanonicalName());
			handledDialogues.put("StatLog", value81);
			Class<Dialogue> value82 = (Class<Dialogue>) Class
					.forName(Train.class.getCanonicalName());
			handledDialogues.put("Train", value82);
			Class<Dialogue> value83 = (Class<Dialogue>) Class
					.forName(ModPanel.class.getCanonicalName());
			handledDialogues.put("ModPanel", value83);
			Class<Dialogue> value84 = (Class<Dialogue>) Class
					.forName(Lawgof.class.getCanonicalName());
			handledDialogues.put("Lawgof", value84);
			Class<Dialogue> value85 = (Class<Dialogue>) Class
					.forName(Nulodion.class.getCanonicalName());
			handledDialogues.put("Nulodion", value85);
			Class<Dialogue> value86 = (Class<Dialogue>) Class
					.forName(MakeAltar.class.getCanonicalName());
			handledDialogues.put("MakeAltar", value86);
			Class<Dialogue> value87 = (Class<Dialogue>) Class
					.forName(ChristmasCrackerD.class.getCanonicalName());
			handledDialogues.put("ChristmasCrackerD", value87);
			
			//handledDialogues.put("ChristmasCrackerD", (Class<Dialogue>) Class.forName(PrayerD.class.getCanonicalName()));
			handledDialogues.put("PrayerD", (Class<Dialogue>) Class.forName(PrayerD.class.getCanonicalName()));
			handledDialogues.put("clan_wars_view", (Class<Dialogue>) Class.forName(ClanWarsViewing.class.getCanonicalName()));
			handledDialogues.put("DiceBag", (Class<Dialogue>) Class.forName(DiceBag.class.getCanonicalName()));
			handledDialogues.put("PartyPete", (Class<Dialogue>) Class.forName(PartyPete.class.getCanonicalName()));
			handledDialogues.put("PartyRoomLever", (Class<Dialogue>) Class.forName(PartyRoomLever.class.getCanonicalName()));
			handledDialogues.put("DrogoDwarf", (Class<Dialogue>) Class.forName(DrogoDwarf.class.getCanonicalName()));
			handledDialogues.put("GeneralStore", (Class<Dialogue>) Class.forName(GeneralStore.class.getCanonicalName()));
			handledDialogues.put("Nurmof", (Class<Dialogue>) Class.forName(Nurmof.class.getCanonicalName()));
			handledDialogues.put("BootDwarf", (Class<Dialogue>) Class.forName(BootDwarf.class.getCanonicalName()));
			handledDialogues.put("MiningGuildDwarf", (Class<Dialogue>) Class.forName(MiningGuildDwarf.class.getCanonicalName()));
			handledDialogues.put("Man", (Class<Dialogue>) Class.forName(Man.class.getCanonicalName()));
			handledDialogues.put("Guildmaster", (Class<Dialogue>) Class.forName(Guildmaster.class.getCanonicalName()));
			handledDialogues.put("Scavvo", (Class<Dialogue>) Class.forName(Scavvo.class.getCanonicalName()));	
			handledDialogues.put("Valaine", (Class<Dialogue>) Class.forName(Valaine.class.getCanonicalName()));	
			handledDialogues.put("Hura", (Class<Dialogue>) Class.forName(Hura.class.getCanonicalName()));
			handledDialogues.put("TzHaarMejJal", (Class<Dialogue>) Class.forName(TzHaarMejJal.class.getCanonicalName()));
			handledDialogues.put("TzHaarMejKah", (Class<Dialogue>) Class.forName(TzHaarMejKah.class.getCanonicalName()));
			handledDialogues.put("LanderD", (Class<Dialogue>) Class.forName(LanderDialouge.class.getCanonicalName()));
			handledDialogues.put("MasterOfFear", (Class<Dialogue>) Class.forName(MasterOfFear.class.getCanonicalName()));
			handledDialogues.put("TokHaarHok", (Class<Dialogue>) Class.forName(TokHaarHok.class.getCanonicalName()));
			handledDialogues.put("NomadThrone", (Class<Dialogue>) Class.forName(NomadThrone.class.getCanonicalName()));
			handledDialogues.put("SimplePlayerMessage", (Class<Dialogue>) Class.forName(SimplePlayerMessage.class.getCanonicalName()));
			handledDialogues.put("BonfireD", (Class<Dialogue>) Class.forName(BonfireD.class.getCanonicalName()));
			handledDialogues.put("MasterChef", (Class<Dialogue>) Class.forName(MasterChef.class.getCanonicalName()));
			handledDialogues.put("FightKilnDialogue", (Class<Dialogue>) Class.forName(FightKilnDialogue.class.getCanonicalName()));
			handledDialogues.put("RewardChest", (Class<Dialogue>) Class.forName(RewardChest.class.getCanonicalName()));
			handledDialogues.put("WizardFinix", (Class<Dialogue>) Class.forName(WizardFinix.class.getCanonicalName()));	
			handledDialogues.put("RunespanPortalD", (Class<Dialogue>) Class.forName(RunespanPortalD.class.getCanonicalName()));
			handledDialogues.put("SorceressGardenNPCs", (Class<Dialogue>) Class.forName(SorceressGardenNPCs.class.getCanonicalName()));
			handledDialogues.put("Marv", (Class<Dialogue>) Class.forName(Marv.class.getCanonicalName()));
			handledDialogues.put("FlamingSkull", (Class<Dialogue>) Class.forName(FlamingSkull.class.getCanonicalName()));
			handledDialogues.put("Hairdresser", (Class<Dialogue>) Class.forName(Hairdresser.class.getCanonicalName()));
			handledDialogues.put("Thessalia", (Class<Dialogue>) Class.forName(Thessalia.class.getCanonicalName()));
			handledDialogues.put("GrilleGoatsD", (Class<Dialogue>) Class.forName(GrilleGoatsDialogue.class.getCanonicalName()));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void reload() {
		handledDialogues.clear();
		init();
	}

	public static final Dialogue getDialogue(Object key) {
		if (key instanceof Dialogue)
			return (Dialogue) key;
		Class<Dialogue> classD = handledDialogues.get(key);
		if (classD == null)
			return null;
		try {
			return classD.newInstance();
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}

	private DialogueHandler() {

	}
}
