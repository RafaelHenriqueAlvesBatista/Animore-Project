package com.example.animoreproject.classes;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.animoreproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAnimalTelaInicial extends RecyclerView.Adapter<AdapterAnimalTelaInicial.ViewHolder> {
    private List<ItemAnimalTelaInicial> listaAnimal;
    private OnAnimalClickListener onAnimalClickListener;

    public AdapterAnimalTelaInicial(List<ItemAnimalTelaInicial> listaAnimal, OnAnimalClickListener listener) {
        this.listaAnimal = listaAnimal;
        this.onAnimalClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animais_tela_inicial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemAnimalTelaInicial animalInicial = listaAnimal.get(position);

        Picasso.get().load(animalInicial.getFoto()).into(holder.imvFotoAnimal);
        holder.txvNomeAnimal.setText(animalInicial.getNome());
        holder.txvIdadeAnimal.setText(animalInicial.getIdade());
        holder.txvLikeAnimal.setText(animalInicial.getLike());
        holder.txvDeslikeAnimal.setText(animalInicial.getDeslike());

        holder.llyFotoAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String animalId = animalInicial.getId();
                    onAnimalClickListener.onAnimalClick(animalId);
                }
            }
        });

        holder.btnLikeAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("BOTAO LIKE CLICADO!");
                // TODO
            }
        });
        holder.btnDeslikeAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("BOTAO DESLIKE CLICADO!");
                // TODO
            }
        });
        holder.btnCoracaoAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("BOTAO CORACAO CLICADO!");
                // TODO
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAnimal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imvFotoAnimal;
        public LinearLayout llyFotoAnimal;
        public TextView txvNomeAnimal, txvIdadeAnimal, txvLikeAnimal, txvDeslikeAnimal;
        public ImageButton btnLikeAnimal, btnDeslikeAnimal, btnCoracaoAnimal;

        public ViewHolder(View view) {
            super(view);
            imvFotoAnimal    = view.findViewById(R.id.imvFotoAnimal);
            llyFotoAnimal    = view.findViewById(R.id.llyFotoAnimal);
            txvNomeAnimal    = view.findViewById(R.id.txvNomeAnimal);
            txvIdadeAnimal   = view.findViewById(R.id.txvIdadeAnimal);
            txvLikeAnimal    = view.findViewById(R.id.txvLikeAnimal);
            txvDeslikeAnimal = view.findViewById(R.id.txvDeslikeAnimal);
            btnLikeAnimal    = view.findViewById(R.id.btnLikeAnimal);
            btnDeslikeAnimal = view.findViewById(R.id.btnDeslikeAnimal);
            btnCoracaoAnimal = view.findViewById(R.id.btnCoracaoAnimal);
        }
    }

    public interface OnAnimalClickListener {
        void onAnimalClick(String IDanimal);
    }
}
