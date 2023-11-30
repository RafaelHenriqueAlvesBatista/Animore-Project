package com.example.animoreproject.classes;

import android.widget.ImageView;

public class ItemMeuAcessorio {
    private String id;
    private String imagem;
    private String nome;
    private String tipo;
    private ImageView imageView;

    public ItemMeuAcessorio(String id, String imagem, String nome, String tipo, ImageView imageView) {
        this.id        = id;
        this.imagem    = imagem;
        this.nome      = nome;
        this.tipo      = tipo;
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
    public String getTipo() {
        return tipo;
    }
    public ImageView getImageView() {
        return imageView;
    }
}
