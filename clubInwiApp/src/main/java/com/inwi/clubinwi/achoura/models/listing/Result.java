
package com.inwi.clubinwi.achoura.models.listing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Result {

    @Expose
    private Currents currents;
    @SerializedName("end_date")
    private String endDate;
    @Expose
    private Pasts pasts;
    @SerializedName("start_date")
    private String startDate;

    public Currents getCurrents() {
        return currents;
    }

    public void setCurrents(Currents currents) {
        this.currents = currents;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Pasts getPasts() {
        return pasts;
    }

    public void setPasts(Pasts pasts) {
        this.pasts = pasts;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

}
