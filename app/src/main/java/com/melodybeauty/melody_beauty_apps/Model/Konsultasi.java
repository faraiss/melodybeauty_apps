package com.melodybeauty.melody_beauty_apps.Model;

public class Konsultasi {
    private String id;
    private String idAntrian;
    private String hasilKonsultasi;
    private String idUser;
    private String noAntrian;
    private String tanggal;
    private String status;
    private String detailKeluhan;
    private String nameKeluhan;

    public Konsultasi(String id, String idAntrian, String hasilKonsultasi, String idUser, String noAntrian, String tanggal, String status, String detailKeluhan, String nameKeluhan) {
        this.id = id;
        this.idAntrian = idAntrian;
        this.hasilKonsultasi = hasilKonsultasi;
        this.idUser = idUser;
        this.noAntrian = noAntrian;
        this.tanggal = tanggal;
        this.status = status;
        this.detailKeluhan = detailKeluhan;
        this.nameKeluhan = nameKeluhan;
    }

    public String getId() {
        return id;
    }

    public String getIdAntrian() {
        return idAntrian;
    }

    public String getHasilKonsultasi() {
        return hasilKonsultasi;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getNoAntrian() {
        return noAntrian;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getStatus() {
        return status;
    }

    public String getDetailKeluhan() {
        return detailKeluhan;
    }

    public String getNameKeluhan() {
        return nameKeluhan;
    }
}
