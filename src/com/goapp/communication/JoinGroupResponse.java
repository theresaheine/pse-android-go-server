package com.goapp.communication;

import com.goapp.server.model.GroupServer;

public class JoinGroupResponse extends Response {
	private GroupServer group;
	
	public JoinGroupResponse(boolean success) {
		super(success);
		// TODO Auto-generated constructor stub
	}

	public GroupServer getGroup() {
		return group;
	}

	public void setGroup(GroupServer group) {
		this.group = group;
	}

}
