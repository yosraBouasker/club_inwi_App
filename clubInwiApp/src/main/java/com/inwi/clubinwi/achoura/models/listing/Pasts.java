
package com.inwi.clubinwi.achoura.models.listing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Pasts {

    @Expose
    private Long countList;
    @Expose
    private Long nbrPage;
    @SerializedName("list")
    private List<PastCadeau> pastCadeau;
    @Expose
    private Long step;

    public Long getCountList() {
        return countList;
    }

    public void setCountList(Long countList) {
        this.countList = countList;
    }

    public Long getNbrPage() {
        return nbrPage;
    }

    public void setNbrPage(Long nbrPage) {
        this.nbrPage = nbrPage;
    }

    public List<PastCadeau> getPastCadeau() {
        return pastCadeau;
    }

    public void setPastCadeau(List<PastCadeau> pastCadeau) {
        this.pastCadeau = pastCadeau;
    }

    public Long getStep() {
        return step;
    }

    public void setStep(Long step) {
        this.step = step;
    }

}
