package com.example.logingit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConteudoAdapter extends RecyclerView.Adapter<ConteudoAdapter.MarcarViewHolder> {
    private List<Conteudo> conteudos;
    private Context context;
    private Integer exame;

    public ConteudoAdapter (List<Conteudo> _conteudos, Context _context, Integer _exame) {
        this.conteudos = _conteudos;
        this.context = _context;
        this.exame = _exame;
    }


    @NonNull
    @Override
    public MarcarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conteudo_view, parent, false);
        return new MarcarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarcarViewHolder holder, int position) {
        Conteudo conteudo = conteudos.get(position);
        holder.radioButton.setText(conteudo.getNome());


        holder.radioButton.setOnClickListener(v -> {
            if (holder.radioButton.isChecked()) {
                String msg;
                BancoControllerConteudo db = new BancoControllerConteudo(context);
                msg = db.TornarConteudoConluido(holder.radioButton.getText().toString(),exame);
                Toast.makeText(context, msg, Toast.LENGTH_LONG);
            } else {
                String msg;
                BancoControllerConteudo db = new BancoControllerConteudo(context);
                msg = db.TornarConteudoIncompleto(holder.radioButton.getText().toString(), exame);
                Toast.makeText(context, msg, Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conteudos.size();
    }

    public static class MarcarViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        public MarcarViewHolder (@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.btConteudo);
        }
    }
}
