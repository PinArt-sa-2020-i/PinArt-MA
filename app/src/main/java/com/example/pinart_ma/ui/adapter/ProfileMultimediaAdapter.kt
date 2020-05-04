package com.example.pinart_ma.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.ui.ViewImageActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_feed.view.*

class ProfileMultimediaAdapter(var multimediasProfile: ArrayList<Multimedia>, var mine: Boolean)
    : RecyclerView.Adapter<ProfileMultimediaAdapter.MultimediaProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultimediaProfileViewHolder {
        var layoutInFlate = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_feed, parent, false)
        return MultimediaProfileViewHolder(layoutInFlate)
    }

    override fun getItemCount(): Int {
        return multimediasProfile.size
    }

    override fun onBindViewHolder(holder: MultimediaProfileViewHolder, position: Int) {
        var itemMultimediaProfile = multimediasProfile[position]
        holder.bindMultimediaProfile(itemMultimediaProfile)
    }


    class MultimediaProfileViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){
        fun  bindMultimediaProfile(multimediaProfile: Multimedia ){
            itemView.setOnClickListener{
                val intent = Intent(itemView.context, ViewImageActivity::class.java)
                intent.putExtra("idMultimedia", multimediaProfile.id)
                itemView.context.startActivity(intent)
            }

            itemView.textViewListFeedItem.text = multimediaProfile.descripcion
            Picasso.get().load(multimediaProfile.url).into(itemView.imageViewListFeedItem);
            itemView.imageViewListFeedItem.clipToOutline = true;
        }
    }
}