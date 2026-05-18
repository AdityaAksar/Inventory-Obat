package com.example.inventoryobat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ObatAdapter(
    private val context: Context,
    obatList: List<Obat>
) : RecyclerView.Adapter<ObatAdapter.ObatViewHolder>() {

    private val obatList = obatList.toMutableList()
    private val obatFullList = obatList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_obat, parent, false)
        return ObatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObatViewHolder, position: Int) {
        val obat = obatList[position]

        holder.tvNamaObat.text = obat.namaObat
        holder.tvJenisObat.text = "Jenis: ${obat.jenisObat}"
        holder.tvStock.text = "Stock: ${obat.stock}"

        if (obat.gambarUrl.isNotEmpty()) {
            Glide.with(context)
                .load(obat.gambarUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgObat)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, InfoProdukActivity::class.java)
            intent.putExtra("obat_id", obat.idObat)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = obatList.size

    fun updateData(newList: List<Obat>) {
        obatList.clear()
        obatList.addAll(newList)
        obatFullList.clear()
        obatFullList.addAll(newList)
        notifyDataSetChanged()
    }

    fun filterObat(keyword: String) {
        obatList.clear()
        if (keyword.isEmpty()) {
            obatList.addAll(obatFullList)
        } else {
            obatFullList.filterTo(obatList) {
                it.namaObat.lowercase().contains(keyword.lowercase())
            }
        }
        notifyDataSetChanged()
    }

    class ObatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgObat: ImageView = itemView.findViewById(R.id.imgObat)
        val tvNamaObat: TextView = itemView.findViewById(R.id.tvNamaObat)
        val tvJenisObat: TextView = itemView.findViewById(R.id.tvJenisObat)
        val tvStock: TextView = itemView.findViewById(R.id.tvStock)
    }
}
