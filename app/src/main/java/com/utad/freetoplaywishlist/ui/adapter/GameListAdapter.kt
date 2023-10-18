package com.utad.freetoplaywishlist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.utad.freetoplaywishlist.R
import com.utad.freetoplaywishlist.databinding.ItemGameBinding
import com.utad.freetoplaywishlist.network.model.GameResponse

class GameListAdapter(val navigateToDetail: (id: Int) -> Unit) :
    ListAdapter<GameResponse, GameListAdapter.GameViewHolder>(GameItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGameBinding.inflate(inflater, parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = getItem(position)

        with(holder) {
            binding.root.setOnClickListener { navigateToDetail(item.id) }
            binding.tvGameGenre.text = item.genre
            binding.tvTitle.text = item.title
            binding.tvShortDescription.text = item.shortDescription

            Glide.with(binding.ivGame)
                .load(item.thumbnail)
                .placeholder(R.drawable.bg_placeholder)
                .into(binding.ivGame)
        }
    }

    inner class GameViewHolder(val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root)
}

object GameItemCallback : DiffUtil.ItemCallback<GameResponse>() {
    override fun areItemsTheSame(oldItem: GameResponse, newItem: GameResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GameResponse, newItem: GameResponse): Boolean {
        return oldItem.thumbnail == newItem.thumbnail
    }

}