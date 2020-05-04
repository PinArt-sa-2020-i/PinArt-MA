package com.example.pinart_ma.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Tag
import com.example.pinart_ma.ui.MainActivity
import kotlinx.android.synthetic.main.list_item_tag_view_image.view.*

class ViewImageTagsAdapter(var tags: ArrayList<Tag>) : RecyclerView.Adapter<ViewImageTagsAdapter.TagViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
         return TagViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_tag_view_image, parent, false))
    }

    override fun getItemCount(): Int {
        return  tags.size
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bindTag(tags[position])
    }

    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindTag(tag: Tag){
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MainActivity::class.java)
                intent.putExtra("typeFragment", "tagFragment")
                intent.putExtra("idTag", tag.id)
                itemView.context.startActivity(intent)
            }
            itemView.nameTagItemTagView.text = tag.name
        }

    }




}