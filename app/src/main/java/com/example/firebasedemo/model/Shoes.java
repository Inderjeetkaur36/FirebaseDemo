package com.example.firebasedemo.model;

import java.io.Serializable;

public class Shoes implements Serializable{

    public int image;
    public String docId;
    public String id;
    public String size;
    public String name;
    public String price;
    public String color;

    public Shoes(){

    }

    public Shoes(int image,String name,String id, String price, String color, String size) {
        this.image = image;
        this.id = id;
        this.name = name;
        this.price = price;
        this.color = color;
        this.size = size;
    }

}

