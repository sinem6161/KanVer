package com.example.kanver.Model;

import com.google.firebase.auth.FirebaseAuth;

public class KanKullanici {

    String Ad;
    String KanGrubu;
    String Adres;

    public KanKullanici(String Ad, String KanGrubu, String Adres){
        this.Ad=Ad;
        this.KanGrubu=KanGrubu;
        this.Adres=Adres;
    }

    public KanKullanici(){}

    public String getAd() {
        return Ad;
    }

    public void setAd(String ad) {
        Ad = ad;
    }

    public String getKanGrubu() {
        return KanGrubu;
    }

    public void setKanGrubu(String kanGrubu) {
        KanGrubu = kanGrubu;
    }

    public String getAdres() {
        return Adres;
    }

    public void setAdres(String adres) {
        Adres = adres;
    }

    public String getId(){return FirebaseAuth.getInstance().getCurrentUser().getUid();}
}
