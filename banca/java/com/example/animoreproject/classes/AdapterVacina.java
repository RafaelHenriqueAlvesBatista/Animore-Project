package com.example.animoreproject.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.animoreproject.R;

import java.util.List;

public class AdapterVacina extends RecyclerView.Adapter<AdapterVacina.ViewHolder> {
    private List<ItemVacina> listaVacina;

    public AdapterVacina(List<ItemVacina> listaVacina) {
        this.listaVacina = listaVacina;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemVacina itemVacina = listaVacina.get(position);
        holder.checkbox.setText(itemVacina.getNome());
        holder.checkbox.setChecked(itemVacina.isSelected());
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> itemVacina.setSelected(isChecked));
    }

    @Override
    public int getItemCount() {
        return listaVacina.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkBox);
        }
    }
}
