package com.example.animoreproject.classes;

import android.widget.ImageView;

public class ItemMeuAnimal {
    private String id;
    private String imagem;
    private String nome;
    private String idade;
    private String raca;
    private ImageView imageView;

    public ItemMeuAnimal(String id, String imagem, String nome, String idade, String raca, ImageView imageView) {
        this.id        = id;
        this.imagem    = imagem;
        this.nome      = nome;
        this.idade     = idade;
        this.raca      = raca;
        this.imageView = imageView;
    }

    public String getId() {
        return id;
    }
    public String getImagem() {
        return imagem;
    }
    public String getNome() {
        return nome;
    }
    public String getIdade() {
        return idade;
    }
    public String getRaca() {
        return raca;
    }
    public ImageView getImageView() {
        return imageView;
    }
}
