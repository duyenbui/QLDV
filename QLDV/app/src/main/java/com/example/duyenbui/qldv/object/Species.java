package com.example.duyenbui.qldv.object;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by code-engine-studio on 22/02/2017.
 */

public class Species extends RealmObject{
    private int id;
    private String notation;
    private String scienceName;
    private String vietnameseName;
    private String otherName;
    private String individualQuantity;
    private String reproductionTraits;
    private String sexualTraits;
    private String ortherTraits;
    private String alertlevel;
    private String discovererName;
    private String biologicalBehavior;
    private String mediumSize;
    private String food;
    private String origin;
    private String image;
    private Date yearDiscover;
    private int status;
    private Date dateUpdate;
    private Date dateCreate;
    private int idGenus;
    private String scienceNameGenus;
    private String vietnameseNameGenus;
    private int idCreator;
    private String nameCreator;
    private int idChecker;
    private String nameChecker;
    private String vietnameseNameFamily;

    public Species() {
    }

    public Species(int id, String vietnameseName, String scienceName, String vietnameseNameFamily, String image) {
        this.id = id;
        this.vietnameseName = vietnameseName;
        this.scienceName = scienceName;
        this.vietnameseNameFamily = vietnameseNameFamily;
        this.image = image;
    }

    public Species(int id, String notation, String scienceName, String vietnameseName, String otherName, String individualQuantity, String reproductionTraits, String sexualTraits, String ortherTraits, String alertlevel, String discovererName, String biologicalBehavior, String food, String mediumSize, String origin, String image, Date yearDiscover, int status, Date dateUpdate, Date dateCreate, int idGenus, String scienceNameGenus, String vietnameseNameGenus, int idCreator, String nameCreator, int idChecker, String nameChecker, String vietnameseNameFamily) {
        this.id = id;
        this.notation = notation;
        this.scienceName = scienceName;
        this.vietnameseName = vietnameseName;
        this.otherName = otherName;
        this.individualQuantity = individualQuantity;
        this.reproductionTraits = reproductionTraits;
        this.sexualTraits = sexualTraits;
        this.ortherTraits = ortherTraits;
        this.alertlevel = alertlevel;
        this.discovererName = discovererName;
        this.biologicalBehavior = biologicalBehavior;
        this.food = food;
        this.mediumSize = mediumSize;
        this.origin = origin;
        this.image = image;
        this.yearDiscover = yearDiscover;
        this.status = status;
        this.dateUpdate = dateUpdate;
        this.dateCreate = dateCreate;
        this.idGenus = idGenus;
        this.scienceNameGenus = scienceNameGenus;
        this.vietnameseNameGenus = vietnameseNameGenus;
        this.idCreator = idCreator;
        this.nameCreator = nameCreator;
        this.idChecker = idChecker;
        this.nameChecker = nameChecker;
        this.vietnameseNameFamily = vietnameseNameFamily;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    public String getScienceName() {
        return scienceName;
    }

    public void setScienceName(String scienceName) {
        this.scienceName = scienceName;
    }

    public String getVietnameseName() {
        return vietnameseName;
    }

    public void setVietnameseName(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getIndividualQuantity() {
        return individualQuantity;
    }

    public void setIndividualQuantity(String individualQuantity) {
        this.individualQuantity = individualQuantity;
    }

    public String getReproductionTraits() {
        return reproductionTraits;
    }

    public void setReproductionTraits(String reproductionTraits) {
        this.reproductionTraits = reproductionTraits;
    }

    public String getSexualTraits() {
        return sexualTraits;
    }

    public void setSexualTraits(String sexualTraits) {
        this.sexualTraits = sexualTraits;
    }

    public String getOrtherTraits() {
        return ortherTraits;
    }

    public void setOrtherTraits(String ortherTraits) {
        this.ortherTraits = ortherTraits;
    }

    public String getAlertlevel() {
        return alertlevel;
    }

    public void setAlertlevel(String alertlevel) {
        this.alertlevel = alertlevel;
    }

    public String getDiscovererName() {
        return discovererName;
    }

    public void setDiscovererName(String discovererName) {
        this.discovererName = discovererName;
    }

    public String getBiologicalBehavior() {
        return biologicalBehavior;
    }

    public void setBiologicalBehavior(String biologicalBehavior) {
        this.biologicalBehavior = biologicalBehavior;
    }

    public String getMediumSize() {
        return mediumSize;
    }

    public void setMediumSize(String mediumSize) {
        this.mediumSize = mediumSize;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getYearDiscover() {
        return yearDiscover;
    }

    public void setYearDiscover(Date yearDiscover) {
        this.yearDiscover = yearDiscover;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public int getIdGenus() {
        return idGenus;
    }

    public void setIdGenus(int idGenus) {
        this.idGenus = idGenus;
    }

    public String getScienceNameGenus() {
        return scienceNameGenus;
    }

    public void setScienceNameGenus(String scienceNameGenus) {
        this.scienceNameGenus = scienceNameGenus;
    }

    public String getVietnameseNameGenus() {
        return vietnameseNameGenus;
    }

    public void setVietnameseNameGenus(String vietnameseNameGenus) {
        this.vietnameseNameGenus = vietnameseNameGenus;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public String getNameCreator() {
        return nameCreator;
    }

    public void setNameCreator(String nameCreator) {
        this.nameCreator = nameCreator;
    }

    public int getIdChecker() {
        return idChecker;
    }

    public void setIdChecker(int idChecker) {
        this.idChecker = idChecker;
    }

    public String getNameChecker() {
        return nameChecker;
    }

    public void setNameChecker(String nameChecker) {
        this.nameChecker = nameChecker;
    }

    public String getVietnameseNameFamily() {
        return vietnameseNameFamily;
    }

    public void setVietnameseNameFamily(String vietnameseNameFamily) {
        this.vietnameseNameFamily = vietnameseNameFamily;
    }
}
