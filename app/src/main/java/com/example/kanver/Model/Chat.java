package com.example.kanver.Model;

public class Chat {
    String alici;
    String gonderen;
    String mesaj;
    String resim;

    public Chat(){}

    public Chat(String alici, String gonderen, String mesaj, String resim){
        this.alici=alici;
        this.gonderen=gonderen;
        this.mesaj=mesaj;
        this.resim=resim;
    }

    public String getAlici() {
        return alici;
    }

    public void setAlici(String alici) {
        this.alici = alici;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }
}
