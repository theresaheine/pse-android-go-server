package edu.kit.pse.bdhkw.client.model.objectStructure;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Determine the view a user becomes when he is admin of the group.
 * @author Theresa Heine
 * @version 1.0
 */
public class GroupAdminClientTest {

    @Test
    public void getView() throws Exception {
        GroupAdminClient groupAdminClient = new GroupAdminClient("Admin", 123456789);

        Assert.assertEquals(true, groupAdminClient.getView());
    }

}