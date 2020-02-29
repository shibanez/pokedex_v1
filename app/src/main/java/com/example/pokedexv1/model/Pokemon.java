package com.example.pokedexv1.model;

public class Pokemon {
    private String name, infoUrl, spriteUrl, gifUrl;

    public Pokemon (String name, String infoUrl, String spriteUrl, String gifUrl) {
        this.name =  name.substring(0, 1).toUpperCase() + name.substring(1);
        this.infoUrl = infoUrl;
        this.spriteUrl = spriteUrl;
        this.gifUrl = gifUrl;
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

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
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
