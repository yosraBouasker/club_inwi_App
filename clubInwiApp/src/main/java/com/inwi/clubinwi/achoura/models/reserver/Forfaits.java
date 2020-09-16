
package com.inwi.clubinwi.achoura.models.reserver;

import java.util.List;
import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Forfaits {

    @Expose
    private List<Actif> actif;
    @Expose
    private Long count;

    public List<Actif> getActif() {
        return actif;
    }

    public void setActif(List<Actif> actif) {
        this.actif = actif;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
