package edu.kit.pse.bdhkw.client.communication;
import com.fasterxml.jackson.annotation.JsonTypeName;

import edu.kit.pse.bdhkw.common.model.GpsObject;
/**
 * Request to share GPS-Coordinates with a target group.
 * Sharing in this case means to store the coordinates in the database,
 * that way, other group members can request them.
 * @author Tarek Wilkening
 *
 */
@JsonTypeName("BroadcastGpsRequest_class")
public class BroadcastGpsRequest extends GroupRequest {
	private GpsObject coordinates;
	private boolean statusGo;
	
	public boolean isStatusGo() {
		return statusGo;
	}

	public void setStatusGo(boolean statusGo) {
		this.statusGo = statusGo;
	}

	public GpsObject getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(GpsObject coordinates) {
		this.coordinates = coordinates;
	}
	
	/**
	 * This is for serialization within android
	 */
	protected BroadcastGpsRequest(Parcel in) {
		coordinates = in.readParcelable(GpsObject.class.getClassLoader());
		senderDeviceId = in.readString();
		statusGo = in.readBoolean();
	}

	public static final Creator<RegistrationRequest> CREATOR = new Creator<RegistrationRequest>() {
        @Override                       
        public RegistrationRequest createFromParcel(Parcel source) {               
        	return new RegistrationRequest(source);
        }                                                   
                                                                    
        @Override                                                   
        public RegistrationRequest[] newArray(int size) {
             return new RegistrationRequest[0];                               }                                          
   };                                              
                                                                 
   @Override                                     
   public int describeContents() {                        
        return 0;                                                   
   }                                                                
                                                                    
   @Override                                                        
   public void writeToParcel(Parcel dest, int flags) {        
	   dest.writeParcelable(coordinates, flags);
        dest.writeString(senderDeviceId);      
        dest.writeBoolean(statusGo);
   } 
}
