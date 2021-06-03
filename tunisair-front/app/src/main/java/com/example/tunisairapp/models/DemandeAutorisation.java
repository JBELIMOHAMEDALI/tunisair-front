package com.example.tunisairapp.models;

import com.google.gson.annotations.SerializedName;

public class DemandeAutorisation {
    @SerializedName("from")
    private String heureDebut;
    @SerializedName("to")
    private String heureFin;
    @SerializedName("description")
    private String raison;
    @SerializedName("status")
    private String etatAutoReq;
    @SerializedName("date")
    private String dateAutoReq;

    public DemandeAutorisation(String heureDebut, String heureFin, String raison, String etatAutoReq, String dateAutoReq){
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.raison = raison;
        this.etatAutoReq = etatAutoReq;
        this.dateAutoReq = dateAutoReq;
    }

    public DemandeAutorisation(String heureDebut, String heureFin, String raison){
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.raison = raison;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getEtatAutoReq() {
        return etatAutoReq;
    }

    public void setEtatAutoReq(String etatAutoReq) {
        this.etatAutoReq = etatAutoReq;
    }


    public String getDateAutoReq() {
        return dateAutoReq;
    }

    public void setDateAutoReq(String dateAutoReq) {
        this.dateAutoReq = dateAutoReq;
    }


}
