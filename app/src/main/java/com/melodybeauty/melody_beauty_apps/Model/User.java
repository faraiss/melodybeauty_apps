package com.melodybeauty.melody_beauty_apps.Model;


import java.util.Date;

public class User {
    private String id;
    private String name;
    private String image;
    private String email;
    private String jenisKelamin;
    private String idKulit;
    private String jenisKulit;
    private String tanggalLahir;
    private String noHp;
    private String Alamat;

    public User(String id, String name, String image, String email, String jenisKelamin, String idKulit,String jenisKulit, String tanggalLahir, String noHp, String alamat) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.jenisKelamin = jenisKelamin;
        this.idKulit = idKulit;
        this.jenisKulit = jenisKulit;
        this.tanggalLahir = tanggalLahir;
        this.noHp = noHp;
        this.Alamat = alamat;
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

    public String getEmail() {
        return email;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getJenisKulit() {
        return jenisKulit;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getAlamat() {
        return Alamat;
    }

    public String getIdKulit(){return idKulit;}

}

