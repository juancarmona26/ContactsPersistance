package co.mobilemakers.contacts;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class Contact {

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

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Nickname: " + getFirstName() + " Last Name: " + getLastName() + "imageURI: " + getImageUrl();
    }
}
