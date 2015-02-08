package co.mobilemakers.contacts;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;
import java.util.List;

public class Contact implements Parcelable {

    public final static String ID = "_ID";
    public final static String FIRST_NAME = "firstName";
    public final static String LAST_NAME = "lastName";
    public final static String NICKNAME = "nickname";
    public final static String IMAGE_URL = "imageURL";
    public final static String CREATION_DATE = "date";

    @DatabaseField(generatedId = true, columnName = ID) private int id;
    @DatabaseField (columnName = FIRST_NAME)private String firstName;
    @DatabaseField (columnName = LAST_NAME) private String lastName;
    @DatabaseField (columnName = NICKNAME) private String nickname;
    @DatabaseField (columnName = IMAGE_URL)private String imageUrl;



    @DatabaseField (columnName = CREATION_DATE) private Date date = new Date();
    public static final Creator<Contact> CREATOR = new Creator<Contact>(){

        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };


    public Contact(Parcel in){

        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        nickname = in.readString();
        imageUrl = in.readString();
        date =(Date) in.readValue(Date.class.getClassLoader());
    }

    public Contact(){

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Nickname: " + getFirstName() + " Last Name: " + getLastName() + "imageURI: " + getImageUrl();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(nickname);
        dest.writeString(imageUrl);
        dest.writeValue(date);
    }
}
