package com.santrikoding.newsapp.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.santrikoding.newsapp.R
import com.santrikoding.newsapp.data.local.entity.ArticleEntity
import com.santrikoding.newsapp.databinding.ItemArticleBinding
import com.santrikoding.newsapp.utils.withDateFormat

class BookmarkAdapter(
    private val onClick: (article: ArticleEntity) -> Unit,
    private val onBookmarkClick: (article: ArticleEntity) -> Unit
) :
    ListAdapter<ArticleEntity, BookmarkAdapter.BookmarkViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder =
        BookmarkViewHolder(
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: BookmarkAdapter.BookmarkViewHolder, position: Int) {
        val articles = getItem(position)
        holder.bind(articles)

        val ivBookmark = holder.binding.ivBookmark
        articles?.let { article ->
            if (article.isBookmark) {
                ivBookmark.setImageDrawable(
                    ContextCompat.getDrawable(
                        ivBookmark.context,
                        R.drawable.ic_bookmarked
                    )
                )
            } else {
                ivBookmark.setImageDrawable(
                    ContextCompat.getDrawable(
                        ivBookmark.context,
                        R.drawable.ic_bookmark
                    )
                )
            }
            ivBookmark.setOnClickListener {
                onBookmarkClick(article)
            }
        }
    }

    inner class BookmarkViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticleEntity) {
            item.let {
                with(binding) {
                    imageArticle.load(it.image) {
                        crossfade(true)
                        crossfade(400)
                        error(R.drawable.ic_broken_image)
                        transformations(RoundedCornersTransformation(25f))
                    }
                    tvTitle.text = it.title
                    tvCategory.text = it.category
                    tvAuthorDate.text = it.author + " - " + it.date.withDateFormat()
                    itemView.setOnClickListener {
                        onClick(item)
                    }
                }
            }
        }

    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ArticleEntity>() {
            override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ArticleEntity,
                newItem: ArticleEntity
            ): Boolean = oldItem == newItem

        }
    }
}