package com.example.pinart_ma.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.ui.ViewImageActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_feed.view.*


class TagsFeedAdapter(var  feedTags: ArrayList<Multimedia>): RecyclerView.Adapter<TagsFeedAdapter.TagFeedViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagFeedViewHolder {
        var layoutInFlate = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_feed, parent, false)
        context = parent.context
        return TagFeedViewHolder(layoutInFlate)
    }

    override fun getItemCount(): Int {
        return  feedTags.size
    }

    override fun onBindViewHolder(holder: TagFeedViewHolder, position: Int) {
        var itemTagFeed = feedTags[position]
        holder.bindTagFeed(itemTagFeed, context)
    }

    class TagFeedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bindTagFeed(feedTag: Multimedia, context: Context){

            itemView.setOnClickListener{
                val intent = Intent(itemView.context, ViewImageActivity::class.java)
                intent.putExtra("idMultimedia", feedTag.id)
                itemView.context.startActivity(intent)
            }
            itemView.textViewListFeedItem.text = feedTag.descripcion

            if(feedTag.url.substring(feedTag.url.length-3, feedTag.url.length) == "gif"){
                val urlGif = feedTag.url
                val uri: Uri = Uri.parse(urlGif)
                Glide.with(context).load(uri).fitCenter().into(itemView.imageViewListFeedItem)
                itemView.imageViewListFeedItem.clipToOutline = true;
            }
            else{
                Picasso.get().load(feedTag.url).into(itemView.imageViewListFeedItem);
                itemView.imageViewListFeedItem.clipToOutline = true;
            }
        }
    }
}
