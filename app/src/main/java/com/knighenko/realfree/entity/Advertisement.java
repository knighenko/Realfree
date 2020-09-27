package com.knighenko.realfree.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class Advertisement implements Parcelable {
    private String title;
    private String url;
    private String imageSrc;
    private String description;

    protected Advertisement(Parcel in) {
        title = in.readString();
        url = in.readString();
        imageSrc = in.readString();
        description = in.readString();
    }

    public static final Creator<Advertisement> CREATOR = new Creator<Advertisement>() {
        @Override
        public Advertisement createFromParcel(Parcel in) {
            return new Advertisement(in);
        }

        @Override
        public Advertisement[] newArray(int size) {
            return new Advertisement[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder("entity.Advertisement{");
        stringBuffer.append("title='").append(title).append('\'').append(", url='").append(url).append('\'').
                append(", imageSrc='").append(imageSrc).append('\'').append(", description='").append(description).append('\'').append('}');
      /*  return "entity.Advertisement{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                '}';*/
         return stringBuffer.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeString(imageSrc);
        parcel.writeString(description);
    }
}
