package com.example.drone.view.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.drone.databinding.ItemSettingsListingBinding


class SettingsAdapter(private val mList: List<String>) : RecyclerView.Adapter<SettingsAdapter.MainViewHolder>() {

    private lateinit var binding: ItemSettingsListingBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        binding = ItemSettingsListingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val items = mList[position]
        holder.textView.text = items
    }

    class MainViewHolder(
        binding: ItemSettingsListingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        val textView: TextView = binding.tvSettings
    }
}



