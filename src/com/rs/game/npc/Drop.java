package com.rs.game.npc;

public class Drop {

	private int itemId, minAmount, maxAmount;
	private double rate;
	@SuppressWarnings("unused")
	public boolean rare;
	public static Drop create(int itemId, double rate, int minAmount, int maxAmount, boolean rare) {
	    return new Drop((short) itemId, rate, minAmount, maxAmount, rare);
	    }

	public Drop(int itemId, double rate, int minAmount, int maxAmount,
			boolean rare) {
		this.itemId = itemId;
		this.rate = rate;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.rare = rare;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public int getExtraAmount() {
		return maxAmount - minAmount;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public int getItemId() {
		return itemId;
	}

	public double getRate() {
		return rate;
	}

	public void setMinAmount(int newMinAmount) {
		this.minAmount = newMinAmount;
	}

	public void setMaxAmount(int newMaxAmount) {
		this.maxAmount = newMaxAmount;		
	}

	public boolean isFromRareTable() {
		return false;
	}

	public void setItemId(short newItemId) {
		this.itemId = newItemId;	
	}

	public void setRate(double newRate) {
		this.rate = newRate;		
	}

}
