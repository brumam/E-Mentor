package com.example.e_mentor.Helpers;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Module implements Serializable, Parcelable {
    private String name;
    private String imageURL;
    private String description;

    public Module() {
        // Default constructor required for calls to DataSnapshot.getValue(Module.class)
    }

    public Module(String name, String imageURL, String description) {
        this.name = name;
        this.imageURL = imageURL;
        this.description = description;
    }


    protected Module(Parcel in) {
        name = in.readString();
        description = in.readString();
        imageURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imageURL);
    }

    public static final Creator<Module> CREATOR = new Creator<Module>() {
        @Override
        public Module createFromParcel(Parcel in) {
            return new Module(in);
        }

        @Override
        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}

