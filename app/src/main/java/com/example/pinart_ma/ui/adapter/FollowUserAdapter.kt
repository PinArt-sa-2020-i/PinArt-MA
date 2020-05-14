package com.example.pinart_ma.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ReportFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.User
import com.example.pinart_ma.ui.MainActivity
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_configuration.*
import kotlinx.android.synthetic.main.list_item_follow.view.*

class FollowUserAdapter(var follows: ArrayList<ArrayList<String>>, var typeFollows: String, var fragment: Fragment)
    :RecyclerView.Adapter<FollowUserAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        var layoutInFlate = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_follow, parent, false)
        return UserViewHolder(layoutInFlate)
    }

    override fun getItemCount(): Int {
        return follows.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if(typeFollows == "followers"){
            var follow = follows[position]
            holder.bindUserFollower(follow[0], follow[1], position)
        }
        else{
            var follow = follows[position]
            holder.bindUserFollowing(follow[0], follow[2])
        }
    }


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindUserFollower(idFollow: String, idUserFollower: String, position: Int){
            val myPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)
            val token = myPreferences.getString("token", "unknown")

            var userFactory = InjectorUtils.provideUserViewModelFactory()
            var userViewModel = ViewModelProviders.of(fragment, userFactory).get(UserViewModel::class.java);

            //Carga el usuario
            userViewModel!!.getUserById(token, idUserFollower).observe(fragment, Observer {
                userResult ->

                if(userResult.foto == null){Picasso.get().load("https://uwosh.edu/deanofstudents/wp-content/uploads/sites/156/2019/02/profile-default.jpg").into(itemView.imageViewItemFollow);}
                else{Picasso.get().load(userResult.foto).into(itemView.imageViewItemFollow)}

                itemView.nameItemFollow.text = userResult.username
                itemView.buttonItemView.text = "Eliminar"
            })

            //Ver
            itemView.nameItemFollow.setOnClickListener {
                val intent = Intent(itemView.context, MainActivity::class.java)
                intent.putExtra("typeFragment", "otherProfileFragment")
                intent.putExtra("idUsuario", idUserFollower.toString())
                itemView.context.startActivity(intent)
            }

            itemView.imageViewItemFollow.setOnClickListener {
                val intent = Intent(itemView.context, MainActivity::class.java)
                intent.putExtra("typeFragment", "otherProfileFragment")
                intent.putExtra("idUsuario", idUserFollower.toString())
                itemView.context.startActivity(intent)
            }



            //Dejar de Seguir
            itemView.buttonItemView.setOnClickListener {
                userViewModel!!.deleteUserFollow(token, idFollow).observe(fragment, Observer {
                    result ->
                    follows.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(adapterPosition, follows.size);
                })
            }
        }

        fun bindUserFollowing(idFollow: String, idUserFollowing: String){
            var idFollowAux = idFollow

            val myPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)
            val token = myPreferences.getString("token", "unknown")
            val id = myPreferences.getString("id", "unknown")

            var userFactory = InjectorUtils.provideUserViewModelFactory()
            var userViewModel = ViewModelProviders.of(fragment, userFactory).get(UserViewModel::class.java);

            //Carga el usuario
            userViewModel!!.getUserById(token, idUserFollowing).observe(fragment, Observer {
                userResult ->
                if(userResult.foto == null){Picasso.get().load("https://uwosh.edu/deanofstudents/wp-content/uploads/sites/156/2019/02/profile-default.jpg").into(itemView.imageViewItemFollow);}
                else{Picasso.get().load(userResult.foto).into(itemView.imageViewItemFollow)}

                itemView.nameItemFollow.text = userResult.username
                itemView.buttonItemView.text = "Siguiendo"
                itemView.buttonItemView.setBackgroundColor(Color.GREEN)
            })

            //Ver usuario
            itemView.nameItemFollow.setOnClickListener {
                val intent = Intent(itemView.context, MainActivity::class.java)
                intent.putExtra("typeFragment", "otherProfileFragment")
                intent.putExtra("idUsuario", idUserFollowing.toString())
                itemView.context.startActivity(intent)
            }

            itemView.imageViewItemFollow.setOnClickListener {
                val intent = Intent(itemView.context, MainActivity::class.java)
                intent.putExtra("typeFragment", "otherProfileFragment")
                intent.putExtra("idUsuario", idUserFollowing.toString())
                itemView.context.startActivity(intent)
            }


            //Seguir / Dejar de Seguir
            itemView.buttonItemView.setOnClickListener{
                if(idFollowAux == "0"){
                    userViewModel!!.createUserFollow(token, id, idUserFollowing).observe(fragment, Observer {
                        result ->
                        itemView.buttonItemView.text = "Siguiendo"
                        itemView.buttonItemView.setBackgroundColor(Color.GREEN)
                        idFollowAux = result.toString()

                    })
                }
                else{
                    userViewModel!!.deleteUserFollow(token, idFollowAux).observe(fragment, Observer {
                        result ->
                        itemView.buttonItemView.text = "Seguir"
                        itemView.buttonItemView.setBackgroundColor(Color.BLUE)
                        idFollowAux = "0"
                    })
                }
            }
        }

    }
}