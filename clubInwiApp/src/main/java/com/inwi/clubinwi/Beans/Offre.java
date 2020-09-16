package com.inwi.clubinwi.Beans;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Offre implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String summary;
    private String description;
    private String start_date;
    private String stop_date;
    private String image;
    private String next_kdo_date;
    private boolean isComming;
    private boolean isCredential;
    private boolean canOfferAndBenefit;
    private boolean hasSubscribe;
    private boolean hasOffered;
    private String point;
    private String compagnie_id;
    private String categorie;
    private String niveau;

    public Offre() {
        super();
    }

    public Offre(String id, String title, String summary, String description, String start_date, String stop_date, String image, String next_kdo_date, boolean isComming, boolean isCredential, boolean canOfferAndBenefit, boolean hasSubscribe,
                 boolean hasOffered, String point, String compagnie_id, String categorie, String niveau) {
        super();
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.start_date = start_date;
        this.stop_date = stop_date;
        this.image = image;
        this.next_kdo_date = next_kdo_date;
        this.isComming = isComming;
        this.isCredential = isCredential;
        this.canOfferAndBenefit = canOfferAndBenefit;
        this.hasSubscribe = hasSubscribe;
        this.point = point;
        this.compagnie_id = compagnie_id;
        this.categorie = categorie;
        this.niveau = niveau;
    }

    public static Offre parseOffre(JSONObject mResponseObject) {
        try {
            Offre mOffre = new Offre();

            mOffre.setId(mResponseObject.getString("id"));
            mOffre.setTitle(mResponseObject.getString("title"));
            mOffre.setSummary(mResponseObject.getString("summary"));
            mOffre.setPoint(mResponseObject.getString("points"));
            mOffre.setCompagnie_id(mResponseObject.getString("compagnie_id"));
            mOffre.setCategorie(mResponseObject.getString("categorie"));
            mOffre.setNiveau(mResponseObject.getString("niveau"));
            mOffre.setDescription(mResponseObject.getString("description"));
            if (mResponseObject.has("start_date"))
               mOffre.setStart_date(mResponseObject.getString("start_date"));
           // mOffre.setStart_date("2018-11-29 23:00:00");
           if (mResponseObject.has("stop_date"))
              mOffre.setStop_date(mResponseObject.getString("stop_date"));
           // mOffre.setStop_date("2018-12-04 23:00:00");
            mOffre.setImage(mResponseObject.getString("image"));
            if (mResponseObject.has("isCredential"))
                mOffre.setCredential(mResponseObject.getBoolean("isCredential"));
            if (mResponseObject.has("isComming"))
                mOffre.setComming(mResponseObject.getBoolean("isComming"));
            if (mResponseObject.has("next_kdo_date"))
                mOffre.setNext_kdo_date(mResponseObject.getString("next_kdo_date"));
            if (mResponseObject.has("canOfferAndBenefit"))
                mOffre.setCanOfferAndBenefit(mResponseObject.getBoolean("canOfferAndBenefit"));
            if (mResponseObject.has("hasSubscribe"))
                mOffre.setHasSubscribe(mResponseObject.getBoolean("hasSubscribe"));
            if (mResponseObject.has("profiter"))
                mOffre.setHasOffered(mResponseObject.getBoolean("profiter"));

            return mOffre;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Offre> parseOffres(JSONArray mResultsArray) {
        if (mResultsArray == null)
            return null;
        try {
            ArrayList<Offre> mOffres = new ArrayList<Offre>();
            for (int i = 0; i < mResultsArray.length(); i++) {
                mOffres.add(parseOffre(mResultsArray.getJSONObject(i)));
            }
            return mOffres;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getCompagnie_id() {
        return compagnie_id;
    }

    public void setCompagnie_id(String compagnie_id) {
        this.compagnie_id = compagnie_id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStop_date() {
        return stop_date;
    }

    public void setStop_date(String stop_date) {
        this.stop_date = stop_date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNext_kdo_date() {
        return next_kdo_date;
    }

    public void setNext_kdo_date(String next_kdo_date) {
        this.next_kdo_date = next_kdo_date;
    }

    public boolean isComming() {
        return isComming;
    }

    public void setComming(boolean isComming) {
        this.isComming = isComming;
    }

    public boolean isCredential() {
        return isCredential;
    }

    public void setCredential(boolean isCredential) {
        this.isCredential = isCredential;
    }

    public boolean isCanOfferAndBenefit() {
        return canOfferAndBenefit;
    }

    public void setCanOfferAndBenefit(boolean canOfferAndBenefit) {
        this.canOfferAndBenefit = canOfferAndBenefit;
    }

    public boolean isHasSubscribe() {
        return hasSubscribe;
    }

    public void setHasSubscribe(boolean hasSubscribe) {
        this.hasSubscribe = hasSubscribe;
    }

    public boolean isHasOffered() {
        return hasOffered;
    }

    public void setHasOffered(boolean hasOffered) {
        this.hasOffered = hasOffered;
    }

}
