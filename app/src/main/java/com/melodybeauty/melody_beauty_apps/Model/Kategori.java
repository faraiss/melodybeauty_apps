package com.melodybeauty.melody_beauty_apps.Model;

public class Kategori {
    private String id;
    private String name;

    public Kategori(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
