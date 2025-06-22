package com.example.logingit.Redacao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logingit.R;

import java.util.List;

public class RedacaoAdapter extends RecyclerView.Adapter<RedacaoAdapter.ViewHolder> {
    private List<Redacao> redacoes;
    private Context context;

    public RedacaoAdapter (List<Redacao> redacoes, Context context) {
        this.redacoes = redacoes;
        this.context = context;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder {
        TextView txtTitulo, txtNota, txtAnalise;
        LinearLayout containerItem;

        public ViewHolder (View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtNota = itemView.findViewById(R.id.txtNota);
            txtAnalise = itemView.findViewById(R.id.txtAnalise);
            containerItem = itemView.findViewById(R.id.containerItem);
        }
    }

    @NonNull
    @Override
    public RedacaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_redacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RedacaoAdapter.ViewHolder holder, int position) {
        Redacao redacao = redacoes.get(position);
        holder.txtTitulo.setText(redacao.getTema());
        holder.txtNota.setText("Nota: " + redacao.getNota());
        holder.txtAnalise.setText(redacao.getAnalise());

        if (redacao.isExpandido()) {
            holder.txtAnalise.setVisibility(View.VISIBLE);
        } else {
            holder.txtAnalise.setVisibility(View.GONE);
        }

        holder.containerItem.setOnClickListener(v -> {
            redacao.setExpandido(!redacao.isExpandido());
            notifyItemChanged(position);

        });
    }

    @Override
    public int getItemCount() {
        return redacoes.size();
    }

}
