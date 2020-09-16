
package com.inwi.clubinwi.achoura.models.listing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PastCadeau {

    @Expose
    private Boolean canOfferAndBenefit;
    @Expose
    private String categorie;
    @SerializedName("compagnie_id")
    private String compagnieId;
    @Expose
    private String description;
    @Expose
    private Boolean hasSubscribe;
    @Expose
    private String id;
    @Expose
    private String image;
    @Expose
    private Boolean isComming;
    @Expose
    private Boolean isCredential;
    @Expose
    private String niveau;
    @Expose
    private String points;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("stop_date")
    private String stopDate;
    @Expose
    private String summary;
    @Expose
    private String title;

    public Boolean getCanOfferAndBenefit() {
        return canOfferAndBenefit;
    }

    public void setCanOfferAndBenefit(Boolean canOfferAndBenefit) {
        this.canOfferAndBenefit = canOfferAndBenefit;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getCompagnieId() {
        return compagnieId;
    }

    public void setCompagnieId(String compagnieId) {
        this.compagnieId = compagnieId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHasSubscribe() {
        return hasSubscribe;
    }

    public void setHasSubscribe(Boolean hasSubscribe) {
        this.hasSubscribe = hasSubscribe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getIsComming() {
        return isComming;
    }

    public void setIsComming(Boolean isComming) {
        this.isComming = isComming;
    }

    public Boolean getIsCredential() {
        return isCredential;
    }

    public void setIsCredential(Boolean isCredential) {
        this.isCredential = isCredential;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
