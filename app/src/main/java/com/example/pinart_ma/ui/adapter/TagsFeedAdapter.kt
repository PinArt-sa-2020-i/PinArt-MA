package com.example.pinart_ma.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Multimedia
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_feed.view.*

class TagsFeedAdapter(var  feedTags: ArrayList<Multimedia>): RecyclerView.Adapter<TagsFeedAdapter.TagFeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagFeedViewHolder {
        var layoutInFlate = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_feed, parent, false)
        return TagFeedViewHolder(layoutInFlate)
    }

    override fun getItemCount(): Int {
        return  feedTags.size
    }

    override fun onBindViewHolder(holder: TagFeedViewHolder, position: Int) {
        var itemTagFeed = feedTags[position]
        holder.bindTagFeed(itemTagFeed)
    }

    class TagFeedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bindTagFeed(feedTag: Multimedia){
            itemView.textViewListFeedItem.text = feedTag.descripcion
            Picasso.get().load(feedTag.url).into(itemView.imageViewListFeedItem);
            itemView.imageViewListFeedItem.clipToOutline = true;
        }
    }
}
