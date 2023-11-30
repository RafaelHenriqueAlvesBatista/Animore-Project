package com.example.animoreproject.classes;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.animoreproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAcessorioTelaInicial extends RecyclerView.Adapter<AdapterAcessorioTelaInicial.ViewHolder> {
    private List<ItemAcessorioTelaInicial> listaAcessorio;
    private OnAcessorioClickListener onAcessorioClickListener;

    public AdapterAcessorioTelaInicial(List<ItemAcessorioTelaInicial> listaAcessorio, OnAcessorioClickListener listener) {
        this.listaAcessorio = listaAcessorio;
        this.onAcessorioClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_acessorios_tela_inicial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemAcessorioTelaInicial acessorioInicial = listaAcessorio.get(position);

        Picasso.get().load(acessorioInicial.getFoto()).into(holder.imvFotoAcessorio);
        holder.txvNomeAcessorio.setText(acessorioInicial.getNome());

        holder.llyFotoAcessorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String acessorioId = acessorioInicial.getId();
                    onAcessorioClickListener.onAcessorioClick(acessorioId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAcessorio.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView    imvFotoAcessorio;
        public LinearLayout llyFotoAcessorio;
        public TextView     txvNomeAcessorio;

        public ViewHolder(View view) {
            super(view);
            imvFotoAcessorio  = view.findViewById(R.id.imvFotoAcessorio);
            llyFotoAcessorio  = view.findViewById(R.id.llyFotoAcessorio);
            txvNomeAcessorio  = view.findViewById(R.id.txvNomeAcessorio);
        }
    }

    public interface OnAcessorioClickListener {
        void onAcessorioClick(String IDacessorio);
    }
}
