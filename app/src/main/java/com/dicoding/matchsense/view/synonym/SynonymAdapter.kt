package com.dicoding.matchsense.view.synonym

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.matchsense.databinding.SynonymCardBinding

class SynonymAdapter(private val listSynonym: ArrayList<String>): RecyclerView.Adapter<SynonymAdapter.ViewHolder>() {

    class ViewHolder(private val binding: SynonymCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bind(synonym: String) {
            binding.synonymTv.text = synonym
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SynonymCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listSynonym.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val synonym = listSynonym[position]
        holder.bind(synonym)
    }

    fun clear() {
        if (listSynonym.isNotEmpty()) {
            listSynonym.clear()
        }
    }
}