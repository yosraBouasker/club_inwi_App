package com.inwi.clubinwi.Beans;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by me on 2/26/18.
 * Tombola
 */

public class Tombola {
    private String title;
    private String description;
    private String image;
    private String ticketId;
    private String compagnieId;
    private String ville;
    private Integer benefit;
    private Integer status;
    private String pdf;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getCompagnieId() {
        return compagnieId;
    }

    public void setCompagnieId(String compagnieId) {
        this.compagnieId = compagnieId;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getBenefit() {
        return benefit;
    }

    public void setBenefit(Integer benefit) {
        this.benefit = benefit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }


    private static Tombola fromJson(JSONObject jsonObject) {
        Tombola tombola = new Tombola();
        tombola.setImage(jsonObject.optString("image"));
        tombola.setVille(jsonObject.optString("ville"));
        tombola.setPdf(jsonObject.optString("pdf"));
        tombola.setTitle(jsonObject.optString("title"));
        tombola.setDescription(jsonObject.optString("description"));
        tombola.setBenefit(jsonObject.optInt("benefit"));
        tombola.setCompagnieId(jsonObject.optString("compagnie_id"));
        tombola.setStatus(jsonObject.optInt("status"));
        tombola.setTicketId(jsonObject.optString("ticket_id"));
        tombola.setMessage(jsonObject.optString("message"));
        return tombola;
    }

    /**
     * Return list of {@link Tombola} from a {@link JSONObject}
     *
     * @param jsonArray the json array
     * @return a list of {@link Tombola}
     */
    public static List<Tombola> tombolasFromJson(JSONArray jsonArray) {

        List<Tombola> tombolas = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            tombolas.add(fromJson(jsonArray.optJSONObject(i)));
        }

        return tombolas;
    }
}
