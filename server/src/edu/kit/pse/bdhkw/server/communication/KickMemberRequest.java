package edu.kit.pse.bdhkw.server.communication;

import com.fasterxml.jackson.annotation.JsonTypeName;

import edu.kit.pse.bdhkw.common.model.SimpleUser;
import edu.kit.pse.bdhkw.server.controller.ResourceManager;
import edu.kit.pse.bdhkw.server.model.GroupServer;

@JsonTypeName("KickMemberRequest_class")
public class KickMemberRequest extends GroupRequest {
	private int targetMemberId;

	public int getTargetMemberId() {
		return targetMemberId;
	}

	public void setTargetMemberId(int targetMemberId) {
		this.targetMemberId = targetMemberId;
	}

	public KickMemberRequest() {
		// TODO Auto-generated constructor stub
	}

	public KickMemberRequest(String senderDeviceId) {
		super(senderDeviceId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response execute() {
		// Get the user who sent this request
		SimpleUser user = ResourceManager.getUser(getSenderDeviceId());
		
		// Get the target group
		GroupServer group = ResourceManager.getGroup(getTargetGroupName());
		
		// Get the user we want to kick from the group
		SimpleUser targetUser = ResourceManager.getUser(targetMemberId);
		
		if (targetUser == null | group.getMember(targetUser) == null) {
			return new Response(false);
		}
		
		// Prepare response 
		Response response;
		
		// Check if we are administrator of the group
		if (group.getMember(user).isAdmin()) {
			response = new Response(true);
			
			group.removeMember(targetUser);
			
			// NEVER FORGET
			ResourceManager.returnGroup(group);
		} else {
			response = new Response(false);
		}
		return response;
	}

}
