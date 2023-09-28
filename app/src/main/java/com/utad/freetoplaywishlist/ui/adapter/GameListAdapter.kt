package com.utad.freetoplaywishlist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utad.freetoplaywishlist.databinding.ItemGameBinding
import com.utad.freetoplaywishlist.network.model.GameResponse

class GameListAdapter :
    ListAdapter<GameResponse, GameListAdapter.GameViewHolder>(GameItemCallback) {


    inner class GameViewHolder(val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGameBinding.inflate(inflater, parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = getItem(position)
        //TODO hacer diseño del item
    }
}

object GameItemCallback : DiffUtil.ItemCallback<GameResponse>() {
    override fun areItemsTheSame(oldItem: GameResponse, newItem: GameResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GameResponse, newItem: GameResponse): Boolean {
        return oldItem.thumbnail == newItem.thumbnail
    }

}