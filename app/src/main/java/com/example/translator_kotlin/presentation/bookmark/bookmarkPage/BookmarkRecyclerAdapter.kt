package com.example.translator_kotlin.presentation.bookmark.bookmarkPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.translator_kotlin.R
import com.example.translator_kotlin.domain.model.Translate
import java.util.*


class BookmarkRecyclerAdapter(
    private val context: Context,
    internal var bookmarkListener: BookmarkListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<Translate> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ItemBookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemBookmarkViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    internal inner class ItemBookmarkViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var translate: Translate

        fun bind(translate: Translate) {
            this.translate = translate

            val textSource = itemView.findViewById<TextView>(R.id.textSource)
            val textTarget = itemView.findViewById<TextView>(R.id.textTarget)
            val imageFavorite = itemView.findViewById<ImageView>(R.id.imageFavorite)
            val textViewLanguageSourceCode =
                itemView.findViewById<TextView>(R.id.textViewLanguageSourceCode)
            val textViewLanguageTargetCode =
                itemView.findViewById<TextView>(R.id.textViewLanguageTargetCode)
            val linearLayoutShowItem =
                itemView.findViewById<LinearLayout>(R.id.linearLayoutShowItem)

            textSource.text = translate.textSource
            textTarget.text = translate.textTarget

            val retValue =
                if (translate.savedInFavorites) R.drawable.ic_favorite_light else R.drawable.ic_favorite_dark
            imageFavorite.background = ContextCompat.getDrawable(context, retValue)

            textViewLanguageSourceCode.text = translate.languageSource.code.uppercase(Locale.getDefault())
            textViewLanguageTargetCode.text = translate.languageTarget.code.uppercase(Locale.getDefault())

            imageFavorite.setOnClickListener {
                if (translate.savedInFavorites)
                    bookmarkListener.removeFromFavorites(translate)
                else
                    bookmarkListener.saveAsFavorite(translate)
            }

            linearLayoutShowItem.setOnClickListener {
                bookmarkListener.openTranslate(translate)
            }

        }
    }

}
