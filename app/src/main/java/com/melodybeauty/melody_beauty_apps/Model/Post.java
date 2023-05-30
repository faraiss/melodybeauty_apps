package com.melodybeauty.melody_beauty_apps.Model;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    String id;
    String title;
    String slug;
    String image;
    String content;
    Date date;
    Date createdAt;
    Date updateAt;

    public Post(String id, String title, String slug, String image, String content, Date date, Date createdAt, Date updateAt) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.image = image;
        this.content = content;
        this.date = date;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
