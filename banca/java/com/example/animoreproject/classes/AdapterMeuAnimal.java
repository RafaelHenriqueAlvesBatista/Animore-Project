package com.example.animoreproject.classes;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animoreproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMeuAnimal extends RecyclerView.Adapter<AdapterMeuAnimal.ViewHolder> {
    private List<ItemMeuAnimal> listaAnimal;
    private OnAnimalClickListener onAnimalClickListener;

    public AdapterMeuAnimal(List<ItemMeuAnimal> listaAnimal, OnAnimalClickListener listener) {
        this.listaAnimal = listaAnimal;
        this.onAnimalClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meus_animais, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemMeuAnimal meuAnimal = listaAnimal.get(position);

        Picasso.get().load(meuAnimal.getImagem()).into(holder.imvMeuAnimal);
        holder.txvNomeMeuAnimal.setText(meuAnimal.getNome());
        holder.txvIdadeMeuAnimal.setText(meuAnimal.getIdade());
        holder.txvRacaMeuAnimal.setText(meuAnimal.getRaca());

        holder.cdvMeuAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String animalId = meuAnimal.getId();
                    onAnimalClickListener.onAnimalClick(animalId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAnimal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imvMeuAnimal;
        public TextView txvNomeMeuAnimal;
        public TextView txvIdadeMeuAnimal;
        public TextView txvRacaMeuAnimal;
        public CardView cdvMeuAnimal;

        public ViewHolder(View view) {
            super(view);
            imvMeuAnimal      = view.findViewById(R.id.imvMeuAnimal);
            txvNomeMeuAnimal  = view.findViewById(R.id.txvNomeMeuAnimal);
            txvIdadeMeuAnimal = view.findViewById(R.id.txvIdadeMeuAnimal);
            txvRacaMeuAnimal  = view.findViewById(R.id.txvRacaMeuAnimal);
            cdvMeuAnimal      = view.findViewById(R.id.cdvMeuAnimal);
        }
    }

    public interface OnAnimalClickListener {
        void onAnimalClick(String IDanimal);
    }
}
