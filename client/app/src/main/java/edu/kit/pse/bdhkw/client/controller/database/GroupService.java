package edu.kit.pse.bdhkw.client.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import edu.kit.pse.bdhkw.client.model.database.DBHelperGroup;
import edu.kit.pse.bdhkw.client.model.database.FeedReaderContract;
import edu.kit.pse.bdhkw.client.model.objectStructure.GroupClient;

import java.util.List;

/**
 * Created by Theresa on 11.01.2017.
 *
 * Gruppe hinzufügen
 * Gruppe löschen
 * Gruppe aktualisieren
 * alle informationen zu einer Gruppe abrufen
 *
 */

public class GroupService {

    private final DBHelperGroup dbHelperGroup;
    private SQLiteDatabase db;

    public GroupService(Context context) {
        dbHelperGroup = new DBHelperGroup(context.getApplicationContext());
    }

    /**
     * Add a new groupClient to groupClient.db database
     * @param groupClient to add to database
     * @return return true if inserting was successful
     */
    public void insertNewGroup(GroupClient groupClient) {
        db = dbHelperGroup.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.FeedEntryGroup.COL_GROUP_NAME, groupClient.getGroupName());
            values.put(FeedReaderContract.FeedEntryGroup.COL_GO_STATUS, groupClient.getGoStatus()
                    .toString()); //ÄH DAS IST KEIN TRUE ODER FALSE
            values.put(FeedReaderContract.FeedEntryGroup.COL_APPOINTMENT_DATE, groupClient
                    .getAppointment().getAppointmentDate().getDate().toString());
            values.put(FeedReaderContract.FeedEntryGroup.COL_APPOINTMENT_TIME, groupClient
                    .getAppointment().getAppointmentDate().getTime().toString());
            values.put(FeedReaderContract.FeedEntryGroup.COL_APPOINTMENT_DEST, groupClient
                    .getAppointment().getAppointmentDestination().getDestinationName());
            values.put(FeedReaderContract.FeedEntryGroup.COL_APPOINTMENT_DEST, groupClient
                    .getAppointment().getAppointmentDestination().getDestinationPosition().getLatitude());
            values.put(FeedReaderContract.FeedEntryGroup.COL_APPOINTMENT_DEST, groupClient
                    .getAppointment().getAppointmentDestination().getDestinationPosition().getLongitude());

            long newRow = db.insert(FeedReaderContract.FeedEntryGroup.TABLE_NAME, null, values);
        } finally {
            db.close();
        }
    }

    /**
     * Get name and go service of the group with the given group id.
     * @param groupName of the group to get information about
     * @return group object
     */
    public GroupClient readGroupData(String groupName) {
        //TODO
        db = dbHelperGroup.getReadableDatabase();
        return null;
    }

    /**
     * To get all groups the actual user is member of and because all the groups saved in the database
     * are just the ones he is member of, we can go through the list and return all group names
     * @return list of all names that are listed in the group database.
     */
    public List<String> readAllGroupNames() {
        //TODO
        db = dbHelperGroup.getReadableDatabase();
        List<String> res = null;
        return res;
    }

    public List<Integer> readAllGroupIds() {
        return null;
    }

    /**
     * Delete a group in group.db.
     * @param groupName of the group to delete
     * @return true if deletion was successful
     */
    public boolean deleteGroupData(String groupName) {
        //TODO
        return false;
    }

    /**
     * Update data when groupClient name or go service of the groupClient have changed.
     * @param groupClient to update name or go service
     * @return true if update was successful
     */
    public boolean updateGroupData(GroupClient groupClient) {
        //TODO
        db = dbHelperGroup.getWritableDatabase();
        return false;
    }

    public void deleteAllGroups() {
    }
}