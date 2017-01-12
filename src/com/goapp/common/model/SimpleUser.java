package com.goapp.common.model;

public class SimpleUser implements UserComponent {
	private String name;
	private int id;
	private String deviceId;
	
	public SimpleUser(String deviceId, String name, int id) {
		this.deviceId = deviceId;
		this.name = name;
		this.id = id;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public String getDeviceId() {
		return this.deviceId;
	}
	
	public void renameUser(String newName) {
		this.name = newName;
	}

}
