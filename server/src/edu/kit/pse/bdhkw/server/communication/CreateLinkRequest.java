package edu.kit.pse.bdhkw.server.communication;

import com.fasterxml.jackson.annotation.JsonTypeName;

import edu.kit.pse.bdhkw.common.model.Link;
import edu.kit.pse.bdhkw.common.model.SimpleUser;
import edu.kit.pse.bdhkw.server.controller.ResourceManager;
import edu.kit.pse.bdhkw.server.model.GroupServer;

@JsonTypeName("CreateLinkRequest_class")
public class CreateLinkRequest extends GroupRequest {
	
	public CreateLinkRequest() {
		// TODO Auto-generated constructor stub
	}

	public CreateLinkRequest(String senderDeviceId) {
		super(senderDeviceId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response execute() {
		// Get the user who sent the request
		SimpleUser user = ResourceManager.getUser(getSenderDeviceId());
		
		// Get the target group
		GroupServer group = ResourceManager.getGroup(getTargetGroupName());
		
		// Prepare response
		Response response;
		
		// Check if user is allowed to perform the operation
		if (group.getMember(user).isAdmin()) {
			
			// Create the invite link for the group
			Link link = group.createInviteLink();
			
			// Send the link in response
			response = new ObjectResponse(true);
			((ObjectResponse) response).addObject("invite_link", link);
			
			// Never forget !!
			ResourceManager.returnGroup(group);
		} else {
			response = new Response(false);
		}
		return response;
	}

}
