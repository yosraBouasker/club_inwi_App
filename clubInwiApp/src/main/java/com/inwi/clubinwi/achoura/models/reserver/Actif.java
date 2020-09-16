
package com.inwi.clubinwi.achoura.models.reserver;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Actif {

    @Expose
    private String id;
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
