package com.example.logingit.Tela_Questao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logingit.Questao;
import com.example.logingit.R;

import java.util.List;

public class QuestoesAdapter extends RecyclerView.Adapter<QuestoesAdapter.ViewHolder> {
    private List<com.example.logingit.Questao> lista;
    private Context context;

    public QuestoesAdapter(List<com.example.logingit.Questao> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMateria, txtAcertos, txtConteudo;

        public  ViewHolder(View itemView) {
            super(itemView);
            txtMateria = itemView.findViewById(R.id.txtMateria);
            txtAcertos = itemView.findViewById(R.id.txtAcertos);
            txtConteudo = itemView.findViewById(R.id.txtConteudo);
        }
    }

    @NonNull
    @Override
    public QuestoesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_questao, parent, false);
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestoesAdapter.ViewHolder holder, int position) {
        Questao item = lista.get(position);
        holder.txtMateria.setText("Matéria: " + item.getMateria());
        holder.txtConteudo.setText("Conteudo: " + item.getConteudo());
        holder.txtAcertos.setText("Acertos: " + item.getAcertos() + " de " + item.getTotal());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
