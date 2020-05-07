package com.example.pinart_ma.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.ui.ViewImageActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_feed.view.*
import java.util.ArrayList

class UserFeedAdapter(var feedUsers:  ArrayList<Multimedia>) : RecyclerView.Adapter<UserFeedAdapter.UserFeedViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFeedViewHolder {
        var layoutInFlate = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_feed, parent, false)
        context = parent.context
        return UserFeedViewHolder(layoutInFlate)
    }

    override fun getItemCount(): Int {
        return feedUsers.size
    }

    override fun onBindViewHolder(holder: UserFeedViewHolder, position: Int) {
        var itemUserFeed = feedUsers[position]
        holder.bindUserFeed(itemUserFeed, context)
    }


    class UserFeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindUserFeed(feedUser: Multimedia, context: Context){

            itemView.setOnClickListener{
                val intent = Intent(itemView.context, ViewImageActivity::class.java)
                intent.putExtra("idMultimedia", feedUser.id)
                itemView.context.startActivity(intent)
            }

            if(feedUser.url.substring(feedUser.url.length-3, feedUser.url.length) == "gif"){
                val urlGif = feedUser.url
                val uri: Uri = Uri.parse(urlGif)
                Glide.with(context).load(uri).fitCenter().into(itemView.imageViewListFeedItem)
                itemView.imageViewListFeedItem.clipToOutline = true;
            }
            else{
                Picasso.get().load(feedUser.url).into(itemView.imageViewListFeedItem);
                itemView.imageViewListFeedItem.clipToOutline = true;
            }


        }

    }
}