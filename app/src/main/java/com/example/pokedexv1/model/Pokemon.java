package com.example.pokedexv1.model;

public class Pokemon {
    private String name, spriteUrl, type1, type2;

    public Pokemon (String name) {
        this.name =  name;
    }

    public Pokemon (String name, String spriteUrl) {
        this.name =  name;
        this.spriteUrl = spriteUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name =  name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }

    public void setSpriteUrl(String spriteUrl) {
        this.spriteUrl = spriteUrl;
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
