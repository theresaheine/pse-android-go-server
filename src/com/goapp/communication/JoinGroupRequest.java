package com.goapp.communication;

import com.goapp.model.SimpleUser;
import com.goapp.client.GroupClient;
import com.goapp.model.Link;

public class JoinGroupRequest extends Request {
	private String targetGroup;
	private Link inviteLink;
	
	public JoinGroupRequest(SimpleUser sender) {
		super(sender);
		// TODO Auto-generated constructor stub
	}

	public String getTargetGroup() {
		return targetGroup;
	}

	public void setTargetGroup(GroupClient targetGroup) {
		this.targetGroup = targetGroup.getName();
	}

	public Link getInviteLink() {
		return inviteLink;
	}

	public void setInviteLink(Link inviteLink) {
		this.inviteLink = inviteLink;
	}

}
