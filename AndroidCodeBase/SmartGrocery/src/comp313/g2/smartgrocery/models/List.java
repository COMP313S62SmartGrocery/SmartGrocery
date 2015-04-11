package comp313.g2.smartgrocery.models;

import android.os.Parcel;
import android.os.Parcelable;

public class List implements Parcelable {
	public long Id;
	public String Name, Color, LastModified, Username;

	public List(Parcel in) {
		Id = in.readLong();
		Name = in.readString();
		Color = in.readString();
		LastModified = in.readString();
		Username = in.readString();
	}

	public List() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(Id);
		dest.writeString(Name);
		dest.writeString(Color);
		dest.writeString(LastModified);
		dest.writeString(Username);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public List createFromParcel(Parcel in) {
			return new List(in);
		}

		public List[] newArray(int size) {
			return new List[size];
		}
	};

}
