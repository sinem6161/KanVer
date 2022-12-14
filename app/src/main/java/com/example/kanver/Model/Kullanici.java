package com.example.kanver.Model;

import com.example.kanver.Adapter.KullaniciAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class Kullanici {
    String ad;
    String email;
    String sehir;
    String parola;
    String Id;
    String adres;
    String kanGrubu;

    public Kullanici(String ad, String kanGrubu, String adres){
        this.ad = ad;
        this.kanGrubu = kanGrubu;
        this.adres = adres;
    }

    public Kullanici(String Ad, String Email, String Sehir, String Parola, String Adres, String KanGrubu, String Id){
        this.ad=Ad;
        this.email=Email;
        this.sehir=Sehir;
        this.parola=Parola;
        this.adres=Adres;
        this.kanGrubu=KanGrubu;
        this.Id=Id;
    }

    public Kullanici(){}

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSehir() {
        return sehir;
    }

    public void setSehir(String sehir) {
        this.sehir = sehir;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getKanGrubu() {
        return kanGrubu;
    }

    public void setKanGrubu(String kanGrubu) {
        this.kanGrubu = kanGrubu;
    }

    public String getId(){return FirebaseAuth.getInstance().getCurrentUser().getUid();}

}
