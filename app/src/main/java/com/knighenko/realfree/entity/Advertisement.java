package com.knighenko.realfree.entity;

public class Advertisement {
    private String title;
    private String url;
    private String imageSrc;

    public String getTitle() {
        return title;
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
        return "entity.Advertisement{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
