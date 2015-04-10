package comp313.g2.smartgrocery.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements Parcelable {
	//private fields
	public int Id=-1;
	public String From="", Subject="", Text="", Date="",Username="";
	public boolean isRead = false;
	
	public Notification(Parcel in) {
        Id = in.readInt();
        Date = in.readString();
        From = in.readString();
        Subject =in.readString();
        Text = in.readString();
        Username =in.readString();
        isRead = in.readInt()==1?true:false;
	}
	
	public Notification(){
		
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(Id);
		arg0.writeString(Date);
		arg0.writeString(From);
		arg0.writeString(Subject);
		arg0.writeString(Text);
		arg0.writeString(Username);
		arg0.writeInt(isRead?1:0);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Notification createFromParcel(Parcel in) {
            return new Notification(in); 
        }

        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
