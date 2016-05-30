package com.example.vivekgoel.mapdemo.navigationdrawer.history;

public class CustomList {
	private int IconID;
	private String address;
	private String slotno;
	
	public CustomList(int IconID, String address, String slotno) {
		super();
		this.address = address;
	}

	public int getIconID() {
		return IconID;
	}

	public String getAddress() {
		return address;
	}

	public String getSlotno() {
		return slotno;
	}
}
