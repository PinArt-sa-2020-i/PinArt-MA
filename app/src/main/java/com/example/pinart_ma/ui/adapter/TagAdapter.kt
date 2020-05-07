package com.example.pinart_ma.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.service.model.Tag
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_feed.view.*

class TagAdapter(var multimediasTag : ArrayList<Multimedia>)  : RecyclerView.Adapter<TagAdapter.MultimediaTagViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultimediaTagViewHolder {
        var layoutInFlate = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_feed, parent, false)
        return MultimediaTagViewHolder(layoutInFlate)
    }

    override fun getItemCount(): Int {
        return multimediasTag.size
    }

    override fun onBindViewHolder(holder: MultimediaTagViewHolder, position: Int) {
        var itemMultimediaTag = multimediasTag[position]
        holder.bindTag(itemMultimediaTag)
    }


    class MultimediaTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindTag(multimediaTag: Multimedia){
            itemView.textViewListFeedItem.text = multimediaTag.descripcion

            Picasso.get().load(multimediaTag.url).into(itemView.imageViewListFeedItem);
            itemView.imageViewListFeedItem.clipToOutline = true;
        }
    }
}