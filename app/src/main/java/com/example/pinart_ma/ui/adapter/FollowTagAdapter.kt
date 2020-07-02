package com.example.pinart_ma.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.ui.MainActivity
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.TagViewModel
import com.example.pinart_ma.viewModel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_follow.view.*

class FollowTagAdapter(var idTags: ArrayList<String>, var fragment: Fragment) :
    RecyclerView.Adapter<FollowTagAdapter.TagViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        var layoutInFlate = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_follow, parent, false)
        return TagViewHolder(layoutInFlate)
    }

    override fun getItemCount(): Int {
        return idTags.size
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        var idTag = idTags[position]
        holder.bindTag(idTag)
    }



    inner class TagViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindTag(idTag: String){

            var followTag = true

            val myPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)
            val token = myPreferences.getString("token", "unknown")
            val id = myPreferences.getString("id", "unknown")

            var tagFactory = InjectorUtils.providerTagViewModelFactory()
            var tagViewModel = ViewModelProviders.of(fragment, tagFactory).get(TagViewModel::class.java);

            //Cargar Etiqueta
            tagViewModel!!.getTagById(token, idTag).observe(fragment, Observer {
                tagResult ->

                Picasso.get().load("https://image.flaticon.com/icons/png/512/94/94699.png").into(itemView.imageViewItemFollow)
                itemView.userItemFollow.text = tagResult.name
                itemView.buttonItemView.text = "Dejar de seguir"
                itemView.buttonItemView.setBackgroundResource(R.drawable.rounded_search)
                itemView.buttonItemView.setTextColor(Color.parseColor("#000000"))
            })

            //Mostar Etiqueta
            itemView.nameItemFollow.setOnClickListener{
                val intent = Intent(itemView.context, MainActivity::class.java)
                intent.putExtra("typeFragment", "tagFragment")
                intent.putExtra("idTag", idTag)
                itemView.context.startActivity(intent)
            }
            itemView.imageViewItemFollow.setOnClickListener {
                val intent = Intent(itemView.context, MainActivity::class.java)
                intent.putExtra("typeFragment", "tagFragment")
                intent.putExtra("idTag", idTag)
                itemView.context.startActivity(intent)
            }



            //Seguir / No seguir
            itemView.buttonItemView.setOnClickListener{
                if(followTag == false){
                    tagViewModel!!.followTag(token, id, idTag).observe(fragment, Observer {
                        result ->
                        followTag = true
                        itemView.buttonItemView.text = "Dejar de seguir"
                        itemView.buttonItemView.setBackgroundResource(R.drawable.rounded_search)
                        itemView.buttonItemView.setTextColor(Color.parseColor("#000000"))
                    })
                }
                else{
                    tagViewModel!!.unFollowTag(token, id, idTag).observe(fragment, Observer {
                        result ->
                        followTag = false
                        itemView.buttonItemView.text = "Seguir"
                        itemView.buttonItemView.setBackgroundResource(R.drawable.rounded_followbutton)
                        itemView.buttonItemView.setTextColor(Color.parseColor("#FFFFFF"))
                    })
                }
            }
        }
    }

}