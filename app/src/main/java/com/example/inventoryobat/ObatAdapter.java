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
        holder.tvJenisObat.setText(obat.getJenisObat().getDisplayName());
        holder.tvStock.setText("Stock: " + obat.getStock());

        if (obat.getGambar() != null && !obat.getGambar().isEmpty()) {
            try {
                Uri imageUri = Uri.parse(obat.getGambar());
                holder.imgObat.setImageURI(imageUri);
            } catch (Exception e) {
                holder.imgObat.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } else {
            holder.imgObat.setImageResource(R.drawable.ic_launcher_foreground);
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
        obatFullList.clear();
        obatFullList.addAll(newList);
        filterObat("");
    }

    public void filterObat(String keyword) {
        obatList.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
            obatList.addAll(obatFullList);
        } else {
            String lower = keyword.toLowerCase();
            for (Obat o : obatFullList) {
                if (o.getNamaObat().toLowerCase().contains(lower)) {
                    obatList.add(o);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ObatViewHolder extends RecyclerView.ViewHolder {
        ImageView imgObat;
        TextView tvNamaObat, tvJenisObat, tvStock;

        public ObatViewHolder(@NonNull View itemView) {
            super(itemView);
            imgObat      = itemView.findViewById(R.id.imgObat);
            tvNamaObat   = itemView.findViewById(R.id.tvNamaObat);
            tvJenisObat  = itemView.findViewById(R.id.tvJenisObat);
            tvStock      = itemView.findViewById(R.id.tvStock);
        }
    }
}
