package app.puretech.e_sport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentProfiledModel {
    @SerializedName("success")
    @Expose
    private String success;


    @SerializedName("iamge")
    @Expose
    private String iamge;

    @SerializedName("message")
    @Expose
    private String message;

    public String getIamge() {
        return iamge;
    }

    public void setIamge(String iamge) {
        this.iamge = iamge;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
