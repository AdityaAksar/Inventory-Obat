package com.example.inventoryobat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ObatAdapter extends RecyclerView.Adapter<ObatAdapter.ObatViewHolder> {

    private Context context;
    private List<Obat> obatList;
    private List<Obat> obatFullList;

    public ObatAdapter(Context context, List<Obat> obatList) {
        this.context = context;
        this.obatList = new ArrayList<>(obatList);
        this.obatFullList = new ArrayList<>(obatList);
    }

    @NonNull
    @Override
    public ObatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_obat, parent, false);
        return new ObatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObatViewHolder holder, int position) {
        Obat obat = obatList.get(position);

        holder.tvNamaObat.setText(obat.getNamaObat());
        holder.tvJenisObat.setText("Jenis: " + obat.getJenisObat());
        holder.tvStock.setText("Stock: " + obat.getStock());

        if (obat.getGambarUrl() != null && !obat.getGambarUrl().isEmpty()) {
            Glide.with(context)
                    .load(obat.getGambarUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.imgObat);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, InfoProdukActivity.class);
            intent.putExtra("obat_id", obat.getIdObat());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return obatList.size();
    }

    public void updateData(List<Obat> newList) {
        obatList.clear();
        obatList.addAll(newList);
        obatFullList.clear();
        obatFullList.addAll(newList);
        notifyDataSetChanged();
    }

    public void filterObat(String keyword) {
        obatList.clear();
        if (keyword.isEmpty()) {
            obatList.addAll(obatFullList);
        } else {
            for (Obat obat : obatFullList) {
                if (obat.getNamaObat().toLowerCase().contains(keyword.toLowerCase())) {
                    obatList.add(obat);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ObatViewHolder extends RecyclerView.ViewHolder {
        ImageView imgObat;
        TextView tvNamaObat;
        TextView tvJenisObat;
        TextView tvStock;

        public ObatViewHolder(@NonNull View itemView) {
            super(itemView);
            imgObat = itemView.findViewById(R.id.imgObat);
            tvNamaObat = itemView.findViewById(R.id.tvNamaObat);
            tvJenisObat = itemView.findViewById(R.id.tvJenisObat);
            tvStock = itemView.findViewById(R.id.tvStock);
        }
    }
}
