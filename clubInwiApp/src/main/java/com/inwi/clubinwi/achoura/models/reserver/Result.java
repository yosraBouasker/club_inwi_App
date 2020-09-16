
package com.inwi.clubinwi.achoura.models.reserver;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Result {

    @Expose
    private String address;
    @Expose
    private String avatar;
    @Expose
    private String birthdate;
    @Expose
    private Long cadeaux;
    @Expose
    private String city;
    @Expose
    private String cni;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("email_address")
    private String emailAddress;
    @Expose
    private Filleuls filleuls;
    @SerializedName("first_name")
    private String firstName;
    @Expose
    private Forfaits forfaits;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("last_name")
    private String lastName;
    @Expose
    private Level level;
    @Expose
    private List<Object> parrain;
    @Expose
    private String points;
    @SerializedName("push_status")
    private Boolean pushStatus;
    @Expose
    private String telephone;
    @Expose
    private String token;
    @Expose
    private String zipcode;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Long getCadeaux() {
        return cadeaux;
    }

    public void setCadeaux(Long cadeaux) {
        this.cadeaux = cadeaux;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Filleuls getFilleuls() {
        return filleuls;
    }

    public void setFilleuls(Filleuls filleuls) {
        this.filleuls = filleuls;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Forfaits getForfaits() {
        return forfaits;
    }

    public void setForfaits(Forfaits forfaits) {
        this.forfaits = forfaits;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<Object> getParrain() {
        return parrain;
    }

    public void setParrain(List<Object> parrain) {
        this.parrain = parrain;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public Boolean getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Boolean pushStatus) {
        this.pushStatus = pushStatus;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}
