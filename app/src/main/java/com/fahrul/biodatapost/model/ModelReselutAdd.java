

        package com.fahrul.biodatapost.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelReselutAdd implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Parcelable.Creator<ModelReselutAdd> CREATOR = new Creator<ModelReselutAdd>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ModelReselutAdd createFromParcel(Parcel in) {
            return new ModelReselutAdd(in);
        }

        public ModelReselutAdd[] newArray(int size) {
            return (new ModelReselutAdd[size]);
        }

    }
            ;
    private final static long serialVersionUID = 8530811385712044827L;

    protected ModelReselutAdd(Parcel in) {
        this.status = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ModelReselutAdd() {
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ModelReselutAdd withStatus(boolean status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ModelReselutAdd withMessage(String message) {
        this.message = message;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
    }

    public int describeContents() {
        return 0;
    }

}
