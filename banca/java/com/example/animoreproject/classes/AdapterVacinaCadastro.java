package com.example.animoreproject.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.animoreproject.R;

import java.util.List;

public class AdapterVacinaCadastro extends RecyclerView.Adapter<AdapterVacinaCadastro.ViewHolder> {
    private List<ItemVacinaCadastro> listaVacina;

    public AdapterVacinaCadastro(List<ItemVacinaCadastro> listaVacina) {
        this.listaVacina = listaVacina;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemVacinaCadastro itemVacinaCadastro = listaVacina.get(position);
        holder.checkbox.setText(itemVacinaCadastro.getNome());
        holder.checkbox.setChecked(itemVacinaCadastro.isSelected());
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> itemVacinaCadastro.setSelected(isChecked));
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

    public void setVacinasTomadas(String vacina) {
        for (int i = 0; i < vacina.length(); i++) {
            listaVacina.get(i).setSelected(vacina.charAt(i) == '1');
        }
        notifyDataSetChanged();
    }
}
