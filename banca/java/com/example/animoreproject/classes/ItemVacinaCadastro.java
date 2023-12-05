package com.example.animoreproject.classes;

public class ItemVacinaCadastro {
    private String nome;
    private boolean isSelected;

    public ItemVacinaCadastro(String nome) {
        this.nome = nome;
        isSelected = false;
    }

    public String getNome() {
        return nome;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
