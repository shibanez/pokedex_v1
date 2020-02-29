package com.example.pokedexv1.model;

import java.io.Serializable;

public class Pokemon implements Serializable {
    private String name, infoUrl, spriteUrl, imageUrl, type1, type2;

    public Pokemon (String name, String infoUrl, String spriteUrl, String imageUrl) {
        this.name =  name.substring(0, 1).toUpperCase() + name.substring(1);
        this.infoUrl = infoUrl;
        this.spriteUrl = spriteUrl;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name =  name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }

    public void setSpriteUrl(String spriteUrl) {
        this.spriteUrl = spriteUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }
}
