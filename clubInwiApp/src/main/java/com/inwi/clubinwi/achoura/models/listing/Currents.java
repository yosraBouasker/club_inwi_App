
package com.inwi.clubinwi.achoura.models.listing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Currents {

    @Expose
    private Long countList;
    @SerializedName("list")
    private List<CurrentCadeau> currentCadeau;
    @Expose
    private Long nbrPage;
    @Expose
    private Long step;

    public Long getCountList() {
        return countList;
    }

    public void setCountList(Long countList) {
        this.countList = countList;
    }

    public List<CurrentCadeau> getCurrentCadeau() {
        return currentCadeau;
    }

    public void setCurrentCadeau(List<CurrentCadeau> currentCadeau) {
        this.currentCadeau = currentCadeau;
    }

    public Long getNbrPage() {
        return nbrPage;
    }

    public void setNbrPage(Long nbrPage) {
        this.nbrPage = nbrPage;
    }

    public Long getStep() {
        return step;
    }

    public void setStep(Long step) {
        this.step = step;
    }

}
