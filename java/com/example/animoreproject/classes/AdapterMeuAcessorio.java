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

import java.util.ArrayList;
import java.util.List;

public class AdapterMeuAcessorio extends RecyclerView.Adapter<AdapterMeuAcessorio.ViewHolder> {
    private List<ItemMeuAcessorio> listaAcessorio;
    private OnAcessorioClickListener onAcessorioClickListener;

    public AdapterMeuAcessorio(List<ItemMeuAcessorio> listaAcessorio, OnAcessorioClickListener listener) {
        this.listaAcessorio = listaAcessorio;
        this.onAcessorioClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meus_acessorios, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemMeuAcessorio meuAcessorio = listaAcessorio.get(position);

        Picasso.get().load(meuAcessorio.getImagem()).into(holder.imvMeuAcessorio);
        holder.txvNomeMeuAcessorio.setText(meuAcessorio.getNome());
        holder.txvTipoMeuAcessorio.setText(meuAcessorio.getTipo());

        holder.cdvMeuAcessorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String acessorioId = meuAcessorio.getId();
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
        public ImageView imvMeuAcessorio;
        public TextView txvNomeMeuAcessorio;
        public TextView txvTipoMeuAcessorio;
        public CardView cdvMeuAcessorio;

        public ViewHolder(View view) {
            super(view);
            imvMeuAcessorio      = view.findViewById(R.id.imvMeuAcessorio);
            txvNomeMeuAcessorio  = view.findViewById(R.id.txvNomeMeuAcessorio);
            txvTipoMeuAcessorio  = view.findViewById(R.id.txvTipoMeuAcessorio);
            cdvMeuAcessorio      = view.findViewById(R.id.cdvMeuAcessorio);
        }
    }

    public interface OnAcessorioClickListener {
        void onAcessorioClick(String IDacessorio);
    }
}
