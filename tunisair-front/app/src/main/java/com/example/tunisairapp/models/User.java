package com.example.tunisairapp.models;

public class User {
    private String email;
    private String password;
    private String matricule;
    private String cin;
    private String name;
    private String lastname;
    private String role;
    private String cnsscnpsnum;
    private String gender;
    private String birthdate;
    private String employment;
    private String entrydate;
    private String position;
    private String corps;
    private String status;
    private String direction;
    private String entity;
    private String place;
    private String service;
    private String affiliate;
    private String leavebalance;
    private String authorizationbalance;


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User() {

    }
    public User(String leavebalance,String authorizationbalance,String matricule) {
        this.leavebalance=leavebalance;
        this.authorizationbalance=authorizationbalance;
        this.matricule=matricule;

    }

    public User(String email, String password, String matricule, String cin, String name, String lastname, String role, String cnsscnpsnum, String gender, String birthdate, String employment, String entrydate, String position, String corps, String status, String direction, String entity, String place, String service, String affiliate, String leavebalance, String authorizationbalance) {
        this.email = email;
        this.password = password;
        this.matricule = matricule;
        this.cin = cin;
        this.name = name;
        this.lastname = lastname;
        this.role = role;
        this.cnsscnpsnum = cnsscnpsnum;
        this.gender = gender;
        this.birthdate = birthdate;
        this.employment = employment;
        this.entrydate = entrydate;
        this.position = position;
        this.corps = corps;
        this.status = status;
        this.direction = direction;
        this.entity = entity;
        this.place = place;
        this.service = service;
        this.affiliate = affiliate;
        this.leavebalance = leavebalance;
        this.authorizationbalance = authorizationbalance;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCnsscnpsnum() {
        return cnsscnpsnum;
    }

    public void setCnsscnpsnum(String cnsscnpsnum) {
        this.cnsscnpsnum = cnsscnpsnum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(String entrydate) {
        this.entrydate = entrydate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCorps() {
        return corps;
    }

    public void setCorps(String corps) {
        this.corps = corps;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(String affiliate) {
        this.affiliate = affiliate;
    }

    public String getLeavebalance() {
        return leavebalance;
    }

    public void setLeavebalance(String leavebalance) {
        this.leavebalance = leavebalance;
    }

    public String getAuthorizationbalance() {
        return authorizationbalance;
    }

    public void setAuthorizationbalance(String authorizationbalance) {
        this.authorizationbalance = authorizationbalance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
