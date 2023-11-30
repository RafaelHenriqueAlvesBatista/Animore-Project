package com.example.animoreproject.classes;

import android.widget.ImageView;

public class ItemAnimalTelaInicial {
    private String id;
    private String nome;
    private String idade;
    private String like;
    private String deslike;
    private String foto;
    private ImageView imageView;

    public ItemAnimalTelaInicial(String id, String nome, String idade, String like, String deslike, String foto, ImageView imageView) {
        this.id        = id;
        this.nome      = nome;
        this.idade     = idade;
        this.like      = like;
        this.deslike   = deslike;
        this.foto      = foto;
        this.imageView = imageView;
    }

    public String getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getIdade() {
        return idade;
    }
    public String getLike() {
        return like;
    }
    public String getDeslike() {
        return deslike;
    }
    public String getFoto() {
        return foto;
    }
    public ImageView getImageView() {
        return imageView;
    }
}
