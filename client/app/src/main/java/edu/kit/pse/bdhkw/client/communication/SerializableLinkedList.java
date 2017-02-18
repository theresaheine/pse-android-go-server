package edu.kit.pse.bdhkw.client.communication;

/**
 * Wrapper class for LinkedList<GpsObject>.
 * @author Tarek Wilkening
 *
 * @param <GpsObject>
 */
public class SerializableLinkedList<T> extends java.util.LinkedList<T> implements Serializable {

	private static final long serialVersionUID = -6129277573659503021L;
}