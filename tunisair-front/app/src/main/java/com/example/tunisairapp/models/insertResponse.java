package com.example.tunisairapp.models;

public class insertResponse {
    private int succes;
    private boolean insert;
    private boolean update;

    public insertResponse(int succes, boolean insert,boolean update) {
        this.succes = succes;
        this.insert = insert;
        this.update = update;
    }

    public insertResponse(){

    }

    public int getSucces() {
        return succes;
    }

    public void setSucces(int succes) {
        this.succes = succes;
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
