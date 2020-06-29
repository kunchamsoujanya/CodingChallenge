package com.example.codingchallenge.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codingchallenge.R
import com.example.codingchallenge.response.Card
import com.example.codingchallenge.response.Cards


class CardsAdapter(private val cards:List<Cards>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TITLE_VIEW_TYPE = 0
        private const val IMAGE_VIEW_TYPE = 1
        private const val TEXT_VIEW_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TEXT_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.text_item, parent, false)
                TextViewHolder(view)
            }
            IMAGE_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.image_description_item_view, parent, false)
                ImageViewHolder(view)
            }
            TITLE_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.title_description_item_view, parent, false)
                TitleViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is TextViewHolder -> holder.bind(cards[position].card)
            is ImageViewHolder -> holder.bind(cards[position].card)
            is TitleViewHolder -> holder.bind(cards[position].card)
        }

    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(cards[position].card_type) {
            "text" -> TEXT_VIEW_TYPE
            "title_description" -> TITLE_VIEW_TYPE
            "image_title_description" -> IMAGE_VIEW_TYPE
            else -> -1
        }
    }

}

class TextViewHolder(private val view:View): RecyclerView.ViewHolder(view) {

    var title: TextView? = null

    init {
        title = view.findViewById(R.id.text)
    }

    fun bind(card: Card) {
        title?.text = card.value
        title?.setTextColor(Color.parseColor(card.attributes.text_color))
        title?.textSize = card.attributes.font.size

    }
}

class TitleViewHolder(private val view:View): RecyclerView.ViewHolder(view) {

    var title: TextView? = null
    var description: TextView? = null

    init {
        title = view.findViewById(R.id.title)
        description = view.findViewById(R.id.description)
    }

    fun bind(card: Card) {
        title?.text = card.title.value
        title?.setTextColor(Color.parseColor(card.title.attributes.text_color))
        title?.textSize = card.title.attributes.font.size

        description?.text = card.description.value
        description?.setTextColor(Color.parseColor(card.description.attributes.text_color))
        description?.textSize = card.description.attributes.font.size

    }
}

class ImageViewHolder(private val view:View): RecyclerView.ViewHolder(view) {

    var title: TextView? = null
    var description: TextView? = null
    var image: ImageView? = null

    init {
        title = view.findViewById(R.id.title)
        description = view.findViewById(R.id.status)
        image = view.findViewById(R.id.image)
    }

    fun bind(card: Card) {
        title?.text = card.title.value
        title?.setTextColor(Color.parseColor(card.title.attributes.text_color))
        title?.textSize = card.title.attributes.font.size

        description?.text = card.description.value
        description?.setTextColor(Color.parseColor(card.description.attributes.text_color))
        description?.textSize = card.description.attributes.font.size
        image?.let {
            val layoutParams = FrameLayout.LayoutParams(card.image.size.width, card.image.size.height)
            it.layoutParams = layoutParams
            Glide.with(itemView).load(card.image.url).into(it)
        }

    }
}




