package com.example.jenny.startexercise.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jenny on 2018/3/25.
 */

public class Home2item implements Parcelable {

    private String content;
    private String date;
    private String icon_path;

    public Home2item(String content, String date, String icon_path) {
        this.content = content;
        this.date = date;
        this.icon_path = icon_path;
    }

    public Home2item() {

    }


    protected Home2item(Parcel in) {
        content = in.readString();
        date = in.readString();
        icon_path = in.readString();
    }

    public static final Creator<Rankitem> CREATOR = new Creator<Rankitem>() {
        @Override
        public Rankitem createFromParcel(Parcel in) {
            return new Rankitem(in);
        }

        @Override
        public Rankitem[] newArray(int size) {
            return new Rankitem[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPic_path() {
        return icon_path;
    }

    public void setPic_path(String icon_path) {
        this.icon_path = icon_path;
    }


    @Override
    public String toString() {
        return "Home2item{" +
                "content='" + content + '\'' +
                ", start_time='" + date + '\'' +
                ", icon_path='" + icon_path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString(icon_path);
    }
}