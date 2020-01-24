package com.day1assignment.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity
public class CardModelClass {
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    private String title;
    private String description;

    private String link;

    private String pubdate;

    private String cat;

    public String getLink() {
        return link;
    }

    public String getPubdate() {
        return pubdate;
    }

    public String getCat() {
        return cat;
    }

    public CardModelClass() {

    }

    public CardModelClass(@NonNull String title, String description, String link, String pubdate, String cat) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubdate = pubdate;
        this.cat = cat;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public String getImageHref() {
        return link;
    }


    public String getmTilte() {
        return title;
    }


    public String getmDecription() {
        return description;
    }

}