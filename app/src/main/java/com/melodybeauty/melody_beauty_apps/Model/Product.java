package com.melodybeauty.melody_beauty_apps.Model;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {
    private String id;
    private String name;
    private String image;
    private String description;
    private String price;
    private String idKategori;
    private String jumlahTerjual;
    private Date createdAt;
    private Date updateAt;

    public Product(String id, String name, String image, String description, String price, String idKategori, String jumlahTerjual, Date createdAt, Date updateAt) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.idKategori = idKategori;
        this.jumlahTerjual = jumlahTerjual;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getIdKategori() {
        return idKategori;
    }

    public String getJumlahTerjual() {
        return jumlahTerjual;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
