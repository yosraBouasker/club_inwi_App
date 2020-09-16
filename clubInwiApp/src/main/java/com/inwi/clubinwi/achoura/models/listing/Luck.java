
package com.inwi.clubinwi.achoura.models.listing;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Luck {

    @Expose
    private String header;
    @Expose
    private String message;
    @Expose
    private Result result;
    @Expose
    private Long status;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

}
