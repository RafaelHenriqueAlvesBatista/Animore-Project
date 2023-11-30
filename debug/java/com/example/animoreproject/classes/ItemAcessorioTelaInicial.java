package com.example.animoreproject.classes;

import android.widget.ImageView;

public class ItemAcessorioTelaInicial {
    private String id;
    private String nome;
    private String foto;
    private ImageView imageView;

    public ItemAcessorioTelaInicial(String id, String nome, String foto, ImageView imageView) {
        this.id        = id;
        this.nome      = nome;
        this.foto      = foto;
        this.imageView = imageView;
    }

    public String getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getFoto() {
        return foto;
    }
    public ImageView getImageView() {
        return imageView;
    }
}
