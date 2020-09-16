
package com.inwi.clubinwi.achoura.models.reserver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Reservation {

    @SerializedName("cadeau_title")
    private Object cadeauTitle;
    @Expose
    private String header;
    @Expose
    private String message;
    @Expose
    private Result result;
    @Expose
    private Long status;

    @Override
    public String toString() {
        return "Reservation{" +
                "cadeauTitle=" + cadeauTitle +
                ", header='" + header + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                ", status=" + status +
                '}';
    }

    public Object getCadeauTitle() {
        return cadeauTitle;
    }

    public void setCadeauTitle(Object cadeauTitle) {
        this.cadeauTitle = cadeauTitle;
    }

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
