package com.example.animoreproject.classes;

public class ItemVacina {
    private String nome;
    private boolean isSelected;

    public ItemVacina(String nome) {
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
