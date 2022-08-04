package com.garibyan.armen.tbc_task_14.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.garibyan.armen.tbc_task_14.databinding.RvNewsItemBinding
import com.garibyan.armen.tbc_task_14.network.News

class NewsAdapter : ListAdapter<News, NewsAdapter.NewsViewHolder>(NewsCallBack()) {

    inner class NewsViewHolder(private val binding: RvNewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: News) = with(binding) {
            itemImage.load(news.cover)
            tvTitle.text = news.titleKA
            tvDescription.text = news.descriptionKA
            tvPublishDate.text = news.publishDate
        }
    }

    class NewsCallBack : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: News, newItem: News) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsViewHolder(
        RvNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}