package com.example.pinart_ma.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Tag
import com.example.pinart_ma.ui.MainActivity
import kotlinx.android.synthetic.main.list_item_search_tag.view.*
import kotlin.collections.ArrayList

class SearchTagsAdapter(var tagsList : ArrayList<Tag>) : RecyclerView.Adapter<SearchTagsAdapter.TagViewHolder>(){

    var tagsFilterList = ArrayList<Tag>()

    init {
        tagsFilterList = tagsList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val tagListView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_search_tag, parent, false)
        return TagViewHolder(tagListView)
    }


    override fun getItemCount(): Int {
        return tagsFilterList.size
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag =tagsFilterList[position]
        holder.bindTag(tag)
    }


    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindTag(tag: Tag){
            itemView.setOnClickListener {
                /*
                val transaction = fragmentActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, SearchInitFragment.newInstance())
                transaction.commit()
                 */

                /*
                val intent = Intent(itemView.context, PostActivity::class.java)
                intent.putExtra(Constans.FOTO, muro1.foto)
                intent.putExtra(Constans.POST, muro1.post)
                itemView.context.startActivity(intent)
                * */

                val intent = Intent(itemView.context, MainActivity::class.java)
                intent.putExtra("typeFragment", "tagFragment")
                intent.putExtra("idTag", tag.id)
                itemView.context.startActivity(intent)

            }
            itemView.nameTextViewListSearchTags.text = tag.name
            itemView.descriptionTextViewListSearchTags.text = tag.description
        }

    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch.isEmpty()){
                    tagsFilterList=tagsList
                }
                else{
                    val resultList = ArrayList<Tag>()
                    for(row in tagsList){
                        if(row.name.toLowerCase().contains(charSearch.toLowerCase())){
                            resultList.add(row)
                        }
                    }
                    tagsFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = tagsFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                tagsFilterList = results?.values as ArrayList<Tag>
                notifyDataSetChanged()
            }
        }
    }



}