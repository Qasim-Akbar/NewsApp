package com.aml.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aml.newsapp.databinding.ItemArticlePreviewBinding
import com.aml.newsapp.models.Article
import com.bumptech.glide.Glide

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>(){

    inner class ArticleViewHolder(
        val itemBinding: ItemArticlePreviewBinding
    ):RecyclerView.ViewHolder(itemBinding.root){
        fun bind(position: Int) {
            val article = differ.currentList[position]
            Glide.with(itemBinding.root).load(article.urlToImage).into(itemBinding.ivArticleImage)
            itemBinding.tvSource.text = article.source?.name
            itemBinding.tvTitle.text = article.title
            itemBinding.tvDescription.text = article.description
            itemBinding.tvPublishedAt.text = article.publishedAt
            itemBinding.root.setOnClickListener{
                onItemClickListener?.let { it(article) }
            }
        }
    }

    private  val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private  var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit){
        onItemClickListener = listener
    }
}