package com.example.animoreproject.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.animoreproject.R;

import java.util.List;

public class AdapterVacinaPerfil extends RecyclerView.Adapter<AdapterVacinaPerfil.ViewHolder> {
    private List<ItemVacinaPerfil> listaVacina;

    public AdapterVacinaPerfil(List<ItemVacinaPerfil> listaVacina) {
        this.listaVacina = listaVacina;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vacinas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemVacinaPerfil itemVacinaPerfil = listaVacina.get(position);
        holder.txvNomeVacina.setText(itemVacinaPerfil.getNome());
    }

    @Override
    public int getItemCount() {
        return listaVacina.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txvNomeVacina;

        public ViewHolder(View itemView) {
            super(itemView);
            txvNomeVacina = itemView.findViewById(R.id.txvNomeVacina);
        }
    }
}
