package com.example.logingit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VazioAdapter extends RecyclerView.Adapter<VazioAdapter.ViewHolder> {



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sem_conteudo_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text_vazio.setText("SEM CONTEUDOS POR HOJE!!");
    }


    @Override
    public int getItemCount() {
        return 1;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_vazio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_vazio = itemView.findViewById(R.id.text_vazio);
        }
    }

}

