package com.example.tunisairapp.models;

import com.google.gson.annotations.SerializedName;

public class DemandeConge {
    @SerializedName("date_request")
    private String dateRequest;
    @SerializedName("from")
    private String dateDebut;
    @SerializedName("to")
    private String datefin;
    @SerializedName("description")
    private String raison;
    @SerializedName("status")
    private String statusRequestConge;
    @SerializedName("justification")
    private String justif;
    private String matricule;
    @SerializedName("id_type_leave")
    private String id_type_Conge;
    @SerializedName("type")
    private String title;

    public DemandeConge(String title,String dateRequest,String dateDebut,
                        String datefin, String raison, String statusRequestConge, String justif, String matricule,
                        String id_type_Conge) {
        this.title = title;
        this.dateRequest=dateRequest;
        this.dateDebut = dateDebut;
        this.datefin = datefin;
        this.raison = raison;
        this.statusRequestConge = statusRequestConge;
        this.justif = justif;
        this.matricule = matricule;
        this.id_type_Conge = id_type_Conge;
    }
    public DemandeConge(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDatefin() {
        return datefin;
    }

    public void setDatefin(String datefin) {
        this.datefin = datefin;
    }

    public String getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(String dateRequest) {
        this.dateRequest = dateRequest;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getStatusRequestConge() {
        return statusRequestConge;
    }

    public void setStatusRequestConge(String statusRequestConge) {
        this.statusRequestConge = statusRequestConge;
    }

    public String getJustif() {
        return justif;
    }

    public void setJustif(String justif) {
        this.justif = justif;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getId_type_Conge() {
        return id_type_Conge;
    }

    public void setId_type_Conge(String id_type_Conge) {
        this.id_type_Conge = id_type_Conge;
    }


}
