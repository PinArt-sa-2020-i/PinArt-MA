package com.example.pinart_ma.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.User
import com.example.pinart_ma.ui.MainActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.list_item_search_user.view.*

class SearchUsersAdapter(var userList: ArrayList<User>) : RecyclerView.Adapter<SearchUsersAdapter.UserViewHolder>(){

    var usersFilterList = ArrayList<User>()

    init{
        usersFilterList = userList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val userListView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_search_user, parent, false)
        return UserViewHolder(userListView)
    }

    override fun getItemCount(): Int {
        return usersFilterList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = usersFilterList[position]
        holder.bindUser(user)
    }

    class UserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindUser(user: User){
            itemView.usernameTextViewListSearchItemUser.text = user.username
            itemView.nameTextViewListSearchItemUser.text = (user.firstname + " " + user.lastname)


            if(user.foto == null){
                Picasso.get().load("https://uwosh.edu/deanofstudents/wp-content/uploads/sites/156/2019/02/profile-default.jpg").into(itemView.imageProfielSearch)}
            else{
                Picasso.get().load(user.foto).into(itemView.imageProfielSearch) }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MainActivity::class.java)
                intent.putExtra("typeFragment", "otherProfileFragment")
                intent.putExtra("idUsuario", user.id.toString())
                itemView.context.startActivity(intent)
            }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                if(charSearch.isEmpty()){
                    usersFilterList=userList

                }
                else{
                    val resultList = ArrayList<User>()
                    for(row in userList){
                        if(row.username.toLowerCase().contains(charSearch.toLowerCase())){
                            resultList.add(row)
                        }
                    }
                    usersFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = usersFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                usersFilterList = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }
}