package co.mobilemakers.contacts;

/**
 * Created by Juan on 05/02/2015.
 */
public class Contact {
    private String firstName;
    private String lastName;
    private String nickname;
    private String imageUrl;

    public Contact(){

    }

    public Contact (String nickname, String imageUrl) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
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

    @Override
    public String toString() {
        String propertiesToString  = "Nickname: " + getFirstName() + " Last Name: " + getLastName() + "imageURI: " + getImageUrl();
        return propertiesToString;
    }
}
