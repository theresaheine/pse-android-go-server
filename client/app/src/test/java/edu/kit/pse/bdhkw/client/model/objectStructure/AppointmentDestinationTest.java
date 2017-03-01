package edu.kit.pse.bdhkw.client.model.objectStructure;

import junit.framework.Assert;

import org.junit.Test;
import org.osmdroid.util.GeoPoint;

/**
 * Tests for AppointmentDestination.
 * @author Theresa Heine
 * @version 1.0
 */
public class AppointmentDestinationTest {

    @Test
    public void testGetDestinationName() {
        AppointmentDestination appointmentDestination = new AppointmentDestination();

        Assert.assertEquals(appointmentDestination.getDestinationName(), "Schloss Karlsruhe");
    }

    @Test
    public void testSetDestinationName() {
        AppointmentDestination appointmentDestination = new AppointmentDestination();
        appointmentDestination.setDestinationName("Mensa am Adenauerring");

        Assert.assertEquals("Mensa am Adenauerring", appointmentDestination.getDestinationName());
    }

    @Test
    public void testSetEmptyDestinationName() {
        AppointmentDestination appointmentDestination = new AppointmentDestination();
        appointmentDestination.setDestinationName("");

        Assert.assertTrue(appointmentDestination.getDestinationName().equals("Schloss Karlsruhe"));
    }

    @Test
    public void testGetDestinationPosition() {
        AppointmentDestination appointmentDestination = new AppointmentDestination();
        Double delta = 0.00001; // Maximum difference between values

        Assert.assertEquals(appointmentDestination.getDestinationPosition().getLatitude(), 49.012941, delta);
        Assert.assertEquals(appointmentDestination.getDestinationPosition().getLongitude(), 8.404409, delta);
    }

    @Test
    public void testSetDestinationPosition() {
        AppointmentDestination appointmentDestination = new AppointmentDestination();
        GeoPoint geoPoint = new GeoPoint(1.999, 34.66);
        appointmentDestination.setDestinationPosition(geoPoint);
        double delta = 0.0001; //Maximum difference between values

        Assert.assertEquals(1.999,appointmentDestination.getDestinationPosition().getLatitude(), delta);
        Assert.assertEquals(34.66, appointmentDestination.getDestinationPosition().getLongitude(), delta);
    }

    @Test
    public void testSetEmptyDestinationPosition() {
        AppointmentDestination appointmentDestination = new AppointmentDestination();
        appointmentDestination.setDestinationPosition(null);

        Assert.assertNotNull(appointmentDestination.getDestinationPosition());
    }
}